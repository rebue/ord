package rebue.ord.pub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import rebue.afc.sgjz.msg.SgjzPayDoneMsg;
import rebue.ord.co.OrdExchangeCo;
import rebue.ord.sub.PayDoneSub;
import rebue.sbs.rabbit.RabbitProducer;

@Service
public class SgjzDonePub implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger _log = LoggerFactory.getLogger(PayDoneSub.class);

    /**
     * 启动标志，防止多次启动
     */
    private boolean bStartedFlag = false;

    @Resource
    private RabbitProducer producer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        // 防止多次启动
        if (bStartedFlag)
            return;
        bStartedFlag = true;        // 声明Exchange

        try {
            producer.declareExchange(OrdExchangeCo.PAY_DONE_EXCHANGE_NAME);
        } catch (Exception e) {
            String msg = "添加手工记账完成消息的Exchange失败";
            _log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

    }

    /**
     * 发送消息
     */
    public void send(SgjzPayDoneMsg msg) {
        producer.send(OrdExchangeCo.PAY_DONE_EXCHANGE_NAME, msg);
    }

}
