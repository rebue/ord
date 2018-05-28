package rebue.ord.ctrl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.svc.OrdTaskSvc;

/**
 * 创建时间：2018年5月21日 下午3:22:06 项目名称：ord-svr
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdTaskCtrl.java 类说明： 订单任务
 */
@RestController
public class OrdTaskCtrl {

	private final static Logger _log = LoggerFactory.getLogger(OrdTaskCtrl.class);

	@Resource
	private OrdTaskSvc ordTaskSvc;

	/**
	 * 查询订单任务数量
	 * Title: getByExecutePlanTimeBeforeNow
	 * Description: 
	 * @param executeState
	 * @param taskType
	 * @return
	 * @date 2018年5月28日 上午11:00:40
	 */
	@GetMapping(value = "/ord/task")
	List<Long> getByExecutePlanTimeBeforeNow(@RequestParam("executeState") byte executeState, @RequestParam("taskType") byte taskType) {
		_log.info("查询订单任务数量的参数为：{}， {}", executeState, taskType);
		return ordTaskSvc.getByExecutePlanTimeBeforeNow(executeState, taskType);
	}
	

	/**
	 * 执行订单签收任务 Title: executeSignInOrderTask Description:
	 * 
	 * @param executeFactTime
	 * @param id
	 * @param doneState
	 * @param noneState
	 * @return
	 * @date 2018年5月21日 下午3:30:46
	 */
	@PostMapping("/ord/task/signin")
	void executeSignInOrderTask(@RequestParam("id") long id) {
		ordTaskSvc.executeSignInOrderTask(id);
	}
	
	/**
	 * 执行取消订单任务 Title: executeSignInOrderTask Description:
	 * 
	 * @param executeFactTime
	 * @param id
	 * @param doneState
	 * @param noneState
	 * @return
	 * @date 2018年5月21日 下午3:30:46
	 */
	@PostMapping("/ord/task/cancleOrder")
	void executeCancelOrderTask(@RequestParam("id") long id) {
		ordTaskSvc.executeCancelOrderTask(id);
	}
}
