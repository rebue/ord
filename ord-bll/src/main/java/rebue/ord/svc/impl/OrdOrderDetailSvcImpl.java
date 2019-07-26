package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import damai.pnt.dic.PointLogTypeDic;
import rebue.ibr.mo.IbrBuyRelationMo;
import rebue.ibr.svr.feign.IbrBuyRelationSvc;
import rebue.ibr.svr.feign.IbrInviteRelationSvc;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.onl.svr.feign.OnlOnlineSpecSvc;
import rebue.ord.dao.OrdOrderDetailDao;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.jo.OrdOrderDetailJo;
import rebue.ord.mapper.OrdOrderDetailMapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.ro.ShiftOrderRo;
import rebue.ord.ro.WaitingBuyPointByUserIdListRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.ModifyInviteIdTo;
import rebue.ord.to.UpdateOrgTo;
import rebue.pnt.svr.feign.PntPointSvc;
import rebue.pnt.to.AddPointTradeTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.BaseSvcImpl;
import rebue.suc.mo.SucUserMo;
import rebue.suc.ro.SucOrgRo;
import rebue.suc.svr.feign.SucOrgSvc;
import rebue.suc.svr.feign.SucUserSvc;

/**
 * 订单详情
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
public class OrdOrderDetailSvcImpl
        extends BaseSvcImpl<java.lang.Long, OrdOrderDetailJo, OrdOrderDetailDao, OrdOrderDetailMo, OrdOrderDetailMapper>
        implements OrdOrderDetailSvc {

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

    @Resource
    private SucUserSvc        sucUserSvc;
    @Resource
    private IbrBuyRelationSvc ibrBuyRelationSvc;
    @Resource
    private SucOrgSvc         sucOrgSvc;
    @Resource
    private OrdTaskSvc        ordTaskSvc;
    @Resource
    private OrdOrderSvc       ordOrderSvc;
    @Resource
    private OnlOnlineSpecSvc  onlOnlineSpecSvc;
    @Resource
    private PntPointSvc       pntPointSvc;

    @Resource
    private OnlOnlinePicSvc onlOnlinePicSvc;

    @Resource
    private IbrInviteRelationSvc ibrInviteRelationSvc;

    @Resource
    private KdiSvc kdiSvc;

    @Resource
    private Mapper dozerMapper;

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
    public int modifyReturn(final Integer returnTotal, final BigDecimal newCashbackTotal, final Byte returnState,
            final Long whereDetailId, final Integer whereReturnedCount, final BigDecimal whereOldCashbackTotal) {
        _log.info("修改订单详情的退货情况: 退货总数-{}, 新的返现金总额-{}, 退货状态-{}, where-订单详情ID-{}, where-之前的已退货数量-{}, where-退货之前的返现金总额-{}",
                returnTotal, newCashbackTotal, returnState, whereDetailId, whereReturnedCount, whereOldCashbackTotal);
        return _mapper.updateReturn(returnTotal, newCashbackTotal, returnState, whereDetailId, whereReturnedCount,
                whereOldCashbackTotal);
    }

    /**
     * 修改订单详情实际金额和退货状态
     *
     * @param id
     * @param newActualAmount
     * @param oldActualAmount
     * @param returnState
     * @param returnedState
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyActualAmountANDReturnState(final Long id, final BigDecimal newActualAmount,
            final BigDecimal oldActualAmount, final Byte returnState, final Byte returnedState,
            BigDecimal realBuyPointTotal) {
        _log.info("修改订单详情实际金额的参数为：详情id-{}，新的实际金额-{}，旧的实际金额-{}, 新的退货状态-{}, 旧的退货状态-{}, 新的总积分-{}", id, newActualAmount,
                oldActualAmount, returnState, returnedState, realBuyPointTotal);
        return _mapper.updateActualAmountANDReturnState(id, newActualAmount, oldActualAmount, returnState,
                returnedState, realBuyPointTotal);
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
    public int updateCashbackSlot(final OrdOrderDetailMo mo) {
        return _mapper.updateCashbackSlot(mo);
    }

    /**
     * 1:根据订单id获取所有的详情
     * 2：根据详情获取每个详情上下家的购买关系
     * 3：根据每个关系id(是详情id)获取该条详情的订单状态。
     */
    @Override
    public List<DetailandBuyRelationRo> listBuyRelationByOrderId(final Long orderId) {
        final List<DetailandBuyRelationRo> result = new ArrayList<>();
        SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        _log.info("根据orderId获取购买关系参数为： {}", orderId);
        _log.info("先查询订单详情参数为： {}", orderId);
        // 查询回来的订单详情列表
        final List<OrdOrderDetailMo> detailList = _mapper.getDetailByOrderId(orderId);
        _log.info("查询订单详情的返回值： {}", detailList);

        // 根据订单详情列表中的id去获取购买关系
        for (int i = 0; i < detailList.size(); i++) {
            DetailandBuyRelationRo item = new DetailandBuyRelationRo();
            item = dozerMapper.map(detailList.get(i), DetailandBuyRelationRo.class);
            List<Map<String, String>> relationInfo = new ArrayList<Map<String, String>>();
            // 获取当前详情供应商
            _log.info("获取当前供应商参数为： {}", detailList.get(i).getSupplierId());
            if (detailList.get(i).getSupplierId() != null) {
                final SucOrgRo sucOrgRo = sucOrgSvc.getById(detailList.get(i).getSupplierId());
                _log.info("获取当前供应商结果为： {}", sucOrgRo.getRecord());
                if (sucOrgRo != null && sucOrgRo.getRecord() != null && sucOrgRo.getRecord().getName() != null) {
                    item.setSupplierName(sucOrgRo.getRecord().getName());
                }
            }

            // 获取当前详情上家，也就是购买关系中id是本详情的关系
            _log.info("获取上家关系");
            _log.info("获取上家关系参数为 id-{}", detailList.get(i).getId());
            IbrBuyRelationMo getUplineResult = ibrBuyRelationSvc.getById(detailList.get(i).getId());
            _log.info("获取上家关系参数为 getUplineResult-{}", getUplineResult);
            if (getUplineResult != null) {
                OrdOrderDetailMo detailRo = super.getById(getUplineResult.getParentId());
                if (detailRo != null) {
                    HashMap<String, String> info = new HashMap<String, String>();
                    // 设置id让页面循环的时候有key值
                    info.put("id", detailRo.getId().toString());
                    // 设置为上家，让页面好区分
                    info.put("relation", "upLine");
                    // 设置关系来源
                    info.put("relationSource", getUplineResult.getRelationSource().toString());
                    // 设置详情退货状态
                    info.put("returnState", detailRo.getReturnState().toString());
                    // 获取用户昵称
                    _log.info("查询上家用户昵称参数为： UserId()-{}", detailRo.getUserId());
                    final SucUserMo UserResult = sucUserSvc.getById(detailRo.getUserId());
                    _log.info("查询上家用户昵称结果为： UserResult-{}", UserResult);
                    if (UserResult != null) {
                        info.put("userName", UserResult.getWxNickname() != null ? UserResult.getWxNickname()
                                : UserResult.getLoginName());
                    }
                    // 获取订单信息
                    _log.info("查询上家订单状态参数为： orderId-{}", detailRo.getOrderId());
                    OrdOrderMo orderResult = ordOrderSvc.getById(detailRo.getOrderId());
                    _log.info("查询上家订单状态结果为： orderResult-{}", orderResult);
                    if (orderResult != null) {
                        // 设置订单编号
                        info.put("orderCode", orderResult.getOrderCode());
                        // 设置订单状态
                        info.put("orderState", orderResult.getOrderState().toString());
                        // 设置签收时间
                        if (orderResult.getReceivedTime() != null) {
                            String receivedTime = formatTime.format(orderResult.getReceivedTime());
                            info.put("receivedTime", receivedTime);
                        }
                    }
                    _log.info("设置的上家信息为 uplineUserInfo-{}", info);
                    relationInfo.add(info);
                }
            }

            // 获取当前详情下家，也就是购买关系中父id是本详情的关系
            _log.info("当前下家关系的的参数为： parentId{}", detailList.get(i).getId());
            final List<IbrBuyRelationMo> getDownLineResult = ibrBuyRelationSvc.list(detailList.get(i).getId());
            _log.info("查询下家关系的结果为： {}", getDownLineResult);
            // 获取每个下家的订单信息
            for (IbrBuyRelationMo ibrBuyRelationMo : getDownLineResult) {
                _log.info("获取下家详情详细信息开始---------------------------------");
                OrdOrderDetailMo detailRo = super.getById(ibrBuyRelationMo.getId());
                HashMap<String, String> info = new HashMap<String, String>();
                if (detailRo != null) {
                    // 设置id让页面循环的时候有key值
                    info.put("id", detailRo.getId().toString());
                    // 设置为下家，让页面好区分
                    info.put("relation", "downLine");
                    // 设置关系来源，首单可能没有来源，要判断
                    if (ibrBuyRelationMo.getRelationSource() != null) {
                        info.put("relationSource", ibrBuyRelationMo.getRelationSource().toString());
                    }
                    // 设置详情退货状态
                    info.put("returnState", detailRo.getReturnState().toString());
                    // 获取用户昵称
                    _log.info("查询下家用户昵称参数为： UserId()-{}", detailRo.getUserId());
                    final SucUserMo UserResult = sucUserSvc.getById(detailRo.getUserId());
                    _log.info("查询下家用户昵称结果为： UserResult-{}", UserResult);
                    if (UserResult != null) {
                        info.put("userName", UserResult.getWxNickname() != null ? UserResult.getWxNickname()
                                : UserResult.getLoginName());
                    }
                    // 获取订单信息
                    _log.info("查询下家订单信息参数为： orderId-{}", detailRo.getOrderId());
                    OrdOrderMo orderResult = ordOrderSvc.getById(detailRo.getOrderId());
                    _log.info("查询下家订单信息结果为： orderResult-{}", orderResult);
                    if (orderResult != null) {
                        // 设置订单编码
                        info.put("orderCode", orderResult.getOrderCode());
                        // 设置订单状态
                        info.put("orderState", orderResult.getOrderState().toString());
                        // 设置签收时间
                        if (orderResult.getReceivedTime() != null) {
                            String receivedTime = formatTime.format(orderResult.getReceivedTime());
                            info.put("receivedTime", receivedTime);
                        }
                    }
                    _log.info("设置的下家信息为 uplineUserInfo-{}", info);
                    relationInfo.add(info);
                }
                _log.info("获取下家详情详细信息结束++++++++++++++++++++++++++++++++++");
            }

            item.setRelationInfo(relationInfo);
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
    public BigDecimal getBuyerOrderedCount(final Long userId, final Long onlineSpecId) {
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
    public int modifyIsDeliverByOrderId(final long orderId) {
        return _mapper.modifyIsDeliverByOrderId(orderId);
    }

    /**
     * 修改返现总额、退货数量
     *
     * @param id
     * @param oldCashbackTotal
     * @param newCashbackTotal
     * @param returnedCount
     * @param returnTotal
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturnNumAndCashbackTotal(final Long id, final BigDecimal oldCashbackTotal,
            final BigDecimal newCashbackTotal, final BigDecimal returnedCount, final BigDecimal returnTotal) {
        _log.info("修改返现总额和退货数量的参数为：id={}, oldCashbackTotal={}, newCashbackTotal={}, returnedCount={}, returnTotal={}",
                id, oldCashbackTotal, newCashbackTotal, returnedCount, returnTotal);
        return _mapper.updateReturnNumAndCashbackTotal(id, oldCashbackTotal, newCashbackTotal, returnedCount,
                returnTotal);
    }

    /**
     * 根据id修改供应商id
     *
     * @param id
     *            订单详情id
     * @param newSupplierId
     *            新供应商id
     * @param oldSupplierId
     *            旧供应商id
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateSupplierIdById(final Long id, final Long newSupplierId, final Long oldSupplierId) {
        _log.info("根据订单详情id修改供应商id的参数为：id={}, newSupplierId={}, oldSupplierId={}", id, newSupplierId, oldSupplierId);
        return _mapper.updateSupplierIdById(id, newSupplierId, oldSupplierId);
    }

    /**
     * 根据订单id修改发货组织和供应商
     */
    @Override
    public int modifyOrg(final UpdateOrgTo to) {
        final Long orderId = to.getId();
        return _mapper.modifyOrg(to.getSupplierId(), to.getDeliverOrgId(), orderId);
    }

    /**
     * 计算首单购买
     *
     * @param onlineSpecId
     *            上线规格ID
     */
    @Override
    public void calcFirstBuy(final Long onlineSpecId) {
        _log.info("计算首单购买: onlineSpecId-{}", onlineSpecId);
        _log.debug("获取首单购买的订单详情");
        final OrdOrderDetailMo orderDetailMo = _mapper.getFirstBuyDetail(onlineSpecId, ReturnStateDic.RETURNED,
                OrderStateDic.PAID);
        _log.debug("获取首单购买的订单详情结果 {}", orderDetailMo);

        if (orderDetailMo == null) {
            _log.warn("未发现有已经支付的订单详情，可能已退货: onlineSpecId-{}", onlineSpecId);
            Ro ro = onlOnlineSpecSvc.modifyIsHaveFirstOrderById(onlineSpecId, false);
            _log.info("计算首单购买修改是否已有首单的返回值为：{}", ro);
            if (ro.getResult() != ResultDic.SUCCESS) {
                throw new RuntimeException("修改是否已有首单失败");
            }
            return;
        }

        if (orderDetailMo.getPaySeq() != null && orderDetailMo.getPaySeq() == 1) {

            _log.info("首单购买已经设置正确，无需修改");
            return;
        }

        _log.debug("清除旧的首单的支付顺序标志");
        _mapper.clearPaySeqOfFirst(onlineSpecId);

        _log.debug("设置新的首单的支付顺序标志");
        _mapper.setFirstPaySeq(orderDetailMo.getId());

        Ro ro = onlOnlineSpecSvc.modifyIsHaveFirstOrderById(onlineSpecId, true);
        _log.info("计算首单购买修改是否已有首单的返回值为：{}", ro);
        if (ro.getResult() != ResultDic.SUCCESS) {
            throw new RuntimeException("修改是否已有首单失败");
        }

        _log.debug("查询该订单是否已经结算，如果已经结算就添加首单积分交易");
        _log.debug("查询该订单是否已经结算的参数为 {}", orderDetailMo.getOrderId());
        final OrdOrderMo ordOrderMo = ordOrderSvc.getById(orderDetailMo.getOrderId());
        _log.debug("查询该订单是否已经结算的返回值为 {}", ordOrderMo);
        if (ordOrderMo.getOrderState() == 5) {

            final AddPointTradeTo addPointTradeTo = new AddPointTradeTo();
            addPointTradeTo.setPointLogType((byte) PointLogTypeDic.ORDER_SETTLE_FIRST_BUY.getCode());
            addPointTradeTo.setChangedTitile("大卖网-商品首单购买奖励积分(订单已经结算却成为首单)");
            addPointTradeTo.setAccountId(ordOrderMo.getUserId());
            addPointTradeTo.setOrderDetailId(orderDetailMo.getId());
            addPointTradeTo.setOrderId(ordOrderMo.getId());
            _log.debug("成本价格{},购买数量{},退货数量{}", orderDetailMo.getCostPrice(), orderDetailMo.getBuyCount(),
                    orderDetailMo.getReturnCount());

            // 首单购买奖励积分 = 成本价 * 实际购买数量
            addPointTradeTo.setChangedPoint(orderDetailMo.getCostPrice()
                    .multiply(orderDetailMo.getBuyCount().subtract(orderDetailMo.getReturnCount())));
            _log.debug("添加一笔新的积分记录: 商品首单购买奖励积分结算买家: addPointTradeTo-{}", addPointTradeTo);
            pntPointSvc.addPointTrade(addPointTradeTo);
        }

    }

    /**
     * 根据用户id计算待入账的积分
     * 
     * @param userId
     * @return
     */
    @Override
    public BigDecimal countWaitingBuyPointByUserId(Long userId) {
        _log.info("查询用户待入账的积分的参数为：{}", userId);
        return _mapper.countWaitingBuyPointByUserId(userId);
    }

    /**
     * 获取用户待入积分
     * 
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<WaitingBuyPointByUserIdListRo> waitingBuyPointByUserIdList(Long userId, Integer pageNum,
            Integer pageSize) {
        _log.info("获取用户待入积分列表信息的参数为：userId-{}; pageNum-{}, pageSize-{}", userId, pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> _mapper.selectWaitingBuyPointByUserId(userId));
    }

    /**
     * 根据上线id修改订单详情供应商和发货组织
     */
    @Override
    public int modifyDeliverAndSupplierByOnlineid(Long supplierId, Long deliverOrgId, Long onlineId) {
        return _mapper.modifyDeliverAndSupplierByOnlineid(supplierId, deliverOrgId, onlineId);
    }

    /**
     * 补偿双倍积分
     */
    @Override
    public void compensatePoint() {
        _log.info("开始补偿双倍积分");
        List<OrdOrderDetailMo> oldPointList = _mapper.selectOldPoint();
        _log.info("补偿双倍积分获取到需要补偿的积分列表参数为：{}", String.valueOf(oldPointList));
        for (OrdOrderDetailMo ordOrderDetailMo : oldPointList) {
            System.out.println(ordOrderDetailMo);

            int updateBuyPointByIdResult = _mapper.updateBuyPointById(ordOrderDetailMo.getId(),
                    ordOrderDetailMo.getBuyPoint(), ordOrderDetailMo.getBuyPoint());
            _log.info("补偿双倍积分修改订单详情积分的返回值为：{}", updateBuyPointByIdResult);
            if (updateBuyPointByIdResult != 1) {
                _log.error("补偿双倍积分修改订单详情积分出现错误，请求的参数为：{}", ordOrderDetailMo);
                return;
            }

            AddPointTradeTo to = new AddPointTradeTo();
            to.setAccountId(ordOrderDetailMo.getUserId());
            to.setPointLogType((byte) PointLogTypeDic.RECHARGE.getCode());
            to.setChangedTitile("大卖网络-积分充值");
            to.setChangedDetail(
                    "补偿购买商品：" + ordOrderDetailMo.getOnlineTitle() + ",规格为：" + ordOrderDetailMo.getSpecName() + "的积分");
            to.setOrderId(ordOrderDetailMo.getOrderId());
            to.setOrderDetailId(ordOrderDetailMo.getId());
            to.setChangedPoint(ordOrderDetailMo.getBuyPoint());
            pntPointSvc.addPointTrade(to);
        }
    }

    @Override
    public int updateIsDeliver(Long OrderId, Long onlineId, Long onlineSpecId) {
        _log.info("根据订单id上线id规格id修改订单详情的发货状态参数为：{},{},{}", OrderId, onlineId, onlineSpecId);
        return _mapper.updateIsDeliver(OrderId, onlineId, onlineSpecId);
    }

    @Override
    public int modifyUserIdByOrderId(Long orderId, Long userId) {
        _log.info("根据订单id修改用户id的参数为：orderId-{},userId-{}", orderId, userId);
        return _mapper.updateUserIdByOrderId(orderId, userId);
    }

    /**
     * 根据订单详情id修改邀请人id
     */
    @Override
    public ShiftOrderRo modifyInviteId(List<ModifyInviteIdTo> modifyInviteIdList) {
        ShiftOrderRo ro = new ShiftOrderRo();
        for (ModifyInviteIdTo modifyInviteIdTo : modifyInviteIdList) {
            if (_mapper.updateInviteIdById(modifyInviteIdTo.getId(), modifyInviteIdTo.getInviterId()) != 1) {
                _log.error("根据订单详情id修改邀请人失败为 id-{},inviteId-{}", modifyInviteIdTo.getId(),
                        modifyInviteIdTo.getInviterId());
                throw new RuntimeException("修改失败");
            }
        }
        ro.setMsg("修改成功");
        ro.setResult(ResultDic.SUCCESS);
        return ro;
    }

}
