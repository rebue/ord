package rebue.ord.svr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.to.OrderSignInTo;
import rebue.sbs.feign.FeignConfig;

/**  
* 创建时间：2018年5月21日 下午12:24:25  
* 项目名称：ord-svr-feign  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：OrdOrderSvc.java  
* 类说明： 订单对内接口
*/
@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdOrderSvc {

	/**
	 * 用户取消订单
	 * Title: cancellationOfOrder
	 * Description: 
	 * @param qo
	 * @return
	 * @date 2018年5月21日 下午12:25:27
	 */
	@PutMapping("/ord/order/cancel")
	CancellationOfOrderRo cancellationOfOrder(OrdOrderMo qo);
	
	/***
	 * 订单签收
	 * Title: orderSignIn
	 * Description: 
	 * @param qo
	 * @return
	 * @date 2018年5月21日 下午12:26:05
	 */
	@PutMapping("/ord/order/ordersignin")
	OrderSignInRo orderSignIn(OrderSignInTo qo);
}
  

