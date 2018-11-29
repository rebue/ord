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

/**
 * 订单启动结算的任务
 */
@Component
public class OrderStartSettleTasks {

    private final static Logger _log = LoggerFactory.getLogger(OrderAutoCancelTasks.class);

    @Resource
    private OrdTaskSvc          taskSvc;

    // startSettleFixedDelay:结算启动任务执行的间隔(毫秒)，默认5分钟检查一次
    @Scheduled(fixedDelayString = "${ord.scheduler.startSettleFixedDelay:300000}")
    public void executeTasks() {
        _log.info("定时执行需要订单启动结算的任务");
        try {
            _log.info("获取所有需要执行订单启动结算的任务列表");
            final List<Long> taskIds = taskSvc.getTaskIdsThatShouldExecute(TaskExecuteStateDic.NONE, OrderTaskTypeDic.START_SETTLE);
            _log.info("获取到所有需要执行订单启动结算的任务的列表为：{}", taskIds);
            for (final Long taskId : taskIds) {
                try {
                    taskSvc.executeStartSettleTask(taskId);
                } catch (final RuntimeException e) {
                    _log.error("执行订单启动结算的任务失败", e);
                }
            }
        } catch (final RuntimeException e) {
            _log.info("获取需要执行订单启动结算任务时出现异常", e);
        }
    }
}
