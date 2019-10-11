package rebue.ord.sub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.afc.sgjz.co.SgjzExchangeCo;
import rebue.afc.sgjz.msg.SgjzPayDoneMsg;
import rebue.sbs.rabbit.RabbitConsumer;

@Service
public class SgjzDoneSub implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger _log = LoggerFactory.getLogger(PayDoneSub.class);

    private final static String SGJZ_DONE_QUEUE_NAME = "rebue.ord.pay.done.queue";

    @Resource
    private RabbitConsumer consumer;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        // 防止里面的代码被运行两次
        if (!(event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext)) {
            return;
        }
        _log.info("手工记账完成的通知");
        consumer.bind(SgjzExchangeCo.PAY_DONE_EXCHANGE_NAME, SGJZ_DONE_QUEUE_NAME, SgjzPayDoneMsg.class, (msg) -> {
            _log.info("收到手工记账完成的通知: {}", msg);
            if (!sgjz(msg)) {
                return false;
            }
            return true;
        });
    }

    /**
     * 处理手工记账完成的通知
     */
    private boolean sgjz(final SgjzPayDoneMsg msg) {
        try {
            _log.info("处理手工记账完成参数为：{}", msg);
            // 订单支付
            return true;
        } catch (final DuplicateKeyException e) {
            _log.warn("收到重复的消息: " + msg, e);
            return true;
        } catch (final Exception e) {
            _log.error("处理手工记账完成通知出现异常", e);
            return false;
        }
    }

}
