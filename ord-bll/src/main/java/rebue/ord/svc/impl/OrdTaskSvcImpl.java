package rebue.ord.svc.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.mapper.OrdTaskMapper;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.OrderSignInTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.wheel.NetUtils;

/**
 * 订单任务
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
public class OrdTaskSvcImpl extends MybatisBaseSvcImpl<OrdTaskMo, java.lang.Long, OrdTaskMapper> implements OrdTaskSvc {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(final OrdTaskMo mo) {
        _log.info("添加订单任务");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    private static final Logger _log = LoggerFactory.getLogger(OrdTaskSvcImpl.class);

    @Resource
    private OrdOrderSvc         orderSvc;
    @Resource
    private OrdSettleTaskSvc    settleTaskSvc;

    /**
     * 获取需要执行任务的ID列表(根据任务类型)
     */
    @Override
    public List<Long> getTaskIdsThatShouldExecute(final TaskExecuteStateDic executeState, final OrderTaskTypeDic taskType) {
        _log.info("根据订单任务状态和任务类型获取订单任务ID列表的参数为：executeState-{}， taskType-{}", executeState, taskType);
        return _mapper.selectThatShouldExecute((byte) taskType.getCode());
    }

    /**
     * 判断订单是否存在仍未完成的任务（包括未执行的和暂停执行的）
     */
    @Override
    public Boolean existUnfinished(final String orderId) {
        _log.info("判断订单是否存在仍未完成的任务（包括未执行的和暂停执行的）");
        return _mapper.existUnfinished(orderId, (byte) OrderTaskTypeDic.SETTLE.getCode());
    }

    /**
     * 执行订单自动签收任务
     * 
     * @param taskId
     *            任务ID
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void executeSignInOrderTask(final Long taskId) {
        _log.info("执行订单签收任务获取任务信息的参数为：{}", taskId);
        // 获取任务信息
        final OrdTaskMo ordTaskMo = _mapper.selectByPrimaryKey(taskId);
        _log.info("执行订单签收任务获取任务信息的返回值为:{}", ordTaskMo);
        if (ordTaskMo == null) {
            _log.error("执行订单签收任务时发现任务不存在，任务编号为：{}", taskId);
            throw new RuntimeException("任务不存在");
        }
        if (ordTaskMo.getExecuteState() != TaskExecuteStateDic.NONE.getCode()) {
            _log.error("执行订单签收任务时发现该任务不处于待执行状态，任务id为：{}", taskId);
            throw new RuntimeException("订单不处于待执行状态");
        }
        _log.info("执行订单签收任务根据订单编号查询订单状态的参数为：{}", ordTaskMo.getOrderId());
        // 根据订单编号查询订单状态
        final byte orderState = orderSvc.selectOrderStateByOrderCode(ordTaskMo.getOrderId());
        _log.info("执行订单签收任务根据订单编号查询订单状态的返回值为：{}", orderState);
        if (orderState == OrderStateDic.DELIVERED.getCode()) {
            try {
                final OrderSignInTo orderSignInTo = new OrderSignInTo();
                orderSignInTo.setOrderId(Long.parseLong(ordTaskMo.getOrderId()));
//                orderSignInTo.setOrderCode(ordTaskMo.getOrderId());
                orderSignInTo.setIp(NetUtils.getFirstIpOfLocalHost());
                orderSignInTo.setMac(NetUtils.getFirstMacAddrOfLocalHost());
                _log.info("执行订单签收任务订单签收的参数为：{}", ordTaskMo);
                // 订单签收
                final OrderSignInRo orderSignInRo = orderSvc.orderSignIn(orderSignInTo);
                _log.info("执行订单签收任务订单签收的返回值为：{}", orderSignInRo);
            } catch (final RuntimeException e) {
                _log.error("执行订单签收任务订单签收时出错", e);
                throw new RuntimeException("订单签收出错");
            }
        }
        final Date executeFactTime = new Date();
        _log.info("执行订单签收任务的参数为：{}", taskId);
        _mapper.executeSignInOrderTask(executeFactTime, taskId, (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
    }

    /**
     * 执行取消订单任务
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro cancelTask(final Long orderId, final OrderTaskTypeDic taskType) {
        _log.info("取消任务: orderId==={}, taskType==={}", orderId, taskType);
        final Ro ro = new Ro();

        // 根据交易类型和订单详情ID查找任务
        final OrdTaskMo condition = new OrdTaskMo();
        condition.setOrderId(orderId.toString());
        condition.setTaskType((byte) taskType.getCode());
        final OrdTaskMo task = getOne(condition);
        if (task == null) {
            final String msg = "找不到任务";
            _log.error("{}-{}", msg, orderId);
            ro.setResult(ResultDic.WARN);
            ro.setMsg(msg);
            return ro;
        }
        if (TaskExecuteStateDic.DONE.getCode() == task.getExecuteState()) {
            final String msg = "任务已被执行，不能取消";
            _log.error("{}-{}", msg, orderId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }
        if (TaskExecuteStateDic.CANCEL.getCode() == task.getExecuteState()) {
            final String msg = "任务已被取消，不能重复取消";
            _log.error("{}-{}", msg, orderId);
            ro.setResult(ResultDic.WARN);
            ro.setMsg(msg);
            return ro;
        }

        // 取消任务，rowCount为影响的行数
        final int rowCount = _mapper.cancelTask(task.getId(), task.getExecuteState(), TaskExecuteStateDic.CANCEL.getCode());
        if (rowCount != 1) {
            _log.info("影响行数为: {}", rowCount);
            final String msg = "任务取消失败: 可能出现并发问题";
            _log.error("{}-{}", msg, orderId);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        final String msg = "任务取消成功";
        _log.info(msg);
        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg(msg);
        return ro;
    }

    /**
     * 执行订单自动取消任务
     * 
     * @param taskId
     *            任务ID
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void executeCancelOrderTask(final Long taskId) {
        _log.info("执行订单取消任务获取任务信息的参数为：{}", taskId);
        // 获取任务信息
        final OrdTaskMo ordTaskMo = _mapper.selectByPrimaryKey(taskId);
        _log.info("执行订单取消任务获取任务信息的返回值为:{}", ordTaskMo);
        if (ordTaskMo == null) {
            _log.error("执行订单取消任务时发现任务不存在，任务编号为：{}", taskId);
            throw new RuntimeException("任务不存在");
        }
        if (ordTaskMo.getExecuteState() != TaskExecuteStateDic.NONE.getCode()) {
            _log.error("执行订单取消任务时发现该任务不处于待执行状态，任务id为：{}", taskId);
            throw new RuntimeException("订单不处于待执行状态");
        }
        _log.info("执行订单取消任务根据订单编号查询订单状态的参数为：{}", ordTaskMo.getOrderId());
        // 根据订单编号查询订单状态
        final byte orderState = orderSvc.selectOrderStateByOrderCode(ordTaskMo.getOrderId());
        _log.info("执行订单取消任务根据订单编号查询订单状态的返回值为：{}", orderState);
        if (orderState == OrderStateDic.ORDERED.getCode()) {
            try {
                final OrdOrderMo ordOrderMo = new OrdOrderMo();
                ordOrderMo.setId(Long.parseLong(ordTaskMo.getOrderId()));
//				ordOrderMo.setOrderCode(ordTaskMo.getOrderId());
                _log.info("执行订单取消任务订单的参数为：{}", ordTaskMo.getOrderId());
                // 订单取消
                final CancellationOfOrderRo cancelRo = orderSvc.cancellationOfOrder(ordOrderMo);
                _log.info("执行订单取消任务订单的返回值为：{}", cancelRo);
            } catch (final RuntimeException e) {
                _log.error("执行订单取消任务订单时出错", e);
                throw new RuntimeException("订单取消出错");
            }
        }
        final Date executeFactTime = new Date();
        _log.info("执行订单取消任务的参数为：{}", taskId);
        _mapper.executeSignInOrderTask(executeFactTime, taskId, (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
    }

    /**
     * 执行订单启动结算的任务
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public void executeStartSettleTask(final Long taskId) {
        _log.info("准备执行订单启动结算的任务: {}", taskId);
        if (taskId == null) {
            final String msg = "参数不正确";
            _log.error(msg);
            throw new RuntimeException(msg);
        }

        // 获取任务信息
        final OrdTaskMo taskMo = getById(taskId);
        if (taskMo == null) {
            final String msg = "执行订单启动结算的任务时发现任务不存在";
            _log.error("{}: taskId-{}", msg, taskId);
            throw new RuntimeException(msg);
        }
        _log.info("任务信息: {}", taskMo);
        if (TaskExecuteStateDic.NONE.getCode() != taskMo.getExecuteState().intValue()) {
            final String msg = "任务不是未执行状态，不能执行";
            _log.error("{}: {}", msg, taskMo);
            throw new RuntimeException(msg);
        }

        _log.info("开始执行任务-添加订单结算子任务");
        // 计算当前时间
        final Date now = new Date();
        try {
            settleTaskSvc.addSettleTasks(Long.valueOf(taskMo.getOrderId()));
        } catch (final RuntimeException e) {
            final String msg = "执行添加订单结算的子任务出现运行时异常";
            _log.error(msg, e);
            throw new RuntimeException(msg);
        }
        _log.info("将任务状态改为已经执行");
        final int rowCount = _mapper.done(now, taskMo.getId(), (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
        if (rowCount != 1) {
            _log.info("影响行数为: {}", rowCount);
            final String msg = "执行任务不成功: 可能出现并发问题";
            _log.error("{}-{}", msg, taskMo);
            throw new RuntimeException(msg);
        }
//        if (taskMo.getTradeType() == TradeTypeDic.SETTLE_COMMISSION.getCode()) {
//            _log.info("发送返佣结算完成通知");
//            final CommissionSettleDoneMsg msg = new CommissionSettleDoneMsg();
//            msg.setOrderDetailId(taskMo.getOrderDetailId());
//            msg.setSettleTime(now);
//            commissionSettleNotifyPub.send(msg);
//        }
    }

    /**
     * 执行订单结算的任务
     */
    @Override
    public void executeSettleTask(final Long taskId) {
        _log.info("准备执行订单结算的任务: {}", taskId);
        if (taskId == null) {
            final String msg = "参数不正确";
            _log.error(msg);
            throw new RuntimeException(msg);
        }

        // 获取任务信息
        final OrdTaskMo taskMo = getById(taskId);
        if (taskMo == null) {
            final String msg = "执行订单结算的任务时发现任务不存在";
            _log.error("{}: taskId-{}", msg, taskId);
            throw new RuntimeException(msg);
        }
        _log.info("任务信息: {}", taskMo);
        if (TaskExecuteStateDic.NONE.getCode() != taskMo.getExecuteState().intValue()) {
            final String msg = "任务不是未执行状态，不能执行";
            _log.error("{}: {}", msg, taskMo);
            throw new RuntimeException(msg);
        }

        _log.info("开始执行任务");
        // 计算当前时间
        final Date now = new Date();
        try {
            settleTaskSvc.executeSettleTask(taskMo);
        } catch (final RuntimeException e) {
            final String msg = "执行任务出现运行时异常";
            _log.error(msg, e);
            throw new RuntimeException(msg);
        }
        _log.info("将任务状态改为已经执行");
        final int rowCount = _mapper.done(now, taskMo.getId(), (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
        if (rowCount != 1) {
            _log.info("影响行数为: {}", rowCount);
            final String msg = "执行任务不成功: 可能出现并发问题";
            _log.error("{}-{}", msg, taskMo);
            throw new RuntimeException(msg);
        }
    }

    /**
     * 执行订单完成结算的任务
     */
    @Override
    public void executeCompleteSettleTask(final Long taskId) {
        _log.info("准备执行订单完成结算的任务: {}", taskId);
        if (taskId == null) {
            final String msg = "参数不正确";
            _log.error(msg);
            throw new RuntimeException(msg);
        }

        // 获取任务信息
        final OrdTaskMo taskMo = getById(taskId);
        if (taskMo == null) {
            final String msg = "执行订单完成结算的任务时发现任务不存在";
            _log.error("{}: taskId-{}", msg, taskId);
            throw new RuntimeException(msg);
        }
        _log.info("任务信息: {}", taskMo);
        if (TaskExecuteStateDic.NONE.getCode() != taskMo.getExecuteState().intValue()) {
            final String msg = "任务不是未执行状态，不能执行";
            _log.error("{}: {}", msg, taskMo);
            throw new RuntimeException(msg);
        }

        _log.info("开始执行任务");
        // 计算当前时间
        final Date now = new Date();
        try {
            settleTaskSvc.executeCompleteSettleTask(Long.parseLong(taskMo.getOrderId()));
        } catch (final RuntimeException e) {
            final String msg = "执行任务出现运行时异常";
            _log.error(msg, e);
            throw new RuntimeException(msg);
        }
        _log.info("将任务状态改为已经执行");
        final int rowCount = _mapper.done(now, taskMo.getId(), (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
        if (rowCount != 1) {
            _log.info("影响行数为: {}", rowCount);
            final String msg = "执行任务不成功: 可能出现并发问题";
            _log.error("{}-{}", msg, taskMo);
            throw new RuntimeException(msg);
        }
    }
}
