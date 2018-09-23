package rebue.ord.sub;

import javax.annotation.Resource;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import rebue.afc.vpay.co.VpayExchangeCo;
import rebue.afc.vpay.msg.VpayPayDoneMsg;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;

/**
 * 订阅V支付完成的通知修改支付返现金的金额及支付余额的金额
 */
@Service
public class VpayPayDoneSub implements ApplicationListener<ContextRefreshedEvent> {
	private final static Logger _log = LoggerFactory.getLogger(VpayPayDoneSub.class);

	/**
	 * 处理添加用户完成通知的队列
	 */
	private final static String PAY_DONE_QUEUE_NAME = "rebue.ord.vpay.pay.done.queue";

	@Resource
	private RabbitConsumer consumer;
	@Resource
	private Mapper dozerMapper;
	@Resource
	private OrdOrderSvc ordOrderSvc;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 防止里面的代码被运行两次
		if (!(event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext))
			return;

		_log.info("订阅微信支付的支付完成的通知");
		consumer.bind(VpayExchangeCo.PAY_DONE_EXCHANGE_NAME, PAY_DONE_QUEUE_NAME, VpayPayDoneMsg.class, (msg) -> {
			_log.info("V支付-收到支付完成的通知: {}", msg);
			return handlePayNotify(msg);
		});
	}

	/**
	 * 处理支付完成的通知
	 */
	private boolean handlePayNotify(VpayPayDoneMsg msg) {
		try {
			OrdOrderMo mo = new OrdOrderMo();
			mo.setId(Long.parseLong(msg.getOrderId()));
			mo.setReturnAmount1(msg.getPayChangeAmount1());
			mo.setReturnAmount2(msg.getPayChangeAmount2());
			int result = ordOrderSvc.modify(mo);
			if (result > 0) {
				_log.info("更新订单可返金额成功！");
				return true;
			} else if (result == 0) {
				_log.info("订单不存在");
				return true;
			} else {
				_log.info("更新订单可返金额失败！");
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
