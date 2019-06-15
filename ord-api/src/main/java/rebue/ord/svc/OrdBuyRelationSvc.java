package rebue.ord.svc;

import java.math.BigDecimal;
import java.util.List;

import rebue.ord.jo.OrdBuyRelationJo;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.robotech.svc.BaseSvc;

/**
 */
public interface OrdBuyRelationSvc extends BaseSvc<java.lang.Long, OrdBuyRelationMo, OrdBuyRelationJo> {

    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);

    List<DetailandBuyRelationRo> getBuyRelationByOrderId(long orderId);

    /**
     * 根据匹配自己匹配购买关系
     */
    boolean getAndUpdateBuyRelationByOwn(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId, long orderTimestamp);

    /**
     * 根据购买规则匹配购买关系
     */
    boolean getAndUpdateBuyRelationByPromote(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId);

    /**
     * 根据邀请规则匹配购买关系
     */
    boolean getAndUpdateBuyRelationByInvite(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId);

    /**
     * 根据匹配差一人，且邀请一人（关系来源是购买关系的）规则匹配购买关系
     */
    boolean getAndUpdateBuyRelationByFour(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId);

    /**
     * 根据匹配差两人的规则匹配购买关系
     */
    boolean getAndUpdateBuyRelationByFive(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId);

    /**
     * 根据匹配差一人的规则匹配购买关系
     */
    boolean getAndUpdateBuyRelationBySix(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId);

    /**
     * 匹配购买关系
     */
    String matchBuyRelation(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId, long downLineOrderId, long orderTimestamp);
    
    
    
    
    /**
     * 根据下家详情id将该条关系的是否已签收改为已经签收。
     * @param detailId
     * @return
     */
    int  modifyIsSignInByDownlineDetailId(Long downlineDetailId);

}
