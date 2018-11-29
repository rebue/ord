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
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.afc.msg.PayDoneMsg;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.svr.feign.AfcSettleTaskSvc;
import rebue.afc.to.RefundGoBackTo;
import rebue.kdi.ro.EOrderRo;
import rebue.kdi.ro.KdiLogisticRo;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.kdi.to.AddKdiLogisticTo;
import rebue.kdi.to.EOrderTo;
import rebue.onl.dic.OnlineSubjectTypeDic;
import rebue.onl.mo.OnlOnlineMo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.mo.OnlOnlineSpecMo;
import rebue.onl.ro.ModifyOnlineSpecInfoRo;
import rebue.onl.svr.feign.OnlCartSvc;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.onl.svr.feign.OnlOnlineSpecSvc;
import rebue.onl.svr.feign.OnlOnlineSvc;
import rebue.onl.to.UpdateOnlineAfterOrderTo;
import rebue.onl.to.UpdateOnlineSpecAfterOrderTo;
import rebue.ord.dic.CancelDeliveryDic;
import rebue.ord.dic.CancellationOfOrderDic;
import rebue.ord.dic.CommissionStateDic;
import rebue.ord.dic.ModifyOrderRealMoneyDic;
import rebue.ord.dic.OrderSignInDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.dic.SetUpExpressCompanyDic;
import rebue.ord.dic.ShipmentConfirmationDic;
import rebue.ord.mapper.OrdOrderMapper;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailDeliverMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.ro.CancelDeliveryRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdBuyRelationRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.OrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailDeliverSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.ListOrderTo;
import rebue.ord.to.OrderDetailTo;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.OrderTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.suc.mo.SucUserMo;
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
public class OrdOrderSvcImpl extends MybatisBaseSvcImpl<OrdOrderMo, java.lang.Long, OrdOrderMapper> implements OrdOrderSvc {

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

    @Resource
    private OrdAddrSvc        ordAddrSvc;
    @Resource
    private OrdOrderDetailSvc orderDetailSvc;
    @Resource
    private OrdReturnSvc      returnSvc;
    @Resource
    private OrdTaskSvc        ordTaskSvc;
    @Resource
    private OnlOnlineSvc      onlineSvc;
    @Resource
    private OnlOnlineSpecSvc  onlOnlineSpecSvc;
    @Resource
    private OnlCartSvc        onlCartSvc;
    @Resource
    private KdiSvc            kdiSvc;
    @Resource
    private OnlOnlinePicSvc   onlOnlinePicSvc;
    @Resource
    private AfcSettleTaskSvc  afcSettleTaskSvc;
    @Resource
    private AfcRefundSvc      refundSvc;
    @Resource
    private SucUserSvc        sucUserSvc;
    @Resource
    private OrdBuyRelationSvc ordBuyRelationSvc;
    @Resource
    private OrdOrderSvc       thisSvc;
    @Resource
    private OrdSettleTaskSvc ordSettleTaskSvc;
    
    @Resource
  	 private OrdOrderDetailDeliverSvc ordOrderDetailDeliverSvc;

    /**
     * 执行取消用户订单时间
     */
    @Value("${ord.cancel-order-time}")
    private int               cancelOrderTime;

    /**
     * 执行用户订单签收时间
     */
    @Value("${ord.signin-order-time}")
    private int               signinOrderTime;

    @Resource
    private Mapper            dozerMapper;

    /**
     * 检查订单是否可结算
     * 1. 订单必须存在
     * 2. 订单必须处于签收状态
     * 3. 订单必须已经记录签收时间
     * 4. 已经超过订单启动结算的时间
     * 5. 如果订单还有退货中的申请未处理完成，不能结算
     */
    @Override
    public Boolean isSettleableOrder(final OrdOrderMo order) {
        _log.debug("订单信息: {}", order);

        if (OrderStateDic.SIGNED.getCode() != order.getOrderState()) {
            final String msg = "订单不处于已签收状态，不能添加结算任务";
            _log.error("{}: 订单信息-{}", msg, order);
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

        _log.debug("订单信息经过检查，可以启动开始结算的任务: {}", order);
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
        if (to.getAddrId() == null || to.getDetails() == null || to.getDetails().isEmpty()) {
            final String msg = "参数错误";
            _log.error("{}:{}", msg, "没有传入用户收货地址/订单详情");
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg(msg);
            return ro;
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
            _log.debug("根据上线ID获取上线信息");
            if (orderDetailTo.getOnlineId() == null || orderDetailTo.getOnlineSpecId() == null || orderDetailTo.getBuyCount() == null) {
                final String msg = "参数错误";
                _log.error("{}: {}", msg, "上线ID/上线规格ID/购买数量不能为空 " + orderDetailTo);
                ro.setResult(ResultDic.PARAM_ERROR);
                ro.setMsg(msg);
                return ro;
            }

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
            if (onlineSpecMo.getCurrentOnlineCount() - onlineSpecMo.getSaleCount() < orderDetailTo.getBuyCount()) {
                final String msg = "商品库存不足";
                _log.error("{}: onlineSpecMo-{}, 购买数量-{}", msg, onlineSpecMo, orderDetailTo.getBuyCount());
                ro.setResult(ResultDic.FAIL);
                ro.setMsg(msg);
                return ro;
            }

            _log.info("检查是否限制购买");
            // 如果是限制购买的商品，同一用户的所有购买数量不能超过限制的数量
            if (onlineSpecMo.getLimitCount() > 0) {
                // 计算更新后的库存
                final int buyerOrderedCount = orderDetailSvc.getBuyerOrderedCount(to.getUserId(), orderDetailTo.getOnlineSpecId());
                if (buyerOrderedCount + orderDetailTo.getBuyCount() > onlineSpecMo.getLimitCount()) {
                    final String msg = "已超过限购数量，不能再购买";
                    _log.error("{}: userId-{} onlineSpecId-{}", msg, to.getUserId(), orderDetailTo.getOnlineSpecId());
                    ro.setResult(ResultDic.FAIL);
                    ro.setMsg(msg);
                    return ro;
                }
            }

            // 开始添加订单详情
            final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
            orderDetailMo.setOnlineId(orderDetailTo.getOnlineId());
            orderDetailMo.setOnlineTitle(onlineMo.getOnlineTitle());
            orderDetailMo.setProductId(onlineMo.getProductId());
            orderDetailMo.setProductSpecId(onlineSpecMo.getProductSpecId());
            orderDetailMo.setIsDelivered(false);
            orderDetailMo.setSpecName(onlineSpecMo.getOnlineSpec());
            orderDetailMo.setBuyPrice(onlineSpecMo.getSalePrice());
            orderDetailMo.setBuyCount(orderDetailTo.getBuyCount());
            // 计算实际价格=单价*数量
            orderDetailMo.setActualAmount(onlineSpecMo.getSalePrice().multiply(BigDecimal.valueOf(orderDetailTo.getBuyCount())));
            orderDetailMo.setBuyUnit(onlineSpecMo.getSaleUnit());
            orderDetailMo.setCostPrice(onlineSpecMo.getCostPrice());
            orderDetailMo.setSupplierId(onlineMo.getSupplierId());
            orderDetailMo.setDeliverOrgId(onlineMo.getDeliverOrgId());
            orderDetailMo.setSubjectType(onlineMo.getSubjectType());
            if (OnlineSubjectTypeDic.NORMAL.getCode() == orderDetailMo.getSubjectType()) {
                orderDetailMo.setCashbackAmount(onlineSpecMo.getCashbackAmount());
                orderDetailMo.setCashbackTotal(new BigDecimal(String.valueOf(orderDetailTo.getBuyCount())).multiply(onlineSpecMo.getCashbackAmount()));
            } else if (OnlineSubjectTypeDic.BACK_COMMISSION.getCode() == orderDetailMo.getSubjectType()) {
                orderDetailMo.setCommissionSlot((byte) 2);
                orderDetailMo.setCommissionState((byte) CommissionStateDic.MATCHING.getCode());
                orderDetailMo.setCashbackAmount(BigDecimal.ZERO);
                orderDetailMo.setCashbackTotal(BigDecimal.ZERO);
            }

            // 添加要更新的上线规格信息
            final UpdateOnlineSpecAfterOrderTo specTo = new UpdateOnlineSpecAfterOrderTo();
            specTo.setOnlineId(orderDetailTo.getOnlineId());
            specTo.setOnlineSpecId(orderDetailTo.getOnlineSpecId());
            specTo.setBuyCount(orderDetailTo.getBuyCount());
            specTo.setCartId(orderDetailTo.getCartId());
            specList.add(specTo);

            // 添加订单详情到上线组织列表中(根据上线组织拆分订单详情)
            List<OrdOrderDetailMo> orderDetails = onlineOrgs.get(onlineMo.getOnlineOrgId());
            if (orderDetails == null) {
                orderDetails = new LinkedList<>();
                onlineOrgs.put(onlineMo.getOnlineOrgId(), orderDetails);
            }
            orderDetails.add(orderDetailMo);

        }

        _log.debug("通过地址ID获取地址详细信息");
        final OrdAddrMo addrMo = ordAddrSvc.getById(to.getAddrId());
        if (addrMo == null) {
            final String msg = "找不到下单的收货地址信息";
            _log.error("{}: addrId-{}", msg, to.getAddrId());
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg(msg);
            return ro;
        }
        _log.info("获取用户收货地址信息为：{}", addrMo);

        final Date now = new Date();
        // 支付订单ID
        final Long payOrderId = _idWorker.getId();
        // 根据上线组织拆单
        for (final Entry<Long, List<OrdOrderDetailMo>> onlineOrg : onlineOrgs.entrySet()) {
            final OrdOrderMo orderMo = new OrdOrderMo();
//            orderMo.setId(_idWorker.getId());
            orderMo.setOrderCode(_idWorker.getIdStr());                         // 订单编号 TODO 重写订单编号的生成算法
            orderMo.setOrderState((byte) OrderStateDic.ORDERED.getCode());      // 订单状态已下单
            orderMo.setOrderTime(now);                                          // 下单时间
            orderMo.setOnlineOrgId(onlineOrg.getKey());                         // 上线组织ID(卖家ID)
            orderMo.setPayOrderId(payOrderId);                                  // 支付订单ID

            orderMo.setUserId(to.getUserId());                  // 下单人用户ID

            _log.debug("遍历订单详情计算订单的下单金额");
            // 下单金额
            BigDecimal orderAmount = BigDecimal.ZERO;
            for (final OrdOrderDetailMo orderDetailMo : onlineOrg.getValue()) {
                // 计算订单的下单金额
                orderAmount = orderAmount.add(orderDetailMo.getBuyPrice().multiply(BigDecimal.valueOf(orderDetailMo.getBuyCount())));
            }
            _log.debug("订单的下单金额为: {}", orderAmount);
            orderMo.setOrderMoney(orderAmount);          // 下单金额
            orderMo.setRealMoney(orderAmount);           // 实际金额=下单金额

            // 用户留言
            final String orderMessages = to.getOrderMessages();
            if (!StringUtils.isBlank(orderMessages)) {
                orderMo.setOrderMessages(orderMessages);
            }

            // 收件人信息
            orderMo.setReceiverName(addrMo.getReceiverName());
            orderMo.setReceiverMobile(addrMo.getReceiverMobile());
            orderMo.setReceiverProvince(addrMo.getReceiverProvince());
            orderMo.setReceiverCity(addrMo.getReceiverCity());
            orderMo.setReceiverExpArea(addrMo.getReceiverExpArea());
            orderMo.setReceiverAddress(addrMo.getReceiverAddress());
            orderMo.setReceiverPostCode(addrMo.getReceiverPostCode());
            orderMo.setReceiverTel(addrMo.getReceiverTel());
            orderMo.setOrderTitle("大卖网络-购买商品");

            _log.info("添加订单信息的参数为：{}", orderMo);
            // 添加订单信息
            add(orderMo);
            // 添加订单详情
            for (final OrdOrderDetailMo orderDetailMo : onlineOrg.getValue()) {
                _log.info("添加订单详情的参数为：{}", orderDetailMo);
                orderDetailMo.setOrderId(orderMo.getId());
                orderDetailMo.setUserId(to.getUserId());
                orderDetailMo.setReturnState((byte) ReturnStateDic.NONE.getCode());
                orderDetailMo.setIsSettleBuyer(false);
                orderDetailSvc.add(orderDetailMo);
            }

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

        ro.setPayOrderId(payOrderId);
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("下单成功");
        return ro;
    }

    /**
     * 查询用户订单信息
     */
    @Override
    public List<Map<String, Object>> selectOrderInfo(final Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
                    _log.info("查询用户订单信息开始获取商品主图");
                    final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailMo.getOnlineId(), (byte) 1);
                    _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                    _log.info("根据上线ID查找上线商品信息");
                    _log.info("参数 " + orderDetailMo.getOnlineId());
                    final OnlOnlineMo onlineMo = onlineSvc.getById(orderDetailMo.getOnlineId());
                    _log.info("返回值{}", onlineMo);
                    _log.info("获取订单下家购买关系");
                    final OrdBuyRelationMo buyRelationMo = new OrdBuyRelationMo();
                    buyRelationMo.setUplineOrderDetailId(orderDetailMo.getId());
                    final List<OrdBuyRelationMo> ordBuyRelationResult = ordBuyRelationSvc.list(buyRelationMo);
                    final List<OrdBuyRelationRo> buyRelationList = new ArrayList<>();
                    if (ordBuyRelationResult.size() == 0) {
                        _log.info("下家购买关系为空");
                    } else {
                        for (int j = 0; j < ordBuyRelationResult.size(); j++) {
                            _log.info("获取下家用户昵称及头像");
                            final SucUserMo userMo = sucUserSvc.getById(ordBuyRelationResult.get(j).getDownlineUserId());
                            if (userMo == null) {
                                _log.info("用户信息为空");
                            } else {
                                _log.info("获取到的用户信息为：{}", userMo);
                                final OrdBuyRelationRo buyRelationRo = new OrdBuyRelationRo();
                                buyRelationRo.setDownlineUserNickName(userMo.getWxNickname());
                                buyRelationRo.setDownlineUserWxFace(userMo.getWxFace());
                                buyRelationRo.setIsSignIn(ordBuyRelationResult.get(j).getIsSignIn());
                                _log.info("添加的用户信息为：{}", buyRelationRo);
                                buyRelationList.add(buyRelationRo);
                            }
                        }
                    }
                    final OrderDetailRo orderDetailRo = new OrderDetailRo();
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
                    orderDetailRo.setCashbackCommissionSlot(orderDetailMo.getCommissionSlot());
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
        final List<Map<String, Object>> orderSpecList = new ArrayList<>();
        for (final OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            final Map<String, Object> specMap = new HashMap<>();
            specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
            specMap.put("specName", ordOrderDetailMo.getSpecName());
            specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
            orderSpecList.add(specMap);
        }
        _log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
        final ModifyOnlineSpecInfoRo specMap = onlOnlineSpecSvc.modifyOnlineSpecInfo(orderSpecList);
        _log.info("查询并修改上线规格信息的返回值为：{}", specMap);
        final int specResult = specMap.getResult().getCode();
        if (specResult != 1) {
            _log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.MODIFY_SPEC_COUNT_ERROR);
            cancellationOfOrderRo.setMsg("修改规格数量失败");
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
    public CancelDeliveryRo cancelDelivery(final OrdOrderMo mo) {
        final CancelDeliveryRo cancelDeliveryRo = new CancelDeliveryRo();
        final Map<String, Object> map = new HashMap<>();
        final long userId = mo.getUserId();
        final long id = mo.getId();
        map.put("userId", userId);
        map.put("orderCode", id);
        _log.info("用户查询订单的参数为：{}", String.valueOf(map));
        final List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消发货失败", id, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.ORDER_NOT_EXIST);
            cancelDeliveryRo.setMsg("订单不存在");
            return cancelDeliveryRo;
        }
        if (orderList.get(0).getOrderState() != OrderStateDic.PAID.getCode()) {
            _log.error("由于订单：{}处于非待发货状态，{}取消发货失败", id, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            cancelDeliveryRo.setMsg("当前状态不允许取消");
            return cancelDeliveryRo;
        }
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(id);
        final List<OrdOrderDetailMo> orderDetailList = orderDetailSvc.list(detailMo);
        _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        if (orderDetailList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消发货失败", id, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.ORDER_NOT_EXIST);
            cancelDeliveryRo.setMsg("订单不存在");
            return cancelDeliveryRo;
        }
        final List<Map<String, Object>> orderSpecList = new ArrayList<>();
        for (final OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            final Map<String, Object> specMap = new HashMap<>();
            specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
            specMap.put("specName", ordOrderDetailMo.getSpecName());
            specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
            orderSpecList.add(specMap);
        }
        _log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
        final ModifyOnlineSpecInfoRo specMap = onlOnlineSpecSvc.modifyOnlineSpecInfo(orderSpecList);
        _log.info("查询并修改上线规格信息的返回值为：{}", specMap);
        final int specResult = specMap.getResult().getCode();
        if (specResult < 1) {
            _log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
            cancelDeliveryRo.setResult(CancelDeliveryDic.MODIFY_SPEC_COUNT_ERROR);
            cancelDeliveryRo.setMsg("修改规格数量失败");
            return cancelDeliveryRo;
        }
        final Date date = new Date();
        mo.setCancelTime(date);
        final int updateResult = _mapper.cancelDeliveryUpdateOrderState(mo);
        if (updateResult != 1) {
            _log.error("{}取消发货：{}失败", userId, id);
            throw new RuntimeException("修改订单状态失败");
        }
        _log.info("{}发货订单：{}成功", userId, id);
        cancelDeliveryRo.setResult(CancelDeliveryDic.SUCCESS);
        cancelDeliveryRo.setMsg("取消发货成功");
        return cancelDeliveryRo;
    }

	/**
	 * 确认发货并修改订单状态
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ShipmentConfirmationRo shipmentConfirmation(final ShipmentConfirmationTo to) {
		final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();
		// 判断是否是已经支付状态
		if (to.getOrderState() != 2) {
			confirmationRo.setResult(ShipmentConfirmationDic.ERROR);
			confirmationRo.setMsg("订单不是已支付状态，不能发货");
			_log.info("订单的状态为：{}", to.getOrderState());
			return confirmationRo;
		}
		// 添加签收任务
		final OrdOrderMo mo = dozerMapper.map(to, OrdOrderMo.class);
		final Date date = new Date();
		mo.setSendTime(date);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, signinOrderTime);
		final Date executePlanTime = calendar.getTime();
		final OrdTaskMo ordTaskMo = new OrdTaskMo();
		ordTaskMo.setOrderId(String.valueOf(mo.getId()));
		//先查询任务是否已经存在
		_log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
		List<OrdTaskMo> ordTaskList=ordTaskSvc.list(ordTaskMo);
		_log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
		if(ordTaskList.size()==0) {
			ordTaskMo.setExecutePlanTime(executePlanTime);
			ordTaskMo.setExecuteState((byte) 0);
			ordTaskMo.setTaskType((byte) 2);
			_log.info("确认发货添加签收任务的参数为：{}", ordTaskMo);
			final int taskAddResult = ordTaskSvc.add(ordTaskMo);
			_log.info("确认发货添加签收任务的返回值为：{}", taskAddResult);
			if (taskAddResult != 1) {
				_log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
				throw new RuntimeException("添加签收任务出错");
			}
		}else {
			_log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
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

		// 根据快递电子面单返回的logisticId和selectDtailId和orderId来添加物流发货表和修改每条详情的发货状态
		for (Long detailId : to.getSelectDetailId()) {
			_log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态开始-------------------------------");
			OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
			ooddmo.setLogisticId(eOrderRo.getLogisticId());
			ooddmo.setOrderId(to.getId());
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
			OrdOrderDetailMo oodMo = new OrdOrderDetailMo();
			oodMo.setId(detailId);
			oodMo.setIsDelivered(true);
			_log.info("修改订单详情发货状态的参数为：{}", oodMo);
			result = orderDetailSvc.modify(oodMo);
			_log.info("修改订单详情发货状态结果为：{}", result);
			if (result != 1) {
				_log.error("添加订单详情发货状态失败");
				throw new RuntimeException("修改订单详情发货状态失败");
			}
			_log.info("根据当前订单详情循环插入发货表和修改订单详情发货状态结束++++++++++++++++++++++++++++++++");
		}

		// 根据没有当前订单详情的所有未详情Id和当前被选择的详情Id长度是否相等来决定是否修改订单状态为已发货。
		if (to.getAllDetaileId().size() == to.getSelectDetailId().size()) {
			_log.debug("需要修改订单状态，订单所有未发货详情等于被选择的详情：AllDetaileId长度-{}, SelectDetailId长度-{}", to.getAllDetaileId().size(),
					to.getSelectDetailId().size());
			_log.info("确认发货并修改订单状态，参数为：{}", mo);
			final int result = _mapper.shipmentConfirmation(mo);
			if (result != 1) {
				_log.info("确认发货并修改订单状态失败，返回值为：{}", result);
				throw new RuntimeException("添加发货表失败");
			}
			_log.info("确认发货并修改订单状态成功，返回值为：{}", result);
		} else {
			_log.info("不需要要修改订单状态，订单所有未发货详情不等于被选择的详情：AllDetaileId长度-{}, SelectDetailId长度-{}",
					to.getAllDetaileId().size(), to.getSelectDetailId().size());
			_log.info("确认发货成功");
		}

		confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
		confirmationRo.setMsg("确认发货成功");
		confirmationRo.setLogisticId(eOrderRo.getLogisticId());
		confirmationRo.setLogisticCode(eOrderRo.getLogisticCode());
		confirmationRo.setPrintPage(eOrderRo.getPrintPage());
		confirmationRo.setFailReason(eOrderRo.getFailReason());
		return confirmationRo;

	}
	
	/**
	 * 获取物流轨迹
	 */
	@Override
	public ShipmentConfirmationRo sendBySupplier(final ShipmentConfirmationTo to) {
		final ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();
		final OrdOrderMo mo = dozerMapper.map(to, OrdOrderMo.class);
		// 添加签收任务
		final Date date = new Date();
		mo.setSendTime(date);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, signinOrderTime);
		final Date executePlanTime = calendar.getTime();
		final OrdTaskMo ordTaskMo = new OrdTaskMo();
		ordTaskMo.setOrderId(String.valueOf(mo.getId()));
		//先查询任务是否已经存在
		_log.info("查看签收任务是否存在的参数为：{}", ordTaskMo);
		List<OrdTaskMo> ordTaskList=ordTaskSvc.list(ordTaskMo);
		_log.info("查看签收任务是否存在的结果为：{}", ordTaskList);
		if(ordTaskList.size()==0) {
			ordTaskMo.setExecutePlanTime(executePlanTime);
			ordTaskMo.setExecuteState((byte) 0);
			ordTaskMo.setTaskType((byte) 2);
			_log.info("确认发货添加签收任务的参数为：{}", ordTaskMo);
			final int taskAddResult = ordTaskSvc.add(ordTaskMo);
			_log.info("确认发货添加签收任务的返回值为：{}", taskAddResult);
			if (taskAddResult != 1) {
				_log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
				throw new RuntimeException("添加签收任务出错");
			}
		}else {
			_log.info("确认发货添加签收任务已经存在，orderId为：{}", ordTaskMo.getOrderId());
		}

		// 添加物流信息
		final AddKdiLogisticTo addKdiLogisticTo = dozerMapper.map(to, AddKdiLogisticTo.class);
		addKdiLogisticTo.setEntryType((byte) 2);
		addKdiLogisticTo.setOrderId(to.getId());
		_log.info("添加物流信息参数为：{}", addKdiLogisticTo);
		final KdiLogisticRo entryResult = kdiSvc.entryLogistics(addKdiLogisticTo);
		_log.info("添加物流信息结果为：{}", entryResult);
		if (entryResult.getResult() != 1) {
			_log.error("添加物流信息出错，订单编号为：{}", mo.getOrderCode());
			throw new RuntimeException("添加物流信息出错");
		}
		// 根据订单id查询订单详情并插入到发货表
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setOrderId(mo.getId());
		_log.info("根据订单id查询订单详情的参数为：{}", detailMo);
		List<OrdOrderDetailMo> detailList = orderDetailSvc.list(detailMo);
		_log.info("根据订单id查询订单详情的结果为：{}", detailList);
		for (OrdOrderDetailMo ordOrderDetailMo : detailList) {
			_log.info("根据当前订单详情循环插入发货表开始-------------------------------");
			OrdOrderDetailDeliverMo ooddmo = new OrdOrderDetailDeliverMo();
			ooddmo.setLogisticId(addKdiLogisticTo.getId());
			ooddmo.setOrderId(mo.getId());
			ooddmo.setOrderDetailId(ordOrderDetailMo.getId());
			_log.info("添加发货表的参数：{}", ooddmo);
			int result = ordOrderDetailDeliverSvc.add(ooddmo);
			if (result != 1) {
				throw new RuntimeException("添加发货表出错");
			}
			_log.info("根据当前订单详情循环插入发货表结束+++++++++++++++++++++++++++++++");
		}

		// 根据订单id来修改订单详情发货状态
		_log.info("根据订单id修改订单详情发货状态的参数为：{}", mo.getId());
		int result = orderDetailSvc.modifyIsDeliverByOrderId(mo.getId());
		_log.info("根据订单id修改订单详情发货状态的结果为：{}", result);
		if (result < 1) {
			_log.error("修改订单详情发货状态失败，订单id为：{}", mo.getId());
			throw new RuntimeException("修改订单详情发货状态失败");
		}
		// 修改订单状态
		_log.info("获取物流轨迹并修改订单状态的参数为：{}", mo);
		result = 0;
		result = _mapper.shipmentConfirmation(mo);
		_log.info("确认发货并修改订单状态的返回值为：{}", result);
		if (result != 1) {
			_log.error("修改订单发货状态失败，订单id为：{}", mo.getId());
			throw new RuntimeException("修改订单发货状态失败");
		}
		confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
		confirmationRo.setMsg("确认发货成功");
		return confirmationRo;
	}

    /**
     * 订单签收
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderSignInRo orderSignIn(final OrderSignInTo to) {
        final OrderSignInRo orderSignInRo = new OrderSignInRo();
        final Map<String, Object> map = new HashMap<>();
        final String orderCode = to.getOrderCode();
        map.put("id", orderCode);
        _log.info("用户查询订单的参数为：{}", String.valueOf(map));
        final List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，取消订单失败", orderCode);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        final long userId = orderList.get(0).getUserId();
        final long orderId = orderList.get(0).getId();
        if (orderList.get(0).getOrderState() != OrderStateDic.DELIVERED.getCode()) {
            _log.error("由于订单：{}处于非待签收状态，{}签收订单失败", orderCode, userId);
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
            _log.error("订单签收查询订单详情时发现没有该订单的订单详情，订单编号为：{}", orderCode);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        final Date date = new Date();
        _log.info("订单签收的时间为：{}", date);
        for (final OrdOrderDetailMo ordOrderDetailMo : detailList) {
            if (ordOrderDetailMo.getSubjectType() == 1) {
                final OrdBuyRelationMo mo = new OrdBuyRelationMo();
                mo.setDownlineOrderDetailId(ordOrderDetailMo.getId());
                final OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(mo);
                _log.info("根据订单详情获取订单购买关系为{}", buyRelationResult);
                if (buyRelationResult != null) {
                    _log.info("订单签收更新购买关系表");
                    final OrdBuyRelationMo updateBuyRelationMo = new OrdBuyRelationMo();
                    updateBuyRelationMo.setId(buyRelationResult.getId());
                    updateBuyRelationMo.setIsSignIn(true);
                    final int updateBuyRelationResult = ordBuyRelationSvc.modify(updateBuyRelationMo);
                    if (updateBuyRelationResult < 1) {
                        _log.error("{}更新购买关系出错，返回值为：{}", userId, updateBuyRelationResult);
                        orderSignInRo.setResult(OrderSignInDic.ERROR);
                        orderSignInRo.setMsg("更新购买关系失败");
                        return orderSignInRo;
                    }
                }
            }
        }
        
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setId(Long.parseLong(orderCode));
        orderMo.setUserId(userId);
        orderMo.setReceivedTime(date);
        orderMo.setReceivedOpId(userId);
        orderMo.setOrderState((byte) OrderStateDic.DELIVERED.getCode());
        _log.info("订单签收的参数为：{}", orderMo);
        final int signInResult = _mapper.orderSignIn(orderMo);
        _log.info("订单签收的返回值为：{}", signInResult);
        if (signInResult < 1) {
            _log.error("{}签收订单出错，返回值为：{}", userId, signInResult);
            final String msg = "签收失败";
            throw new RuntimeException(msg);
        }
        
        _log.info("订单签收取消自动签收任务的参数为：{}", orderId);
        Ro cancelTaskRo = ordTaskSvc.cancelTask(orderId, OrderTaskTypeDic.SIGNED);
        _log.info("订单签收取消自动签收任务的返回值为：{}", cancelTaskRo);
        if (cancelTaskRo.getResult() != ResultDic.SUCCESS) {
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
    public int modifyRefund(final BigDecimal refundTotal, final Byte orderState, final Long whereOrderId, final BigDecimal whereRefundedTotal) {
        _log.info("修改退款金额(根据订单ID和已退款总额): 退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", refundTotal, orderState, whereOrderId, whereRefundedTotal);
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
     * 处理订单支付完成的通知
     * 1. 根据支付订单ID获取所有订单(如果没有找到，退款)
     * 2. 判断通知回来的支付金额是否和订单中记录的实际交易金额相同(如果不同，退款)
     * 3. 取消订单自动取消任务
     * 4. 按不同发货组织拆单，并重新计算拆单后的订单实际交易金额
     * 5. 匹配购买关系
     * 6. 根据支付订单ID修改订单状态为已支付
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
            final RefundGoBackTo refundGoBackTo = new RefundGoBackTo();
            refundGoBackTo.setOrderId(payDoneMsg.getOrderId());
            refundGoBackTo.setTradeTitle(msg);
            refundSvc.refundGoBack(refundGoBackTo);
            return true;
        }
        _log.debug("根据支付订单ID获取所有订单的结果: {}", orders);

        // 计算订单总额(所有订单中记录的实际交易金额之和)
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (final OrdOrderMo order : orders) {
            orderTotal = orderTotal.add(order.getRealMoney());
        }
        _log.debug("计算订单总额的结果是: {}", orderTotal);

        _log.info("2. 判断通知回来的支付金额是否和订单中记录的实际交易金额相同(如果不同，退款)");
        if (!payDoneMsg.getPayAmount().equals(orderTotal)) {
            final String msg = "支付金额与订单中记录的实际金额不一致(" + payDoneMsg.getPayAmount() + ":" + orderTotal + ")，只能退款";
            _log.warn("{}: payOrderId-{}\n可能是在去支付订单后未收到通知时修改了订单的实际金额", msg, payOrderId);
            final RefundGoBackTo refundGoBackTo = new RefundGoBackTo();
            refundGoBackTo.setOrderId(payDoneMsg.getOrderId());
            refundGoBackTo.setTradeTitle(msg);
            Ro refundGoBackRo = refundSvc.refundGoBack(refundGoBackTo);
            return refundGoBackRo.getResult().equals(ResultDic.SUCCESS);
        }
        
        _log.info("3. 取消订单自动取消任务");
        for (OrdOrderMo ordOrderMo : orders) {
        	_log.info("处理订单支付完成的通知取消订单自动取消任务的参数为：{}", ordOrderMo.getId());
			Ro cancelTaskRo = ordTaskSvc.cancelTask(ordOrderMo.getId(), OrderTaskTypeDic.CANCEL);
			_log.info("处理订单支付完成的通知取消订单自动取消任务的返回值为：{}", cancelTaskRo);
			if (cancelTaskRo.getResult() != ResultDic.SUCCESS) {
				_log.error("处理订单支付完成的通知取消订单自动取消任务时出现错误，结算id为：{}, 订单id为：{}", payOrderId, ordOrderMo.getId());
				return false;
			}
		}

        final List<OrdOrderDetailMo> orderDetailAlls = new LinkedList<>();
        _log.info("4. 按不同发货组织拆单，并重新计算拆单后的订单实际交易金额");
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
                    _log.debug("修改订单的发货组织为此详情的发货组织");
                    final OrdOrderMo modifyMo = new OrdOrderMo();
                    modifyMo.setId(order.getId());
                    modifyMo.setDeliverOrgId(orderDetail.getDeliverOrgId());
                    thisSvc.modify(modifyMo);
                    continue;
                }
                OrdOrderMo tempOrder = orderMap.get(orderDetail.getDeliverOrgId());
                if (tempOrder == null) {
                    _log.debug("发现不同发货组织的订单详情，需要添加新订单");
                    tempOrder = dozerMapper.map(order, OrdOrderMo.class);
                    tempOrder.setId(null);
                    tempOrder.setDeliverOrgId(orderDetail.getDeliverOrgId());
                    thisSvc.add(tempOrder);
                }
                if (tempOrder.getId() != orderDetail.getOrderId()) {
                    _log.debug("找到的订单不是原来详情的订单，替换详情的父订单为找到的订单");
                    final OrdOrderDetailMo modifyMo = new OrdOrderDetailMo();
                    modifyMo.setId(orderDetail.getId());
                    modifyMo.setOrderId(tempOrder.getId());
                    orderDetailSvc.modify(modifyMo);
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

        _log.info("5. 匹配购买关系");
        _log.info("遍历订单详情: 添加订单购买关系");
        for (final OrdOrderDetailMo orderDetail : orderDetailAlls) {
            try {
                _log.debug("订单详情商品类型为：{}" + orderDetail.getSubjectType());
                if (orderDetail.getSubjectType() == 1) {
                    _log.debug("全返商品添加购买关系");
                    final long userId = orderDetail.getUserId();
                    final long onlineId = orderDetail.getOnlineId();
                    final BigDecimal buyPrice = orderDetail.getBuyPrice();
                    final long downLineDetailId = orderDetail.getId();
                    final long downLineOrderId = orderDetail.getOrderId();
                    final String matchBuyRelationResult = ordBuyRelationSvc.matchBuyRelation(userId, onlineId, buyPrice, downLineDetailId, downLineOrderId);
                    _log.debug(matchBuyRelationResult);
                }
            } catch (final Exception e) {
                _log.error("匹配购买关系报错：", e);
            }
        }

        _log.info("6. 根据支付订单ID修改订单状态为已支付");
        _log.info("订单支付完成，根据PAY_ORDER_ID修改订单状态为已支付: payOrderId-{}, payTime-{}", payOrderId, payDoneMsg.getPayTime());
        final int result = _mapper.paidOrder(payOrderId, payDoneMsg.getPayTime());
        _log.debug("订单支付完成通知修改订单信息的返回值为：{}", result);
        if (result == 0) {
            _log.warn("根据支付订单ID修改订单状态为已支付不成功，可能碰到并发的问题: payOrderId-{}", payOrderId);
            return false;
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
     * 查询用户待返现订单信息
     */
    // @Override
    // public List<Map<String, Object>> getCashBackOrder(Map<String, Object> map)
    // throws ParseException, IntrospectionException, IllegalAccessException,
    // IllegalArgumentException, InvocationTargetException {
    // List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    // _log.info("查询用户待返现任务的参数为：{}", map.toString());
    // long accountId = Long.parseLong(String.valueOf(map.get("userId")));
    // byte executeState = (byte) SettleTaskExecuteStateDic.NONE.getCode();
    // byte tradType = (byte) TradeTypeDic.SETTLE_CASHBACK.getCode();
    // byte pageNum = Byte.parseByte(String.valueOf(map.get("pageNum")));
    // byte pageSize = Byte.parseByte(String.valueOf(map.get("pageSize")));
    // List<AfcSettleTaskMo> cashBackList =
    // afcSettleTaskSvc.getCashBackTask(accountId, executeState, tradType, pageNum,
    // pageSize);
    // _log.info("获取到的用户待返现任务信息为：{}", String.valueOf(cashBackList));
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd
    // HH:mm:ss");
    // if (cashBackList.size() != 0) {
    // for (int i = 0; i < cashBackList.size(); i++) {
    // OrdOrderMo mo = new OrdOrderMo();
    // _log.info("获取定单信息的订单号为：{}", cashBackList.get(i).getOrderId());
    // mo.setOrderCode(cashBackList.get(i).getOrderId());
    // List<OrdOrderMo> orderInfo = _mapper.selectSelective(mo);
    // if (orderInfo.size() == 0) {
    // _log.info("根据订单号查询订单为空：{}");
    // continue;
    // }
    // _log.info("获取到的订单信息为：{}", String.valueOf(orderInfo));
    // Map<String, Object> hm = new HashMap<String, Object>();
    // String l = simpleDateFormat.format(cashBackList.get(i).getExecutePlanTime());
    // Date date = simpleDateFormat.parse(l);
    // long ts = date.getTime();
    // _log.info("转换时间得到的时间戳为：{}", ts);
    // hm.put("dateline", ts / 1000);
    // hm.put("finishDate", ts / 1000 + 86400);
    // hm.put("system", System.currentTimeMillis() / 1000);
    // OrdOrderMo obj = orderInfo.get(0);
    // BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
    // PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    // for (PropertyDescriptor property : propertyDescriptors) {
    // String key = property.getName();
    // if (!key.equals("class")) {
    // Method getter = property.getReadMethod();
    // Object value = getter.invoke(obj);
    // hm.put(key, value);
    // }
    // }
    // _log.info("查询用户待返现订单信息hm里面的值为：{}", String.valueOf(hm));
    // OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
    // detailMo.setOrderId(Long.parseLong(cashBackList.get(i).getOrderId()));
    // _log.info("查询用户待返现订单获取订单详情的参数为：{}", detailMo.toString());
    // List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
    // _log.info("查询用户订单信息获取订单详情的返回值为：{}", String.valueOf(orderDetailList));
    // List<OrderDetailRo> orderDetailRoList = new ArrayList<OrderDetailRo>();
    // for (OrdOrderDetailMo orderDetailMo : orderDetailList) {
    // _log.info("查询用户订单信息开始获取商品主图");
    // List<OnlOnlinePicMo> onlinePicList =
    // onlOnlinePicSvc.list(orderDetailMo.getOnlineId(), (byte) 1);
    // _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
    // OrderDetailRo orderDetailRo = new OrderDetailRo();
    // orderDetailRo.setId(orderDetailMo.getId());
    // orderDetailRo.setOrderId(orderDetailMo.getOrderId());
    // orderDetailRo.setOnlineId(orderDetailMo.getOnlineId());
    // orderDetailRo.setProductId(orderDetailMo.getProductId());
    // orderDetailRo.setOnlineTitle(orderDetailMo.getOnlineTitle());
    // orderDetailRo.setSpecName(orderDetailMo.getSpecName());
    // orderDetailRo.setBuyCount(orderDetailMo.getBuyCount());
    // orderDetailRo.setBuyPrice(orderDetailMo.getBuyPrice());
    // orderDetailRo.setCashbackAmount(orderDetailMo.getCashbackAmount());
    // orderDetailRo.setBuyUnit(orderDetailMo.getBuyUnit());
    // orderDetailRo.setReturnState(orderDetailMo.getReturnState());
    // orderDetailRo.setGoodsQsmm(onlinePicList.get(0).getPicPath());
    // orderDetailRo.setReturnCount(orderDetailMo.getReturnCount());
    // orderDetailRo.setCashbackTotal(orderDetailMo.getCashbackTotal());
    // orderDetailRoList.add(orderDetailRo);
    // }
    // hm.put("items", orderDetailRoList);
    // list.add(i, hm);
    // }
    // }
    // _log.info("最新获取用户订单信息的返回值为：{}", String.valueOf(list));
    // return list;
    // }

    /**
     * 分页查询订单
     */
    @Override
    public PageInfo<OrdOrderRo> listOrder(final ListOrderTo to, final int pageNum, final int pageSize) {
        _log.info("获取订单的参数为: {}", to);
        _log.info("orderList: ro-{}; pageNum-{}; pageSize-{}", to, pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> _mapper.listOrder(to));
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
}
