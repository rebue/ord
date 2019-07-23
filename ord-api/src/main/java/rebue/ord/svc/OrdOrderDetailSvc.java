package rebue.ord.svc;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;

import rebue.ord.jo.OrdOrderDetailJo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.ro.ShiftOrderRo;
import rebue.ord.ro.WaitingBuyPointByUserIdListRo;
import rebue.ord.to.ModifyInviteIdTo;
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
    int modifyReturn(Integer returnTotal, BigDecimal newCashbackTotal, Byte returnState, Long whereDetailId,
            Integer whereReturnedCount, BigDecimal whereOldCashbackTotal);

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

    List<DetailandBuyRelationRo> listBuyRelationByOrderId(Long orderId);

    /**
     * 得到买家已下单指定上线规格商品的数量(以此来限制买家购买)
     *
     * @param userId
     *            购买用户的用户ID
     * @param onlineSpecId
     *            上线规格ID
     */
    BigDecimal getBuyerOrderedCount(Long userId, Long onlineSpecId);

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
    int modifyReturnNumAndCashbackTotal(Long id, BigDecimal oldCashbackTotal, BigDecimal newCashbackTotal,
            BigDecimal returnedCount, BigDecimal returnTotal);

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
    int modifyActualAmountANDReturnState(Long id, BigDecimal newActualAmount, BigDecimal oldActualAmount,
            Byte returnState, Byte returnedState, BigDecimal realBuyPointTotal);

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

    /**
     * 根据用户id计算待入账的积分
     * 
     * @param userId
     * @return
     */
    BigDecimal countWaitingBuyPointByUserId(Long userId);

    /**
     * 获取用户待入积分
     * 
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<WaitingBuyPointByUserIdListRo> waitingBuyPointByUserIdList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 根据上线id修改订单详情供应商和发货组织
     * 
     * @param mo
     * @return
     */
    int modifyDeliverAndSupplierByOnlineid(Long supplierId, Long deliverOrgId, Long onlineId);

    /**
     * 补偿双倍积分
     */
    void compensatePoint();

    /**
     * 根据订单id，上线id，规格id去修改订单状态
     *
     * @param orderId
     *            订单id
     * @param onlineId
     *            上线id
     * @param onlineSpecId
     *            规格id
     * @return
     */
    int updateIsDeliver(Long orderId, Long onlineId, Long onlineSpecId);

    int modifyUserIdByOrderId(Long orderId, Long userId);

    /**
     * 根据订单详情id修改订单详情邀请人id
     * 
     * @param id
     * @param inviteId
     * @return
     */
    ShiftOrderRo modifyInviteId(List<ModifyInviteIdTo> modifyInviteIdList);

}
