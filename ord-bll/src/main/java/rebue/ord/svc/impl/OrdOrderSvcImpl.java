package rebue.ord.svc.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.ord.mapper.OrdOrderMapper;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.OrdOrderSvc;

import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.kdi.ro.EOrderRo;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.kdi.to.EOrderTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.ro.DeleteCartAndModifyInventoryRo;
import rebue.onl.svr.feign.OnlCartSvc;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.onl.svr.feign.OnlOnlineSpecSvc;
import rebue.onl.svr.feign.OnlOnlineSvc;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import java.math.BigDecimal;
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
public class OrdOrderSvcImpl
		extends
			MybatisBaseSvcImpl<OrdOrderMo, java.lang.Long, OrdOrderMapper>
		implements
			OrdOrderSvc {

	/**
	 */
	private final static Logger _log = LoggerFactory
			.getLogger(OrdOrderSvcImpl.class);
	/**
	 */
	@Resource
	private OrdAddrSvc ordAddrSvc;
	/**
	 */
	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;
	/**
	 */
	@Resource
	private OnlOnlineSpecSvc onlOnlineSpecSvc;
	
	/**
	 */
	@Resource
	private OnlOnlineSvc onlOnlineSvc;
	/**
	 */
	@Resource
	private OnlCartSvc onlCartSvc;
	/**
	 */
	@Resource
	private KdiSvc kdiSvc;
	/**
	 */
	@Resource
	private OnlOnlinePicSvc onlOnlinePicSvc;

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
	public Map<String, Object> placeOrder(String orderJson)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, OrdOrderRo.class);
		List<OrdOrderRo> orderList = mapper.readValue(orderJson, javaType);
		_log.info("用户下单的参数为：{}", orderList.toString());
		OrdAddrMo addrMo = new OrdAddrMo();
		addrMo.setId(orderList.get(0).getAddress());
		addrMo.setUserId(orderList.get(0).getUserId());
		_log.info("获取用户收货地址信息的参数为：{}", addrMo.toString());
		List<OrdAddrMo> addrList = ordAddrSvc.list(addrMo);
		_log.info("根据收货地址编号和用户编号获取用户收货地址信息的返回值为：{}", addrList.toString());
		if (addrList.size() == 0) {
			resultMap.put("result", -1);
			resultMap.put("msg", "收货地址不能为空");
			return resultMap;
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
		if (orderMessages != null && !orderMessages.equals("")
				&& !orderMessages.equals("null")) {
			orderMo.setOrderMessages(orderMessages);
		}
		if (addrList.get(0).getReceiverPostCode() != null
				&& !addrList.get(0).getReceiverPostCode().equals("")) {
			orderMo.setReceiverPostCode(addrList.get(0).getReceiverPostCode());
		}
		if (addrList.get(0).getReceiverTel() != null
				&& !addrList.get(0).getReceiverTel().equals("")) {
			orderMo.setReceiverTel(addrList.get(0).getReceiverTel());
		}
		_log.info("添加订单信息的参数为：{}", orderMo);
		int insertOrderResult = add(orderMo);
		_log.info("添加订单信息的返回值为：{}", insertOrderResult);
		if (insertOrderResult < 1) {
			_log.error("{}添加订单信息失败", userId);
			throw new RuntimeException("生成订单出错");
		}
		List<DeleteCartAndModifyInventoryRo> cartAndSpecList = new ArrayList<DeleteCartAndModifyInventoryRo>();
		for (int i = 0; i < orderList.size(); i++) {
			long onlineId = orderList.get(i).getOnlineId();
			String OnlineSpec = orderList.get(i).getOnlineSpec();
			int buyCount = orderList.get(i).getNumber();
			DeleteCartAndModifyInventoryRo deleteCartAndModifyInventoryRo = new DeleteCartAndModifyInventoryRo();
			deleteCartAndModifyInventoryRo.setOnlineId(onlineId);
			deleteCartAndModifyInventoryRo.setBuyCount(buyCount);
			deleteCartAndModifyInventoryRo.setOnlineSpec(OnlineSpec);
			deleteCartAndModifyInventoryRo.setCartId(orderList.get(i)
					.getCartId());
			cartAndSpecList.add(deleteCartAndModifyInventoryRo);
			OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
			detailMo.setId(_idWorker.getId());
			detailMo.setOrderId(orderId);
			detailMo.setOnlineId(onlineId);
			detailMo.setProduceId(orderList.get(i).getProduceId());
			detailMo.setOnlineTitle(orderList.get(i).getOnlineTitle());
			detailMo.setSpecName(OnlineSpec);
			detailMo.setBuyCount(buyCount);
			detailMo.setBuyPrice(orderList.get(i).getSalePrice());
			detailMo.setCashbackAmount(orderList.get(i).getCashbackAmount());
			detailMo.setReturnState((byte) 0);
			detailMo.setCashbackTotal(new BigDecimal(String.valueOf(buyCount)).multiply(orderList.get(i).getCashbackAmount()));
			_log.info("添加订单详情的参数为：{}", detailMo.toString());
			int intserOrderDetailresult = ordOrderDetailSvc.add(detailMo);
			_log.info("添加订单详情的返回值为：{}", intserOrderDetailresult);
			if (intserOrderDetailresult < 1) {
				_log.error("{}添加订单详情失败", userId);
				throw new RuntimeException("生成订单详情出错");
			}
		}
		_log.info("删除购物车和修改上线数量的参数为：{}", String.valueOf(cartAndSpecList));
		Map<String, Object> deleteAndUpdateMap = new HashMap<String, Object>();
		try {
			deleteAndUpdateMap = onlOnlineSpecSvc.deleteCartAndUpdateOnlineCount(mapper.writeValueAsString(cartAndSpecList));
		} catch (Exception e) {
			_log.error("删除购物车和修改上线数量失败");
			e.printStackTrace();
		}
		_log.info("删除购物车和修改上线数量的返回值为：{}", String.valueOf(deleteAndUpdateMap));
		int deleteAndUpdateResult = Integer.parseInt(String.valueOf(deleteAndUpdateMap.get("result")));
		if (deleteAndUpdateResult < 1) {
			_log.error("{}删除购物车和修改上线数量失败", userId);
			throw new RuntimeException(String.valueOf(deleteAndUpdateMap.get("msg")));
		}
		resultMap.put("orderId", orderId);
		resultMap.put("result", 1);
		resultMap.put("msg", "下单成功");
		return resultMap;
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
			throws ParseException, IntrospectionException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		_log.info("查询用户订单信息的参数为：{}", map.toString());
		List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
		_log.info("获取到的用户订单信息为：{}", String.valueOf(orderList));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderList.size() != 0) {
			for (int i = 0; i < orderList.size(); i++) {
				Map<String, Object> hm = new HashMap<String, Object>();
				String l = simpleDateFormat.format(orderList.get(i).getOrderTime());
				Date date = simpleDateFormat.parse(l);
				long ts = date.getTime();
				_log.info("转换时间得到的时间戳为：{}", ts);
				hm.put("dateline", ts / 1000);
				hm.put("finishDate", ts / 1000 + 86400);
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
				/// 查询订单详情信息
				List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
				_log.info("查询用户订单信息获取订单详情的返回值为：{}", String.valueOf(orderDetailList));
				List<OrderDetailRo> orderDetailRoList = new ArrayList<OrderDetailRo>();
				for (OrdOrderDetailMo orderDetailMo : orderDetailList) {
					List<OnlOnlinePicMo> onlinePicList = new ArrayList<OnlOnlinePicMo>();
					try {
						// 获取上线主图
						onlinePicList = onlOnlinePicSvc.list(orderDetailMo.getOnlineId(), (byte) 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					_log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
					OrderDetailRo orderDetailRo = new OrderDetailRo();
					orderDetailRo.setId(orderDetailMo.getId());
					orderDetailRo.setOrderId(orderDetailMo.getOrderId());
					orderDetailRo.setOnlineId(orderDetailMo.getOnlineId());
					orderDetailRo.setProduceId(orderDetailMo.getProduceId());
					orderDetailRo
							.setOnlineTitle(orderDetailMo.getOnlineTitle());
					orderDetailRo.setSpecName(orderDetailMo.getSpecName());
					orderDetailRo.setBuyCount(orderDetailMo.getBuyCount());
					orderDetailRo.setBuyPrice(orderDetailMo.getBuyPrice());
					orderDetailRo.setCashbackAmount(orderDetailMo
							.getCashbackAmount());
					orderDetailRo.setBuyUnit(orderDetailMo.getBuyUnit());
					orderDetailRo
							.setReturnState(orderDetailMo.getReturnState());
					orderDetailRo.setGoodsQsmm(onlinePicList.get(0)
							.getPicPath());
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
	public Map<String, Object> cancellationOfOrder(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户编号
		long userId = mo.getUserId();
		// 订单编号
		String orderCode = mo.getOrderCode();
		// ==========================================查询用户订单信息开始=================================
		map.put("userId", userId);
		map.put("orderCode", orderCode);
		List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
		_log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -1);
			resultMap.put("msg", "订单不存在");
			return resultMap;
		}
		if (orderList.get(0).getOrderState() != 1) {
			_log.error("由于订单：{}处于非待支付状态，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -2);
			resultMap.put("msg", "当前状态不允许取消");
			return resultMap;
		}
		// ==========================================查询用户订单信息结束=================================
		
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		// ==========================================查询用户订单详情开始=================================
		detailMo.setOrderId(Long.parseLong(orderCode));
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
		_log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList.size() == 0) {
			_log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -1);
			resultMap.put("msg", "订单不存在");
			return resultMap;
		}
		// 购买规格信息
		List<Map<String, Object>> orderSpecList = new ArrayList<Map<String, Object>>();
		for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
			Map<String, Object> specMap = new HashMap<String, Object>();
			specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
			specMap.put("specName", ordOrderDetailMo.getSpecName());
			specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
			orderSpecList.add(specMap);
		}
		// ==========================================查询用户订单详情结束=================================
		
		// ==========================================查询并修改上线规格信息开始=================================
		_log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
		// 查询并修改上线规格信息
		Map<String, Object> specMap = onlOnlineSpecSvc.updateSpenInfo(orderSpecList);
		_log.info("查询并修改上线规格信息的返回值为：{}", String.valueOf(specMap));
		int specResult = Integer.parseInt(String.valueOf(specMap.get("result")));
		if (specResult < 1) {
			_log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
			throw new RuntimeException("修改规格数量失败");
		}
		// ==========================================查询并修改上线规格信息结束=================================
		Date date = new Date();
		
		// ==========================================用户取消订单并修改状态开始=================================
		mo.setCancelTime(date);
		_log.info("取消订单并修改状态的参数为：", mo.toString());
		int updateResult = _mapper.cancellationOrderUpdateOrderState(mo);
		_log.info("取消订单并修改状态的返回值为：{}", updateResult);
		if (updateResult < 1) {
			_log.error("{}取消订单：{}失败", userId, orderCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "修改订单状态失败");
			throw new RuntimeException("修改订单状态失败");
		}
		_log.info("{}取消订单：{}成功", userId, orderCode);
		// ==========================================用户取消订单并修改状态结束=================================
		resultMap.put("result", 1);
		resultMap.put("msg", "取消订单成功");
		return resultMap;
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
	public Map<String, Object> updateOrderRealMoney(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		_log.info("修改订单实际金额的参数为：{}", mo.toString());
		int result = _mapper.updateOrderRealMoney(mo);
		_log.info("修改订单实际金额的返回值为：{}", result);
		if (result < 1) {
			resultMap.put("result", result);
			resultMap.put("msg", "修改订单实际金额失败");
			return resultMap;
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "修改订单实际金额成功");
		return resultMap;
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
	public Map<String, Object> setUpExpressCompany(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<>();
		_log.info("设置快递公司的参数为：{}", mo.toString());
		int result = _mapper.setUpExpressCompany(mo);
		_log.info("设置快递公司的返回值为：{}", result);
		if (result < 1) {
			_log.error("设置快递公司出错，返回值为：{}", result);
			resultMap.put("result", result);
			resultMap.put("msg", "设置失败");
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "设置成功");
		return resultMap;
	}

	/**
	 * 取消发货并修改订单状态 2018年4月9日18:57:36
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> cancelDeliveryUpdateOrderState(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		long userId = mo.getUserId();
		String orderCode = mo.getOrderCode();
		map.put("userId", userId);
		map.put("orderCode", orderCode);
		_log.info("用户查询订单的参数为：{}", String.valueOf(map));
		List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
		_log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -1);
			resultMap.put("msg", "订单不存在");
			return resultMap;
		}
		if (orderList.get(0).getOrderState() != 2) {
			_log.error("由于订单：{}处于非待发货状态，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -2);
			resultMap.put("msg", "当前状态不允许取消");
			return resultMap;
		}
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setOrderId(Long.parseLong(orderCode));
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(detailMo);
		_log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList.size() == 0) {
			_log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -1);
			resultMap.put("msg", "订单不存在");
			return resultMap;
		}
		// 购买规格信息
		List<Map<String, Object>> orderSpecList = new ArrayList<Map<String, Object>>();
		for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
			Map<String, Object> specMap = new HashMap<String, Object>();
			specMap.put("onlineId", ordOrderDetailMo.getOnlineId());
			specMap.put("specName", ordOrderDetailMo.getSpecName());
			specMap.put("buyCount", ordOrderDetailMo.getBuyCount());
			orderSpecList.add(specMap);
		}
		// ==========================================查询用户订单详情结束=================================
		
		// ==========================================查询并修改上线规格信息开始=================================
		_log.info("查询并修改上线规格信息的参数为：{}", String.valueOf(orderSpecList));
		// 查询并修改上线规格信息
		Map<String, Object> specMap = onlOnlineSpecSvc.updateSpenInfo(orderSpecList);
		_log.info("查询并修改上线规格信息的返回值为：{}", String.valueOf(specMap));
		int specResult = Integer.parseInt(String.valueOf(specMap.get("result")));
		if (specResult < 1) {
			_log.info("取消订单时出现修改上线规格信息出错，返回值为：{}", specResult);
			throw new RuntimeException("修改规格数量失败");
		}
		Date date = new Date();
		mo.setCancelTime(date);
		int updateResult = _mapper.cancelDeliveryUpdateOrderState(mo);
		if (updateResult < 1) {
			_log.error("{}取消订单：{}失败", userId, orderCode);
			throw new RuntimeException("修改订单状态失败");
		}
		_log.info("{}取消订单：{}成功", userId, orderCode);
		resultMap.put("result", 1);
		resultMap.put("msg", "取消发货成功");
		return resultMap;
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
	public Map<String, Object> shipmentConfirmation(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Date date = new Date();
		mo.setSendTime(date);
		_log.info("确认发货并修改订单状态的参数为：{}", mo.toString());
		int result = _mapper.shipmentConfirmation(mo);
		_log.info("确认发货并修改订单状态的返回值为：{}", result);
		if (result < 1) {
			_log.error("确认发货出现异常，返回值为：{}", result);
			throw new RuntimeException("确认发货失败");
		}
		_log.info("确认发货成功，返回值为：{}", result);
		String shipperCode = mo.getShipperCode();
		EOrderTo eOrderTo = new EOrderTo();
		eOrderTo.setOrderId(Long.parseLong(mo.getOrderCode()));
		eOrderTo.setShipperCode(shipperCode);
		eOrderTo.setOrderTitle(mo.getOrderTitle());
		String senderName = "余蓓蓓";
		if (shipperCode.equals("HTKY")) {
			senderName = "微薄利";
		}
		eOrderTo.setSenderName(senderName);
		eOrderTo.setSenderMobile("13657882081");
		eOrderTo.setSenderProvince("广西壮族自治区");
		eOrderTo.setSenderCity("南宁市");
		eOrderTo.setSenderExpArea("西乡塘区");
		eOrderTo.setSenderAddress("安吉华尔街工谷微薄利商超1楼wboly.com");
		eOrderTo.setReceiverName(mo.getReceiverName());
		eOrderTo.setReceiverMobile(mo.getReceiverMobile());
		eOrderTo.setReceiverProvince(mo.getReceiverProvince());
		eOrderTo.setReceiverCity(mo.getReceiverCity());
		eOrderTo.setReceiverExpArea(mo.getReceiverExpArea());
		eOrderTo.setReceiverAddress(mo.getReceiverAddress());
		if (shipperCode.equals("YZPY")) {
			eOrderTo.setSenderPostCode("530001");
			eOrderTo.setReceiverPostCode(mo.getReceiverPostCode());
		}
		_log.info("调用快递电子面单的参数为：{}", eOrderTo.toString());
		EOrderRo eOrderRo = new EOrderRo();
		try {
			eOrderRo = kdiSvc.eorder(eOrderTo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_log.info("调用快递电子面单的返回值为：{}", eOrderRo.toString());
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
		_log.info("调用快递电子面单成功，返回值为：{}", result);
		resultMap.put("result", 1);
		resultMap.put("msg", "确认发货成功");
		resultMap.put("logisticId", eOrderRo.getLogisticId());
		resultMap.put("logisticCode", eOrderRo.getLogisticCode());
		resultMap.put("printPage", eOrderRo.getPrintPage());
		resultMap.put("failReason", eOrderRo.getFailReason());
		return resultMap;
	}

	/**
	 * 订单签收 Title: orderSignIn Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月14日 下午2:20:19
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> orderSignIn(OrdOrderMo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		long userId = mo.getUserId();
		String orderCode = mo.getOrderCode();
		map.put("userId", userId);
		map.put("orderCode", orderCode);
		_log.info("用户查询订单的参数为：{}", String.valueOf(map));
		List<OrdOrderMo> orderList = _mapper.selectOrderInfo(map);
		_log.info("用户查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("由于订单：{}不存在，{}取消订单失败", orderCode, userId);
			resultMap.put("result", -1);
			resultMap.put("msg", "订单不存在");
			return resultMap;
		}
		if (orderList.get(0).getOrderState() != 3) {
			_log.error("由于订单：{}处于非待签收状态，{}签收订单失败", orderCode, userId);
			resultMap.put("result", -2);
			resultMap.put("msg", "当前状态不允许签收");
			return resultMap;
		}
		Date date = new Date();
		mo.setReceivedTime(date);
		_log.info("订单签收的参数为：{}", mo.toString());
		int signInResult = _mapper.orderSignIn(mo);
		_log.info("订单签收的返回值为：{}", signInResult);
		if (signInResult < 1) {
			_log.error("{}签收订单出错，返回值为：{}", userId, signInResult);
			throw new RuntimeException("签收失败");
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "签收成功");
		return resultMap;
	}

	/**
	 * 根据订单编号修改退货金额
	 * Title: modifyReturnAmountByorderCode
	 * Description: 
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
	 * 根据订单编号修改订单状态
	 * Title: modifyOrderStateByOderCode
	 * Description: 
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
	 * 根据订单编号查询退货金额
	 * Title: selectReturnAmountByOrderCode
	 * Description: 
	 * @param orderCode
	 * @return
	 * @date 2018年5月11日 上午11:14:42
	 */
	@Override
	public OrdOrderMo selectReturnAmountByOrderCode(String orderCode) {
		_log.info("根据订单编号查询退货金额的参数为：{}", orderCode);
		OrdOrderMo orderMo = _mapper.selectReturnAmountByOrderCode(orderCode);
		_log.info("根据订单编号查询退货金额的返回值为：{}", orderMo.toString());
		return orderMo;
	}

	@Override
	/**
	 * 根据订单编号修改订单
	 * Title: updateByOrderCode
	 * Description: 
	 * @param OrdOrderMo
	 * @return
	 * @date 2018年5月15日
	 */
	public int updateByOrderCode(OrdOrderMo mo) {
		int result = _mapper.updateByOrderCode(mo);
		return result;
	}
	
}
