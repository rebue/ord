package rebue.ord.svc.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.afc.ro.RefundRo;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.to.RefundTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.ord.dic.AddReturnDic;
import rebue.ord.dic.AgreeToARefundDic;
import rebue.ord.dic.AgreeToReturnDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReceivedAndRefundedDic;
import rebue.ord.dic.RejectReturnDic;
import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToARefundRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 用户退货信息
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
public class OrdReturnSvcImpl extends MybatisBaseSvcImpl<OrdReturnMo, java.lang.Long, OrdReturnMapper>
		implements OrdReturnSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(OrdReturnMo mo) {
		_log.info("添加用户退货信息");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	/**
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdReturnSvcImpl.class);

	/**
	 */
	@Resource
	private OrdReturnSvc ordReturnSvc;

	/**
	 */
	@Resource
	private OrdReturnPicSvc ordReturnPicSvc;

	/**
	 */
	@Resource
	private OrdOrderSvc ordOrderSvc;

	/**
	 */
	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	/**
	 */
	@Resource
	private AfcRefundSvc afcRefundSvc;

	@Resource
	private Mapper dozerMapper;

	@Resource
	private OnlOnlinePicSvc onlOnlinePicSvc;

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
	public AddReturnRo addReturn(OrdOrderReturnTo to) {
		_log.info("添加用户退货信息的参数为：{}", to.toString());
		AddReturnRo addReturnRo = new AddReturnRo();
		long orderId = to.getOrderId();
		long orderCode = to.getOrderCode();
		long orderDetailId = to.getOrderDetailId();
		long userId = to.getUserId();
		int returnNum = to.getReturnNum();
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderCode));
		orderMo.setUserId(userId);
		_log.info("查询订单信息的参数为：{}", orderMo);
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		if (orderList.size() == 0) {
			_log.error("添加退货信息出现订单不存在，订单编号为：{}", orderCode);
			addReturnRo.setResult(AddReturnDic.ORDER_NOT_EXIST);
			addReturnRo.setMsg("订单不存在");
			return addReturnRo;
		}
		byte orderState = orderList.get(0).getOrderState();
		if (orderState == OrderStateDic.CANCEL.getCode()) {
			_log.error("添加退货信息出现订单已取消，订单编号为：{}", orderCode);
			addReturnRo.setResult(AddReturnDic.ORDER_ALREADY_CANCEL);
			addReturnRo.setMsg("订单已取消");
			return addReturnRo;
		}
		if (orderState == OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode()) {
			_log.error("添加退货信息出现订单未支付，订单编号为：{}", orderCode);
			addReturnRo.setResult(AddReturnDic.ORDER_NOT_PAY);
			addReturnRo.setMsg("订单未支付");
			return addReturnRo;
		}
		if (orderList.get(0).getRealMoney() == orderList.get(0).getReturnTotal()) {
			_log.error("添加退货信息出现订单已全部退完，订单编号为：{}", orderCode);
			addReturnRo.setResult(AddReturnDic.ORDER_ALREADY_RETURN_FINISH);
			addReturnRo.setMsg("该订单已全部退完");
			return addReturnRo;
		}
		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(orderId);
		orderDetailMo.setSpecName(to.getSpecName());
		_log.info("查询订单详情的参数为：{}", orderDetailMo);
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
			addReturnRo.setResult(AddReturnDic.ORDER_NOT_EXIST);
			addReturnRo.setMsg("订单不存在");
			return addReturnRo;
		}
		if (orderDetailList.get(0).getReturnState() != 0) {
			_log.error("添加退货信息出现订单详情退货状态不处于未退货状态，订单详情编号为：{}", orderDetailId);
			addReturnRo.setResult(AddReturnDic.CURRENT_STATE_NOT_EXIST_RETURN);
			addReturnRo.setMsg("当前状态不允许退货");
			return addReturnRo;
		}
		Integer returnCount = orderDetailList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		int buyCount = orderDetailList.get(0).getBuyCount();
		if (buyCount == returnCount) {
			_log.error("添加退货信息出现订单详情退货数量等于购买数量，订单详情编号为：{}", orderDetailId);
			addReturnRo.setResult(AddReturnDic.GOODS_ALREADY_RETURN_FINISH);
			addReturnRo.setMsg("该商品已退完");
			return addReturnRo;
		}
		int newReturnCount = returnCount + returnNum;
		if (buyCount < newReturnCount) {
			_log.error("添加退货信息出现订单详情退货数量大于订单购买数量，订单详情编号为：{}", orderDetailId);
			addReturnRo.setResult(AddReturnDic.NOT_RETURN_COUNT_GR_BUY_COUNT);
			addReturnRo.setMsg("退货数量不能大于购买数量");
			return addReturnRo;
		}
		Date date = new Date();
		long returnCode = _idWorker.getId();
		OrdReturnMo ordReturnMo = new OrdReturnMo();
		ordReturnMo.setId(_idWorker.getId());
		ordReturnMo.setReturnCode(returnCode);
		ordReturnMo.setOrderId(orderId);
		ordReturnMo.setOrderDetailId(orderDetailId);
		ordReturnMo.setReturnCount(returnNum);
		ordReturnMo.setReturnRental(to.getReturnPrice());
		ordReturnMo.setReturnType(to.getReturnType());
		ordReturnMo.setApplicationState((byte) 1);
		ordReturnMo.setRefundState((byte) 1);
		ordReturnMo.setReturnReason(to.getReturnReason());
		ordReturnMo.setApplicationOpId(userId);
		ordReturnMo.setApplicationTime(date);
		ordReturnMo.setUserId(userId);
		_log.info("添加退货信息的参数为：{}", ordReturnMo);
		int insertReturnresult = _mapper.insertSelective(ordReturnMo);
		_log.info("添加退货信息的返回值为：{}", insertReturnresult);
		if (insertReturnresult < 1) {
			_log.error("添加退货信息出错，返回值为：{}", insertReturnresult);
			addReturnRo.setResult(AddReturnDic.ADD_RETURN_ERROR);
			addReturnRo.setMsg("添加退货信息出错");
			return addReturnRo;
		}
		String[] returnPics = to.getReturnImg().split(",");
		for (String returnPic : returnPics) {
			OrdReturnPicMo ordReturnPicMo = new OrdReturnPicMo();
			ordReturnPicMo.setId(_idWorker.getId());
			ordReturnPicMo.setReturnId(ordReturnMo.getId());
			ordReturnPicMo.setPicPath(returnPic);
			int insertReturnPicResult = ordReturnPicSvc.add(ordReturnPicMo);
			_log.error("添加退货图片返回值：{}" + insertReturnPicResult);
			if (insertReturnPicResult < 1) {
				_log.error("添加退货图片出错，返回值为：{}", insertReturnresult);
				throw new RuntimeException("添加退货图片失败");
			}
		}
		orderDetailMo.setReturnState((byte) 1);
		orderDetailMo.setId(orderDetailId);
		orderDetailMo.setReturnCount(newReturnCount);
		int updateOrderDetailStateResult = ordOrderDetailSvc.modify(orderDetailMo);
		if (updateOrderDetailStateResult < 1) {
			_log.error("修改订单详情状态失败，返回值为：{}", updateOrderDetailStateResult);
			throw new RuntimeException("修改订单详情状态失败");
		}
		addReturnRo.setResult(AddReturnDic.SUCCESS);
		addReturnRo.setMsg("提交成功");
		return addReturnRo;
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
	 * @date 2018年4月27日上午9:42:371、判断退货编号、订单编号、订单详情ID、拒绝用户编号、拒绝原因等参数是否已传过来2、
	 *       查询根据退货编号和订单编号查询退货信息 ，判断该退货编号是否存在和查询退货订单的申请状态是否处于待审核状态
	 *       3、查询订单信息并判断订单状态是否处于已取消状态 4、根据订单编号和订单详情ID查询订单详情信息并判断该订单详情的状态是否处于已退货的状态
	 *       5、修改订单详情退货数量和状态 6、修改退货订单信息
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RejectReturnRo rejectReturn(OrdReturnMo record) {
		_log.info("拒绝退货的请求参数为：{}", record);
		RejectReturnRo rejectReturnRo = new RejectReturnRo();
		long returnCode = record.getReturnCode();
		long orderId = record.getOrderId();
		long orderDetailId = record.getOrderDetailId();
		long rejectOpId = record.getRejectOpId();
		String rejectReason = record.getRejectReason();
		if (returnCode == 0 || orderId == 0 || orderDetailId == 0 || rejectOpId == 0 || rejectReason.equals("")
				|| rejectReason == null || rejectReason.equals("null")) {
			_log.error("拒绝退货时出现参数不正确，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.PARAM_NOT_CORRECT);
			rejectReturnRo.setMsg("参数不正确");
			return rejectReturnRo;
		}
		_log.info("拒绝退货查询退货信息的参数为：{}", record);
		List<OrdReturnRo> returnList = _mapper.selectReturnPageList(record);
		_log.info("拒绝退货查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("拒绝退货查询退货信息时出现退货信息为空，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.RETURN_NOT_EXIST);
			rejectReturnRo.setMsg("退货信息不存在");
			return rejectReturnRo;
		}
		if (returnList.get(0).getApplicationState() != 1) {
			_log.error("拒绝退货时出现退货状态不处于待审核状态，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.CURRENT_STATE_NOT_EXIST_REJECT);
			rejectReturnRo.setMsg("当前状态不允许拒绝");
			return rejectReturnRo;
		}
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("拒绝退货查询订单信息的参数为：{}", orderMo);
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("拒绝退货查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("拒绝退货查询订单信息时出现订单信息不存在，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.ORDER_NOT_EXIST);
			rejectReturnRo.setMsg("订单不存在");
			return rejectReturnRo;
		}
		if (orderList.get(0).getOrderState() == OrderStateDic.CANCEL.getCode()) {
			_log.error("拒绝退货时出现订单状态为取消状态，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.ORDER_ALREADY_CANCEL);
			rejectReturnRo.setMsg("该订单已取消，拒绝退货失败");
			return rejectReturnRo;
		}
		_log.info("拒绝退货查询订单详情的参数为：{}", orderDetailId);
		OrdOrderDetailMo orderDetailMo = ordOrderDetailSvc.getById(orderDetailId);
		_log.info("拒绝退货查询订单详情的返回值为：{}", orderDetailMo);
		Long detailId = orderDetailMo.getId();
		detailId = detailId == null ? 0 : detailId;
		if (orderDetailMo == null) {
			_log.error("拒绝退货查询订单详情时出现订单详情不存在：退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.ORDER_NOT_EXIST);
			rejectReturnRo.setMsg("订单不存在");
			return rejectReturnRo;
		}
		if (orderDetailMo.getReturnState() != 1) {
			_log.error("拒绝退货时出现订单详情退货状态不处于退货中状态，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.ORDER_ALREADY_RETURN_OR_NOT_RETURN);
			rejectReturnRo.setMsg("该退货订单已退货或该订单未退货");
			return rejectReturnRo;
		}
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setId(orderDetailMo.getId());
		detailMo.setReturnCount(orderDetailMo.getReturnCount() - returnList.get(0).getReturnCount());
		detailMo.setReturnState((byte) 0);
		_log.info("拒绝退货修改订单详情信息的参数为：{}", detailMo);
		int updateOrderDetailStateResult = ordOrderDetailSvc.modify(detailMo);
		_log.info("拒绝退货修改订单详情信息的返回值为：{}", updateOrderDetailStateResult);
		if (updateOrderDetailStateResult < 1) {
			_log.error("拒绝退货修改订单详情信息出错，退货编号为{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.MODIFY_ORDER_DETAIL_ERROR);
			rejectReturnRo.setMsg("修改订单详情信息出错");
			return rejectReturnRo;
		}
		Date date = new Date();
		record.setRejectTime(date);
		record.setFinishOpId(rejectOpId);
		record.setFinishTime(date);
		_log.info("拒绝退货的参数为：{}", record);
		int refusedReturnResult = _mapper.refusedReturn(record);
		_log.info("拒绝退货的返回值为：{}", refusedReturnResult);
		if (refusedReturnResult != 1) {
			_log.error("拒绝退货时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("拒绝退货出错");
		}
		rejectReturnRo.setResult(RejectReturnDic.SUCCESS);
		rejectReturnRo.setMsg("操作成功");
		return rejectReturnRo;
	}

	/**
	 * 同意退货 Title: agreeToReturn Description:
	 *
	 * @return
	 * @date 2018年5月5日 下午3:26:49 1、判断参数是否齐全
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public AgreeToReturnRo agreeToReturn(OrdOrderReturnTo mo) {
		AgreeToReturnRo agreeToReturnRo = new AgreeToReturnRo();
		Long returnCode = mo.getReturnCode();
		Long orderId = mo.getOrderCode();
		Long orderDetailId = mo.getOrderDetailId();
		Long reviewOpId = mo.getOpId();
		BigDecimal bd = new BigDecimal("0");
		BigDecimal returnAmount1 = new BigDecimal(String.valueOf(mo.getReturnAmount1()));
		BigDecimal returnAmount2 = new BigDecimal(String.valueOf(mo.getReturnAmount2()));
		BigDecimal subtractCashback = new BigDecimal(String.valueOf(mo.getSubtractCashback()));
		BigDecimal totalReturn = new BigDecimal(returnAmount1.add(returnAmount2).doubleValue());
		if (returnCode == null || orderId == null || orderDetailId == null || reviewOpId == null
				|| subtractCashback.compareTo(bd) == -1) {
			_log.error("同意退货时出现参数为空的情况，同意退货失败");
			agreeToReturnRo.setResult(AgreeToReturnDic.PARAM_NOT_CORRECT);
			agreeToReturnRo.setMsg("参数不正确");
			return agreeToReturnRo;
		}
		if (returnAmount1.compareTo(bd) == -1 && returnAmount2.compareTo(bd) == -1) {
			_log.error("同意退货时出现退到余额和返现金都为空，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.PARAM_NOT_CORRECT);
			agreeToReturnRo.setMsg("参数不正确");
			return agreeToReturnRo;
		}
		OrdReturnMo ordReturnMo = new OrdReturnMo();
		ordReturnMo.setReturnCode(returnCode);
		_log.info("同意退货查询退货信息的参数为：{}", returnCode);
		List<OrdReturnMo> returnList = _mapper.selectSelective(ordReturnMo);
		_log.info("同意退货查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("同意退货查询退货信息时出现找不到退货信息，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.RETURN_NOT_EXIST);
			agreeToReturnRo.setMsg("退货信息不存在");
			return agreeToReturnRo;
		}
		if (returnList.get(0).getApplicationState() != 1) {
			_log.error("同意退货时出现退货订单已审核，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.RETURN_ALREADY_APPROVE);
			agreeToReturnRo.setMsg("该退货单已审核");
			return agreeToReturnRo;
		}
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("同意退货查询订单信息的参数为：{}", orderId);
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("同意退货查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("同意退货查询订单信息时出现没有该订单，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_NOT_EXIST);
			agreeToReturnRo.setMsg("没有找到该订单信息");
			return agreeToReturnRo;
		}
		if (orderList.get(0).getOrderState() == OrderStateDic.CANCEL.getCode()
				|| orderList.get(0).getOrderState() == OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode()) {
			_log.error("同意退货时发现该订单未支付或已取消，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_NOT_PAY_OR_ALREADY_CANCEL);
			agreeToReturnRo.setMsg("该订单未支付或已取消");
			return agreeToReturnRo;
		}
		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(orderId);
		orderDetailMo.setId(orderDetailId);
		_log.info("同意退货查询订单详情信息的参数为：{}", orderDetailMo.toString());
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(orderDetailMo);
		_log.info("同意退货查询订单详情信息的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList.size() == 0) {
			_log.error("同意退货查询订单详情信息时发现没有找到该订单详情信息，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_DETAIL_NOT_NULL);
			agreeToReturnRo.setMsg("没有找到该退货商品信息");
			return agreeToReturnRo;
		}
		if (orderDetailList.get(0).getReturnState() != 1) {
			_log.error("同意退货时发现该商品并未申请退货或已完成退货，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.GOODS_NOT_APPLYFOR_RETURN_OR_ALREADY_FINISH);
			agreeToReturnRo.setMsg("该商品未申请退货或已完成退货");
			return agreeToReturnRo;
		}
		BigDecimal orderReturnTotal = orderList.get(0).getReturnTotal();
		orderReturnTotal = orderReturnTotal == null ? bd : orderReturnTotal;
		BigDecimal orderReturnAmount1 = orderList.get(0).getReturnAmount1();
		orderReturnAmount1 = orderReturnAmount1 == null ? bd : orderReturnAmount1;
		BigDecimal orderReturnAmount2 = orderList.get(0).getReturnAmount2();
		orderReturnAmount2 = orderReturnAmount2 == null ? bd : orderReturnAmount2;
		orderMo.setReturnTotal(new BigDecimal(orderReturnTotal.add(totalReturn).doubleValue()));
		orderMo.setReturnAmount1(new BigDecimal(orderReturnAmount1.add(returnAmount1).doubleValue()));
		orderMo.setReturnAmount2(new BigDecimal(orderReturnAmount2.add(returnAmount2).doubleValue()));
		_log.info("同意退货修改订单退货金额的参数为{}", orderMo.toString());
		int modifyReturnAmountResult = ordOrderSvc.modifyReturnAmountByorderCode(orderMo);
		_log.info("同意退货修改订单退货金额的返回值为：{}", modifyReturnAmountResult);
		if (modifyReturnAmountResult != 1) {
			_log.error("同意退货修改订单退货金额时出现错误，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.MODIFY_ORDER_RETURN_AMOUNT_ERROR);
			agreeToReturnRo.setMsg("修改订单退货金额错误");
			return agreeToReturnRo;
		}
		Integer orderDetailReturnCount = orderDetailList.get(0).getReturnCount();
		orderDetailReturnCount = orderDetailReturnCount == null ? 0 : orderDetailReturnCount;
		BigDecimal cashBackTotal = orderDetailList.get(0).getCashbackTotal();
		cashBackTotal = cashBackTotal == null ? bd : cashBackTotal;
		Integer returnCount = returnList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		Integer newReturnCount = +orderDetailReturnCount;
		BigDecimal newCashBackTotal = new BigDecimal(cashBackTotal.subtract(totalReturn).doubleValue());
		orderDetailMo.setReturnCount(newReturnCount);
		orderDetailMo.setCashbackTotal(newCashBackTotal);
		_log.info("同意退货修改订单详情退货数量和返现总额的参数为：{}", orderDetailMo);
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
		_log.info("同意退货修改退货信息的参数为：{}", ordReturnMo);
		int returnApproveResult = _mapper.returnApprove(ordReturnMo);
		_log.info("同意退货修改退货信息的返回值为：{}", returnApproveResult);
		if (returnApproveResult != 1) {
			_log.error("同意退货修改退货信息时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改退货信息错误");
		}
		agreeToReturnRo.setResult(AgreeToReturnDic.SUCCESS);
		agreeToReturnRo.setMsg("审核成功");
		return agreeToReturnRo;
	}

	/**
	 * 同意退款 Title: agreeToARefund Description:
	 *
	 * @return
	 * @date 2018年5月5日 下午3:41:42
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public AgreeToARefundRo agreeToARefund(OrdOrderReturnTo to) {
		AgreeToARefundRo agreeToARefundRo = new AgreeToARefundRo();
		Long returnCode = to.getReturnCode();
		Long orderId = to.getOrderCode();
		Long orderDetailId = to.getOrderDetailId();
		BigDecimal bd = new BigDecimal("0");
		BigDecimal returnAmount1 = new BigDecimal(to.getReturnAmount1());
		BigDecimal returnAmount2 = new BigDecimal(to.getReturnAmount2());
		BigDecimal subtractCashback = new BigDecimal(to.getSubtractCashback());
		Long refundOpId = to.getOpId();
		if (returnCode == null || orderId == null || orderDetailId == null || refundOpId == null
				|| subtractCashback == null) {
			_log.error("同意退款时出现参数不全");
			agreeToARefundRo.setResult(AgreeToARefundDic.PARAM_NOT_CORRECT);
			agreeToARefundRo.setMsg("参数不正确");
			return agreeToARefundRo;
		}
		BigDecimal returnRental = new BigDecimal(returnAmount1.add(returnAmount2).doubleValue());
		if (returnRental.compareTo(bd) == -1 || returnRental.compareTo(bd) == 0) {
			_log.error("同意退款时出现退款金额为空，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.REFUND_AMOUNT_NOT_NULL);
			agreeToARefundRo.setMsg("退款金额不能为空");
			return agreeToARefundRo;
		}
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("同意退款查询订单信息的参数为：{}", orderId);
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("同意退款查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("同意退款查询订单时发现没有该订单，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.ORDER_NOT_EXIST);
			agreeToARefundRo.setMsg("订单不存在");
			return agreeToARefundRo;
		}
		byte orderState = orderList.get(0).getOrderState();
		if (orderState == OrderStateDic.CANCEL.getCode()
				|| orderState == OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode()) {
			_log.error("同意退款时发现订单未支付或已取消，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.ORDER_NOT_PAY_OR_ALREADY_CANCEL);
			agreeToARefundRo.setMsg("该订单未支付或已取消");
			return agreeToARefundRo;
		}
		BigDecimal returnCashbackToBuyer = returnAmount2;
		if (orderState == OrderStateDic.ALREADY_SIGN_IN.getCode()
				|| orderState == OrderStateDic.ALREADY_SETTLEMENT.getCode()) {
			returnCashbackToBuyer = returnAmount2.subtract(subtractCashback).setScale(4, BigDecimal.ROUND_HALF_UP);
		}
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setOrderId(orderId);
		detailMo.setId(orderDetailId);
		_log.info("同意退款查询订单详情的参数为：{}", detailMo);
		List<OrdOrderDetailMo> detailList = ordOrderDetailSvc.list(detailMo);
		_log.info("同意退款查询订单详情的返回值为：{}", String.valueOf(detailList));
		if (detailList.size() == 0) {
			_log.error("同意退货查询订单详情时发现没有该详情信息，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.USER_NOT_PURCHASE_THE_GOODS);
			agreeToARefundRo.setMsg("用户未购买该商品");
			return agreeToARefundRo;
		}
		if (detailList.get(0).getReturnState() != 1) {
			_log.error("同意退款时发现用户未申请退款，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.USER_NOT_APPLYFOR_REFUND);
			agreeToARefundRo.setMsg("用户未申请退款");
			return agreeToARefundRo;
		}
		OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setReturnCode(returnCode);
		_log.info("同意退款查询退货信息的参数为：{}", returnCode);
		List<OrdReturnMo> returnList = _mapper.selectSelective(returnMo);
		_log.info("同意退款查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("同意退款时发现没有该退款信息，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.RETURN_NOT_EXIST);
			agreeToARefundRo.setMsg("没有找到该退款信息");
			return agreeToARefundRo;
		}
		if (returnList.get(0).getApplicationState() != 1) {
			_log.info("同意退款时发现该退货单不处于待审核状态，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.ORDER_ALREADY_REFUND_OR_ALREADY_CANCEL_APPLYFOR);
			agreeToARefundRo.setMsg("该订单已退款或已取消申请");
			return agreeToARefundRo;
		}
		if (returnList.get(0).getRefundState() == 2) {
			_log.error("同意退款时发现该退货单已退款，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.RETURN_ALREADY_REFUND);
			agreeToARefundRo.setMsg("该退款单已退款");
			return agreeToARefundRo;
		}
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
		int modifyReturnAmountResult = ordOrderSvc.modifyReturnAmountByorderCode(orderMo);
		_log.info("同意退款修改订单退货金额的返回值为：{}", modifyReturnAmountResult);
		if (modifyReturnAmountResult != 1) {
			_log.error("同意退款修改订单退货金额时出错，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.MODIFY_ORDER_RETURN_AMOUNT_ERROR);
			agreeToARefundRo.setMsg("修改订单退货金额出错");
			return agreeToARefundRo;
		}
		Integer returnCount = detailList.get(0).getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		int newReturnCount = returnCount + to.getReturnNum();
		if (newReturnCount == detailList.get(0).getBuyCount()) {
			detailMo.setReturnState((byte) 2);
		} else {
			detailMo.setReturnState((byte) 3);
		}
		detailMo.setReturnCount(newReturnCount);
		BigDecimal cashbackTotal = detailList.get(0).getCashbackTotal();
		cashbackTotal = cashbackTotal == null ? bd : cashbackTotal;
		BigDecimal newCashBackTotal = new BigDecimal(cashbackTotal.subtract(subtractCashback).doubleValue());
		detailMo.setCashbackTotal(newCashBackTotal);
		_log.info("同意退款修改订单详情信息的参数为：{}", detailMo.toString());
		int modifyReturnCountAndCashBackTotalResult = ordOrderDetailSvc.modifyReturnCountAndCashBackTotal(detailMo);
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
		int confirmTheRefundResult = _mapper.confirmTheRefund(returnMo);
		_log.info("同意退款修改退货信息的返回值为：{}", confirmTheRefundResult);
		if (confirmTheRefundResult != 1) {
			_log.error("同意退款修改退货信息时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("修改退货信息出错");
		}
		RefundTo refundTo = new RefundTo();
		refundTo.setOrderId(String.valueOf(orderId));
		refundTo.setOrderDetailId(String.valueOf(orderDetailId));
		refundTo.setBuyerAccountId(returnList.get(0).getApplicationOpId());
		refundTo.setTradeTitle("用户退货-退款");
		refundTo.setTradeDetail(detailList.get(0).getOnlineTitle());
		refundTo.setReturnBalanceToBuyer(returnAmount1);
		refundTo.setReturnCashbackToBuyer(returnCashbackToBuyer);
		refundTo.setOpId(refundOpId);
		refundTo.setMac(to.getMac());
		refundTo.setIp(to.getIp());
		_log.info("已收到货并退款执行退款的参数为：{}", refundTo);
		RefundRo refundResult = afcRefundSvc.refund(refundTo);
		_log.info("已收到货并退款执行退款的返回值为：{}", refundResult);
		if (refundResult.getResult().getCode() != 1) {
			_log.error("已收到货并退款执行退款出错，退货编号为：{}", returnCode);
			throw new RuntimeException("v支付出错，退款失败");
		}
		agreeToARefundRo.setResult(AgreeToARefundDic.SUCCESS);
		agreeToARefundRo.setMsg("退款成功");
		return agreeToARefundRo;
	}

	/**
	 * 已收到货并退款 Title: receivedAndRefunded Description:
	 *
	 * @return
	 * @date 2018年5月5日 下午3:43:00
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ReceivedAndRefundedRo receivedAndRefunded(OrdOrderReturnTo to) {
		ReceivedAndRefundedRo receivedAndRefundedRo = new ReceivedAndRefundedRo();
		Long returnCode = to.getReturnCode();
		returnCode = returnCode == null ? 0L : returnCode;
		Long opId = to.getOpId();
		opId = opId == null ? 0L : opId;
		String ip = to.getIp();
		String mac = to.getMac();
		if (returnCode == 0 || opId == 0 || ip == null || ip.equals("") || ip.equals("null") || mac == null
				|| mac.equals("") || mac.equals("null")) {
			_log.error("已收到货并退款时发现参数不全");
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.PARAM_NOT_CORRECT);
			receivedAndRefundedRo.setMsg("参数不正确");
			return receivedAndRefundedRo;
		}
		OrdReturnMo returnMo = new OrdReturnMo();
		returnMo.setReturnCode(returnCode);
		_log.info("已收到货并退款查询退货信息的参数为：{}", returnCode);
		List<OrdReturnMo> returnList = _mapper.selectSelective(returnMo);
		_log.info("已收到货并退款查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("已收到货并退款时发现退货信息为空，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.RETURN_NOT_NULL);
			receivedAndRefundedRo.setMsg("没有找到该退货信息");
			return receivedAndRefundedRo;
		}
		if (returnList.get(0).getApplicationState() != 2) {
			_log.error("已收到货并退款时发现退货状态不处于退货中，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.CURRENT_STATE_NOT_EXIST_REFUND);
			receivedAndRefundedRo.setMsg("当前状态不允许退款");
			return receivedAndRefundedRo;
		}
		long orderId = returnList.get(0).getOrderId();
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("已收到货并退款查询订单信息的参数为：{}", orderId);
		List<OrdOrderMo> orderList = ordOrderSvc.list(orderMo);
		_log.info("已收到货并退款查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList.size() == 0) {
			_log.error("已收到货并退款查询订单信息时发现没有该订单，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ORDER_NOT_EXIST);
			receivedAndRefundedRo.setMsg("没有找到该订单信息");
			return receivedAndRefundedRo;
		}
		if (orderList.get(0).getOrderState() == OrderStateDic.CANCEL.getCode()
				|| orderList.get(0).getOrderState() == OrderStateDic.ALREADY_PLACE_AN_ORDER.getCode()) {
			_log.error("已收到货并退款时发现订单处于取消或待支付状态，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ORDER_NOT_PAY_OR_ALREADY_CANCEL);
			receivedAndRefundedRo.setMsg("该订单已取消或未支付");
			return receivedAndRefundedRo;
		}
		BigDecimal returnCashbackToBuyer = returnList.get(0).getReturnAmount2();
		if (orderList.get(0).getOrderState() == OrderStateDic.ALREADY_SIGN_IN.getCode()
				|| orderList.get(0).getOrderState() == OrderStateDic.ALREADY_SETTLEMENT.getCode()) {
			returnCashbackToBuyer = returnList.get(0).getReturnAmount2()
					.subtract(returnList.get(0).getSubtractCashback()).setScale(4, BigDecimal.ROUND_HALF_UP);
		}
		long orderDetailId = returnList.get(0).getOrderDetailId();
		OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
		detailMo.setId(orderDetailId);
		detailMo.setOrderId(orderId);
		_log.info("已收到货并退款查询订单详情信息的参数为：{}", detailMo.toString());
		List<OrdOrderDetailMo> detailList = ordOrderDetailSvc.list(detailMo);
		_log.info("已收到货并退款查询订单详情信息的返回值为：{}", String.valueOf(detailList));
		if (detailList.size() == 0) {
			_log.error("已收到货并退款查询订单详情信息时发现订单详情信息为空，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.USER_NOT_PURCHASE_GOODS);
			receivedAndRefundedRo.setMsg("该商品未购买");
			return receivedAndRefundedRo;
		}
		byte returnState = detailList.get(0).getReturnState();
		if (returnState != 1) {
			_log.error("已收到货并退款查询订单详情时发现退货状态不处于退货中，退货编号为：{}", returnCode);
			receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.GOODS_NOT_APPLYFOR_OR_HAVE_FINISHED_RETURN);
			receivedAndRefundedRo.setMsg("该商品未申请或已完成退货");
			return receivedAndRefundedRo;
		}
		if (orderList.get(0).getReturnTotal().compareTo(orderList.get(0).getRealMoney()) == 0) {
			_log.info("已收到货并退款修改订单状态的参数为：{}", orderId);
			int modifyOrderStateResult = ordOrderSvc.modifyOrderStateByOderCode(orderId, (byte) -1);
			_log.info("已收到货并退款修改订单状态的返回值为：{}", modifyOrderStateResult);
			if (modifyOrderStateResult != 1) {
				_log.error("已收到货并退款修改订单状态出错，退货编号为：{}", returnCode);
				receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.MODIFY_ORDER_STATE_ERROR);
				receivedAndRefundedRo.setMsg("修改订单状态出错");
				return receivedAndRefundedRo;
			}
		}
		if (detailList.get(0).getReturnCount() == detailList.get(0).getBuyCount()) {
			detailMo.setReturnState((byte) 2);
		} else {
			detailMo.setReturnState((byte) 3);
		}
		long detailId = detailList.get(0).getId();
		_log.info("已收到货并退款修改订单详情退货状态的参数为：{}，{}", detailId, detailMo.getReturnState());
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
		_log.info("已收到货并退款确认收到货的参数为：{}", returnMo);
		int confirmReceiptOfGoodsResult = _mapper.confirmReceiptOfGoods(returnMo);
		_log.info("已收到货并退款确认收到货的返回值为：{}", confirmReceiptOfGoodsResult);
		if (confirmReceiptOfGoodsResult != 1) {
			_log.error("已收到货并退款确认收到货时出现错误，退货编号为：{}", returnCode);
			throw new RuntimeException("确认收到货出错");
		}
		_log.info("已收到货并退款退款并扣减返现金额的返回值为：{}", receivedAndRefundedRo);
		RefundTo refundTo = new RefundTo();
		refundTo.setOrderId(String.valueOf(orderId));
		refundTo.setOrderDetailId(String.valueOf(orderDetailId));
		refundTo.setBuyerAccountId(returnList.get(0).getApplicationOpId());
		refundTo.setTradeTitle("用户退货-退款");
		refundTo.setTradeDetail(detailList.get(0).getOnlineTitle());
		refundTo.setReturnBalanceToBuyer(returnList.get(0).getReturnAmount1());
		refundTo.setReturnCashbackToBuyer(returnCashbackToBuyer);
		refundTo.setOpId(opId);
		refundTo.setMac(mac);
		refundTo.setIp(ip);
		_log.info("已收到货并退款执行退款的参数为：{}", refundTo);
		RefundRo refundResult = afcRefundSvc.refund(refundTo);
		_log.info("已收到货并退款执行退款的返回值为：{}", refundResult);
		if (refundResult.getResult().getCode() != 1) {
			_log.error("已收到货并退款执行退款出错，退货编号为：{}", returnCode);
			throw new RuntimeException("v支付出错，退款失败");
		}
		receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.SUCCESS);
		receivedAndRefundedRo.setMsg("退款成功");
		return receivedAndRefundedRo;
	}

	@Override
	public List<Map<String, Object>> selectReturningInfo(Map<String, Object> map) throws ParseException,
			IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_log.info("查询用户退货中订单信息的参数为：{}", map.toString());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<OrdReturnMo> orderReturnList = _mapper.selectReturningOrder(map);
		_log.info("查询的结果为: {}", String.valueOf(orderReturnList));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderReturnList.size() != 0) {
			for (int i = 0; i < orderReturnList.size(); i++) {
				Map<String, Object> hm = new HashMap<String, Object>();
				String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
				Date date = simpleDateFormat.parse(l);
				long ts = date.getTime();
				_log.info("转换时间得到的时间戳为：{}", ts);
				hm.put("dateline", ts / 1000);
				hm.put("finishDate", ts / 1000 + 86400);
				hm.put("system", System.currentTimeMillis() / 1000);
				OrdReturnMo obj = orderReturnList.get(i);
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
				_log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
				OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
				detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
				_log.info("查询用户退货订单获取订单详情的参数为：{}", detailMo.toString());
				OrdOrderDetailMo orderDetailResult = ordOrderDetailSvc.getById(detailMo.getId());
				_log.info("查询用户订单信息获取订单详情的返回值为：{}", orderDetailResult);
				List<OrderDetailRo> orderDetailRoList = new ArrayList<OrderDetailRo>();
				_log.info("查询用户订单信息开始获取商品主图");
				List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailResult.getOnlineId(), (byte) 1);
				_log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
				OrderDetailRo orderDetailRo = new OrderDetailRo();
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
	public List<Map<String, Object>> selectReturnInfo(Map<String, Object> map) throws ParseException,
			IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		_log.info("查询用户退货中订单信息的参数为：{}", map.toString());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<OrdReturnMo> orderReturnList = _mapper.selectReturnOrder(map);
		_log.info("查询的结果为: {}", String.valueOf(orderReturnList));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (orderReturnList.size() != 0) {
			for (int i = 0; i < orderReturnList.size(); i++) {
				Map<String, Object> hm = new HashMap<String, Object>();
				String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
				Date date = simpleDateFormat.parse(l);
				long ts = date.getTime();
				_log.info("转换时间得到的时间戳为：{}", ts);
				hm.put("dateline", ts / 1000);
				hm.put("finishDate", ts / 1000 + 86400);
				hm.put("system", System.currentTimeMillis() / 1000);
				OrdReturnMo obj = orderReturnList.get(i);
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
				_log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
				OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
				detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
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
					orderDetailRoList.add(orderDetailRo);
				}
				hm.put("items", orderDetailRoList);
				list.add(i, hm);
			}
		}
		_log.info("最新获取用户退货订单信息的返回值为：{}", String.valueOf(list));
		return list;
	}
}
