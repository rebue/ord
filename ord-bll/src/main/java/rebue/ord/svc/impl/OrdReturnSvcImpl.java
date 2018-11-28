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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.afc.dic.TradeTypeDic;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.svr.feign.AfcSettleTaskSvc;
import rebue.afc.to.RefundTo;
import rebue.afc.to.TaskTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReturnApplicationStateDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.ReturnPageListRo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.AddReturnTo;
import rebue.ord.to.AgreeReturnTo;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.ord.to.ReceivedAndRefundedTo;
import rebue.ord.to.RejectReturnTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 用户退货信息
 *
 * 在单独使用不带任何参数的 @Transactional 注释时， propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED， 而且事务不会针对受控异常(checked exception)回滚。
 *
 * 注意： 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdReturnSvcImpl extends MybatisBaseSvcImpl<OrdReturnMo, java.lang.Long, OrdReturnMapper>
		implements OrdReturnSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdReturnSvcImpl.class);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(final OrdReturnMo mo) {
		_log.info("添加用户退货信息");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	@Resource
	private OrdReturnSvc thisSvc;
	@Resource
	private OrdReturnPicSvc ordReturnPicSvc;
	@Resource
	private OrdOrderSvc orderSvc;

	@Resource
	private OrdOrderDetailSvc orderDetailSvc;

	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

	@Resource
	private AfcRefundSvc refundSvc;

	@Resource
	private Mapper dozerMapper;

	@Resource
	private OnlOnlinePicSvc onlOnlinePicSvc;

	@Resource
	private AfcSettleTaskSvc afcSettleTaskSvc;

	/**
	 * 买家返款限制时间
	 */
	@Value("${ord.return-limit-time}")
	private int returnLimitTime;

	/**
	 * 添加用户退货信息 1、首先查询订单信息是是否存在和订单的状态 2、查询订单详情是否存在和是否可以退货
	 * 3、根据订单ID和订单详情ID查询退货订单退货信息，如果该订单退过货，则获取退货的数量 4、判断退货数量是否等于订单数量
	 * 5、判断已退数量当前退货数量是否大于订单数量 6、添加退货信息 7、修改订单详情退货数量和修改订单详情退货状态
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro addReturn(final AddReturnTo to) {
		_log.info("添加用户退货信息的参数为：{}", to);
		final Ro ro = new Ro();
		if (to.getOrderId() == null || to.getOrderDetailId() == null || to.getReturnCount() == null
				|| to.getReturnType() == null || to.getReturnReason() == null || to.getApplicationOpId() == null) {
			_log.error("添加用户退货信息时出现参数不正确，请求的参数为：{}", to);
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg("参数不正确");
			return ro;
		}

		_log.info("添加用户退货信息查询订单信息的参数为：{}", to.getOrderId());
		final OrdOrderMo ordOrderMo = orderSvc.getById(to.getOrderId());
		_log.info("添加用户退货信息查询订单信息的返回值为：{}", ordOrderMo);

		if (ordOrderMo == null) {
			_log.error("添加用户退货信息查询订单时发现订单不存在，申请操作人为：{}", to.getApplicationOpId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("订单不存在");
			return ro;
		}

		if (ordOrderMo.getOrderState().intValue() == OrderStateDic.SIGNED.getCode()
				|| ordOrderMo.getOrderState().intValue() == OrderStateDic.SETTLED.getCode()) {
			// 签收七天后时间戳
			final Long receivedTimestamp = ordOrderMo.getReceivedTime().getTime() + 86400000 * 7;
			// 当前时间戳
			final Long currentTimestamp = System.currentTimeMillis();
			System.out.println(receivedTimestamp >= currentTimestamp);
			if (receivedTimestamp >= currentTimestamp != true) {
				_log.error("添加用户退货信息时发现订单已超过退货时间，订单id为：{}", ordOrderMo.getId());
				ro.setResult(ResultDic.FAIL);
				ro.setMsg("该订单已超出退货时间");
				return ro;
			}
		}

		// 判断订单状态如果为取消、待支付状态或者已经超过退货时间的则不允许退货
		if (ordOrderMo.getOrderState().intValue() == OrderStateDic.CANCEL.getCode()
				|| ordOrderMo.getOrderState().intValue() == OrderStateDic.ORDERED.getCode()) {
			_log.error("添加用户退货信息时发现订单状态或已超过退货时间，订单id为：{}", ordOrderMo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前状态不允许退货");
			return ro;
		}

		_log.info("添加用户退货查询订单详情的参数为：{}", to.getOrderDetailId());
		final OrdOrderDetailMo ordOrderDetailMo = orderDetailSvc.getById(to.getOrderDetailId());
		_log.info("添加用户退货查询订单详情的返回值为：{}", ordOrderDetailMo);

		if (ordOrderDetailMo == null) {
			_log.error("添加用户退货查询订单详情时发现没有该订单详情，申请操作人为：{}", to.getApplicationOpId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("订单详情不存在");
			return ro;
		}

		if (ordOrderDetailMo.getReturnState().intValue() != ReturnStateDic.NONE.getCode()
				&& ordOrderDetailMo.getReturnState().intValue() != ReturnStateDic.PART_RETURNED.getCode()) {
			_log.error("添加用户退货查询订单详情时发现退货状态不处于未退货或部分已退状态，订单详情id为：{}", to.getOrderDetailId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前状态不允许退货");
			return ro;
		}

		// 实际购买数量
		final int realBuyCount = ordOrderDetailMo.getBuyCount() - ordOrderDetailMo.getReturnCount();
		if (realBuyCount < to.getReturnCount()) {
			_log.error("添加用户退货时发现退货数量大于实际购买数量，订单详情id为：{}", to.getOrderDetailId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("退货数量不能大于实际购买数量");
			return ro;
		}

		// 退货id
		final Long returnId = _idWorker.getId();

		final OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setId(returnId);
		returnMo.setReturnCode(_idWorker.getId());
		returnMo.setOrderId(to.getOrderId());
		returnMo.setOrderDetailId(to.getOrderDetailId());
		returnMo.setReturnCount(to.getReturnCount());
		returnMo.setReturnType(to.getReturnType());
		returnMo.setApplicationState((byte) ReturnApplicationStateDic.PENDING_REVIEW.getCode());
		returnMo.setReturnReason(to.getReturnReason());
		returnMo.setUserId(ordOrderMo.getUserId());
		returnMo.setApplicationOpId(to.getApplicationOpId());
		returnMo.setApplicationTime(new Date());
		_log.info("添加用户退货信息的参数为：{}", returnMo);
		final int addReturnResult = thisSvc.add(returnMo);
		_log.info("添加用户退货信息的返回值为：{}", addReturnResult);
		if (addReturnResult != 1) {
			_log.error("添加用户退货信息时出现错误，订单详情id为：{}", to.getOrderDetailId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("添加出错");
			return ro;
		}

		if (to.getReturnPicPath() != null && !to.getReturnPicPath().equals("")
				&& !to.getReturnPicPath().equals("null")) {
			final String[] returnPicPath = to.getReturnPicPath().split(",");
			for (final String element : returnPicPath) {
				final OrdReturnPicMo ordReturnPicMo = new OrdReturnPicMo();
				ordReturnPicMo.setId(_idWorker.getId());
				ordReturnPicMo.setPicPath(element);
				ordReturnPicMo.setReturnId(returnId);
				_log.info("添加用户退货信息添加退货图片的参数为：{}", ordReturnPicMo);
				final int addReturnPicResult = ordReturnPicSvc.add(ordReturnPicMo);
				_log.info("添加用户退货信息添加退货图片的参数为：{}", addReturnPicResult);
				if (addReturnPicResult != 1) {
					_log.info("添加用户退货信息添加退货图片时出现错误，订单详情id为：{}", to.getOrderDetailId());
					throw new RuntimeException("添加退货图片失败");
				}
			}
		}

		_log.info("添加用户退货信息修改订单详情退货状态的参数为：{}", to.getOrderDetailId());
		final int modifyReturnStateByIdResult = orderDetailSvc.modifyReturnStateById(to.getOrderDetailId(),
				(byte) ReturnStateDic.RETURNING.getCode());
		_log.info("添加用户退货信息修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
		if (modifyReturnStateByIdResult != 1) {
			_log.error("添加用户退货信息修改订单详情状态出现错误，订单详情id为：{}", to.getOrderDetailId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("添加出错");
			return ro;
		}

		_log.info("添加用户退货信息成功，订单详情id为：{}", to.getOrderDetailId());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("添加成功");
		return ro;
	}

	/**
	 * 查询分页列表信息
	 */
	@Override
	public PageInfo<ReturnPageListRo> selectReturnPageList(final ReturnPageListRo qo, final int pageNum,
			final int pageSize) {
		_log.info("selectReturnPageList: qo-{}; pageNum-{}; pageSize-{}", qo, pageNum, pageSize);
		final PageInfo<ReturnPageListRo> result = PageHelper.startPage(pageNum, pageSize)
				.doSelectPageInfo(() -> _mapper.selectReturnPageList(qo));
		_log.info("查询退货信息的返回值为：{}", result.getList());

		for (final ReturnPageListRo item : result.getList()) {
			final OrdReturnPicMo mo = new OrdReturnPicMo();
			mo.setReturnId(item.getId());
			_log.info("查询退货图片的参数为：{}", mo);
			final List<OrdReturnPicMo> picList = ordReturnPicSvc.list(mo);
			_log.info("查询退货图片的返回值为：{}", picList);
			item.setPicList(picList);
		}
		_log.info("查询退货信息和图片的返回值为：{}", result.getList());
		return result;
	}

	/**
	 * 拒绝退货
	 * 
	 * 1、判断参数是否正确 2、查询根据退货编号和订单编号查询退货信息 ，判断该退货编号是否存在和查询退货订单的申请状态是否处于待审核状态
	 * 3、查询订单信息并判断订单状态是否处于已取消状态 4、根据订单编号和订单详情ID查询订单详情信息并判断该订单详情的状态是否处于已退货的状态
	 * 5、修改订单详情退货数量和状态 6、修改退货订单信息
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro rejectReturn(final RejectReturnTo to) {
		_log.info("拒绝退货的请求参数为：{}", to);
		Ro ro = new Ro();
		if (to.getId() == null || to.getRejectOpId() == null || to.getRejectReason() == null) {
			_log.error("拒绝退货时出现参数错误，请求的参数为：{}", to);
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg("参数错误");
			return ro;
		}

		_log.info("拒绝退货查询退货信息的参数为：{}", to.getId());
		OrdReturnMo ordReturnMo = thisSvc.getById(to.getId());
		_log.info("拒绝退货查询退货信息的返回值为：{}", ordReturnMo);
		if (ordReturnMo == null) {
			_log.error("拒绝退货查询退货信息时发现没有该退货信息，请求参数为：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("没有该退货信息");
			return ro;
		}

		if (ordReturnMo.getApplicationState().intValue() != ReturnApplicationStateDic.RETURNING.getCode()
				&& ordReturnMo.getApplicationState().intValue() != ReturnApplicationStateDic.PENDING_REVIEW.getCode()) {
			_log.error("拒绝退货查询退货信息时发现申请状态不处于待审核或退货中，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前状态不允许拒绝");
			return ro;
		}

		_log.info("拒绝退货查询订单信息的参数为：{}", ordReturnMo.getOrderId());
		OrdOrderMo orderMo = orderSvc.getById(ordReturnMo.getOrderId());
		_log.info("拒绝退货查询订单信息的返回值为：{}", orderMo);
		if (orderMo == null) {
			_log.error("拒绝退货查询订单信息时发现没有该订单，退货id为：{}", ordReturnMo.getOrderId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("没有找到该订单信息");
			return ro;
		}

		if (orderMo.getOrderState().intValue() == OrderStateDic.CANCEL.getCode()
				|| orderMo.getOrderState().intValue() == OrderStateDic.ORDERED.getCode()) {
			_log.error("拒绝退货时发现订单未支付或已取消，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前订单未支付或已取消，拒绝退货失败。。。");
			return ro;
		}

		_log.info("拒绝退货查询订单详情信息的参数为：{}", ordReturnMo.getOrderDetailId());
		OrdOrderDetailMo orderDetailMo = orderDetailSvc.getById(ordReturnMo.getOrderDetailId());
		_log.info("拒绝退货查询订单详情信息的返回值为：{}", orderDetailMo);
		if (orderDetailMo == null) {
			_log.error("拒绝退货查询订单详情时发现该订单详情不存在，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("没有找到该订单详情");
			return ro;
		}

		if (orderDetailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
			_log.error("拒绝退货查询订单详情时发现该详情并未申请退货，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("该订单详情并未申请退货");
			return ro;
		}

		Date now = new Date();

		OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setId(to.getId());
		returnMo.setRejectOpId(to.getRejectOpId());
		returnMo.setRejectReason(to.getRejectReason());
		returnMo.setRejectTime(now);
		returnMo.setFinishOpId(to.getRejectOpId());
		returnMo.setFinishTime(now);
		_log.info("拒绝退货修改退货信息的参数为：{}", returnMo);
		int refusedReturnResult = _mapper.refusedReturn(returnMo);
		_log.info("拒绝退货修改退货信息的返回值为：{}", refusedReturnResult);
		if (refusedReturnResult != 1) {
			_log.error("拒绝退货修改退货信息时出现错误，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("操作出错");
			return ro;
		}

		_log.info("拒绝退货修改订单详情退货状态的参数为：{}", orderDetailMo.getId());
		final int modifyReturnStateByIdResult = orderDetailSvc.modifyReturnStateById(orderDetailMo.getId(),
				(byte) ReturnStateDic.NONE.getCode());
		_log.info("添加用户退货信息修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
		if (modifyReturnStateByIdResult != 1) {
			_log.error("添加用户退货信息修改订单详情状态出现错误，订单详情id为：{}", orderDetailMo.getId());
			throw new RuntimeException("操作错误");
		}

		_log.info("拒绝退货成功，退货id为：{}", to.getId());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("操作成功");
		return ro;
	}

	/**
	 * 同意退款
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro agreeRefund(final OrdOrderReturnTo to) {
		_log.info("同意退款的参数为：{}", to);
		final Ro ro = new Ro();

		// 退款补偿金参数为空设置默认值
		if (to.getRefundCompensation() == null) {
			to.setRefundCompensation(BigDecimal.ZERO);
		}

		_log.debug("检查参数的正确性");
		if (to.getReturnId() == null || to.getOrderId() == null || to.getOrderDetailId() == null || to.getOpId() == null //
				|| (to.getRefundAmount() == null && (to.getRefundAmount1() == null || to.getRefundAmount2() == null))) {
			final String msg = "参数错误";
			_log.error("{}: {}", msg, "没有传入退货ID/订单ID/退款金额/退款金额1(余额)/退款金额2(返现金)/退款补偿金/订单详情ID/操作人");
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg(msg);
			return ro;
		}

		// 是否自动计算退款: 传入退款金额参数则为自动计算退款，否则为自定义退款
		final boolean isAutoCalcRefund = to.getRefundAmount() != null;
		_log.debug(isAutoCalcRefund ? "自动计算退款: 根据用户输入退款金额自动计算退回的返现金和余额" : "自定义退款: 用户具体指定退回返现金和余额的数字");
		// 如果是自定义退款，退款金额 = 退款金额1(余额) + 退款金额2(返现金)
		if (!isAutoCalcRefund) {
			to.setRefundAmount(to.getRefundAmount1().add(to.getRefundAmount2()));
		}

		final OrdOrderMo order = orderSvc.getById(to.getOrderId());
		if (order == null) {
			final String msg = "订单不存在";
			_log.error("{}: {}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}
		_log.info("获取到退款的订单信息：{}", order);

		if (order.getOrderState() == OrderStateDic.CANCEL.getCode()
				|| order.getOrderState() == OrderStateDic.ORDERED.getCode()
				|| order.getOrderState() == OrderStateDic.SETTLED.getCode()) {
			final String msg = "订单处于已取消/未支付/已结算三种状态之一，不能退款";
			_log.error("{}：{}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		final OrdOrderDetailMo detail = orderDetailSvc.getById(to.getOrderDetailId());
		_log.info("获取到退款的订单详情信息：{}", detail);
		if (detail == null) {
			final String msg = "订单详情不存在";
			_log.error("{}：{}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		if (ReturnStateDic.RETURNING.getCode() != detail.getReturnState()) {
			final String msg = "订单详情的状态并未在退货中";
			_log.error("{}：{}", msg, detail);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		final OrdReturnMo returnInfo = thisSvc.getById(to.getReturnId());
		_log.info("获取到退货单信息：{}", returnInfo);
		if (returnInfo == null) {
			final String msg = "退货单不存在";
			_log.error("{}：{}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		if (ReturnApplicationStateDic.PENDING_REVIEW.getCode() != returnInfo.getApplicationState()
				&& ReturnApplicationStateDic.RETURNING.getCode() != returnInfo.getApplicationState()) {
			final String msg = "退货单不处于待审核状态，不能审核";
			_log.error("{}：{}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		// 订单已经退款总额
		final BigDecimal refundedTotal = order.getReturnTotal();
		// 订单退款总额 = 订单已经退款总额 + 本次退款金额 + 扣除补偿金
		final BigDecimal refundTotal = refundedTotal.add(to.getRefundAmount()).add(to.getRefundCompensation());
		_log.debug("订单已经退款总额：{}, 本次退款金额: {}", refundedTotal, to.getRefundAmount());
		_log.debug("订单退款总额 = 订单已经退款总额 + 本次退款金额 + 扣除补偿金：{}", refundTotal);
		_log.debug("订单实际支付金额：{}", order.getRealMoney());
		if (refundTotal.compareTo(order.getRealMoney()) > 0) {
			final String msg = "订单退款总额大于订单实际支付金额，不能退款";
			_log.error("{}：{}", msg, to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		// 修改订单退款金额
		// 如果订单退货总额==订单实际支付金额，修改订单状态为取消
		Byte orderState = null;
		if (refundTotal.compareTo(order.getRealMoney()) == 0) {
			_log.debug("订单退货总额==订单实际支付金额，修改订单状态为取消");
			orderState = (byte) OrderStateDic.CANCEL.getCode();
		}
		// 修改订单退款金额
		final int modifyRefundRowCount = orderSvc.modifyRefund(refundTotal, orderState, order.getId(), refundedTotal);
		if (modifyRefundRowCount != 1) {
			final String msg = "修改订单退款金额出错，可能出现并发问题";
			_log.error("{}：退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", msg, refundTotal, orderState, order.getId(),
					refundedTotal);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg(msg);
			return ro;
		}

		// 退货数量为空，则表示仅退款，不为空，则表示退货退款
		if (to.getReturnNum() != null && to.getReturnNum() > 0) {
			// 订单详情已退货数量
			final Integer returnedCount = detail.getReturnCount();
			// 订单详情退货总数 = 已退货数量 + 本次退货数量
			final int returnTotal = returnedCount + returnInfo.getReturnCount();
			// 退货状态
			Byte returnState = null;
			// 如果退货总数==购买数量，则为已退货
			if (returnTotal == detail.getBuyCount()) {
				returnState = (byte) ReturnStateDic.RETURNED.getCode();
			}
			// 如果退货总数<购买数量，则为部分退货
			else if (returnTotal < detail.getBuyCount()) {
				returnState = (byte) ReturnStateDic.PART_RETURNED.getCode();
			} else {
				final String msg = "退货总数不能大于购买数量";
				_log.error("{}：退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", msg, refundTotal, orderState, order.getId(),
						refundedTotal);
				throw new RuntimeException(msg);
			}
			// 之前的返现金总额
			final BigDecimal oldCashbackTotal = detail.getCashbackTotal() == null ? BigDecimal.ZERO
					: detail.getCashbackTotal();
			// 退货后的返现金总额 = 返现金额 * (购买数量 - 退货总数)
			final BigDecimal newCashbackTotal = detail.getCashbackAmount()
					.multiply(BigDecimal.valueOf(detail.getBuyCount() - returnTotal));
			final int modifyReturnRowCount = orderDetailSvc.modifyReturn(returnTotal, newCashbackTotal, returnState, //
					detail.getId(), returnedCount, oldCashbackTotal);
			if (modifyReturnRowCount != 1) {
				final String msg = "修改订单详情的退货情况出错，可能出现并发问题";
				_log.error("{}：退货总数-{}, 新的返现金总额-{}, 退货状态-{}, where-订单详情ID-{}, where-之前的已退货数量-{}, where-退货之前的返现金总额-{}",
						msg, returnTotal, newCashbackTotal, returnState, detail.getId(), returnedCount,
						oldCashbackTotal);
				throw new RuntimeException(msg);
			}

			// 修改退货单
			final Date now = new Date();
			// 本次退款总额 = 本次退款金额 + 本次扣除补偿金额
			final BigDecimal currentRefundTotal = to.getRefundAmount().add(to.getRefundCompensation());
			_log.info("确认退款修改退货单的参数为：本次退款总额-{} 扣除补偿金额-{} 操作人ID-{} 操作时间-{} 退货ID-{}", currentRefundTotal, //
					to.getRefundCompensation(), to.getOpId(), now, to.getReturnId());
			final int confirmRefundRowCount = _mapper.confirmRefund(currentRefundTotal, //
					to.getRefundCompensation(), (byte) ReturnApplicationStateDic.TURNED.getCode(), to.getOpId(), now,
					to.getReturnId());
			_log.info("同意退款修改退货信息的返回值为：{}", confirmRefundRowCount);
		}

		_log.info("获取该定单详情做为下家的购买关系记录");
		final OrdBuyRelationMo buyRelationParamMo = new OrdBuyRelationMo();
		buyRelationParamMo.setDownlineOrderDetailId(detail.getId());
		final OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(buyRelationParamMo);
		_log.info("获取该定单详情做为下家的购买关系记录为:{}", buyRelationResult);
		if (buyRelationResult == null) {
			_log.info("该定单详情做为下家的购买关系记录为空");
		} else {
			_log.info("删除该定单详情做为下家的购买关系记录");
			final int delResult = ordBuyRelationSvc.del(buyRelationResult.getId());
			_log.info("删除该定单详情的购买关系记录的返回值为：{}", delResult);
			if (delResult != 1) {
				_log.info("删除该定单详情做为下家的购买关系记录失败");
				throw new RuntimeException("删除该定单详情做为下家的购买关系记录失败");
			}
			_log.info("更新该定单详情上家的返佣名额");
			final OrdOrderDetailMo ordDetailMo = orderDetailSvc.getById(buyRelationResult.getUplineOrderDetailId());
			if (ordDetailMo == null) {
				_log.info("该定单上家的定单详情为空");
				throw new RuntimeException("该定单上家的定单详情为空");
			}
			final byte commissionSlot = ordDetailMo.getCommissionSlot();
			ordDetailMo.setCommissionSlot((byte) (commissionSlot + 1));
			final int updateUplineDetailResult = orderDetailSvc.modify(ordDetailMo);
			if (updateUplineDetailResult != 1) {
				_log.info("更新该定单详情上家的返佣名额失败");
				throw new RuntimeException("更新该定单详情上家的返佣名额失败");
			}
		}
		_log.info("获取该定单详情下家购买关系");
		final OrdBuyRelationMo buyRelationParamMo1 = new OrdBuyRelationMo();
		buyRelationParamMo1.setUplineOrderDetailId(detail.getId());
		final List<OrdBuyRelationMo> buyRelationResult1 = ordBuyRelationSvc.list(buyRelationParamMo1);
		if (buyRelationResult1.size() == 0) {
			_log.info("该定单详情下家购买关系为空");
		} else {
			for (int i = 0; i < buyRelationResult1.size(); i++) {
				_log.info("重新匹配该定单详情下家购买关系，购买关系ID：" + buyRelationResult1.get(i).getId());
				_log.info("全返商品添加购买关系");
				final long userId = buyRelationResult1.get(i).getDownlineUserId();
				final long onlineId = detail.getOnlineId();
				final BigDecimal buyPrice = detail.getBuyPrice();
				final long downLineDetailId = detail.getId();
				final long downLineOrderId = detail.getOrderId();
				final String matchBuyRelationResult = ordBuyRelationSvc.matchBuyRelation(userId, onlineId, buyPrice,
						downLineDetailId, downLineOrderId);
				_log.info(matchBuyRelationResult);
				_log.info("删除购买关系：" + buyRelationResult1.get(i).getId());
				final int delResult = ordBuyRelationSvc.del(buyRelationResult1.get(i).getId());
				if (delResult != 1) {
					_log.info("删除购买关系失败：" + buyRelationResult1.get(i).getId());
					throw new RuntimeException("删除购买关系失败");
				}
			}
		}

		// 退款
		final RefundTo refundTo = new RefundTo();
		refundTo.setOrderId(String.valueOf(order.getPayOrderId()));
		refundTo.setRefundId(to.getReturnId());
		refundTo.setBuyerAccountId(order.getUserId());
		refundTo.setSellerAccountId(order.getOnlineOrgId());
		refundTo.setSupplierAccountId(detail.getSupplierId());
		refundTo.setTradeTitle("用户退货-退款");
		refundTo.setTradeDetail(detail.getOnlineTitle() + detail.getSpecName());
		refundTo.setRefundAmount(to.getRefundAmount());
		refundTo.setReturnBalanceToBuyer(to.getRefundAmount1());
		refundTo.setReturnCashbackToBuyer(to.getRefundAmount2());
		refundTo.setReturnCompensationToSeller(to.getRefundCompensation());
		refundTo.setOpId(to.getOpId());
		refundTo.setMac("不在获取MAC地址");
		refundTo.setIp(to.getIp());
		_log.info("退款的参数为：{}", refundTo);
		final Ro refundRo = refundSvc.refund(refundTo);
		_log.info("退款返回值为：{}", refundRo);
		if (ResultDic.SUCCESS != refundRo.getResult()) {
			_log.error("退款出错，收到错误信息：{}", refundRo);
			throw new RuntimeException(refundRo.getMsg());
		}

		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("退款成功");
		return ro;
	}

	/**
	 * 同意退货 1、判断参数是否齐全 2、查询退货信息并判断退货信息是否存在和申请状态是否为待审核状态 3、修改退货信息（添加审核操作人、时间、修改申请状态）
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro agreeReturn(final AgreeReturnTo to) {
		_log.info("同意退货的参数为：{}", to);
		final Ro ro = new Ro();
		if (to.getId() == null || to.getReviewOpId() == null) {
			_log.error("同意退货时出现参数错误，请求的参数为：{}", to);
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg("参数错误");
			return ro;
		}

		_log.info("同意退货查询退货信息的参数为：{}", to.getId());
		final OrdReturnMo ordReturnMo = thisSvc.getById(to.getId());
		_log.info("同意退货查询退货信息的返回值为：{}", ordReturnMo);
		if (ordReturnMo == null) {
			_log.error("同意退货查询退货信息时发现退货信息不存在，请求参数为：{}", to);
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg("退货信息不存在");
			return ro;
		}

		if (ordReturnMo.getApplicationState().intValue() != ReturnApplicationStateDic.PENDING_REVIEW.getCode()) {
			_log.error("同意退货时发现该退货申请状态不处于待审核，请求参数为：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前状态不允许审核");
			return ro;
		}

		final OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setId(to.getId());
		returnMo.setReviewOpId(to.getReviewOpId());
		returnMo.setReviewTime(new Date());
		_log.info("同意退货修改退货信息的参数为：{}", returnMo);
		final int returnApproveResult = _mapper.returnApprove(returnMo);
		_log.info("同意退货修改退货信息的返回值为：{}", returnApproveResult);
		if (returnApproveResult != 1) {
			_log.error("同意退货修改退货信息时出现错误，请求参数为：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("操作失败");
			return ro;
		}
		_log.info("同意退货成功，请求参数为：{}", to);
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("操作成功");
		return ro;
	}

	/**
	 * 已收到货并退款 1、判断请求参数 2、查询退货信息并判断是否存在和申请状态是否处于退货中 3、查询订单信息并判断订单状态是否处于作废、已下单（待支付）状态
	 * 4、查询订单详情并判断
	 * 
	 * @deprecated
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro receivedAndRefunded(final ReceivedAndRefundedTo to) {
		_log.info("已收到货并退款的参数为：{}", to);
		final Ro ro = new Ro();
		if (to.getId() == null || to.getIp() == null || to.getOpId() == null || (to.getRefundAmount() == null
				&& to.getReturnBalanceToBuyer() == null && to.getReturnCashbackToBuyer() == null)) {
			_log.error("已收到货并退款出现参数错误，请求的参数为：{}", to);
			ro.setResult(ResultDic.PARAM_ERROR);
			ro.setMsg("参数错误");
			return ro;
		}

		_log.info("已收到货并退款查询退货信息的参数为：{}", to.getId());
		final OrdReturnMo ordReturnMo = thisSvc.getById(to.getId());
		_log.info("已收到货并退款查询退货信息的返回值为：{}", ordReturnMo);
		if (ordReturnMo == null) {
			_log.error("已收到货并退还查询退货信息时发现该退货信息不存在，请求参数为：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("退货信息不存在");
			return ro;
		}

		if (ordReturnMo.getApplicationState().intValue() != ReturnApplicationStateDic.RETURNING.getCode()) {
			_log.error("已收到货并退款查询退货信息时发现申请状态不处于退货中，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前状态不允许退款");
			return ro;
		}

		_log.info("已收到货并退款查询订单信息的参数为：{}", ordReturnMo.getOrderId());
		final OrdOrderMo ordOrderMo = orderSvc.getById(ordReturnMo.getOrderId());
		_log.info("已收到货并退款查询订单信息的返回值为：{}", ordOrderMo);
		if (ordOrderMo == null) {
			_log.error("已收到货并退款查询订单信息时发现订单不存在，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("订单不存在");
			return ro;
		}

		if (ordOrderMo.getOrderState().intValue() == OrderStateDic.CANCEL.getCode()
				|| ordOrderMo.getOrderState().intValue() == OrderStateDic.ORDERED.getCode()) {
			_log.error("已收到货并退款查询订单信息时发现订单状态处于已取消或待支付状态，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前订单状态不允许退款");
			return ro;
		}

		_log.info("已收到货并退款查询订单详情的参数为：{}", ordReturnMo.getOrderDetailId());
		final OrdOrderDetailMo ordOrderDetailMo = orderDetailSvc.getById(ordReturnMo.getOrderDetailId());
		_log.info("已收到货并退款查询订单详情的返回值为：{}", ordOrderDetailMo);
		if (ordOrderDetailMo == null) {
			_log.error("已收到货并退款查询订单详情时发现该订单详情不存在，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("订单详情不存在");
			return ro;
		}

		if (ordOrderDetailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
			_log.info("已收到货并退款查询订单详情时发现该详情退货状态不处于退货中，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("当前退货状态允许退款");
			return ro;
		}

		final Date now = new Date();

		final OrdReturnMo returnMo = new OrdReturnMo();
		// 退款总额
		BigDecimal refundTotal = new BigDecimal("0");
		// 退款补偿金额(退货退款产生的需补偿给卖家的金额，例如补偿运费)
		BigDecimal refundCompensation = new BigDecimal("0");
		if (to.getReturnCompensationToSeller() != null) {
			refundCompensation = to.getReturnCompensationToSeller();
			returnMo.setRefundCompensation(to.getReturnCompensationToSeller());
		}
		if (to.getRefundAmount() != null) {
			refundTotal = to.getRefundAmount();
		} else {
			refundTotal = refundCompensation.add(to.getReturnBalanceToBuyer()).add(to.getReturnCompensationToSeller())
					.setScale(4, BigDecimal.ROUND_HALF_UP);
		}
		returnMo.setId(to.getId());
		returnMo.setRefundTotal(refundTotal);
		returnMo.setRefundCompensation(refundCompensation);
		returnMo.setApplicationState((byte) ReturnApplicationStateDic.RETURNING.getCode());
		returnMo.setRefundOpId(to.getOpId());
		returnMo.setRefundTime(now);
		returnMo.setFinishOpId(to.getOpId());
		returnMo.setFinishTime(now);
		returnMo.setReceiveOpId(to.getOpId());
		returnMo.setReceiveTime(now);
		_log.info("已收到货并退款修改退货信息的参数为：{}", returnMo);
		final int confirmReceiptOfGoodsResult = _mapper.confirmReceiptOfGoods(returnMo);
		_log.info("已收到货并退款修改退货信息的返回值为：{}", confirmReceiptOfGoodsResult);
		if (confirmReceiptOfGoodsResult != 1) {
			_log.error("已收到货并退款修改退货信息出错，退货id为：{}", to.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("操作出错");
			return ro;
		}

		// 订单详情退货状态
		Byte returnState = (byte) ReturnStateDic.PART_RETURNED.getCode();

		// 订单详情真实购买数量
		final int realBuyCount = ordOrderDetailMo.getBuyCount() - ordOrderDetailMo.getReturnCount()
				- ordReturnMo.getReturnCount();
		if (realBuyCount <= 0) {
			returnState = (byte) ReturnStateDic.RETURNED.getCode();
		}
		_log.info("已收到货并退款修改订单详情退货状态的参数为：{}", ordReturnMo.getOrderDetailId());
		final int modifyReturnStateByIdResult = orderDetailSvc.modifyReturnStateById(ordReturnMo.getOrderDetailId(),
				returnState);
		_log.info("已收到货并退款修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
		if (modifyReturnStateByIdResult != 1) {
			_log.error("已收到货并退款修改订单详情状态出现错误，订单详情id为：{}", ordReturnMo.getOrderDetailId());
			throw new RuntimeException("修改出错");
		}

		final RefundTo refundTo = new RefundTo();
		refundTo.setOrderId(ordReturnMo.getOrderId().toString());
		refundTo.setBuyerAccountId(ordReturnMo.getUserId());
		refundTo.setSellerAccountId(ordOrderMo.getOnlineOrgId());
		refundTo.setSupplierAccountId(ordOrderDetailMo.getSupplierId());
		refundTo.setTradeTitle("大卖网络-用户退货退款");
		refundTo.setTradeDetail("退货的商品为：" + ordOrderDetailMo.getOnlineTitle() + " " + ordOrderDetailMo.getSpecName());
		refundTo.setOpId(to.getOpId());
		refundTo.setMac("不再获取MAC地址");
		refundTo.setIp(to.getIp());
		if (to.getRefundAmount() != null) {
			refundTo.setRefundAmount(to.getRefundAmount());
		} else {
			refundTo.setReturnBalanceToBuyer(to.getReturnBalanceToBuyer());
			refundTo.setReturnCashbackToBuyer(to.getReturnCashbackToBuyer());
		}
		refundTo.setReturnCompensationToSeller(to.getReturnCompensationToSeller());
		_log.info("已收到货并退款执行退款的参数为：{}", refundTo);
		final Ro refundRo = refundSvc.refund(refundTo);
		_log.info("已收到货并退款执行退款的返回值为：{}", refundRo);
		if (refundRo.getResult() != ResultDic.SUCCESS) {
			_log.error("已收到货并退款执行退款出现错误，退货编号为：{}", to.getId());
			throw new RuntimeException("执行退款出错");
		}
		_log.info("已收到货并退款成功，退货id为：{}", to.getId());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("操作成功");
		return ro;
	}

	/**
	 * 查询退货中的数据
	 */
	@Override
	public List<Map<String, Object>> selectReturningInfo(final Map<String, Object> map) throws ParseException,
			IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_log.info("查询用户退货中订单信息的参数为：{}", map.toString());
		final List<Map<String, Object>> list = new ArrayList<>();
		final List<OrdReturnMo> orderReturnList = _mapper.selectReturningOrder(map);
		_log.info("查询的结果为: {}", String.valueOf(orderReturnList));
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderReturnList.size() != 0) {
			for (int i = 0; i < orderReturnList.size(); i++) {
				final Map<String, Object> hm = new HashMap<>();
				final String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
				final Date date = simpleDateFormat.parse(l);
				final long ts = date.getTime();
				_log.info("转换时间得到的时间戳为：{}", ts);
				hm.put("dateline", ts / 1000);
				hm.put("finishDate", ts / 1000 + 86400);
				hm.put("system", System.currentTimeMillis() / 1000);
				final OrdReturnMo obj = orderReturnList.get(i);
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
				_log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
				final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
				detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
				_log.info("查询用户退货订单获取订单详情的参数为：{}", detailMo.toString());
				final OrdOrderDetailMo orderDetailResult = orderDetailSvc.getById(detailMo.getId());
				_log.info("查询用户订单信息获取订单详情的返回值为：{}", orderDetailResult);
				final List<OrderDetailRo> orderDetailRoList = new ArrayList<>();
				_log.info("查询用户订单信息开始获取商品主图");
				final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailResult.getOnlineId(),
						(byte) 1);
				_log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
				final OrderDetailRo orderDetailRo = new OrderDetailRo();
				orderDetailRo.setId(orderDetailResult.getId());
				orderDetailRo.setOrderId(orderDetailResult.getOrderId());
				orderDetailRo.setOnlineId(orderDetailResult.getOnlineId());
				orderDetailRo.setProductId(orderDetailResult.getProductId());
				orderDetailRo.setOnlineTitle(orderDetailResult.getOnlineTitle());
				orderDetailRo.setSpecName(orderDetailResult.getSpecName());
				orderDetailRo.setBuyCount(orderDetailResult.getBuyCount());
				orderDetailRo.setBuyPrice(orderDetailResult.getBuyPrice());
				orderDetailRo.setCashbackAmount(orderDetailResult.getCashbackAmount());
				orderDetailRo.setBuyUnit(orderDetailResult.getBuyUnit());
				orderDetailRo.setReturnState(orderDetailResult.getReturnState());
				orderDetailRo.setGoodsQsmm(onlinePicList.get(0).getPicPath());
				orderDetailRo.setReturnCount(orderDetailResult.getReturnCount());
				orderDetailRo.setCashbackTotal(orderDetailResult.getCashbackTotal());
				orderDetailRo.setSubjectType(orderDetailResult.getSubjectType());
				orderDetailRoList.add(orderDetailRo);
				hm.put("items", orderDetailRoList);
				list.add(i, hm);
			}
		}
		_log.info("最新获取用户退货订单信息的返回值为：{}", String.valueOf(list));
		return list;
	}

	@Override
	public List<Map<String, Object>> selectReturnInfo(final Map<String, Object> map) throws ParseException,
			IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
		final List<Map<String, Object>> list = new ArrayList<>();
		final List<OrdReturnMo> orderReturnList = _mapper.selectReturnOrder(map);
		_log.info("查询的结果为: {}", String.valueOf(orderReturnList));
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderReturnList.size() != 0) {
			for (int i = 0; i < orderReturnList.size(); i++) {
				final Map<String, Object> hm = new HashMap<>();
				final String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
				final Date date = simpleDateFormat.parse(l);
				final long ts = date.getTime();
				_log.info("转换时间得到的时间戳为：{}", ts);
				hm.put("dateline", ts / 1000);
				hm.put("finishDate", ts / 1000 + 86400);
				hm.put("system", System.currentTimeMillis() / 1000);
				final OrdReturnMo obj = orderReturnList.get(i);
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
				_log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
				final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
				detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
				_log.info("查询用户退货订单获取订单详情的参数为：{}", detailMo.toString());
				final OrdOrderDetailMo orderDetailResult = orderDetailSvc.getById(detailMo.getId());
				_log.info("查询用户订单信息获取订单详情的返回值为：{}", orderDetailResult);
				final List<OrderDetailRo> orderDetailRoList = new ArrayList<>();
				_log.info("查询用户订单信息开始获取商品主图");
				final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailResult.getOnlineId(),
						(byte) 1);
				_log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
				final OrderDetailRo orderDetailRo = new OrderDetailRo();
				orderDetailRo.setId(orderDetailResult.getId());
				orderDetailRo.setOrderId(orderDetailResult.getOrderId());
				orderDetailRo.setOnlineId(orderDetailResult.getOnlineId());
				orderDetailRo.setProductId(orderDetailResult.getProductId());
				orderDetailRo.setOnlineTitle(orderDetailResult.getOnlineTitle());
				orderDetailRo.setSpecName(orderDetailResult.getSpecName());
				orderDetailRo.setBuyCount(orderDetailResult.getBuyCount());
				orderDetailRo.setBuyPrice(orderDetailResult.getBuyPrice());
				orderDetailRo.setCashbackAmount(orderDetailResult.getCashbackAmount());
				orderDetailRo.setBuyUnit(orderDetailResult.getBuyUnit());
				orderDetailRo.setReturnState(orderDetailResult.getReturnState());
				orderDetailRo.setGoodsQsmm(onlinePicList.get(0).getPicPath());
				orderDetailRo.setReturnCount(orderDetailResult.getReturnCount());
				orderDetailRo.setCashbackTotal(orderDetailResult.getCashbackTotal());
				orderDetailRo.setSubjectType(orderDetailResult.getSubjectType());
				orderDetailRoList.add(orderDetailRo);
				hm.put("items", orderDetailRoList);
				list.add(i, hm);
			}
		}
		_log.info("最新获取用户退货订单信息的返回值为：{}", String.valueOf(list));
		return list;
	}

	/**
	 * 判断订单是否有订单详情在退货中(退货状态在待审核、退货中都算)
	 */
	@Override
	public Boolean hasReturningInOrder(final Long orderId) {
		_log.info("判断订单是否有订单详情在退货中: orderId-{}", orderId);
		return _mapper.hasReturningInOrder(orderId);
	}

	/**
	 * 取消退货
	 * 
	 * @param mo
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro cancelReturn(final OrdReturnMo mo) {
		_log.info("取消退货的请求参数为：{}", mo);
		final Ro ro = new Ro();
		_log.info("取消退货查询退货信息的参数为：{}", mo);
		final OrdReturnMo ordReturnMo = _mapper.selectByPrimaryKey(mo.getId());
		_log.info("取消退货查询退货信息的返回值为：{}", ordReturnMo);
		if (ordReturnMo == null) {
			_log.error("取消订单查询退货信息返回为空，退货id为: {}", mo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数错误");
			return ro;
		}

		// 修改订单详情退货状态
		_log.info("取消退货修改订单详情退货状态的参数为：{}", ordReturnMo.getOrderDetailId());
		final int modifyReturnStateByIdResult = orderDetailSvc.modifyReturnStateById(ordReturnMo.getOrderDetailId(),
				(byte) 0);
		_log.info("取消退货修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
		if (modifyReturnStateByIdResult != 1) {
			_log.error("取消订单修改订单详情状态失败，退货id为: {}", mo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("修改状态失败");
			return ro;
		}

		// 恢复该详情返佣任务
		final TaskTo taskTo = new TaskTo();
		taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
		taskTo.setOrderDetailId(String.valueOf(ordReturnMo.getOrderDetailId()));
		_log.info("取消退货恢复返佣任务的参数为：{}", taskTo);
		final Ro resumeTask = afcSettleTaskSvc.resumeTask(taskTo);
		_log.info("取消退货恢复返佣任务的返回值为：{}", resumeTask);
		if (resumeTask.getResult().getCode() == ResultDic.FAIL.getCode()) {
			_log.error("取消订单恢复返佣任务失败，退货id为: {}", mo.getId());
			throw new RuntimeException("恢复返佣失败");
		}

		// 修改退货信息表申请状态和取消的操作人等信息
		_log.info("取消退货信息退货信息的参数为：{}", mo);
		final int updateByPrimaryKeySelectiveResult = _mapper.updateByPrimaryKeySelective(mo);
		_log.info("取消退货信息退货信息的返回值为：{}", updateByPrimaryKeySelectiveResult);
		if (updateByPrimaryKeySelectiveResult != 1) {
			_log.error("取消退货修改退货信息出错，退货id为：{}", mo.getId());
			throw new RuntimeException("操作失败");
		}
		_log.info("取消退货成功，退货id为：{}", mo.getId());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("操作成功");
		return ro;
	}
}
