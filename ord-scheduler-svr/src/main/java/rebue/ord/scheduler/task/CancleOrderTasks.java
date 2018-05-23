package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svr.feign.OrdOrderSvc;
import rebue.ord.svr.feign.OrdTaskSvc;

/**
 * 创建时间：2018年5月22日 项目名称：ord-scheduler-svr
 * 
 * @author jjl
 * @version 1.0
 * @since JDK 1.8 文件名称：SignInTasks.java 类说明： 取消订单任务
 */
@Component
public class CancleOrderTasks {

	private final static Logger _log = LoggerFactory.getLogger(CancleOrderTasks.class);

	@Resource
	OrdOrderSvc orderSvc;

	@Resource
	OrdTaskSvc taskSvc;

	public void executeTasks() {
		_log.info("定时执行需要自动取消订单的任务");
		try {
			OrdTaskMo ordTaskMo = new OrdTaskMo();
			ordTaskMo.setExecuteState((byte) 0);
			ordTaskMo.setTaskType((byte) 1);
			// 获取所有需要执行取消订单的任务
			List<OrdTaskMo> list = taskSvc.list((byte)0,(byte)1);
			for (OrdTaskMo taskMo : list) {
				try {
					taskSvc.executeCancelOrderTask(taskMo.getId());
                } catch (RuntimeException e) {
                	_log.info("执行订单取消失败，返回值为：{}", e);
                }
			}
		} catch (RuntimeException e) {
			_log.info("获取需要执行自动取消任务时出现异常", e);
		}
	}
}
