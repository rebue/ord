package rebue.ord.svr.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.robotech.ro.Ro;
import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdSettleTaskSvc {

	/**
	 * 执行结算任务
	 * @param id
	 * @return
	 */
	@PostMapping("/ord/settletask/execute")
	Ro executeSettleTask(@RequestParam("id") Long id);
	
	/**
	 * 获取结算任务
	 * 
	 * @param to
	 * @return
	 */
	@GetMapping("/ord/settletask/getid")
	List<Long> getTaskIdsThatShouldExecute();
}
