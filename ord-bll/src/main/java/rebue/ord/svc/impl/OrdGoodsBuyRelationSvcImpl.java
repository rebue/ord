package rebue.ord.svc.impl;

import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.ord.mapper.OrdGoodsBuyRelationMapper;
import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.ord.svc.OrdGoodsBuyRelationSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.sbs.redis.RedisClient;

/**
 * 用户商品购买关系
 *
 * 在单独使用不带任何参数的 @Transactional 注释时， propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED， 而且事务不会针对受控异常（checked exception）回滚。
 *
 * 注意： 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdGoodsBuyRelationSvcImpl
		extends MybatisBaseSvcImpl<OrdGoodsBuyRelationMo, java.lang.Long, OrdGoodsBuyRelationMapper>
		implements OrdGoodsBuyRelationSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdGoodsBuyRelationSvcImpl.class);

	@Resource
	private OrdGoodsBuyRelationSvc thisSvc;

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(OrdGoodsBuyRelationMo mo) {
		_log.info("添加用户商品购买关系");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	/**
	 * 重写添加用户商品购买关系（此方法不处理重复添加）
	 * @param mo
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int addEx(OrdGoodsBuyRelationMo mo) {
		_log.info("重写添加用户商品购买关系（此方法不处理重复添加）的参数为：{}", mo);
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		try {
			return super.add(mo);
		} catch (DuplicateKeyException e) {
			_log.error("重写添加用户商品购买关系（此方法不处理重复添加）出现重复添加，请求的参数为：{}", mo);
			return 1;
		}
	}

	@Resource
	private RedisClient redisClient;

	/**
	 * 导出redis中的购买关系到数据库中
	 */
	@Override
	public void exportGoodsBuyRelation() {
		// String
		Map<String, String> listByWildcard = redisClient.listByWildcard("rebue.suc.svc.user.buy_relation.*");
		System.out.println("模糊查询");
		for (Entry<String, String> item : listByWildcard.entrySet()) {
			System.out.println(item.getKey() + ":" + item.getValue());
			String key = item.getKey();
			// 替换key中的前缀rebue.suc.svc.user.buy_relation.
			String keys = key.replace("rebue.suc.svc.user.buy_relation.", "");
			_log.info("替换前缀之后的值为：{}", keys);
			// 获取用户Id（下家Id）
			String userId = keys.substring(0, 18);
			_log.info("获取到的用户Id（下家Id）为：{}", userId);
			// 获取上线Id
			String onlineId = keys.substring(18, 36);
			_log.info("获取到的上家Id为：{}", onlineId);
		}
	}

	@Override
	public OrdGoodsBuyRelationMo getBuyRelation(OrdGoodsBuyRelationMo mo) {
		// TODO Auto-generated method stub
		return _mapper.getBuyRelation(mo);
	}
}
