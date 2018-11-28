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
import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.dic.SettleTaskTypeDic;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.robotech.dic.TaskExecuteStateDic;

/**
 * 结算任务
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdSettleTaskSvcImpl implements OrdSettleTaskSvc {

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
		calendar.add(Calendar.HOUR_OF_DAY, startSettleDelay.intValue());
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

		// 检查订单是否可结算
		// XXX 按理说在添加订单结算任务时就已经做过校验了，这里重复做一些校验更安全些，每个环节都要如此严谨
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}

		// 计算当前时间
		final Date now = new Date();

		_log.info("添加结算成本给供应商(余额+)的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_COST_TO_SUPPLIER, settleSupplierDelay);

		_log.info("添加结算返现金给买家的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_CASHBACK_TO_BUYER, settleCashbackToBuyerDelay);

		_log.info("添加释放卖家的已占用保证金的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.FREE_DEPOSIT_USED_OF_SELLER, freeDepositUsedOfSellerDelay);

		_log.info("添加结算利润给卖家(余额+)的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_PROFIT_TO_SELLER, settleProfitToSellerDelay);

		_log.info("添加结算平台服务费的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_PLATFORM_SERVICE_FEE, settlePlatformServiceFeeDelay);

		_log.info("添加结算返佣金的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.SETTLE_COMMISSION, settleCommissionDelay);

		_log.info("添加完成结算的任务");
		addSettleSubTask(orderId, now, SettleTaskTypeDic.COMPLETE_SETTLE, completeSettleDelay);
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
		calendar.add(Calendar.MINUTE, delay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());

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

		// 检查订单是否可结算
		// XXX 按理说在添加订单结算任务时就已经做过校验了，这里重复做一些校验更安全些，每个环节都要如此严谨
		if (!orderSvc.isSettleableOrder(order)) {
			final String msg = "订单不能结算";
			_log.error("{}: {}", msg, order);
			throw new RuntimeException(msg);
		}

		// 计算计划执行时间
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(order.getReceivedTime());
		calendar.add(Calendar.HOUR_OF_DAY,
				startSettleDelay.multiply(BigDecimal.valueOf(60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
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

		// 计算当前时间
		final Date now = new Date();
		// 结算任务类型
		final SettleTaskTypeDic settleTaskType = SettleTaskTypeDic.getItem(taskMo.getSubTaskType());

		// 如果是完成结算的任务
		if (SettleTaskTypeDic.COMPLETE_SETTLE == settleTaskType) {
			if (taskSvc.existUnfinished(taskMo.getOrderId())) {
				final String msg = "订单仍然存在着尚未执行完成的结算任务，请稍后再试";
				_log.warn("{}: orderId-{}", msg, taskMo.getOrderId());
				throw new RuntimeException(msg);
			}

			// 设置订单结算完成
			final int rowCount = orderSvc.completeSettle(now, taskMo.getOrderId());
			if (rowCount != 1) {
				final String msg = "设置结算完成失败，可能出现并发问题：orderId-{}";
				_log.error(msg, orderId);
				throw new RuntimeException(msg);
			}
			return;
		}

		_log.debug("遍历订单详情列表");
		for (final OrdOrderDetailMo orderDetail : orderDetailList) {
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
			// 释放卖家的已占用保证金
			case FREE_DEPOSIT_USED_OF_SELLER: {
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
				final AfcTradeMo tradeMo = new AfcTradeMo();
				tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_SELLER.getCode());
				tradeMo.setAccountId(order.getOnlineOrgId());
				// 获取订单详情真实购买数量
				final int realBuyCount = orderDetail.getBuyCount() - orderDetail.getReturnCount();
				// 总成本 = 真实购买数量 * 成本价格
				final BigDecimal costPriceTotal = orderDetail.getCostPrice().multiply(BigDecimal.valueOf(realBuyCount));
				// 平台服务费 = 实际成交金额 * 平台服务费比例
				final BigDecimal platformServiceFee = orderDetail.getActualAmount().multiply(platformServiceFeeRatio);
				// 卖家利润 = 实际成交金额 - 总成本 - 总返现金额 - 平台服务费
				final BigDecimal profitAmount = orderDetail.getActualAmount().subtract(costPriceTotal)
						.subtract(orderDetail.getCashbackTotal()).subtract(platformServiceFee)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				tradeMo.setTradeAmount(profitAmount);
				tradeMo.setTradeTitle("结算利润给卖家(余额+)");
				addAccountTrade(taskMo, orderDetail, tradeMo, now);
				break;
			}
			// 结算平台服务费
			case SETTLE_PLATFORM_SERVICE_FEE: {
				final AfcPlatformTradeMo tradeMo = new AfcPlatformTradeMo();
				tradeMo.setPlatformTradeType((byte) PlatformTradeTypeDic.CHARGE_SEVICE_FEE.getCode());
				// 平台服务费 = 实际成交金额 * 平台服务费比例
				final BigDecimal platformServiceFee = orderDetail.getActualAmount().multiply(platformServiceFeeRatio)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				tradeMo.setTradeAmount(platformServiceFee);

				tradeMo.setOrderId(taskMo.getOrderId());
				tradeMo.setOrderDetailId(orderDetail.getId().toString());
				tradeMo.setModifiedTimestamp(now.getTime());
				afcPlatformTradeSvc.addTrade(tradeMo);
				break;
			}
			// 结算返佣金
			case SETTLE_COMMISSION: {
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
	 * 添加一笔账户交易
	 */
	private void addAccountTrade(final OrdTaskMo taskMo, final OrdOrderDetailMo orderDetail, final AfcTradeMo tradeMo,
			final Date now) {
		tradeMo.setTradeTime(now);
		if (tradeMo.getOrderId() == null) {
			tradeMo.setOrderId(taskMo.getOrderId());
		}
		if (tradeMo.getOrderDetailId() == null) {
			tradeMo.setOrderDetailId(orderDetail.getId().toString());
		}
		tradeMo.setTradeDetail(orderDetail.getOnlineTitle() + orderDetail.getSpecName());
		tradeMo.setOpId(0L);
		_log.info("执行添加一笔交易的参数为：{}", tradeMo);
		afcTradeSvc.addTrade(tradeMo);
	}

	/**
	 * 结算返佣
	 */
	private void settleCommission(final OrdTaskMo taskMo, final OrdOrderMo order, final OrdOrderDetailMo orderDetail,
			final Date now) {
		// 判断订单详情板块类型是否为全返
		if (orderDetail.getSubjectType() == 1) {
			final OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
			ordBuyRelationMo.setDownlineOrderDetailId(orderDetail.getId());
			_log.info("添加结算任务查询订单购买关系的参数为：{}", ordBuyRelationMo);
			final OrdBuyRelationMo buyRelationResult = buyRelationSvc.getOne(ordBuyRelationMo);
			_log.info("添加结算任务查询订单购买关系的返回值为：{}", ordBuyRelationMo);

			if (buyRelationResult != null) {
				// 根据购买关系查找上家定单详情,定单已签收且定单详情存在且不是退货状态才发起返佣任务
				_log.info("添加结算任务根据上家订单详情id查询详情信息的参数为：{}", buyRelationResult.getUplineOrderDetailId());
				final OrdOrderDetailMo uplineDetailResult = orderDetailSvc
						.getById(buyRelationResult.getUplineOrderDetailId());
				_log.info("添加结算任务根据上家订单详情id查询详情信息的返回值为：{}", uplineDetailResult);

				_log.info("添加结算任务根据上家订单id查询订单信息的参数为：{}", buyRelationResult.getUplineOrderId());
				final OrdOrderMo uplineOrderResult = orderSvc.getById(buyRelationResult.getUplineOrderId());
				_log.info("添加结算任务根据上家订单id查询订单信息的返回值为：{}", uplineOrderResult);

				_log.info("订单详情做为下家的购买关系记录：{}", uplineDetailResult);
				if (uplineDetailResult != null && uplineDetailResult.getReturnState() == 0
						&& uplineOrderResult.getOrderState() == 4) {
					// 获取上线买家商品详情的的下家购买关系记录，如果有2个且都已签收则执行返佣任务
					final OrdBuyRelationMo uplineBuyRelationMo = new OrdBuyRelationMo();
					uplineBuyRelationMo.setUplineOrderDetailId(buyRelationResult.getUplineOrderDetailId());
					uplineBuyRelationMo.setIsSignIn(true);
					_log.info("添加结算任务上家购买关系的参数为：{}", uplineBuyRelationMo);
					final List<OrdBuyRelationMo> uplineBuyRelationList = buyRelationSvc.list(uplineBuyRelationMo);
					_log.info("添加结算任务上家购买关系的返回值为：{}", uplineBuyRelationList);

					if (uplineBuyRelationList.size() == 2) {
						final String orderIds = buyRelationResult.getUplineOrderId() + ","
								+ uplineBuyRelationList.get(0).getDownlineOrderId() + ","
								+ uplineBuyRelationList.get(1).getDownlineOrderId();
						_log.info("添加结算任务查询订单签收时间的参数为：{}", orderIds);
						final List<OrdOrderMo> signTimeList = orderSvc.getOrderSignTime(orderIds);
						_log.info("添加结算任务查询订单签收时间的返回值为：{}", signTimeList);

						// 订单签收后七天时间戳
						final Long receivedTime = signTimeList.get(0).getReceivedTime().getTime() + 86400000 * 7;
						// 当前时间戳
						final Long thisTimestamp = System.currentTimeMillis();
						if (receivedTime >= thisTimestamp) {
							final AfcTradeMo tradeMo = new AfcTradeMo();
							tradeMo.setAccountId(buyRelationResult.getUplineUserId());
							tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_COMMISSION.getCode());
							tradeMo.setTradeAmount(orderDetail.getBuyPrice());
							tradeMo.setTradeTitle("大卖网络-上家返佣结算");

							tradeMo.setOrderId(buyRelationResult.getUplineOrderId().toString());
							tradeMo.setOrderDetailId(buyRelationResult.getUplineOrderDetailId().toString());

							addAccountTrade(taskMo, orderDetail, tradeMo, now);
						}
					}
				}
			}
			_log.info("查询定单详情做为上家的购买关系");
			// 获取该详情下家购买关系，如有2个下家且已签收，并且没有退货则发起结算本家返佣任务
			final OrdBuyRelationMo downLineRelationParam = new OrdBuyRelationMo();
			downLineRelationParam.setUplineOrderDetailId(orderDetail.getId());
			downLineRelationParam.setIsSignIn(true);
			_log.info("执行返佣结算任务查询订单详情作为上家时的购买关系参数为：{}", downLineRelationParam);
			final List<OrdBuyRelationMo> downLineBuyRelationList = buyRelationSvc.list(downLineRelationParam);
			_log.info("执行返佣结算任务查询订单详情作为上家时的购买关系返回值为：{}", String.valueOf(downLineBuyRelationList));

			if (downLineBuyRelationList.size() == 2) {
				_log.info("执行返佣结算任务作为上家时查询第一个下家订单详情的参数为：{}", downLineBuyRelationList.get(0).getDownlineOrderDetailId());
				final OrdOrderDetailMo downLineDetailResult1 = orderDetailSvc
						.getById(downLineBuyRelationList.get(0).getDownlineOrderDetailId());
				_log.info("执行返佣结算任务作为上家时查询第一个下家订单详情的返回值为：{}", downLineDetailResult1);

				_log.info("执行返佣结算任务作为上家时查询第二个下家订单详情的参数为：{}", downLineBuyRelationList.get(1).getDownlineOrderDetailId());
				final OrdOrderDetailMo downLineDetailResult2 = orderDetailSvc
						.getById(downLineBuyRelationList.get(1).getDownlineOrderDetailId());
				_log.info("执行返佣结算任务作为上家时查询第二个下家订单详情的返回值为：{}", downLineDetailResult2);

				if (downLineDetailResult1 != null && downLineDetailResult2 != null
						&& downLineDetailResult1.getReturnState() == 0 && downLineDetailResult2.getReturnState() == 0) {
					final String orderIds = orderDetail.getOrderId() + ","
							+ downLineBuyRelationList.get(0).getDownlineOrderId() + ","
							+ downLineBuyRelationList.get(1).getDownlineOrderId();
					_log.info("添加结算任务查询订单签收时间的参数为：{}", orderIds);
					final List<OrdOrderMo> signTimeList = orderSvc.getOrderSignTime(orderIds);
					_log.info("添加结算任务查询订单签收时间的返回值为：{}", signTimeList);

					// 订单签收后七天时间戳
					final Long receivedTime = signTimeList.get(0).getReceivedTime().getTime() + 86400000 * 7;
					// 当前时间戳
					final Long thisTimestamp = System.currentTimeMillis();
					if (receivedTime >= thisTimestamp) {
						final AfcTradeMo tradeMo = new AfcTradeMo();
						tradeMo.setAccountId(order.getUserId());
						tradeMo.setTradeType((byte) TradeTypeDic.SETTLE_COMMISSION.getCode());
						tradeMo.setTradeAmount(orderDetail.getBuyPrice());
						tradeMo.setTradeTitle("大卖网络-本家返佣结算");
					}
				}
			}
		}
	}

}