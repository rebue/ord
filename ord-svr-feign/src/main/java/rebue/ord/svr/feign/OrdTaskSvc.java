package rebue.ord.svr.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.ord.mo.OrdTaskMo;
import rebue.sbs.feign.FeignConfig;

/**
 * 创建时间：2018年5月21日 下午3:21:05 
 * 项目名称：ord-svr-feign
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdTaskSvc.java 
 * 类说明： 订单任务内部接口
 */
@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdTaskSvc {

	/**
	 * 查询订单任务信息 Title: list Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年5月21日 下午3:24:56
	 */
	@GetMapping(value = "/ord/task") 
	List<OrdTaskMo> list(@RequestParam("executeState") int executeState, @RequestParam("taskType") int taskType);

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
	void executeSignInOrderTask(@RequestParam("id") long id);
}
