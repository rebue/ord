package rebue.ord.sub;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.afc.co.AfcExchangeCo;
import rebue.afc.msg.PayDoneMsg;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;
import rebue.suc.ro.SucRegRo;
import rebue.suc.svr.feign.SucUserSvc;
import rebue.wheel.idworker.IdWorker3;

/**
 * 订阅支付完成的通知修改订单状态
 */
@Service
public class PayDoneSub implements ApplicationListener<ContextRefreshedEvent> {
	private final static Logger _log = LoggerFactory.getLogger(PayDoneSub.class);

	/**
	 * 处理V支付完成通知的队列
	 */
	private final static String PAY_DONE_QUEUE_NAME = "rebue.ord.pay.done.queue";

	@Value("${appid:0}")
	private int _appid;

	protected IdWorker3 _idWorker;

	@PostConstruct
	public void init() {
		_idWorker = new IdWorker3(_appid);
	}

	@Resource
	private RabbitConsumer consumer;

	@Resource
	private Mapper dozerMapper;

	@Resource
	private OrdOrderSvc ordOrderSvc;

	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	/**
	 */
	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

	/**
	 */
	@Resource
	private SucUserSvc sucUserSvc;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 防止里面的代码被运行两次
		if (!(event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext))
			return;
		_log.info("订阅支付完成的通知");
		consumer.bind(AfcExchangeCo.PAY_DONE_EXCHANGE_NAME, PAY_DONE_QUEUE_NAME, PayDoneMsg.class, (msg) -> {
			_log.info("收到支付完成的通知: {}", msg);
			if (!handlePayNotify(msg))
				return false;
			_log.info("添加订单购买关系");
			OrdOrderMo orderMo = ordOrderSvc.getById(Long.parseLong(msg.getOrderId()));
			_log.info("获取用户订单详情信息");
			OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
			detailMo.setOrderId(Long.parseLong(msg.getOrderId()));
			OrdOrderDetailMo detailMoResult = ordOrderDetailSvc.getOne(detailMo);
			_log.info("获取到的定单详情为：{}" + detailMoResult);
			long id = Long.parseLong(msg.getOrderId());
			long onlineId = detailMoResult.getOnlineId();
			_log.info("获取用户购买关系上家");
			boolean getBuyRelationResult = getAndUpdateBuyRelation(id, onlineId, detailMoResult.getBuyPrice(),
					detailMoResult.getId(), detailMoResult.getOrderId());
			_log.info(msg.getOrderId() + "获取用户购买关系的返回值为：{}" + getBuyRelationResult);
			if (getBuyRelationResult == false) {
				_log.info("获取用户注册关系上家");
				boolean getRegRelationResult = getAndUpdateRegRelation(id, onlineId, detailMoResult.getBuyPrice(),
						detailMoResult.getId(), detailMoResult.getOrderId());
				_log.info(msg.getOrderId() + "获取用户注册关系的返回值为：{}" + getRegRelationResult);
				if (getRegRelationResult == false) {
					_log.info("获取其它关系上家");
					boolean getOtherRelationResult = getAndUpdateOtherRelation(id, onlineId,
							detailMoResult.getBuyPrice(), detailMoResult.getId(), detailMoResult.getOrderId());
					_log.info(msg.getOrderId() + "获取其它关系的返回值为：{}" + getOtherRelationResult);
				}
			}
			return true;
		});
	}

	/**
	 * 处理支付完成的通知
	 */
	private boolean handlePayNotify(PayDoneMsg msg) {
		try {
			_log.info("v支付订单支付完成通知修改订单信息的参数为：", msg);
			// 订单支付
			int result = ordOrderSvc.orderPay(msg.getOrderId(), msg.getPayTime());
			_log.info("v支付订单支付完成通知修改订单信息的返回值为：{}", result);
			if (result > 0) {
				_log.info("修改订单状态成功！");
				return true;
			} else if (result == 0) {
				_log.info("订单不存在！");
				return true;
			} else {
				_log.info("修改订单状态失败！");
				return false;
			}

		} catch (DuplicateKeyException e) {
			_log.warn("收到重复的消息: " + msg, e);
			return true;
		} catch (Exception e) {
			_log.error("处理支付完成通知出现异常", e);
			return false;
		}
	}

	/**
	 * 获取购买关系并更新购买关系表
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelation(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("获取用户购买关系的id:" + id + "onlineId:" + onlineId + "buyPricce:" + buyPrice);
		Long buyRelation = sucUserSvc.getBuyRelation(id, onlineId);
		_log.info("获取用户购买关系返回值为：" + buyRelation);
		if (buyRelation == null || buyRelation == id) {
			return false;
		}
		// 根据产品上线ID查找购买关系用户的购买记录，看是否有符合要求的订单详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setUserId(buyRelation);
		mo.setReturnState((byte) 0);
		_log.info("获取用户上线购买关系订单详情的参数为：{}" + mo);
		OrdOrderDetailMo orderDetailMo = ordOrderDetailSvc.getFullReturnDetail(mo);
		_log.info("获取用户上线购买关系订单详情的返回值为：{}" + orderDetailMo);
		if (orderDetailMo == null) {
			return false;
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailMo.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailMo.getId());
		if ((orderDetailMo.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		int updateOrderDetailResult = ordOrderDetailSvc.modify(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailMo.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailMo.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailMo.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		return true;
	}

	/**
	 * 获取注册关系并更新购买关系表
	 *
	 * @param id
	 * @param onlineId
	 * @param buyPrice
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateRegRelation(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		_log.info("获取用户信息参数：{}" + id);
		SucRegRo sucReg = sucUserSvc.getRegInfo(id);
		_log.info("获取用户信息返回值为：" + sucReg);
		if (sucReg.getResult() != 1) {
			return false;
		}
		// 根据产品上线ID查找注册关系用户的购买记录，看是否有符合要求的订单详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setUserId(sucReg.getRecord().getId());
		mo.setReturnState((byte) 0);
		_log.info("获取用户注册关系订单详情的参数：{}" + mo);
		OrdOrderDetailMo orderDetailMo = ordOrderDetailSvc.getFullReturnDetail(mo);
		_log.info("获取用户注册关系订单详情的返回值为：{}" + orderDetailMo);
		if (orderDetailMo == null) {
			return false;
		}
		// 更新注册关系上线订单详情的返现名额
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailMo.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailMo.getId());
		if ((orderDetailMo.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新注册关系上线订单详情的返现名额
		int updateOrderDetailResult = ordOrderDetailSvc.modify(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返现名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailMo.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailMo.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailMo.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		return true;
	}

	/**
	 * 获取除购买关系和注册关系外的一个购买上家,并更新购买关系表
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateOtherRelation(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 根据产品上线ID和价格查找订单详情记录，看是否有符合要求的订单详情
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setId(downLineDetailId);
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setReturnState((byte) 0);
		OrdOrderDetailMo orderDetailMo = ordOrderDetailSvc.getOtherFullReturnDetail(mo);
		_log.info("获取上家订单详情的返回值为：{}" + orderDetailMo);
		if (orderDetailMo == null) {
			return false;
		}
		// 更新购买关系订单详情的返现名额
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailMo.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailMo.getId());
		if ((orderDetailMo.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返现名额
		int updateOrderDetailResult = ordOrderDetailSvc.modify(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返现名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailMo.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailMo.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailMo.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		return true;
	}

}
