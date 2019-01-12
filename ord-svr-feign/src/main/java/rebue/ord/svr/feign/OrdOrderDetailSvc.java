package rebue.ord.svr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdOrderDetailSvc {
	
	/**
	 * 根据上线id修改订单详情供应商和发货组织
	 * @param mo
	 * @return
	 */
	@PutMapping("/ord/modifyDeliverAndSupplierByOnlineid")
	int modifyDeliverAndSupplierByOnlineid(@RequestParam("supplierId") Long supplierId ,@RequestParam("deliverOrgId") Long deliverOrgId,@RequestParam("onlineId") Long onlineId);
}
