package rebue.ord.svc.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.svc.OrdReturnSvc;

import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.to.OrdOrderReturnTo;

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
public class OrdReturnSvcImpl extends MybatisBaseSvcImpl<OrdReturnMo, java.lang.Long, OrdReturnMapper> implements OrdReturnSvc {

	private final static Logger _log = LoggerFactory.getLogger(OrdReturnSvcImpl.class);
	
	@Resource
	private OrdReturnSvc ordReturnSvc;
	
	@Resource
	private OrdReturnPicSvc ordReturnPicSvc;
	
	@Resource
	private OrdOrderSvc ordOrderSvc;
	
	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	/**
	 * 添加用户退货信息 
	 * Title: addEx 
	 * Description:
	 * 1、首先查询订单信息是是否存在和订单的状态
	 * 2、查询订单详情是否存在和是否可以退货
	 * 3、根据订单ID和订单详情ID查询退货订单退货信息，如果该订单退过货，则获取退货的数量
	 * 4、判断退货数量是否等于订单数量
	 * 5、判断已退数量 + 当前退货数量是否大于订单数量
	 * 6、添加退货信息
	 * 7、修改订单详情退货数量和修改订单详情退货状态
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
			throw new RuntimeException("该订单不存在");
		}
		// 订单状态
		byte orderState = orderList.get(0).getOrderState();
		if (orderState == -1) {
			_log.error("添加退货信息出现订单已取消，订单编号为：{}", orderCode);
			throw new RuntimeException("该订单已取消");
		}
		if (orderState == 1) {
			_log.error("添加退货信息出现订单未支付，订单编号为：{}", orderCode);
			throw new RuntimeException("该订单未支付");
		}
		if (orderList.get(0).getRealMoney() == orderList.get(0).getReturnTotal()) {
			_log.error("添加退货信息出现订单已全部退完，订单编号为：{}", orderCode);
			throw new RuntimeException("该订单已全部退完");
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
			throw new RuntimeException("该订单不存在");
		}
		if (orderDetailList.get(0).getReturnState() != 0) {
			_log.error("添加退货信息出现订单详情退货状态不处于未退货状态，订单详情编号为：{}", orderDetailId);
			throw new RuntimeException("当前状态不允许退货");
		}
		// 订单详情退货数量
		Integer returnCount = orderDetailList.get(0).getReturnCount();
		returnCount=returnCount == null ? 0 : returnCount;
		// 订单详情购买数量
		int buyCount = orderDetailList.get(0).getBuyCount();
		if (buyCount == returnCount) {
			_log.error("添加退货信息出现订单详情退货数量等于购买数量，订单详情编号为：{}", orderDetailId);
			throw new RuntimeException("该规格已退完");
		}
		// 最新订单详情退货数量
		int newReturnCount = returnCount + returnNum;
		if (buyCount < newReturnCount) {
			_log.error("添加退货信息出现订单详情退货数量大于订单购买数量，订单详情编号为：{}", orderDetailId);
			throw new RuntimeException("退货数量不能大于购买数量");
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
		ordReturnMo.setReturnType((byte) 2);
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
     * 查询分页列表信息
     * Title: selectReturnPageList
     * Description: 
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
	 * 拒绝退货
	 * Title: rejectReturn
	 * Description: 
	 * @param record
	 * @return
	 * @date 2018年4月27日 上午9:42:37
	 * 1、判断退货编号、订单编号、订单详情ID、拒绝用户编号、拒绝原因等参数是否已传过来
	 * 2、查询根据退货编号和订单编号查询退货信息，判断该退货编号是否存在和查询退货订单的申请状态是否处于待审核状态
	 * 3、查询订单信息并判断订单状态是否处于已取消状态
	 * 4、根据订单编号和订单详情ID查询订单详情信息并判断该订单详情的状态是否处于已退货的状态
	 * 5、修改订单详情退货数量和状态
	 * 6、修改退货订单信息
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
		if (returnCode == 0 || orderId == 0 || orderDetailId == 0 || rejectOpId == 0 || rejectReason.equals("") || rejectReason == null || rejectReason.equals("null")) {
			_log.error("拒绝退货时出现参数不正确，退货编号为：{}", returnCode);
			throw new RuntimeException("参数不正确");
		}
		
		// ===================================拒绝退货第二步==============================
		_log.info("拒绝退货查询退货信息的参数为：{}", record.toString());
		List<OrdReturnRo> returnList = _mapper.selectReturnPageList(record);
		_log.info("拒绝退货查询退货信息的返回值为：{}", String.valueOf(returnList));
		if (returnList.size() == 0) {
			_log.error("拒绝退货查询退货信息时出现退货信息为空，退货编号为：{}", returnCode);
			throw new RuntimeException("退货信息不存在");
		}
		
		if (returnList.get(0).getApplicationState() != 1) {
			_log.error("拒绝退货时出现退货状态不处于待审核状态，退货编号为：{}", returnCode);
			throw new RuntimeException("当前状态不允许审核");
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
			throw new RuntimeException("该用户订单不存在");
		}
		
		if (orderList.get(0).getOrderState() == -1) {
			_log.error("拒绝退货时出现订单状态为取消状态，退货编号为：{}", returnCode);
			throw new RuntimeException("该订单已取消，拒绝退货失败");
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
			throw new RuntimeException("该用户 订单不存在");
		}
		
		if (orderDetailMo.getReturnState() != 1) {
			_log.error("拒绝退货时出现订单详情退货状态不处于退货中状态，退货编号为：{}", returnCode);
			throw new RuntimeException("该退货订单已退货或该订单未退货");
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
		resultMap.put("result", 1);
		resultMap.put("msg", "操作成功");
		return resultMap;
	}
	
	/**
	 * 同意退货
	 * Title: agreeToReturn
	 * Description: 
	 * @return
	 * @date 2018年5月5日 下午3:26:49
	 */
	public Map<String, Object> agreeToReturn() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return resultMap;
	}
	
	/**
	 * 同意退款
	 * Title: agreeToARefund
	 * Description: 
	 * @return
	 * @date 2018年5月5日 下午3:41:42
	 */
	public Map<String, Object> agreeToARefund() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return resultMap;
	}
	
	/**
	 * 已收到货并退款
	 * Title: receivedAndRefunded
	 * Description: 
	 * @return
	 * @date 2018年5月5日 下午3:43:00
	 */
	public Map<String, Object> receivedAndRefunded() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return resultMap;
	}
}
