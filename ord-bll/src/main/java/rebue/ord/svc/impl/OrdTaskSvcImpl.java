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
import rebue.ord.mapper.OrdTaskMapper;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdTaskSvc;
import rebue.ord.to.OrderSignInTo;
import rebue.robotech.dic.TaskExecuteStateDic;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;
import rebue.wheel.NetUtils;

/**
 * 订单任务
 *
 * 在单独使用不带任何参数的 @Transactional 注释时，
 * propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED，
 * 而且事务不会针对受控异常（checked exception）回滚。
 *
 * 注意：
 * 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
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
    private OrdOrderSvc         ordOrderSvc;

    /**
     * 执行订单签收任务 Title: executeSignInOrderTask Description:
     *
     * @param executeFactTime
     * @param id
     * @param doneState
     * @param noneState
     * @return
     * @date 2018年5月21日 下午3:30:06
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void executeSignInOrderTask(final long id) {
        _log.info("执行订单签收任务获取任务信息的参数为：{}", id);
        // 获取任务信息
        final OrdTaskMo ordTaskMo = _mapper.selectByPrimaryKey(id);
        _log.info("执行订单签收任务获取任务信息的返回值为:{}", ordTaskMo);
        if (ordTaskMo == null) {
            _log.error("执行订单签收任务时发现任务不存在，任务编号为：{}", id);
            throw new RuntimeException("任务不存在");
        }
        if (ordTaskMo.getExecuteState() != TaskExecuteStateDic.NONE.getCode()) {
            _log.error("执行订单签收任务时发现该任务不处于待执行状态，任务id为：{}", id);
            throw new RuntimeException("订单不处于待执行状态");
        }
        _log.info("执行订单签收任务根据订单编号查询订单状态的参数为：{}", ordTaskMo.getOrderId());
        // 根据订单编号查询订单状态
        final byte orderState = ordOrderSvc.selectOrderStateByOrderCode(ordTaskMo.getOrderId());
        _log.info("执行订单签收任务根据订单编号查询订单状态的返回值为：{}", orderState);
        if (orderState == OrderStateDic.DELIVERED.getCode()) {
            try {
                final OrderSignInTo orderSignInTo = new OrderSignInTo();
                orderSignInTo.setOrderCode(ordTaskMo.getOrderId());
                orderSignInTo.setIp(NetUtils.getFirstIpOfLocalHost());
                orderSignInTo.setMac(NetUtils.getFirstMacAddrOfLocalHost());
                _log.info("执行订单签收任务订单签收的参数为：{}", ordTaskMo);
                // 订单签收
                final OrderSignInRo orderSignInRo = ordOrderSvc.orderSignIn(orderSignInTo);
                _log.info("执行订单签收任务订单签收的返回值为：{}", orderSignInRo);
            } catch (final RuntimeException e) {
                _log.error("执行订单签收任务订单签收时出错", e);
                throw new RuntimeException("订单签收出错");
            }
        }
        final Date executeFactTime = new Date();
        _log.info("执行订单签收任务的参数为：{}", id);
        _mapper.executeSignInOrderTask(executeFactTime, id, (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
    }

    // 执行取消订单任务
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void executeCancelOrderTask(final long id) {
        _log.info("执行订单取消任务获取任务信息的参数为：{}", id);
        // 获取任务信息
        final OrdTaskMo ordTaskMo = _mapper.selectByPrimaryKey(id);
        _log.info("执行订单取消任务获取任务信息的返回值为:{}", ordTaskMo);
        if (ordTaskMo == null) {
            _log.error("执行订单取消任务时发现任务不存在，任务编号为：{}", id);
            throw new RuntimeException("任务不存在");
        }
        if (ordTaskMo.getExecuteState() != TaskExecuteStateDic.NONE.getCode()) {
            _log.error("执行订单取消任务时发现该任务不处于待执行状态，任务id为：{}", id);
            throw new RuntimeException("订单不处于待执行状态");
        }
        _log.info("执行订单取消任务根据订单编号查询订单状态的参数为：{}", ordTaskMo.getOrderId());
        // 根据订单编号查询订单状态
        final byte orderState = ordOrderSvc.selectOrderStateByOrderCode(ordTaskMo.getOrderId());
        _log.info("执行订单取消任务根据订单编号查询订单状态的返回值为：{}", orderState);
        if (orderState == OrderStateDic.ORDERED.getCode()) {
            try {
                final OrdOrderMo ordOrderMo = new OrdOrderMo();
                ordOrderMo.setId(Long.parseLong(ordTaskMo.getOrderId()));
                ordOrderMo.setOrderCode(ordTaskMo.getOrderId());
                _log.info("执行订单取消任务订单的参数为：{}", ordTaskMo.getOrderId());
                // 订单取消
                final CancellationOfOrderRo cancelRo = ordOrderSvc.cancellationOfOrder(ordOrderMo);
                _log.info("执行订单取消任务订单的返回值为：{}", cancelRo);
            } catch (final RuntimeException e) {
                _log.error("执行订单取消任务订单时出错", e);
                throw new RuntimeException("订单取消出错");
            }
        }
        final Date executeFactTime = new Date();
        _log.info("执行订单取消任务的参数为：{}", id);
        _mapper.executeSignInOrderTask(executeFactTime, id, (byte) TaskExecuteStateDic.DONE.getCode(), (byte) TaskExecuteStateDic.NONE.getCode());
    }

    /**
     * 根据任务状态和任务类型查询任务数量
     */
    @Override
    public List<Long> getByExecutePlanTimeBeforeNow(final byte executeState, final byte taskType) {
        _log.info("根据任务状态查询和任务类型查询任务数量的参数为：executeState===={}， taskType========={}", executeState, taskType);
        return _mapper.selectByExecutePlanTimeBeforeNow(executeState, taskType);
    }
}
