package rebue.ord.svr.feign;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.OrdSettleRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.to.OrderSignInTo;
import rebue.sbs.feign.FeignConfig;

/**
 * 创建时间：2018年5月21日 下午12:24:25 项目名称：ord-svr-feign
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderSvc.java 类说明： 订单对内接口
 */
@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdOrderSvc {

    /**
     * 用户取消订单 Title: cancellationOfOrder Description:
     * 
     * @param qo
     * @return
     * @date 2018年5月21日 下午12:25:27
     */
    @PutMapping("/ord/order/cancel")
    CancellationOfOrderRo cancellationOfOrder(OrdOrderMo qo);

    /***
     * 订单签收 Title: orderSignIn Description:
     * 
     * @param qo
     * @return
     * @date 2018年5月21日 下午12:26:05
     */
    @PutMapping("/ord/order/ordersignin")
    OrderSignInRo orderSignIn(OrderSignInTo qo);

    /**
     * 获取供应商待结算和已结算的订单
     * 
     * @param mo
     * @return
     */
    @GetMapping("/ord/order/getSettleTotal")
    OrdSettleRo getSettleTotal(@RequestParam("supplierId") Long supplierId);

    /**
     * 根据用户和时间查询已经支付的订单
     * 
     * @param mo
     * @return
     */
    @GetMapping("/ord/order/havepaidorderbyuserandtime")
    List<OrdOrderMo> havePaidOrderByUserAndTimeList(@RequestParam("userId") Long userId,
            @RequestParam("receivedTime") Date receivedTime);

    /**
     * 根据用户id查询订单状态不为退货和未支付且支付时间为最新的订单信息
     * 
     * @param userId
     * @return
     */
    @GetMapping("/ord/order/getLatestOne")
    OrdOrderMo getLatestOneByUserId(@RequestParam("userId") Long userId);

    /**
     * 获取单个订单信息
     *
     */
    @GetMapping("/ord/order/getbyid")
    OrdOrderMo getById(@RequestParam("id") final java.lang.Long id);
}
