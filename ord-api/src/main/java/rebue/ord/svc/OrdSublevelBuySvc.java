package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;
import rebue.ord.mo.OrdSublevelBuyMo;

public interface OrdSublevelBuySvc extends MybatisBaseSvc<OrdSublevelBuyMo, java.lang.Long>{
	
	/**
	 * 根据详情ID，更新购买关系
	 */
	
	int updateByOrderDetailId (OrdSublevelBuyMo mo);

}