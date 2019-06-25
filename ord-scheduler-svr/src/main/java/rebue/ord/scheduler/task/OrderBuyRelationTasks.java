package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.svr.feign.OrdTaskSvc;
import rebue.robotech.dic.TaskExecuteStateDic;


@Component
public class OrderBuyRelationTasks {
	

    private final static Logger _log = LoggerFactory.getLogger(OrderSettleTasks.class);

    @Resource
    private OrdTaskSvc          taskSvc;

    // settleFixedDelay:结算任务执行的间隔(毫秒)，默认5分钟检查一次
    @Scheduled(fixedDelayString = "${ord.scheduler.buyRelation:300000}")
    public void executeTasks() {
        _log.info("定时执行需要订单匹配关系的的任务");
        try {
        	
        } catch (final RuntimeException e) {
            _log.info("获取需要执行订单匹配关系任务时出现异常", e);
        }
    }

}
