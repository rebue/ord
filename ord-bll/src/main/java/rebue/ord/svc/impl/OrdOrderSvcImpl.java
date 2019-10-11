package rebue.ord.svc.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.afc.msg.PayDoneMsg;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.to.RefundApprovedTo;
import rebue.afc.to.RefundImmediateTo;
import rebue.ibr.dic.TaskTypeDic;
import rebue.ibr.mo.IbrBuyRelationMo;
import rebue.ibr.mo.IbrBuyRelationTaskMo;
import rebue.ibr.mo.IbrInviteRelationMo;
import rebue.ibr.svr.feign.IbrBuyRelationSvc;
import rebue.ibr.svr.feign.IbrBuyRelationTaskSvc;
import rebue.ibr.svr.feign.IbrInviteRelationSvc;
import rebue.kdi.ro.EOrderRo;
import rebue.kdi.ro.KdiLogisticRo;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.kdi.to.AddKdiLogisticTo;
import rebue.kdi.to.EOrderTo;
import rebue.onl.dic.OnlineSubjectTypeDic;
import rebue.onl.mo.OnlOnlineMo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.mo.OnlOnlineSpecMo;
import rebue.onl.svr.feign.OnlCartSvc;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.onl.svr.feign.OnlOnlineSpecSvc;
import rebue.onl.svr.feign.OnlOnlineSvc;
import rebue.onl.to.UpdateOnlineAfterOrderTo;
import rebue.onl.to.UpdateOnlineSpecAfterOrderTo;
import rebue.ord.dic.CancellationOfOrderDic;
import rebue.ord.dic.CommissionStateDic;
import rebue.ord.dic.ModifyOrderRealMoneyDic;
import rebue.ord.dic.OrderSignInDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.dic.SetUpExpressCompanyDic;
import rebue.ord.dic.ShipmentConfirmationDic;
import rebue.ord.jo.OrdOrderJo;
import rebue.ord.mapper.OrdOrderMapper;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.mo.OrdOrderDetailDeliverMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.ro.BulkShipmentRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.DetailAndRelationRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdBuyRelationRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrdSettleRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.OrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShiftOrderRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.ord.svc.OrdOrderDetailDeliverSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.BulkShipmentTo;
import rebue.ord.to.CancelDeliveryTo;
import rebue.ord.to.DeliverAndGetTraceTo;
import rebue.ord.to.ListOrderTo;
import rebue.ord.to.OrderDetailTo;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.OrderTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.ord.to.UpdateOrgTo;
import rebue.prd.mo.PrdProductMo;
import rebue.prd.mo.PrdProductSpecMo;
import rebue.prd.svr.feign.PrdProductSpecSvc;
import rebue.prd.svr.feign.PrdProductSvc;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.suc.co.StaticUserId;
import rebue.suc.mo.SucOrgMo;
import rebue.suc.mo.SucUserMo;
import rebue.suc.svr.feign.SucOrgSvc;
import rebue.suc.svr.feign.SucUserSvc;

/**
 * 订单信息
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
public class OrdOrderSvcImpl extends MybatisBaseSvcImpl<OrdOrderMo, java.lang.Long, OrdOrderMapper>
        implements OrdOrderSvc {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdOrderSvcImpl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(final OrdOrderMo mo) {
        _log.info("添加订单信息");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    /**
     * 执行取消用户订单时间
     */
    @Value("${ord.testSupplierOrgId:-1}")
    private Long testSupplierOrgId;

    /**
     * 执行取消用户订单时间
     */
    @Value("${ord.cancel-order-time}")
    private int cancelOrderTime;

    /**
     * 执行用户订单签收时间
     */
    @Value("${ord.signin-order-time}")
    private int signinOrderTime;

    @Resource
    private OrdAddrSvc ordAddrSvc;

    @Resource
    private OrdOrderDetailSvc orderDetailSvc;

    @Resource
    private OrdReturnSvc returnSvc;

    @Resource
    private OrdTaskSvc ordTaskSvc;

    @Resource
    private OnlOnlineSvc onlineSvc;

    @Resource
    private OnlOnlineSpecSvc onlOnlineSpecSvc;

    @Resource
    private OnlCartSvc onlCartSvc;

    @Resource
    private KdiSvc kdiSvc;

    @Resource
    private OnlOnlinePicSvc onlOnlinePicSvc;

    @Resource
    private AfcRefundSvc refundSvc;

    @Resource
    private SucUserSvc sucUserSvc;

    @Resource
    private SucOrgSvc sucOrgSvc;

    @Resource
    private OrdOrderSvc thisSvc;

    @Resource
    private OrdSettleTaskSvc ordSettleTaskSvc;

    @Resource
    private AfcRefundSvc afcRefundSvc;

    @Resource
    private PrdProductSvc prdProductSvc;

    @Resource
    private PrdProductSpecSvc prdProductSpecSvc;

    @Resource
    private OrdOrderDetailDeliverSvc ordOrderDetailDeliverSvc;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private IbrBuyRelationSvc ibrBuyRelationSvc;

    @Resource
    private IbrInviteRelationSvc ibrInviteRelationSvc;

    @Resource
    private IbrBuyRelationTaskSvc ibrBuyRelationTaskSvc;

    /**
     * 检查订单是否可结算 1. 订单必须存在 2. 订单必须处于签收状态 3. 订单必须已经记录签收时间 4. 已经超过订单启动结算的时间 5.
     * 如果订单还有退货中的申请未处理完成，不能结算
     */
    @Override
    public Boolean isSettleableOrder(final OrdOrderMo order) {
        _log.debug("订单信息: {}", order);
        if (OrderStateDic.SIGNED.getCode() > order.getOrderState()) {
            final String msg = "订单未签收，不能添加结算任务";
            _log.error("{}: 订单状态-{} 订单信息-{}", msg, OrderStateDic.getItem(order.getOrderState()).name(), order);
            return false;
        }
        if (order.getReceivedTime() == null) {
            final String msg = "订单没有记录签收时间";
            _log.error("{}: 订单信息-{}", msg, order);
            return false;
        }
        // 判断订单是否有订单详情在退货中
        if (returnSvc.hasReturningInOrder(order.getId())) {
            final String msg = "订单还有退货中的申请未处理完成，不能结算";
            _log.error(msg);
            return false;
        }
        _log.debug("订单信息经过检查，可以进行结算: {}", order);
        return true;
    }

    /**
     * 下订单
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderRo order(final OrderTo to) {
        final OrderRo ro = new OrderRo();
        _log.info("用户下单的参数为：{}", to);
        _log.debug("检查参数的正确性");
        if (to.getDetails() == null || to.getDetails().isEmpty()) {
            final String msg = "参数错误";
            _log.error("{}:{}", msg, "没有传入用户收货地址/订单详情");
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg(msg);
            return ro;
        }
        // 如果用户ID为空可能是线下支付，设置常量用户id
        if (to.getUserId() == null) {
            _log.info("检查到用户id为空，设置静态用户Id，请确认确实需要设置静态用户Id!!!!!!!!!!!!!!!!!!!!!!!");
            _log.info("检查到用户id为空，设置静态用户Id，请确认确实需要设置静态用户Id!!!!!!!!!!!!!!!!!!!!!!!");
            _log.info("检查到用户id为空，设置静态用户Id，请确认确实需要设置静态用户Id!!!!!!!!!!!!!!!!!!!!!!!");
            to.setUserId(StaticUserId.USER_ID);
            to.setIsTester(false);
        }

        _log.debug("参数正确");
        // 上线列表(获取过的上线信息放进这里，避免重复获取)
        final Map<Long, OnlOnlineMo> onlines = new LinkedHashMap<>();
        // 上线组织列表(不同上线组织的订单详情需要拆单)
        final Map<Long, List<OrdOrderDetailMo>> onlineOrgs = new LinkedHashMap<>();
        // 要更新的上线规格列表
        final List<UpdateOnlineSpecAfterOrderTo> specList = new ArrayList<>();

        _log.debug("遍历订单详情");
        for (final OrderDetailTo orderDetailTo : to.getDetails()) {
            if (orderDetailTo.getBuyCount() == null || orderDetailTo.getBuyCount().compareTo(BigDecimal.ZERO) <= 0) {
                final String msg = "参数错误";
                _log.error("{}: {}", msg, "购买数量不能为空或小于等于0 " + orderDetailTo);
                ro.setResult(ResultDic.PARAM_ERROR);
                ro.setMsg(msg);
                return ro;
            }

            // 添加的订单详情
            OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setIsDelivered(to.getIsNowReceived());
            if (orderDetailTo.getBuyPrice() != null) {
                orderDetailMo.setBuyPrice(orderDetailTo.getBuyPrice());
            }
            orderDetailMo.setBuyCount(orderDetailTo.getBuyCount());

            if (!StringUtils.isBlank(orderDetailTo.getBuyUnit())) {
                orderDetailMo.setBuyUnit(orderDetailTo.getBuyUnit());
            }

            _log.debug("根据产品ID获取产品信息");
            // 根据产品ID获取产品信息(在不是临时商品的情况下才去获取)
            if (!orderDetailTo.getIsTempGood()) {
                // 如果是上线商品，则需要根据上线规格id获取该条上线规格记录，再获取产品的相应信息,否则可以直接使用产品规格Id直接获取
                if (orderDetailTo.getOnlineSpecId() != null) {
                    OnlOnlineSpecMo onlineSpacResult = onlOnlineSpecSvc.getById(orderDetailTo.getOnlineSpecId());
                    _log.info("获取产品规格信息的参数为-{}", onlineSpacResult.getProductSpecId());
                    PrdProductSpecMo prdProductSeocResult = prdProductSpecSvc
                            .getById(onlineSpacResult.getProductSpecId());
                    _log.info("获取产品规格信息的结果为-{}", prdProductSeocResult);
                    orderDetailMo.setSpecName(prdProductSeocResult.getName());
                    orderDetailMo.setBuyUnit(prdProductSeocResult.getUnit());
                    _log.info("获取产品信息的参数为-{}", prdProductSeocResult.getProductId());
                    PrdProductMo prdProductResult = prdProductSvc.getById(prdProductSeocResult.getProductId());
                    _log.info("获取产品信息的结果为-{}", prdProductResult);
                    orderDetailMo.setProductId(orderDetailTo.getProductId());
                    orderDetailMo.setProductSpecId(orderDetailTo.getProductSpecId());

                } else {
                    _log.info("获取产品信息的参数为-{}", orderDetailTo.getProductId());
                    PrdProductMo prdProductResult = prdProductSvc.getById(orderDetailTo.getProductId());
                    _log.info("获取产品信息的结果为-{}", prdProductResult);
                    orderDetailMo.setProductId(orderDetailTo.getProductId());
                    orderDetailMo.setProductSpecId(orderDetailTo.getProductSpecId());
                    _log.info("获取产品规格信息的参数为-{}", orderDetailTo.getProductSpecId());
                    PrdProductSpecMo prdProductSeocResult = prdProductSpecSvc.getById(orderDetailTo.getProductSpecId());
                    _log.info("获取产品规格信息的结果为-{}", prdProductSeocResult);
                    orderDetailMo.setSpecName(prdProductSeocResult.getName());
                    orderDetailMo.setBuyUnit(prdProductSeocResult.getUnit());
                }

            }

            _log.debug("根据上线ID获取上线信息");
            if (orderDetailTo.getOnlineId() == null || orderDetailTo.getOnlineSpecId() == null) {
                if (!to.getIsNowReceived()) {
                    final String msg = "参数错误";
                    _log.error("{}: {}", msg, "上线ID/上线规格ID不能为空 " + orderDetailTo);
                    ro.setResult(ResultDic.PARAM_ERROR);
                    ro.setMsg(msg);
                    return ro;
                }
                // 临时商品或未上架产品
                List<OrdOrderDetailMo> orderDetails = new LinkedList<>();
                if (orderDetailTo.getIsTempGood()) {
                    onlineOrgs.put(1L, orderDetails); // 只是为了在下面使用上线组织拆单的时候临时商品和上线商品会被拆开
                } else {
                    onlineOrgs.put(2L, orderDetails);
                }
                orderDetailMo.setActualAmount(orderDetailMo.getBuyPrice().multiply(orderDetailMo.getBuyCount()));
                orderDetailMo.setCashbackAmount(new BigDecimal("0"));
                orderDetailMo.setCashbackTotal(new BigDecimal("0"));
                if (orderDetailTo.getIsTempGood()) {
                    orderDetailMo.setSpecName(orderDetailTo.getGoodName());
                }
                orderDetails.add(orderDetailMo);

            } else {
                _log.debug("获取上线信息");
                // 如果已经获取过上线信息，从Map中获取就可避免重复获取，减轻数据库负担
                OnlOnlineMo onlineMo = onlines.get(orderDetailTo.getOnlineId());
                if (onlineMo == null) {
                    onlineMo = onlineSvc.getById(orderDetailTo.getOnlineId());
                    if (onlineMo == null) {
                        final String msg = "找不到上线的信息";
                        _log.error("{}: onlineId-{}", msg, orderDetailTo.getOnlineId());
                        ro.setResult(ResultDic.PARAM_ERROR);
                        ro.setMsg(msg);
                        return ro;
                    }
                    _log.info("获取上线的信息为：{}", onlineMo);
                    // 将获取到的上线信息放入Map中
                    onlines.put(orderDetailTo.getOnlineId(), onlineMo);
                }

                _log.debug("根据上线规格ID获取上线规格信息");
                final OnlOnlineSpecMo onlineSpecMo = onlOnlineSpecSvc.getById(orderDetailTo.getOnlineSpecId());
                if (onlineSpecMo == null) {
                    final String msg = "找不到上线规格的信息";
                    _log.error("{}: onlineSpecId-{}", msg, orderDetailTo.getOnlineSpecId());
                    ro.setResult(ResultDic.PARAM_ERROR);
                    ro.setMsg(msg);
                    return ro;
                }
                _log.info("获取上线规格的信息为：{}", onlineSpecMo);

                _log.info("检查购买数量是否超过库存数量");
                // XXX 检查购买数量是否超过库存数量：判断上线数量-销售数量是否小于购买数量-1
                if (onlineSpecMo.getCurrentOnlineCount().subtract(onlineSpecMo.getSaleCount())
                        .compareTo(orderDetailTo.getBuyCount()) == -1) {
                    if (!to.getIsNowReceived()) {
                        final String msg = "商品库存不足";
                        _log.error("{}: onlineSpecMo-{}, 购买数量-{}", msg, onlineSpecMo, orderDetailTo.getBuyCount());
                        ro.setResult(ResultDic.FAIL);
                        ro.setMsg(msg);
                        return ro;
                    } else {
                        // TODO 临时增加库存(增加数量为CurrentOnlineCount-SaleCount-BuyCount)
                    }
                }
                _log.info("检查是否限制购买");
                // 如果是限制购买的商品，同一用户的所有购买数量不能超过限制的数量
                if (onlineSpecMo.getLimitCount().compareTo(BigDecimal.ZERO) == 1) {
                    // 计算更新后的库存
                    final BigDecimal buyerOrderedCount = orderDetailSvc.getBuyerOrderedCount(to.getUserId(),
                            orderDetailTo.getOnlineSpecId());
                    if (buyerOrderedCount.add(orderDetailTo.getBuyCount())
                            .compareTo(onlineSpecMo.getLimitCount()) == 1) {
                        final String msg = "已超过限购数量，不能再购买";
                        _log.error("{}: userId-{} onlineSpecId-{}", msg, to.getUserId(),
                                orderDetailTo.getOnlineSpecId());
                        ro.setResult(ResultDic.FAIL);
                        ro.setMsg(msg);
                        return ro;
                    }
                }

                orderDetailMo.setOnlineId(orderDetailTo.getOnlineId());
                orderDetailMo.setOnlineSpecId(orderDetailTo.getOnlineSpecId());
                orderDetailMo.setOnlineTitle(onlineMo.getOnlineTitle());
                orderDetailMo.setSpecName(onlineSpecMo.getOnlineSpec());
                orderDetailMo.setBuyPrice(onlineSpecMo.getSalePrice());
                orderDetailMo.setBuyUnit(onlineSpecMo.getSaleUnit());
                orderDetailMo.setCostPrice(onlineSpecMo.getCostPrice());
                // 计算实际价格=单价*数量
                orderDetailMo.setActualAmount(orderDetailMo.getBuyPrice().multiply(orderDetailMo.getBuyCount()));
                if (onlineSpecMo.getCashbackAmount() != null) {
                    orderDetailMo.setCashbackAmount(onlineSpecMo.getCashbackAmount());
                    orderDetailMo
                            .setCashbackTotal(orderDetailMo.getBuyCount().multiply(onlineSpecMo.getCashbackAmount()));
                }
                if (onlineSpecMo.getBuyPoint() != null) {
                    orderDetailMo.setBuyPoint(onlineSpecMo.getBuyPoint());
                    orderDetailMo.setBuyPointTotal(onlineSpecMo.getBuyPoint().multiply(orderDetailMo.getBuyCount()));
                }

                // 判断是否是测试用户
                if (to.getIsTester()) {
                    orderDetailMo.setSupplierId(testSupplierOrgId);
                    orderDetailMo.setDeliverOrgId(testSupplierOrgId);
                } else {
                    orderDetailMo.setSupplierId(onlineMo.getSupplierId());
                    orderDetailMo.setDeliverOrgId(onlineMo.getDeliverOrgId());
                }

                orderDetailMo.setSubjectType(onlineMo.getSubjectType());
                // 添加订单详情到上线组织列表中(根据上线组织拆分订单详情)
                List<OrdOrderDetailMo> orderDetails = onlineOrgs.get(onlineMo.getOnlineOrgId());
                if (orderDetails == null) {
                    orderDetails = new LinkedList<>();
                    onlineOrgs.put(onlineMo.getOnlineOrgId(), orderDetails);
                }

                if (OnlineSubjectTypeDic.BACK_COMMISSION.getCode() == orderDetailMo.getSubjectType()) {
                    if (orderDetailTo.getInviteId() != null) {
                        orderDetailMo.setInviteId(orderDetailTo.getInviteId());
                    }
                    orderDetailMo.setBuyCount(BigDecimal.ONE);
                    orderDetailMo.setCommissionSlot((byte) 2);
                    orderDetailMo.setCommissionState((byte) CommissionStateDic.MATCHING.getCode());
                    orderDetailMo.setCashbackAmount(BigDecimal.ZERO);
                    orderDetailMo.setCashbackTotal(BigDecimal.ZERO);
                    orderDetailMo.setActualAmount(orderDetailMo.getBuyPrice());
                    orderDetailMo.setBuyPointTotal(orderDetailMo.getBuyPoint());
                    // 如果订单购买数量有小数则不拆单
//                    if (BigDecimal.valueOf(orderDetailTo.getBuyCount().intValue())
//                            .compareTo(orderDetailTo.getBuyCount()) == 0) {
//                  _log.info("订单购买数量没有小数，开始拆单");
                    _log.info("开始拆单");
                    for (int i = 0; i < orderDetailTo.getBuyCount().intValue(); i++) {
                        orderDetails.add(orderDetailMo);
                        orderDetailMo = dozerMapper.map(orderDetailMo, OrdOrderDetailMo.class);
                    }
//                    } else {
//                        _log.info("订单购买数量有小数，不拆单");
//                        orderDetails.add(orderDetailMo);
//                        orderDetailMo = dozerMapper.map(orderDetailMo, OrdOrderDetailMo.class);
//                    }
                } else {
                    orderDetails.add(orderDetailMo);
                }

                // 添加要更新的上线规格信息
                final UpdateOnlineSpecAfterOrderTo specTo = new UpdateOnlineSpecAfterOrderTo();
                specTo.setOnlineId(orderDetailTo.getOnlineId());
                specTo.setOnlineSpecId(orderDetailTo.getOnlineSpecId());
                specTo.setBuyCount(orderDetailTo.getBuyCount());
                specTo.setCartId(orderDetailTo.getCartId());
                specList.add(specTo);
            }

        }
        OrdAddrMo addrMo = new OrdAddrMo();
        // 当场签收不需要地址
        if (to.getAddrId() != null) {
            _log.debug("通过地址ID获取地址详细信息");
            addrMo = ordAddrSvc.getById(to.getAddrId());
            if (addrMo == null) {
                // 不是当场签收的商品必须要填收货地址
                if (to.getIsNowReceived() != true) {
                    final String msg = "找不到下单的收货地址信息";
                    _log.error("{}: addrId-{}", msg, to.getAddrId());
                    ro.setResult(ResultDic.PARAM_ERROR);
                    ro.setMsg(msg);
                    return ro;
                }
            }
            _log.info("获取用户收货地址信息为：{}", addrMo);
        }

        final Date now = new Date();
        // 支付订单ID
        final Long payOrderId = _idWorker.getId();
        // 根据上线组织拆单
        for (final Entry<Long, List<OrdOrderDetailMo>> onlineOrg : onlineOrgs.entrySet()) {
            final OrdOrderMo orderMo = new OrdOrderMo();
            orderMo.setId(_idWorker.getId());
            if (to.getIsNowReceived()) {
                orderMo.setOrderState((byte) OrderStateDic.SIGNED.getCode());
                orderMo.setPayTime(new Date());
                orderMo.setSendTime(new Date());
                orderMo.setReceivedTime(new Date());
            } else {
                orderMo.setOrderState((byte) OrderStateDic.ORDERED.getCode());
            }

            // 下单时间
            orderMo.setOrderTime(now);
            // 上线组织ID(卖家ID)
            orderMo.setOnlineOrgId(onlineOrg.getKey());
            // 支付订单ID
            orderMo.setPayOrderId(payOrderId);
            // 下单人用户ID
            orderMo.setUserId(to.getUserId());
            // 是否当场签收
            if (to.getIsNowReceived() != null && to.getIsNowReceived() == true) {
                orderMo.setIsNowReceived(to.getIsNowReceived());
            }
            // orderMo.setOrderCode(_idWorker.getIdStr()); // 订单编号 TODO 重写订单编号的生成算法
            orderMo.setOrderCode(genOrderCode(orderMo.getOrderTime(), orderMo.getId(), orderMo.getUserId()));
            _log.info("遍历订单详情计算订单的下单金额");
            // 下单金额
            BigDecimal orderAmount = BigDecimal.ZERO;
            for (final OrdOrderDetailMo orderDetailMo : onlineOrg.getValue()) {
                _log.info("订单详情金额和数量--------: {}---{}", orderDetailMo.getBuyPrice(), orderDetailMo.getBuyCount());
                // 计算订单的下单金额
                orderAmount = orderAmount.add(orderDetailMo.getBuyPrice().multiply(orderDetailMo.getBuyCount()));

            }
            _log.info("订单的下单金额为: {}", orderAmount);
            // 下单金额
            orderMo.setOrderMoney(orderAmount);
            // 实际金额=下单金额
            orderMo.setRealMoney(orderAmount);
            // 用户留言
            final String orderMessages = to.getOrderMessages();
            if (!StringUtils.isBlank(orderMessages)) {
                orderMo.setOrderMessages(orderMessages);
            }

            // 如果收货地址为null，则说明该商品为线下店铺的商品
            if (addrMo != null) {
                // 收件人信息
                orderMo.setReceiverName(addrMo.getReceiverName());
                orderMo.setReceiverMobile(addrMo.getReceiverMobile());
                orderMo.setReceiverProvince(addrMo.getReceiverProvince());
                orderMo.setReceiverCity(addrMo.getReceiverCity());
                orderMo.setReceiverExpArea(addrMo.getReceiverExpArea());
                orderMo.setReceiverAddress(addrMo.getReceiverAddress());
                orderMo.setReceiverPostCode(addrMo.getReceiverPostCode());
                orderMo.setReceiverTel(addrMo.getReceiverTel());
            }

            orderMo.setOrderTitle("大卖网络-购买商品");
            _log.info("添加订单信息的参数为：{}", orderMo);
            // 添加订单信息
            add(orderMo);
            // 添加订单详情
            // 当前订单详情的下单时间戳(购买关系匹配自己的详情需要用来过滤，只匹配小于当前订单详情的下单时间戳的详情)
            long orderTimestamp = System.currentTimeMillis();
            for (final OrdOrderDetailMo orderDetailMo : onlineOrg.getValue()) {
                _log.info("添加订单详情：{}", orderDetailMo);
                // 计算当前详情的下单时间戳
                orderDetailMo.setOrderTimestamp(orderTimestamp++);
                orderDetailMo.setOrderId(orderMo.getId());
                orderDetailMo.setUserId(to.getUserId());
                orderDetailMo.setReturnState((byte) ReturnStateDic.NONE.getCode());
                orderDetailMo.setIsSettleBuyer(false);
                orderDetailSvc.add(orderDetailMo);
            }

            if (!to.getIsNowReceived()) {
                _log.debug("计算自动取消订单的执行时间");
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.MINUTE, cancelOrderTime);
                final Date executePlanTime = calendar.getTime();
                _log.debug("取消订单的执行时间为: {}", executePlanTime);
                _log.debug("准备添加自动取消订单的任务");
                final OrdTaskMo ordTaskMo = new OrdTaskMo();
                ordTaskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
                ordTaskMo.setExecutePlanTime(executePlanTime);
                ordTaskMo.setTaskType((byte) OrderTaskTypeDic.CANCEL.getCode());
                ordTaskMo.setOrderId(String.valueOf(orderMo.getId()));
                _log.debug("添加自动取消订单任务的参数为：{}", ordTaskMo);
                // 添加取消订单任务
                ordTaskSvc.add(ordTaskMo);
            } else {
                // 添加一笔交易记录
                // 添加启动结算任务
                ordSettleTaskSvc.addStartSettleTask(orderMo.getId());
            }

            final UpdateOnlineAfterOrderTo updateOnlineTo = new UpdateOnlineAfterOrderTo();
            updateOnlineTo.setUserId(to.getUserId());
            updateOnlineTo.setSpecList(specList);
            _log.debug("更新上线信息(下单后)：{}", updateOnlineTo);
            final Ro updateOnlineRo = onlineSvc.updateOnlineAfterOrder(updateOnlineTo);
            _log.info("更新上线信息(下单后)的返回值为：{}", updateOnlineRo);
            if (updateOnlineRo.getResult() != ResultDic.SUCCESS) {
                _log.error("更新上线信息(下单后)失败: {}", updateOnlineRo);
                throw new RuntimeException(updateOnlineRo.getMsg());
            }
        }
        if (to.getIsNowReceived()) {
            ro.setMsg("支付成功");
        } else {
            ro.setMsg("下单成功");
        }
        ro.setPayOrderId(payOrderId);
        ro.setResult(ResultDic.SUCCESS);
        return ro;
    }

    /**
     * 查询用户订单信息
     */
    @Override
    public List<Map<String, Object>> selectOrderInfo(final Map<String, Object> map) throws ParseException,
            IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final List<Map<String, Object>> list = new ArrayList<>();
        _log.info("查询用户订单信息的参数为：{}", map.toString());
        final List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("获取到的用户订单信息为：{}", String.valueOf(orderList));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (orderList.size() != 0) {
            for (int i = 0; i < orderList.size(); i++) {
                final Map<String, Object> hm = new HashMap<>();
                // 下单时间
                final String orderTime = simpleDateFormat.format(orderList.get(i).getOrderTime());
                Date date = new Date();
                date = simpleDateFormat.parse(orderTime);
                final long orderTimes = date.getTime();
                _log.info("转换下单时间得到的时间戳为：{}", orderTimes);
                if (orderList.get(i).getSendTime() != null) {
                    // 发货时间
                    final String sendTime = simpleDateFormat.format(orderList.get(i).getSendTime());
                    date = new Date();
                    date = simpleDateFormat.parse(sendTime);
                    final long sendTimes = date.getTime();
                    _log.info("转换发货时间得到的时间戳为：{}", orderTimes);
                    hm.put("sendTimes", sendTimes / 1000);
                }
                if (orderList.get(i).getReceivedTime() != null) {
                    // 签收时间
                    final String receivedTime = simpleDateFormat.format(orderList.get(i).getReceivedTime());
                    date = new Date();
                    date = simpleDateFormat.parse(receivedTime);
                    final long receivedTimes = date.getTime();
                    _log.info("转换发货时间得到的时间戳为：{}", receivedTimes);
                    hm.put("receivedTimes", receivedTimes / 1000);
                }
                hm.put("orderTimes", orderTimes / 1000);
                // 系统时间戳
                hm.put("system", System.currentTimeMillis() / 1000);
                final OrdOrderMo obj = orderList.get(i);
                final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor property : propertyDescriptors) {
                    final String key = property.getName();
                    if (!key.equals("class")) {
                        final Method getter = property.getReadMethod();
                        final Object value = getter.invoke(obj);
                        hm.put(key, value);
                    }
                }
                _log.info("查询用户订单信息hm里面的值为：{}", String.valueOf(hm));
                final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setOrderId(orderList.get(i).getId());
                _log.info("查询用户订单信息获取订单详情的参数为：{}", detailMo.toString());
                final List<OrdOrderDetailMo> orderDetailList = orderDetailSvc.list(detailMo);
                _log.info("查询用户订单信息获取订单详情的返回值为：{}", String.valueOf(orderDetailList));
                final List<OrderDetailRo> orderDetailRoList = new ArrayList<>();
                for (final OrdOrderDetailMo orderDetailMo : orderDetailList) {
                    final OrderDetailRo orderDetailRo = new OrderDetailRo();

                    _log.info("查询用户订单信息开始获取商品主图");
                    final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailMo.getOnlineId(),
                            (byte) 1);
                    _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                    _log.info("根据上线ID查找上线商品信息");
                    _log.info("参数 " + orderDetailMo.getOnlineId());
                    final OnlOnlineMo onlineMo = onlineSvc.getById(orderDetailMo.getOnlineId());
                    _log.info("返回值{}", onlineMo);
                    _log.info("获取订单本家购买关系参数id-{}", orderDetailMo.getId());
                    final IbrBuyRelationMo ordNativeBuyRelationResult = ibrBuyRelationSvc
                            .getById(orderDetailMo.getId());
                    if (ordNativeBuyRelationResult != null) {
                        orderDetailRo.setSettled(ordNativeBuyRelationResult.getIsSettled());
                        orderDetailRo.setChildrenCount(ordNativeBuyRelationResult.getChildrenCount());
                    }
                    _log.info("获取订单下家购买关系参数id-{}", orderDetailMo.getId());
                    final List<IbrBuyRelationMo> ordBuyRelationResult = ibrBuyRelationSvc.list(orderDetailMo.getId());
                    final List<OrdBuyRelationRo> buyRelationList = new ArrayList<>();
                    if (ordBuyRelationResult.size() == 0) {
                        _log.info("下家购买关系为空");
                    } else {
                        for (int j = 0; j < ordBuyRelationResult.size(); j++) {
                            // 先获取订单详情中的用户id再根据用户id去获取用户信息。
                            _log.info("获取下家订单详情信息以便下面获取用户信息参数为 id-{}", ordBuyRelationResult.get(j).getId());
                            OrdOrderDetailMo detailResult = orderDetailSvc.getById(ordBuyRelationResult.get(j).getId());
                            _log.info("获取下家用户昵称及头像");
                            final SucUserMo userMo = sucUserSvc.getById(detailResult.getUserId());
                            if (userMo == null) {
                                _log.info("用户信息为空");
                            } else {
                                _log.info("获取到的用户信息为：{}", userMo);
                                final OrdBuyRelationRo buyRelationRo = new OrdBuyRelationRo();
                                buyRelationRo.setDownlineUserNickName(userMo.getWxNickname());
                                buyRelationRo.setDownlineUserWxFace(userMo.getWxFace());
                                _log.info("添加的用户信息为：{}", buyRelationRo);
                                buyRelationList.add(buyRelationRo);
                            }
                        }
                    }
                    orderDetailRo.setOrdBuyRelation(buyRelationList);
                    orderDetailRo.setSubjectType(onlineMo.getSubjectType());
                    orderDetailRo.setId(orderDetailMo.getId());
                    orderDetailRo.setOrderId(orderDetailMo.getOrderId());
                    orderDetailRo.setOnlineId(orderDetailMo.getOnlineId());
                    orderDetailRo.setProductId(orderDetailMo.getProductId());
                    orderDetailRo.setOnlineTitle(orderDetailMo.getOnlineTitle());
                    orderDetailRo.setSpecName(orderDetailMo.getSpecName());
                    orderDetailRo.setBuyCount(orderDetailMo.getBuyCount());
                    orderDetailRo.setBuyPrice(orderDetailMo.getBuyPrice());
                    orderDetailRo.setCashbackAmount(orderDetailMo.getCashbackAmount());
                    orderDetailRo.setBuyUnit(orderDetailMo.getBuyUnit());
                    orderDetailRo.setReturnState(orderDetailMo.getReturnState());
                    orderDetailRo.setGoodsQsmm(onlinePicList.get(0).getPicPath());
                    orderDetailRo.setReturnCount(orderDetailMo.getReturnCount());
                    orderDetailRo.setCashbackTotal(orderDetailMo.getCashbackTotal());

                    orderDetailRo.setBuyPoint(
                            orderDetailMo.getBuyPoint() == null ? BigDecimal.ZERO : orderDetailMo.getBuyPoint());
                    orderDetailRo.setBuyPointTotal(orderDetailMo.getBuyPointTotal() == null ? BigDecimal.ZERO
                            : orderDetailMo.getBuyPointTotal());
                    orderDetailRo.setPaySeq(orderDetailMo.getPaySeq());
                    orderDetailRoList.add(orderDetailRo);
                }
                hm.put("items", orderDetailRoList);
                list.add(i, hm);
            }
        }
        _log.info("最新获取用户订单信息的返回值为：{}", String.valueOf(list));
        return list;
    }

    /**
     * 用户取消订单
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CancellationOfOrderRo cancellationOfOrder(final OrdOrderMo mo) {
        final CancellationOfOrderRo cancellationOfOrderRo = new CancellationOfOrderRo();
        final Map<String, Object> map = new HashMap<>();
        final Long id = mo.getId();
        map.put("id", id);
        final List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，取消订单失败", id);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.ORDER_NOT_EXIST);
            cancellationOfOrderRo.setMsg("订单不存在");
            return cancellationOfOrderRo;
        }
        final long userId = orderList.get(0).getUserId();
        if (orderList.get(0).getOrderState() != OrderStateDic.ORDERED.getCode()) {
            _log.error("由于订单：{}处于非待支付状态，{}取消订单失败", id, userId);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            cancellationOfOrderRo.setMsg("当前状态不允许取消");
            return cancellationOfOrderRo;
        }
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(id);
        final List<OrdOrderDetailMo> orderDetailList = orderDetailSvc.list(detailMo);
        _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        if (orderDetailList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消订单失败", id, userId);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.ORDER_NOT_EXIST);
            cancellationOfOrderRo.setMsg("订单不存在");
            return cancellationOfOrderRo;
        }
        final Date date = new Date();
        mo.setCancelTime(date);
        mo.setOrderState((byte) OrderStateDic.ORDERED.getCode());
        _log.info("取消订单并修改状态的参数为：{}", mo);
        final int updateResult = _mapper.cancellationOrderUpdateOrderState(mo);
        _log.info("取消订单并修改状态的返回值为：{}", updateResult);
        if (updateResult != 1) {
            _log.error("{}取消订单：{}失败", userId, id);
            throw new RuntimeException("修改订单状态失败");
        }
        for (final OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            _log.info("取消订单根据上线规格id修改销售数量的参数为：onlineSpecId-{}, buyCount-{}", ordOrderDetailMo.getOnlineSpecId(),
                    ordOrderDetailMo.getBuyCount());
            final Ro modifySaleCountByIdResult = onlOnlineSpecSvc
                    .modifySaleCountById(ordOrderDetailMo.getOnlineSpecId(), ordOrderDetailMo.getBuyCount());
            _log.info("取消订单根据上线规格id修改销售数量的返回值为：{}", modifySaleCountByIdResult);
            if (modifySaleCountByIdResult.getResult() != ResultDic.SUCCESS) {
                throw new RuntimeException("修改规格数量失败");
            }
        }
        _log.info("{}取消订单：{}成功", userId, id);
        cancellationOfOrderRo.setResult(CancellationOfOrderDic.SUCCESS);
        cancellationOfOrderRo.setMsg("取消订单成功");
        return cancellationOfOrderRo;
    }

    /**
     * 修改订单实际金额 Title: updateOrderRealMoney Description:
     *
     * @param mo
     * @return
     * @date 2018年4月12日 下午2:59:38
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ModifyOrderRealMoneyRo modifyOrderRealMoney(final OrdOrderMo mo) {
        final ModifyOrderRealMoneyRo modifyOrderRealMoneyRo = new ModifyOrderRealMoneyRo();
        _log.info("修改订单实际金额的参数为：{}", mo);
        final int result = _mapper.updateOrderRealMoney(mo);
        _log.info("修改订单实际金额的返回值为：{}", result);
        if (result < 1) {
            modifyOrderRealMoneyRo.setResult(ModifyOrderRealMoneyDic.ERROR);
            modifyOrderRealMoneyRo.setMsg("修改失败");
            return modifyOrderRealMoneyRo;
        }
        modifyOrderRealMoneyRo.setResult(ModifyOrderRealMoneyDic.SUCCESS);
        modifyOrderRealMoneyRo.setMsg("修改成功");
        return modifyOrderRealMoneyRo;
    }

    /**
     * 设置快递公司
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public SetUpExpressCompanyRo setUpExpressCompany(final OrdOrderMo mo) {
        final SetUpExpressCompanyRo expressCompanyRo = new SetUpExpressCompanyRo();
        _log.info("设置快递公司的参数为：{}", mo);
        final int result = _mapper.setUpExpressCompany(mo);
        _log.info("设置快递公司的返回值为：{}", result);
        if (result < 1) {
            _log.error("设置快递公司出错，返回值为：{}", result);
            expressCompanyRo.setResult(SetUpExpressCompanyDic.ERROR);
            expressCompanyRo.setMsg("设置失败");
            return expressCompanyRo;
        }
        expressCompanyRo.setResult(SetUpExpressCompanyDic.SUCCESS);
        expressCompanyRo.setMsg("设置成功");
        return expressCompanyRo;
    }

    /**
     * 取消发货
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro cancelDelivery(final CancelDeliveryTo to) {
        _log.info("取消发货的请求参数为：{}", to);
        final Ro ro = new Ro();
        if (to.getId() == null || to.getCancelingOrderOpId() == null || to.getCanceldeliReason() == null
                || to.getOpIp() == null) {
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg("参数错误");
            return ro;
        }
        _log.info("取消发货查询订单的参数为：{}", to.getId());
        final OrdOrderMo ordOrderMo = thisSvc.getById(to.getId());
        _log.info("取消发货查询订单的返回值为：{}", ordOrderMo);
        if (ordOrderMo == null) {
            _log.error("取消订单时发现没有该订单，订单id为：{}", to.getId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("没有该订单");
            return ro;
        }
        if (ordOrderMo.getOrderState() != OrderStateDic.PAID.getCode()) {
            _log.error("取消发货时发现该订单状态不处于已支付（待发货）状态，订单id为：{}", to.getId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("当前状态不允许取消");
            return ro;
        }
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(to.getId());
        final List<OrdOrderDetailMo> orderDetailList = orderDetailSvc.list(detailMo);
        _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        if (orderDetailList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消发货失败", to.getId(), to.getCancelingOrderOpId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("订单不存在");
            return ro;
        }
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setId(to.getId());
        orderMo.setCancelingOrderOpId(to.getCancelingOrderOpId());
        orderMo.setCanceldeliReason(to.getCanceldeliReason());
        _log.info("取消发货修改订单状态的参数为：{}", orderMo);
        final int cancelDeliveryUpdateOrderStateResult = _mapper.cancelDeliveryUpdateOrderState(orderMo);
        _log.info("取消发货修改订单状态的返回值为：{}", cancelDeliveryUpdateOrderStateResult);
        if (cancelDeliveryUpdateOrderStateResult != 1) {
            _log.error("取消发货修改订单状态出错，订单id为：{}", to.getId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("修改订单状态失败");
            return ro;
        }
        System.out.println("订单真实金额=" + ordOrderMo.getRealMoney() + ", 退货总额=" + ordOrderMo.getReturnTotal());
        // 订单真实购买金额 = 订单真实购买金额 - 退货总额
        final BigDecimal realMoney = ordOrderMo.getRealMoney().subtract(ordOrderMo.getReturnTotal()).setScale(4,
                BigDecimal.ROUND_HALF_UP);
        final RefundApprovedTo approvedTo = new RefundApprovedTo();
        approvedTo.setOrderId(ordOrderMo.getPayOrderId().toString());
        approvedTo.setIsAutoCalcRefund(true);
        approvedTo.setRefundId(ordOrderMo.getId());
        approvedTo.setBuyerAccountId(ordOrderMo.getUserId());
        approvedTo.setSellerAccountId(ordOrderMo.getOnlineOrgId());
        approvedTo.setTradeTitle("大卖网络-取消发货退款");
        approvedTo.setTradeDetail(to.getCanceldeliReason());
        approvedTo.setRefundAmount(realMoney);
        approvedTo.setReturnCompensationToSeller(BigDecimal.ZERO);
        approvedTo.setOpId(to.getCancelingOrderOpId());
        approvedTo.setIp(to.getOpIp());
        approvedTo.setMac("不再获取MAC地址");
        _log.info("取消发货执行退款的参数为：{}", approvedTo);
        final Ro refundApprovedRo = afcRefundSvc.refundApproved(approvedTo);
        _log.info("取消发货执行退款的参数为：{}", refundApprovedRo);
        if (refundApprovedRo.getResult() != ResultDic.SUCCESS) {
            _log.error("取消发货执行退款出现错误，订单号为：{}", to.getId());
            throw new RuntimeException("退款失败");
        }
        for (final OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            _log.info("取消订单根据上线规格id修改销售数量的参数为：onlineSpecId-{}, buyCount-{}", ordOrderDetailMo.getOnlineSpecId(),
                    ordOrderDetailMo.getBuyCount());
            final Ro modifySaleCountByIdResult = onlOnlineSpecSvc
                    .modifySaleCountById(ordOrderDetailMo.getOnlineSpecId(), ordOrderDetailMo.getBuyCount());
            _log.info("取消订单根据上线规格id修改销售数量的返回值为：{}", modifySaleCountByIdResult);
        }
        _log.info("{}取消发货订单：{}成功", to.getCancelingOrderOpId(), to.getId());
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("取消发货成功");
        return ro;
    }

    /**
     * 发货 ： merge=true split=false 第一种发货方式（默认）
     * 需要上线id，上线规格id，将该订单下上线id和规格id等于传过去的发一个包裹。
     * 
     * 第二种发货方式 merge=true split=true 需要上线id，上线规格id，有多少个详情传过去就发多少个包裹。
     * 
     * 第三种发货方式 merge=false split=false 需要详情，将选择的详情发一个包裹
     * 
     * 第四种发货方式 merge=false split=true 需要详情，将选择的详情分别发一个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo deliver(final ShipmentConfirmationTo to) {
        ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();
        _log.info("发货的参数为：{}", to);

        // 判断是否是已经支付状态或者待收货状态
        if (to.getOrderState() != 2 && to.getOrderState() != 3) {
            confirmationRo.setResult(ShipmentConfirmationDic.ERROR);
            confirmationRo.setMsg("订单不是已支付或者待收货状态，不能发货或添加物流订单");
            _log.info("订单的状态为：{}", to.getOrderState());
            return confirmationRo;
        }

        // 第一种发货方式
        if (to.isMerge() == true && to.isSplit() == false) {
            confirmationRo = deliverForMergeIsTrueAndSplitIsFalse(to);
            _log.info("第一种发货方式的返回值：{}", confirmationRo);
        }

        // 第二种发货方式
        if (to.isMerge() == true && to.isSplit() == true) {
            confirmationRo = deliverForMergeIsTrueAndSplitIsTrue(to);
            _log.info("第二种发货方式的返回值：{}", confirmationRo);
        }

        // 第三中种发货方式
        if (to.isMerge() == false && to.isSplit() == false) {
            confirmationRo = deliverForMergeIsFalseAndSplitIsFalse(to);
            _log.info("第二种发货方式的返回值：{}", confirmationRo);
        }

        // 第四种发货方式
        if (to.isMerge() == false && to.isSplit() == true) {
            confirmationRo = deliverForMergeIsFalseAndSplitIsTrue(to);
            _log.info("第二种发货方式的返回值：{}", confirmationRo);
        }

        if (to.isFirst()) {
            _log.info("首次发货，需要判断是否修改订单状态 first: {}", to.isFirst());

            // 根据没有当前订单详情的所有未发货详情Id和当前被选择的详情Id长度是否相等来决定是否修改订单状态为已发货。
            if (to.getAllDetaile().size() == to.getSelectDetaile().size()) {
                _log.debug("需要修改订单状态，订单所有未发货详情等于被选择的详情：AllDetaileId长度-{}, SelectDetailId长度-{}",
                        to.getAllDetaile().size(), to.getSelectDetaile().size());
                final OrdOrderMo ordOrderMo = new OrdOrderMo();
                final Date date = new Date();
                ordOrderMo.setSendOpId(to.getSendOpId());
                ordOrderMo.setSendTime(date);
                ordOrderMo.setId(to.getId());
                _log.info("确认发货并修改订单状态，参数为：{}", ordOrderMo);
                final int result = _mapper.shipmentConfirmation(ordOrderMo);
                if (result != 1) {
                    _log.info("确认发货并修改订单状态失败，返回值为：{}", result);
                    throw new RuntimeException("添加发货表失败");
                }
                _log.info("确认发货并修改订单状态成功，返回值为：{}", result);
            } else {
                _log.info("不需要要修改订单状态，订单所有未发货详情不等于被选择的详情：AllDetaile长度-{}, SelectDetaile长度-{}",
                        to.getAllDetaile().size(), to.getSelectDetaile().size());
                _log.info("确认发货成功");
            }
        } else {
            _log.info("不是首次发货，不需要判断是否修改订单状态 first: {}", to.isFirst());
        }
        return confirmationRo;
    }

    /**
     * 获取物流轨迹 ：
     * 
     * 第一种获取方式 merge=true split=false（默认）需要上线id，上线规格id，将该订单下上线id和规格id等于传过去的发一个包裹。
     * 
     * 第二种获取方式 merge=true split=true 需要上线id，上线规格id，有多少个详情传过去就发多少个包裹。
     * 
     * 第三种获取方式 merge=false split=false 需要详情，将选择的详情发一个包裹
     * 
     * 第四种获取方式 merge=false split=true 需要详情，将选择的详情分别发一个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro deliverAndGetTrace(final DeliverAndGetTraceTo to) {
        final Ro deliverAndGetTraceRo = new Ro();
        // 判断是否是已经支付状态待收货状态
        if (to.getOrderState() != 2 && to.getOrderState() != 3) {
            deliverAndGetTraceRo.setResult(ResultDic.FAIL);
            deliverAndGetTraceRo.setMsg("订单不是已支付或者待收货状态，不能发货或添加物流订单");
            _log.info("订单的状态为：{}", to.getOrderState());
            return deliverAndGetTraceRo;
        }

        // 第一种订阅方式
        if (to.isMerge() == true && to.isSplit() == false) {
            final Ro getTraceRo = getTraceForMergeIsTrueAndSplitIsFalse(to);
            _log.info("第一种订阅方式的返回值：{}", getTraceRo);
        }

        // 第二种订阅方式
        if (to.isMerge() == true && to.isSplit() == true) {
            final Ro getTraceRo = getTraceForMergeIsTrueAndSplitIsTrue(to);
            _log.info("第二种订阅方式的返回值：{}", getTraceRo);
        }

        // 第三种订阅方式
        if (to.isMerge() == false && to.isSplit() == false) {
            final Ro getTraceRo = getTraceForMergeIsFalseAndSplitIsFalse(to);
            _log.info("第三种订阅方式的返回值：{}", getTraceRo);
        }

        // 第四种订阅方式
        if (to.isMerge() == false && to.isSplit() == true) {
            final Ro getTraceRo = getTraceForMergeIsFalseAndSplitIsTrue(to);
            _log.info("第四种订阅方式的返回值：{}", getTraceRo);
        }

        if (to.isFirst()) {
            _log.info("首次订阅，需要判断是否修改订单状态 first: {}", to.isFirst());

            // 根据没有当前订单详情的所有未发货详情Id和当前被选择的详情Id长度是否相等来决定是否修改订单状态为已发货。
            if (to.getAllDetaile().size() == to.getSelectDetaile().size()) {
                _log.debug("需要修改订单状态，订单所有未发货详情等于被选择的详情：AllDetaileId长度-{}, SelectDetailId长度-{}",
                        to.getAllDetaile().size(), to.getSelectDetaile().size());
                final OrdOrderMo ordOrderMo = new OrdOrderMo();
                final Date date = new Date();
                ordOrderMo.setSendOpId(to.getSendOpId());
                ordOrderMo.setSendTime(date);
                ordOrderMo.setId(to.getId());
                _log.info("确认发货并修改订单状态，参数为：{}", ordOrderMo);
                final int result = _mapper.shipmentConfirmation(ordOrderMo);
                if (result != 1) {
                    _log.info("确认发货并修改订单状态失败，返回值为：{}", result);
                    throw new RuntimeException("添加发货表失败");
                }
                _log.info("确认发货并修改订单状态成功，返回值为：{}", result);
            } else {
                _log.info("不需要要修改订单状态，订单所有未发货详情不等于被选择的详情：AllDetaile长度-{}, SelectDetaile长度-{}",
                        to.getAllDetaile().size(), to.getSelectDetaile().size());
                _log.info("确认发货成功");
            }
        } else {
            _log.info("不是首次订阅，不需要判断是否修改订单状态 first: {}", to.isFirst());
        }

        deliverAndGetTraceRo.setResult(ResultDic.SUCCESS);
        deliverAndGetTraceRo.setMsg("确认发货成功");
        return deliverAndGetTraceRo;
    }

    /**
     * 第一种获取轨迹方式（默认）： merge=true split=false
     * 需要上线id，上线规格id，将该订单下上线id和规格id等于传过去的发一个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro getTraceForMergeIsTrueAndSplitIsFalse(final DeliverAndGetTraceTo to) {
        _log.info("第一种订阅方式 merge=true split=false ");
        final Ro getTrace = new Ro();

        // 录入并添加签收任务
        // 设置物流id，提前设置是因为下面发货表需要用到且需要关辆，所有在这里设置并传过去就不用在录入的时候返回回来
        to.setLogisticId(_idWorker.getId());
        _log.info("参数为 ：{}", to);

        String orderTitle = "";

        // 插入发货表和修改详情
        for (int i = 0; i < to.getSelectDetaile().size(); i++) {
            _log.info("开始根据上线id和上线规格id修改详情状态和插入发货表开始----------------------");
            // 循环当前订单下的所有详情
            final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setOrderId(to.getId());
            orderDetailMo.setOnlineId(to.getSelectDetaile().get(i).getOnlineId());
            orderDetailMo.setOnlineSpecId(to.getSelectDetaile().get(i).getOnlineSpecId());
            _log.info("获取订单id,上线id，上线规格id获取详情的参数：{}", orderDetailMo);
            final List<OrdOrderDetailMo> detailResult = orderDetailSvc.list(orderDetailMo);
            _log.info("获取订单id,上线id，上线规格id获取详情的结果为：{}", detailResult);
            orderTitle += detailResult.get(0).getOnlineTitle() + detailResult.get(0).getSpecName() + "x"
                    + detailResult.size();

            // 修改详情的发货状态 和插入到发货表
            for (final OrdOrderDetailMo ordOrderDetailMo2 : detailResult) {
                if (to.isFirst()) {
                    _log.info("首次订阅，需要修改详情 first: {}", to.isFirst());
                    // 修改详情的发货状态
                    _log.info("修改订单详情发货状态的参数是：orderId：{}，onlineId：{}，onlineSpecId：{}", to.getId(),
                            ordOrderDetailMo2.getOnlineId(), ordOrderDetailMo2.getOnlineSpecId());
                    final int updateResult = orderDetailSvc.updateIsDeliver(to.getId(), ordOrderDetailMo2.getOnlineId(),
                            ordOrderDetailMo2.getOnlineSpecId());
                    _log.info("修改结果是 {}", updateResult);
                    if (updateResult < 1) {
                        _log.error("修改订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                    _log.info("修改订单详情成功");
                } else {
                    _log.info("不是首次订阅，不需要修改详情 first: {}", to.isFirst());
                }
                // 插入到发货表
                final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                ooddmo.setLogisticId(to.getLogisticId());
                ooddmo.setOrderId(to.getId());
                ooddmo.setOrderDetailId(ordOrderDetailMo2.getId());
                _log.info("添加发货表的参数：{}", ooddmo);
                final int result = ordOrderDetailDeliverSvc.add(ooddmo);
                _log.info("添加发货表的结果：{}", result);
                if (result != 1) {
                    _log.error("添加发货表失败");
                    throw new RuntimeException("添加发货表失败");
                }
            }

            _log.info("开始根据上线id和上线规格id修改详情状态和插入发货表结束+++++++++++++++++++++++");
        }

        // 设置物流编号和订单标题（orderTitle）
        to.setLogisticCode(to.getLogisticCodeArr().get(0));
        to.setOrderTitle(orderTitle);
        KdiLogisticRo KdiLogisticRo = new KdiLogisticRo();
        try {
            KdiLogisticRo = callTraceAndaddTask(to);
        } catch (final RuntimeException e) {
            throw new RuntimeException("录入并添加签收任务失败");
        }

        _log.info("录入并添加签收任务的返回值为：{}", KdiLogisticRo);

        getTrace.setMsg("发货成功");
        getTrace.setResult(ResultDic.SUCCESS);
        return getTrace;
    }

    /**
     * 第二种获取方式 merge=true split=true 需要上线id，上线规格id，有多少个详情传过去就发多少个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro getTraceForMergeIsTrueAndSplitIsTrue(final DeliverAndGetTraceTo to) {
        _log.info("第二种订阅方式 merge=true split=true ");
        _log.info("参数为 ：{}", to);
        final Ro getTrace = new Ro();

        // 插入发货表和修改详情
        for (int i = 0; i < to.getSelectDetaile().size(); i++) {
            _log.info("开始根据上线id和上线规格id修改详情状态和插入发货表开始----------------------");

            // 循环当前订单下的所有详情
            final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setOrderId(to.getId());
            orderDetailMo.setOnlineId(to.getSelectDetaile().get(i).getOnlineId());
            orderDetailMo.setOnlineSpecId(to.getSelectDetaile().get(i).getOnlineSpecId());
            _log.info("获取订单id,上线id，上线规格id获取详情的参数：{}", orderDetailMo);
            final List<OrdOrderDetailMo> detailResult = orderDetailSvc.list(orderDetailMo);
            _log.info("获取订单id,上线id，上线规格id获取详情的结果为：{}", detailResult);

            // 设置物流id，提前设置是因为下面发货表需要用到且需要关联，所有在这里设置并传过去就不用在录入的时候返回回来
            to.setLogisticId(_idWorker.getId());

            // 设置订单标题，也就是orderTitle
            to.setOrderTitle(detailResult.get(0).getOnlineTitle() + detailResult.get(0).getSpecName() + "x"
                    + detailResult.size());

            // 设置物流标号，因为和选择的详情一样长，所有选择i
            to.setLogisticCode(to.getLogisticCodeArr().get(i));
            KdiLogisticRo KdiLogisticRo = new KdiLogisticRo();
            try {
                KdiLogisticRo = callTraceAndaddTask(to);
            } catch (final RuntimeException e) {
                throw new RuntimeException("录入并添加签收任务失败");
            }
            _log.info("录入并添加签收任务的返回值为：{}", KdiLogisticRo);

            // 修改详情的发货状态 和插入到发货表
            for (final OrdOrderDetailMo ordOrderDetailMo2 : detailResult) {
                if (to.isFirst()) {
                    _log.info("首次订阅，需要修改详情 first: {}", to.isFirst());
                    // 修改详情的发货状态
                    _log.info("修改订单详情发货状态的参数是：orderId：{}，onlineId：{}，onlineSpecId：{}", to.getId(),
                            ordOrderDetailMo2.getOnlineId(), ordOrderDetailMo2.getOnlineSpecId());
                    final int updateResult = orderDetailSvc.updateIsDeliver(to.getId(), ordOrderDetailMo2.getOnlineId(),
                            ordOrderDetailMo2.getOnlineSpecId());
                    _log.info("修改结果是 {}", updateResult);
                    if (updateResult < 1) {
                        _log.error("修改订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                    _log.info("修改订单详情成功");
                } else {
                    _log.info("不是首次订阅，不需要修改详情 first: {}", to.isFirst());
                }
                // 插入到发货表
                final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                ooddmo.setLogisticId(to.getLogisticId());
                ooddmo.setOrderId(to.getId());
                ooddmo.setOrderDetailId(ordOrderDetailMo2.getId());
                _log.info("添加发货表的参数：{}", ooddmo);
                final int result = ordOrderDetailDeliverSvc.add(ooddmo);
                _log.info("添加发货表的结果：{}", result);
                if (result != 1) {
                    _log.error("添加发货表失败");
                    throw new RuntimeException("添加发货表失败");
                }
            }

        }
        getTrace.setMsg("发货成功");
        getTrace.setResult(ResultDic.SUCCESS);
        return getTrace;
    }

    /**
     * 第三种获取方式 merge=false split=false 需要详情，将选择的详情发一个包裹
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro getTraceForMergeIsFalseAndSplitIsFalse(final DeliverAndGetTraceTo to) {
        _log.info("第三种订阅方式 merge=false split=false ");
        _log.info("参数为 ：{}", to);
        final Ro getTrace = new Ro();

        // 提前设置物流id，提前设置是因为下面发货表需要用到且需要关辆，所有在这里设置并传过去就不用在录入的时候返回回来
        to.setLogisticId(_idWorker.getId());

        // 设置订单标题，也就是orderTitle
        String orderTitle = "";
        // 循环修改详情和插入发货表
        for (int i = 0; i < to.getSelectDetaile().size(); i++) {
            _log.info("第三种发货方式循环开始---------------------");
            orderTitle += to.getSelectDetaile().get(i).getOnlineTitle() + "等";

            if (to.isFirst()) {
                _log.info("首次订阅，需要修改详情 first: {}", to.isFirst());
                // 修改详情的发货状态
                final OrdOrderDetailMo modifyDetailMo = new OrdOrderDetailMo();
                modifyDetailMo.setId(to.getSelectDetaile().get(i).getId());
                modifyDetailMo.setIsDelivered(true);
                _log.info("修改订单详情发货状态的参数是：modifyDetailMo：{}", modifyDetailMo);
                final int updateResult = orderDetailSvc.modify(modifyDetailMo);
                _log.info("修改结果是 {}", updateResult);
                if (updateResult < 1) {
                    _log.error("修改订单详情发货状态失败");
                    throw new RuntimeException("修改订单详情发货状态失败");
                }
                _log.info("修改订单详情成功");
            } else {
                _log.info("不是首次订阅，不需要修改详情 first: {}", to.isFirst());
            }

            // 插入到发货表
            final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
            ooddmo.setLogisticId(to.getLogisticId());
            ooddmo.setOrderId(to.getId());
            ooddmo.setOrderDetailId(to.getSelectDetaile().get(i).getId());
            _log.info("添加发货表的参数：{}", ooddmo);
            final int result = ordOrderDetailDeliverSvc.add(ooddmo);
            _log.info("添加发货表的结果：{}", result);
            if (result != 1) {
                _log.error("添加发货表失败");
                throw new RuntimeException("添加发货表失败");
            }
            _log.info("添加发货表成功");

            _log.info("第三种发货方式循环结束++++++++++++++++++++++");
        }

        // 设置物流编号和订单标题（orderTitle）和调用物流
        to.setLogisticCode(to.getLogisticCodeArr().get(0));
        to.setOrderTitle(orderTitle);
        KdiLogisticRo KdiLogisticRo = new KdiLogisticRo();
        try {
            KdiLogisticRo = callTraceAndaddTask(to);
        } catch (final RuntimeException e) {
            throw new RuntimeException("录入并添加签收任务失败");
        }

        _log.info("录入并添加签收任务的返回值为：{}", KdiLogisticRo);

        getTrace.setMsg("发货成功");
        getTrace.setResult(ResultDic.SUCCESS);
        return getTrace;
    }

    /**
     * 第四种获取方式 merge=false split=true 需要详情，将选择的详情分别发一个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro getTraceForMergeIsFalseAndSplitIsTrue(final DeliverAndGetTraceTo to) {
        _log.info("第四种订阅方式 merge=false split=true ");
        _log.info("参数为 ：{}", to);
        final Ro getTrace = new Ro();

        // 循环修改详情和插入发货表
        for (int i = 0; i < to.getSelectDetaile().size(); i++) {
            _log.info("第四种获取方式循环开始---------------------");

            // 设置订单标题，也就是orderTitle
            to.setOrderTitle(to.getSelectDetaile().get(i).getOnlineTitle() + to.getSelectDetaile().get(i).getSpecName()
                    + "x" + to.getSelectDetaile().size());

            // 提前设置物流id，提前设置是因为下面发货表需要用到且需要关联，所有在这里设置并传过去就不用在录入的时候返回回来
            to.setLogisticId(_idWorker.getId());

            // 设置物流编号，因为和选择的详情一样长，所有选择i
            to.setLogisticCode(to.getLogisticCodeArr().get(i));
            KdiLogisticRo KdiLogisticRo = new KdiLogisticRo();
            try {
                KdiLogisticRo = callTraceAndaddTask(to);
            } catch (final RuntimeException e) {
                throw new RuntimeException("录入并添加签收任务失败");
            }
            _log.info("录入并添加签收任务的返回值为：{}", KdiLogisticRo);

            if (to.isFirst()) {
                _log.info("首次订阅，需要修改详情 first: {}", to.isFirst());
                // 修改详情的发货状态
                final OrdOrderDetailMo modifyDetailMo = new OrdOrderDetailMo();
                modifyDetailMo.setId(to.getSelectDetaile().get(i).getId());
                modifyDetailMo.setIsDelivered(true);
                _log.info("修改订单详情发货状态的参数是：modifyDetailMo：{}", modifyDetailMo);
                final int updateResult = orderDetailSvc.modify(modifyDetailMo);
                _log.info("修改结果是 {}", updateResult);
                if (updateResult < 1) {
                    _log.error("修改订单详情发货状态失败");
                    throw new RuntimeException("修改订单详情发货状态失败");
                }
                _log.info("修改订单详情成功");
            } else {
                _log.info("不是首次订阅，不需要修改详情 first: {}", to.isFirst());
            }

            // 插入到发货表
            final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
            ooddmo.setLogisticId(to.getLogisticId());
            ooddmo.setOrderId(to.getId());
            ooddmo.setOrderDetailId(to.getSelectDetaile().get(i).getId());
            _log.info("添加发货表的参数：{}", ooddmo);
            final int result = ordOrderDetailDeliverSvc.add(ooddmo);
            _log.info("添加发货表的结果：{}", result);
            if (result != 1) {
                _log.error("添加发货表失败");
                throw new RuntimeException("添加发货表失败");
            }
            _log.info("添加发货表成功");
            _log.info("第四种发货方式循环开始++++++++++++++++++++++");
        }

        getTrace.setMsg("发货成功");
        getTrace.setResult(ResultDic.SUCCESS);
        return getTrace;
    }

    /**
     * 录入订单并订阅轨迹并添加签收任务
     * 
     * @param to
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public KdiLogisticRo callTraceAndaddTask(final DeliverAndGetTraceTo to) {
        _log.info("录入订单并订阅轨迹并添加签收任务参数为：{}", to);
        final OrdOrderMo mo = dozerMapper.map(to, OrdOrderMo.class);
        if (to.isFirst()) {
            _log.info("首次录入，需要判断是否添加签收任务：isFirst {}", to.isFirst());

            // 添加签收任务
            final Date date = new Date();
            mo.setSendTime(date);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, signinOrderTime);
            final Date executePlanTime = calendar.getTime();
            final OrdTaskMo ordTaskMo = new OrdTaskMo();
            ordTaskMo.setOrderId(String.valueOf(mo.getId()));
            ordTaskMo.setTaskType((byte) 2);
            // 先查询任务是否已经存在
            _log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
            final List<OrdTaskMo> ordTaskList = ordTaskSvc.list(ordTaskMo);
            _log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
            if (ordTaskList.size() == 0) {
                ordTaskMo.setExecutePlanTime(executePlanTime);
                ordTaskMo.setExecuteState((byte) 0);
                _log.info("确认发货添加签收任务的参数为：{}", ordTaskMo);
                final int taskAddResult = ordTaskSvc.add(ordTaskMo);
                _log.info("确认发货添加签收任务的返回值为：{}", taskAddResult);
                if (taskAddResult != 1) {
                    _log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
                    throw new RuntimeException("添加签收任务出错");
                }
            } else {
                _log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
            }
        } else {
            _log.info("不是首次录入，不需要判断是否添加签收任务：isFirst {}", to.isFirst());
        }
        // 添加物流信息
        final AddKdiLogisticTo addKdiLogisticTo = dozerMapper.map(to, AddKdiLogisticTo.class);
        addKdiLogisticTo.setEntryType((byte) 2);
        addKdiLogisticTo.setOrderId(to.getId());
        addKdiLogisticTo.setLogisticId(to.getLogisticId());
        _log.info("添加物流信息参数为：{}", addKdiLogisticTo);
        final KdiLogisticRo entryResult = kdiSvc.entryLogistics(addKdiLogisticTo);
        _log.info("添加物流信息结果为：{}", entryResult);
        if (entryResult.getResult() != 1) {
            _log.error("添加物流信息出错，订单编号为：{}", mo.getOrderCode());
            throw new RuntimeException("添加物流信息出错");
        }
        return entryResult;
    }

    /**
     * 
     * 发货 第一种发货方式（默认）： merge=true split=false
     * 需要上线id，上线规格id，将该订单下上线id和规格id等于传过去的发一个包裹。
     * 
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo deliverForMergeIsTrueAndSplitIsFalse(final ShipmentConfirmationTo to) {
        _log.info("第一种发货方式 merge=true split=false ");
        _log.info("参数为 ：{}", to);
        final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();

        // 添加签收任务并调用快递鸟
        final EOrderRo callKdiNiaoAndaddTaskRo = callKdiNiaoAndaddTask(to);
        _log.info("调用快递鸟的返回值为：{}", callKdiNiaoAndaddTaskRo);
        for (final OrdOrderDetailMo ordOrderDetailMo : to.getSelectDetaile()) {
            _log.info("开始根据上线id和上线规格id修改详情状态和插入发货表开始----------------------");

            // 循环当前订单下的所有详情
            final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setOrderId(to.getId());
            orderDetailMo.setOnlineId(ordOrderDetailMo.getOnlineId());
            orderDetailMo.setOnlineSpecId(ordOrderDetailMo.getOnlineSpecId());
            _log.info("获取订单id,上线id，上线规格id获取详情的参数：{}", orderDetailMo);
            final List<OrdOrderDetailMo> detailResult = orderDetailSvc.list(orderDetailMo);
            _log.info("获取订单id,上线id，上线规格id获取详情的结果为：{}", detailResult);

            // 修改详情的发货状态 和插入到发货表
            for (final OrdOrderDetailMo ordOrderDetailMo2 : detailResult) {
                if (to.isFirst()) {
                    _log.info("是首次发货，需要修改订单详情状态 to.isFirst()：{}", to.isFirst());

                    // 修改详情的发货状态
                    _log.info("修改订单详情发货状态的参数是：orderId：{}，onlineId：{}，onlineSpecId：{}", to.getId(),
                            ordOrderDetailMo2.getOnlineId(), ordOrderDetailMo2.getOnlineSpecId());
                    final int updateResult = orderDetailSvc.updateIsDeliver(to.getId(), ordOrderDetailMo2.getOnlineId(),
                            ordOrderDetailMo2.getOnlineSpecId());
                    _log.info("修改结果是 {}", updateResult);
                    if (updateResult < 1) {
                        _log.error("修改订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                    _log.info("修改订单详情成功");
                } else {
                    _log.info("不是首次发货，不需要修改订单详情状态 to.isFirst()：{}", to.isFirst());
                }
                // 插入到发货表
                final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                ooddmo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
                ooddmo.setOrderId(to.getId());
                ooddmo.setOrderDetailId(ordOrderDetailMo2.getId());
                _log.info("添加发货表的参数：{}", ooddmo);
                final int result = ordOrderDetailDeliverSvc.add(ooddmo);
                _log.info("添加发货表的结果：{}", result);
                if (result != 1) {
                    _log.error("添加发货表失败");
                    throw new RuntimeException("添加发货表失败");
                }
            }
            _log.info("开始根据上线id和上线规格id修改详情状态和插入发货表结束+++++++++++++++++++++++");
        }

        _log.info("第一种发货方式成功。。。。。。。。。。。。。");
        confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
        confirmationRo.setMsg("确认发货成功");
        confirmationRo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
        confirmationRo.setLogisticCode(callKdiNiaoAndaddTaskRo.getLogisticCode());
        confirmationRo.setPrintPage(callKdiNiaoAndaddTaskRo.getPrintPage());
        confirmationRo.setFailReason(callKdiNiaoAndaddTaskRo.getFailReason());
        return confirmationRo;
    }

    /**
     * 第二种发货方式 merge=true split=true 需要上线id，上线规格id，有多少个详情传过去就发多少个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo deliverForMergeIsTrueAndSplitIsTrue(final ShipmentConfirmationTo to) {
        _log.info("第二种发货方式 merge=true split=true ");
        _log.info("参数为 ：{}", to);
        final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();

        // 声明要拼接返回的打印页面
        String printPage = "";
        // 用来判断是否是第一次循环，是的话不用设置加上打印页面
        int i = 0;

        // 循环调用快递鸟
        for (final OrdOrderDetailMo OrdOrderDetailMo : to.getSelectDetaile()) {
            _log.info("第二种发货方式循环开始----------------------");
            // 获取当前上线id和上线规格id的详情
            final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setOrderId(to.getId());
            orderDetailMo.setOnlineId(OrdOrderDetailMo.getOnlineId());
            orderDetailMo.setOnlineSpecId(OrdOrderDetailMo.getOnlineSpecId());
            _log.info("获取订单详情的参数：{}", orderDetailMo);
            final List<OrdOrderDetailMo> detailResult = orderDetailSvc.list(orderDetailMo);
            _log.info("获取订单详情的结果为：{}", detailResult);

            // 拼接发货备注(也就是orderDetail)，先判断是否有留言
            if (to.getOrderMessages() != null) {
                to.setOrderDetail(to.getOrderMessages() + "," + detailResult.get(0).getOnlineTitle() + "."
                        + detailResult.get(0).getSpecName() + "x" + detailResult.size());
            } else {
                to.setOrderDetail(detailResult.get(0).getOnlineTitle() + "." + detailResult.get(0).getSpecName() + "x"
                        + detailResult.size());
            }

            // 添加签收任务并调用快递鸟
            final EOrderRo callKdiNiaoAndaddTaskRo = callKdiNiaoAndaddTask(to);

            // 修改详情的发货状态和插入到发货表
            for (final OrdOrderDetailMo ordOrderDetailMo2 : detailResult) {
                // 修改详情的发货状态
                if (to.isFirst()) {
                    _log.info("是首次发货，需要修改订单详情状态 to.isFirst()：{}", to.isFirst());

                    _log.info("修改订单详情发货状态的参数是：orderId：{}，onlineId：{}，onlineSpecId：{}", to.getId(),
                            ordOrderDetailMo2.getOnlineId(), ordOrderDetailMo2.getOnlineSpecId());
                    final int updateResult = orderDetailSvc.updateIsDeliver(to.getId(), ordOrderDetailMo2.getOnlineId(),
                            ordOrderDetailMo2.getOnlineSpecId());
                    _log.info("修改结果是 {}", updateResult);
                    if (updateResult < 1) {
                        _log.error("修改订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                    _log.info("修改订单详情成功");
                } else {
                    _log.info("不是首次发货，不需要修改订单详情状态 to.isFirst()：{}", to.isFirst());
                }
                // 插入到发货表
                final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                ooddmo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
                ooddmo.setOrderId(to.getId());
                ooddmo.setOrderDetailId(ordOrderDetailMo2.getId());
                _log.info("添加发货表的参数：{}", ooddmo);
                final int result = ordOrderDetailDeliverSvc.add(ooddmo);
                _log.info("添加发货表的结果：{}", result);
                if (result != 1) {
                    _log.error("添加发货表失败");
                    throw new RuntimeException("添加发货表失败");
                }
                _log.info("添加发货表成功");
            }
            // 拼接打印页面,第一页不用加，否则会多一张空白页面在前面
            if (i > 0) {
                printPage += "<br><div style=\"page-break-after:always\"></div>\n\r";
            }
            printPage += callKdiNiaoAndaddTaskRo.getPrintPage();
            i += 1;

            _log.info("第二种发货方式循环结束+++++++++++++++");
        }
        _log.info("第二种发货方式成功。。。。。。。。。。。。。");
        confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
        confirmationRo.setMsg("确认发货成功");
        confirmationRo.setPrintPage(printPage);
        return confirmationRo;
    }

    /**
     * 第三种发货方式 merge=false split=false 需要详情，将选择的详情发一个包裹,且根据详情id修改发货状态
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo deliverForMergeIsFalseAndSplitIsFalse(final ShipmentConfirmationTo to) {
        _log.info("第三种发货方式 merge=false split=false ");
        _log.info("参数为 ：{}", to);
        final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();

        // 添加签收任务并调用快递鸟
        final EOrderRo callKdiNiaoAndaddTaskRo = callKdiNiaoAndaddTask(to);
        _log.info("调用快递鸟的返回值为：{}", callKdiNiaoAndaddTaskRo);

        // 循环修改订单详情的状态和插入发到发货表
        for (final OrdOrderDetailMo OrdOrderDetailMo : to.getSelectDetaile()) {
            _log.info("第三种发货方式循环开始----------------------");

            if (to.isFirst()) {
                _log.info("是首次发货，需要修改订单详情状态 to.isFirst()：{}", to.isFirst());

                // 修改详情的发货状态
                final OrdOrderDetailMo modifyDetailMo = new OrdOrderDetailMo();
                modifyDetailMo.setId(OrdOrderDetailMo.getId());
                modifyDetailMo.setIsDelivered(true);
                _log.info("修改订单详情发货状态的参数是：modifyDetailMo：{}", modifyDetailMo);
                final int updateResult = orderDetailSvc.modify(modifyDetailMo);
                _log.info("修改结果是 {}", updateResult);
                if (updateResult < 1) {
                    _log.error("修改订单详情发货状态失败");
                    throw new RuntimeException("修改订单详情发货状态失败");
                }
                _log.info("修改订单详情成功");
            } else {
                _log.info("不是首次发货，不需要修改订单详情状态 to.isFirst()：{}", to.isFirst());
            }
            // 插入到发货表
            final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
            ooddmo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
            ooddmo.setOrderId(to.getId());
            ooddmo.setOrderDetailId(OrdOrderDetailMo.getId());
            _log.info("添加发货表的参数：{}", ooddmo);
            final int result = ordOrderDetailDeliverSvc.add(ooddmo);
            _log.info("添加发货表的结果：{}", result);
            if (result != 1) {
                _log.error("添加发货表失败");
                throw new RuntimeException("添加发货表失败");
            }
            _log.info("添加发货表成功");

            _log.info("第三种发货方式循环结束++++++++++++++++++++++");
        }

        confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
        confirmationRo.setMsg("确认发货成功");
        confirmationRo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
        confirmationRo.setLogisticCode(callKdiNiaoAndaddTaskRo.getLogisticCode());
        confirmationRo.setPrintPage(callKdiNiaoAndaddTaskRo.getPrintPage());
        confirmationRo.setFailReason(callKdiNiaoAndaddTaskRo.getFailReason());
        return confirmationRo;
    }

    /**
     * 第四种发货方式 merge=false split=true 需要详情，将选择的详情分别发一个包裹。
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo deliverForMergeIsFalseAndSplitIsTrue(final ShipmentConfirmationTo to) {
        _log.info("第四种发货方式 merge=false split=true ");
        _log.info("参数为 ：{}", to);
        final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();

        // 声明要拼接返回的打印页面
        String printPage = "";
        // 用来判断是否是第一次循环，是的话不用设置加上打印页面
        int i = 0;

        // 调用快递鸟，并修改订单详情的状态和插入发到发货表
        for (final OrdOrderDetailMo OrdOrderDetailMo : to.getSelectDetaile()) {

            // 拼接发货备注(也就是orderDetail)
            if (to.getOrderMessages() != null) {
                to.setOrderDetail(to.getOrderMessages() + "," + OrdOrderDetailMo.getOnlineTitle() + "."
                        + OrdOrderDetailMo.getSpecName() + "x" + OrdOrderDetailMo.getBuyCount());

            } else {
                to.setOrderDetail(OrdOrderDetailMo.getOnlineTitle() + "." + OrdOrderDetailMo.getSpecName() + "x"
                        + OrdOrderDetailMo.getBuyCount());

            }

            // 添加签收任务并调用快递鸟
            final EOrderRo callKdiNiaoAndaddTaskRo = callKdiNiaoAndaddTask(to);
            _log.info("调用快递鸟的结果是 ：{}", callKdiNiaoAndaddTaskRo);

            if (to.isFirst()) {
                _log.info("是首次发货，需要修改订单详情状态 to.isFirst()：{}", to.isFirst());
                // 修改详情的发货状态
                final OrdOrderDetailMo modifyDetailMo = new OrdOrderDetailMo();
                modifyDetailMo.setId(OrdOrderDetailMo.getId());
                modifyDetailMo.setIsDelivered(true);
                _log.info("修改订单详情发货状态的参数是：modifyDetailMo：{}", modifyDetailMo);
                final int updateResult = orderDetailSvc.modify(modifyDetailMo);
                _log.info("修改结果是 {}", updateResult);
                if (updateResult < 1) {
                    _log.error("修改订单详情发货状态失败");
                    throw new RuntimeException("修改订单详情发货状态失败");
                }
                _log.info("修改订单详情成功");
            } else {
                _log.info("不是首次发货，不需要修改订单详情状态 to.isFirst()：{}", to.isFirst());
            }

            // 插入到发货表
            final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
            ooddmo.setLogisticId(callKdiNiaoAndaddTaskRo.getLogisticId());
            ooddmo.setOrderId(to.getId());
            ooddmo.setOrderDetailId(OrdOrderDetailMo.getId());
            _log.info("添加发货表的参数：{}", ooddmo);
            final int result = ordOrderDetailDeliverSvc.add(ooddmo);
            _log.info("添加发货表的结果：{}", result);
            if (result != 1) {
                _log.error("添加发货表失败");
                throw new RuntimeException("添加发货表失败");
            }
            _log.info("添加发货表成功");

            // 拼接打印页面,第一页不用加，否则会多一张空白页面在前面
            if (i > 0) {
                printPage += "<br><div style=\"page-break-after:always\"></div>\n\r";
            }
            printPage += callKdiNiaoAndaddTaskRo.getPrintPage();
            i += 1;
        }

        _log.info("第四种发货方式成功。。。。。。。。。。。。。");
        confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
        confirmationRo.setMsg("确认发货成功");
        confirmationRo.setPrintPage(printPage);
        return confirmationRo;
    }

    /*
     * 调用快递鸟且添加签收任务
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EOrderRo callKdiNiaoAndaddTask(final ShipmentConfirmationTo to) {
        // 添加签收任务
        final OrdOrderMo mo = dozerMapper.map(to, OrdOrderMo.class);

        if (to.isFirst()) {
            _log.info("是首次发货，需要判断是否添加签收任务 to.isFirst()：{}", to.isFirst());
            final Date date = new Date();
            mo.setSendTime(date);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, signinOrderTime);
            final Date executePlanTime = calendar.getTime();
            final OrdTaskMo ordTaskMo = new OrdTaskMo();
            ordTaskMo.setOrderId(String.valueOf(mo.getId()));
            ordTaskMo.setTaskType((byte) OrderStateDic.SIGNED.getCode());
            // 先查询任务是否已经存在
            _log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
            final List<OrdTaskMo> ordTaskList = ordTaskSvc.list(ordTaskMo);
            _log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
            if (ordTaskList.size() == 0) {
                ordTaskMo.setExecutePlanTime(executePlanTime);
                ordTaskMo.setExecuteState((byte) 0);
                _log.info("确认发货添加签收任务的参数为：{}", ordTaskMo);
                final int taskAddResult = ordTaskSvc.add(ordTaskMo);
                _log.info("确认发货添加签收任务的返回值为：{}", taskAddResult);
                if (taskAddResult != 1) {
                    _log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
                    throw new RuntimeException("添加签收任务出错");
                }
            } else {
                _log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
            }
        } else {
            _log.info("不是首次发货，不需要判断是否添加签收任务 to.isFirst()：{}", to.isFirst());
        }

        // 调用快递鸟打印电子面单
        final EOrderTo eoderTo = new EOrderTo();
        eoderTo.setShipperId(to.getShipperId());
        eoderTo.setShipperCode(to.getShipperCode());
        eoderTo.setOrderId(mo.getId());
        eoderTo.setOrderDetail(to.getOrderDetail());
        eoderTo.setOrderTitle(mo.getOrderTitle());
        eoderTo.setReceiverName(mo.getReceiverName());
        eoderTo.setReceiverProvince(mo.getReceiverProvince());
        eoderTo.setReceiverCity(mo.getReceiverCity());
        eoderTo.setReceiverExpArea(mo.getReceiverExpArea());
        eoderTo.setReceiverAddress(mo.getReceiverAddress());
        eoderTo.setReceiverPostCode(mo.getReceiverPostCode());
        eoderTo.setReceiverTel(mo.getReceiverTel());
        eoderTo.setReceiverMobile(mo.getReceiverMobile());
        eoderTo.setSenderName(to.getSenderName());
        eoderTo.setSenderMobile(to.getSenderMobile());
        eoderTo.setSenderTel(to.getSenderTel());
        eoderTo.setSenderProvince(to.getSenderProvince());
        eoderTo.setSenderCity(to.getSenderCity());
        eoderTo.setSenderAddress(to.getSenderAddress());
        eoderTo.setSenderExpArea(to.getSenderExpArea());
        eoderTo.setSenderPostCode(to.getSenderPostCode());
        eoderTo.setOrgId(to.getOrgId());
        _log.info("调用快递电子面单的参数为：{}", eoderTo);
        final EOrderRo eOrderRo = kdiSvc.eorder(eoderTo);
        _log.info("调用快递电子面单的返回值为：{}", eOrderRo);
        if (eOrderRo.getResult().getCode() == -1) {
            _log.error("调用快递电子面单出现参数错误");
            throw new RuntimeException("调用快递电子面单参数错误");
        }
        if (eOrderRo.getResult().getCode() == -2) {
            _log.error("重复调用快递电子面单");
            throw new RuntimeException("该订单已发货");
        }
        if (eOrderRo.getResult().getCode() == -3) {
            _log.error("调用快递电子面单失败");
            throw new RuntimeException("调用快递电子面单失败");
        }
        _log.info("调用快递电子面单成功，返回值为：{}", eOrderRo);

        return eOrderRo;
    }

    /**
     * 订单签收
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderSignInRo orderSignIn(final OrderSignInTo to) {
        final OrderSignInRo orderSignInRo = new OrderSignInRo();
        final Long orderId = to.getOrderId();
        // map.put("id", orderId);
        // _log.info("用户查询订单的参数为：{}", String.valueOf(map));
        // final List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        final OrdOrderMo order = thisSvc.getById(orderId);
        _log.info("用户查询订单信息的返回值为：{}", order);
        if (order == null) {
            _log.error("由于订单：{}不存在，取消订单失败", orderId);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        final Long userId = order.getUserId();
        // final Long orderId = orderList.get(0).getId();
        if (order.getOrderState() != OrderStateDic.DELIVERED.getCode() && order.getIsNowReceived() == false) {
            _log.error("由于订单：{}处于非待签收状态，{}签收订单失败", orderId, userId);
            orderSignInRo.setResult(OrderSignInDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            orderSignInRo.setMsg("当前状态不允许签收");
            return orderSignInRo;
        }
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(orderId);
        _log.info("订单签收查询订单详情的参数为：{}", orderId);
        final List<OrdOrderDetailMo> detailList = orderDetailSvc.list(detailMo);
        _log.info("订单签收查询订单详情的返回值为：{}", String.valueOf(detailList));
        if (detailList.size() == 0) {
            _log.error("订单签收查询订单详情时发现没有该订单的订单详情，订单编号为：{}", orderId);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        final Date date = new Date();
        _log.info("订单签收的时间为：{}", date);
//        这里注释掉是因为下家是否签收这个字段开始弃用了
//        for (final OrdOrderDetailMo ordOrderDetailMo : detailList) {
//            if (ordOrderDetailMo.getSubjectType() == 1) {
//                final OrdBuyRelationMo mo = new OrdBuyRelationMo();
//                mo.setDownlineOrderDetailId(ordOrderDetailMo.getId());
//                final OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(mo);
//                _log.info("根据订单详情获取订单购买关系为{}", buyRelationResult);
//                if (buyRelationResult != null) {
//                    _log.info("订单签收更新购买关系表");
//                    final OrdBuyRelationMo updateBuyRelationMo = new OrdBuyRelationMo();
//                    updateBuyRelationMo.setId(buyRelationResult.getId());
//                    updateBuyRelationMo.setIsSignIn(true);
//                    final int updateBuyRelationResult = ordBuyRelationSvc.modify(updateBuyRelationMo);
//                    if (updateBuyRelationResult < 1) {
//                        _log.error("{}更新购买关系出错，返回值为：{}", userId, updateBuyRelationResult);
//                        orderSignInRo.setResult(OrderSignInDic.ERROR);
//                        orderSignInRo.setMsg("更新购买关系失败");
//                        return orderSignInRo;
//                    }
//                }
//            }
//        }
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setId(orderId);
        orderMo.setUserId(userId);
        orderMo.setReceivedTime(date);
        orderMo.setReceivedOpId(userId);
        orderMo.setOrderState(order.getIsNowReceived() == false ? (byte) OrderStateDic.DELIVERED.getCode()
                : (byte) OrderStateDic.PAID.getCode());
        _log.info("订单签收的参数为：{}", orderMo);
        final int signInResult = _mapper.orderSignIn(orderMo);
        _log.info("订单签收的返回值为：{}", signInResult);
        if (signInResult < 1) {
            _log.error("{}签收订单出错，返回值为：{}", userId, signInResult);
            final String msg = "签收失败";
            throw new RuntimeException(msg);
        }
        _log.info("订单签收取消自动签收任务的参数为：{}", orderId);
        final Ro cancelTaskRo = ordTaskSvc.cancelTask(orderId, OrderTaskTypeDic.SIGNED);
        _log.info("订单签收取消自动签收任务的返回值为：{}", cancelTaskRo);
        if (cancelTaskRo.getResult() != ResultDic.SUCCESS && order.getIsNowReceived() == false) {
            _log.error("签收订单取消签收任务失败，订单id为：{}", orderId);
            throw new RuntimeException("签收错误");
        }
        _log.info("订单签收添加结算任务的参数为：{}", orderId);
        ordSettleTaskSvc.addStartSettleTask(orderId);
        _log.info("订单签收成功，订单id为：{}", orderId);
        orderSignInRo.setResult(OrderSignInDic.SUCCESS);
        orderSignInRo.setMsg("签收成功");
        return orderSignInRo;
    }

    /**
     * 修改订单退款金额(根据订单ID和已退款总额)
     *
     * @param refundTotal
     *            退款总额
     *
     * @param orderState
     *            订单状态
     * @param whereOrderId
     *            where条件-订单ID
     * @param whereRefundedTotal
     *            where条件-已退款总额
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyRefund(final BigDecimal refundTotal, final Byte orderState, final Long whereOrderId,
            final BigDecimal whereRefundedTotal) {
        _log.info("修改退款金额(根据订单ID和已退款总额): 退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", refundTotal, orderState, whereOrderId,
                whereRefundedTotal);
        return _mapper.updateRefund(refundTotal, orderState, whereOrderId, whereRefundedTotal);
    }

    /**
     * 根据订单编号修改订单状态
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyOrderStateByOderCode(final long orderCode, final byte orderState) {
        _log.info("修改订单状态的参数为：{}，{}", orderCode, orderState);
        return _mapper.modifyOrderStateByOderCode(orderCode, orderState);
    }

    /**
     * 根据订单编号查询退货金额
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrdOrderMo selectReturnAmountByOrderCode(final String orderCode) {
        _log.info("根据订单编号查询退货金额的参数为：{}", orderCode);
        final OrdOrderMo orderMo = _mapper.selectReturnAmountByOrderCode(orderCode);
        _log.info("根据订单编号查询退货金额的返回值为：{}", orderMo);
        return orderMo;
    }

    /**
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateByOrderCode(final OrdOrderMo mo) {
        final int result = _mapper.updateByOrderCode(mo);
        return result;
    }

    /**
     * 设置订单结算完成
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int completeSettle(final Date closeTime, final String orderId) {
        _log.info("结算完成：orderId-{}", orderId);
        return _mapper.completeSettle(closeTime, orderId);
    }

    /**
     * 生成订单编号
     *
     * @param orderTime
     *            下单时间
     * @param orderId
     *            订单ID
     * @param userId
     *            用户ID
     * @return YYMMDDHH-订单ID末8位-用户ID末8位
     */
    private String genOrderCode(final Date orderTime, final Long orderId, final Long userId) {
        final SimpleDateFormat sdf = new SimpleDateFormat("YYMMDDHH");
        final String YYMMDDHH = sdf.format(orderTime);
        final String orderId8 = StringUtils.right(orderId.toString(), 8);
        final String userId8 = StringUtils.right(userId.toString(), 8);
        return YYMMDDHH + "-" + orderId8 + "-" + userId8;
    }

    /**
     * 处理订单支付完成的通知
     * 1. 根据支付订单ID获取所有订单(如果没有找到，退款)
     * 2. 判断通知回来的支付金额是否和订单中记录的实际交易金额相同(如果不同，退款)
     * 3. 取消订单自动取消任务
     * 4. 按不同发货组织拆单，并重新计算拆单后的订单实际交易金额
     * 5. 根据支付订单id将订单该为已支付
     * 6. 添加首单计算的任务
     * 7. 添加匹配购买关系任务
     * 8. 判断订单是否是当场签收的，是的话调用订单签收接口
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean handleOrderPaidNotify(final PayDoneMsg payDoneMsg) {
        _log.info("处理订单支付完成的通知：payDoneMsg-{}", payDoneMsg);
        // XXX 在本服务中支付传递的orderId实际上是payOrderId
        final Long payOrderId = Long.parseLong(payDoneMsg.getOrderId());
        _log.info("1. 根据支付订单ID获取所有订单(如果没有找到，退款)");
        final OrdOrderMo conditions = new OrdOrderMo();
        conditions.setPayOrderId(payOrderId);
        final List<OrdOrderMo> orders = _mapper.selectSelective(conditions);
        if (orders.isEmpty()) {
            final String msg = "根据支付订单ID找不到任何订单，只能退款";
            _log.warn("{}: payOrderId-{}\n可能订单支付后未收到通知时再次支付", msg, payOrderId);
            final RefundImmediateTo refundGoBackTo = new RefundImmediateTo();
            refundGoBackTo.setOrderId(payDoneMsg.getOrderId());
            refundGoBackTo.setTradeTitle(msg);
            refundSvc.refundImmediate(refundGoBackTo);
            return true;
        }
        _log.debug("根据支付订单ID获取所有订单的结果: {}", orders);

        _log.info("2. 判断订单状态并计算订单总额(所有订单中记录的实际交易金额之和)");
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (final OrdOrderMo order : orders) {
            if (order.getOrderState() > OrderStateDic.ORDERED.getCode()) {
                return true;
            }
            orderTotal = orderTotal.add(order.getRealMoney());

        }
        _log.debug("计算订单总额的结果是: {}", orderTotal);

        _log.info("2. 判断通知回来的支付金额是否和订单中记录的实际交易金额相同(如果不同，退款)");
        if (payDoneMsg.getPayAmount().compareTo(orderTotal) != 0) {
            final String msg = "支付金额与订单中记录的实际金额不一致(" + payDoneMsg.getPayAmount() + ":" + orderTotal + ")，只能退款";
            _log.warn("{}: payOrderId-{}\n可能是在去支付订单后未收到通知时修改了订单的实际金额", msg, payOrderId);
            final RefundImmediateTo refundGoBackTo = new RefundImmediateTo();
            refundGoBackTo.setOrderId(payDoneMsg.getOrderId());
            refundGoBackTo.setTradeTitle(msg);
            final Ro refundGoBackRo = refundSvc.refundImmediate(refundGoBackTo);
            return refundGoBackRo.getResult().equals(ResultDic.SUCCESS);
        }

        _log.info("3. 取消订单自动取消任务");
        for (final OrdOrderMo ordOrderMo : orders) {
            _log.info("处理订单支付完成的通知取消订单自动取消任务的参数为：{}", ordOrderMo.getId());
            final Ro cancelTaskRo = ordTaskSvc.cancelTask(ordOrderMo.getId(), OrderTaskTypeDic.CANCEL);
            _log.info("处理订单支付完成的通知取消订单自动取消任务的返回值为：{}", cancelTaskRo);
            if (cancelTaskRo.getResult() == ResultDic.FAIL) {
                _log.error("处理订单支付完成的通知取消订单自动取消任务时出现错误，结算id为：{}, 订单id为：{}", payOrderId, ordOrderMo.getId());
                return false;
            }
        }

        _log.info("4. 按不同发货组织拆单，并重新计算拆单后的订单实际交易金额");
        final List<OrdOrderDetailMo> orderDetailAlls = new LinkedList<>();
        _log.debug("遍历订单，一个订单一个订单的处理拆单问题");
        for (final OrdOrderMo order : orders) {
            _log.debug("目前遍历到的订单信息-{}", order);
            // 排除取消状态的订单
            if (OrderStateDic.CANCEL.getCode() == order.getOrderState()) {
                _log.debug("被取消的订单: payOrderId-{}", payOrderId);
                continue;
            }
            if (OrderStateDic.ORDERED.getCode() != order.getOrderState()) {
                _log.error("订单不在下单状态，可能碰到并发的问题: payOrderId-{}", payOrderId);
                return true;
            }
            _log.debug("根据orderId获取订单详情列表");
            final List<OrdOrderDetailMo> orderDetails = orderDetailSvc.listByOrderId(order.getId());
            if (orderDetails == null || orderDetails.isEmpty()) {
                _log.error("没有找到订单详情，错误的数据: orderId-{}", order.getId());
                return false;
            }
            _log.debug("根据orderId获取订单详情列表的结果: {}", orderDetails);
            _log.debug("遍历订单详情: 按不同发货组织添加订单到订单Map中");
            // 订单Map，不同发货组织添加一个entry
            final Map<Long, OrdOrderMo> orderMap = new LinkedHashMap<>();
            for (final OrdOrderDetailMo orderDetail : orderDetails) {
                _log.debug("目前遍历到的订单详情信息-{}", orderDetail);
                // 添加到orderDetailAlls，给后面的添加订单购买关系用
                orderDetailAlls.add(orderDetail);
                // 如果订单Map为空，说明是第一个详情，将当前订单加入到map中，并修改订单的发货组织为此详情的发货组织
                if (orderMap.isEmpty()) {
                    orderMap.put(orderDetail.getDeliverOrgId(), order);
                    if (orderDetail.getDeliverOrgId() != null) {
                        _log.debug("修改订单的发货组织为此详情的发货组织");
                        final OrdOrderMo modifyOrderMo = new OrdOrderMo();
                        modifyOrderMo.setId(order.getId());
                        modifyOrderMo.setDeliverOrgId(orderDetail.getDeliverOrgId());
                        thisSvc.modify(modifyOrderMo);
                    }
                    continue;
                }
                OrdOrderMo tempOrder = orderMap.get(orderDetail.getDeliverOrgId());
                if (tempOrder == null) {
                    _log.debug("发现不同发货组织的订单详情，需要添加新订单");
                    tempOrder = dozerMapper.map(order, OrdOrderMo.class);
                    tempOrder.setId(_idWorker.getId());
                    tempOrder.setDeliverOrgId(orderDetail.getDeliverOrgId());
                    // tempOrder.setOrderCode(_idWorker.getIdStr());
                    tempOrder.setOrderCode(
                            genOrderCode(tempOrder.getOrderTime(), tempOrder.getId(), tempOrder.getUserId()));
                    thisSvc.add(tempOrder);
                    orderMap.put(orderDetail.getDeliverOrgId(), tempOrder);
                }
                if (tempOrder.getId() != orderDetail.getOrderId()) {
                    _log.debug("找到的订单不是原来详情的订单，替换详情的父订单为找到的订单");
                    orderDetail.setOrderId(tempOrder.getId());
                    final OrdOrderDetailMo modifyOrderDetailMo = new OrdOrderDetailMo();
                    modifyOrderDetailMo.setId(orderDetail.getId());
                    modifyOrderDetailMo.setOrderId(tempOrder.getId());
                    orderDetailSvc.modify(modifyOrderDetailMo);
                }
            }
            if (orderMap.size() == 1) {
                _log.debug("此订单没有不同组织的订单详情，不用拆单: {}", order);
                continue;
            }
            _log.debug("重新计算拆单后的金额");
            for (final Entry<Long, OrdOrderMo> orderEntry : orderMap.entrySet()) {
                final OrdOrderMo tempMo = orderEntry.getValue();
                _log.debug("遍历到的订单: {}", tempMo);
                _mapper.updateAmountAfterSplitOrder(tempMo.getId());
            }
        }

        _log.info("5. 根据订单支付ID修改订单状态为已支付");
        _log.info("订单支付完成，根据订单支付ID修改订单状态为已支付  payTime-{}", payDoneMsg.getPayTime());
        int result = _mapper.paidOrder(payOrderId, payDoneMsg.getPayTime(), (byte) OrderStateDic.PAID.getCode(),
                (byte) OrderStateDic.ORDERED.getCode());
        _log.debug("订单支付完成通知修改订单信息的返回值为：{}", result);
        if (result == 0) {
            _log.warn("根据支付订单ID修改订单状态为已支付不成功，可能碰到并发的问题: payOrderId-{}", payOrderId);
            return false;
        }

        _log.info("6. 添加计算首单的任务");
        _log.info("遍历订单详情: 添加计算首单的任务");
        for (final OrdOrderDetailMo orderDetail : orderDetailAlls) {
            try {
                _log.debug("添加计算首单的任务: orderDetail-{}", orderDetail);
                _log.debug("设置计算首单任务的执行时间为150分钟后执行");
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, 150);
                final Date executePlanTime = calendar.getTime();
                _log.debug("计算首单任务的执行时间为: {}", executePlanTime);
                _log.debug("准备添加计算首单的任务");
                final OrdTaskMo ordTaskMo = new OrdTaskMo();
                ordTaskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
                ordTaskMo.setExecutePlanTime(executePlanTime);
                ordTaskMo.setTaskType((byte) OrderTaskTypeDic.CALC_FIRST_BUY.getCode());
                ordTaskMo.setOrderId(String.valueOf(orderDetail.getOnlineSpecId())); // 计算首单的任务的订单ID其实是上线规格ID
                _log.debug("添加计算首单任务的参数为：{}", ordTaskMo);
                // 添加计算首单任务
                ordTaskSvc.addEx(ordTaskMo);
            } catch (final DuplicateKeyException e) {
                _log.info("已经存在计算首单的任务：onlineSpecId-" + orderDetail.getOnlineSpecId(), e);
            } catch (final Exception e) {
                _log.error("添加计算首单的任务报错：", e);
            }
        }

        _log.info("7. 添加匹配购买关系任务");
        _log.info("遍历订单详情: 添加匹配购买关系任务");
        for (final OrdOrderDetailMo orderDetail : orderDetailAlls) {
            try {
                _log.info("订单详情商品类型为：subjectType-{}", orderDetail.getSubjectType());
                if (orderDetail.getSubjectType() == 1) {
                    // 添加匹配任务,五分钟后执行。
                    IbrBuyRelationTaskMo addTaskMo = new IbrBuyRelationTaskMo();

                    addTaskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
                    addTaskMo.setTaskType((byte) TaskTypeDic.MATCH_BUY_RELATION.getCode());
                    addTaskMo.setExecutePlanTime(new Date());
                    addTaskMo.setOrderDetailId(orderDetail.getId());
                    _log.info("添加匹配购买关系的参数为:{}", addTaskMo);
                    ibrBuyRelationTaskSvc.add(addTaskMo);
                }
            } catch (final Exception e) {
                _log.error("添加匹配购买关系任务报错：", e);
            }
        }

        _log.info("8. 判断订单是否是当场签收。");
        _log.info("遍历订单如果订单是当前签收的则调用订单签收接口");
        OrderSignInTo orderSignInTo = new OrderSignInTo();
        for (final OrdOrderMo order : orders) {
            if (order.getIsNowReceived()) {
                orderSignInTo.setOrderId(order.getId());
                _log.info("调用签收接口的参数为：orderSignInTo-{}", orderSignInTo);
                OrderSignInRo orderSignInRo = orderSignIn(orderSignInTo);
                _log.info("调用签收接口的结果为：orderSignInRo-{}", orderSignInRo);
            }
        }

        return true;
    }

    /**
     * 根据订单编号查询订单状态
     */
    @Override
    public Byte selectOrderStateByOrderCode(final String orderCode) {
        return _mapper.selectOrderStateByOrderCode(orderCode);
    }

    /**
     * 分页查询订单
     */
    @Override
    public PageInfo<OrdOrderRo> listOrder(final ListOrderTo to, final int pageNum, final int pageSize) {
        _log.info("获取订单的参数为: {}", to);
        _log.info("orderList: ro-{}; pageNum-{}; pageSize-{}", to, pageNum, pageSize);
        final PageInfo<OrdOrderRo> result = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> _mapper.listOrder(to));
        _log.info("获取订单的结果为: {}", result.getList());
        final List<SucOrgMo> sucOrgResult = sucOrgSvc.listAll();
        _log.info("获取所有组织的结果为: {}", sucOrgResult);
        for (final OrdOrderRo ordOrderRo : result.getList()) {
            for (final SucOrgMo sucOrgMo : sucOrgResult) {
                if (ordOrderRo.getOnlineOrgId() != null && ordOrderRo.getOnlineOrgId().equals(sucOrgMo.getId())) {
                    _log.info("设置上线组织ordOrderRo-{},sucOrgMo-{}", ordOrderRo, sucOrgMo);
                    ordOrderRo.setOnlineOrgName(sucOrgMo.getName());
                }
                if (ordOrderRo.getDeliverOrgId() != null && ordOrderRo.getDeliverOrgId().equals(sucOrgMo.getId())) {
                    _log.info("设置发货组织ordOrderRo-{},sucOrgMo-{}", ordOrderRo, sucOrgMo);
                    ordOrderRo.setDeliverOrgName(sucOrgMo.getName());
                }
            }
        }
        return result;
    }

    /**
     * 修改收件人信息
     *
     * @param mo
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro modifyOrderReceiverInfo(final OrdOrderMo mo) {
        final Ro ro = new Ro();
        _log.info("修改收件人信息的参数为：{}", mo);
        final int updateOrderReceiverInfoResult = _mapper.updateOrderReceiverInfo(mo);
        _log.info("修改收件人信息的返回值为：{}", updateOrderReceiverInfoResult);
        if (updateOrderReceiverInfoResult == 0) {
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("修改出错");
            return ro;
        }
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("修改成功");
        return ro;
    }

    /**
     * 根据订单id修改支付订单id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro modifyPayOrderId(final Long id) {
        final Ro ro = new Ro();

        _log.info("根据订单id修改支付订单id查询订单信息的参数为：{}", id);
        final OrdOrderMo ordOrderMo = thisSvc.getById(id);
        _log.info("根据订单id修改支付订单id查询订单信息的返回值为：{}", ordOrderMo);
        if (ordOrderMo == null) {
            _log.error("根据订单id修改支付订单id时发现该订单不存在，订单id为: {}", id);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("无效的订单");
            return ro;
        }

        if (ordOrderMo.getOrderState() != OrderStateDic.ORDERED.getCode()) {
            _log.error("根据订单id修改支付订单id时发现该订单状态不处于待支付状态，请求的id为：{}", id);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("该订单不处于待支付状态");
            return ro;
        }

        final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
        orderDetailMo.setOrderId(id);
        _log.info("根据订单id修改支付订单id查询订单详情的参数为：{}", orderDetailMo);
        final List<OrdOrderDetailMo> detailList = orderDetailSvc.list(orderDetailMo);
        _log.info("根据订单id修改支付订单id查询订单详情的返回值为：{}", String.valueOf(detailList));
        if (detailList.size() == 0) {
            _log.error("根据订单id修改支付订单id时发现该订单详情为空，订单id为：{}", id);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("无效的订单");
            return ro;
        }
        for (final OrdOrderDetailMo ordOrderDetailMo : detailList) {
            _log.info("根据订单id修改支付订单id查询上线信息的参数为：{}", ordOrderDetailMo.getOnlineId());
            final OnlOnlineMo onlOnlineMo = onlineSvc.getById(ordOrderDetailMo.getOnlineId());
            _log.info("根据订单id修改支付订单id查询上线信息的返回值为：{}", onlOnlineMo);
            if (onlOnlineMo == null) {
                _log.info("根据订单id修改支付订单id查询上线信息时发现该商品不存在，订单id为：{}", id);
                ro.setResult(ResultDic.FAIL);
                ro.setMsg("该商品不存在");
                return ro;
            }
            if (onlOnlineMo.getOnlineState() == 0) {
                _log.error("根据订单id修改支付订单id时发先该商品已下线，订单id为：{}", id);
                ro.setResult(ResultDic.FAIL);
                ro.setMsg("商品：" + ordOrderDetailMo.getOnlineTitle() + "已下线");
                return ro;
            }
        }

        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setPayOrderId(ordOrderMo.getPayOrderId());
        _log.info("根据订单id修改支付订单id根据支付订单id查询订单信息的参数为：{}", orderMo);
        final int orderCount = _mapper.countSelective(orderMo);
        _log.info("根据订单id修改支付订单id根据支付订单id查询订单信息的返回值为：{}", orderCount);
        if (orderCount > 1) {
            final Long payOrderId = _idWorker.getId();
            final int result = _mapper.updatePayOrderId(payOrderId, id);
            if (result != 1) {
                _log.error("根据订单id修改支付订单id时出现错误, 订单id为: {]", id);
                ro.setResult(ResultDic.FAIL);
                ro.setMsg("请求失败");
                return ro;
            }
            _log.error("根据订单id修改支付订单id成功, 订单id为: {]", id);
            ro.setResult(ResultDic.SUCCESS);
            ro.setMsg(String.valueOf(payOrderId));
            return ro;
        } else {
            _log.error("根据订单id修改支付订单id成功, 订单id为: {]", id);
            ro.setResult(ResultDic.SUCCESS);
            ro.setMsg(String.valueOf(ordOrderMo.getPayOrderId()));
            return ro;
        }
    }

    /**
     * 根据订单id查询订单签收时间
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrdOrderMo> getOrderSignTime(final String orderIds) {
        _log.info("查询订单签收时间的参数为：{}", orderIds);
        return _mapper.selectOrderSignTime(orderIds);
    }

    @Override
    public PageInfo<OrdOrderRo> SupplierlistOrder(final ListOrderTo to, final int pageNum, final int pageSize) {
        _log.info("供应商获取订单的参数为: {}", to);
        _log.info("orderList: ro-{}; pageNum-{}; pageSize-{}", to, pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> _mapper.listOrderSupplier(to));
    }

    /**
     * 根据组织Id获取未结算或者已经结算的详情的总额
     */
    @Override
    public OrdSettleRo getSettleTotal(final Long supplierId) {
        _log.info("根据供应商Id获取未结算或者已经结算的详情的总额: {}", supplierId);
        final OrdSettleRo result = new OrdSettleRo();
        OrdSettleRo temp = new OrdSettleRo();
        temp = _mapper.getNotSettleTotal(supplierId);
        _log.info("获取供应商等待结算详情的结果: {}", temp);
        if (temp != null) {
            result.setNotSettle(temp.getNotSettle());
        }
        temp = _mapper.getSettleTotal(supplierId);
        _log.info("获取供应商已经结算详情的结果: {}", temp);
        if (temp != null) {
            result.setAlreadySettle(temp.getAlreadySettle());
        }

        return result;
    }

    /**
     * 修改组织
     */
    @Override
    public Ro modifyOrg(final UpdateOrgTo to) {
        final Ro ro = new Ro();
        _log.info("修改组订单发货组织的参数为: {}", to);
        int i = _mapper.updateOrg(to.getDeliverOrgId(), to.getId());
        _log.info("修改组织的结果为: {}", i);
        if (i != 1) {
            throw new RuntimeException("修改失败");
        }
        _log.info("修改组订单详情发货组织和供应商的参数为: {}", to);
        i = orderDetailSvc.modifyOrg(to);
        _log.info("修改组订单详情发货组织和供应商的结果为: {}", i);
        if (i < 1) {
            throw new RuntimeException("修改失败");
        }
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("修改成功");
        return ro;
    }

    @Override
    public BigDecimal getCommissionTotal(final Long userId) {
        return _mapper.getCommissionTotal(userId);
    }

    /**
     * 根据组织id获取未发货的订单数
     */
    @Override
    public BigDecimal getUnshipmentsByDeliverOrgId(final Long deliverOrgId) {
        return _mapper.getUnshipmentsByDeliverOrgId(deliverOrgId);
    }

    /**
     * 根据用户和时间查询已经签收的订单
     * 
     * @param mo
     * @return
     */
    @Override
    public List<OrdOrderMo> havePaidOrderByUserAndTimeList(final OrdOrderMo mo) {
        _log.info("根据用户和时间查询已经签收的订单的参数为：{}", mo);
        return _mapper.selectHavePaidOrderByUserAndTime(mo);
    }

    /**
     * 批量发货并打印
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BulkShipmentRo bulkShipment(final BulkShipmentTo qo) {
        final BulkShipmentRo shipmentRo = new BulkShipmentRo();
        final List<String> batchPrinting = new ArrayList<>();
        final OrdOrderMo[] ordOrderMos = qo.getReceiver();
        for (final OrdOrderMo mo : ordOrderMos) {
            // 获取订单的订单详情
            List<OrdOrderDetailMo> list = new ArrayList<>();
            list = orderDetailSvc.listByOrderId(mo.getId());

            // 订单状态为2(未发货)先修改订单状态再调用快递鸟打印电子面单,订单状态为3(已发货)则直接调用快递鸟打印电子面单
            if (mo.getOrderState() == 2) {
                // 添加签收任务
                final Date date = new Date();
                mo.setSendTime(date);
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, signinOrderTime);
                final Date executePlanTime = calendar.getTime();
                final OrdTaskMo ordTaskMo = new OrdTaskMo();
                ordTaskMo.setOrderId(String.valueOf(mo.getId()));
                ordTaskMo.setTaskType((byte) 2);

                // 先查询任务是否已经存在
                _log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
                final List<OrdTaskMo> ordTaskList = ordTaskSvc.list(ordTaskMo);
                _log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
                if (ordTaskList.size() == 0) {
                    ordTaskMo.setExecuteState((byte) 0);
                    ordTaskMo.setExecutePlanTime(executePlanTime);
                    _log.info("添加签收任务的参数：{}", ordTaskMo);
                    final int taskAddResult = ordTaskSvc.add(ordTaskMo);
                    _log.info("添加签收任务返回的返回值：{}", taskAddResult);
                    if (taskAddResult != 1) {
                        _log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
                        throw new RuntimeException("添加签收任务出错");
                    }
                } else {
                    _log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
                }

                // 整理订单详情
                String orderDetails = "";
                final Map<String, String> map = new HashMap<>();
                final Map<String, Integer> detailMap = new HashMap<>();
                for (final OrdOrderDetailMo ordorderdetailmo : list) {
                    final String online = ordorderdetailmo.getOnlineId() + "-" + ordorderdetailmo.getOnlineSpecId();
                    if (map.containsKey(online)) {
                        final String orderDetail = map.get(online);
                        final Integer value = detailMap.get(orderDetail);
                        detailMap.put(orderDetail, value + 1);
                    } else {
                        final String orderDetail = ordorderdetailmo.getOnlineTitle() + "-"
                                + ordorderdetailmo.getSpecName();
                        map.put(online, orderDetail);
                        detailMap.put(orderDetail, 1);
                    }
                }
                for (final String detailKey : detailMap.keySet()) {
                    final Integer detailValue = detailMap.get(detailKey);
                    orderDetails += detailKey + "*" + detailValue + ";";
                }

                // 调用快递鸟打印电子面单
                final EOrderTo eoderTo = new EOrderTo();
                eoderTo.setShipperId(qo.getShipperId());
                eoderTo.setShipperCode(qo.getShipperCode());
                eoderTo.setOrderId(mo.getId());
                eoderTo.setOrderDetail(orderDetails);
                eoderTo.setOrderTitle(mo.getOrderTitle());
                eoderTo.setReceiverName(mo.getReceiverName());
                eoderTo.setReceiverProvince(mo.getReceiverProvince());
                eoderTo.setReceiverCity(mo.getReceiverCity());
                eoderTo.setReceiverExpArea(mo.getReceiverExpArea());
                eoderTo.setReceiverAddress(mo.getReceiverAddress());
                eoderTo.setReceiverPostCode(mo.getReceiverPostCode());
                eoderTo.setReceiverTel(mo.getReceiverTel());
                eoderTo.setReceiverMobile(mo.getReceiverMobile());
                eoderTo.setSenderName(qo.getSenderName());
                eoderTo.setSenderMobile(qo.getSenderMobile());
                eoderTo.setSenderTel(qo.getSenderTel());
                eoderTo.setSenderProvince(qo.getSenderProvince());
                eoderTo.setSenderCity(qo.getSenderCity());
                eoderTo.setSenderAddress(qo.getSenderAddress());
                eoderTo.setSenderExpArea(qo.getSenderExpArea());
                eoderTo.setSenderPostCode(qo.getSenderPostCode());
                eoderTo.setOrgId(qo.getOrgId());
                _log.info("调用快递电子面单的参数为：{}", eoderTo);
                final EOrderRo eOrderRo = kdiSvc.eorder(eoderTo);
                _log.info("调用快递电子面单的返回值为：{}", eOrderRo);
                if (eOrderRo.getResult().getCode() == -1) {
                    _log.error("调用快递电子面单出现参数错误");
                    throw new RuntimeException("调用快递电子面单参数错误");
                }
                if (eOrderRo.getResult().getCode() == -2) {
                    _log.error("重复调用快递电子面单");
                    throw new RuntimeException("该订单已发货");
                }
                if (eOrderRo.getResult().getCode() == -3) {
                    _log.error("调用快递电子面单失败");
                    throw new RuntimeException("调用快递电子面单失败");
                }
                _log.info("调用快递电子面单成功，返回值为：{}", eOrderRo);
                // 获取订单详情id的集合
                final List<Long> selectDetailId = new ArrayList<>();
                for (final OrdOrderDetailMo ordorderdetailmo : list) {
                    selectDetailId.add(ordorderdetailmo.getId());
                }
                // 根据快递电子面单返回的logisticId和selectDtailId和orderId来添加物流发货表和修改每条详情的发货状态
                for (final Long detailId : selectDetailId) {
                    _log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态开始-------------------------------");
                    final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                    ooddmo.setLogisticId(eOrderRo.getLogisticId());
                    ooddmo.setOrderId(mo.getId());
                    ooddmo.setOrderDetailId(detailId);
                    _log.info("添加发货表的参数：{}", ooddmo);
                    int result = ordOrderDetailDeliverSvc.add(ooddmo);
                    _log.info("添加发货表的结果为：{}", result);
                    if (result != 1) {
                        _log.error("添加发货表失败");
                        throw new RuntimeException("添加发货表失败");
                    }
                    // 修改每条详情的发货状态
                    result = 0;
                    final OrdOrderDetailMo oodMo = new OrdOrderDetailMo();
                    oodMo.setId(detailId);
                    oodMo.setIsDelivered(true);
                    _log.info("修改订单详情发货状态的参数为：{}", oodMo);
                    result = orderDetailSvc.modify(oodMo);
                    _log.info("修改订单详情发货状态结果为：{}", result);
                    if (result != 1) {
                        _log.error("添加订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                }
                _log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态结束++++++++++++++++++++++++++++++++");
                if (eOrderRo.getFailReason() == null) {
                    batchPrinting.add(eOrderRo.getPrintPage());
                    shipmentRo.setMsg("批量发货成功");
                    shipmentRo.setResult(ShipmentConfirmationDic.SUCCESS);
                } else {
                    return shipmentRo;
                }

                // 修改订单状态
                _log.info("确认发货并修改订单状态，参数为:{}", mo);
                final int modifyResult = _mapper.shipmentConfirmation(mo);
                if (modifyResult != 1) {
                    _log.info("确认发货并修改订单状态失败，返回值为：{}", modifyResult);
                    throw new RuntimeException("添加发货表失败");
                }
                _log.info("确认发货并修改订单状态成功，返回值为：{}", modifyResult);

            } else {
                // 整理订单详情
                String orderDetails = "";
                final Map<String, String> map = new HashMap<>();
                final Map<String, Integer> detailMap = new HashMap<>();
                for (final OrdOrderDetailMo ordorderdetailmo : list) {
                    final String online = ordorderdetailmo.getOnlineId() + "-" + ordorderdetailmo.getOnlineSpecId();
                    if (map.containsKey(online)) {
                        final String orderDetail = map.get(online);
                        final Integer value = detailMap.get(orderDetail);
                        detailMap.put(orderDetail, value + 1);
                    } else {
                        final String orderDetail = ordorderdetailmo.getOnlineTitle() + "-"
                                + ordorderdetailmo.getSpecName();
                        map.put(online, orderDetail);
                        detailMap.put(orderDetail, 1);
                    }
                }
                for (final String detailKey : detailMap.keySet()) {
                    final Integer detailValue = detailMap.get(detailKey);
                    orderDetails += detailKey + "*" + detailValue + ";";
                }
                // 调用快递鸟打印电子面单
                final EOrderTo eoderTo = new EOrderTo();
                eoderTo.setShipperId(qo.getShipperId());
                eoderTo.setShipperCode(qo.getShipperCode());
                eoderTo.setOrderId(mo.getId());
                eoderTo.setOrderDetail(orderDetails);
                eoderTo.setOrderTitle(mo.getOrderTitle());
                eoderTo.setReceiverName(mo.getReceiverName());
                eoderTo.setReceiverProvince(mo.getReceiverProvince());
                eoderTo.setReceiverCity(mo.getReceiverCity());
                eoderTo.setReceiverExpArea(mo.getReceiverExpArea());
                eoderTo.setReceiverAddress(mo.getReceiverAddress());
                eoderTo.setReceiverPostCode(mo.getReceiverPostCode());
                eoderTo.setReceiverTel(mo.getReceiverTel());
                eoderTo.setReceiverMobile(mo.getReceiverMobile());
                eoderTo.setSenderName(qo.getSenderName());
                eoderTo.setSenderMobile(qo.getSenderMobile());
                eoderTo.setSenderTel(qo.getSenderTel());
                eoderTo.setSenderProvince(qo.getSenderProvince());
                eoderTo.setSenderCity(qo.getSenderCity());
                eoderTo.setSenderAddress(qo.getSenderAddress());
                eoderTo.setSenderExpArea(qo.getSenderExpArea());
                eoderTo.setSenderPostCode(qo.getSenderPostCode());
                eoderTo.setOrgId(qo.getOrgId());
                _log.info("调用快递电子面单的参数为：{}", eoderTo);
                final EOrderRo eOrderRo = kdiSvc.eorder(eoderTo);
                _log.info("调用快递电子面单的返回值为：{}", eOrderRo);
                if (eOrderRo.getResult().getCode() == -1) {
                    _log.error("调用快递电子面单出现参数错误");
                    throw new RuntimeException("调用快递电子面单参数错误");
                }
                if (eOrderRo.getResult().getCode() == -2) {
                    _log.error("重复调用快递电子面单");
                    throw new RuntimeException("该订单已发货");
                }
                if (eOrderRo.getResult().getCode() == -3) {
                    _log.error("调用快递电子面单失败");
                    throw new RuntimeException("调用快递电子面单失败");
                }
                _log.info("调用快递电子面单成功，返回值为：{}", eOrderRo);

                if (eOrderRo.getFailReason() == null) {
                    batchPrinting.add(eOrderRo.getPrintPage());
                    shipmentRo.setMsg("批量发货成功");
                    shipmentRo.setResult(ShipmentConfirmationDic.SUCCESS);
                } else {
                    return shipmentRo;
                }
            }
            // 整理批量打印的页面
            String printPage = "";
            if (shipmentRo.getResult() == ShipmentConfirmationDic.SUCCESS) {
                for (int i = 0; i < batchPrinting.size(); i++) {
                    if (i != 0) {
                        printPage += "<br><div style=\"page-break-after:always\"></div>\n\r";
                    }
                    printPage += batchPrinting.get(i);
                }
            }
            shipmentRo.setPrintPage(printPage);
        }
        return shipmentRo;
    }

    /**
     * 批量订阅并发货
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BulkShipmentRo bulkSubscription(final BulkShipmentTo qo) {
        final BulkShipmentRo shipmentRo = new BulkShipmentRo();
        final OrdOrderMo[] ordOrderMos = qo.getReceiver();
        for (final OrdOrderMo mo : ordOrderMos) {
            // 获取订单的订单详情
            List<OrdOrderDetailMo> list = new ArrayList<>();
            list = orderDetailSvc.listByOrderId(mo.getId());

            // 订单状态为2(未发货)先修改订单状态再添加物流信息,订单状态为3(已发货)则直接添加物流信息
            if (mo.getOrderState() == 2) {

                // 添加签收任务
                final Date date = new Date();
                mo.setSendTime(date);
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, signinOrderTime);
                final Date executePlanTime = calendar.getTime();
                final OrdTaskMo ordTaskMo = new OrdTaskMo();
                ordTaskMo.setOrderId(String.valueOf(mo.getId()));
                ordTaskMo.setTaskType((byte) 2);

                // 先查询任务是否已经存在
                _log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
                final List<OrdTaskMo> ordTaskList = ordTaskSvc.list(ordTaskMo);
                _log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
                if (ordTaskList.size() == 0) {
                    ordTaskMo.setExecuteState((byte) 0);
                    ordTaskMo.setExecutePlanTime(executePlanTime);
                    _log.info("添加签收任务的参数：{}", ordTaskMo);
                    final int taskAddResult = ordTaskSvc.add(ordTaskMo);
                    _log.info("添加签收任务返回的返回值：{}", taskAddResult);
                    if (taskAddResult != 1) {
                        _log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
                        throw new RuntimeException("添加签收任务出错");
                    }
                } else {
                    _log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
                }

                // 整理订单详情
                String orderDetails = "";
                final Map<String, String> map = new HashMap<>();
                final Map<String, Integer> detailMap = new HashMap<>();
                for (final OrdOrderDetailMo ordorderdetailmo : list) {
                    final String online = ordorderdetailmo.getOnlineId() + "-" + ordorderdetailmo.getOnlineSpecId();
                    if (map.containsKey(online)) {
                        final String orderDetail = map.get(online);
                        final Integer value = detailMap.get(orderDetail);
                        detailMap.put(orderDetail, value + 1);
                    } else {
                        final String orderDetail = ordorderdetailmo.getOnlineTitle() + "-"
                                + ordorderdetailmo.getSpecName();
                        map.put(online, orderDetail);
                        detailMap.put(orderDetail, 1);
                    }
                }
                for (final String detailKey : detailMap.keySet()) {
                    final Integer detailValue = detailMap.get(detailKey);
                    orderDetails += detailKey + "*" + detailValue + ";";
                }
                // 添加物流信息
                final AddKdiLogisticTo addKdiLogisticTo = new AddKdiLogisticTo();
                addKdiLogisticTo.setShipperId(qo.getShipperId());
                addKdiLogisticTo.setShipperCode(qo.getShipperCode());
                addKdiLogisticTo.setOrderId(mo.getId());
                addKdiLogisticTo.setOrderTitle(orderDetails);
                addKdiLogisticTo.setReceiverName(mo.getReceiverName());
                addKdiLogisticTo.setReceiverProvince(mo.getReceiverProvince());
                addKdiLogisticTo.setReceiverCity(mo.getReceiverCity());
                addKdiLogisticTo.setReceiverExpArea(mo.getReceiverExpArea());
                addKdiLogisticTo.setReceiverAddress(mo.getReceiverAddress());
                addKdiLogisticTo.setReceiverPostCode(mo.getReceiverPostCode());
                addKdiLogisticTo.setReceiverTel(mo.getReceiverTel());
                addKdiLogisticTo.setReceiverMobile(mo.getReceiverMobile());
                addKdiLogisticTo.setSenderName(qo.getSenderName());
                addKdiLogisticTo.setSenderMobile(qo.getSenderMobile());
                addKdiLogisticTo.setSenderTel(qo.getSenderTel());
                addKdiLogisticTo.setSenderProvince(qo.getSenderProvince());
                addKdiLogisticTo.setSenderCity(qo.getSenderCity());
                addKdiLogisticTo.setSenderAddress(qo.getSenderAddress());
                addKdiLogisticTo.setSenderExpArea(qo.getSenderExpArea());
                addKdiLogisticTo.setSenderPostCode(qo.getSenderPostCode());
                addKdiLogisticTo.setOrgId(qo.getOrgId());
                addKdiLogisticTo.setEntryType((byte) 2);
                addKdiLogisticTo.setOrderId(mo.getId());
                addKdiLogisticTo.setLogisticId(_idWorker.getId());
                addKdiLogisticTo.setLogisticCode(qo.getExpressNumber() + "");
                _log.info("添加物流信息参数为:{}", addKdiLogisticTo);
                final KdiLogisticRo entryResult = kdiSvc.entryLogistics(addKdiLogisticTo);
                _log.info("添加物流信息的结果为:{}", entryResult);
                if (entryResult.getResult() != 1) {
                    _log.error("添加物流信息出错，订单编号为：{}", mo.getOrderCode());
                    throw new RuntimeException("添加物流信息出错");
                }
                // 获取订单详情id的集合
                final List<Long> selectDetaiLId = new ArrayList<>();
                for (final OrdOrderDetailMo ordOrderDetailMo : list) {
                    selectDetaiLId.add(ordOrderDetailMo.getId());
                }
                // 根据logisticId、selectDtailId和orderId来添加物流发货表和修改每条详情的发货状态
                for (final Long detailId : selectDetaiLId) {
                    _log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态开始-------------------------------");
                    final OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
                    ooddmo.setLogisticId(addKdiLogisticTo.getLogisticId());
                    ooddmo.setOrderId(mo.getId());
                    ooddmo.setOrderDetailId(detailId);
                    _log.info("添加发货表的参数：{}", ooddmo);
                    int result = ordOrderDetailDeliverSvc.add(ooddmo);
                    _log.info("添加发货表的结果为：{}", result);
                    if (result != 1) {
                        _log.error("添加发货表失败");
                        throw new RuntimeException("添加发货表失败");
                    }
                    // 修改每条详情的发货状态
                    result = 0;
                    final OrdOrderDetailMo oodMo = new OrdOrderDetailMo();
                    oodMo.setId(detailId);
                    oodMo.setIsDelivered(true);
                    _log.info("修改订单详情发货状态的参数为：{}", oodMo);
                    result = orderDetailSvc.modify(oodMo);
                    _log.info("修改订单详情发货状态结果为：{}", result);
                    if (result != 1) {
                        _log.error("添加订单详情发货状态失败");
                        throw new RuntimeException("修改订单详情发货状态失败");
                    }
                }
                _log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态结束++++++++++++++++++++++++++++++++");
                if (entryResult.getResult() == 1) {
                    shipmentRo.setMsg("批量订阅并发货成功");
                    shipmentRo.setResult(ShipmentConfirmationDic.SUCCESS);
                } else {
                    return shipmentRo;
                }

                // 修改订单状态
                _log.info("确认发货并修改订单状态，参数为:{}", mo);
                final int modifyResult = _mapper.shipmentConfirmation(mo);
                if (modifyResult != 1) {
                    _log.info("确认发货并修改订单状态失败，返回值为：{}", modifyResult);
                    throw new RuntimeException("添加发货表失败");
                }
                _log.info("确认发货并修改订单状态成功，返回值为：{}", modifyResult);

            } else {
                // 整理订单详情
                String orderDetails = "";
                final Map<String, String> map = new HashMap<>();
                final Map<String, Integer> detailMap = new HashMap<>();
                for (final OrdOrderDetailMo ordorderdetailmo : list) {
                    final String online = ordorderdetailmo.getOnlineId() + "-" + ordorderdetailmo.getOnlineSpecId();
                    if (map.containsKey(online)) {
                        final String orderDetail = map.get(online);
                        final Integer value = detailMap.get(orderDetail);
                        detailMap.put(orderDetail, value + 1);
                    } else {
                        final String orderDetail = ordorderdetailmo.getOnlineTitle() + "-"
                                + ordorderdetailmo.getSpecName();
                        map.put(online, orderDetail);
                        detailMap.put(orderDetail, 1);
                    }
                }
                for (final String detailKey : detailMap.keySet()) {
                    final Integer detailValue = detailMap.get(detailKey);
                    orderDetails += detailKey + "*" + detailValue + ";";
                }
                // 添加物流信息
                final AddKdiLogisticTo addKdiLogisticTo = new AddKdiLogisticTo();
                addKdiLogisticTo.setShipperId(qo.getShipperId());
                addKdiLogisticTo.setShipperCode(qo.getShipperCode());
                addKdiLogisticTo.setOrderId(mo.getId());
                addKdiLogisticTo.setOrderTitle(orderDetails);
                addKdiLogisticTo.setReceiverName(mo.getReceiverName());
                addKdiLogisticTo.setReceiverProvince(mo.getReceiverProvince());
                addKdiLogisticTo.setReceiverCity(mo.getReceiverCity());
                addKdiLogisticTo.setReceiverExpArea(mo.getReceiverExpArea());
                addKdiLogisticTo.setReceiverAddress(mo.getReceiverAddress());
                addKdiLogisticTo.setReceiverPostCode(mo.getReceiverPostCode());
                addKdiLogisticTo.setReceiverTel(mo.getReceiverTel());
                addKdiLogisticTo.setReceiverMobile(mo.getReceiverMobile());
                addKdiLogisticTo.setSenderName(qo.getSenderName());
                addKdiLogisticTo.setSenderMobile(qo.getSenderMobile());
                addKdiLogisticTo.setSenderTel(qo.getSenderTel());
                addKdiLogisticTo.setSenderProvince(qo.getSenderProvince());
                addKdiLogisticTo.setSenderCity(qo.getSenderCity());
                addKdiLogisticTo.setSenderAddress(qo.getSenderAddress());
                addKdiLogisticTo.setSenderExpArea(qo.getSenderExpArea());
                addKdiLogisticTo.setSenderPostCode(qo.getSenderPostCode());
                addKdiLogisticTo.setOrgId(qo.getOrgId());
                addKdiLogisticTo.setEntryType((byte) 2);
                addKdiLogisticTo.setOrderId(mo.getId());
                addKdiLogisticTo.setLogisticId(_idWorker.getId());
                addKdiLogisticTo.setLogisticCode(qo.getExpressNumber() + "");
                _log.info("添加物流信息参数为:{}", addKdiLogisticTo);
                final KdiLogisticRo entryResult = kdiSvc.entryLogistics(addKdiLogisticTo);
                _log.info("添加物流信息的结果为:{}", entryResult);
                if (entryResult.getResult() != 1) {
                    _log.error("添加物流信息出错，订单编号为：{}", mo.getOrderCode());
                    throw new RuntimeException("添加物流信息出错");
                }
                if (entryResult.getResult() == 1) {
                    shipmentRo.setMsg("批量订阅并发货成功");
                    shipmentRo.setResult(ShipmentConfirmationDic.SUCCESS);
                } else {
                    return shipmentRo;
                }
            }
        }
        return shipmentRo;
    }

    /**
     * 转移订单和购买关系
     * 
     * @param orderId
     *            订单id
     * @param newUserId
     *            新用户id
     * @param oldUserId
     *            旧用户id
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShiftOrderRo shiftOrder(final Long payOrderId, final Long newUserId, final Long oldUserId) {
        _log.info("转移订单的参数为：payOrderId-{}, newUserId-{}, oldUserId-{}", payOrderId, newUserId, oldUserId);

        final ShiftOrderRo ro = new ShiftOrderRo();
        if (payOrderId == null || newUserId == null) {
            _log.error("转移订单时发现orderId/userId为null, 请求的参数为：payOrderId-{}, newUserId-{}, oldUserId-{}", payOrderId,
                    newUserId, oldUserId);
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg("参数错误");
            return ro;
        }

        OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setPayOrderId(payOrderId);
        // 根据订单id查询订单信息
        final OrdOrderMo ordOrderMo = thisSvc.getOne(orderMo);
        _log.info("转移订单根据订单id查询订单信息的返回值为：{}", ordOrderMo);

        if (ordOrderMo == null) {
            _log.error("转移订单查询订单信息时没有发现订单信息，请求的参数为：payOrderId-{}, newUserId-{}, oldUserId-{}", payOrderId, newUserId,
                    oldUserId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("没有发现该订单");
            return ro;
        }

        // 如果订单状态不等于已下单（待支付）则不允许转移订单
        if (ordOrderMo.getOrderState() != OrderStateDic.ORDERED.getCode()) {
            _log.error("转移订单时发现该订单不处于已下单（待支付）状态，请求的参数为：payOrderId-{}， newUserId-{}, oldUserId-{}", payOrderId,
                    newUserId, oldUserId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("该订单已支付或已取消");
            return ro;
        }

        // 是否存在该用户
        final Boolean isExistUser = sucUserSvc.exist(newUserId);
        _log.info("转移订单判断新用户是否存在的返回值为：{}", isExistUser);
        if (!isExistUser) {
            _log.error("转移订单判断新用户是否存在时发现新的用户不存在，请求的参数为：payOrderId-{}， newUserId-{}, oldUserId-{}", payOrderId,
                    newUserId, oldUserId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("您的账号不存在");
            return ro;
        }

        final int updateUserIdByIdAndUserIdResult = _mapper.updateUserIdByIdAndUserId(payOrderId, newUserId, oldUserId);
        _log.info("转移订单的返回值为：{}", updateUserIdByIdAndUserIdResult);
        if (updateUserIdByIdAndUserIdResult != 1) {
            _log.error("转移订单时出现错误，请求的参数为：payOrderId-{}， newUserId-{}, oldUserId-{}", payOrderId, newUserId, oldUserId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("操作出现异常");
            return ro;
        }

        _log.info("转移订单成功，请求的参数为：payOrderId-{}， newUserId-{}, oldUserId-{}", payOrderId, newUserId, oldUserId);

        // 循环订单去修改订单详情中的用户id以便后面匹配关系。
        orderMo = new OrdOrderMo();
        orderMo.setPayOrderId(payOrderId);
        _log.info("根据订单支付id获取所有订单的参数为orderMo-{}", orderMo);
        final List<OrdOrderMo> orderList = super.list(orderMo);
        _log.info("根据订单支付id获取所有订单的结果为orderMo-{}", orderList);
        for (final OrdOrderMo order : orderList) {
            final int i = orderDetailSvc.modifyUserIdByOrderId(order.getId(), newUserId);
            if (i == 0) {
                _log.error("修改订单详情用户id出错：order.getId()-{},newUserId-{}", order.getId(), newUserId);
                throw new RuntimeException("修改订单详情用户id出错");
            }

        }
        _log.info("转移成功");

        // 获取所有订单详情和每个详情邀请关系
        ro.setRealMoney(ordOrderMo.getRealMoney());
        ro.setUserId(ordOrderMo.getUserId());
        ro.setPayOrderId(ordOrderMo.getPayOrderId());
        BigDecimal totalNumber = BigDecimal.ZERO;
        OrdOrderDetailMo getDetailMo = new OrdOrderDetailMo();
        List<DetailAndRelationRo> detailInfo = new ArrayList<>();
        for (final OrdOrderMo order : orderList) {
            getDetailMo.setOrderId(order.getId());
            _log.info("根据订单id获取订单详情的参数为：getDetailMo-{}", getDetailMo);
            List<OrdOrderDetailMo> getDetailResult = orderDetailSvc.list(getDetailMo);
            _log.info("根据订单id获取订单详情的结果为：getDetailResult-{}", getDetailResult);
            for (OrdOrderDetailMo ordOrderDetailMo : getDetailResult) {
                totalNumber = totalNumber.add(ordOrderDetailMo.getBuyCount());
                DetailAndRelationRo orderDetailAndRelationRo = dozerMapper.map(ordOrderDetailMo,
                        DetailAndRelationRo.class);
                // 设置邀请人是否存在和邀请人信息
                if (ordOrderDetailMo.getSubjectType() == 1 && ordOrderDetailMo.getInviteId() == null) {
                    _log.info("邀请人并未存在在详情中，需要去邀请表中查询邀请人id");
                    orderDetailAndRelationRo.setInviterExist(false);
                    IbrInviteRelationMo getInviteMo = new IbrInviteRelationMo();
                    getInviteMo.setInviterId(ordOrderDetailMo.getUserId());
                    _log.info("根据用户id获取邀请关系的参数为：userId-{}", ordOrderDetailMo.getUserId());
                    IbrInviteRelationMo getInviteResult = ibrInviteRelationSvc.getOne(ordOrderDetailMo.getUserId());
                    _log.info("根据用户id获取邀请关系的结果为：getInviteResult-{}", getInviteResult);
                    if (getInviteResult != null) {
                        _log.info("根据邀请人用户id获取其头像和名称参数为：inviteeId-{}", getInviteResult.getInviteeId());
                        SucUserMo getUserResult = sucUserSvc.getById(getInviteResult.getInviteeId());
                        _log.info("根据邀请人用户id获取其头像和名称结果为：getUserResult-{}", getUserResult);
                        if (getUserResult != null) {
                            orderDetailAndRelationRo.setInviterName(getUserResult.getWxNickname());
                            orderDetailAndRelationRo.setInviterWxFace(getUserResult.getWxFace());
                            orderDetailAndRelationRo.setInviterId(getUserResult.getId());
                        }
                    }
                } else if (ordOrderDetailMo.getSubjectType() == 1 && ordOrderDetailMo.getInviteId() != null) {
                    _log.info("邀请人已存在在详情中，直接去用户表查询邀请人信息");
                    orderDetailAndRelationRo.setInviterExist(true);
                    _log.info("根据邀请人用户id获取其头像和名称参数为：inviteeId-{}", ordOrderDetailMo.getInviteId());
                    SucUserMo getUserResult = sucUserSvc.getById(ordOrderDetailMo.getInviteId());
                    _log.info("根据邀请人用户id获取其头像和名称结果为：getUserResult-{}", getUserResult);
                    if (getUserResult != null) {
                        orderDetailAndRelationRo.setInviterName(getUserResult.getWxNickname());
                        orderDetailAndRelationRo.setInviterWxFace(getUserResult.getWxFace());
                        orderDetailAndRelationRo.setInviterId(getUserResult.getId());
                    }
                }
                _log.info("根据上线id获取商品上线图片的参数为 onlineId-{}", ordOrderDetailMo.getOnlineId());
                List<OnlOnlinePicMo> getPicResult = onlOnlinePicSvc.list(ordOrderDetailMo.getOnlineId(), (byte) 1);
                _log.info("根据上线id获取商品上线图片的结果为 getPicResult-{}", getPicResult);
                if (getPicResult.size() != 0) {
                    orderDetailAndRelationRo.setPicPath(getPicResult.get(0).getPicPath());
                }
                detailInfo.add(orderDetailAndRelationRo);
            }
        }
        ro.setDetailInfo(detailInfo);
        ro.setTotalNumber(totalNumber);
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("转移成功");
        return ro;
    }

    /**
     * 根据用户id查询订单状态不为退货和未支付且支付时间为最新的订单信息
     */
    @Override
    public OrdOrderMo getLatestOneByUserId(final Long userId) {
        OrdOrderMo orderMo = new OrdOrderMo();
        orderMo = _mapper.getLatestOneByUserId(userId);
        _log.info("查询订单状态不为退货和未支付且支付时间为最新的订单信息的返回值为-{}", orderMo);
        return orderMo;
    }

    @Override
    public OrdOrderJo getJoById(Long arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrdOrderJo> listJoAll() {
        // TODO Auto-generated method stub
        return null;
    }

}