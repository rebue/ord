package rebue.ord.svc;

import java.util.List;

import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 */
public interface OrdBuyRelationSvc extends MybatisBaseSvc<OrdBuyRelationMo, java.lang.Long> {

    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);
    
    
    
    List<DetailandBuyRelationRo>  getBuyRelationByOrderId(long orderId);
}
