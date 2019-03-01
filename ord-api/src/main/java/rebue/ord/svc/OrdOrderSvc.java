package rebue.ord.svc;

import com.github.pagehelper.PageInfo;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import rebue.afc.msg.PayDoneMsg;
import rebue.ord.jo.OrdOrderJo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrdSettleRo;
import rebue.ord.ro.OrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.to.CancelDeliveryTo;
import rebue.ord.to.ListOrderTo;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.OrderTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.ord.to.UpdateOrgTo;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.BaseSvc;

/**
 * 订单信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdOrderSvc extends BaseSvc<java.lang.Long, OrdOrderMo, OrdOrderJo> {

    /**
     * 下订单
     */
    OrderRo order(OrderTo to);

    /**
     * 查询用户订单信息 Title: selectOrderInfo Description:
     *
     * @param mo
     * @return
     * @throws ParseException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @date 2018年4月9日 下午4:48:40
     */
    List<Map<String, Object>> selectOrderInfo(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     * 取消订单 Title: cancellationOfOrder Description:
     *
     * @return
     * @date 2018年4月9日 下午6:49:19
     */
    CancellationOfOrderRo cancellationOfOrder(OrdOrderMo mo);

    /**
     * 修改订单实际金额 Title: updateOrderRealMoney Description:
     *
     * @param mo
     * @return
     * @date 2018年4月12日 下午2:59:53
     */
    ModifyOrderRealMoneyRo modifyOrderRealMoney(OrdOrderMo mo);

    /**
     * 设置快递公司 Title: setUpExpressCompany Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 上午11:23:14
     */
    SetUpExpressCompanyRo setUpExpressCompany(OrdOrderMo mo);

    /**
     * 本店发货 Title: sendAndPrint Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 下午6:19:09
     */
    ShipmentConfirmationRo deliver(ShipmentConfirmationTo mo);

    ShipmentConfirmationRo deliverAndGetTrace(ShipmentConfirmationTo to);

    /**
     * 订单签收 Title: orderSignIn Description:
     *
     * @param mo
     * @return
     * @date 2018年4月14日 下午2:29:58
     */
    OrderSignInRo orderSignIn(OrderSignInTo mo);

    /**
     * 修改订单退款金额(根据订单ID和已退款总额)
     *
     * @param refundTotal
     *            退款总额
     * @param orderState
     *            订单状态
     * @param whereOrderId
     *            where条件-订单ID
     * @param whereRefundedTotal
     *            where条件-已退款总额
     */
    int modifyRefund(BigDecimal refundTotal, Byte orderState, Long whereOrderId, BigDecimal whereRefundedTotal);

    /**
     * 根据订单编号修改订单状态 Title: modifyOrderStateByOderCode Description:
     *
     * @param orderCode
     * @param orderState
     * @return
     * @date 2018年5月8日 上午10:45:35
     */
    int modifyOrderStateByOderCode(long orderCode, byte orderState);

    /**
     * 根据订单编号查询退货金额 Title: selectReturnAmountByOrderCode Description:
     *
     * @param orderCode
     * @return
     * @date 2018年5月11日 上午11:15:03
     */
    OrdOrderMo selectReturnAmountByOrderCode(String orderCode);

    /**
     * 根据订单编号修改订单
     */
    int updateByOrderCode(OrdOrderMo record);

    /**
     * 设置订单结算完成
     */
    int completeSettle(Date closeTime, String orderId);

    /**
     * 订单支付
     */
    boolean handleOrderPaidNotify(PayDoneMsg msg);

    /**
     * 根据订单id查询订单状态
     */
    Byte selectOrderStateByOrderCode(String id);

    /**
     * 分页查询订单
     */
    PageInfo<OrdOrderRo> listOrder(ListOrderTo to, int pageNum, int pageSize);

    /**
     * 供应商分页查询订单
     */
    PageInfo<OrdOrderRo> SupplierlistOrder(ListOrderTo to, int pageNum, int pageSize);



    /**
     * 修改收件人信息
     *
     * @param mo
     * @return
     */
    Ro modifyOrderReceiverInfo(OrdOrderMo mo);

    /**
     * 根据订单id修改支付订单id
     *
     * @param id
     * @return
     */
    Ro modifyPayOrderId(Long id);

    /**
     * 根据订单id查询订单签收时间
     *
     * @param orderIds
     * @return
     */
    List<OrdOrderMo> getOrderSignTime(String orderIds);

    /**
     * 检查订单是否可结算
     * 1. 订单必须存在
     * 2. 订单必须处于签收状态
     * 3. 订单必须已经记录签收时间
     * 4. 已经超过订单启动结算的时间
     * 5. 如果订单还有退货中的申请未处理完成，不能结算
     */
    Boolean isSettleableOrder(OrdOrderMo order);

    /**
     * 取消发货
     * @param to
     * @return
     */
    Ro cancelDelivery(CancelDeliveryTo to);

    /**
     *  根据供应商id获取已经结算或者待结算总额
     *  @param order
     *  @return
     */
    OrdSettleRo getSettleTotal(Long supplierId);

    /**
     *  修改组织
     *  @param to
     *  @return
     */
    Ro modifyOrg(UpdateOrgTo to);
    
    /**
     * 根据用户id来获取已支付，已发货，已签收的订单详情待全返金额
     * @param userId
     * @return
     */
    BigDecimal getCommissionTotal(@RequestParam("userId") final java.lang.Long userId);
    
    /**
     * 根据组织id获取未发货的订单数
     * @param deliverOrgId
     * @return
     */
    public BigDecimal getUnshipmentsByDeliverOrgId(@RequestParam("deliverOrgId") final java.lang.Long deliverOrgId);

	/**
	 * 根据用户和时间查询已经支付的订单
	 * 
	 * @param mo
	 * @return
	 */
	List<OrdOrderMo> havePaidOrderByUserAndTimeList(OrdOrderMo mo);
	
    
    /**
     * 第一种发货方式 Merge=True Split=false
     * @param to
     * @return
     */
    ShipmentConfirmationRo deliverForMergeIsTrueAndSplitIsFalse( ShipmentConfirmationTo to);
	
}
