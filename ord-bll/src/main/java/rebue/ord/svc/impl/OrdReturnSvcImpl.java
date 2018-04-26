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
		} catch (Exception e) {
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
		int returnCount = orderDetailList.get(0).getReturnCount();
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
		
		// =============================修改订单详情状态开始=============================
		orderDetailMo.setReturnState((byte) 1);
		orderDetailMo.setId(orderDetailId);
		orderDetailMo.setReturnCount(newReturnCount);
		int updateOrderDetailStateResult = ordOrderDetailSvc.modify(orderDetailMo);
		if (updateOrderDetailStateResult < 1) {
			_log.error("修改订单详情状态失败，返回值为：{}", updateOrderDetailStateResult);
			throw new RuntimeException("修改订单详情状态失败");
		}
		// =============================修改订单详情状态结束=============================
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
     * 退货审核通过
     * Title: updateReturnApprove
     * Description: 
     * @param record
     * @return
     * @date 2018年4月21日 下午5:14:48
     */
	@Override
	public Map<String, Object> updateReturnApprove(OrdReturnMo record) {
		Date date = new Date();
		record.setReviewTime(date);
		_log.info("退货审核通过的参数为：{}", record.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 退货审核通过
		int result = _mapper.updateReturnApprove(record);
		_log.info("退货审核通过的返回值为：{}", result);
		if (result < 0) {
			_log.error("退货审核通过时出现错误，返回值为：{}", result);
			throw new RuntimeException("审核通过失败");
		}
		resultMap.put("result", 1);
		resultMap.put("msg", "审核通过成功");
		return resultMap;
	}

}
