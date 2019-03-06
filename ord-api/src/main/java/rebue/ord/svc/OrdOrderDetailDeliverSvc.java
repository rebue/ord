package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import rebue.ord.mo.OrdOrderDetailDeliverMo;
import rebue.ord.ro.OrderdetaildeliverRo;

/**
 * 订单详情发货信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdOrderDetailDeliverSvc extends MybatisBaseSvc<OrdOrderDetailDeliverMo, java.lang.Long>{
	
	List<OrderdetaildeliverRo> listOrderdetaildeliver(@RequestParam("orderId") final java.lang.Long orderId);

}