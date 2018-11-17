package rebue.ord.svc;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.CancelDeliveryRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.to.ListOrderTo;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.OrderTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * 订单信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdOrderSvc extends MybatisBaseSvc<OrdOrderMo, java.lang.Long> {

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
    List<Map<String, Object>> selectOrderInfo(Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

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
     * 取消发货 Title: cancelDeliveryUpdateOrderState Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 下午2:59:19
     */
    CancelDeliveryRo cancelDelivery(OrdOrderMo mo);

    /**
     * 本店发货 Title: sendAndPrint Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 下午6:19:09
     */
    ShipmentConfirmationRo shipmentConfirmation(ShipmentConfirmationTo mo);

    /**
     * 供应商发货
     */

    ShipmentConfirmationRo sendBySupplier(ShipmentConfirmationTo to);

    /**
     * 订单签收 Title: orderSignIn Description:
     *
     * @param mo
     * @return
     * @date 2018年4月14日 下午2:29:58
     */
    OrderSignInRo orderSignIn(OrderSignInTo mo);

    /**
     * 根据退货编号修改退货金额 Title: modifyReturnAmountByorderCode Description:
     *
     * @param mo
     * @return
     * @date 2018年5月7日 上午9:18:51
     */
    int modifyReturnAmountByorderCode(OrdOrderMo mo);

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
     * 结算完成
     */
    int finishSettlement(Date closeTime, String orderId);

    /**
     * 订单支付
     */
    boolean handleOrderPaidNotify(Long payOrderId, Date payTime);

    /**
     * 根据订单id查询订单状态
     */
    Byte selectOrderStateByOrderCode(String id);

    /**
     * 查询用户待返现订单信息
     */
//    List<Map<String, Object>> getCashBackOrder(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     * 分页查询订单
     */
    PageInfo<OrdOrderRo> listOrder(ListOrderTo to, int pageNum, int pageSize);

    /**
     * 修改收件人信息
     * 
     * @param mo
     * @return
     */
    Ro modifyOrderReceiverInfo(OrdOrderMo mo);

    /**
     * 根据订单id修改支付订单id
     * @param id
     * @return
     */
    Ro modifyPayOrderId(Long id);
}
