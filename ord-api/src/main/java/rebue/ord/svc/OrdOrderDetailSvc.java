package rebue.ord.svc;

import java.math.BigDecimal;

import rebue.ord.mo.OrdOrderDetailMo;
import rebue.robotech.svc.MybatisBaseSvc;

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
     * 根据上线ID及价格查找上家用户全返商品,如有多个则找到时间最早的一个商品详情，以确定上家
     */
    
    OrdOrderDetailMo getFullReturnDetail(OrdOrderDetailMo mo);
    
}
