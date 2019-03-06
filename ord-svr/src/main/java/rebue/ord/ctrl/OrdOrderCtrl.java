package rebue.ord.ctrl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import rebue.ord.dic.CancellationOfOrderDic;
import rebue.ord.dic.OrderSignInDic;
import rebue.ord.dic.ShipmentConfirmationDic;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.BulkShipmentRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrdOrderRo;
import rebue.ord.ro.OrdSettleRo;
import rebue.ord.ro.OrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.to.BulkShipmentTo;
import rebue.ord.to.CancelDeliveryTo;
import rebue.ord.to.DeliverAndGetTraceTo;
import rebue.ord.to.ListOrderTo;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.OrderTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.ord.to.UpdateOrgTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.suc.svr.feign.SucUserSvc;
import rebue.wheel.AgentUtils;
import rebue.wheel.turing.JwtUtils;

/**
 * 订单信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdOrderCtrl {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdOrderCtrl.class);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Resource
	private OrdOrderSvc svc;

	/**
	 * 获取单个订单信息
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@GetMapping("/ord/order/getbyid")
	OrdOrderMo getById(@RequestParam("id") final java.lang.Long id) {
		_log.info("get OrdOrderMo by id: " + id);
		return svc.getById(id);
	}

	/**
	 * 添加订单信息
	 */
	Ro add(@RequestBody final OrdOrderMo mo) throws Exception {
		throw new RuntimeException("不提供此方法");
	}

	/**
	 * 修改订单信息
	 */
	Ro modify(@RequestBody final OrdOrderMo mo) throws Exception {
		throw new RuntimeException("不提供此方法");
	}

	/**
	 * 删除订单信息
	 */
	Ro del(@RequestParam("id") final java.lang.Long id) {
		throw new RuntimeException("不提供此方法");
	}

	/**
	 * 是否测试模式（测试模式下不用从Cookie中获取用户ID）
	 */
	@Value("${debug:false}")
	private Boolean isDebug;

	@Resource
	private SucUserSvc sucSvc;

	/**
	 * 前面经过的代理
	 */
	@Value("${afc.passProxy:noproxy}")
	private String passProxy;

	/**
	 * 查询订单信息
	 *
	 * @mbg.overrideByMethodName
	 */
	@GetMapping("/ord/order")
	PageInfo<OrdOrderRo> list(final ListOrderTo to, @RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, final HttpServletRequest req)
			throws ParseException {
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 5;
		}
		_log.info("list OrdOrderMo:" + to + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
		if (pageSize > 50) {
			final String msg = "pageSize不能大于50";
			_log.error(msg);
			throw new IllegalArgumentException(msg);
		}
		if (!isDebug) {
			final Long orgId = (Long) JwtUtils.getJwtAdditionItemInCookie(req, "orgId");
			if (orgId == null) {
				return new PageInfo<>();
			}
			to.setOrgId(orgId);
		} else {
			to.setOrgId(520874560590053376L);
		}
		_log.info("获取当前用户的组织ID: {}", to.getOrgId());
		// 查询订单
		final PageInfo<OrdOrderRo> result = svc.listOrder(to, pageNum, pageSize);
		_log.info("result: " + result);
		return result;
	}

	/**
	 * 供应商查询订单信息
	 *
	 * @mbg.overrideByMethodName
	 */
	@GetMapping("/ord/order/Supplier")
	PageInfo<OrdOrderRo> listSupplier(final ListOrderTo to,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, final HttpServletRequest req)
			throws ParseException {
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 5;
		}
		_log.info("list OrdOrderMo:" + to + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
		if (pageSize > 50) {
			final String msg = "pageSize不能大于50";
			_log.error(msg);
			throw new IllegalArgumentException(msg);
		}
		if (!isDebug) {
			final Long orgId = (Long) JwtUtils.getJwtAdditionItemInCookie(req, "orgId");
			if (orgId == null) {
				return new PageInfo<>();
			}
			to.setOrgId(orgId);
		}
		_log.info("当前用户的组织ID: {}", to.getOrgId());
		// 查询订单
		final PageInfo<OrdOrderRo> result = svc.SupplierlistOrder(to, pageNum, pageSize);
		_log.info("result: " + result.getList());
		return result;
	}

	/**
	 * 修改订单实际金额信息 2018年4月12日14:51:59
	 */
	@PutMapping("/ord/order")
	ModifyOrderRealMoneyRo modifyOrderRealMoney(@RequestBody final OrdOrderMo vo) throws Exception {
		_log.info("修改订单实际金额的参数为：{}", vo);
		return svc.modifyOrderRealMoney(vo);
	}

	/**
	 * 查询订单信息
	 */
	@GetMapping("/ord/order/info")
	List<Map<String, Object>> orderInfo(@RequestParam final Map<String, Object> map) throws ParseException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		_log.info("查询订单信息的参数为：{}", map.toString());
		final List<Map<String, Object>> list = svc.selectOrderInfo(map);
		_log.info("查询订单信息的返回值：{}", String.valueOf(list));
		return list;
	}

	/**
	 * 下订单
	 */
	@PostMapping("/ord/order")
	OrderRo order(@RequestBody final OrderTo to, final HttpServletRequest req)
			throws NumberFormatException, ParseException {
		OrderRo ro = new OrderRo();
		_log.info("用户下订单的参数为：{}", to);
		// FIXME 为兼容旧的微信公众号网站，暂时由传过来的参数中指定当前用户
		// _log.info("设置下单的用户为当前用户");
		// if (!isDebug || to.getUserId() == null) {
		// to.setUserId(JwtUtils.getJwtUserIdInCookie(req));
		// }
		// 设置是否是测试用户
		to.setIsTester(sucSvc.isTester(to.getUserId()));
		try {
			ro = svc.order(to);
		} catch (final RuntimeException e) {
			_log.error("下订单出现运行时异常", e);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(e.getMessage());
		}
		_log.info("返回值为：{}", ro);
		return ro;
	}

	/**
	 * 用户取消订单
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/cancel")
	CancellationOfOrderRo cancellationOfOrder(@RequestBody final OrdOrderMo qo) {
		_log.info("用户取消订单的参数为：{}", qo);
		CancellationOfOrderRo cancellationOfOrderRo = new CancellationOfOrderRo();
		try {
			cancellationOfOrderRo = svc.cancellationOfOrder(qo);
		} catch (final RuntimeException e) {
			final String msg = e.getMessage();
			if (msg.equals("修改订单状态失败")) {
				cancellationOfOrderRo.setResult(CancellationOfOrderDic.MODIFY_ORDER_STATE_ERROR);
				cancellationOfOrderRo.setMsg(msg);
			} else {
				cancellationOfOrderRo.setResult(CancellationOfOrderDic.ERROR);
				cancellationOfOrderRo.setMsg("取消订单失败");
			}
		} finally {
			return cancellationOfOrderRo;
		}
	}

	/**
	 * 取消发货 Title: cancellationOfOrder Description:
	 *
	 * @param qo
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @date 2018年4月9日 下午7:37:13
	 */
	@PutMapping("/ord/order/canceldelivery")
	Ro cancelDelivery(@RequestBody final CancelDeliveryTo to, final HttpServletRequest req)
			throws NumberFormatException, ParseException {
		_log.info("用户取消发货的参数为：{}", to);
		Long loginId = 520469568947224576L;
		if (!isDebug) {
			loginId = JwtUtils.getJwtUserIdInCookie(req);
		}
		final Ro ro = new Ro();
		if (loginId == null) {
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("您未登录，请登录后再试。。。");
			return ro;
		}
		to.setCancelingOrderOpId(loginId);
		to.setOpIp(AgentUtils.getIpAddr(req, passProxy));
		try {
			return svc.cancelDelivery(to);
		} catch (final RuntimeException e) {
			_log.error("用户取消发货出现异常，{}", e);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("取消失败");
			return ro;
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
	SetUpExpressCompanyRo setUpExpressCompany(final OrdOrderMo qo) {
		_log.info("设置快递公司的参数为：{}", qo);
		return svc.setUpExpressCompany(qo);
	}

	/**
	 * 确认发货 Title: sendAndPrint Description:
	 *
	 * @param qo
	 * @return
	 * @date 2018年4月13日 下午6:23:46
	 */
	@PutMapping("/ord/order/deliver")
	ShipmentConfirmationRo shipmentConfirmation(@RequestBody final ShipmentConfirmationTo qo) {
		_log.info("发货的参数为：{}", qo);
		return svc.deliver(qo);
	}

	/**
	 * 订阅轨迹发货
	 */
	@PutMapping("/ord/order/getTraceAndDeliver")
	Ro deliverAndGetTrace(@RequestBody final DeliverAndGetTraceTo to) {
		_log.info("订阅轨迹的参数为：{}", to);
		return svc.deliverAndGetTrace(to);
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
	OrderSignInRo orderSignIn(final OrderSignInTo qo) {
		_log.info("订单签收的参数为：{}", qo.toString());
		OrderSignInRo orderSignInRo = new OrderSignInRo();
		try {
			orderSignInRo = svc.orderSignIn(qo);
		} catch (final RuntimeException e) {
			orderSignInRo.setResult(OrderSignInDic.ERROR);
			orderSignInRo.setMsg("签收失败");
		} finally {
			return orderSignInRo;
		}
	}

	/**
	 * 根据定单编号获取单个订单信息
	 */
	@GetMapping("/ord/getByOrderCode/{orderCode}")
	OrdOrderMo getByOrderCode(@PathVariable("orderCode") final java.lang.Long orderCode) {
		_log.info("根据定单编号查找定单: " + orderCode);
		final String orderCodeStr = String.valueOf(orderCode);
		final OrdOrderMo mo = new OrdOrderMo();
		mo.setOrderCode(orderCodeStr);
		final OrdOrderMo result = svc.getOne(mo);
		_log.info("get: " + result);
		return result;
	}

	/**
	 * 修改收件人信息
	 */
	@PutMapping("/ord/order/modifyreceiverinfo")
	Ro modifyOrderReceiverInfo(final OrdOrderMo qo) {
		_log.info("修改收件人信息的参数为：{}", qo);
		return svc.modifyOrderReceiverInfo(qo);
	}

	/**
	 * 查询订单信息(list)
	 */
	@GetMapping("/ord/order/listselective")
	List<OrdOrderMo> listSelective(final OrdOrderMo mo) {
		_log.info("listSelective OrdOrderMo:", mo);
		return svc.list(mo);
	}

	/**
	 * 根据订单id修改支付订单id
	 */
	@PutMapping("/ord/order/modifypayorderid")
	Ro modifyPayOrderId(@RequestParam("id") final java.lang.Long id) {
		_log.info("modifyPayOrderId OrdOrderMo by id: {}", id);
		return svc.modifyPayOrderId(id);
	}

	/**
	 * 根据供应商Id获取未发货的订单详情的总成本价
	 */
	@GetMapping("/ord/order/getSettleTotal")
	OrdSettleRo getSettleTotal(@RequestParam("supplierId") final java.lang.Long supplierId) {
		_log.info("根据供应商Id获取订单详情未结算和已结算的成本总价：{}", supplierId);
		return svc.getSettleTotal(supplierId);
	}

	/**
	 * 修deliverOrgId和supplierId
	 */
	@PutMapping("/ord/order/updateOrg")
	Ro updateOrg(@RequestBody final UpdateOrgTo to) throws Exception {
		_log.info("updateOrg UpdateOrgTo: {}", to);
		final Ro ro = svc.modifyOrg(to);
		return ro;
	}

	/**
	 * 根据用户id来获取已支付，已发货，已签收的订单详情待全返金额
	 */
	@GetMapping("/ord/order/commissionTotal")
	BigDecimal getCommissionTotal(@RequestParam("userId") final java.lang.Long userId) {
		_log.info("根据用户id获待全返金额getCommissionTotal userId: {}", userId);

		return svc.getCommissionTotal(userId);
	}

	/**
	 * 根据组织id查询未发货订单的总数
	 * 
	 * @param deliverOrgId
	 * @return
	 */
	@GetMapping("/ord/order/unshipments")
	BigDecimal getUnshipmentsByDeliverOrgId(@RequestParam("deliverOrgId") final java.lang.Long deliverOrgId) {
		_log.info("根据组织id查询未发货订单总数的参数 deliverOrgId: {}", deliverOrgId);
		return svc.getUnshipmentsByDeliverOrgId(deliverOrgId);
	}

	/**
	 * 根据用户和时间查询已经支付的订单
	 * 
	 * @param mo
	 * @return
	 */
	@GetMapping("/ord/order/havepaidorderbyuserandtime")
	List<OrdOrderMo> havePaidOrderByUserAndTimeList(@RequestParam("userId") Long userId,
			@RequestParam("receivedTime") Date receivedTime) {
		_log.info("根据用户和时间查询已经支付的订单的请求参数为：userId-{}, receivedTime-{}", userId, receivedTime);
		OrdOrderMo mo = new OrdOrderMo();
		mo.setUserId(userId);
		mo.setReceivedTime(receivedTime);
		return svc.havePaidOrderByUserAndTimeList(mo);
	}
	
	/**
	 * 批量发货
	 * @param qo
	 * @return
	 */
	@SuppressWarnings("finally")
	@PutMapping("/ord/order/bulkShipment")
	BulkShipmentRo bulkShipment(@RequestBody final BulkShipmentTo qo) {
		BulkShipmentRo bulkShipmentRo=new BulkShipmentRo();
		_log.info("批量打印快递面单的参数为：qo-{}",qo);		
		try {
			bulkShipmentRo = svc.bulkShipment(qo);
			_log.info("批量打印的返回值为：{}", bulkShipmentRo);
		}catch (final RuntimeException e) {
			final String msg = e.getMessage();
			if (msg.equals("调用快递电子面单参数错误")) {
				bulkShipmentRo.setResult(ShipmentConfirmationDic.PARAN_ERROR);
				bulkShipmentRo.setMsg(msg);
				_log.error(msg);
			} else if (msg.equals("该订单已发货")) {
				bulkShipmentRo.setResult(ShipmentConfirmationDic.ORDER_ALREADY_SHIPMENTS);
				bulkShipmentRo.setMsg(msg);
				_log.error(msg);
			} else if (msg.equals("调用快递电子面单失败")) {
				bulkShipmentRo.setResult(ShipmentConfirmationDic.INVOKE_ERROR);
				bulkShipmentRo.setMsg(msg);
				_log.error(msg);
			} else {
				bulkShipmentRo.setResult(ShipmentConfirmationDic.ERROR);
				bulkShipmentRo.setMsg("确认发货失败");
				_log.error(msg);
			}
		}finally {
			return bulkShipmentRo;
		}
	}

}
