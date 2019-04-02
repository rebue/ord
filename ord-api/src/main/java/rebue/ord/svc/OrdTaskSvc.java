package rebue.ord.svc;

import java.util.List;



import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.jo.OrdTaskJo;
import rebue.ord.mo.OrdTaskMo;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.BaseSvc;

/**
 * 订单任务
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdTaskSvc extends BaseSvc<java.lang.Long, OrdTaskMo, OrdTaskJo> {

	/**
	 * 添加任务，如果重复添加不抛异常，事务不会回滚
	 */
	int addEx(OrdTaskMo mo);

	/**
	 * } 根据订单任务状态和任务类型获取订单任务ID列表
	 */
	List<Long> getTaskIdsThatShouldExecute(TaskExecuteStateDic executeState, OrderTaskTypeDic taskType);

	/**
	 * 判断订单是否存在仍未完成的任务（包括未执行的和暂停执行的）
	 */
	Boolean existUnfinished(String orderId);

	/**
	 * 执行订单自动签收的任务
	 *
	 * @param taskId
	 *            任务ID
	 */
	void executeSignInOrderTask(Long taskId);

	/**
	 * 执行订单自动取消的任务
	 *
	 * @param taskId
	 *            任务ID
	 */
	void executeCancelOrderTask(Long taskId);

	/**
	 * 执行订单启动结算的任务
	 */
	void executeStartSettleTask(Long taskId);

	/**
	 * 执行订单结算的任务
	 */
	void executeSettleTask(Long taskId);

	/**
	 * 执行取消订单任务
	 */
	Ro cancelTask(Long orderId, OrderTaskTypeDic taskType);

	/**
	 * 执行订单完成结算的任务
	 */
	void executeCompleteSettleTask(Long taskId);

	/**
	 * 执行计算首单购买的任务
	 */
	void executeCalcFirstBuy(Long taskId);
}
