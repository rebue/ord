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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.afc.dic.TradeTypeDic;
import rebue.afc.ro.RefundRo;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.svr.feign.AfcSettleTaskSvc;
import rebue.afc.to.RefundTo;
import rebue.afc.to.TaskTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.ord.dic.AddReturnDic;
import rebue.ord.dic.AgreeToARefundDic;
import rebue.ord.dic.AgreeToReturnDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReceivedAndRefundedDic;
import rebue.ord.dic.RejectReturnDic;
import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdBuyRelationMo;
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
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
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

	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

	/**
	 */
	@Resource
	private AfcRefundSvc afcRefundSvc;

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
		// 获取现在时间,判断退货时间是否在签收后7天内
		long nowTime = new Date().getTime();
		Date signInTime = orderList.get(0).getReceivedTime();
		if(signInTime!=null) {
			long limitTime = signInTime.getTime() + returnLimitTime * 60 * 60 * 1000;
			if (limitTime < nowTime) {
				_log.error("超过收货后七天申请退款：{}", orderCode);
				String returnReason = "超过收货后七天退款:"+to.getReturnReason();
				to.setReturnReason(returnReason);
			}
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
		orderDetailMo.setId(orderDetailId);
		orderDetailMo.setSpecName(to.getSpecName());
		_log.info("查询订单详情的参数为：{}", orderDetailMo);
		OrdOrderDetailMo orderDetailList = new OrdOrderDetailMo();
		try {
			orderDetailList = ordOrderDetailSvc.getById(orderDetailId);
			_log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
		} catch (Exception e) {
			_log.error("===========查询订单详情出错了===========");
			e.printStackTrace();
		}
		if (orderDetailList == null) {
			_log.error("添加退货信息出现订单详情不存在，订单详情编号为：{}", orderDetailId);
			addReturnRo.setResult(AddReturnDic.ORDER_NOT_EXIST);
			addReturnRo.setMsg("订单不存在");
			return addReturnRo;
		}
		if (orderDetailList.getReturnState() != 0) {
			_log.error("添加退货信息出现订单详情退货状态不处于未退货状态，订单详情编号为：{}", orderDetailId);
			addReturnRo.setResult(AddReturnDic.CURRENT_STATE_NOT_EXIST_RETURN);
			addReturnRo.setMsg("当前状态不允许退货");
			return addReturnRo;
		}
		Integer returnCount = orderDetailList.getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		int buyCount = orderDetailList.getBuyCount();
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
		_log.error("暂停返佣任务");
		TaskTo taskTo = new TaskTo();
		taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
		taskTo.setOrderDetailId(String.valueOf(orderDetailId));
		Ro ro = afcSettleTaskSvc.suspendTask(taskTo);
		_log.info("暂停返佣任务返回值:{}", ro);
		if (ro.getResult().getCode() == ResultDic.FAIL.getCode()) {
			_log.info("暂停返佣任务失败");
			throw new RuntimeException("暂停返佣任务失败");
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
	public PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnRo qo, int pageNum, int pageSize) {
		_log.info("selectReturnPageList: qo-{}; pageNum-{}; pageSize-{}", qo, pageNum, pageSize);
		PageInfo<OrdReturnRo> result=PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> _mapper.selectReturnPageList(qo));
		_log.info("查询退货信息的返回值为：{}", result.getList());
			
			for (OrdReturnRo item : result.getList()) {
				OrdReturnPicMo mo=new OrdReturnPicMo();
				mo.setReturnId(item.getId());
				_log.info("查询退货图片的参数为：{}", mo);
				 List<OrdReturnPicMo> picList=ordReturnPicSvc.list(mo);
				 _log.info("查询退货图片的返回值为：{}", picList);
				 item.setPicList(picList);
			}
			_log.info("查询退货信息和图片的返回值为：{}", result.getList());
		return result;
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
	public RejectReturnRo rejectReturn(OrdReturnRo record) {
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
		// orderMo.setOrderCode(String.valueOf(orderId));
		_log.info("拒绝退货查询订单信息的参数为：{}", orderMo);
		OrdOrderMo orderList = ordOrderSvc.getById(orderId);
		_log.info("拒绝退货查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList == null) {
			_log.error("拒绝退货查询订单信息时出现订单信息不存在，退货编号为：{}", returnCode);
			rejectReturnRo.setResult(RejectReturnDic.ORDER_NOT_EXIST);
			rejectReturnRo.setMsg("订单不存在");
			return rejectReturnRo;
		}
		if (orderList.getOrderState() == OrderStateDic.CANCEL.getCode()) {
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
		// 恢复该详情返佣任务
		TaskTo taskTo = new TaskTo();
		taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
		taskTo.setOrderDetailId(String.valueOf(orderDetailId));
		Ro ro = afcSettleTaskSvc.resumeTask(taskTo);
		_log.info("恢复返佣任务返回值:{}", ro);
		if (ro.getResult().getCode() == ResultDic.FAIL.getCode()) {
			_log.info("恢复返佣任务失败");
			throw new RuntimeException("恢复返佣任务失败");
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
		// orderDetailMo.setOrderId(orderId);
		// orderDetailMo.setId(orderDetailId);
		_log.info("同意退货查询订单详情信息的参数为：{}", orderDetailMo.toString());
		OrdOrderDetailMo orderDetailList = ordOrderDetailSvc.getById(orderDetailId);
		_log.info("同意退货查询订单详情信息的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList == null) {
			_log.error("同意退货查询订单详情信息时发现没有找到该订单详情信息，退货编号为：{}", returnCode);
			agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_DETAIL_NOT_NULL);
			agreeToReturnRo.setMsg("没有找到该退货商品信息");
			return agreeToReturnRo;
		}
		if (orderDetailList.getReturnState() != 1) {
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
		Integer orderDetailReturnCount = orderDetailList.getReturnCount();
		orderDetailReturnCount = orderDetailReturnCount == null ? 0 : orderDetailReturnCount;
		BigDecimal cashBackTotal = orderDetailList.getCashbackTotal();
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
		_log.info("删除该定单详情的购买关系记录");
		int delResult = ordBuyRelationSvc.del(orderDetailList.getId());
		_log.info("删除该定单详情的购买关系记录的返回值为：{}", delResult);
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
		_log.error("同意退款的参数为：{}", to);
		AgreeToARefundRo agreeToARefundRo = new AgreeToARefundRo();
		Long returnCode = to.getReturnCode();
		Long orderId = to.getOrderId();
		Long orderDetailId = to.getOrderDetailId();
		BigDecimal bd = new BigDecimal("0");
		BigDecimal returnAmount1 = new BigDecimal(Double.toString(to.getReturnAmount1()));
		BigDecimal returnAmount2 = new BigDecimal(Double.toString(to.getReturnAmount2()));
		// BigDecimal subtractCashback = new BigDecimal(to.getSubtractCashback());
		BigDecimal subtractCashback = bd;
		Long refundOpId = to.getOpId();
		if (returnCode == null || orderId == null || orderDetailId == null || refundOpId == null
				|| subtractCashback == null) {
			_log.error("同意退款时出现参数不全");
			agreeToARefundRo.setResult(AgreeToARefundDic.PARAM_NOT_CORRECT);
			agreeToARefundRo.setMsg("参数不正确");
			return agreeToARefundRo;
		}
		BigDecimal returnRental = new BigDecimal(returnAmount1.add(returnAmount2).toString());
		_log.error("本次退款总额=返现金+余额：{}", returnRental);
		if (returnRental.compareTo(bd) == -1 || returnRental.compareTo(bd) == 0) {
			_log.error("同意退款时出现退款金额为空，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.REFUND_AMOUNT_NOT_NULL);
			agreeToARefundRo.setMsg("退款金额不能为空");
			return agreeToARefundRo;
		}
		OrdOrderMo orderMo = new OrdOrderMo();
		orderMo.setId(orderId);
		_log.info("同意退款查询订单信息的参数为：{}", orderId);
		OrdOrderMo orderList = ordOrderSvc.getById(orderId);
		_log.info("同意退款查询订单信息的返回值为：{}", String.valueOf(orderList));
		if (orderList == null) {
			_log.error("同意退款查询订单时发现没有该订单，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.ORDER_NOT_EXIST);
			agreeToARefundRo.setMsg("订单不存在");
			return agreeToARefundRo;
		}
		byte orderState = orderList.getOrderState();
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
		_log.info("同意退款查询订单详情的ID为：{}", orderDetailId);
		OrdOrderDetailMo detailList = ordOrderDetailSvc.getById(orderDetailId);
		_log.info("同意退款查询订单详情的返回值为：{}", String.valueOf(detailList));
		if (detailList == null) {
			_log.error("同意退货查询订单详情时发现没有该详情信息，退货编号为：{}", returnCode);
			agreeToARefundRo.setResult(AgreeToARefundDic.USER_NOT_PURCHASE_THE_GOODS);
			agreeToARefundRo.setMsg("用户未购买该商品");
			return agreeToARefundRo;
		}
		if (detailList.getReturnState() != 1) {
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
		BigDecimal returnTotal = orderList.getReturnTotal();
		_log.error("查询已经退款总额：{}", returnTotal);
		if (returnTotal == null) {
			returnTotal = new BigDecimal("0");
		}
		BigDecimal newOrderReturnTotal = new BigDecimal(returnTotal.add(returnRental).toString());
		_log.error("已经退货总额加上(本次退款总额=返现金+余额)：{}", newOrderReturnTotal);
		_log.error("查询实际金额为：{}", orderList.getRealMoney());
		if (orderList.getRealMoney().compareTo(newOrderReturnTotal) == 0) {
			_log.error("实际金额等于退货总额，修改订单状态为-1=取消：realMoney={}, returnTotal={}", orderList.getRealMoney(),newOrderReturnTotal);
			orderMo.setOrderState((byte) -1);
		}else {
			_log.error("实际金额不等于退货总额，不修改订单状态：realMoney={}, returnTotal={}", orderList.getRealMoney(),newOrderReturnTotal);
		}
		BigDecimal oldReturnAmount1 = orderList.getReturnAmount1();
		_log.error("查询已经退款余额：{}", oldReturnAmount1);
		if (oldReturnAmount1 == null) {
			oldReturnAmount1 = new BigDecimal("0");
		}
		BigDecimal newReturnAmount1 = new BigDecimal(oldReturnAmount1.add(returnAmount1).toString());
		_log.error("已经退款余额加上本次退款余额：{}", newReturnAmount1);
		
		
		BigDecimal oldReturnAmount2 = orderList.getReturnAmount2();
		_log.error("查询已经退款返现金：{}", oldReturnAmount2);
		if (oldReturnAmount2 == null) {
			oldReturnAmount2 = new BigDecimal("0");
		}
		BigDecimal newReturnAmount2 = new BigDecimal(oldReturnAmount2.add(returnAmount2).toString());
		_log.error("已经退款返现金加上本次退款返现金：{}", newReturnAmount2);
		orderMo.setReturnAmount1(newReturnAmount1);
		orderMo.setReturnAmount2(newReturnAmount2);
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
		Integer returnCount = detailList.getReturnCount();
		returnCount = returnCount == null ? 0 : returnCount;
		int newReturnCount = returnCount + to.getReturnNum();
		if (newReturnCount == detailList.getBuyCount()) {
			detailMo.setReturnState((byte) 2);
		} else {
			detailMo.setReturnState((byte) 3);
		}
		detailMo.setReturnCount(newReturnCount);
		BigDecimal cashbackTotal = detailList.getCashbackTotal();
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
		refundTo.setTradeDetail(detailList.getOnlineTitle());
		refundTo.setReturnBalanceToBuyer(returnAmount1);
		refundTo.setReturnCashbackToBuyer(returnCashbackToBuyer);
		refundTo.setOpId(refundOpId);
		// refundTo.setMac(to.getMac());
		// refundTo.setIp(to.getIp());
		refundTo.setMac("没有获取到MAC地址");
		refundTo.setIp("没有获取到IP地址");
		_log.info("已收到货并退款执行退款的参数为：{}", refundTo);
		RefundRo refundResult = afcRefundSvc.refund(refundTo);
		_log.info("已收到货并退款执行退款的返回值为：{}", refundResult);
		if (refundResult.getResult().getCode() != 1) {
			_log.error("已收到货并退款执行退款出错，退货编号为：{}", returnCode);
			if ("重复退款".equals(refundResult.getMsg())) {
				agreeToARefundRo.setResult(AgreeToARefundDic.SUCCESS);
				agreeToARefundRo.setMsg("退款成功");
				return agreeToARefundRo;
			} else {
				throw new RuntimeException("v支付出错，退款失败");
			}
		}
		_log.info("获取该定单详情做为下家的购买关系记录");
		OrdBuyRelationMo buyRelationParamMo = new OrdBuyRelationMo();
		buyRelationParamMo.setDownlineOrderDetailId(detailList.getId());
		OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(buyRelationParamMo);
		_log.info("获取该定单详情做为下家的购买关系记录为:{}", buyRelationResult);
		if (buyRelationResult == null) {
			_log.info("该定单详情做为下家的购买关系记录为空");
		} else {
			_log.info("删除该定单详情做为下家的购买关系记录");
			int delResult = ordBuyRelationSvc.del(buyRelationResult.getId());
			_log.info("删除该定单详情的购买关系记录的返回值为：{}", delResult);
			if (delResult != 1) {
				_log.info("删除该定单详情做为下家的购买关系记录失败");
				throw new RuntimeException("删除该定单详情做为下家的购买关系记录失败");
			}
			_log.info("更新该定单详情上家的返佣名额");
			OrdOrderDetailMo ordDetailMo = ordOrderDetailSvc.getById(buyRelationResult.getUplineOrderDetailId());
			if (ordDetailMo == null) {
				_log.info("该定单上家的定单详情为空");
				throw new RuntimeException("该定单上家的定单详情为空");
			}
			byte commissionSlot = ordDetailMo.getCommissionSlot();
			ordDetailMo.setCommissionSlot((byte) (commissionSlot + 1));
			int updateUplineDetailResult = ordOrderDetailSvc.modify(ordDetailMo);
			if (updateUplineDetailResult != 1) {
				_log.info("更新该定单详情上家的返佣名额失败");
				throw new RuntimeException("更新该定单详情上家的返佣名额失败");
			}
		}
		_log.info("获取该定单详情下家购买关系");
		OrdBuyRelationMo buyRelationParamMo1 = new OrdBuyRelationMo();
		buyRelationParamMo1.setUplineOrderDetailId(detailList.getId());
		List<OrdBuyRelationMo> buyRelationResult1 = ordBuyRelationSvc.list(buyRelationParamMo1);
		if (buyRelationResult1.size() == 0) {
			_log.info("该定单详情下家购买关系为空");
		} else {
			for (int i = 0; i < buyRelationResult1.size(); i++) {
				_log.info("重新匹配该定单详情下家购买关系，购买关系ID：" + buyRelationResult1.get(i).getId());
				_log.info("全返商品添加购买关系");
				long userId = buyRelationResult1.get(i).getDownlineUserId();
				long onlineId = detailList.getOnlineId();
				BigDecimal buyPrice = detailList.getBuyPrice();
				long downLineDetailId = detailList.getId();
				long downLineOrderId = detailList.getOrderId();
				String matchBuyRelationResult = ordBuyRelationSvc.matchBuyRelation(userId, onlineId, buyPrice,
						downLineDetailId, downLineOrderId);
				_log.info(matchBuyRelationResult);
				_log.info("删除购买关系：" + buyRelationResult1.get(i).getId());
				int delResult = ordBuyRelationSvc.del(buyRelationResult1.get(i).getId());
				if (delResult != 1) {
					_log.info("删除购买关系失败：" + buyRelationResult1.get(i).getId());
					throw new RuntimeException("删除购买关系失败");
				}
			}
		}
		// 取消该详情返佣任务
		_log.info("取消该详情返佣任务");
		TaskTo taskTo = new TaskTo();
		taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
		taskTo.setOrderDetailId(String.valueOf(orderDetailId));
		Ro ro = afcSettleTaskSvc.cancelTask(taskTo);
		_log.info("取消返佣任务返回值:{}", ro);
		if (ro.getResult().getCode() == ResultDic.FAIL.getCode()) {
			_log.info("取消返佣任务失败");
			throw new RuntimeException("取消返佣任务失败");
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

	/**
	 * 查询退货中的数据
	 */
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
		_log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
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
	
	/**
	 * 取消退货
	 * @param mo
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro cancelReturn(OrdReturnMo mo) {
		_log.info("取消退货的请求参数为：{}", mo);
		Ro ro = new Ro();
		_log.info("取消退货查询退货信息的参数为：{}", mo);
		OrdReturnMo ordReturnMo = _mapper.selectByPrimaryKey(mo.getId());
		_log.info("取消退货查询退货信息的返回值为：{}", ordReturnMo);
		if (ordReturnMo == null) {
			_log.error("取消订单查询退货信息返回为空，退货id为: {}", mo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数错误");
			return ro;
		}
		
		// 修改订单详情退货状态
		_log.info("取消退货修改订单详情退货状态的参数为：{}", ordReturnMo.getOrderDetailId());
		int modifyReturnStateByIdResult = ordOrderDetailSvc.modifyReturnStateById(ordReturnMo.getOrderDetailId(), (byte) 0);
		_log.info("取消退货修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
		if (modifyReturnStateByIdResult != 1) {
			_log.error("取消订单修改订单详情状态失败，退货id为: {}", mo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("修改状态失败");
			return ro;
		}
		
		// 恢复该详情返佣任务
		TaskTo taskTo = new TaskTo();
		taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
		taskTo.setOrderDetailId(String.valueOf(ordReturnMo.getOrderDetailId()));
		_log.info("取消退货恢复返佣任务的参数为：{}", taskTo);
		Ro resumeTask = afcSettleTaskSvc.resumeTask(taskTo);
		_log.info("取消退货恢复返佣任务的返回值为：{}", resumeTask);
		if (resumeTask.getResult().getCode() == ResultDic.FAIL.getCode()) {
			_log.error("取消订单恢复返佣任务失败，退货id为: {}", mo.getId());
			throw new RuntimeException("恢复返佣失败");
		}
		
		// 修改退货信息表申请状态和取消的操作人等信息
		_log.info("取消退货信息退货信息的参数为：{}", mo);
		int updateByPrimaryKeySelectiveResult = _mapper.updateByPrimaryKeySelective(mo);
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
