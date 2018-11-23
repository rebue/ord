package rebue.ord.svc;

import rebue.robotech.ro.Ro;
import rebue.robotech.svc.MybatisBaseSvc;

import java.util.List;

import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.to.CancelSettleTaskTo;
import rebue.ord.to.ResumeSettleTaskTo;
import rebue.ord.to.SuspendSettleTaskTo;

/**
 * 结算任务
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdSettleTaskSvc extends MybatisBaseSvc<OrdSettleTaskMo, java.lang.Long> {

	/**
	 * 暂停结算任务
	 * 
	 * @param to
	 * @return
	 */
	Ro suspendSettleTask(SuspendSettleTaskTo to);

	/**
	 * 恢复结算任务
	 * 
	 * @param to
	 * @return
	 */
	Ro resumeSettleTask(ResumeSettleTaskTo to);

	/**
	 * 取消结算任务
	 * 
	 * @param to
	 * @return
	 */
	Ro cancelSettleTask(CancelSettleTaskTo to);

	/**
	 * 获取结算任务列表
	 * 
	 * @return
	 */
	List<Long> getTaskIdsThatShouldExecute();

	/**
	 * 添加结算任务（根据结算类型添加）
	 * @param mo
	 * @return
	 */
	Ro addSettleTask(OrdSettleTaskMo mo);

	/**
	 * 执行结算任务
	 * @param id
	 * @return
	 */
	Ro executeSettleTask(Long id);

}