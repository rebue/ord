package rebue.ord.sub;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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

import com.github.dozermapper.core.Mapper;

import rebue.afc.co.AfcExchangeCo;
import rebue.afc.msg.PayDoneMsg;
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
    private final static Logger _log                = LoggerFactory.getLogger(PayDoneSub.class);

    /**
     * 处理支付完成通知的队列
     */
    private final static String PAY_DONE_QUEUE_NAME = "rebue.ord.pay.done.queue";

    @Value("${appid:0}")
    private int                 _appid;

    protected IdWorker3         _idWorker;

    @PostConstruct
    public void init() {
        _idWorker = new IdWorker3(_appid);
    }

    @Resource
    private RabbitConsumer         consumer;

    @Resource
    private Mapper                 dozerMapper;

    @Resource
    private OrdOrderSvc            ordOrderSvc;

    @Resource
    private OrdOrderDetailSvc      ordOrderDetailSvc;

    @Resource
    private OrdGoodsBuyRelationSvc ordGoodsBuyRelationSvc;

    /**
     */
    @Resource
    private SucUserSvc             sucUserSvc;

    /**
     */
    @Resource
    private OrdBuyRelationSvc      ordBuyRelationSvc;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        // 防止里面的代码被运行两次
        if (!(event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext)) {
            return;
        }
        _log.info("订阅支付完成的通知");
        consumer.bind(AfcExchangeCo.PAY_DONE_EXCHANGE_NAME, PAY_DONE_QUEUE_NAME, PayDoneMsg.class, (msg) -> {
            _log.info("收到支付完成的通知: {}", msg);
            if (!handlePayNotify(msg)) {
                return false;
            }
            return true;
        });
    }

    /**
     * 处理支付完成的通知
     */
    private boolean handlePayNotify(final PayDoneMsg msg) {
        try {
            _log.info("订单支付完成通知修改订单信息的参数为：{}", msg);
            // 订单支付
            return ordOrderSvc.handleOrderPaidNotify(msg);
        } catch (final DuplicateKeyException e) {
            _log.warn("收到重复的消息: " + msg, e);
            return true;
        } catch (final Exception e) {
            _log.error("处理支付完成通知出现异常", e);
            return false;
        }
    }
}
