package rebue.ord.svc;

import java.util.Map;
import rebue.ord.mo.OrdAddrMo;
import rebue.robotech.svc.MybatisBaseSvc;

public interface OrdAddrSvc extends MybatisBaseSvc<OrdAddrMo, java.lang.Long> {

    /**
     *  修改用户默认收货地址 Title: exUpdate Description:
     *
     *  @param mo
     *  @return
     *  @date 2018年4月8日 下午3:15:22
     */
    Map<String, Object> updateDef(OrdAddrMo mo);

    /**
     *  修改用户收货地址 Title: update Description:
     *
     *  @param mo
     *  @return
     *  @date 2018年4月8日 下午4:09:18
     */
    Map<String, Object> update(OrdAddrMo mo);
}
