package rebue.ord.svc.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.afc.mo.AfcTradeMo;
import rebue.afc.svr.feign.AfcTradeSvc;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.dic.SettleTaskExecuteStateDic;
import rebue.ord.dic.SettleTypeDic;
import rebue.ord.mapper.OrdSettleTaskMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.to.CancelSettleTaskTo;
import rebue.ord.to.ResumeSettleTaskTo;
import rebue.ord.to.SuspendSettleTaskTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 结算任务
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
public class OrdSettleTaskSvcImpl extends MybatisBaseSvcImpl<OrdSettleTaskMo, java.lang.Long, OrdSettleTaskMapper>
		implements OrdSettleTaskSvc {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdSettleTaskSvcImpl.class);

	@Resource
	private OrdSettleTaskSvc thisSvc;

	@Resource
	private OrdOrderSvc ordOrderSvc;

	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;

	@Resource
	private AfcTradeSvc afcTradeSvc;

	@Resource
	private OrdBuyRelationSvc ordBuyRelationSvc;

	/**
	 * 供应商结算计划执行时间
	 */
	@Value("${ord.supplier-settle-execute-plan-time}")
	private int supplierSettleExecutePlanTime;

	/**
	 * 已占用保证金结算计划执行时间
	 */
	@Value("${ord.occupied-deposit-settle-execute-plan-time}")
	private int occupiedDepositSettleExecutePlanTime;

	/**
	 * 已占用保证金结算计划执行时间
	 */
	@Value("${ord.seller-settle-execute-plan-time}")
	private int sellerSettleExecutePlanTime;

	/**
	 * 用户返现结算计划执行时间
	 */
	@Value("${ord.user-cashback-settle-execute-plan-time}")
	private int userCashbackSettleExecutePlanTime;

	/**
	 * 用户返佣结算计划执行时间
	 */
	@Value("${ord.user-commission-settle-execute-plan-time}")
	private int userCommissionSettleExecutePlanTime;

	/**
	 * 平台服务费结算计划执行时间
	 */
	@Value("${ord.platform-service-fee-settle-execute-plan-time}")
	private int platformServiceFeeExecutePlanTime;

	/**
	 * 平台服务费比例
	 */
	@Value("${ord.platform-service-fee-ratio}")
	private BigDecimal platformServiceFeeRatio;

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int add(OrdSettleTaskMo mo) {
		_log.info("添加结算任务");
		// 如果id为空那么自动生成分布式id
		if (mo.getId() == null || mo.getId() == 0) {
			mo.setId(_idWorker.getId());
		}
		return super.add(mo);
	}

	/**
	 * 暂停结算任务
	 * 
	 * @param to
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro suspendSettleTask(SuspendSettleTaskTo to) {
		_log.info("暂停结算任务的参数为：{}", to);
		Ro ro = new Ro();
		if (to.getOrderId() == null || to.getTradeType() == null) {
			_log.error("暂停结算任务出现参数错误");
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数错误");
			return ro;
		}

		OrdSettleTaskMo mo = new OrdSettleTaskMo();
		mo.setTradeType(to.getTradeType());
		mo.setOrderId(String.valueOf(to.getOrderId()));
		_log.info("暂停结算任务查询结算任务的参数为：{}", mo);
		OrdSettleTaskMo settleTaskMo = this.thisSvc.getOne(mo);
		_log.info("暂停结算任务查询结算任务的返回值为：{}", settleTaskMo);
		if (settleTaskMo == null) {
			_log.error("暂停结算任务时找不到订单编号为：{}、结算类型为：{}的任务", to.getOrderId(), to.getTradeType());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("找不到该结算任务");
			return ro;
		}

		int result = _mapper.updateSettleExecuteState(to.getTradeType(), to.getOrderId(),
				SettleTaskExecuteStateDic.NOT_PERFORMED.getCode(), SettleTaskExecuteStateDic.TIME_OUT.getCode());
		_log.info("暂停结算任务的返回值为：{}", result);
		if (result != 1) {
			_log.error("暂停结算任务出现错误，订单编号为：{}，结算类型为：{}", to.getOrderId(), to.getTradeType());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("暂停结算出现错误");
			return ro;
		}

		_log.info("暂停结算成功，订单编号为：{}，结算类型为：{}", to.getOrderId(), to.getTradeType());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("暂停结算成功");
		return ro;
	}

	/**
	 * 恢复结算任务
	 * 
	 * @param to
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro resumeSettleTask(ResumeSettleTaskTo to) {
		_log.info("恢复结算任务的参数为：{}", to);
		Ro ro = new Ro();
		if (to.getOrderId() == null || to.getTradeType() == null) {
			_log.error("恢复结算任务出现参数错误");
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数错误");
			return ro;
		}

		OrdSettleTaskMo mo = new OrdSettleTaskMo();
		mo.setTradeType(to.getTradeType());
		;
		mo.setOrderId(String.valueOf(to.getOrderId()));
		_log.info("恢复结算任务查询结算任务的参数为：{}", mo);
		OrdSettleTaskMo ordSettleTaskMo = thisSvc.getOne(mo);
		_log.info("恢复结算任务查询结算任务的返回值为：{}", ordSettleTaskMo);
		if (ordSettleTaskMo == null) {
			_log.error("恢复结算任务查询结算任务时发现没有该结算任务，订单编号为：{}、结算类型为：{}", to.getOrderId(), to.getTradeType());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("结算任务不存在");
			return ro;
		}

		int result = _mapper.updateSettleExecuteState(to.getTradeType(), to.getOrderId(),
				SettleTaskExecuteStateDic.TIME_OUT.getCode(), SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		_log.info("恢复结算任务的返回值为：{}", result);
		if (result != 1) {
			_log.error("恢复结算任务出现错误，订单编号为：{}、结算类型为：{}", to.getOrderId(), to.getTradeType());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("恢复结算任务出错");
			return ro;
		}

		_log.info("恢复结算任务成功，订单编号为：{}、结算类型为：{}", to.getOrderId(), to.getTradeType());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("恢复结算任务成功");
		return ro;
	}

	/**
	 * 取消结算任务
	 * 
	 * @param to
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro cancelSettleTask(CancelSettleTaskTo to) {
		_log.info("取消结算任务的参数为：{}", to);
		Ro ro = new Ro();
		if (to.getOrderId() == null || to.getTradeType() == null) {
			_log.error("取消结算任务出现参数错误：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数错误");
			return ro;
		}

		OrdSettleTaskMo mo = new OrdSettleTaskMo();
		mo.setTradeType(to.getTradeType());
		mo.setOrderId(String.valueOf(to.getOrderId()));
		_log.info("取消结算任务查询结算任务的参数为：{}", mo);
		OrdSettleTaskMo settleTaskMo = thisSvc.getOne(mo);
		_log.info("取消结算任务查询结算任务的返回值为：{}", settleTaskMo);
		if (settleTaskMo == null) {
			_log.error("取消结算任务查询结算任务时发现没有该结算任务，请求的参数为：{}", to);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("结算任务不存在");
			return ro;
		}

		int result = _mapper.cancelSettleTask(settleTaskMo.getId(), settleTaskMo.getExecuteState(),
				SettleTaskExecuteStateDic.CANCEL.getCode());
		_log.info("取消结算任务的返回值为：{}", result);
		if (result != 1) {
			_log.error("取消结算任务时出现错误，订单编号为：{}、结算类型为：{}", to.getOrderId(), to.getTradeType());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("取消结算任务出错");
			return ro;
		}

		_log.info("取消结算任务时成功，订单编号为：{}、结算类型为：{}", to.getOrderId(), to.getTradeType());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("取消结算任务成功");
		return ro;
	}

	/**
	 * 获取结算任务列表
	 * 
	 * @return
	 */
	@Override
	public List<Long> getTaskIdsThatShouldExecute() {
		return _mapper.selectByExecutePlanTimeBeforeNow();
	}

	/**
	 * 执行结算任务
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro executeSettleTask(Long id) {
		_log.info("执行结算任务的参数为：{}", id);
		Ro ro = new Ro();
		if (id == null) {
			_log.error("执行结算任务时发现参数不对");
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("参数不对");
			return ro;
		}

		_log.info("执行结算任务根据id获取任务信息的参数为：{}", id);
		OrdSettleTaskMo settleTaskMo = thisSvc.getById(id);
		_log.info("执行结算任务根据id获取任务信息的返回值为：{}", settleTaskMo);
		if (settleTaskMo == null) {
			_log.error("执行结算任务根据id查询任务信息时发现没有该任务信息，id为：{}", id);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("结算任务不存在");
			return ro;
		}

		if (TaskExecuteStateDic.NONE.getCode() != settleTaskMo.getExecuteState().intValue()) {
			_log.error("执行结算任务时发现该任务不处于未执行状态，id为：{}", id);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("任务不是未执行状态，不能执行");
			return ro;
		}

		_log.info("执行结算任务查询订单信息的参数为：{}", settleTaskMo.getOrderId());
		OrdOrderMo ordOrderMo = ordOrderSvc.getById(Long.parseLong(settleTaskMo.getOrderId()));
		_log.info("执行结算任务查询订单信息的返回值为：{}", ordOrderMo);

		if (ordOrderMo == null) {
			_log.error("执行结算任务时发现该订单不存在, 任务id为：{}", id);
			CancelSettleTaskTo cancelSettleTaskTo = new CancelSettleTaskTo();
			cancelSettleTaskTo.setOrderId(Long.parseLong(settleTaskMo.getOrderId()));
			cancelSettleTaskTo.setTradeType(settleTaskMo.getTradeType());
			return thisSvc.cancelSettleTask(cancelSettleTaskTo);
		}

		if (ordOrderMo.getOrderState().intValue() != OrderStateDic.SIGNED.getCode()) {
			_log.error("执行结算任务时发现该订单状态不处于已签收状态，任务id为：{}", id);
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("订单状态不处于已签收状态");
			return ro;
		}

		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(Long.parseLong(settleTaskMo.getOrderId()));
		_log.info("执行结算任务查询订单详情的参数为：{}", orderDetailMo);
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(orderDetailMo);
		_log.info("执行结算任务查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
		if (orderDetailList.size() == 0) {
			_log.error("执行结算任务时发现该订单详情不存在, 任务id为：{}", id);
			CancelSettleTaskTo cancelSettleTaskTo = new CancelSettleTaskTo();
			cancelSettleTaskTo.setOrderId(Long.parseLong(settleTaskMo.getOrderId()));
			cancelSettleTaskTo.setTradeType(settleTaskMo.getTradeType());
			return thisSvc.cancelSettleTask(cancelSettleTaskTo);
		}

		for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
			if (ordOrderDetailMo.getReturnState().intValue() == ReturnStateDic.RETURNING.getCode()) {
				_log.error("执行结算任务时发现订单详情处于退货中，任务id为：{}", id);
				ro.setResult(ResultDic.FAIL);
				ro.setMsg("订单详情处于退货中");
				return ro;
			}
		}

		// 计算当前时间
		final Date now = new Date();

		try {
			// 修改结算任务状态为已执行
			int updateSettleExecuteStateResult = _mapper.updateSettleExecuteState(settleTaskMo.getTradeType(),
					Long.parseLong(settleTaskMo.getOrderId()), SettleTaskExecuteStateDic.NOT_PERFORMED.getCode(),
					SettleTaskExecuteStateDic.ALREADY_PERFORMED.getCode());
			_log.info("执行结算任务改变任务执行状态的返回值为：{}", updateSettleExecuteStateResult);
			if (updateSettleExecuteStateResult != 1) {
				_log.error("执行结算任务改变任务执行状态失败，任务id为: {}", id);
				ro.setResult(ResultDic.FAIL);
				ro.setMsg("修改结算任务状态失败");
				return ro;
			}

			switch (SettleTypeDic.getItem(settleTaskMo.getTradeType())) {
			case WAIT_SETTLE:
				try {
					_log.info("执行结算任务添加结算任务的参数为：{}", settleTaskMo);
					Ro addSettleTaskRo = thisSvc.addSettleTask(settleTaskMo);
					_log.info("执行结算任务添加结算任务的返回值为：{}", addSettleTaskRo);
					if (addSettleTaskRo.getResult() != ResultDic.SUCCESS) {
						_log.info("执行结算任务添加结算任务失败，任务id为：{}", id);
						throw new RuntimeException("执行结算任务失败");
					}
					_log.info("执行结算任务添加结算任务成功，任务id为：{}", id);
					return addSettleTaskRo;
				} catch (RuntimeException e) {
					_log.error("执行结算任务添加结算任务时出现异常, {}", e);
					throw new RuntimeException("执行结算任务添加结算任务时出现异常");
				}

			case SETTLE_CASHBACK:
				_log.info("执行结算任务查询订单信息的参数为：{}", settleTaskMo.getOrderId());

				for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
					AfcTradeMo afcTradeMo = new AfcTradeMo();
					afcTradeMo.setAccountId(ordOrderMo.getUserId());
					afcTradeMo.setTradeType(settleTaskMo.getTradeType());
					afcTradeMo.setTradeAmount(ordOrderDetailMo.getCashbackTotal());
					afcTradeMo.setTradeTitle("用户订单返现");
					afcTradeMo.setTradeDetail("订单编号为：" + settleTaskMo.getOrderId());
					afcTradeMo.setTradeTime(now);
					afcTradeMo.setOrderId(settleTaskMo.getOrderId());
					afcTradeMo.setOrderDetailId(ordOrderDetailMo.getId().toString());
					afcTradeMo.setOpId(0L);
					afcTradeMo.setMac("不在获取MAC地址");
					afcTradeMo.setIp(settleTaskMo.getIp());
					_log.info("执行用户返现金结算任务添加一笔交易的参数为：{}", afcTradeMo);
					afcTradeSvc.addTrade(afcTradeMo);
				}
				_log.info("执行用户返现结算成功，任务id为：{}", id);
				ro.setResult(ResultDic.SUCCESS);
				ro.setMsg("执行用户返现结算成功");
				return ro;

			case SETTLE_COMMISSION:
				_log.info("执行返佣结算任务的参数为：{}", orderDetailList);
				for (final OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
					// 判断订单详情板块类型是否为全返
					if (ordOrderDetailMo.getSubjectType() == 1) {
						final OrdBuyRelationMo ordBuyRelationMo = new OrdBuyRelationMo();
						ordBuyRelationMo.setDownlineOrderDetailId(ordOrderDetailMo.getId());
						_log.info("添加结算任务查询订单购买关系的参数为：{}", ordBuyRelationMo);
						final OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(ordBuyRelationMo);
						_log.info("添加结算任务查询订单购买关系的返回值为：{}", ordBuyRelationMo);

						if (buyRelationResult != null) {
							// 根据购买关系查找上家定单详情,定单已签收且定单详情存在且不是退货状态才发起返佣任务
							_log.info("添加结算任务根据上家订单详情id查询详情信息的参数为：{}", buyRelationResult.getUplineOrderDetailId());
							final OrdOrderDetailMo uplineDetailResult = ordOrderDetailSvc
									.getById(buyRelationResult.getUplineOrderDetailId());
							_log.info("添加结算任务根据上家订单详情id查询详情信息的返回值为：{}", uplineDetailResult);

							_log.info("添加结算任务根据上家订单id查询订单信息的参数为：{}", buyRelationResult.getUplineOrderId());
							final OrdOrderMo uplineOrderResult = ordOrderSvc
									.getById(buyRelationResult.getUplineOrderId());
							_log.info("添加结算任务根据上家订单id查询订单信息的返回值为：{}", uplineOrderResult);

							_log.info("订单详情做为下家的购买关系记录：{}", uplineDetailResult);
							if (uplineDetailResult != null && uplineDetailResult.getReturnState() == 0
									&& uplineOrderResult.getOrderState() == 4) {
								// 获取上线买家商品详情的的下家购买关系记录，如果有2个且都已签收则执行返佣任务
								final OrdBuyRelationMo uplineBuyRelationMo = new OrdBuyRelationMo();
								uplineBuyRelationMo.setUplineOrderDetailId(buyRelationResult.getUplineOrderDetailId());
								uplineBuyRelationMo.setIsSignIn(true);
								_log.info("添加结算任务上家购买关系的参数为：{}", uplineBuyRelationMo);
								final List<OrdBuyRelationMo> uplineBuyRelationList = ordBuyRelationSvc
										.list(uplineBuyRelationMo);
								_log.info("添加结算任务上家购买关系的返回值为：{}", uplineBuyRelationList);

								if (uplineBuyRelationList.size() == 2) {
									String orderIds = buyRelationResult.getUplineOrderId() + ","
											+ uplineBuyRelationList.get(0).getDownlineOrderId() + ","
											+ uplineBuyRelationList.get(1).getDownlineOrderId();
									_log.info("添加结算任务查询订单签收时间的参数为：{}", orderIds);
									List<OrdOrderMo> signTimeList = ordOrderSvc.getOrderSignTime(orderIds);
									_log.info("添加结算任务查询订单签收时间的返回值为：{}", signTimeList);

									// 订单签收后七天时间戳
									Long receivedTime = signTimeList.get(0).getReceivedTime().getTime() + 86400000 * 7;
									// 当前时间戳
									Long thisTimestamp = System.currentTimeMillis();
									if (receivedTime >= thisTimestamp) {
										AfcTradeMo afcTradeMo = new AfcTradeMo();
										afcTradeMo.setAccountId(buyRelationResult.getUplineUserId());
										afcTradeMo.setTradeType(settleTaskMo.getTradeType());
										afcTradeMo.setTradeAmount(ordOrderDetailMo.getBuyPrice());
										afcTradeMo.setTradeTitle("大卖网络-上家返佣结算");
										afcTradeMo.setTradeDetail("订单编号为：" + settleTaskMo.getOrderId());
										afcTradeMo.setTradeTime(now);
										afcTradeMo.setOrderId(buyRelationResult.getUplineOrderId().toString());
										afcTradeMo.setOrderDetailId(
												buyRelationResult.getUplineOrderDetailId().toString());
										afcTradeMo.setOpId(0L);
										afcTradeMo.setMac("不在获取MAC地址");
										afcTradeMo.setIp(settleTaskMo.getIp());
										_log.info("执行上家返佣结算任务添加一笔交易的参数为：{}", afcTradeMo);
										afcTradeSvc.addTrade(afcTradeMo);
									}
								}
							}
						}
						_log.info("查询定单详情做为上家的购买关系");
						// 获取该详情下家购买关系，如有2个下家且已签收，并且没有退货则发起结算本家返佣任务
						final OrdBuyRelationMo downLineRelationParam = new OrdBuyRelationMo();
						downLineRelationParam.setUplineOrderDetailId(ordOrderDetailMo.getId());
						downLineRelationParam.setIsSignIn(true);
						_log.info("执行返佣结算任务查询订单详情作为上家时的购买关系参数为：{}", downLineRelationParam);
						final List<OrdBuyRelationMo> downLineBuyRelationList = ordBuyRelationSvc
								.list(downLineRelationParam);
						_log.info("执行返佣结算任务查询订单详情作为上家时的购买关系返回值为：{}", String.valueOf(downLineBuyRelationList));

						if (downLineBuyRelationList.size() == 2) {
							_log.info("执行返佣结算任务作为上家时查询第一个下家订单详情的参数为：{}",
									downLineBuyRelationList.get(0).getDownlineOrderDetailId());
							final OrdOrderDetailMo downLineDetailResult1 = ordOrderDetailSvc
									.getById(downLineBuyRelationList.get(0).getDownlineOrderDetailId());
							_log.info("执行返佣结算任务作为上家时查询第一个下家订单详情的返回值为：{}", downLineDetailResult1);

							_log.info("执行返佣结算任务作为上家时查询第二个下家订单详情的参数为：{}",
									downLineBuyRelationList.get(1).getDownlineOrderDetailId());
							final OrdOrderDetailMo downLineDetailResult2 = ordOrderDetailSvc
									.getById(downLineBuyRelationList.get(1).getDownlineOrderDetailId());
							_log.info("执行返佣结算任务作为上家时查询第二个下家订单详情的返回值为：{}", downLineDetailResult2);

							if (downLineDetailResult1 != null && downLineDetailResult2 != null
									&& downLineDetailResult1.getReturnState() == 0
									&& downLineDetailResult2.getReturnState() == 0) {
								String orderIds = ordOrderDetailMo.getOrderId() + ","
										+ downLineBuyRelationList.get(0).getDownlineOrderId() + ","
										+ downLineBuyRelationList.get(1).getDownlineOrderId();
								_log.info("添加结算任务查询订单签收时间的参数为：{}", orderIds);
								List<OrdOrderMo> signTimeList = ordOrderSvc.getOrderSignTime(orderIds);
								_log.info("添加结算任务查询订单签收时间的返回值为：{}", signTimeList);

								// 订单签收后七天时间戳
								Long receivedTime = signTimeList.get(0).getReceivedTime().getTime() + 86400000 * 7;
								// 当前时间戳
								Long thisTimestamp = System.currentTimeMillis();
								if (receivedTime >= thisTimestamp) {
									AfcTradeMo afcTradeMo = new AfcTradeMo();
									afcTradeMo.setAccountId(ordOrderMo.getUserId());
									afcTradeMo.setTradeType(settleTaskMo.getTradeType());
									afcTradeMo.setTradeAmount(ordOrderDetailMo.getBuyPrice());
									afcTradeMo.setTradeTitle("大卖网络-本家返佣结算");
									afcTradeMo.setTradeDetail("订单编号为：" + settleTaskMo.getOrderId());
									afcTradeMo.setTradeTime(now);
									afcTradeMo.setOrderId(ordOrderMo.getId().toString());
									afcTradeMo.setOrderDetailId(ordOrderDetailMo.getId().toString());
									afcTradeMo.setOpId(0L);
									afcTradeMo.setMac("不在获取MAC地址");
									afcTradeMo.setIp(settleTaskMo.getIp());
									_log.info("执行本家返佣结算任务添加一笔交易的参数为：{}", afcTradeMo);
									afcTradeSvc.addTrade(afcTradeMo);
								}
							}
						}
					}
				}
			case SETTLE_SUPPLIER:
				_log.info("执行供应商结算的参数为：{}", String.valueOf(orderDetailList));
				for (OrdOrderDetailMo detailMo : orderDetailList) {
					if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNED.getCode()
							&& detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						// 获取订单详情真实购买数量
						int realBuyCount = detailMo.getBuyCount() - detailMo.getReturnCount();
						// 订单详情总成本价 = 成本价 * 真实购买数量
						BigDecimal costPriceTotal = detailMo.getCostPrice().multiply(new BigDecimal(realBuyCount))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						AfcTradeMo afcTradeMo = new AfcTradeMo();
						afcTradeMo.setAccountId(detailMo.getSupplierId());
						afcTradeMo.setTradeType(settleTaskMo.getTradeType());
						afcTradeMo.setTradeAmount(costPriceTotal);
						afcTradeMo.setTradeTitle("大卖网络-供应商结算");
						afcTradeMo.setTradeDetail("订单详情id为：" + detailMo.getId());
						afcTradeMo.setTradeTime(now);
						afcTradeMo.setOrderId(ordOrderMo.getId().toString());
						afcTradeMo.setOrderDetailId(detailMo.getId().toString());
						afcTradeMo.setOpId(0L);
						afcTradeMo.setMac("不在获取MAC地址");
						afcTradeMo.setIp(settleTaskMo.getIp());
						_log.info("执行供应商结算任务添加一笔交易的参数为：{}", afcTradeMo);
						afcTradeSvc.addTrade(afcTradeMo);
					} else if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						_log.info("订单详情处于退货中，供应商结算暂停，任务id为：{}", id);
						throw new RuntimeException("订单详情处于退货中，供应商结算暂停");
					}
				}
			case SETTLE_SELLER:
				_log.info("执行卖家结算的参数为：{}", String.valueOf(orderDetailList));
				for (OrdOrderDetailMo detailMo : orderDetailList) {
					if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNED.getCode()
							&& detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						// 获取订单详情真实购买数量
						int realBuyCount = detailMo.getBuyCount() - detailMo.getReturnCount();
						// 购买总额 = 真实购买数量 * 购买金额（单价）
						BigDecimal buyPriceTotal = detailMo.getBuyPrice().multiply(new BigDecimal(realBuyCount))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						// 总成本 = 真实购买数量 * 成本价格
						BigDecimal costPriceTotal = detailMo.getCostPrice().multiply(new BigDecimal(realBuyCount))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						// 平台服务费 = 购买总额 * 平台服务费比例
						BigDecimal platformServiceFee = buyPriceTotal.multiply(platformServiceFeeRatio).setScale(4,
								BigDecimal.ROUND_HALF_UP);
						// 卖家利润 = 购买总额 - 总成本 - 总返现金额 - 平台服务费
						BigDecimal sellerProfit = buyPriceTotal.subtract(costPriceTotal)
								.subtract(detailMo.getCashbackTotal()).subtract(platformServiceFee)
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						AfcTradeMo afcTradeMo = new AfcTradeMo();
						afcTradeMo.setAccountId(ordOrderMo.getOnlineOrgId());
						afcTradeMo.setTradeType(settleTaskMo.getTradeType());
						afcTradeMo.setTradeAmount(sellerProfit);
						afcTradeMo.setTradeTitle("大卖网络-卖家利润结算");
						afcTradeMo.setTradeDetail("订单详情id为：" + detailMo.getId());
						afcTradeMo.setTradeTime(now);
						afcTradeMo.setOrderId(ordOrderMo.getId().toString());
						afcTradeMo.setOrderDetailId(detailMo.getId().toString());
						afcTradeMo.setOpId(0L);
						afcTradeMo.setMac("不在获取MAC地址");
						afcTradeMo.setIp(settleTaskMo.getIp());
						_log.info("执行卖家利润结算任务添加一笔交易的参数为：{}", afcTradeMo);
						afcTradeSvc.addTrade(afcTradeMo);
					} else if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						_log.info("订单详情处于退货中，供应商结算暂停，任务id为：{}", id);
						throw new RuntimeException("订单详情处于退货中，卖家利润结算暂停");
					}
				}
			case SETTLE_DEPOSIT_USED:
				_log.info("执行已占用保证金结算任务的参数为：{}", String.valueOf(orderDetailList));
				for (OrdOrderDetailMo detailMo : orderDetailList) {
					if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNED.getCode()
							&& detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						// 获取订单详情真实购买数量
						int realBuyCount = detailMo.getBuyCount() - detailMo.getReturnCount();
						// 总成本 = 真实购买数量 * 成本价格
						BigDecimal costPriceTotal = detailMo.getCostPrice().multiply(new BigDecimal(realBuyCount))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						// 需要释放的保证金额 = 真实购买数量 * 成本价格
						BigDecimal depositUsed = costPriceTotal.multiply(new BigDecimal(realBuyCount));
						AfcTradeMo afcTradeMo = new AfcTradeMo();
						afcTradeMo.setAccountId(ordOrderMo.getOnlineOrgId());
						afcTradeMo.setTradeType(settleTaskMo.getTradeType());
						afcTradeMo.setTradeAmount(depositUsed);
						afcTradeMo.setTradeTitle("大卖网络-卖家保证金结算");
						afcTradeMo.setTradeDetail("订单详情id为：" + detailMo.getId());
						afcTradeMo.setTradeTime(now);
						afcTradeMo.setOrderId(ordOrderMo.getId().toString());
						afcTradeMo.setOrderDetailId(detailMo.getId().toString());
						afcTradeMo.setOpId(0L);
						afcTradeMo.setMac("不在获取MAC地址");
						afcTradeMo.setIp(settleTaskMo.getIp());
						_log.info("执行卖家保证金结算任务添加一笔交易的参数为：{}", afcTradeMo);
						afcTradeSvc.addTrade(afcTradeMo);
					} else if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						_log.info("订单详情处于退货中，已占用保证金结算暂停，任务id为：{}", id);
						throw new RuntimeException("订单详情处于退货中，已占用保证金结算暂停");
					}
				}
			case SETTLE_PLATFORM_SERVICE_FEE:
				_log.info("执行平台服务费结算任务的参数为：{}", String.valueOf(orderDetailList));
				for (OrdOrderDetailMo detailMo : orderDetailList) {
					if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNED.getCode()
							&& detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						// 获取订单详情真实购买数量
						int realBuyCount = detailMo.getBuyCount() - detailMo.getReturnCount();
						// 购买总额 = 真实购买数量 * 购买价格（单价）
						BigDecimal buyPriceTotal = detailMo.getBuyPrice().multiply(new BigDecimal(realBuyCount))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						// 平台服务费 = 购买总额 * 平台服务费比例
						BigDecimal platformServiceFee = buyPriceTotal.multiply(platformServiceFeeRatio);
						AfcTradeMo afcTradeMo = new AfcTradeMo();
						afcTradeMo.setAccountId(ordOrderMo.getOnlineOrgId());
						afcTradeMo.setTradeType(settleTaskMo.getTradeType());
						afcTradeMo.setTradeAmount(platformServiceFee);
						afcTradeMo.setTradeTitle("大卖网络-平台服务费结算");
						afcTradeMo.setTradeDetail("订单详情id为：" + detailMo.getId());
						afcTradeMo.setTradeTime(now);
						afcTradeMo.setOrderId(ordOrderMo.getId().toString());
						afcTradeMo.setOrderDetailId(detailMo.getId().toString());
						afcTradeMo.setOpId(0L);
						afcTradeMo.setMac("不在获取MAC地址");
						afcTradeMo.setIp(settleTaskMo.getIp());
						_log.info("执行平台服务费结算任务添加一笔交易的参数为：{}", afcTradeMo);
						afcTradeSvc.addTrade(afcTradeMo);
					} else if (detailMo.getReturnState().intValue() != ReturnStateDic.RETURNING.getCode()) {
						_log.info("订单详情处于退货中，平台服务费结算暂停，任务id为：{}", id);
						throw new RuntimeException("订单详情处于退货中，平台服务费结算暂停");
					}
				}
			default:
				final String msg = "任务执行不支持此结算类型";
				_log.error(msg + ": {}", settleTaskMo.getTradeType());
				throw new RuntimeException(msg);
			}
		} catch (Exception e) {
			_log.error("执行结算任务时出现异常，{}", e);
			throw new RuntimeException("执行结算任务时出现异常");
		}
	}

	/**
	 * 添加结算任务（根据结算类型添加）
	 * 
	 * @param mo
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Ro addSettleTask(OrdSettleTaskMo mo) {
		_log.info("添加结算任务的参数为：{}", mo);
		Ro ro = new Ro();
		// 计算当前时间
		final Date now = new Date();

		OrdSettleTaskMo ordSettleTaskMo = new OrdSettleTaskMo();
		_log.info("========================添加合作者结算任务开始====================");
		ordSettleTaskMo.setId(_idWorker.getId());
		ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + supplierSettleExecutePlanTime));
		ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_SUPPLIER.getCode());
		ordSettleTaskMo.setOrderId(mo.getOrderId());
		ordSettleTaskMo.setIp(mo.getIp());
		_log.info("添加供应商结算任务的参数为：{}", ordSettleTaskMo);
		int addSupplierSettleResult = thisSvc.add(ordSettleTaskMo);
		_log.info("添加供应商结算任务的返回值为：{}", addSupplierSettleResult);
		if (addSupplierSettleResult != 1) {
			_log.error("添加供应商结算任务出现错误，任务id为：{}", mo.getId());
			ro.setResult(ResultDic.FAIL);
			ro.setMsg("添加供应商结算任务出错");
			return ro;
		}
		_log.info("添加供应商结算任务成功，任务id为：{}", mo.getId());

		ordSettleTaskMo = new OrdSettleTaskMo();
		ordSettleTaskMo.setId(_idWorker.getId());
		ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + occupiedDepositSettleExecutePlanTime));
		ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_DEPOSIT_USED.getCode());
		ordSettleTaskMo.setOrderId(mo.getOrderId());
		ordSettleTaskMo.setIp(mo.getIp());
		_log.info("添加已占用保证金结算任务的参数为：{}", ordSettleTaskMo);
		int addOccupiedDepositSettleResult = thisSvc.add(ordSettleTaskMo);
		_log.info("添加已占用保证金结算任务的返回值为：{}", addOccupiedDepositSettleResult);
		if (addOccupiedDepositSettleResult != 1) {
			_log.error("添加已占用保证金结算任务出现错误，任务id为：{}", mo.getId());
			throw new RuntimeException("添加已占用保证金结算任务出错");
		}
		_log.info("添加已占用保证金结算任务成功，任务id为：{}", mo.getId());

		ordSettleTaskMo = new OrdSettleTaskMo();
		ordSettleTaskMo.setId(_idWorker.getId());
		ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + sellerSettleExecutePlanTime));
		ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_SELLER.getCode());
		ordSettleTaskMo.setOrderId(mo.getOrderId());
		ordSettleTaskMo.setIp(mo.getIp());
		_log.info("添加卖家结算任务的参数为：{}", ordSettleTaskMo);
		int addSellerSettleResult = thisSvc.add(ordSettleTaskMo);
		_log.info("添加卖家结算任务的返回值为：{}", addSellerSettleResult);
		if (addSellerSettleResult != 1) {
			_log.error("添加卖家结算任务出现错误，任务id为：{}", mo.getId());
			throw new RuntimeException("添加卖家结算任务出错");
		}
		_log.info("添加卖家结算任务成功，任务id为：{}", mo.getId());

		_log.info("========================添加合作者结算任务结束====================");

		_log.info("========================添加用户结算任务开始====================");

		OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
		orderDetailMo.setOrderId(Long.parseLong(mo.getOrderId()));
		_log.info("添加结算任务查询订单号详情的参数为：{}", orderDetailMo);
		List<OrdOrderDetailMo> orderDetailList = ordOrderDetailSvc.list(orderDetailMo);
		_log.info("添加结算任务查询订单号详情的返回值为：{}", String.valueOf(orderDetailList));

		// 是否添加返佣任务
		Boolean isAddCommission = false;
		// 是否添加返现任务
		Boolean isCashback = false;
		for (OrdOrderDetailMo ordOrderDetailMo : orderDetailList) {
			if (ordOrderDetailMo.getSubjectType().intValue() == 1) {
				isAddCommission = true;
			}
			if (ordOrderDetailMo.getSubjectType().intValue() == 0) {
				isCashback = true;
			}
		}

		// 判断是否添加返佣任务
		if (isAddCommission) {
			ordSettleTaskMo = new OrdSettleTaskMo();
			ordSettleTaskMo.setId(_idWorker.getId());
			ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
			ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + userCommissionSettleExecutePlanTime));
			ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_COMMISSION.getCode());
			ordSettleTaskMo.setOrderId(mo.getOrderId());
			ordSettleTaskMo.setIp(mo.getIp());
			_log.info("添加用户返佣结算任务的参数为：{}", ordSettleTaskMo);
			int addUserCommissionSettleResult = thisSvc.add(ordSettleTaskMo);
			_log.info("添加用户返佣结算任务的返回值为：{}", addUserCommissionSettleResult);
			if (addUserCommissionSettleResult != 1) {
				_log.error("添加用户返佣结算任务出现错误，任务id为：{}", mo.getId());
				throw new RuntimeException("添加用户返佣结算任务出错");
			}
			_log.info("添加用户返佣结算任务成功，任务id为：{}", mo.getId());
		}

		// 判断是否添加返现任务
		if (isCashback) {
			ordSettleTaskMo = new OrdSettleTaskMo();
			ordSettleTaskMo.setId(_idWorker.getId());
			ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
			ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + userCashbackSettleExecutePlanTime));
			ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_CASHBACK.getCode());
			ordSettleTaskMo.setOrderId(mo.getOrderId());
			ordSettleTaskMo.setIp(mo.getIp());
			_log.info("添加用户返现中结算任务的参数为：{}", ordSettleTaskMo);
			int addUserCashbackSettleResult = thisSvc.add(ordSettleTaskMo);
			_log.info("添加用户返现中结算任务的返回值为：{}", addUserCashbackSettleResult);
			if (addUserCashbackSettleResult != 1) {
				_log.error("添加用户返现中结算任务出现错误，任务id为：{}", mo.getId());
				throw new RuntimeException("添加用户返现中结算任务出错");
			}
			_log.info("添加用户返现中结算任务成功，任务id为：{}", mo.getId());
		}

		_log.info("========================添加用户结算任务结束====================");

		ordSettleTaskMo = new OrdSettleTaskMo();
		ordSettleTaskMo.setId(_idWorker.getId());
		ordSettleTaskMo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		ordSettleTaskMo.setExecutePlanTime(new Date(now.getTime() + platformServiceFeeExecutePlanTime));
		ordSettleTaskMo.setTradeType((byte) SettleTypeDic.SETTLE_PLATFORM_SERVICE_FEE.getCode());
		ordSettleTaskMo.setOrderId(mo.getOrderId());
		ordSettleTaskMo.setIp(mo.getIp());
		_log.info("添加平台服务费结算任务的参数为：{}", ordSettleTaskMo);
		int addPlatformServiceFeeSettleResult = thisSvc.add(ordSettleTaskMo);
		_log.info("添加平台服务费结算任务的返回值为：{}", addPlatformServiceFeeSettleResult);
		if (addPlatformServiceFeeSettleResult != 1) {
			_log.error("添加平台服务费结算任务出现错误，任务id为：{}", mo.getId());
			throw new RuntimeException("添加平台服务费结算任务出错");
		}
		_log.info("添加平台服务费结算任务成功，任务id为：{}", mo.getId());
		ro.setResult(ResultDic.SUCCESS);
		ro.setMsg("添加结算任务成功");
		return ro;
	}
}
