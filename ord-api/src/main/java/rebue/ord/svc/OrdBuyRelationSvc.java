package rebue.ord.svc;

import java.math.BigDecimal;
import java.util.List;

import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 */
public interface OrdBuyRelationSvc extends MybatisBaseSvc<OrdBuyRelationMo, java.lang.Long> {

	int updateByUplineOrderDetailId(OrdBuyRelationMo mo);

	List<DetailandBuyRelationRo> getBuyRelationByOrderId(long orderId);

	boolean getAndUpdateBuyRelationByOwn(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);

	boolean getAndUpdateBuyRelationByPromote(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);

	boolean getAndUpdateBuyRelationByInvite(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);

	boolean getAndUpdateBuyRelationByFour(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);

	boolean getAndUpdateBuyRelationByFive(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);

	boolean getAndUpdateBuyRelationBySix(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);
	
	String matchBuyRelation(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,long downLineOrderId);
}
