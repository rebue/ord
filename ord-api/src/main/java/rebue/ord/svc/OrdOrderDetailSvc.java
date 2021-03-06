package rebue.ord.svc;

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
     *  根据订单编号、详情ID修改退货数量、返现总额 Title: modifyReturnCountAndCashBackTotal
     *  Description:
     *
     *  @param orderId
     *  @param orderDetailId
     *  @param returnCount
     *  @param cashbackTotal
     *  @return
     *  @date 2018年5月7日 上午9:54:27
     */
    int modifyReturnCountAndCashBackTotal(OrdOrderDetailMo mo);

    /**
     *  根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     *
     *  @param id
     *  @param returnState
     *  @return
     *  @date 2018年5月8日 上午10:59:44
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

    List<DetailandBuyRelationRo> listByOrderId(Long orderId);
}
