package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.afc.returngoods.ro.ReturnGoodsByBuyerRo;
import rebue.afc.returngoods.to.ReturnGoodsByBuyerTo;
import rebue.afc.svr.feign.AfcReturnGoodsSvc;
import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
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
public class OrdReturnSvcImpl extends MybatisBaseSvcImpl<OrdReturnMo, java.lang.Long, OrdReturnMapper>
		implements OrdReturnSvc {

	private final static Logger _log = LoggerFactory.getLogger(OrdReturnSvcImpl.class);

	@Resource
	private OrdReturnSvc ordReturnSvc;

	@Resource
	private OrdReturnPicSvc ordReturnPicSvc;

	@Resource
	private OrdOrderSvc ordOrderSvc;

	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	/*
	 * @Resource private AfcReturnGoodsSvc afcReturnGoodsSvr;
	 */

	/**
	 * 添加用户退货信息 Title: addEx Description: 1、首先查询订单信息是是否存在和订单的状态 2、查询订单详情是否存在和是否可以退货
	 * 3、根据订单ID和订单详情ID查询退货订单退货信息，如果该订单退过货，则获取退货的数量 4、判断退货数量是否等于订单数量 5、判断已退数量 +
	 * 当前退货数量是否大于订单数量 6、添加退货信息 7、修改订单详情退货数量和修改订单详情退货状态
	 * 
	 * @param to
	 * @return
	 * @date 2018年4月19日 下午2:52:14
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> addEx(OrdOrderReturnTo to) {
		_log.info("添加用户退货信息的参数为：{}", to.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 订单编号
		long orderCode = to.getOrderCode();
		// 订单详情Id
		long orderDetailId = to.getOrderDetailId();
		// 用户ID
		long userId = to.getUserId();
		// 退货数量
		int returnNum = to.getReturnNum();

		// =============================查询订单状态开始=============================
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderCode));
		orderMo.setUserId(userId);
		_log.info("查询订单信息的参数为：{}", orderMo.toString());
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		if (orderList.size() == 0) {
			_log.error("添加退货信息出现订单不存在，订单编号为：{}", orderCode);
			resultMap.put("result", -1);
			resultMap.put("msg", "该订单不存在");
			return resultMap;
		}
		// 订单状态
		byte orderState = orderList.get(0).getOrderState();
		if (orderState == -1) {
			_log.error("添加退货信息出现订单已取消，订单编号为：{}", orderCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "该订单已取消");
			return resultMap;
		}
		if (orderState == 1) {
			_log.error("添加退货信息出现订单未支付，订单编号为：{}", orderCode);
			resultMap.put("result", -3);
			resultMap.put("msg", "该订单未支付");
			return resultMap;
		}
		if (orderList.get(0).getRealMoney() == orderList.get(0).getReturnTotal()) {
			_log.error("添加退货信息出现订单已全部退完，订单编号为：{}", orderCode);
			resultMap.put("result", -4);
			resultMap.put("msg", "该订单已全部退完");
			return resultMap;
		}
		// =============================查询订单状态结束=============================

		// =============================查询订单详情状态开始=============================
		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(orderCode);
		orderDetailMo.setSpecName(to.getSpecName());
		_log.info("查询订单详情的参数为：{}", orderDetailMo.toString());
		List<OrdOrderDetailMo> orderDetailList = new ArrayList<OrdOrderDetailMo>();
		try {
			orderDetailList = ordOrderDetailSvc.list(orderDetailMo);
			_log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
		} catch (Exception e) {
			_log.error("===========查询订单详情出错了===========");
			e.printStackTrace();
		}
		if (orderDetailList.size() == 0) {
			_log.error("添加退货信息出现订单详情不存在，订单详情编号为：{}", orderDetailId);
			resultMap.put("result", -5);
			resultMap.put("msg", "该订单不存在");
			return resultMap;
		}
		if (orderDetailList.get(0).getReturnState() != 0) {
			_log.error("添加退货信息出现订单详情退货状态不处于未退货状态，订单详情编号为：{}", orderDetailId);
			resultMap.put("result", -6);
			resultMap.put("msg", "当前状态不允许退货");
			return resultMap;
		}
		// 订单详情退货数量
		Integer returnCount = orderDetailList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		// 订单详情购买数量
		int buyCount = orderDetailList.get(0).getBuyCount();
		if (buyCount == returnCount) {
			_log.error("添加退货信息出现订单详情退货数量等于购买数量，订单详情编号为：{}", orderDetailId);
			resultMap.put("result", -7);
			resultMap.put("msg", "该规格已退完");
			return resultMap;
		}
		// 最新订单详情退货数量
		int newReturnCount = returnCount + returnNum;
		if (buyCount < newReturnCount) {
			_log.error("添加退货信息出现订单详情退货数量大于订单购买数量，订单详情编号为：{}", orderDetailId);
			resultMap.put("result", -8);
			resultMap.put("msg", "退货数量不能大于购买数量");
			return resultMap;
		}
		// =============================查询订单详情状态结束=============================

		// =============================添加退货信息开始=============================
		// 申请时间
		Date date = new Date();
		// 退货编号
		long returnCode = _idWorker.getId();
		OrdReturnMo ordReturnMo = new OrdReturnMo();
		ordReturnMo.setId(_idWorker.getId());
		ordReturnMo.setReturnCode(returnCode);
		ordReturnMo.setOrderId(orderCode);
		ordReturnMo.setOrderDetailId(orderDetailId);
		ordReturnMo.setReturnCount(returnNum);
		ordReturnMo.setReturnRental(to.getReturnPrice());
		ordReturnMo.setReturnType(to.getReturnType());
		ordReturnMo.setApplicationState((byte) 1);
		ordReturnMo.setRefundState((byte) 1);
		ordReturnMo.setReturnReason(to.getReturnReason());
		ordReturnMo.setApplicationOpId(userId);
		ordReturnMo.setApplicationTime(date);
		_log.info("添加退货信息的参数为：{}", ordReturnMo.toString());
		int insertReturnresult = 0;
		try {
			insertReturnresult = _mapper.insertSelective(ordReturnMo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_log.info("添加退货信息的返回值为：{}", insertReturnresult);
		if (insertReturnresult < 1) {
			_log.error("添加退货信息出错，返回值为：{}", insertReturnresult);
			throw new RuntimeException("添加退货信息失败");
		}
		// =============================添加退货信息结束=============================

		// =============================添加退货图片开始=============================
		// 退货图片，以逗号隔开
		String[] returnPics = to.getReturnImg().split(",");
		for (String returnPic : returnPics) {
			OrdReturnPicMo ordReturnPicMo = new OrdReturnPicMo();
			ordReturnPicMo.setId(_idWorker.getId());
			ordReturnPicMo.setReturnId(returnCode);
			ordReturnPicMo.setPicPath(returnPic);
			int insertReturnPicResult = ordReturnPicSvc.add(ordReturnPicMo);
			if (insertReturnPicResult < 1) {
				_log.error("添加退货图片出错，返回值为：{}", insertReturnresult);
				throw new RuntimeException("添加退货图片失败");
			}
		}
		// =============================添加退货图片结束=============================

		// =============================修改订单详情状态和数量开始=============================
		orderDetailMo.setReturnState((byte) 1);
		orderDetailMo.setId(orderDetailId);
		orderDetailMo.setReturnCount(newReturnCount);
		int updateOrderDetailStateResult = ordOrderDetailSvc.modify(orderDetailMo);
		if (updateOrderDetailStateResult < 1) {
			_log.error("修改订单详情状态失败，返回值为：{}", updateOrderDetailStateResult);
			throw new RuntimeException("修改订单详情状态失败");
		}
		// =============================修改订单详情状态和数量结束=============================
		resultMap.put("result", 1);
		resultMap.put("msg", "提交成功");
		return resultMap;
	}

	/**
	 * 查询分页列表信息 Title: selectReturnPageList Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年4月21日 下午3:35:27
	 */
	@Override
	public PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnMo qo, int pageNum, int pageSize) {
		_log.info("selectReturnPageList: qo-{}; pageNum-{}; pageSize-{}", qo, pageNum, pageSize);
		return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> _mapper.selectReturnPageList(qo));
	}

	/**
	 * 拒绝退货 Title: rejectReturn Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年4月27日 上午9:42:37 1、判断退货编号、订单编号、订单详情ID、拒绝用户编号、拒绝原因等参数是否已传过来
	 *       2、查询根据退货编号和订单编号查询退货信息，判断该退货编号是否存在和查询退货订单的申请状态是否处于待审核状态
	 *       3、查询订单信息并判断订单状态是否处于已取消状态 4、根据订单编号和订单详情ID查询订单详情信息并判断该订单详情的状态是否处于已退货的状态
	 *       5、修改订单详情退货数量和状态 6、修改退货订单信息
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> rejectReturn(OrdReturnMo record) {
		_log.info("拒绝退货的请求参数为：{}", record.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 退货编号
		long returnCode = record.getReturnCode();
		// 订单编号
		long orderId = record.getOrderId();
		// 订单详情ID
		long orderDetailId = record.getOrderDetailId();
		// 拒绝退货操作人编号
		long rejectOpId = record.getRejectOpId();
		// 拒绝原因
		String rejectReason = record.getRejectReason();

		// ===================================拒绝退货第一步==============================
		if (returnCode == 0 || orderId == 0 || orderDetailId == 0 || rejectOpId == 0 || rejectReason.equals("")
				|| rejectReason == null || rejectReason.equals("null")) {
			_log.error("拒绝退货时出现参数不正确，退货编号为：{}", returnCode);
			resultMap.put("result", -1);
			resultMap.put("msg", "参数不正确");
			return resultMap;
		}

		// ===================================拒绝退货第二步==============================
		_log.info("拒绝退货查询退货信息的参数为：{}", record.toString());
		List<OrdReturnRo> returnList = _mapper.selectReturnPageList(record);
		_log.info("拒绝退货查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("拒绝退货查询退货信息时出现退货信息为空，退货编号为：{}", returnCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "退货信息不存在");
			return resultMap;
		}

		if (returnList.get(0).getApplicationState() != 1) {
			_log.error("拒绝退货时出现退货状态不处于待审核状态，退货编号为：{}", returnCode);
			resultMap.put("result", -3);
			resultMap.put("msg", "当前状态不允许审核");
			return resultMap;
		}

		// ===================================拒绝退货第三步==============================
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("拒绝退货查询订单信息的参数为：{}", orderMo.toString());
		// 查询订单信息
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("拒绝退货查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("拒绝退货查询订单信息时出现订单信息不存在，退货编号为：{}", returnCode);
			resultMap.put("result", -4);
			resultMap.put("msg", "该用户订单不存在");
			return resultMap;
		}

		if (orderList.get(0).getOrderState() == -1) {
			_log.error("拒绝退货时出现订单状态为取消状态，退货编号为：{}", returnCode);
			resultMap.put("result", -5);
			resultMap.put("msg", "该订单已取消，拒绝退货失败");
			return resultMap;
		}

		// ===================================拒绝退货第四步==============================
		_log.info("拒绝退货查询订单详情的参数为：{}", orderDetailId);
		OrdOrderDetailMo orderDetailMo = ordOrderDetailSvc.getById(orderDetailId);
		_log.info("拒绝退货查询订单详情的返回值为：{}", orderDetailMo.toString());
		// 订单详情Id
		Long detailId = orderDetailMo.getId();
		detailId = detailId == null ? 0 : detailId;
		if (orderDetailMo.getId() == 0) {
			_log.error("拒绝退货查询订单详情时出现订单详情不存在：退货编号为：{}", returnCode);
			resultMap.put("result", -6);
			resultMap.put("msg", "该用户 订单不存在");
			return resultMap;
		}

		if (orderDetailMo.getReturnState() != 1) {
			_log.error("拒绝退货时出现订单详情退货状态不处于退货中状态，退货编号为：{}", returnCode);
			resultMap.put("result", -7);
			resultMap.put("msg", "该退货订单已退货或该订单未退货");
			return resultMap;
		}

		// ===================================拒绝退货第五步==============================
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setId(orderDetailMo.getId());
		detailMo.setReturnCount(orderDetailMo.getReturnCount() - returnList.get(0).getReturnCount());
		detailMo.setReturnState((byte) 0);
		_log.info("拒绝退货修改订单详情信息的参数为：{}", detailMo.toString());
		// 修改订单详情信息
		int updateOrderDetailStateResult = ordOrderDetailSvc.modify(orderDetailMo);
		_log.info("拒绝退货修改订单详情信息的返回值为：{}", updateOrderDetailStateResult);
		if (updateOrderDetailStateResult < 1) {
			_log.error("拒绝退货修改订单详情信息出错，退货编号为{}", returnCode);
			throw new RuntimeException("修改订单详情信息出错");
		}

		Date date = new Date();

		record.setRejectTime(date);
		record.setFinishOpId(rejectOpId);
		record.setFinishTime(date);
		_log.info("拒绝退货的参数为：{}", record.toString());
		// 拒绝退货
		int refusedReturnResult = _mapper.refusedReturn(record);
		_log.info("拒绝退货的返回值为：{}", refusedReturnResult);
		if (refusedReturnResult != 1) {
			_log.error("拒绝退货时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("拒绝退货出错");
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "操作成功");
		return resultMap;
	}

	/**
	 * 同意退货 Title: agreeToReturn Description:
	 * 
	 * @return
	 * @date 2018年5月5日 下午3:26:49 1、判断参数是否齐全
	 */
	@SuppressWarnings("null")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> agreeToReturn(OrdOrderReturnTo mo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 退货编号
		Long returnCode = mo.getReturnCode();
		// 订单编号
		Long orderId = mo.getOrderCode();
		// 订单详情编号
		Long orderDetailId = mo.getOrderDetailId();
		// 同意退货操作人编号
		Long reviewOpId = mo.getOpId();
		BigDecimal bd = new BigDecimal("0");
		// 退货金额（余额）
		BigDecimal returnAmount1 = new BigDecimal(String.valueOf(mo.getReturnAmount1()));
		// 退货金额（返现金）
		BigDecimal returnAmount2 = new BigDecimal(String.valueOf(mo.getReturnAmount2()));
		// 扣减返现金额
		BigDecimal subtractCashback = new BigDecimal(String.valueOf(mo.getSubtractCashback()));
		// 退货总额 = 退货金额（余额） + 退货金额（返现金）
		BigDecimal totalReturn = new BigDecimal(returnAmount1.add(returnAmount2).doubleValue());

		if (returnCode == null || orderId == null || orderDetailId == null || reviewOpId == null
				|| subtractCashback.compareTo(bd) == -1) {
			_log.error("同意退货时出现参数为空的情况，同意退货失败");
			resultMap.put("result", -1);
			resultMap.put("msg", "参数不正确");
			return resultMap;
		}

		if (returnAmount1.compareTo(bd) == -1 && returnAmount2.compareTo(bd) == -1) {
			_log.error("同意退货时出现退到余额和返现金都为空，退货编号为：{}", returnCode);
			resultMap.put("result", -1);
			resultMap.put("msg", "参数不正确");
			return resultMap;
		}

		OrdReturnMo ordReturnMo = new OrdReturnMo();
		ordReturnMo.setReturnCode(returnCode);
		_log.info("同意退货查询退货信息的参数为：{}", returnCode);
		// 查询退货信息
		List<OrdReturnMo> returnList = _mapper.selectSelective(ordReturnMo);
		_log.info("同意退货查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("同意退货查询退货信息时出现找不到退货信息，退货编号为：{}", returnCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "退货信息不存在");
			return resultMap;
		}

		if (returnList.get(0).getApplicationState() != 1) {
			_log.error("同意退货时出现退货订单已审核，退货编号为：{}", returnCode);
			resultMap.put("result", -3);
			resultMap.put("msg", "该退货单已审核");
			return resultMap;
		}

		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("同意退货查询订单信息的参数为：{}", orderId);
		// 查询订单信息
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("同意退货查询订单信息的返回值为：{}", String.valueOf(orderList));

		if (orderList.size() == 0) {
			_log.error("同意退货查询订单信息时出现没有该订单，退货编号为：{}", returnCode);
			resultMap.put("result", -4);
			resultMap.put("msg", "没有找到该订单信息");
			return resultMap;
		}

		if (orderList.get(0).getOrderState() == -1 || orderList.get(0).getOrderState() == 1) {
			_log.error("同意退货时发现该订单未支付或已取消，退货编号为：{}", returnCode);
			resultMap.put("result", -5);
			resultMap.put("msg", "该订单未支付或已取消");
			return resultMap;
		}

		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(orderId);
		orderDetailMo.setId(orderDetailId);
		_log.info("同意退货查询订单详情信息的参数为：{}", orderDetailMo.toString());
		// 查询订单详情信息
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(orderDetailMo);
		_log.info("同意退货查询订单详情信息的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList.size() == 0) {
			_log.error("同意退货查询订单详情信息时发现没有找到该订单详情信息，退货编号为：{}", returnCode);
			resultMap.put("result", -6);
			resultMap.put("msg", "没有找到该订单详情信息");
			return resultMap;
		}

		if (orderDetailList.get(0).getReturnState() != 1) {
			_log.error("同意退货时发现该商品并未申请退货或已完成退货，退货编号为：{}", returnCode);
			resultMap.put("result", -7);
			resultMap.put("msg", "该商品未申请退货或已完成退货");
			return resultMap;
		}

		// 订单退货总额
		BigDecimal orderReturnTotal = orderList.get(0).getReturnTotal();
		orderReturnTotal = orderReturnTotal == null ? bd : orderReturnTotal;

		// 订单退货金额（余额）
		BigDecimal orderReturnAmount1 = orderList.get(0).getReturnAmount1();
		orderReturnAmount1 = orderReturnAmount1 == null ? bd : orderReturnAmount1;

		// 订单退货金额（返现金）
		BigDecimal orderReturnAmount2 = orderList.get(0).getReturnAmount2();
		orderReturnAmount2 = orderReturnAmount2 == null ? bd : orderReturnAmount2;

		orderMo.setReturnTotal(new BigDecimal(orderReturnTotal.add(totalReturn).doubleValue()));
		orderMo.setReturnAmount1(new BigDecimal(orderReturnAmount1.add(returnAmount1).doubleValue()));
		orderMo.setReturnAmount2(new BigDecimal(orderReturnAmount2.add(returnAmount2).doubleValue()));
		_log.info("同意退货修改订单退货金额的参数为{}", orderMo.toString());
		// 修改订单退货金额
		int modifyReturnAmountResult = ordOrderSvc.modifyReturnAmountByorderCode(orderMo);
		_log.info("同意退货修改订单退货金额的返回值为：{}", modifyReturnAmountResult);
		if (modifyReturnAmountResult != 1) {
			_log.error("同意退货修改订单退货金额时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改订单退货金额错误");
		}

		// 订单详情退货数量
		Integer orderDetailReturnCount = orderDetailList.get(0).getReturnCount();
		orderDetailReturnCount = orderDetailReturnCount == null ? 0 : orderDetailReturnCount;

		// 返现总额
		BigDecimal cashBackTotal = orderDetailList.get(0).getCashbackTotal();
		cashBackTotal = cashBackTotal == null ? bd : cashBackTotal;

		Integer returnCount = returnList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		// 新退货数量
		Integer newReturnCount =  + orderDetailReturnCount;

		// 新返现总额
		BigDecimal newCashBackTotal = new BigDecimal(cashBackTotal.subtract(totalReturn).doubleValue());
		orderDetailMo.setReturnCount(newReturnCount);
		orderDetailMo.setCashbackTotal(newCashBackTotal);
		_log.info("同意退货修改订单详情退货数量和返现总额的参数为：{}", orderDetailMo.toString());
		// 修改订单详情退货数量和返现总额
		int modifyReturnCountAndCashBackTotalResult = ordOrderDetailSvc
				.modifyReturnCountAndCashBackTotal(orderDetailMo);
		_log.info("同意退货修改订单详情退货数量和返现总额的返回值为：{}", modifyReturnCountAndCashBackTotalResult);
		if (modifyReturnCountAndCashBackTotalResult != 1) {
			_log.error("同意退货修改订单详情退货数量和返现总额时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改退货数量和返现总额出错");
		}

		Date date = new Date();
		ordReturnMo.setReviewOpId(reviewOpId);
		ordReturnMo.setReviewTime(date);
		ordReturnMo.setReturnRental(totalReturn);
		ordReturnMo.setReturnAmount1(returnAmount1);
		ordReturnMo.setReturnAmount2(returnAmount2);
		ordReturnMo.setSubtractCashback(subtractCashback);
		_log.info("同意退货修改退货信息的参数为：{}", ordReturnMo.toString());
		// 修改退货信息
		int returnApproveResult = _mapper.returnApprove(ordReturnMo);
		_log.info("同意退货修改退货信息的返回值为：{}", returnApproveResult);
		if (returnApproveResult != 1) {
			_log.error("同意退货修改退货信息时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改退货信息错误");
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "审核成功");
		return resultMap;
	}

	/**
	 * 同意退款 Title: agreeToARefund Description:
	 * 
	 * @return
	 * @date 2018年5月5日 下午3:41:42
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> agreeToARefund(OrdOrderReturnTo to) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 退货编号
		Long returnCode = to.getReturnCode();
		// 订单编号
		Long orderId = to.getOrderCode();
		// 订单详情编号
		Long orderDetailId = to.getOrderDetailId();
		BigDecimal bd = new BigDecimal("0");
		// 退货金额（余额）
		BigDecimal returnAmount1 = new BigDecimal(to.getReturnAmount1());
		// 退货金额（返现金）
		BigDecimal returnAmount2 = new BigDecimal(to.getReturnAmount2());
		// 扣减返现金额
		BigDecimal subtractCashback = new BigDecimal(to.getSubtractCashback());
		// 退货操作人
		Long refundOpId = to.getOpId();

		if (returnCode == null || orderId == null || orderDetailId == null || refundOpId == null
				|| subtractCashback == null) {
			_log.error("同意退款时出现参数不全");
			resultMap.put("result", -1);
			resultMap.put("msg", "参数不正确");
			return resultMap;
		}

		// 退货总额
		BigDecimal returnRental = new BigDecimal(returnAmount1.add(returnAmount2).doubleValue());
		if (returnRental.compareTo(bd) == -1 || returnRental.compareTo(bd) == 0) {
			_log.error("同意退款时出现退款金额为空，退货编号为：{}", returnCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "退款金额不能为空");
			return resultMap;
		}

		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("同意退款查询订单信息的参数为：{}", orderId);
		// 查询订单信息
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("同意退款查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("同意退款查询订单时发现没有该订单，退货编号为：{}", returnCode);
			resultMap.put("result", -3);
			resultMap.put("msg", "没有发现该订单");
			return resultMap;
		}

		if (orderList.get(0).getOrderState() == -1 || orderList.get(0).getOrderState() == 1) {
			_log.error("同意退款时发现订单未支付或已取消，退货编号为：{}", returnCode);
			resultMap.put("result", -4);
			resultMap.put("msg", "该订单未支付或已取消");
			return resultMap;
		}

		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setOrderId(orderId);
		detailMo.setId(orderDetailId);
		_log.info("同意退款查询订单详情的参数为：{}", detailMo.toString());
		// 查询订单详情信息
		List<OrdOrderDetailMo> detailList = ordOrderDetailSvc.list(detailMo);
		_log.info("同意退款查询订单详情的返回值为：{}", String.valueOf(detailList));
		if (detailList.size() == 0) {
			_log.error("同意退货查询订单详情时发现没有该详情信息，退货编号为：{}", returnCode);
			resultMap.put("result", -5);
			resultMap.put("msg", "用户未购买该商品");
			return resultMap;
		}

		if (detailList.get(0).getReturnState() != 1) {
			_log.error("同意退款时发现用户未申请退款，退货编号为：{}", returnCode);
			resultMap.put("result", -6);
			resultMap.put("msg", "用户未申请退款");
			return resultMap;
		}

		OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setReturnCode(returnCode);
		_log.info("同意退款查询退货信息的参数为：{}", returnCode);
		// 查询退货信息
		List<OrdReturnMo> returnList = _mapper.selectSelective(returnMo);
		_log.info("同意退款查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("同意退款时发现没有该退款信息，退货编号为：{}", returnCode);
			resultMap.put("result", -7);
			resultMap.put("msg", "没有找到该退款信息");
			return resultMap;
		}

		if (returnList.get(0).getApplicationState() != 1) {
			_log.info("同意退款时发现该退货单不处于待审核状态，退货编号为：{}", returnCode);
			resultMap.put("result", -8);
			resultMap.put("msg", "该订单已退款或已取消申请");
			return resultMap;
		}

		if (returnList.get(0).getRefundState() == 2) {
			_log.error("同意退款时发现该退货单已退款，退货编号为：{}", returnCode);
			resultMap.put("result", -9);
			resultMap.put("msg", "该退款单已退款");
			return resultMap;
		}

		// 订单退货总额
		BigDecimal returnTotal = orderList.get(0).getReturnTotal();
		if (returnTotal == null) {
			returnTotal = new BigDecimal("0");
		}
		BigDecimal newOrderReturnTotal = new BigDecimal(returnTotal.add(returnRental).doubleValue());
		if (orderList.get(0).getRealMoney().compareTo(newOrderReturnTotal) == 0) {
			orderMo.setOrderState((byte) -1);
		}
		orderMo.setReturnAmount1(returnAmount1);
		orderMo.setReturnAmount2(returnAmount2);
		orderMo.setReturnTotal(newOrderReturnTotal);
		_log.info("同意退款修改订单退货金额的参数为：{}", orderMo.toString());
		// 修改订单退货金额
		int modifyReturnAmountResult = ordOrderSvc.modifyReturnAmountByorderCode(orderMo);
		_log.info("同意退款修改订单退货金额的返回值为：{}", modifyReturnAmountResult);
		if (modifyReturnAmountResult != 1) {
			_log.error("同意退款修改订单退货金额时出错，退货编号为：{}", returnCode);
			throw new RuntimeException("修改订单退货金额出错");
		}

		// 已退货数量
		Integer returnCount = detailList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;

		// 新的退货数量
		int newReturnCount = returnCount + to.getReturnNum();
		if (newReturnCount == detailList.get(0).getBuyCount()) {
			detailMo.setReturnState((byte) 2);
		} else {
			detailMo.setReturnState((byte) 3);
		}

		detailMo.setReturnCount(newReturnCount);
		BigDecimal cashbackTotal = detailList.get(0).getCashbackTotal();
		cashbackTotal = cashbackTotal == null ? bd : cashbackTotal;
		// 新返现总额
		BigDecimal newCashBackTotal = new BigDecimal(cashbackTotal.subtract(subtractCashback).doubleValue());
		detailMo.setCashbackTotal(newCashBackTotal);
		_log.info("同意退款修改订单详情信息的参数为：{}", detailMo.toString());
		// 修改订单详情退货数量和返现总额
		int	modifyReturnCountAndCashBackTotalResult = ordOrderDetailSvc.modifyReturnCountAndCashBackTotal(detailMo);
		_log.info("同意退款修改订单详情信息的返回值为：{}", modifyReturnCountAndCashBackTotalResult);
		if (modifyReturnCountAndCashBackTotalResult != 1) {
			throw new RuntimeException("修改订单详情出错");
		}

		Date date = new Date();

		returnMo.setReturnAmount1(returnAmount1);
		returnMo.setReturnAmount2(returnAmount2);
		returnMo.setReturnRental(returnRental);
		returnMo.setSubtractCashback(subtractCashback);
		returnMo.setRefundOpId(refundOpId);
		returnMo.setRefundState((byte) 2);
		returnMo.setRefundTime(date);
		returnMo.setFinishOpId(refundOpId);
		returnMo.setFinishTime(date);
		_log.info("同意退款修改退货信息的参数为：{}", returnMo.toString());
		// 修改退货信息
		int confirmTheRefundResult = _mapper.confirmTheRefund(returnMo);
		_log.info("同意退款修改退货信息的返回值为：{}", confirmTheRefundResult);
		if (confirmTheRefundResult != 1) {
			_log.error("同意退款修改退货信息时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改退货信息出错");
		}
		ReturnGoodsByBuyerTo goodsByBuyerTo = new ReturnGoodsByBuyerTo();
		goodsByBuyerTo.setUserId(returnList.get(0).getApplicationOpId());
		goodsByBuyerTo.setReturnGoodsOrderId(String.valueOf(returnCode));
		goodsByBuyerTo.setSaleOrderId(String.valueOf(orderId));
		goodsByBuyerTo.setTradeTitle("大卖网络商品退货");
		goodsByBuyerTo.setTradeDetail(detailList.get(0).getOnlineTitle() + "-" + detailList.get(0).getSpecName());
		goodsByBuyerTo.setBalanceAmount(returnAmount1.doubleValue());
		goodsByBuyerTo.setCashbackAmount(returnAmount2.doubleValue());
		goodsByBuyerTo.setOpId(refundOpId);
		goodsByBuyerTo.setIp(to.getIp());
		goodsByBuyerTo.setMac(to.getMac());
		_log.info("同意退款执行退款的参数为：{}", goodsByBuyerTo.toString());
		/*
		 * // 退款 ReturnGoodsByBuyerRo returnGoodsByBuyerResult =
		 * afcReturnGoodsSvr.returnGoodsByBuyer(goodsByBuyerTo);
		 * _log.info("同意退款执行退款的返回值为：{}", returnGoodsByBuyerResult); int result =
		 * returnGoodsByBuyerResult.getResult().getCode(); if (result != 1) {
		 * _log.error("同意退款退款时出现出错，退货编号为：{}", returnCode); throw new
		 * RuntimeException("v支付出错，退款失败"); }
		 */
		resultMap.put("result", 1);
		resultMap.put("msg", "退款成功");
		return resultMap;
	}

	/**
	 * 已收到货并退款 Title: receivedAndRefunded Description:
	 * 
	 * @return
	 * @date 2018年5月5日 下午3:43:00
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> receivedAndRefunded(OrdOrderReturnTo to) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 退货编号
		Long returnCode = to.getReturnCode();
		returnCode = returnCode == null ? 0L : returnCode;
		// 操作人编号
		Long opId = to.getOpId();
		opId = opId == null ? 0L : opId;
		// ip地址
		String ip = to.getIp();
		// mac地址
		String mac = to.getMac();
		if (returnCode == 0 || opId == 0 || ip == null || ip.equals("") || ip.equals("null") || mac == null
				|| mac.equals("") || mac.equals("null")) {
			_log.error("已收到货并退款时发现参数不全");
			resultMap.put("result", -1);
			resultMap.put("msg", "参数不正确");
			return resultMap;
		}

		OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setReturnCode(returnCode);
		_log.info("已收到货并退款查询退货信息的参数为：{}", returnCode);
		// 查询退货信息
		List<OrdReturnMo> returnList = _mapper.selectSelective(returnMo);
		_log.info("已收到货并退款查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("已收到货并退款时发现退货信息为空，退货编号为：{}", returnCode);
			resultMap.put("result", -2);
			resultMap.put("msg", "没有找到该退货信息");
			return resultMap;
		}

		if (returnList.get(0).getApplicationState() != 2) {
			_log.error("已收到货并退款时发现退货状态不处于退货中，退货编号为：{}", returnCode);
			resultMap.put("result", -3);
			resultMap.put("msg", "当前状态不允许退款");
			return resultMap;
		}

		// 订单编号
		long orderId = returnList.get(0).getOrderId();
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("已收到货并退款查询订单信息的参数为：{}", orderId);
		// 查询订单信息
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("已收到货并退款查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("已收到货并退款查询订单信息时发现没有该订单，退货编号为：{}", returnCode);
			resultMap.put("result", -4);
			resultMap.put("msg", "没有找到该订单信息");
			return resultMap;
		}

		if (orderList.get(0).getOrderState() == -1 || orderList.get(0).getOrderState() == 1) {
			_log.error("已收到货并退款时发现订单处于取消或待支付状态，退货编号为：{}", returnCode);
			resultMap.put("result", -5);
			resultMap.put("msg", "该订单已取消或未支付");
			return resultMap;
		}

		// 订单详情ID
		long orderDetailId = returnList.get(0).getOrderDetailId();
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setId(orderDetailId);
		detailMo.setOrderId(orderId);
		_log.info("已收到货并退款查询订单详情信息的参数为：{}", detailMo.toString());
		// 查询订单详情信息
		List<OrdOrderDetailMo> detailList = ordOrderDetailSvc.list(detailMo);
		_log.info("已收到货并退款查询订单详情信息的返回值为：{}", String.valueOf(detailList));
		if (detailList.size() == 0) {
			_log.error("已收到货并退款查询订单详情信息时发现订单详情信息为空，退货编号为：{}", returnCode);
			resultMap.put("result", -6);
			resultMap.put("msg", "该商品未购买");
			return resultMap;
		}

		// 退货状态
		byte returnState = detailList.get(0).getReturnState();

		if (returnState != 1) {
			_log.error("已收到货并退款查询订单详情时发现退货状态不处于退货中，退货编号为：{}", returnCode);
			resultMap.put("result", -7);
			resultMap.put("msg", "该商品未申请或已完成退货");
			return resultMap;
		}

		if (orderList.get(0).getReturnTotal().compareTo(orderList.get(0).getRealMoney()) == 0) {
			_log.info("已收到货并退款修改订单状态的参数为：{}", orderId);
			// 修改订单状态为取消状态
			int modifyOrderStateResult = ordOrderSvc.modifyOrderStateByOderCode(orderId, (byte) -1);
			_log.info("已收到货并退款修改订单状态的返回值为：{}", modifyOrderStateResult);
			if (modifyOrderStateResult != 1) {
				_log.error("已收到货并退款修改订单状态出错，退货编号为：{}", returnCode);
				throw new RuntimeException("修改订单状态出错");
			}
		}

		if (detailList.get(0).getReturnCount() == detailList.get(0).getBuyCount()) {
			detailMo.setReturnState((byte) 2);
			
		} else {
			detailMo.setReturnState((byte) 3);
		}
		// 订单详情ID
		long detailId = detailList.get(0).getId();
		_log.info("已收到货并退款修改订单详情退货状态的参数为：{}，{}", detailId, detailMo.getReturnState());
		// 根据订单详情ID修改退货状态
		int modifyReturnStateResult = ordOrderDetailSvc.modifyReturnStateById(detailId, detailMo.getReturnState());
		_log.info("已收到货并退款修改订单详情退货状态的返回值为：{}", modifyReturnStateResult);
		if (modifyReturnStateResult != 1) {
			_log.error("已收到货并退款修改订单详情退货状态时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改订单详情退货状态出错");
		}
		Date date = new Date();
		returnMo.setRefundOpId(opId);
		returnMo.setRefundTime(date);
		returnMo.setReceiveOpId(opId);
		returnMo.setReceiveTime(date);
		returnMo.setRefundState((byte) 2);
		_log.info("已收到货并退款确认收到货的参数为：{}", returnMo.toString());
		// 确认收到货
		int confirmReceiptOfGoodsResult = _mapper.confirmReceiptOfGoods(returnMo);
		_log.info("已收到货并退款确认收到货的返回值为：{}", confirmReceiptOfGoodsResult);
		if (confirmReceiptOfGoodsResult != 1) {
			_log.error("已收到货并退款确认收到货时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("确认收到货出错");
		}

		ReturnGoodsByBuyerTo buyerTo = new ReturnGoodsByBuyerTo();
		buyerTo.setUserId(returnList.get(0).getApplicationOpId());
		buyerTo.setOpId(opId);
		buyerTo.setSaleOrderId(String.valueOf(returnList.get(0).getOrderId()));
		buyerTo.setReturnGoodsOrderId(returnCode.toString());
		buyerTo.setTradeTitle("大卖网络-用户退货退款");
		buyerTo.setTradeDetail(orderList.get(0).getOrderTitle());
		buyerTo.setBalanceAmount(returnList.get(0).getReturnAmount1().doubleValue());
		buyerTo.setCashbackAmount(returnList.get(0).getReturnAmount2().doubleValue());
		buyerTo.setSubtractCashback(returnList.get(0).getSubtractCashback().doubleValue());
		buyerTo.setMac(mac);
		buyerTo.setIp(ip);
		_log.info("已收到货并退款退款并扣减返现金额的参数为：{}", buyerTo.toString());
		// 退款并扣减返现金额
		/*
		 * resultMap = afcReturnGoodsSvr.returnRefundAndSubtractCashback(buyerTo);
		 * _log.info("已收到货并退款退款并扣减返现金额的返回值为：{}", resultMap.toString()); if
		 * (!resultMap.get("result").equals("1")) {
		 * _log.error("已收到货并退款退款并扣减返现金额时出错，退货编号为：{}", returnCode); throw new
		 * RuntimeException("v支付出错，退款失败"); }
		 */
		resultMap.put("result", 1);
		resultMap.put("msg", "退款成功");
		_log.info("已收到货并退款退款并扣减返现金额的返回值为：{}", resultMap.toString());
		return resultMap;
	}
}
