package rebue.ord.sub;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import rebue.afc.co.PayNotifyCo;
import rebue.afc.ro.PayNotifyRo;
import rebue.afc.svc.AfcPaySvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.sbs.rabbit.RabbitConsumer;

/**
 * 订阅支付完成的通知修改订单状态
 */
@Service
public class PayNotifySub implements ApplicationListener<ContextRefreshedEvent> {
    private final static Logger _log = LoggerFactory.getLogger(PayNotifySub.class);

    private static AtomicInteger count      = new AtomicInteger();

    @Resource
    private AfcPaySvc           paySvc;

    @Resource
    private RabbitConsumer      consumer;

    @Resource
    private Mapper              dozerMapper;
    
    @Resource
    private OrdOrderSvc           ordOrderSvc;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 防止里面的代码被运行两次
    	  if (!(event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext))
              return;
          if (count.incrementAndGet() > 1)
              return;

        _log.info("订阅支付完成的通知");
        consumer.bind(PayNotifyCo.PAY_NOTIFY_EXCHANGE_NAME, PayNotifyCo.PAY_NOTIFY_QUEUE_NAME, PayNotifyRo.class, (ro) -> {
            _log.info("收到支付完成的通知: {}", ro);
            PayNotifyRo msg = dozerMapper.map(ro, PayNotifyRo.class);
            return handlePayNotify(msg);
        });
    }

    /**
     * 处理支付完成的通知
     */
    private boolean handlePayNotify(PayNotifyRo msg) {
        try {
        	long orderCode = Long.parseLong(msg.getOrderId());
        	byte orderState = (byte)2;
        	int result = ordOrderSvc.modifyOrderStateByOderCode(orderCode,orderState);
            if(result>0) {
            	return true;
            }else {
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
