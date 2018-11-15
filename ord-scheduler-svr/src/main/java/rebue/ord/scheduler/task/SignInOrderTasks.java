package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rebue.ord.svr.feign.OrdTaskSvc;
import rebue.robotech.dic.TaskExecuteStateDic;

/**
 * 订单签收任务
 */
@Component
public class SignInOrderTasks {
    private final static Logger _log = LoggerFactory.getLogger(SignInOrderTasks.class);

    @Resource
    private OrdTaskSvc          taskSvc;

    @Scheduled(fixedDelayString = "${ord.scheduler.tradeFixedDelay}")
    public void executeTasks() {
        _log.info("定时执行需要自动签收订单的任务");
        try {
            _log.info("获取所有需要执行订单签收的任务列表");
            final List<Long> tasks = taskSvc.getByExecutePlanTimeBeforeNow((byte) TaskExecuteStateDic.NONE.getCode(), (byte) 2);
            _log.info("获取到的订单签收任务为：{}", String.valueOf(tasks));
            for (final Long id : tasks) {
                try {
                    taskSvc.executeSignInOrderTask(id);
                } catch (final RuntimeException e) {
                    _log.info("执行订单签收失败", e);
                }
            }
        } catch (final RuntimeException e) {
            _log.info("获取需要执行自动签收的订单任务时出现异常", e);
        }
    }
}
