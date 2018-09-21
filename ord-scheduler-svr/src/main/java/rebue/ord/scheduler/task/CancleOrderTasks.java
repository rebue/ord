package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rebue.ord.svr.feign.OrdTaskSvc;

/**
 * 取消订单任务
 */
@Component
public class CancleOrderTasks {
    private final static Logger _log = LoggerFactory.getLogger(CancleOrderTasks.class);

    @Resource
    private OrdTaskSvc          taskSvc;

    @Scheduled(fixedDelayString = "${ord.scheduler.cancelTradeFixedDelay}")
    public void executeTasks() {
        _log.info("定时执行需要自动取消订单的任务");
        try {
            _log.info("获取所有需要执行取消订单的任务列表");
            List<Long> tasks = taskSvc.getByExecutePlanTimeBeforeNow((byte) 0, (byte) 1);
            _log.info("获取到所有需要执行取消订单任务的列表为：{}", String.valueOf(tasks));
            for (Long id : tasks) {
                try {
                    taskSvc.executeCancelOrderTask(id);
                } catch (RuntimeException e) {
                    _log.info("执行订单取消失败", e);
                }
            }
        } catch (RuntimeException e) {
            _log.info("获取需要执行自动取消任务时出现异常", e);
        }
    }
}
