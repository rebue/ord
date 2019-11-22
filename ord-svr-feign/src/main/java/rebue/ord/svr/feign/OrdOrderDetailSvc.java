package rebue.ord.svr.feign;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.ord.mo.OrdOrderDetailMo;
import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "ord-svr", configuration = FeignConfig.class, contextId = "ord-svr-order-detail")
public interface OrdOrderDetailSvc {

    /**
     * 根据上线id修改订单详情供应商和发货组织
     * 
     * @param mo
     * @return
     */
    @PutMapping("/ord/modifyDeliverAndSupplierByOnlineid")
    int modifyDeliverAndSupplierByOnlineid(@RequestParam("supplierId") Long supplierId,
            @RequestParam("deliverOrgId") Long deliverOrgId, @RequestParam("onlineId") Long onlineId);

    @GetMapping("/ord/order-detail/get-one")
    OrdOrderDetailMo getOneDetail(@RequestParam("userId") Long userId, @RequestParam("buyPrice") BigDecimal buyPrice);

    /**
     * 获取单个订单详情
     *
     */
    @GetMapping("/ord/orderdetail/getbyid")
    OrdOrderDetailMo getById(@RequestParam("id") java.lang.Long id);

    @PostMapping("/ord/export-data")
    void exportData();

    @PostMapping("/ord/export-data２")
    void exportData２();
    
    
    @GetMapping("/ord/detailList")
    List<OrdOrderDetailMo> listAll(@RequestParam("orderId") java.lang.Long orderId);
}
