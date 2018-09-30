package rebue.ord.sub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import rebue.afc.co.AfcExchangeCo;
import rebue.afc.msg.CommissionSettleDoneMsg;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;

/**
 * 创建时间：2018年5月17日 下午2:59:44 项目名称：ord-bll
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：SettleNotifySub.java 类说明： 订阅返佣结算完成通知-修改上线订单详情状态
 */
@Service
public class CommissionSettleDoneSub implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger _log = LoggerFactory.getLogger(CommissionSettleDoneSub.class);

	/**
	 * 处理添加用户完成通知的队列
	 */
	private final static String COMMISSION_SETTLE_DONE_QUEUE_NAME = "rebue.ord.afc.commission.settle.done.queue";

	@Resource
	private RabbitConsumer consumer;

	@Resource
	private OrdOrderSvc ordOrderSvc;
	
	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

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

		_log.info("结算完成通知的队列为: {} - {}", AfcExchangeCo.COMMISSION_SETTLE_DONE_EXCHANGE_NAME, COMMISSION_SETTLE_DONE_QUEUE_NAME);

		consumer.bind(AfcExchangeCo.COMMISSION_SETTLE_DONE_EXCHANGE_NAME, COMMISSION_SETTLE_DONE_QUEUE_NAME,
				CommissionSettleDoneMsg.class, (msg) -> {
					try {
						_log.info("接收到返佣结算完成通知参数为: {}", msg);
						// 根据结算订单详情ID获取购买关系
						OrdBuyRelationMo buyRelationMo = new OrdBuyRelationMo();
						buyRelationMo.setDownlineOrderDetailId(Long.parseLong(msg.getOrderDetailId()));
						OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(buyRelationMo);
						if(buyRelationResult ==null) {
							_log.warn("查找到的购买关系为空 ");
							return true;
						}else {
							_log.info("根据购买关系更新上线订单详情返佣状态 ");
							OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
							orderDetailMo.setId(buyRelationResult.getUplineOrderDetailId());
							orderDetailMo.setCommissionState((byte) 2);
							int updateDetailResult = ordBuyRelationSvc.modify(buyRelationMo);
							if(updateDetailResult != 1) {
								_log.error("结算完成通知修改上线订单返佣状态时出现错误，参数为：{}", msg);
								return true;
							}
							_log.info("结算完成通知修改上线订单详情状态成功返回，订单详情ID为：{}", msg.getOrderDetailId());
							return true;
						}
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
