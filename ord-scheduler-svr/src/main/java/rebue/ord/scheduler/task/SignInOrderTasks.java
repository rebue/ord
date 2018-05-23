package rebue.ord.scheduler.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rebue.ord.dic.TaskExecuteStateDic;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svr.feign.OrdTaskSvc;

/**
 * 创建时间：2018年5月21日 下午12:11:51 项目名称：ord-scheduler-svr
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：SignInTasks.java 类说明： 订单签收任务
 */
@Component
public class SignInOrderTasks {

	private final static Logger _log = LoggerFactory.getLogger(SignInOrderTasks.class);

	@Resource
	OrdTaskSvc taskSvc;

	@Scheduled(fixedDelayString = "${ord.scheduler.tradeFixedDelay}")
	public void executeTasks() {
		_log.info("定时执行需要自动签收订单的任务");
		try {

			// 获取所有需要执行订单签收的任务
			List<OrdTaskMo> list = taskSvc.list((byte) TaskExecuteStateDic.NOT_EXECUTE.getCode(), (byte) 2);
			_log.info("获取到的订单签收任务为：{}", String.valueOf(list));
			for (OrdTaskMo taskMo : list) {
				try {
					taskSvc.executeSignInOrderTask(taskMo.getId());
                } catch (RuntimeException e) {
                	_log.info("执行订单签收失败，返回值为：{}", e);
                }
			}
		} catch (RuntimeException e) {
			_log.info("获取需要执行自动签收的订单任务时出现异常", e);
		}
	}
}
