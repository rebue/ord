package rebue.ord.svc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import rebue.ord.jo.OrdOrderDetailJo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.to.UpdateOrgTo;
import rebue.robotech.svc.BaseSvc;

/**
 * 订单详情
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdOrderDetailSvc extends BaseSvc<java.lang.Long, OrdOrderDetailMo, OrdOrderDetailJo> {

    /**
     * 根据OrderId获取订单详情列表
     */
    List<OrdOrderDetailMo> listByOrderId(Long orderId);

    // 
    int modifyReturn(Integer returnTotal, BigDecimal newCashbackTotal, Byte returnState, Long whereDetailId, Integer whereReturnedCount, BigDecimal whereOldCashbackTotal);

    /**
     * 根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     */
    int modifyReturnStateById(long id, byte returnState);

    /**
     * 根据订单ID修改发货状态 Title: modifyReturnStateById Description:
     */
    int modifyIsDeliverByOrderId(long orderId);

    /**
     * 更新订单详情的返佣名额字段
     */
    int updateCashbackSlot(OrdOrderDetailMo mo);

    /**
     * 添加购买关系时，更新订单详情返佣名额字段
     */
    int updateCommissionSlotForBuyRelation(OrdOrderDetailMo mo);

    OrdOrderDetailMo getOrderDetailForBuyRelation(Map<String, Object> map);

    OrdOrderDetailMo getOrderDetailForOneCommissonSlot(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByInvite(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByFour(Map<String, Object> map);

    List<DetailandBuyRelationRo> listBuyRelationByOrderId(Long orderId);

    /**
     * 得到买家已下单指定上线规格商品的数量(以此来限制买家购买)
     *
     * @param userId
     *            购买用户的用户ID
     * @param onlineSpecId
     *            上线规格ID
     */
    int getBuyerOrderedCount(Long userId, Long onlineSpecId);

    /**
     * 设置订单详情已结算返现金给买家
     */
    void settleBuyer(Long orderDetailId);

    /**
     * 修改返现总额、退货数量
     *
     * @param id
     * @param oldCashbackTotal
     * @param newCashbackTotal
     * @param returnedCount
     * @param returnTotal
     * @return
     */
    int modifyReturnNumAndCashbackTotal(Long id, BigDecimal oldCashbackTotal, BigDecimal newCashbackTotal, Integer returnedCount, Integer returnTotal);

    /**
     * 修改订单详情实际金额和退货状态
     *
     * @param id
     * @param newActualAmount
     * @param oldActualAmount
     * @param returnState
     * @param returnedState
     * @return
     */
    int modifyActualAmountANDReturnState(Long id, BigDecimal newActualAmount, BigDecimal oldActualAmount, Byte returnState, Byte returnedState);

    /**
     * 根据id修改供应商id
     *
     * @param id
     *            订单详情id
     * @param newSupplierId
     *            新供应商id
     * @param oldSupplierId
     *            旧供应商id
     * @return
     */
    int updateSupplierIdById(Long id, Long newSupplierId, Long oldSupplierId);

    /**
     * 计算首单购买
     *
     * @param onlineSpecId
     *            上线规格ID
     */
    void calcFirstBuy(Long onlineSpecId);

    /**
     * 修改组织
     */
    int modifyOrg(UpdateOrgTo to);
}
