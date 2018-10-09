package rebue.ord.svc;

import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * 用户商品购买关系
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdGoodsBuyRelationSvc extends MybatisBaseSvc<OrdGoodsBuyRelationMo, java.lang.Long> {

    /**
     *  导出redis中的购买关系到数据库中
     */
    void exportGoodsBuyRelation();

    /**
     * 获取用户购买关系
     */
    OrdGoodsBuyRelationMo getBuyRelation(OrdGoodsBuyRelationMo mo);
}
