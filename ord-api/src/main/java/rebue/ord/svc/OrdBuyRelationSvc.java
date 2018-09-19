package rebue.ord.svc;

import rebue.ord.mo.OrdBuyRelationMo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdBuyRelationSvc extends MybatisBaseSvc<OrdBuyRelationMo, java.lang.Long> {

    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);
}
