package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.ord.mapper.OrdBuyRelationMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdGoodsBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.suc.mo.SucUserMo;
import rebue.suc.svr.feign.SucUserSvc;

/**
 * 订单购买关系
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
public class OrdBuyRelationSvcImpl extends MybatisBaseSvcImpl<OrdBuyRelationMo, java.lang.Long, OrdBuyRelationMapper>
		implements OrdBuyRelationSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdBuyRelationSvcImpl.class);

	@Resource
	private OrdOrderSvc ordOrderSvc;

	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	@Resource
	private OrdGoodsBuyRelationSvc ordGoodsBuyRelationSvc;

	/**
	 */
	@Resource
	private SucUserSvc sucUserSvc;

	@Resource
	private OrdBuyRelationSvc selfSvc;

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(OrdBuyRelationMo mo) {
		_log.info("添加订单购买关系");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	@Override
	public int updateByUplineOrderDetailId(OrdBuyRelationMo mo) {
		return _mapper.updateByUplineOrderDetailId(mo);
	}

	@Override
	/**
	 * 根据匹配自己规则匹配购买关系 1.查找用户购买同款产品中剩余1个购买名额的记录，如果已有购买关系下家不是自己，则添加购买关系记录;
	 * 2.如1结果为空，查用户购买同款产品中剩余两个购买名额的记录，并添加购买关系记录；
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByOwn(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("获取用户购买关系的id:" + id + " onlineId:" + onlineId + " buyPricce:" + buyPrice + " downLineDetailId:"
				+ downLineDetailId + " downLineOrderId" + downLineOrderId);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取该订单购买关系下家
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(relationMo);
		_log.info("获取到该详情的购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		// 获取用户购买该产品还有两个名额的详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("id", downLineDetailId);
		map.put("onlineId", onlineId);
		map.put("buyPrice", buyPrice);
		map.put("userId", id);
		map.put("returnState", (byte) 0);
		map.put("commissionSlot", (byte) 1);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		_log.info("获取用户自己购买剩余1个购买名额的订单详情的参数为：{}" + map);
		OrdOrderDetailMo orderDetailOfOneCommissionSlot = ordOrderDetailSvc.getOrderDetailForBuyRelation(map);
		_log.info("查找订单详情的购买关系记录");
		if (orderDetailOfOneCommissionSlot == null) {
			_log.info("获取用户购买过该产品且还有1个匹配名额的记录为空");
		} else {
			OrdBuyRelationMo relationParam = new OrdBuyRelationMo();
			relationParam.setUplineOrderDetailId(orderDetailOfOneCommissionSlot.getId());
			List<OrdBuyRelationMo> relationResult = selfSvc.list(relationParam);
			_log.info("获取到的购买关系结果为:{}", relationResult);
			if (relationResult.size() != 0 && relationResult.get(0).getRelationSource() != null
					&& relationResult.get(0).getRelationSource() != 1) {
				// 添加购买关系记录
				_log.info("在购买关系表中添加记录");
				OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
				ordBuyRelationMo.setId(_idWorker.getId());
				ordBuyRelationMo.setUplineOrderId(orderDetailOfOneCommissionSlot.getOrderId());
				ordBuyRelationMo.setUplineUserId(orderDetailOfOneCommissionSlot.getUserId());
				ordBuyRelationMo.setUplineOrderDetailId(orderDetailOfOneCommissionSlot.getId());
				ordBuyRelationMo.setDownlineUserId(id);
				ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
				ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
				ordBuyRelationMo.setRelationSource((byte) 1);
				_log.error("添加购买关系参数:{}", ordBuyRelationMo);
				int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
				if (addBuyRelationResult != 1) {
					_log.error("{}添加下级购买信息失败", id);
					throw new RuntimeException("生成购买关系出错");
				}
				// 更新购买关系订单详情的返佣名额
				OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
				updateOrderDetailMo.setCommissionSlot((byte) 0);
				updateOrderDetailMo.setId(orderDetailOfOneCommissionSlot.getId());
				updateOrderDetailMo.setCommissionState((byte) 1);
				int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
				if (updateOrderDetailResult != 1) {
					_log.error("{}更新订单详情返佣名额失败", id);
					throw new RuntimeException("更新订单详情返现名额失败");
				}
				_log.info("根据匹配自己规则匹配差一人匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
				return true;
			}
		}
		map.put("commissionSlot", (byte) 2);
		_log.info("获取用户自己购买剩余2个购买名额的订单详情的参数为：{}" + map);
		OrdOrderDetailMo orderDetailOfTwoCommissionSlot = ordOrderDetailSvc.getOrderDetailForBuyRelation(map);
		if (orderDetailOfTwoCommissionSlot == null) {
			_log.info("获取用户购买过该产品且还有两个匹配名额的记录为空");
		} else {
			_log.info("获取用户购买过该产品且还有两个匹配名额的记录为：{}", orderDetailOfTwoCommissionSlot);
			// 添加购买关系记录
			_log.info("在购买关系表中添加记录");
			OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setId(_idWorker.getId());
			ordBuyRelationMo.setUplineOrderId(orderDetailOfTwoCommissionSlot.getOrderId());
			ordBuyRelationMo.setUplineUserId(orderDetailOfTwoCommissionSlot.getUserId());
			ordBuyRelationMo.setUplineOrderDetailId(orderDetailOfTwoCommissionSlot.getId());
			ordBuyRelationMo.setDownlineUserId(id);
			ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
			ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
			ordBuyRelationMo.setRelationSource((byte) 1);
			_log.error("添加购买关系参数:{}", ordBuyRelationMo);
			int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
			if (addBuyRelationResult != 1) {
				_log.error("{}添加下级购买信息失败", id);
				throw new RuntimeException("生成购买关系出错");
			}
			// 更新订单详情的返佣名额
			OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
			updateOrderDetailMo.setCommissionSlot((byte) 1);
			updateOrderDetailMo.setId(orderDetailOfTwoCommissionSlot.getId());
			int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
			if (updateOrderDetailResult != 1) {
				_log.error("{}更新订单详情返佣名额失败", id);
				throw new RuntimeException("更新订单详情返现名额失败");
			}
			_log.info("根据匹配自己规则匹配差两人匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
			return true;
		}
		return false;
	}

	@Override
	/**
	 * 根据邀请购买规则匹配购买关系
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByPromote(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("获取用户购买关系的id:" + id + "onlineId:" + onlineId + "buyPricce:" + buyPrice);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取订单购买关系
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(relationMo);
		_log.info("获取到的该详情购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		OrdGoodsBuyRelationMo goodsBuyRelationMo = new OrdGoodsBuyRelationMo();
		goodsBuyRelationMo.setDownlineUserId(id);
		goodsBuyRelationMo.setOnlineId(onlineId);
		OrdGoodsBuyRelationMo buyRelationResult = ordGoodsBuyRelationSvc.getBuyRelation(goodsBuyRelationMo);
		_log.info("获取用户购买关系返回值为：{}", buyRelationResult);
		if (buyRelationResult == null) {
			_log.info("获取到的购买关系为空");
			return false;
		}
		// 根据产品上线ID查找购买关系用户的购买记录，看是否有符合要求的订单详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("id", downLineDetailId);
		map.put("onlineId", onlineId);
		map.put("buyPrice", buyPrice);
		map.put("userId", buyRelationResult.getUplineUserId());
		map.put("returnState", (byte) 0);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		_log.info("获取用户上线购买关系订单详情的参数为：{}" + map);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(map);
		if (orderDetailResult == null) {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 2);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		_log.error("更新订单详情返佣名额失败", id);
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		_log.info("根据匹配邀请规则匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
		return true;
	}

	@Override
	/**
	 * 匹配邀请关系
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByInvite(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配邀请关系的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取该订单购买关系
		OrdBuyRelationMo downLineRelationMo = new OrdBuyRelationMo();
		downLineRelationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(downLineRelationMo);
		_log.info("获取到的购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		// 获取用户购买该产品还有两个名额的详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("onlineId", onlineId);
		map.put("buyPrice", buyPrice);
		map.put("returnState", (byte) 0);
		map.put("downLineUserId", id);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getAndUpdateBuyRelationByInvite(map);
		if (orderDetailResult == null) {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(orderDetailResult.getId());
		List<OrdBuyRelationMo> relationResult = selfSvc.list(relationMo);
		_log.info("获取到的购买关系结果为:{}", relationResult);
		if (relationResult.size() == 0 || relationResult.get(0).getRelationSource() != 1) {
			// 添加购买关系记录
			_log.info("在购买关系表中添加记录");
			OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setId(_idWorker.getId());
			ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
			ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
			ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
			ordBuyRelationMo.setDownlineUserId(id);
			ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
			ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
			ordBuyRelationMo.setRelationSource((byte) 3);
			_log.error("添加购买关系参数:{}", ordBuyRelationMo);
			int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
			if (addBuyRelationResult != 1) {
				_log.error("{}添加下级购买信息失败", id);
				throw new RuntimeException("生成购买关系出错");
			}
			OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
			updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
			updateOrderDetailMo.setId(orderDetailResult.getId());
			if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
				updateOrderDetailMo.setCommissionState((byte) 1);
			}
			// 更新购买关系订单详情的返佣名额
			int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
			if (updateOrderDetailResult != 1) {
				_log.error("{}更新订单详情返佣名额失败", id);
				throw new RuntimeException("更新订单详情返现名额失败");
			}
			_log.info("根据邀请关系规则匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
			return true;
		} else {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
	}

	@Override
	/**
	 * 根据匹配差一人，且邀请一人（关系来源是购买关系的）的订单详情
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByFour(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差一人，且邀请一人（关系来源是购买关系的）的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取订单购买关系
		OrdBuyRelationMo downLineRelationMo = new OrdBuyRelationMo();
		downLineRelationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(downLineRelationMo);
		_log.info("获取到的购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		// 获取用户购买该产品还有两个名额的详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("onlineId",onlineId);
		map.put("buyPrice",buyPrice);
		map.put("returnState",(byte) 0);
		map.put("commissionSlot",(byte) 1);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getAndUpdateBuyRelationByFour(map);
		if (orderDetailResult == null) {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(orderDetailResult.getId());
		List<OrdBuyRelationMo> relationResult = selfSvc.list(relationMo);
		_log.info("获取到的购买关系结果为:{}", relationResult);
		if (relationResult.size() != 0 && relationResult.get(0).getRelationSource() != null
				&& relationResult.get(0).getRelationSource() == 2) {
			// 添加购买关系记录
			_log.info("在购买关系表中添加记录");
			OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setId(_idWorker.getId());
			ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
			ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
			ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
			ordBuyRelationMo.setDownlineUserId(id);
			ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
			ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
			ordBuyRelationMo.setRelationSource((byte) 3);
			_log.error("添加购买关系参数:{}", ordBuyRelationMo);
			int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
			if (addBuyRelationResult != 1) {
				_log.error("{}添加下级购买信息失败", id);
				throw new RuntimeException("生成购买关系出错");
			}
			OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
			updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
			updateOrderDetailMo.setId(orderDetailResult.getId());
			if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
				updateOrderDetailMo.setCommissionState((byte) 1);
			}
			// 更新购买关系订单详情的返佣名额
			int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
			if (updateOrderDetailResult != 1) {
				_log.error("{}更新订单详情返佣名额失败", id);
				throw new RuntimeException("更新订单详情返现名额失败");
			}
			_log.info("根据匹配差一人，且邀请一人规则匹配购买关系成功，匹配的购买关系ID为:{}" + ordBuyRelationMo.getId());
			return true;
		} else {
			_log.info("邀请关系没有符合匹配规则的订单详情");
			return false;
		}
	}

	@Override
	/**
	 * 根据匹配差两人的规则匹配购买关系
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationByFive(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差两人的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取订单购买关系
		OrdBuyRelationMo relationMo = new OrdBuyRelationMo();
		relationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(relationMo);
		_log.info("获取到的购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		// 获取用户购买该产品还有两个名额的详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("id", downLineDetailId);
		map.put("onlineId", onlineId);
		map.put("buyPrice", buyPrice);
		map.put("returnState", (byte) 0);
		map.put("commissionSlot", (byte) 2);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(map);
		if (orderDetailResult == null) {
			_log.info("没有符合差两人匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 4);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		return true;
	}

	@Override
	/**
	 * 匹配差一人的订单详情
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean getAndUpdateBuyRelationBySix(long id, long onlineId, BigDecimal buyPrice, long downLineDetailId,
			long downLineOrderId) {
		// 获取用户购买关系
		_log.info("匹配差一人的订单详情的用户id:" + id + "onlineId:" + onlineId + "buyPrice:" + buyPrice);
		long downLineRelationId1 = 0;
		long downLineRelationId2 = 0;
		// 获取订单购买关系
		OrdBuyRelationMo downLineRelationMo = new OrdBuyRelationMo();
		downLineRelationMo.setUplineOrderDetailId(downLineDetailId);
		List<OrdBuyRelationMo> selfRelationResult = selfSvc.list(downLineRelationMo);
		_log.info("获取到的购买关系结果为:{}", selfRelationResult);
		if (selfRelationResult.size() == 1) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
		} else if (selfRelationResult.size() == 2) {
			downLineRelationId1 = selfRelationResult.get(0).getDownlineOrderDetailId();
			downLineRelationId2 = selfRelationResult.get(1).getDownlineOrderDetailId();
		}
		// 获取用户购买该产品还有两个名额的详情记录
		Map<String, Object> map = new HashMap<>();
		map.put("id", downLineDetailId);
		map.put("onlineId", onlineId);
		map.put("buyPrice", buyPrice);
		map.put("returnState", (byte) 0);
		map.put("commissionSlot", (byte) 1);
		map.put("downLineRelationId1", downLineRelationId1);
		map.put("downLineRelationId2", downLineRelationId2);
		OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getOrderDetailForBuyRelation(map);
		if (orderDetailResult == null) {
			_log.info("没有符合差一人匹配规则的订单详情");
			return false;
		}
		// 添加购买关系记录
		_log.info("在购买关系表中添加记录");
		OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
		ordBuyRelationMo.setId(_idWorker.getId());
		ordBuyRelationMo.setUplineOrderId(orderDetailResult.getOrderId());
		ordBuyRelationMo.setUplineUserId(orderDetailResult.getUserId());
		ordBuyRelationMo.setUplineOrderDetailId(orderDetailResult.getId());
		ordBuyRelationMo.setDownlineUserId(id);
		ordBuyRelationMo.setDownlineOrderDetailId(downLineDetailId);
		ordBuyRelationMo.setDownlineOrderId(downLineOrderId);
		ordBuyRelationMo.setRelationSource((byte) 5);
		_log.error("添加购买关系参数:{}", ordBuyRelationMo);
		int addBuyRelationResult = selfSvc.add(ordBuyRelationMo);
		if (addBuyRelationResult != 1) {
			_log.error("{}添加下级购买信息失败", id);
			throw new RuntimeException("生成购买关系出错");
		}
		OrdOrderDetailMo updateOrderDetailMo = new OrdOrderDetailMo();
		updateOrderDetailMo.setCommissionSlot((byte) (orderDetailResult.getCommissionSlot() - 1));
		updateOrderDetailMo.setId(orderDetailResult.getId());
		if ((orderDetailResult.getCommissionSlot() - 1) == 0) {
			updateOrderDetailMo.setCommissionState((byte) 1);
		}
		// 更新购买关系订单详情的返佣名额
		int updateOrderDetailResult = ordOrderDetailSvc.updateCommissionSlotForBuyRelation(updateOrderDetailMo);
		if (updateOrderDetailResult != 1) {
			_log.error("{}更新订单详情返佣名额失败", id);
			throw new RuntimeException("更新订单详情返现名额失败");
		}
		return true;
	}

	/**
	 * 根据orderId或许购买关系
	 */
	@Override
	public List<DetailandBuyRelationRo> getBuyRelationByOrderId(long orderId) {
		List<DetailandBuyRelationRo> result = new ArrayList<DetailandBuyRelationRo>();

		_log.info("根据orderId获取购买关系参数为： {}", orderId);
		_log.info("先查询订单详情参数为： {}", orderId);
		// 查询回来的订单详情列表
		List<OrdOrderDetailMo> detailList = _mapper.getDetailByOrderId(orderId);
		_log.info("查询订单详情的返回值： {}", detailList);
		// 根据订单详情列表中的id去获取购买关系
		OrdBuyRelationMo mo = new OrdBuyRelationMo();
		for (int i = 0; i < detailList.size(); i++) {
			// 映射当前详情的所有字段
			DetailandBuyRelationRo item = new DetailandBuyRelationRo();
			item.setId(detailList.get(i).getId());
			item.setOrderId(detailList.get(i).getOrderId());
			item.setProductId(detailList.get(i).getProductId());
			item.setOnlineTitle(detailList.get(i).getOnlineTitle());
			item.setSpecName(detailList.get(i).getSpecName());
			item.setBuyCount(detailList.get(i).getBuyCount());
			item.setBuyPrice(detailList.get(i).getBuyPrice());
			item.setCashbackAmount(detailList.get(i).getCashbackAmount());
			item.setBuyUnit(detailList.get(i).getBuyUnit());
			item.setReturnCount(detailList.get(i).getReturnCount());
			item.setReturnState(detailList.get(i).getReturnState());
			item.setCashbackCommissionSlot(detailList.get(i).getCommissionSlot());
			item.setCashbackCommissionState(detailList.get(i).getCommissionState());
			item.setSubjectType(detailList.get(i).getSubjectType());
			mo.setUplineOrderDetailId(detailList.get(i).getId());
			_log.info("查询购买关系的参数为： {}", mo);
			// 已经获取到第一条订单详情的所有购买关系
			List<OrdBuyRelationMo> list = super.list(mo);
			_log.info("查询购买关系的结果为： {}", list);
			for (int j = 0; j < list.size(); j++) {
				_log.info("当前购买关系是： {}", list.get(j));
				_log.info("当前订单详情id是： {}", detailList.get(i).getId());
				// 判断当前关系里面该订单详情是不是作为下家
				if (list.get(j).getDownlineOrderDetailId().equals(detailList.get(i).getId())) {
					_log.info("该条关系中当前订单详情是作为下家的,订单详情id是： {}", detailList.get(j).getId());
					Long uId = list.get(j).getUplineUserId();
					// 当前条购买关系的上家名字
					_log.info("开始获取上家名字id为： {}", uId);
					SucUserMo uUserName = sucUserSvc.getById(uId);
					_log.info("获取上家的结果为： {}", uUserName);
					item.setUplineRelationSource(list.get(j).getRelationSource());
					if (uUserName != null) {
						item.setUplineUserName(uUserName.getWxNickname());
					}
				} else {
					Long dId = list.get(j).getDownlineUserId();
					// 当前条购买关系的下家名字
					_log.info("开始获取下家名字id为： {}", dId);
					SucUserMo dUserName = sucUserSvc.getById(dId);
					_log.info("获取下家的结果为： {}", dUserName);
					if (item.getDownlineUserName1() == null) {
						_log.info("设置第一个下家名字： {}", dUserName);
						item.setDownlineRelationSource1(list.get(j).getRelationSource());
						if (dUserName != null) {
							item.setDownlineUserName1(dUserName.getWxNickname());
						}
					} else {
						_log.info("设置第二个下家名字： {}", dUserName);
						item.setDownlineRelationSource2(list.get(j).getRelationSource());
						if (dUserName != null) {
							item.setDownlineUserName2(dUserName.getWxNickname());
						}
					}
				}

			}
			result.add(item);
		}
		return result;

	}
}
