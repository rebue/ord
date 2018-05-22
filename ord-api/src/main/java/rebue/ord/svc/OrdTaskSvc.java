package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;

import rebue.ord.mo.OrdTaskMo;

public interface OrdTaskSvc extends MybatisBaseSvc<OrdTaskMo, java.lang.Long>{

	/**
	 * 执行订单签收任务
	 * Title: executeSignInOrderTask
	 * Description: 
	 * @param executeFactTime
	 * @param id
	 * @param doneState
	 * @param noneState
	 * @return
	 * @date 2018年5月21日 下午3:30:46
	 */
	void executeSignInOrderTask(long id);
	
	//执行取消订单任务
	void executeCancelOrderTask(long id);
}