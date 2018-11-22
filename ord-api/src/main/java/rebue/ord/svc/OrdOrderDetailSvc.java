package rebue.ord.svc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * 订单详情
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdOrderDetailSvc extends MybatisBaseSvc<OrdOrderDetailMo, java.lang.Long> {

    /**
     * 根据OrderId获取订单详情列表
     */
    List<OrdOrderDetailMo> listByOrderId(Long orderId);

    /**
     * 修改订单详情的退货情况(根据订单详情ID、已退货数量、旧的返现金总额，修改退货总数、返现金总额以及退货状态)
     *
     * @param returnTotal
     *            退货总数
     * @param newCashbackTotal
     *            新的返现金总额
     * @param returnState
     *            退货状态
     * @param whereDetailId
     *            where-订单详情ID
     * @param whereReturnedCount
     *            where-之前的已退货数量
     * @param whereOldCashbackTotal
     *            where-退货之前的返现金总额
     */
    int modifyReturn(Integer returnTotal, BigDecimal newCashbackTotal,//
            Byte returnState, Long whereDetailId, //
            Integer whereReturnedCount, BigDecimal whereOldCashbackTotal);

    /**
     * 根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     */
    int modifyReturnStateById(long id, byte returnState);

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

}
