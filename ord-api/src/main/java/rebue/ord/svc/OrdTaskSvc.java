package rebue.ord.svc;

import java.util.List;
import rebue.ord.mo.OrdTaskMo;
import rebue.robotech.svc.MybatisBaseSvc;

public interface OrdTaskSvc extends MybatisBaseSvc<OrdTaskMo, java.lang.Long> {

    /**
     *  执行订单签收任务
     *  Title: executeSignInOrderTask
     *  Description:
     *  @param executeFactTime
     *  @param id
     *  @param doneState
     *  @param noneState
     *  @return
     *  @date 2018年5月21日 下午3:30:46
     */
    void executeSignInOrderTask(long id);

    // 执行取消订单任务
    void executeCancelOrderTask(long id);

    /**
     *  根据订单任务状态和任务类型查询订单任务数量
     *  Title: getByExecutePlanTimeBeforeNow
     *  Description:
     *  @param executeState
     *  @param taskType
     *  @return
     *  @date 2018年5月28日 上午10:51:47
     */
    List<Long> getByExecutePlanTimeBeforeNow(byte executeState, byte taskType);
}
