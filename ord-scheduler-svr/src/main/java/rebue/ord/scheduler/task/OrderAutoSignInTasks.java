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
 * 订单自动签收任务
 */
@Component
public class OrderAutoSignInTasks {
    private final static Logger _log = LoggerFactory.getLogger(OrderAutoSignInTasks.class);

    @Resource
    private OrdTaskSvc          taskSvc;

    // signInFixedDelay:订单自动签收任务执行的间隔（毫秒）,默认5分钟检查一次
    @Scheduled(fixedDelayString = "${ord.scheduler.signInFixedDelay:300000}")
    public void executeTasks() {
        _log.info("定时执行需要订单自动签收的任务");
        try {
            _log.info("获取所有需要执行订单签收的任务列表");
            final List<Long> taskIds = taskSvc.getTaskIdsThatShouldExecute(TaskExecuteStateDic.NONE, OrderTaskTypeDic.SIGNED);
            _log.info("获取到的订单自动签收的任务为：{}", taskIds);
            for (final Long taskId : taskIds) {
                try {
                    taskSvc.executeSignInOrderTask(taskId);
                } catch (final RuntimeException e) {
                    _log.error("执行订单自动签收的任务失败", e);
                }
            }
        } catch (final RuntimeException e) {
            _log.info("获取需要执行订单自动签收的任务时出现异常", e);
        }
    }
}
