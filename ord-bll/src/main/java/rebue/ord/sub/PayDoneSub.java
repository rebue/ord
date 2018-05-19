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

import rebue.afc.co.AfcExchangeCo;
import rebue.afc.msg.PayDoneMsg;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;

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

		_log.info("订阅支付完成的通知");
		consumer.bind(AfcExchangeCo.PAY_DONE_EXCHANGE_NAME, PAY_DONE_QUEUE_NAME, PayDoneMsg.class, (msg) -> {
			_log.info("收到支付完成的通知: {}", msg);
			return handlePayNotify(msg);
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
