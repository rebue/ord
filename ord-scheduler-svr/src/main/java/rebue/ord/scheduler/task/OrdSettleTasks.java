package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import rebue.ord.svr.feign.OrdSettleTaskSvc;

public class OrdSettleTasks {

    private final static Logger _log = LoggerFactory.getLogger(CancleOrderTasks.class);

    @Resource
    private OrdSettleTaskSvc    settleTaskSvc;

    // settleFixedDelay:结算任务执行的间隔（毫秒）
    @Scheduled(fixedDelayString = "${ord.scheduler.settleFixedDelay:608400000}")
    public void executeTasks() {
        _log.info("定时执行需要自动取消订单的任务");
        try {
            _log.info("获取所有需要执行取消订单的任务列表");
            final List<Long> tasks = settleTaskSvc.getTaskIdsThatShouldExecute();
            _log.info("获取到所有需要执行取消订单任务的列表为：{}", String.valueOf(tasks));
            for (final Long id : tasks) {
                try {
                    settleTaskSvc.executeSettleTask(id);
                } catch (final RuntimeException e) {
                    _log.info("执行订单取消失败", e);
                }
            }
        } catch (final RuntimeException e) {
            _log.info("获取需要执行自动取消任务时出现异常", e);
        }
    }
}
