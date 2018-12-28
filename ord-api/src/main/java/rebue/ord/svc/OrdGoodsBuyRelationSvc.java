package rebue.ord.svc;

import rebue.ord.jo.OrdGoodsBuyRelationJo;
import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.robotech.svc.BaseSvc;

/**
 * 用户商品购买关系
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdGoodsBuyRelationSvc extends BaseSvc<java.lang.Long, OrdGoodsBuyRelationMo, OrdGoodsBuyRelationJo> {

    /**
     *  导出redis中的购买关系到数据库中
     */
    void exportGoodsBuyRelation();

    /**
     * 获取用户购买关系
     */
    OrdGoodsBuyRelationMo getBuyRelation(OrdGoodsBuyRelationMo mo);

    /**
     * 重写添加用户商品购买关系（此方法不处理重复添加）
     * @param mo
     * @return
     */
	int addEx(OrdGoodsBuyRelationMo mo);
}
