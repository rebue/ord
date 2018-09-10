package rebue.ord.svc.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.afc.dic.SettleTaskExecuteStateDic;
import rebue.afc.dic.TradeTypeDic;
import rebue.afc.mo.AfcSettleTaskMo;
import rebue.afc.ro.AddSettleTasksRo;
import rebue.afc.svr.feign.AfcSettleTaskSvc;
import rebue.afc.to.AddSettleTasksDetailTo;
import rebue.afc.to.AddSettleTasksTo;
import rebue.kdi.mo.KdiCompanyMo;
import rebue.kdi.mo.KdiSenderMo;
import rebue.kdi.ro.EOrderRo;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.kdi.to.EOrderTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.ro.DeleteCartAndModifyInventoryRo;
import rebue.onl.ro.ModifyOnlineSpecInfoRo;
import rebue.onl.svr.feign.OnlCartSvc;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.onl.svr.feign.OnlOnlineSpecSvc;
import rebue.onl.svr.feign.OnlOnlineSvc;
import rebue.ord.dic.CancelDeliveryDic;
import rebue.ord.dic.CancellationOfOrderDic;
import rebue.ord.dic.ModifyOrderRealMoneyDic;
import rebue.ord.dic.OrderSignInDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.SetUpExpressCompanyDic;
import rebue.ord.dic.ShipmentConfirmationDic;
import rebue.ord.dic.UsersToPlaceTheOrderDic;
import rebue.ord.mapper.OrdOrderMapper;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.ro.CancelDeliveryRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.ro.UsersToPlaceTheOrderRo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.ShipmentConfirmationTo;
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
public class OrdOrderSvcImpl extends MybatisBaseSvcImpl<OrdOrderMo, java.lang.Long, OrdOrderMapper> implements OrdOrderSvc {

    /**
     * @mbg.generated
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(OrdOrderMo mo) {
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    /**
     * @mbg.generated
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdOrderSvcImpl.class);

    /**
     */
    @Resource
    private OrdAddrSvc          ordAddrSvc;

    /**
     */
    @Resource
    private OrdOrderDetailSvc   ordOrderDetailSvc;

    @Resource
    private OrdTaskSvc          ordTaskSvc;

    /**
     */
    @Resource
    private OnlOnlineSpecSvc    onlOnlineSpecSvc;

    /**
     */
    @Resource
    private OnlOnlineSvc        onlOnlineSvc;

    /**
     */
    @Resource
    private OnlCartSvc          onlCartSvc;

    /**
     */
    @Resource
    private KdiSvc              kdiSvc;

    /**
     */
    @Resource
    private OnlOnlinePicSvc     onlOnlinePicSvc;

    /**
     */
    @Resource
    private AfcSettleTaskSvc    afcSettleTaskSvc;

    /**
     * 买家返款时间
     */
    @Value("${ord.settle-buyer-cashback-time}")
    private int                 settleBuyerCashbackTime;

    /**
     * 执行取消用户订单时间
     */
    @Value("${ord.cancel-order-time}")
    private int                 cancelOrderTime;

    /**
     * 执行用户订单签收时间
     */
    @Value("${ord.signin-order-time}")
    private int                 signinOrderTime;

    @Resource
    private Mapper              dozerMapper;
    @Resource
    private ObjectMapper        objectMapper;

    /**
     * 用户下订单 Title: placeOrder Description:
     *
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @date 2018年4月9日 上午10:53:44
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UsersToPlaceTheOrderRo usersToPlaceTheOrder(String orderJson) throws JsonParseException, JsonMappingException, IOException {
        UsersToPlaceTheOrderRo placeTheOrderRo = new UsersToPlaceTheOrderRo();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, OrdOrderRo.class);

        List<OrdOrderRo> orderList = objectMapper.readValue(orderJson, javaType);
        _log.info("用户下单的参数为：{}", orderList.toString());
        OrdAddrMo addrMo = new OrdAddrMo();
        addrMo.setId(orderList.get(0).getAddress());
        addrMo.setUserId(orderList.get(0).getUserId());
        _log.info("获取用户收货地址信息的参数为：{}", addrMo);
        List<OrdAddrMo> addrList = ordAddrSvc.list(addrMo);
        _log.info("根据收货地址编号和用户编号获取用户收货地址信息的返回值为：{}", addrList.toString());
        if (addrList.size() == 0) {
            _log.error("用户下订单时出现收货地址为空，用户编号为：{}", orderList.get(0).getUserId());
            placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.DELIVERY_ADDRESS_NOT_NULL);
            placeTheOrderRo.setMsg("收货地址不能为空");
            return placeTheOrderRo;
        }
        String orderTitle = "";
        if (orderList.size() > 1) {
            orderTitle = String.valueOf(orderList.get(0).getOnlineTitle()) + "等商品购买。。。";
        } else {
            orderTitle = String.valueOf(orderList.get(0).getOnlineTitle());
        }
        Date date = new Date();
        long orderId = _idWorker.getId();
        long userId = orderList.get(0).getUserId();
        OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setOrderCode(String.valueOf(orderId));
        orderMo.setOrderTitle(orderTitle);
        orderMo.setOrderMoney(orderList.get(0).getTotalPrice());
        orderMo.setRealMoney(orderList.get(0).getTotalPrice());
        orderMo.setOrderState((byte) 1);
        orderMo.setUserId(userId);
        orderMo.setUserName(orderList.get(0).getUserName());
        orderMo.setOrderTime(date);
        orderMo.setReceiverName(addrList.get(0).getReceiverName());
        orderMo.setReceiverMobile(addrList.get(0).getReceiverMobile());
        orderMo.setReceiverProvince(addrList.get(0).getReceiverProvince());
        orderMo.setReceiverCity(addrList.get(0).getReceiverCity());
        orderMo.setReceiverExpArea(addrList.get(0).getReceiverExpArea());
        orderMo.setReceiverAddress(addrList.get(0).getReceiverAddress());
        String orderMessages = orderList.get(0).getOrderMessages();
        if (orderMessages != null && !orderMessages.equals("") && !orderMessages.equals("null")) {
            orderMo.setOrderMessages(orderMessages);
        }
        if (addrList.get(0).getReceiverPostCode() != null && !addrList.get(0).getReceiverPostCode().equals("")) {
            orderMo.setReceiverPostCode(addrList.get(0).getReceiverPostCode());
        }
        if (addrList.get(0).getReceiverTel() != null && !addrList.get(0).getReceiverTel().equals("")) {
            orderMo.setReceiverTel(addrList.get(0).getReceiverTel());
        }
        _log.info("添加订单信息的参数为：{}", orderMo);
        int insertOrderResult = add(orderMo);
        _log.info("添加订单信息的返回值为：{}", insertOrderResult);
        if (insertOrderResult != 1) {
            _log.error("{}添加订单信息失败", userId);
            placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.CREATE_ORDER_ERROR);
            placeTheOrderRo.setMsg("生成订单出错");
            return placeTheOrderRo;
        }
        List<DeleteCartAndModifyInventoryRo> cartAndSpecList = new ArrayList<DeleteCartAndModifyInventoryRo>();
        for (int i = 0; i < orderList.size(); i++) {
            long onlineId = orderList.get(i).getOnlineId();
            String OnlineSpec = orderList.get(i).getOnlineSpec();
            int buyCount = orderList.get(i).getNumber();
            byte subjectType = orderList.get(i).getSubjectType();
            DeleteCartAndModifyInventoryRo deleteCartAndModifyInventoryRo = new DeleteCartAndModifyInventoryRo();
            deleteCartAndModifyInventoryRo.setOnlineId(onlineId);
            deleteCartAndModifyInventoryRo.setBuyCount(buyCount);
            deleteCartAndModifyInventoryRo.setOnlineSpec(OnlineSpec);
            deleteCartAndModifyInventoryRo.setCartId(orderList.get(i).getCartId());
            cartAndSpecList.add(deleteCartAndModifyInventoryRo);
            if (subjectType == 0) {
                _log.info("普通商品添加订单详情");
                OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setId(_idWorker.getId());
                detailMo.setOrderId(orderId);
                detailMo.setOnlineId(onlineId);
                detailMo.setProduceId(orderList.get(i).getProductId());
                detailMo.setOnlineTitle(orderList.get(i).getOnlineTitle());
                detailMo.setSpecName(OnlineSpec);
                detailMo.setBuyCount(buyCount);
                detailMo.setBuyPrice(orderList.get(i).getSalePrice());
                detailMo.setCashbackAmount(orderList.get(i).getCashbackAmount());
                detailMo.setReturnState((byte) 0);
                detailMo.setUserId(userId);
                detailMo.setCashbackTotal(new BigDecimal(String.valueOf(buyCount)).multiply(orderList.get(i).getCashbackAmount()));
                _log.info("添加订单详情的参数为：{}", detailMo);
                int intserOrderDetailresult = ordOrderDetailSvc.add(detailMo);
                _log.info("添加订单详情的返回值为：{}", intserOrderDetailresult);
                if (intserOrderDetailresult != 1) {
                    _log.error("{}添加订单详情失败", userId);
                    throw new RuntimeException("生成订单详情出错");
                }
            } else {
                _log.info("全返商品添加订单详情");
                for (int j = 0; j < buyCount; j++) {
                    OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                    detailMo.setId(_idWorker.getId());
                    detailMo.setOrderId(orderId);
                    detailMo.setOnlineId(onlineId);
                    detailMo.setProduceId(orderList.get(i).getProductId());
                    detailMo.setOnlineTitle(orderList.get(i).getOnlineTitle());
                    detailMo.setSpecName(OnlineSpec);
                    detailMo.setBuyCount(1);
                    detailMo.setBuyPrice(orderList.get(i).getSalePrice());
                    detailMo.setCashbackAmount(orderList.get(i).getCashbackAmount());
                    detailMo.setReturnState((byte) 0);
                    detailMo.setUserId(userId);
                    detailMo.setCashbackTotal(new BigDecimal(String.valueOf(buyCount)).multiply(orderList.get(i).getCashbackAmount()));
                    _log.info("添加订单详情的参数为：{}", detailMo);
                    int intserOrderDetailresult = ordOrderDetailSvc.add(detailMo);
                    _log.info("添加订单详情的返回值为：{}", intserOrderDetailresult);
                    if (intserOrderDetailresult != 1) {
                        _log.error("{}添加订单详情失败", userId);
                        throw new RuntimeException("生成订单详情出错");
                    }
                }
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, cancelOrderTime);
        // 取消订单的时间
        Date executePlanTime = calendar.getTime();
        OrdTaskMo ordTaskMo = new OrdTaskMo();
        ordTaskMo.setExecuteState((byte) 0);
        ordTaskMo.setExecutePlanTime(executePlanTime);
        ordTaskMo.setTaskType((byte) 1);
        ordTaskMo.setOrderId(String.valueOf(orderId));
        _log.info("用户下订单添加取消订单任务的参数为：{}", ordTaskMo);
        // 添加取消订单任务
        int taskAddResult = ordTaskSvc.add(ordTaskMo);
        _log.info("用户下订单添加取消订单任务的返回值为：{}", taskAddResult);
        if (taskAddResult != 1) {
            _log.error("用户下订单添加取消订单任务时出现错误，用户编号为：{}", userId);
            throw new RuntimeException("添加取消订单任务失败");
        }
        _log.info("删除购物车和修改上线数量的参数为：{}", String.valueOf(cartAndSpecList));
        Map<String, Object> deleteAndUpdateMap = onlOnlineSpecSvc.deleteCartAndUpdateOnlineCount(objectMapper.writeValueAsString(cartAndSpecList));
        if (deleteAndUpdateMap == null || deleteAndUpdateMap.size() == 0) {
            _log.error("{}删除购物车和修改上线数量失败", userId);
            throw new RuntimeException(String.valueOf(deleteAndUpdateMap.get("msg")));
        }
        _log.info("删除购物车和修改上线数量的返回值为：{}", String.valueOf(deleteAndUpdateMap));
        int deleteAndUpdateResult = Integer.parseInt(String.valueOf(deleteAndUpdateMap.get("result")));
        if (deleteAndUpdateResult != 1) {
            _log.error("{}删除购物车和修改上线数量失败", userId);
            throw new RuntimeException(String.valueOf(deleteAndUpdateMap.get("msg")));
        }
        placeTheOrderRo.setOrderId(orderId);
        placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.SUCCESS);
        placeTheOrderRo.setMsg("下单成功");
        return placeTheOrderRo;
    }

    /**
     * 查询用户订单信息 2018年4月9日16:49:17
     *
     * @throws ParseException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Override
    public List<Map<String, Object>> selectOrderInfo(Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        _log.info("查询用户订单信息的参数为：{}", map.toString());
        List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("获取到的用户订单信息为：{}", String.valueOf(orderList));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (orderList.size() != 0) {
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> hm = new HashMap<String, Object>();
                // 下单时间
                String orderTime = simpleDateFormat.format(orderList.get(i).getOrderTime());
                Date date = new Date();
                date = simpleDateFormat.parse(orderTime);
                long orderTimes = date.getTime();
                _log.info("转换下单时间得到的时间戳为：{}", orderTimes);
                if (orderList.get(i).getSendTime() != null) {
                    // 发货时间
                    String sendTime = simpleDateFormat.format(orderList.get(i).getSendTime());
                    date = new Date();
                    date = simpleDateFormat.parse(sendTime);
                    long sendTimes = date.getTime();
                    _log.info("转换发货时间得到的时间戳为：{}", orderTimes);
                    hm.put("sendTimes", sendTimes / 1000);
                }
                if (orderList.get(i).getReceivedTime() != null) {
                    // 签收时间
                    String receivedTime = simpleDateFormat.format(orderList.get(i).getReceivedTime());
                    date = new Date();
                    date = simpleDateFormat.parse(receivedTime);
                    long receivedTimes = date.getTime();
                    _log.info("转换发货时间得到的时间戳为：{}", receivedTimes);
                    hm.put("receivedTimes", receivedTimes / 1000);
                }
                hm.put("orderTimes", orderTimes / 1000);
                // 系统时间戳
                hm.put("system", System.currentTimeMillis() / 1000);
                OrdOrderMo obj = orderList.get(i);
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    if (!key.equals("class")) {
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(obj);
                        hm.put(key, value);
                    }
                }
                _log.info("查询用户订单信息hm里面的值为：{}", String.valueOf(hm));
                OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setOrderId(Long.parseLong(orderList.get(i).getOrderCode()));
                _log.info("查询用户订单信息获取订单详情的参数为：{}", detailMo.toString());
                List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
                _log.info("查询用户订单信息获取订单详情的返回值为：{}", String.valueOf(orderDetailList));
                List<OrderDetailRo> orderDetailRoList = new ArrayList<OrderDetailRo>();
                for (OrdOrderDetailMo orderDetailMo : orderDetailList) {
                    _log.info("查询用户订单信息开始获取商品主图");
                    List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailMo.getOnlineId(), (byte) 1);
                    _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                    OrderDetailRo orderDetailRo = new OrderDetailRo();
                    orderDetailRo.setId(orderDetailMo.getId());
                    orderDetailRo.setOrderId(orderDetailMo.getOrderId());
                    orderDetailRo.setOnlineId(orderDetailMo.getOnlineId());
                    orderDetailRo.setProduceId(orderDetailMo.getProduceId());
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
     * 用户取消订单 2018年4月9日18:57:36
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CancellationOfOrderRo cancellationOfOrder(OrdOrderMo mo) {
        CancellationOfOrderRo cancellationOfOrderRo = new CancellationOfOrderRo();
        Map<String, Object> map = new HashMap<String, Object>();
        String orderCode = mo.getOrderCode();
        map.put("orderCode", orderCode);
        List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，取消订单失败", orderCode);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.ORDER_NOT_EXIST);
            cancellationOfOrderRo.setMsg("订单不存在");
            return cancellationOfOrderRo;
        }
        long userId = orderList.get(0).getUserId();
        if (orderList.get(0).getOrderState() != OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode()) {
            _log.error("由于订单：{}处于非待支付状态，{}取消订单失败", orderCode, userId);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            cancellationOfOrderRo.setMsg("当前状态不允许取消");
            return cancellationOfOrderRo;
        }
        OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(Long.parseLong(orderCode));
        List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
        _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        if (orderDetailList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.ORDER_NOT_EXIST);
            cancellationOfOrderRo.setMsg("订单不存在");
            return cancellationOfOrderRo;
        }
        List<Map<String, Object>> orderSpecList = new ArrayList<Map<String, Object>>();
        for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            Map<String, Object> specMap = new HashMap<String, Object>();
            specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
            specMap.put("specName", ordOrderDetailMo.getSpecName());
            specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
            orderSpecList.add(specMap);
        }
        _log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
        ModifyOnlineSpecInfoRo specMap = onlOnlineSpecSvc.modifyOnlineSpecInfo(orderSpecList);
        _log.info("查询并修改上线规格信息的返回值为：{}", specMap);
        int specResult = specMap.getResult().getCode();
        if (specResult != 1) {
            _log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
            cancellationOfOrderRo.setResult(CancellationOfOrderDic.MODIFY_SPEC_COUNT_ERROR);
            cancellationOfOrderRo.setMsg("修改规格数量失败");
            return cancellationOfOrderRo;
        }
        Date date = new Date();
        mo.setCancelTime(date);
        mo.setOrderState((byte) OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode());
        _log.info("取消订单并修改状态的参数为：", mo);
        int updateResult = _mapper.cancellationOrderUpdateOrderState(mo);
        _log.info("取消订单并修改状态的返回值为：{}", updateResult);
        if (updateResult != 1) {
            _log.error("{}取消订单：{}失败", userId, orderCode);
            throw new RuntimeException("修改订单状态失败");
        }
        _log.info("{}取消订单：{}成功", userId, orderCode);
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
    public ModifyOrderRealMoneyRo modifyOrderRealMoney(OrdOrderMo mo) {
        ModifyOrderRealMoneyRo modifyOrderRealMoneyRo = new ModifyOrderRealMoneyRo();
        _log.info("修改订单实际金额的参数为：{}", mo);
        int result = _mapper.updateOrderRealMoney(mo);
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
     * 设置快递公司 Title: setTheCourier Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 上午11:12:39
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public SetUpExpressCompanyRo setUpExpressCompany(OrdOrderMo mo) {
        SetUpExpressCompanyRo expressCompanyRo = new SetUpExpressCompanyRo();
        _log.info("设置快递公司的参数为：{}", mo);
        int result = _mapper.setUpExpressCompany(mo);
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
     * 取消发货 2018年5月16日10:22:40
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CancelDeliveryRo cancelDelivery(OrdOrderMo mo) {
        CancelDeliveryRo cancelDeliveryRo = new CancelDeliveryRo();
        Map<String, Object> map = new HashMap<String, Object>();
        long userId = mo.getUserId();
        String orderCode = mo.getOrderCode();
        map.put("userId", userId);
        map.put("orderCode", orderCode);
        _log.info("用户查询订单的参数为：{}", String.valueOf(map));
        List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消发货失败", orderCode, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.ORDER_NOT_EXIST);
            cancelDeliveryRo.setMsg("订单不存在");
            return cancelDeliveryRo;
        }
        if (orderList.get(0).getOrderState() != OrderStateDic.ALREADY_PAY.getCode()) {
            _log.error("由于订单：{}处于非待发货状态，{}取消发货失败", orderCode, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            cancelDeliveryRo.setMsg("当前状态不允许取消");
            return cancelDeliveryRo;
        }
        OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(Long.parseLong(orderCode));
        List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
        _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        if (orderDetailList.size() == 0) {
            _log.error("由于订单：{}不存在，{}取消发货失败", orderCode, userId);
            cancelDeliveryRo.setResult(CancelDeliveryDic.ORDER_NOT_EXIST);
            cancelDeliveryRo.setMsg("订单不存在");
            return cancelDeliveryRo;
        }
        List<Map<String, Object>> orderSpecList = new ArrayList<Map<String, Object>>();
        for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
            Map<String, Object> specMap = new HashMap<String, Object>();
            specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
            specMap.put("specName", ordOrderDetailMo.getSpecName());
            specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
            orderSpecList.add(specMap);
        }
        _log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
        ModifyOnlineSpecInfoRo specMap = onlOnlineSpecSvc.modifyOnlineSpecInfo(orderSpecList);
        _log.info("查询并修改上线规格信息的返回值为：{}", specMap);
        int specResult = specMap.getResult().getCode();
        if (specResult < 1) {
            _log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
            cancelDeliveryRo.setResult(CancelDeliveryDic.MODIFY_SPEC_COUNT_ERROR);
            cancelDeliveryRo.setMsg("修改规格数量失败");
            return cancelDeliveryRo;
        }
        Date date = new Date();
        mo.setCancelTime(date);
        int updateResult = _mapper.cancelDeliveryUpdateOrderState(mo);
        if (updateResult != 1) {
            _log.error("{}取消发货：{}失败", userId, orderCode);
            throw new RuntimeException("修改订单状态失败");
        }
        _log.info("{}发货订单：{}成功", userId, orderCode);
        cancelDeliveryRo.setResult(CancelDeliveryDic.SUCCESS);
        cancelDeliveryRo.setMsg("取消发货成功");
        return cancelDeliveryRo;
    }

    /**
     * 确认发货并修改订单状态 Title: sendAndPrint Description:
     *
     * @param mo
     * @return
     * @date 2018年4月13日 下午6:18:44
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ShipmentConfirmationRo shipmentConfirmation(ShipmentConfirmationTo to) {
        ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();
        OrdOrderMo mo = dozerMapper.map(to, OrdOrderMo.class);
        Date date = new Date();
        mo.setSendTime(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, signinOrderTime);
        // 取消订单的时间
        Date executePlanTime = calendar.getTime();
        OrdTaskMo ordTaskMo = new OrdTaskMo();
        ordTaskMo.setExecutePlanTime(executePlanTime);
        ordTaskMo.setExecuteState((byte) 0);
        ordTaskMo.setTaskType((byte) 2);
        ordTaskMo.setOrderId(mo.getOrderCode());
        _log.info("确认发货添加签收任务的参数为：{}", ordTaskMo);
        // 添加签收任务
        int taskAddResult = ordTaskSvc.add(ordTaskMo);
        _log.info("确认发货添加签收任务的返回值为：{}", taskAddResult);
        if (taskAddResult != 1) {
            _log.error("确认发货添加签收任务时出错，订单编号为：{}", mo.getOrderCode());
            throw new RuntimeException("添加签收任务出错");
        }
        EOrderTo eoderTo = new EOrderTo();
        eoderTo.setShipperId(to.getShipperId());
        eoderTo.setOrderId(Long.parseLong(mo.getOrderCode()));
        eoderTo.setOrderTitle(mo.getOrderTitle());
        eoderTo.setReceiverName(mo.getReceiverName());
        eoderTo.setReceiverProvince(mo.getReceiverProvince());
        eoderTo.setReceiverCity(mo.getReceiverCity());
        eoderTo.setReceiverExpArea(mo.getReceiverExpArea());
        eoderTo.setReceiverAddress(mo.getReceiverAddress());
        eoderTo.setReceiverPostCode(mo.getReceiverPostCode());
        eoderTo.setReceiverTel(mo.getReceiverTel());
        eoderTo.setReceiverMobile(mo.getReceiverMobile());
        // 获取并手动设置默认发件人的信息
        KdiSenderMo kdiSenderMo = new KdiSenderMo();
        kdiSenderMo.setIsDefault(true);
        Map<String, Object> map = new HashMap<>();
        map.put("mo", kdiSenderMo);
        _log.info("获取默认发件人的参数为：{}", String.valueOf(map));
        List<KdiSenderMo> senderInfo = kdiSvc.getSenderInfo(map);
        _log.info("获取默认发件人返回值为：{}", senderInfo);
        if (senderInfo == null) {
            _log.info("默认发件人为null");
            return null;
        } else {
            eoderTo.setSenderName(senderInfo.get(0).getSenderName());
            eoderTo.setSenderMobile(senderInfo.get(0).getSenderMobile());
            eoderTo.setSenderTel(senderInfo.get(0).getSenderTel());
            eoderTo.setSenderProvince(senderInfo.get(0).getSenderProvince());
            eoderTo.setSenderCity(senderInfo.get(0).getSenderCity());
            eoderTo.setSenderAddress(senderInfo.get(0).getSenderAddress());
            eoderTo.setSenderExpArea(senderInfo.get(0).getSenderExpArea());
        }
        _log.info("调用快递电子面单的参数为：{}", eoderTo);
        EOrderRo eOrderRo = kdiSvc.eorder(eoderTo);
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
        mo.setOrderState((byte) OrderStateDic.ALREADY_PAY.getCode());
        mo.setLogisticCode(eOrderRo.getLogisticCode());
        mo.setLogisticId(eOrderRo.getLogisticId());
        _log.info("确认发货并修改订单状态的参数为：{}", mo);
        int result = _mapper.shipmentConfirmation(mo);
        _log.info("确认发货并修改订单状态的返回值为：{}", result);
        if (result != 1) {
            _log.error("确认发货出现异常，返回值为：{}", result);
            confirmationRo.setResult(ShipmentConfirmationDic.ERROR);
            confirmationRo.setMsg("确认发货失败");
            return confirmationRo;
        }
        _log.info("确认发货成功，返回值为：{}", result);
        _log.info("调用快递电子面单成功，返回值为：{}", result);
        confirmationRo.setResult(ShipmentConfirmationDic.SUCCESS);
        confirmationRo.setMsg("确认发货成功");
        confirmationRo.setLogisticId(eOrderRo.getLogisticId());
        confirmationRo.setLogisticCode(eOrderRo.getLogisticCode());
        confirmationRo.setPrintPage(eOrderRo.getPrintPage());
        confirmationRo.setFailReason(eOrderRo.getFailReason());
        // 获取并设置快递公司
        List<KdiCompanyMo> CompanyList = kdiSvc.kdiCompanyList();
        _log.info("获取到的所以快递公司：{}", CompanyList);
        OrdOrderMo ordOrderMo = new OrdOrderMo();
        if (CompanyList != null) {
            for (int i = 0; i < CompanyList.size(); i++) {
                if (CompanyList.get(i).getId().equals(to.getShipperId())) {
                    ordOrderMo.setShipperName(CompanyList.get(i).getCompanyName());
                    ordOrderMo.setOrderCode(to.getOrderCode());
                }
            }
            SetUpExpressCompanyRo setResult = setUpExpressCompany(ordOrderMo);
            _log.info("设置快递公司的返回值为：{}", setResult);
        }
        return confirmationRo;
    }

    /**
     * 订单签收 Title: orderSignIn Description:
     *
     * @param mo
     * @param to
     * @return
     * @date 2018年4月14日 下午2:20:19
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderSignInRo orderSignIn(OrderSignInTo to) {
        OrderSignInRo orderSignInRo = new OrderSignInRo();
        Map<String, Object> map = new HashMap<String, Object>();
        String orderCode = to.getOrderCode();
        map.put("orderCode", orderCode);
        _log.info("用户查询订单的参数为：{}", String.valueOf(map));
        List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
        _log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("由于订单：{}不存在，取消订单失败", orderCode);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        long userId = orderList.get(0).getUserId();
        if (orderList.get(0).getOrderState() != OrderStateDic.ALREADY_DELIVER_GOODS.getCode()) {
            _log.error("由于订单：{}处于非待签收状态，{}签收订单失败", orderCode, userId);
            orderSignInRo.setResult(OrderSignInDic.CURRENT_STATE_NOT_EXIST_CANCEL);
            orderSignInRo.setMsg("当前状态不允许签收");
            return orderSignInRo;
        }
        OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setOrderId(Long.parseLong(orderCode));
        _log.info("订单签收查询订单详情的参数为：{}", orderCode);
        List<OrdOrderDetailMo> detailList = ordOrderDetailSvc.list(detailMo);
        _log.info("订单签收查询订单详情的返回值为：{}", String.valueOf(detailList));
        if (detailList.size() == 0) {
            _log.error("订单签收查询订单详情时发现没有该订单的订单详情，订单编号为：{}", orderCode);
            orderSignInRo.setResult(OrderSignInDic.ORDER_NOT_EXIST);
            orderSignInRo.setMsg("订单不存在");
            return orderSignInRo;
        }
        Date date = new Date();
        _log.info("订单签收的时间为：{}", date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, settleBuyerCashbackTime);
        Date buyerCashbackDate = calendar.getTime();
        _log.info("订单签收的执行返款的时间为：{}", buyerCashbackDate);
        AddSettleTasksTo settleTasksTo = new AddSettleTasksTo();
        settleTasksTo.setOrderId(orderCode);
        settleTasksTo.setBuyerAccountId(userId);
        settleTasksTo.setSettleBuyerCashbackTime(buyerCashbackDate);
        settleTasksTo.setIp(to.getIp());
        settleTasksTo.setMac(to.getMac());
        List<AddSettleTasksDetailTo> addSettleTasksDetailList = new ArrayList<AddSettleTasksDetailTo>();
        for (OrdOrderDetailMo ordOrderDetailMo : detailList) {
            AddSettleTasksDetailTo settleTasksDetailTo = new AddSettleTasksDetailTo();
            settleTasksDetailTo.setOrderDetailId(ordOrderDetailMo.getId().toString());
            settleTasksDetailTo.setSettleBuyerCashbackAmount(ordOrderDetailMo.getCashbackTotal());
            settleTasksDetailTo.setSettleBuyerCashbackTitle("大卖网络-用户返款");
            settleTasksDetailTo.setSettleBuyerCashbackDetail(ordOrderDetailMo.getOnlineTitle());
            _log.info("订单签收添加结算的参数为：{}", settleTasksTo);
            addSettleTasksDetailList.add(settleTasksDetailTo);
        }
        settleTasksTo.setDetails(addSettleTasksDetailList);
        _log.info("订单签收添加结算的参数为：{}", settleTasksTo.toString());
        AddSettleTasksRo addSettleTasksRo = afcSettleTaskSvc.addSettleTasks(settleTasksTo);
        _log.info("订单签收添加结算的返回值为：{}", addSettleTasksRo);
        if (addSettleTasksRo.getResult().getCode() != 1) {
            _log.error("订单签收添加结算时出现错误，订单编号为：{}", orderCode);
            orderSignInRo.setResult(OrderSignInDic.ADD_SETTLEMENT_ERROR);
            orderSignInRo.setMsg("添加结算出错");
            return orderSignInRo;
        }
        OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setOrderCode(orderCode);
        orderMo.setUserId(userId);
        orderMo.setReceivedTime(date);
        orderMo.setReceivedOpId(userId);
        orderMo.setOrderState((byte) OrderStateDic.ALREADY_DELIVER_GOODS.getCode());
        _log.info("订单签收的参数为：{}", orderMo);
        int signInResult = _mapper.orderSignIn(orderMo);
        _log.info("订单签收的返回值为：{}", signInResult);
        if (signInResult < 1) {
            _log.error("{}签收订单出错，返回值为：{}", userId, signInResult);
            orderSignInRo.setResult(OrderSignInDic.ERROR);
            orderSignInRo.setMsg("签收失败");
            return orderSignInRo;
        }
        orderSignInRo.setResult(OrderSignInDic.SUCCESS);
        orderSignInRo.setMsg("签收成功");
        return orderSignInRo;
    }

    /**
     * 根据订单编号修改退货金额 Title: modifyReturnAmountByorderCode Description:
     *
     * @param mo
     * @return
     * @date 2018年5月7日 上午9:18:19
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyReturnAmountByorderCode(OrdOrderMo mo) {
        return _mapper.modifyReturnAmountByorderCode(mo);
    }

    /**
     * 根据订单编号修改订单状态 Title: modifyOrderStateByOderCode Description:
     *
     * @param orderCode
     * @param orderState
     * @return
     * @date 2018年5月8日 上午10:45:12
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyOrderStateByOderCode(long orderCode, byte orderState) {
        _log.info("修改订单状态的参数为：{}，{}", orderCode, orderState);
        return _mapper.modifyOrderStateByOderCode(orderCode, orderState);
    }

    /**
     * 根据订单编号查询退货金额 Title: selectReturnAmountByOrderCode Description:
     *
     * @param orderCode
     * @return
     * @date 2018年5月11日 上午11:14:42
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrdOrderMo selectReturnAmountByOrderCode(String orderCode) {
        _log.info("根据订单编号查询退货金额的参数为：{}", orderCode);
        OrdOrderMo orderMo = _mapper.selectReturnAmountByOrderCode(orderCode);
        _log.info("根据订单编号查询退货金额的返回值为：{}", orderMo);
        return orderMo;
    }

    /**
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateByOrderCode(OrdOrderMo mo) {
        int result = _mapper.updateByOrderCode(mo);
        return result;
    }

    /**
     * 结算完成 Title: finishSettlement Description:
     *
     * @param closeTime
     * @param orderCode
     * @return
     * @date 2018年5月17日 下午3:18:49
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int finishSettlement(Date closeTime, String orderCode) {
        _log.info("结算完成的参数为：{}，{}", closeTime, orderCode);
        return _mapper.finishSettlement(closeTime, orderCode);
    }

    /**
     * 订单支付 Title: orderPay Description:
     *
     * @param orderCode
     * @param payTime
     * @return
     * @date 2018年5月18日 上午11:20:37
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int orderPay(String orderCode, Date payTime) {
        _log.info("订单支付的参数为：{}，{}", orderCode, payTime);
        return _mapper.orderPay(orderCode, payTime);
    }

    /**
     * 根据订单编号查询订单状态 Title: selectOrderStateByOrderCode Description:
     *
     * @param orderCode
     * @return
     * @date 2018年5月21日 下午5:00:25
     */
    @Override
    public byte selectOrderStateByOrderCode(String orderCode) {
        return _mapper.selectOrderStateByOrderCode(orderCode);
    }

    /**
     * 查询用户待返现订单信息 2018年5月29日
     *
     * @throws ParseException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Override
    public List<Map<String, Object>> getCashBackOrder(Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        _log.info("查询用户待返现任务的参数为：{}", map.toString());
        long accountId = Long.parseLong(String.valueOf(map.get("userId")));
        byte executeState = (byte) SettleTaskExecuteStateDic.NONE.getCode();
        byte tradType = (byte) TradeTypeDic.SETTLE_CASHBACK.getCode();
        byte pageNum = Byte.parseByte(String.valueOf(map.get("pageNum")));
        byte pageSize = Byte.parseByte(String.valueOf(map.get("pageSize")));
        List<AfcSettleTaskMo> cashBackList = afcSettleTaskSvc.getCashBackTask(accountId, executeState, tradType, pageNum, pageSize);
        _log.info("获取到的用户待返现任务信息为：{}", String.valueOf(cashBackList));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (cashBackList.size() != 0) {
            for (int i = 0; i < cashBackList.size(); i++) {
                OrdOrderMo mo = new OrdOrderMo();
                _log.info("获取定单信息的订单号为：{}", cashBackList.get(i).getOrderId());
                mo.setOrderCode(cashBackList.get(i).getOrderId());
                List<OrdOrderMo> orderInfo = _mapper.selectSelective(mo);
                if (orderInfo.size() == 0) {
                    _log.info("根据订单号查询订单为空：{}");
                    continue;
                }
                _log.info("获取到的订单信息为：{}", String.valueOf(orderInfo));
                Map<String, Object> hm = new HashMap<String, Object>();
                String l = simpleDateFormat.format(cashBackList.get(i).getExecutePlanTime());
                Date date = simpleDateFormat.parse(l);
                long ts = date.getTime();
                _log.info("转换时间得到的时间戳为：{}", ts);
                hm.put("dateline", ts / 1000);
                hm.put("finishDate", ts / 1000 + 86400);
                hm.put("system", System.currentTimeMillis() / 1000);
                OrdOrderMo obj = orderInfo.get(0);
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    if (!key.equals("class")) {
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(obj);
                        hm.put(key, value);
                    }
                }
                _log.info("查询用户待返现订单信息hm里面的值为：{}", String.valueOf(hm));
                OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setOrderId(Long.parseLong(cashBackList.get(i).getOrderId()));
                _log.info("查询用户待返现订单获取订单详情的参数为：{}", detailMo.toString());
                List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
                _log.info("查询用户订单信息获取订单详情的返回值为：{}", String.valueOf(orderDetailList));
                List<OrderDetailRo> orderDetailRoList = new ArrayList<OrderDetailRo>();
                for (OrdOrderDetailMo orderDetailMo : orderDetailList) {
                    _log.info("查询用户订单信息开始获取商品主图");
                    List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailMo.getOnlineId(), (byte) 1);
                    _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                    OrderDetailRo orderDetailRo = new OrderDetailRo();
                    orderDetailRo.setId(orderDetailMo.getId());
                    orderDetailRo.setOrderId(orderDetailMo.getOrderId());
                    orderDetailRo.setOnlineId(orderDetailMo.getOnlineId());
                    orderDetailRo.setProduceId(orderDetailMo.getProduceId());
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
                    orderDetailRoList.add(orderDetailRo);
                }
                hm.put("items", orderDetailRoList);
                list.add(i, hm);
            }
        }
        _log.info("最新获取用户订单信息的返回值为：{}", String.valueOf(list));
        return list;
    }
}
