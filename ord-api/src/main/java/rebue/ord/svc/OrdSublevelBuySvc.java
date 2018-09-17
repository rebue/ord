package rebue.ord.svc;

import rebue.ord.mo.OrdSublevelBuyMo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * 下级购买信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdSublevelBuySvc extends MybatisBaseSvc<OrdSublevelBuyMo, java.lang.Long> {

    int updateByOrderDetailId(OrdSublevelBuyMo mo);
}
