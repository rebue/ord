package rebue.ord.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.OrdOrderSvc;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class OrdOrderCtrl {
	/**
	 * @mbg.generated
	 */
	private final static Logger _log = LoggerFactory
			.getLogger(OrdOrderCtrl.class);

	/**
	 * @mbg.generated
	 */
	@Resource
	private OrdOrderSvc svc;

	/**
	 * 删除订单信息
	 * 
	 * @mbg.generated
	 */
	@DeleteMapping("/ord/order/{id}")
	Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
		_log.info("save OrdOrderMo:" + id);
		svc.del(id);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		_log.info("delete OrdOrderMo success!");
		return result;
	}

	/**
	 * 查询订单信息
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/order")
	PageInfo<OrdOrderMo> list(OrdOrderMo qo,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize) {
		_log.info("list OrdOrderMo:" + qo + ", pageNum = " + pageNum
				+ ", pageSize = " + pageSize);

		if (pageSize > 50) {
			String msg = "pageSize不能大于50";
			_log.error(msg);
			throw new IllegalArgumentException(msg);
		}

		PageInfo<OrdOrderMo> result = svc.list(qo, pageNum, pageSize);
		_log.info("result: " + result);
		return result;
	}

	/**
	 * 获取单个订单信息
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/order/{orderCode}")
	OrdOrderMo get(@PathVariable("orderCode") String orderCode) {
		_log.info("get OrdOrderMo by id: " + orderCode);
		OrdOrderMo result = svc.selectReturnAmountByOrderCode(orderCode);
		_log.info("get: " + result);
		return result;
	}

	/**
	 * 修改订单实际金额信息 2018年4月12日14:51:59
	 */
	@PutMapping("/ord/order")
	Map<String, Object> modify(OrdOrderMo vo) throws Exception {
		_log.info("修改订单实际金额的参数为：{}", vo.toString());
		return svc.updateOrderRealMoney(vo);
	}

	/**
	 * 查询订单信息 Title: orderInfo Description:
	 * 
	 * @param qo
	 * @return
	 * @throws ParseException
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date 2018年4月9日 下午3:06:37
	 */
	@GetMapping("/ord/order/info")
	List<Map<String, Object>> orderInfo(@RequestParam Map<String, Object> map)
			throws ParseException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			IntrospectionException {
		_log.info("查询订单信息的参数为：{}", map.toString());
		List<Map<String, Object>> list = svc.selectOrderInfo(map);
		_log.info("查询订单信息的返回值：{}", String.valueOf(list));
		return list;
	}

	/**
	 * 用户下订单 Title: placeOrder Description:
	 * 
	 * @param ro
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @date 2018年4月9日 上午10:55:18
	 */
	@SuppressWarnings("finally")
	@PostMapping("/ord/order")
	Map<String, Object> placeOrder(String orderJson) throws JsonParseException,
			JsonMappingException, IOException {
		_log.info("用户下订单的参数为：{}", orderJson);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = svc.placeOrder(orderJson);
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("生成订单出错")) {
				resultMap.put("result", -2);
				resultMap.put("msg", "生成订单出错");
			} else if (msg.equals("生成订单详情出错")) {
				resultMap.put("result", -3);
				resultMap.put("msg", "生成订单详情出错");
			} else if (msg.contains("未上线")) {
				_log.error(msg);
				resultMap.put("result", -4);
				resultMap.put("msg", msg);
			} else if (msg.contains("购物车中找不到")) {
				_log.error(msg);
				resultMap.put("result", -5);
				resultMap.put("msg", msg);
			} else if (msg.contains("扣减上线数量失败")) {
				_log.error(msg);
				resultMap.put("result", -6);
				resultMap.put("msg", msg);
			} else if (msg.equals("删除购物车失败")) {
				_log.error(msg);
				resultMap.put("result", -7);
				resultMap.put("msg", msg);
			} else if (msg.contains("库存不足")) {
				_log.error(msg);
				resultMap.put("result", -8);
				resultMap.put("msg", msg);
			} else {
				_log.error(msg);
				resultMap.put("result", -9);
				resultMap.put("msg", "下订单失败");
			}
		} finally {
			return resultMap;
		}
	}

	/**
	 * 用户取消订单 Title: cancellationOfOrder Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月9日 下午7:37:13
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/cancel")
	Map<String, Object> cancellationOfOrder(OrdOrderMo qo) {
		_log.info("用户取消订单的参数为：{}", qo.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = svc.cancellationOfOrder(qo);
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("修改库存失败")) {
				resultMap.put("result", -3);
				resultMap.put("msg", "修改库存失败");
			} else if (msg.equals("修改订单状态失败")) {
				resultMap.put("result", -4);
				resultMap.put("msg", "修改订单状态失败");
			}
		} finally {
			return resultMap;
		}
	}

	/**
	 * 取消发货 Title: cancellationOfOrder Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月9日 下午7:37:13
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/canceldelivery")
	Map<String, Object> cancelDeliveryUpdateOrderState(OrdOrderMo qo) {
		_log.info("用户取消订单的参数为：{}", qo.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = svc.cancelDeliveryUpdateOrderState(qo);
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("修改库存失败")) {
				resultMap.put("result", -3);
				resultMap.put("msg", "修改库存失败");
			} else if (msg.equals("修改订单状态失败")) {
				resultMap.put("result", -4);
				resultMap.put("msg", "修改订单状态失败");
			}
		} finally {
			return resultMap;
		}
	}

	/**
	 * 设置快递公司 Title: setUpExpressCompany Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月13日 上午11:24:17
	 */
	@PutMapping("/ord/order/setupexpresscompany")
	Map<String, Object> setUpExpressCompany(OrdOrderMo qo) {
		_log.info("设置快递公司的参数为：{}", qo.toString());
		return svc.setUpExpressCompany(qo);
	}

	/**
	 * 确认发货 Title: sendAndPrint Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月13日 下午6:23:46
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/shipmentconfirmation")
	Map<String, Object> shipmentConfirmation(OrdOrderMo qo) {
		_log.info("确认发货的参数为：{}", qo.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = svc.shipmentConfirmation(qo);
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("确认发货失败")) {
				resultMap.put("result", -1);
				resultMap.put("msg", msg);
				_log.error(msg);
			} else if (msg.equals("调用快递电子面单参数错误")) {
				resultMap.put("result", -2);
				resultMap.put("msg", msg);
				_log.error(msg);
			} else if (msg.equals("该订单已发货")) {
				resultMap.put("result", -3);
				resultMap.put("msg", msg);
				_log.error(msg);
			} else if (msg.equals("调用快递电子面单失败")) {
				resultMap.put("result", -4);
				resultMap.put("msg", msg);
				_log.error(msg);
			} else {
				resultMap.put("result", -5);
				resultMap.put("msg", "确认发货失败");
				_log.error(msg);
			}
		} finally {
			return resultMap;
		}
	}

	/**
	 * 订单签收 Title: orderSignIn Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月14日 下午2:30:53
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/ordersignin")
	Map<String, Object> orderSignIn(OrdOrderMo qo) {
		_log.info("订单签收的参数为：{}", qo.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = svc.orderSignIn(qo);
		} catch (RuntimeException e) {
			resultMap.put("result", -3);
			resultMap.put("msg", "签收失败");
		} finally {
			return resultMap;
		}
	}
	
	
}
