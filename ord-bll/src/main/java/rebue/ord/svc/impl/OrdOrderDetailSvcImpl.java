package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.ord.mapper.OrdOrderDetailMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.suc.mo.SucUserMo;
import rebue.suc.svr.feign.SucUserSvc;

/**
 * 订单详情
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
public class OrdOrderDetailSvcImpl extends MybatisBaseSvcImpl<OrdOrderDetailMo, java.lang.Long, OrdOrderDetailMapper> implements OrdOrderDetailSvc {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdOrderDetailSvcImpl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(final OrdOrderDetailMo mo) {
        _log.info("添加订单详情");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    /**
     */
    @Resource
    private SucUserSvc        sucUserSvc;

    @Resource
    private OrdBuyRelationSvc selfSvc;

    /**
     * 修改订单详情的退货情况(根据订单详情ID、已退货数量、旧的返现金总额，修改退货总数、返现金总额以及退货状态)
     *
     * @param returnTotal
     *            退货总数
     * @param newCashbackTotal
     *            新的返现金总额
     * @param returnState
     *            退货状态
     * @param whereDetailId
     *            where-订单详情ID
     * @param whereReturnedCount
     *            where-之前的已退货数量
     * @param whereOldCashbackTotal
     *            where-退货之前的返现金总额
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturn(final Integer returnTotal, final BigDecimal newCashbackTotal, final Byte returnState, final Long whereDetailId, final Integer whereReturnedCount,
            final BigDecimal whereOldCashbackTotal) {
        _log.info("修改订单详情的退货情况: 退货总数-{}, 新的返现金总额-{}, 退货状态-{}, where-订单详情ID-{}, where-之前的已退货数量-{}, where-退货之前的返现金总额-{}", returnTotal, newCashbackTotal, returnState, whereDetailId,
                whereReturnedCount, whereOldCashbackTotal);
        return _mapper.updateReturn(returnTotal, newCashbackTotal, returnState, whereDetailId, whereReturnedCount, whereOldCashbackTotal);
    }
    
    /**
     * 修改订单详情实际金额
     * @param id 详情id
     * @param newActualAmount 新实际金额
     * @param oldActualAmount 旧实际金额
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyActualAmount(Long id, BigDecimal newActualAmount, BigDecimal oldActualAmount) {
    	_log.info("修改订单详情实际金额的参数为：详情id-{}，新的实际金额-{}，旧的实际金额-{}", id, newActualAmount, oldActualAmount);
    	return _mapper.updateActualAmount(id, newActualAmount, oldActualAmount);
    }

    /**
     * 根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     *
     * @param id
     * @param returnState
     * @return
     * @date 2018年5月8日 上午10:59:02
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturnStateById(final long id, final byte returnState) {
        return _mapper.modifyReturnStateById(returnState, id);
    }

    /**
     * 用户匹配自己购买关系，获取用户还有两个匹配名额的订单详情
     */
    @Override
    public OrdOrderDetailMo getOrderDetailForBuyRelation(final Map<String, Object> map) {
        return _mapper.getOrderDetailForBuyRelation(map);
    }

    /**
     * 根据OrderId获取订单详情列表
     */
    @Override
    public List<OrdOrderDetailMo> listByOrderId(final Long orderId) {
        _log.info("根据OrderId获取订单详情列表", orderId);
        final OrdOrderDetailMo conditions = new OrdOrderDetailMo();
        conditions.setOrderId(orderId);
        return _mapper.selectSelective(conditions);
    }

    @Override
    public int updateCommissionSlotForBuyRelation(final OrdOrderDetailMo mo) {
        return _mapper.updateCommissionSlotForBuyRelation(mo);
    }

    @Override
    public int updateCashbackSlot(final OrdOrderDetailMo mo) {
        return _mapper.updateCashbackSlot(mo);
    }

    @Override
    public OrdOrderDetailMo getAndUpdateBuyRelationByFour(final Map<String, Object> map) {
        return _mapper.getAndUpdateBuyRelationByFour(map);
    }

    @Override
    public OrdOrderDetailMo getAndUpdateBuyRelationByInvite(final Map<String, Object> map) {
        return _mapper.getAndUpdateBuyRelationByInvite(map);
    }

    @Override
    public OrdOrderDetailMo getOrderDetailForOneCommissonSlot(final Map<String, Object> map) {
        return _mapper.getOrderDetailForOneCommissonSlot(map);
    }

    @Override
    public List<DetailandBuyRelationRo> listBuyRelationByOrderId(final Long orderId) {
        final List<DetailandBuyRelationRo> result = new ArrayList<>();
        _log.info("根据orderId获取购买关系参数为： {}", orderId);
        _log.info("先查询订单详情参数为： {}", orderId);
        // 查询回来的订单详情列表
        final List<OrdOrderDetailMo> detailList = _mapper.getDetailByOrderId(orderId);
        _log.info("查询订单详情的返回值： {}", detailList);
        // 根据订单详情列表中的id去获取购买关系
        final OrdBuyRelationMo uPmo = new OrdBuyRelationMo();
        final OrdBuyRelationMo dWmo = new OrdBuyRelationMo();
        for (int i = 0; i < detailList.size(); i++) {
            // 映射当前详情的所有字段
            final DetailandBuyRelationRo item = new DetailandBuyRelationRo();
            item.setIsDeliver(detailList.get(i).getIsDelivered());
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
            uPmo.setUplineOrderDetailId(detailList.get(i).getId());
            _log.info("当前下家关系的的参数为： {}", uPmo);
            final List<OrdBuyRelationMo> Uplist = selfSvc.list(uPmo);
            _log.info("查询下家关系的结果为： {}", Uplist);
            for (int j = 0; j < Uplist.size(); j++) {
                final Long dId = Uplist.get(j).getDownlineUserId();
                // 当前条购买关系的下家名字
                _log.info("开始获取下家名字id为： {}", dId);
                final SucUserMo dUserName = sucUserSvc.getById(dId);
                _log.info("获取下家的结果为： {}", dUserName);
                if (item.getDownlineUserName1() == null) {
                    _log.info("设置第一个下家名字： {}", dUserName);
                    item.setDownlineRelationSource1(Uplist.get(j).getRelationSource());
                    item.setDownlineIsSignIn1(Uplist.get(j).getIsSignIn());
                    if (dUserName != null) {
                        item.setDownlineUserName1(dUserName.getWxNickname());
                    }
                } else {
                    _log.info("设置第二个下家名字： {}", dUserName);
                    item.setDownlineRelationSource2(Uplist.get(j).getRelationSource());
                    item.setDownlineIsSignIn2(Uplist.get(j).getIsSignIn());
                    if (dUserName != null) {
                        item.setDownlineUserName2(dUserName.getWxNickname());
                    }
                }
            }
            dWmo.setDownlineOrderDetailId(detailList.get(i).getId());
            _log.info("当前订单详情上家关系的参数为： {}", dWmo);
            // 已经获取到第一条订单详情的所有购买关系
            final List<OrdBuyRelationMo> dwList = selfSvc.list(dWmo);
            _log.info("当前订单详情上家关系的结果为： {}", dwList);
            for (int j = 0; j < dwList.size(); j++) {
                final Long uId = dwList.get(0).getUplineUserId();
                // 当前条购买关系的上家名字
                _log.info("开始获取上家名字id为： {}", uId);
                final SucUserMo uUserName = sucUserSvc.getById(uId);
                _log.info("获取上家的结果为： {}", uUserName);
                item.setUplineRelationSource(dwList.get(0).getRelationSource());
                item.setUplineIsSignIn(dwList.get(0).getIsSignIn());
                if (uUserName != null) {
                    item.setUplineUserName(uUserName.getWxNickname());
                }
            }
            result.add(item);
        }
        return result;
    }

    /**
     * 得到买家已下单指定上线规格商品的数量(以此来限制买家购买)
     * 
     * @param userId
     *            购买用户的用户ID
     * @param onlineSpecId
     *            上线规格ID
     */
    @Override
    public int getBuyerOrderedCount(final Long userId, final Long onlineSpecId) {
        _log.info("得到买家已下单指定上线规格商品的数量(以此来限制买家购买): userId-{} onlineSpecId-{}", userId, onlineSpecId);
        return _mapper.getBuyerOrderedCount(userId, onlineSpecId);
    }

    /**
     * 设置订单详情已结算返现金给买家
     */
    @Override
    public void settleBuyer(final Long orderDetailId) {
        _log.info("设置订单详情已结算返现金给买家: orderDetailId-{}", orderDetailId);
        final OrdOrderDetailMo mo = new OrdOrderDetailMo();
        mo.setId(orderDetailId);
        mo.setIsSettleBuyer(true);
        _mapper.updateByPrimaryKeySelective(mo);
    }
    /**
     * 根据订单修改订单发货状态
     */
	@Override
	public int modifyIsDeliverByOrderId(long orderId) {
		return _mapper.modifyIsDeliverByOrderId(orderId);
	}

}
