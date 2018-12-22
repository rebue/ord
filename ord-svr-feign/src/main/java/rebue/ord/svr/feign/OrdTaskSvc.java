package rebue.ord.svr.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.ord.dic.OrderTaskTypeDic;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.sbs.feign.FeignConfig;

/**
 * 创建时间：2018年5月21日 下午3:21:05 项目名称：ord-svr-feign
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdTaskSvc.java 类说明： 订单任务内部接口
 */
@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdTaskSvc {

    /**
     * 获取订单任务ID列表(根据订单任务状态和任务类型)
     */
    @GetMapping(value = "/ord/tasks")
    List<Long> getTaskIdsThatShouldExecute(@RequestParam("executeState") TaskExecuteStateDic executeState, @RequestParam("taskType") OrderTaskTypeDic taskType);

    /**
     * 执行订单自动签收的任务
     */
    @PostMapping("/ord/task/signin")
    void executeSignInOrderTask(@RequestParam("taskId") Long taskId);

    /**
     * 执行订单自动取消的任务
     */
    @PostMapping("/ord/task/cancleOrder")
    void executeCancelOrderTask(@RequestParam("taskId") Long taskId);

    /**
     * 执行订单启动结算的任务
     */
    @PostMapping("/ord/task/executeStartSettle")
    void executeStartSettleTask(@RequestParam("taskId") Long taskId);

    /**
     * 执行订单结算的任务
     */
    @PostMapping("/ord/task/executeSettle")
    void executeSettleTask(@RequestParam("taskId") final Long taskId);

    /**
     * 执行订单结算完成的任务
     */
    @PostMapping("/ord/task/executeCompleteSettle")
    void executeCompleteSettleTask(@RequestParam("taskId") final Long taskId);

    /**
     * 执行计算首单购买的任务
     */
    @PostMapping("/ord/task/executeCalcFirstBuy")
    void executeCalcFirstBuy(@RequestParam("taskId") Long taskId);

}
