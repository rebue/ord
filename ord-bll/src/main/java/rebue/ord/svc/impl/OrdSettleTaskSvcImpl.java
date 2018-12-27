package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.afc.dic.TradeTypeDic;
import rebue.afc.mo.AfcPlatformTradeMo;
import rebue.afc.mo.AfcTradeMo;
import rebue.afc.platform.dic.PlatformTradeTypeDic;
import rebue.afc.svr.feign.AfcPlatformTradeTradeSvc;
import rebue.afc.svr.feign.AfcTradeSvc;
import rebue.ord.dic.CommissionStateDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.dic.SettleTaskTypeDic;
import rebue.ord.mapper.OrdBuyRelationMapper;
import rebue.ord.mapper.OrdOrderDetailMapper;
import rebue.ord.mapper.OrdSettleTaskMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.pnt.dic.PointLogTypeDic;
import rebue.pnt.svr.feign.PntPointSvc;
import rebue.pnt.to.AddPointTradeTo;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.wheel.exception.RuntimeExceptionX;

/**
 * 结算任务
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdSettleTaskSvcImpl extends MybatisBaseSvcImpl<OrdSettleTaskMo, java.lang.Long, OrdSettleTaskMapper>
		implements OrdSettleTaskSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(final OrdSettleTaskMo mo) {
		_log.info("添加结算任务");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	private static final Logger _log = LoggerFactory.getLogger(OrdSettleTaskSvcImpl.class);

	/**
	 * 启动结算任务执行的延迟时间(单位小时)，默认为7*24+1小时
	 */
	@Value("${ord.settle.startSettleDelay:169}")
	private BigDecimal startSettleDelay;

	/**
	 * 供应商结算任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settleSupplierDelay:1}")
	private BigDecimal settleSupplierDelay;

	/**
	 * 结算返现金给用户任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settleCashbackToBuyerDelay:1}")
	private BigDecimal settleCashbackToBuyerDelay;

	/**
	 * 结算购买积分给买家任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settlePointToBuyerDelay:1}")
	private BigDecimal settlePointToBuyerDelay;

	/**
	 * 释放卖家的已占用保证金任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.freeDepositUsedOfSellerDelay:1}")
	private BigDecimal freeDepositUsedOfSellerDelay;

	/**
	 * 结算利润给卖家任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settleProfitToSellerDelay:1}")
	private BigDecimal settleProfitToSellerDelay;

	/**
	 * 结算平台服务费任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settlePlatformServiceFeeDelay:1}")
	private BigDecimal settlePlatformServiceFeeDelay;

	/**
	 * 结算返佣任务执行的延迟时间(单位小时)
	 */
	@Value("${ord.settle.settleCommissionDelay:1}")
	private BigDecimal settleCommissionDelay;

	/**
	 * 完成结算任务执行的延迟时间(单位小时)，须设置在所有结算任务之后
	 */
	@Value("${ord.settle.completeSettleDelay:2}")
	private BigDecimal completeSettleDelay;

	/**
	 * 平台服务费比例(例如6%则设置0.06，默认为0)
	 */
	@Value("${ord.settle.platformServiceFeeRatio:0}")
	private BigDecimal platformServiceFeeRatio;

	@Resource
	private OrdSettleTaskSvc thisSvc;
	@Resource
	private OrdTaskSvc taskSvc;
	@Resource
	private OrdOrderSvc orderSvc;
	@Resource
	private OrdOrderDetailSvc orderDetailSvc;
	@Resource
	private AfcTradeSvc afcTradeSvc;
	@Resource
	private AfcPlatformTradeTradeSvc afcPlatformTradeSvc;
	@Resource
	private OrdBuyRelationSvc buyRelationSvc;
	@Resource
	private PntPointSvc pntPointSvc;

	@Resource
	private OrdBuyRelationMapper buyRelationMapper;
	@Resource
	private OrdOrderDetailMapper orderDetailMapper;

	/**
	 * 添加启动结算订单的任务(根据订单ID添加)
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addStartSettleTask(final Long orderId) {
		_log.info("添加启动结算订单的任务：orderId-{}", orderId);
		if (orderId == null) {
			final String msg = "参数不正确";
			_log.error(msg);
			throw new RuntimeException(msg);
		}
		final OrdOrderMo order = orderSvc.getById(orderId);
		if (order == null) {
			final String msg = "订单不存在";
			_log.error("{}: orderId-{}", msg, orderId);
			throw new RuntimeException(msg);
		}
		// 检查订单是否可结算
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}
		// 计算当前时间
		final Date now = new Date();
		// 添加启动结算的任务
		final OrdTaskMo taskMo = new OrdTaskMo();
		taskMo.setOrderId(orderId.toString());
		taskMo.setTaskType((byte) OrderTaskTypeDic.START_SETTLE.getCode());
		taskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
		// 计算计划执行时间
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE,
				startSettleDelay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		taskMo.setExecutePlanTime(calendar.getTime());
		taskSvc.add(taskMo);
		_log.info("添加启动结算订单的任务成功：orderId-{}", orderId);
	}

	/**
	 * 添加结算任务(启动结算任务执行时添加)
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addSettleTasks(final Long orderId) {
		_log.info("添加订单结算任务：orderId-{}", orderId);
		if (orderId == null) {
			final String msg = "参数不正确";
			_log.error(msg);
			throw new RuntimeException(msg);
		}
		final OrdOrderMo order = orderSvc.getById(orderId);
		if (order == null) {
			final String msg = "订单不存在";
			_log.error("{}: orderId-{}", msg, orderId);
			throw new RuntimeException(msg);
		}
		if (order.getOrderState() == OrderStateDic.CANCEL.getCode()) {
			_log.error("添加订单结算任务查询订单信息时发现订单处于作废状态，订单id为：{}", orderId);
			return;
		}
		// XXX 按理说在添加订单结算任务时就已经做过校验了，这里重复做一些校验更安全些，每个环节都要如此严谨
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}
		final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(orderId);
		_log.info("添加订单结算任务查询订单详情信息的参数为：{}", orderDetailMo);
		final List<OrdOrderDetailMo> detailList = orderDetailSvc.list(orderDetailMo);
		_log.info("添加订单结算任务查询订单详情信息的参数为：{}", String.valueOf(detailList));
		if (detailList.size() == 0) {
			_log.error("添加订单结算任务查询订单详情信息时发现没有订单详情，订单id为：{}", orderId);
			throw new RuntimeException("订单详情不存在");
		}
		for (final OrdOrderDetailMo ordOrderDetailMo : detailList) {
			if (ordOrderDetailMo.getReturnState() == ReturnStateDic.RETURNING.getCode()) {
				_log.error("添加订单结算任务任务时发现有订单详情处于退货状态，暂时不添加结算任务，订单id为：{}", orderId);
				throw new RuntimeException("订单详情处于退货中");
			}
		}
		// 计算当前时间
		final Date now = new Date();
		_log.info("添加结算成本给供应商(余额+)的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_COST_TO_SUPPLIER, settleSupplierDelay);
		_log.info("添加结算返现金给买家的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_CASHBACK_TO_BUYER, settleCashbackToBuyerDelay);
		_log.info("添加结算购买积分给买家的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_POINT_TO_BUYER, settlePointToBuyerDelay);
		_log.info("添加释放卖家的已占用保证金的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.FREE_DEPOSIT_USED_OF_SELLER, freeDepositUsedOfSellerDelay);
		_log.info("添加结算利润给卖家(余额+)的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_PROFIT_TO_SELLER, settleProfitToSellerDelay);
		_log.info("添加结算平台服务费的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_PLATFORM_SERVICE_FEE, settlePlatformServiceFeeDelay);
		_log.info("添加结算返佣金的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_COMMISSION, settleCommissionDelay);
		// 添加启动结算的任务
		final OrdTaskMo taskMo = new OrdTaskMo();
		taskMo.setOrderId(orderId.toString());
		taskMo.setTaskType((byte) OrderTaskTypeDic.COMPLETE_SETTLE.getCode());
		taskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
		// 计算计划执行时间
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE,
				completeSettleDelay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		taskMo.setExecutePlanTime(calendar.getTime());
		_log.info("添加订单结算完成任务的参数为:{}", taskMo);
		taskSvc.add(taskMo);
		_log.info("添加启动结算订单的任务成功：orderId-{}", orderId);
	}

	/**
	 * 添加结算子任务
	 *
	 * @param orderId 订单ID
	 * @param now     当前时间
	 * @param delay   延迟时间
	 */
	private void addSettleSubTask(final Long orderId, final Date now, final SettleTaskTypeDic taskType,
			final BigDecimal delay) {
		final Calendar calendar = Calendar.getInstance();
		final OrdTaskMo subTaskMo = new OrdTaskMo();
		subTaskMo.setOrderId(orderId.toString());
		subTaskMo.setTaskType((byte) OrderTaskTypeDic.SETTLE.getCode());
		subTaskMo.setExecuteState((byte) TaskExecuteStateDic.NONE.getCode());
		subTaskMo.setSubTaskType((byte) taskType.getCode());
		// 计算计划执行时间
		calendar.setTime(now);
		calendar.add(Calendar.MINUTE,
				delay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		subTaskMo.setExecutePlanTime(calendar.getTime());
		taskSvc.add(subTaskMo);
	}

	/**
	 * 执行结算任务(子任务)
	 */
	@Override
	public void executeSettleTask(final OrdTaskMo taskMo) {
		_log.info("添加订单结算任务：taskMo-{}", taskMo);
		if (taskMo.getOrderId() == null) {
			final String msg = "参数不正确";
			_log.error(msg);
			throw new RuntimeException(msg);
		}
		final Long orderId = Long.valueOf(taskMo.getOrderId());
		final OrdOrderMo order = orderSvc.getById(orderId);
		if (order == null) {
			final String msg = "订单不存在";
			_log.error("{}: orderId-{}", msg, orderId);
			throw new RuntimeException(msg);
		}
		// XXX 按理说在添加订单结算任务时就已经做过校验了，这里重复做一些校验更安全些，每个环节都要如此严谨
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}
		// 计算计划执行时间
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(order.getReceivedTime());
		calendar.add(Calendar.MINUTE,
				startSettleDelay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		_log.info("计算计划执行时间为:{}", calendar.getTimeInMillis());
		_log.info("当前时间戳为:{}", System.currentTimeMillis());
		if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
			final String msg = "还未到订单启动结算的时间";
			_log.error("{}: 订单信息-{}", msg, order);
			throw new RuntimeException(msg);
		}
		final OrdOrderDetailMo conditions = new OrdOrderDetailMo();
		conditions.setOrderId(orderId);
		final List<OrdOrderDetailMo> orderDetailList = orderDetailSvc.list(conditions);
		if (orderDetailList.size() <= 0) {
			final String msg = "找不到订单详情";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}
		_log.info("订单详情列表：{}", orderDetailList);
		// 结算任务类型
		final SettleTaskTypeDic settleTaskType = SettleTaskTypeDic.getItem(taskMo.getSubTaskType());
		_log.debug("遍历订单详情列表");
		for (final OrdOrderDetailMo orderDetail : orderDetailList) {
			_log.debug("遍历订单详情列表: orderDetail-{}", orderDetail);
			// 计算当前时间
			final Date now = new Date();
			if (ReturnStateDic.RETURNING.getCode() == orderDetail.getReturnState()) {
				final String msg = "订单详情正处于退货中，不能结算";
				_log.error("{}: {}", msg, orderDetail);
				throw new RuntimeException(msg);
			}
			if (ReturnStateDic.RETURNED.getCode() == orderDetail.getReturnState()) {
				final String msg = "订单详情已经退货，不用结算";
				_log.info("{}: {}", msg, orderDetail);
				continue;
			}
			switch (settleTaskType) {
			// 结算成本给供应商(余额+)
			case SETTLE_COST_TO_SUPPLIER: {
				_log.info("结算成本给供应商(余额+), 订单id为：{}, 订单详情id为：{}", orderDetail.getOrderId(), orderDetail.getId());
				if (orderDetail.getSupplierId() == null || orderDetail.getSupplierId() == 0
						|| orderDetail.getCostPrice() == null
						|| orderDetail.getCostPrice().compareTo(BigDecimal.ZERO) <= 0) {
					_log.error("结算成本给供应商时出现供应商或成本为空或0的情况，订单详情为：{}", orderDetail);
					continue;
				}
				final AfcTradeMo tradeMo = new AfcTradeMo();
				tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_SUPPLIER.getCode());
				tradeMo.setAccountId(orderDetail.getSupplierId());
				// 获取订单详情真实购买数量
				final int realBuyCount = orderDetail.getBuyCount() - orderDetail.getReturnCount();
				// 订单详情总成本价 = 成本价 * 真实购买数量
				final BigDecimal costPriceTotal = orderDetail.getCostPrice().multiply(BigDecimal.valueOf(realBuyCount))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				tradeMo.setTradeAmount(costPriceTotal);
				tradeMo.setTradeTitle("结算供应商(将成本打到供应商的余额)");
				addAccountTrade(taskMo, orderDetail, tradeMo, now);
				break;
			}
			// 结算返现金给买家
			case SETTLE_CASHBACK_TO_BUYER: {
				_log.info("结算返现金给买家: orderDetail-{}", orderDetail);
				final BigDecimal cashbackTotal = orderDetail.getCashbackTotal();
				if (cashbackTotal == null || cashbackTotal.compareTo(BigDecimal.ZERO) == 0) {
					_log.debug("结算返现金给买家时发现返现总额为null或为0，订单详情为：{}", orderDetail);
					continue;
				}

				if (cashbackTotal.compareTo(BigDecimal.ZERO) < 0) {
					throw new RuntimeExceptionX("结算返现金给买家时发现返现总额<0，订单详情为：" + orderDetail);
				}

				// 设置订单详情已结算返现金给买家
				orderDetailSvc.settleBuyer(orderDetail.getId());
				final AfcTradeMo tradeMo = new AfcTradeMo();
				tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_CASHBACK.getCode());
				tradeMo.setAccountId(order.getUserId());
				tradeMo.setTradeAmount(orderDetail.getCashbackTotal());
				tradeMo.setTradeTitle("结算返现金给买家");
				addAccountTrade(taskMo, orderDetail, tradeMo, now);
				break;
			}
			// 结算购买所得积分给买家
			case SETTLE_POINT_TO_BUYER: {
				_log.info("结算购买所得积分给买家: orderDetail-{}", orderDetail);
				final BigDecimal pointsTotal = orderDetail.getBuyPointTotal();
				if (pointsTotal == null || pointsTotal.compareTo(BigDecimal.ZERO) == 0) {
					_log.debug("结算购买所得积分给买家时发现积分总额为null或为0，订单详情为：{}", orderDetail);
					continue;
				}

				if (pointsTotal.compareTo(BigDecimal.ZERO) < 0) {
					throw new RuntimeExceptionX("结算购买所得积分给买家时发现积分总额<0，订单详情为：" + orderDetail);
				}

				final AddPointTradeTo addPointTradeTo = new AddPointTradeTo();
				addPointTradeTo.setAccountId(order.getUserId());
				addPointTradeTo.setPointLogType((byte) PointLogTypeDic.ORDER_SETTLE.getCode());
				addPointTradeTo.setChangedTitile("大卖网-商品购买积分");
				addPointTradeTo.setChangedDetail(orderDetail.getOnlineTitle() + orderDetail.getSpecName()//
						+ " x " + (orderDetail.getBuyCount() - orderDetail.getReturnCount()));
				addPointTradeTo.setOrderId(order.getId());
				addPointTradeTo.setOrderDetailId(orderDetail.getId());
				addPointTradeTo.setChangedPoint(orderDetail.getBuyPointTotal());
				_log.debug("添加一笔新的积分记录: 商品购买所得积分结算买家: addPointTradeTo-{}", addPointTradeTo);
				pntPointSvc.addPointTrade(addPointTradeTo);

				// 如果是首单支付，结算商品首单购买奖励积分
				if (orderDetail.getPaySeq() != null && orderDetail.getPaySeq() == 1) {
					addPointTradeTo.setPointLogType((byte) PointLogTypeDic.ORDER_SETTLE_FIRST_BUY.getCode());
					addPointTradeTo.setChangedTitile("大卖网-商品首单购买奖励积分");
					// 首单购买奖励积分 = 成本价 * 实际购买数量
					addPointTradeTo.setChangedPoint(orderDetail.getCostPrice()
							.multiply(BigDecimal.valueOf(orderDetail.getBuyCount() - orderDetail.getReturnCount())));
					_log.debug("添加一笔新的积分记录: 商品首单购买奖励积分结算买家: addPointTradeTo-{}", addPointTradeTo);
					pntPointSvc.addPointTrade(addPointTradeTo);
				}
				break;
			}
			// 释放卖家的已占用保证金
			case FREE_DEPOSIT_USED_OF_SELLER: {
				_log.info("释放卖家的已占用保证金");
				final AfcTradeMo tradeMo = new AfcTradeMo();
				tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_DEPOSIT_USED.getCode());
				tradeMo.setAccountId(order.getOnlineOrgId());
				// 获取订单详情真实购买数量
				final int realBuyCount = orderDetail.getBuyCount() - orderDetail.getReturnCount();
				// 总成本 = 真实购买数量 * 成本价格
				final BigDecimal costPriceTotal = orderDetail.getCostPrice().multiply(BigDecimal.valueOf(realBuyCount))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				// 需要释放的保证金额 = 真实购买数量 * 成本价格
				final BigDecimal depositUsed = costPriceTotal.multiply(BigDecimal.valueOf(realBuyCount));
				tradeMo.setTradeAmount(depositUsed);
				tradeMo.setTradeTitle("释放卖家的已占用保证金");
				addAccountTrade(taskMo, orderDetail, tradeMo, now);
				break;
			}
			// 结算利润给卖家(余额+)
			case SETTLE_PROFIT_TO_SELLER: {
				_log.info("结算利润给卖家(余额+)");
				final AfcTradeMo tradeMo = new AfcTradeMo();
				tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_SELLER.getCode());
				tradeMo.setAccountId(order.getOnlineOrgId());
				// 获取订单详情真实购买数量
				final int realBuyCount = orderDetail.getBuyCount() - orderDetail.getReturnCount();
				// 总成本 = 真实购买数量 * 成本价格
				final BigDecimal costPriceTotal = orderDetail.getCostPrice().multiply(BigDecimal.valueOf(realBuyCount));
				// 平台服务费
				BigDecimal platformServiceFee = null;
				// 实际成交金额
				BigDecimal actualAmount = null;
				// 旧数据实际成交金额为null
				if (orderDetail.getActualAmount() == null) {
					// 实际成交金额 = 购买金额(单价) * (购买数量 - 退货数量)
					actualAmount = orderDetail.getBuyPrice()
							.multiply(BigDecimal.valueOf(orderDetail.getBuyCount() - orderDetail.getReturnCount()));
					// 平台服务费 = 实际成交金额 * 平台服务费比例
					platformServiceFee = actualAmount.multiply(platformServiceFeeRatio);
				} else {
					actualAmount = orderDetail.getActualAmount();
					// 平台服务费 = 实际成交金额 * 平台服务费比例
					platformServiceFee = actualAmount.multiply(platformServiceFeeRatio);
				}
				// 卖家利润 = 实际成交金额 - 总成本 - 总返现金额 - 平台服务费
				final BigDecimal profitAmount = actualAmount.subtract(costPriceTotal)
						.subtract(orderDetail.getCashbackTotal()).subtract(platformServiceFee)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				tradeMo.setTradeAmount(profitAmount);
				tradeMo.setTradeTitle("结算利润给卖家(余额+)");
				addAccountTrade(taskMo, orderDetail, tradeMo, now);
				break;
			}
			// 结算平台服务费
			case SETTLE_PLATFORM_SERVICE_FEE: {
				_log.info("结算平台服务费");
				if (platformServiceFeeRatio.compareTo(BigDecimal.ZERO) == 0) {
					_log.warn("结算平台服务费时发现平台服务费费比例为0，订单详情为：{}", orderDetail);
					continue;
				}
				final AfcPlatformTradeMo tradeMo = new AfcPlatformTradeMo();
				tradeMo.setPlatformTradeType((byte) PlatformTradeTypeDic.CHARGE_SEVICE_FEE.getCode());
				BigDecimal platformServiceFee = null;
				if (orderDetail.getActualAmount() == null) {
					// 真实购买数量 = 购买数量 - 退货数量
					final Integer realBuyCount = orderDetail.getBuyCount() - orderDetail.getReturnCount();
					// 实际成交金额 = 真实购买数量 * 购买金额（单价）
					final BigDecimal actualAmount = orderDetail.getBuyPrice()
							.multiply(BigDecimal.valueOf(realBuyCount));
					// 平台服务费 = 实际成交金额 * 平台服务费比例
					platformServiceFee = actualAmount.multiply(platformServiceFeeRatio).setScale(4,
							BigDecimal.ROUND_HALF_UP);
				} else {
					// 平台服务费 = 实际成交金额 * 平台服务费比例
					platformServiceFee = orderDetail.getActualAmount().multiply(platformServiceFeeRatio).setScale(4,
							BigDecimal.ROUND_HALF_UP);
				}
				tradeMo.setTradeAmount(platformServiceFee);
				tradeMo.setOrderId(taskMo.getOrderId());
				tradeMo.setOrderDetailId(orderDetail.getId().toString());
				tradeMo.setModifiedTimestamp(now.getTime());
				afcPlatformTradeSvc.addTrade(tradeMo);
				break;
			}
			// 结算返佣金
			case SETTLE_COMMISSION: {
				_log.info("结算返佣金, 订单id为：{}, 订单详情id为: {}", orderDetail.getOrderId(), orderDetail.getId());
				if (orderDetail.getCashbackTotal() != null
						&& orderDetail.getCashbackTotal().compareTo(BigDecimal.ZERO) > 0) {
					_log.warn("结算反佣金时发现反现总额大于0，说明不是全返商品，订单详情为：{}", orderDetail);
					continue;
				}
				settleCommission(taskMo, order, orderDetail, now);
				break;
			}
			default:
				final String msg = "不能识别的结算任务类型";
				_log.error("{}: {}", msg, taskMo.getSubTaskType());
				throw new RuntimeException(msg);
			}
		}
	}

	/**
	 * 执行订单结算完成的任务(根据订单ID)
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void executeCompleteSettleTask(final Long orderId) {
		_log.info("执行订单结算完成的任务：orderId-{}", orderId);
		if (orderId == null) {
			final String msg = "参数不正确";
			_log.error(msg);
			throw new RuntimeException(msg);
		}
		final OrdOrderMo order = orderSvc.getById(orderId);
		if (order == null) {
			final String msg = "订单不存在";
			_log.error("{}: orderId-{}", msg, orderId);
			throw new RuntimeException(msg);
		}
		// 检查订单是否可结算
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}
		// 计算当前时间
		final Date now = new Date();
		if (taskSvc.existUnfinished(orderId.toString())) {
			final String msg = "订单仍然存在着尚未执行完成的结算任务，请稍后再试";
			_log.warn("{}: orderId-{}", msg, orderId);
			throw new RuntimeException(msg);
		}
		// 设置订单结算完成
		final int rowCount = orderSvc.completeSettle(now, orderId.toString());
		if (rowCount != 1) {
			final String msg = "设置结算完成失败，可能出现并发问题：orderId-{}";
			_log.error(msg, orderId);
			throw new RuntimeException(msg);
		}
		_log.info("执行订单结算完成的任务成功：orderId-{}", orderId);
	}

	/**
	 * 添加一笔账户交易
	 */
	private void addAccountTrade(final OrdTaskMo taskMo, final OrdOrderDetailMo orderDetail, final AfcTradeMo tradeMo,
			final Date now) {
		_log.info("addAccountTrade: taskMo-{} orderDetail-{} tradeMo-{}", taskMo, orderDetail, tradeMo);
		tradeMo.setTradeTime(now);
		if (tradeMo.getOrderId() == null) {
			tradeMo.setOrderId(taskMo.getOrderId());
		}
		if (tradeMo.getOrderDetailId() == null) {
			tradeMo.setOrderDetailId(orderDetail.getId().toString());
		}
		tradeMo.setTradeDetail(orderDetail.getOnlineTitle() + orderDetail.getSpecName() + " x " //
				+ (orderDetail.getBuyCount() - orderDetail.getReturnCount()));
		tradeMo.setOpId(0L);
		_log.info("执行添加一笔交易的参数为：{}", tradeMo);
		afcTradeSvc.addTrade(tradeMo);
	}

	/**
	 * 结算返佣 1. 如果本订单详情符合返佣条件，则返佣 2. 如果本订单详情的上家订单详情符合返佣条件，则返佣
	 */
	private void settleCommission(final OrdTaskMo taskMo, final OrdOrderMo order, final OrdOrderDetailMo orderDetail,
			final Date now) {
		// 判断订单详情板块类型是否为全返
		if (orderDetail.getSubjectType() == 1) {
			_log.info("**********************************************************************");
			_log.info("* 开始结算返佣");
			_log.info("**********************************************************************");
			_log.info("1. 如果本订单详情符合返佣条件，则返佣");
			handleCommission(orderDetail, order.getUserId(), now);
			_log.info("2. 如果本订单详情的上家订单详情符合返佣条件，则返佣");
			final OrdBuyRelationMo uplineBuyRelationConditions = new OrdBuyRelationMo();
			uplineBuyRelationConditions.setDownlineOrderDetailId(orderDetail.getId());
			_log.info("获取与上家的购买关系的参数：{}", uplineBuyRelationConditions);
			final OrdBuyRelationMo uplineBuyRelation = buyRelationSvc.getOne(uplineBuyRelationConditions);
			if (uplineBuyRelation == null) {
				_log.info("没有上家");
			} else {
				_log.info("获取与上家的购买关系的返回值：{}", uplineBuyRelation);
				_log.info("获取上家订单详情的参数：上家订单详情ID-{}", uplineBuyRelation.getUplineOrderDetailId());
				final OrdOrderDetailMo uplineOrderDetail = orderDetailSvc
						.getById(uplineBuyRelation.getUplineOrderDetailId());
				_log.info("获取上家订单详情的返回值：{}", uplineOrderDetail);
				handleCommission(uplineOrderDetail, uplineBuyRelation.getUplineUserId(), now);
			}
		}
	}

	/**
	 * 处理返佣(订单详情如果符合返佣条件，则返佣)
	 * 
	 * @param orderDetail 要判断是否返佣的订单详情
	 */
	private void handleCommission(final OrdOrderDetailMo orderDetail, final Long buyerId, final Date now) {
		_log.info("处理返佣(订单详情如果符合返佣条件，则返佣): orderDetail-{}", orderDetail);
		if (orderDetail == null || orderDetail.getCommissionSlot() != 0 //
				&& orderDetail.getReturnState() != ReturnStateDic.NONE.getCode()) {
			_log.info("订单详情返佣名额不为0/不是未退货状态，未满足返佣条件");
			return;
		}

		// 最晚签收时间=当前时间7天前
		final Date lastTime = new Date(now.getTime() - 86400000 * 7);
		_log.info("最晚签收时间不得大于{}", lastTime);

		_log.info("统计所有已签收超过7天的上下家订单详情的数量的参数: {}", orderDetail.getId());
		final int settledCount = buyRelationMapper.countSettledOfRelations(orderDetail.getId(), lastTime,
				ReturnStateDic.NONE);
		_log.info("统计所有已签收超过7天的上下家订单详情的数量的返回值: {}", settledCount);
		if (settledCount < 3) {
			_log.info("已签收数量不够3个，未满足返佣条件: 数量-{}", settledCount);
			return;
		} else if (settledCount > 3) {
			new RuntimeExceptionX("已签收数量超过3个，数据不正常: 数量-" + settledCount + " orderDetail-" + orderDetail);
		}

		_log.debug("修改订单详情的返佣状态为已返");
		final OrdOrderDetailMo modifyOrderDetail = new OrdOrderDetailMo();
		modifyOrderDetail.setId(orderDetail.getId());
		modifyOrderDetail.setCommissionState((byte) CommissionStateDic.RETURNED.getCode());
		orderDetailMapper.updateByPrimaryKeySelective(modifyOrderDetail);

		_log.debug("添加一笔返佣交易");
		final AfcTradeMo tradeMo = new AfcTradeMo();
		tradeMo.setAccountId(buyerId);
		tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_COMMISSION.getCode());
		tradeMo.setTradeAmount(orderDetail.getBuyPrice());
		tradeMo.setTradeTitle("大卖网络-拼全返佣金");
		tradeMo.setOrderId(orderDetail.getOrderId().toString());
		tradeMo.setOrderDetailId(orderDetail.getId().toString());
		addAccountTrade(null, orderDetail, tradeMo, now);
	}
}
