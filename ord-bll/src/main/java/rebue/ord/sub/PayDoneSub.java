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
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdGoodsBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;
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

	@Resource
	private OrdGoodsBuyRelationSvc ordGoodsBuyRelationSvc;

	/**
	 */
	@Resource
	private SucUserSvc sucUserSvc;

	/**
	 */
	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

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
						long userId = detailMoResult.get(i).getUserId();
						long onlineId = detailMoResult.get(i).getOnlineId();
						BigDecimal buyPrice = detailMoResult.get(i).getBuyPrice();
						long downLineDetailId = detailMoResult.get(i).getId();
						long downLineOrderId = detailMoResult.get(i).getOrderId();
						String matchBuyRelationResult = ordBuyRelationSvc.matchBuyRelation(userId, onlineId, buyPrice,
								downLineDetailId, downLineOrderId);
						_log.info(matchBuyRelationResult);
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
}
