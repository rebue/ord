package rebue.ord.svc;

import rebue.ord.jo.OrdSettleTaskJo;
import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.robotech.svc.BaseSvc;

/**
 * 结算任务
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdSettleTaskSvc extends BaseSvc<java.lang.Long, OrdSettleTaskMo, OrdSettleTaskJo> {

    /**
     * 添加启动结算订单的任务(根据订单ID添加)
     */
    void addStartSettleTask(Long orderId);

    /**
     * 添加结算任务(启动结算任务执行时添加)
     */
    void addSettleTasks(Long orderId);

    /**
     * 执行结算任务
     */
    void executeSettleTask(OrdTaskMo taskMo);

    /**
     * 执行订单结算完成的任务(根据订单ID)
     */
    void executeCompleteSettleTask(Long orderId);
}
