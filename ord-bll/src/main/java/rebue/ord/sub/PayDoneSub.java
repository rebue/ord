package rebue.ord.sub;

import java.math.BigDecimal;
import java.util.List;

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
import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdGoodsBuyRelationSvc;
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

	@Resource
	private OrdGoodsBuyRelationSvc ordGoodsBuyRelationSvc;

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
			List<OrdOrderDetailMo> detailMoResult = ordOrderDetailSvc.list(detailMo);
			_log.info("获取到的定单详情为：{}" + detailMoResult);
			for (int i = 0; i < detailMoResult.size(); i++) {
				try {
					_log.info("订单详情商品类型为：{}" + detailMoResult.get(i).getSubjectType());
					if (detailMoResult.get(i).getSubjectType() == 1) {
						_log.info("全返商品添加购买关系");
						long id = detailMoResult.get(i).getUserId();
						long onlineId = detailMoResult.get(i).getOnlineId();
						_log.info("按匹配自己匹配购买关系");
						boolean getBuyRelationResultByOwn = getAndUpdateBuyRelationByOwn(id, onlineId,
								detailMoResult.get(i).getBuyPrice(), detailMoResult.get(i).getId(),
								detailMoResult.get(i).getOrderId());
						_log.info(detailMoResult.get(i).getId() + "按匹配自己匹配购买关系的返回值为：{}" + getBuyRelationResultByOwn);
						if (getBuyRelationResultByOwn == false) {
							_log.info("根据邀请规则匹配购买关系");
							boolean getRegRelationResultByPromote = getAndUpdateBuyRelationByPromote(id, onlineId,
									detailMoResult.get(i).getBuyPrice(), detailMoResult.get(i).getId(),
									detailMoResult.get(i).getOrderId());
							_log.info(detailMoResult.get(i).getId() + "根据邀请规则匹配购买关系的返回值为：{}"
									+ getRegRelationResultByPromote);
							if (getRegRelationResultByPromote == false) {
								_log.info("根据匹配差一人，且邀请一人（关系来源是购买关系的）规则匹配购买关系");
								boolean getOtherRelationResultByThree = getAndUpdateBuyRelationByThree(id, onlineId,
										detailMoResult.get(i).getBuyPrice(), detailMoResult.get(i).getId(),
										detailMoResult.get(i).getOrderId());
								_log.info(detailMoResult.get(i).getId() + "根据匹配差一人，且邀请一人（关系来源是购买关系的）规则匹配购买关系的返回值为：{}"
										+ getOtherRelationResultByThree);
								if (getOtherRelationResultByThree == false) {
									_log.info("根据匹配差两人的规则匹配购买关系");
									boolean getOtherRelationResultByFour = getAndUpdateBuyRelationByFour(id, onlineId,
											detailMoResult.get(i).getBuyPrice(), detailMoResult.get(i).getId(),
											detailMoResult.get(i).getOrderId());
									_log.info(detailMoResult.get(i).getId() + "根据匹配差两人的规则匹配购买关系的返回值为：{}"
											+ getOtherRelationResultByFour);
									if (getOtherRelationResultByFour == false) {
										_log.info("根据匹配差一人的规则匹配购买关系");
										boolean getOtherRelationResultByFive = getAndUpdateBuyRelationByFive(id,
												onlineId, detailMoResult.get(i).getBuyPrice(),
												detailMoResult.get(i).getId(), detailMoResult.get(i).getOrderId());
										_log.info(detailMoResult.get(i).getId() + "根据匹配差一人的规则匹配购买关系的返回值为：{}"
												+ getOtherRelationResultByFive);
										if (getOtherRelationResultByFive == false) {
											_log.info(detailMoResult.get(i).getId() + "匹配购买关系失败");
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					_log.error("匹配购买关系报错：", e);
					e.printStackTrace();
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
	 * 根据匹配自己规则匹配购买关系 1.查找用户购买同款产品中剩余1个购买名额的记录，如果已有购买关系下家不是自己，则添加购买关系记录;
	 * 2.如1结果为空，查用户购买同款产品中剩余两个购买名额的记录，并添加购买关系记录；
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByOwn(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("获取用户购买关系的id:" + id + "onlineId:" + onlineId + "buyPricce:" + buyPrice);
		// 获取用户购买该产品还有两个名额的详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setId(downLineDetailId);
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setUserId(id);
		mo.setReturnState((byte) 0);
		mo.setCommissionSlot((byte) 1);
		_log.info("获取用户自己购买剩余1个购买名额的订单详情的参数为：{}" + mo);
		OrdOrderDetailMo orderDetailOfOneCommissionSlot = ordOrderDetailSvc.getOrderDetailForBuyRelation(mo);
		_log.info("查找订单详情的购买关系记录");
		if (orderDetailOfOneCommissionSlot == null) {
			_log.info("获取用户购买过该产品且还有1个匹配名额的记录为空");
		} else {
			OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
			relationMo.setUplineOrderDetailId(orderDetailOfOneCommissionSlot.getId());
			List<OrdBuyRelationMo> relationResult = ordBuyRelationSvc.list(relationMo);
			_log.info("获取到的购买关系结果为:{}", relationResult);
			if (relationResult.size() != 0 && relationResult.get(0).getRelationSource() != null
					&& relationResult.get(0).getRelationSource() != 1) {
				// 添加购买关系记录
				_log.info("在购买关系表中添加记录");
				OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
				ordBuyRelationMo.setId(_idWorker.getId());
				ordBuyRelationMo.setUplineOrderId(orderDetailOfOneCommissionSlot.getOrderId());
				ordBuyRelationMo.setUplineUserId(orderDetailOfOneCommissionSlot.getUserId());
				ordBuyRelationMo.setUplineOrderDetailId(orderDetailOfOneCommissionSlot.getId());
				ordBuyRelationMo.setDownlineUserId(id);
				ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
				ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
				ordBuyRelationMo.setRelationSource((byte) 1);
				_log.error("添加购买关系参数:{}", ordBuyRelationMo);
				int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
				if (addBuyRelationResult != 1) {
					_log.error("{}添加下级购买信息失败", id);
					throw new RuntimeException("生成购买关系出错");
				}
				// 更新购买关系订单详情的返佣名额
				OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
				updateOrderDetailMo.setCommissionSlot((byte) 0);
				updateOrderDetailMo.setId(orderDetailOfOneCommissionSlot.getId());
				updateOrderDetailMo.setCommissionState((byte) 1);
				int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
				if (updateOrderDetailResult != 1) {
					_log.error("{}更新订单详情返佣名额失败", id);
					throw new RuntimeException("更新订单详情返现名额失败");
				}
				_log.info("根据匹配自己规则匹配差一人匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
				return true;
			}
		}
		mo.setCommissionSlot((byte) 2);
		_log.info("获取用户自己购买剩余2个购买名额的订单详情的参数为：{}" + mo);
		OrdOrderDetailMo orderDetailOfTwoCommissionSlot = ordOrderDetailSvc.getOrderDetailForBuyRelation(mo);
		if (orderDetailOfTwoCommissionSlot == null) {
			_log.info("获取用户购买过该产品且还有两个匹配名额的记录为空");
		} else {
			_log.info("获取用户购买过该产品且还有两个匹配名额的记录为：{}", orderDetailOfTwoCommissionSlot);
			// 添加购买关系记录
			_log.info("在购买关系表中添加记录");
			OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setId(_idWorker.getId());
			ordBuyRelationMo.setUplineOrderId(orderDetailOfTwoCommissionSlot.getOrderId());
			ordBuyRelationMo.setUplineUserId(orderDetailOfTwoCommissionSlot.getUserId());
			ordBuyRelationMo.setUplineOrderDetailId(orderDetailOfTwoCommissionSlot.getId());
			ordBuyRelationMo.setDownlineUserId(id);
			ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
			ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
			ordBuyRelationMo.setRelationSource((byte) 1);
			_log.error("添加购买关系参数:{}", ordBuyRelationMo);
			int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
			if (addBuyRelationResult != 1) {
				_log.error("{}添加下级购买信息失败", id);
				throw new RuntimeException("生成购买关系出错");
			}
			// 更新订单详情的返佣名额
			OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
			updateOrderDetailMo.setCommissionSlot((byte) 1);
			updateOrderDetailMo.setId(orderDetailOfTwoCommissionSlot.getId());
			int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
			if (updateOrderDetailResult != 1) {
				_log.error("{}更新订单详情返佣名额失败", id);
				throw new RuntimeException("更新订单详情返现名额失败");
			}
			_log.info("根据匹配自己规则匹配差两人匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
			return true;
		}
		return false;
	}

	/**
	 * 根据邀请规则匹配购买关系
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByPromote(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("获取用户购买关系的id:" + id + "onlineId:" + onlineId + "buyPricce:" + buyPrice);
		OrdGoodsBuyRelationMo goodsBuyRelationMo = new OrdGoodsBuyRelationMo();
		goodsBuyRelationMo.setDownlineUserId(id);
		goodsBuyRelationMo.setOnlineId(onlineId);
		OrdGoodsBuyRelationMo buyRelationResult = ordGoodsBuyRelationSvc.getBuyRelation(goodsBuyRelationMo);
		_log.info("获取用户购买关系返回值为：{}", buyRelationResult);
		if (buyRelationResult == null) {
			_log.info("获取到的购买关系为空");
			return false;
		}
		// 根据产品上线ID查找购买关系用户的购买记录，看是否有符合要求的订单详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setId(downLineDetailId);
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setUserId(buyRelationResult.getUplineUserId());
		mo.setReturnState((byte) 0);
		_log.info("获取用户上线购买关系订单详情的参数为：{}" + mo);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(mo);
		if (orderDetailResult == null) {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 2);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		_log.error("更新订单详情返佣名额失败", id);
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		_log.info("根据匹配邀请规则匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
		return true;
	}

	/**
	 * 根据匹配差一人，且邀请一人（关系来源是购买关系的）的订单详情
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByThree(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差一人，且邀请一人（关系来源是购买关系的）的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		// 获取用户购买该产品还有两个名额的详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setReturnState((byte) 0);
		mo.setCommissionSlot((byte) 1);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelationThree(mo);
		if (orderDetailResult == null) {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(orderDetailResult.getId());
		List<OrdBuyRelationMo> relationResult = ordBuyRelationSvc.list(relationMo);
		_log.info("获取到的购买关系结果为:{}", relationResult);
		if (relationResult.size() != 0 && relationResult.get(0).getRelationSource() != null
				&& relationResult.get(0).getRelationSource() == 2) {
			// 添加购买关系记录
			_log.info("在购买关系表中添加记录");
			OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setId(_idWorker.getId());
			ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
			ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
			ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
			ordBuyRelationMo.setDownlineUserId(id);
			ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
			ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
			ordBuyRelationMo.setRelationSource((byte) 3);
			_log.error("添加购买关系参数:{}", ordBuyRelationMo);
			int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
			if (addBuyRelationResult != 1) {
				_log.error("{}添加下级购买信息失败", id);
				throw new RuntimeException("生成购买关系出错");
			}
			OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
			updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
			updateOrderDetailMo.setId(orderDetailResult.getId());
			if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
				updateOrderDetailMo.setCommissionState((byte) 1);
			}
			// 更新购买关系订单详情的返佣名额
			int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
			if (updateOrderDetailResult != 1) {
				_log.error("{}更新订单详情返佣名额失败", id);
				throw new RuntimeException("更新订单详情返现名额失败");
			}
			_log.info("根据匹配差一人，且邀请一人规则匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
			return true;
		} else {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
	}

	/**
	 * 根据匹配差两人的规则匹配购买关系
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByFour(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差两人的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		// 获取用户购买该产品还有两个名额的详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setId(downLineDetailId);
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setReturnState((byte) 0);
		mo.setCommissionSlot((byte) 2);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(mo);
		if (orderDetailResult == null) {
			_log.info("没有符合差两人匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 4);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		return true;
	}

	/**
	 * 匹配差一人的订单详情
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByFive(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差一人的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		// 获取用户购买该产品还有两个名额的详情记录
		OrdOrderDetailMo mo = new OrdOrderDetailMo();
		mo.setId(downLineDetailId);
		mo.setOnlineId(onlineId);
		mo.setBuyPrice(buyPrice);
		mo.setReturnState((byte) 0);
		mo.setCommissionSlot((byte) 1);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(mo);
		if (orderDetailResult == null) {
			_log.info("没有符合差一人匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 5);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = ordBuyRelationSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		return true;
	}
}
