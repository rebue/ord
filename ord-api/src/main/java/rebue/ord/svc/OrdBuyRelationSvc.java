package rebue.ord.svc;

import rebue.ord.mo.OrdBuyRelationMo;
import rebue.robotech.svc.MybatisBaseSvc;

public interface OrdBuyRelationSvc extends MybatisBaseSvc<OrdBuyRelationMo, java.lang.Long>{
	
	int updateByUplineOrderDetailId(OrdBuyRelationMo mo);
	
}