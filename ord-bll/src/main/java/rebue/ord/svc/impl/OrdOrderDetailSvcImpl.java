package rebue.ord.svc.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.ord.mapper.OrdOrderDetailMapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

@Service
/**
 * <pre>
 * 在单独使用不带任何参数 的 @Transactional 注释时，
 * propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED，
 * 而且事务不会针对受控异常（checked exception）回滚。
 * 注意：
 * 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 * </pre>
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class OrdOrderDetailSvcImpl extends MybatisBaseSvcImpl<OrdOrderDetailMo, java.lang.Long, OrdOrderDetailMapper> implements OrdOrderDetailSvc {

    /**
     * @mbg.generated
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(OrdOrderDetailMo mo) {
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    /**
     *  根据订单编号、详情ID修改退货数量和返现总额 Title: modifyReturnCountAndCashBackTotal
     *  Description:
     *
     *  @param orderId
     *  @param orderDetailId
     *  @param returnCount
     *  @param cashbackTotal
     *  @return
     *  @date 2018年5月7日 上午9:53:45
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturnCountAndCashBackTotal(OrdOrderDetailMo mo) {
        return _mapper.modifyReturnCountAndCashBackTotal(mo);
    }

    /**
     *  根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     *
     *  @param id
     *  @param returnState
     *  @return
     *  @date 2018年5月8日 上午10:59:02
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturnStateById(long id, byte returnState) {
        return _mapper.modifyReturnStateById(returnState, id);
    }
}
