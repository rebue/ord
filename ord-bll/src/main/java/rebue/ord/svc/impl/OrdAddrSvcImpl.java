package rebue.ord.svc.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.ord.mapper.OrdAddrMapper;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 用户收货地址
 *
 * 在单独使用不带任何参数的 @Transactional 注释时，
 * propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED，
 * 而且事务不会针对受控异常（checked exception）回滚。
 *
 * 注意：
 * 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdAddrSvcImpl extends MybatisBaseSvcImpl<OrdAddrMo, java.lang.Long, OrdAddrMapper> implements OrdAddrSvc {

    /**
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdAddrSvcImpl.class);

    /**
     *  添加用户收货地址 2018年4月8日13:48:03
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(OrdAddrMo mo) {
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        Date date = new Date();
        mo.setOpTime(date);
        int j =super.add(mo);
        if(j==1) {
        	OrdAddrMo pm=new OrdAddrMo();
        	pm.setUserId(mo.getUserId());
        	List<OrdAddrMo> result=super.list(pm);
            _log.info("添加发件人后查询是否只有一个收货地址的结果为：{}", result.size());
        	if(result.size()==1) {
        		pm.setId(result.get(0).getId());
        		pm.setUserId(result.get(0).getUserId());
        		pm.setIsDef(true);
                _log.info("将唯一的一个收货地址改为默认的参数为：{}", pm);
        		int i=_mapper.updateByPrimaryKeySelective(pm);
                _log.info("将唯一的一个收货地址改为默认的结果为：{}", i);
        	}
        }
        return j ;
    }

    /**
     *  修改用户默认收货地址 Title: exUpdate Description:
     *
     *  @param mo
     *  @date 2018年4月8日 下午3:05:54
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Map<String, Object> updateDef(OrdAddrMo mo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        _log.info("修改用户默认收货地址的参数为：{}", mo);
        List<OrdAddrMo> list = _mapper.selectSelective(mo);
        long newId = mo.getId();
        boolean isDef = mo.getIsDef();
        long oldId = 0;
        if (list.size() != 0) {
            oldId = list.get(0).getId();
            mo.setId(oldId);
            mo.setIsDef(false);
            int result = _mapper.updateByPrimaryKeySelective(mo);
            if (result < 1) {
                _log.error("{}修改原默认收货地址失败", mo.getUserId());
                throw new RuntimeException("设置失败");
            }
        }
        mo.setId(newId);
        mo.setIsDef(isDef);
        int result = _mapper.updateByPrimaryKeySelective(mo);
        if (result > 0) {
            _log.info("{}修改用户收货地址成功", mo.getUserId());
            resultMap.put("result", 1);
            resultMap.put("msg", "设置成功");
        } else {
            _log.error("{}修改用户收货地址失败", mo.getUserId());
            throw new RuntimeException("设置失败");
        }
        return resultMap;
    }

    /**
     *  修改用户收货地址 2018年4月8日16:09:49
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Map<String, Object> update(OrdAddrMo mo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean newisDef = mo.getIsDef();
        long newId = mo.getId();
        if (newisDef) {
            List<OrdAddrMo> list = _mapper.selectSelective(mo);
            if (list.size() != 0) {
                mo.setId(list.get(0).getId());
                mo.setIsDef(false);
                _log.info("修改默认收货地址的参数为：{}", mo);
                int result = _mapper.updateByPrimaryKeySelective(mo);
                if (result < 1) {
                    _log.error("{}修改原默认收货地址失败", mo.getUserId());
                    throw new RuntimeException("修改原默认收货地址失败");
                }
            }
        }
        Date date = new Date();
        mo.setOpTime(date);
        mo.setId(newId);
        mo.setIsDef(newisDef);
        _log.info("修改用户收货地址的参数为：{}", mo);
        int result = _mapper.updateByPrimaryKeySelective(mo);
        if (result > 0) {
            _log.info("{}修改用户收货地址成功", mo.getUserId());
            resultMap.put("result", 1);
            resultMap.put("msg", "修改成功");
        } else {
            _log.error("{}修改用户收货地址失败", mo.getUserId());
            throw new RuntimeException("修改失败");
        }
        return resultMap;
    }
}
