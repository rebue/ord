package rebue.ord.sub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import rebue.afc.co.AfcExchangeCo;
import rebue.afc.msg.SettleDoneMsg;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;

/**
 * 创建时间：2018年5月17日 下午2:59:44 项目名称：ord-bll
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：SettleNotifySub.java 类说明： 订阅结算完成通知-修改订单状态
 */
@Service
public class SettleDoneSub implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger _log = LoggerFactory.getLogger(SettleDoneSub.class);

	/**
	 * 处理添加用户完成通知的队列
	 */
	private final static String SETTLE_DONE_QUEUE_NAME = "rebue.ord.afc.settle.done.queue";

	@Resource
	private RabbitConsumer consumer;

	@Resource
	private OrdOrderSvc ordOrderSvc;

	/**
	 * 启动标志，防止多次启动
	 */
	private boolean bStartedFlag = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 防止多次启动
		if (bStartedFlag)
			return;
		bStartedFlag = true;

		_log.info("结算完成通知的队列为: {} - {}", AfcExchangeCo.SETTLE_DONE_EXCHANGE_NAME, SETTLE_DONE_QUEUE_NAME);

		consumer.bind(AfcExchangeCo.SETTLE_DONE_EXCHANGE_NAME, SETTLE_DONE_QUEUE_NAME,
				SettleDoneMsg.class, (msg) -> {
					try {
						_log.info("接收到结算完成通知参数为: {}", msg);
						// 修改订单状态和添加结算完成时间
						int finishSettlementResult = ordOrderSvc.finishSettlement(msg.getSettleTime(),
								msg.getOrderId());
						_log.info("结算完成通知修改订单状态的返回值为：{}", finishSettlementResult);
						if (finishSettlementResult != 1) {
							_log.error("结算完成通知修改订单状态时出现错误，参数为：{}", msg);
							return true;
						}
						_log.info("结算完成通知修改订单状态成功返回，订单编号为：{}", msg.getOrderId());
						return true;
					} catch (DuplicateKeyException e) {
						_log.warn("收到重复的消息: " + msg, e);
						return true;
					} catch (Exception e) {
						_log.error("添加账户出现异常", e);
						return false;
					}
				});
	}
}
