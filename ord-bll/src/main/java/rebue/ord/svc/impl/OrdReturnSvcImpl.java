package rebue.ord.svc.impl;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rebue.afc.dic.TradeTypeDic;
import rebue.afc.svr.feign.AfcRefundSvc;
import rebue.afc.svr.feign.AfcSettleTaskSvc;
import rebue.afc.to.RefundTo;
import rebue.afc.to.TaskTo;
import rebue.onl.mo.OnlOnlinePicMo;
import rebue.onl.svr.feign.OnlOnlinePicSvc;
import rebue.ord.dic.AddReturnDic;
import rebue.ord.dic.AgreeToReturnDic;
import rebue.ord.dic.OrderStateDic;
import rebue.ord.dic.ReceivedAndRefundedDic;
import rebue.ord.dic.RejectReturnDic;
import rebue.ord.dic.ReturnApplicationStateDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.mapper.OrdReturnMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrderDetailRo;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.svc.OrdReturnPicSvc;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.ord.to.OrdReturnTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 用户退货信息
 *
 * 在单独使用不带任何参数的 @Transactional 注释时， propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED， 而且事务不会针对受控异常(checked exception)回滚。
 *
 * 注意： 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdReturnSvcImpl extends MybatisBaseSvcImpl<OrdReturnMo, java.lang.Long, OrdReturnMapper> implements OrdReturnSvc {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdReturnSvcImpl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(final OrdReturnMo mo) {
        _log.info("添加用户退货信息");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    @Resource
    private OrdReturnSvc      thisSvc;
    @Resource
    private OrdReturnPicSvc   ordReturnPicSvc;
    @Resource
    private OrdOrderSvc       oderSvc;

    /**
     */
    @Resource
    private OrdOrderDetailSvc orderDetailSvc;

    @Resource
    private OrdBuyRelationSvc ordBuyRelationSvc;

    /**
     */
    @Resource
    private AfcRefundSvc      refundSvc;

    @Resource
    private Mapper            dozerMapper;

    @Resource
    private OnlOnlinePicSvc   onlOnlinePicSvc;

    @Resource
    private AfcSettleTaskSvc  afcSettleTaskSvc;

    /**
     * 买家返款限制时间
     */
    @Value("${ord.return-limit-time}")
    private int               returnLimitTime;

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
    public AddReturnRo addReturn(final OrdOrderReturnTo to) {
        _log.info("添加用户退货信息的参数为：{}", to.toString());
        final AddReturnRo addReturnRo = new AddReturnRo();
        final long orderId = to.getOrderId();
        final long orderCode = to.getOrderCode();
        final long orderDetailId = to.getOrderDetailId();
        final long userId = to.getUserId();
        final int returnNum = to.getReturnNum();
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setOrderCode(String.valueOf(orderCode));
        orderMo.setUserId(userId);
        _log.info("查询订单信息的参数为：{}", orderMo);
        final List<OrdOrderMo> orderList = oderSvc.list(orderMo);

        if (orderList.size() == 0) {
            _log.error("添加退货信息出现订单不存在，订单编号为：{}", orderCode);
            addReturnRo.setResult(AddReturnDic.ORDER_NOT_EXIST);
            addReturnRo.setMsg("订单不存在");
            return addReturnRo;
        }
        // 获取现在时间,判断退货时间是否在签收后7天内
        final long nowTime = new Date().getTime();
        final Date signInTime = orderList.get(0).getReceivedTime();
        if (signInTime != null) {
            final long limitTime = signInTime.getTime() + returnLimitTime * 60 * 60 * 1000;
            if (limitTime < nowTime) {
                _log.error("超过收货后七天申请退款：{}", orderCode);
                final String returnReason = "超过收货后七天退款:" + to.getReturnReason();
                to.setReturnReason(returnReason);
            }
        }
        final byte orderState = orderList.get(0).getOrderState();
        if (orderState == OrderStateDic.CANCEL.getCode()) {
            _log.error("添加退货信息出现订单已取消，订单编号为：{}", orderCode);
            addReturnRo.setResult(AddReturnDic.ORDER_ALREADY_CANCEL);
            addReturnRo.setMsg("订单已取消");
            return addReturnRo;
        }
        if (orderState == OrderStateDic.ORDERED.getCode()) {
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
        final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
        orderDetailMo.setId(orderDetailId);
        orderDetailMo.setSpecName(to.getSpecName());
        _log.info("查询订单详情的参数为：{}", orderDetailMo);
        OrdOrderDetailMo orderDetailList = new OrdOrderDetailMo();
        try {
            orderDetailList = orderDetailSvc.getById(orderDetailId);
            _log.info("查询订单详情的返回值为：{}", String.valueOf(orderDetailList));
        } catch (final Exception e) {
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
        final int buyCount = orderDetailList.getBuyCount();
        if (buyCount == returnCount) {
            _log.error("添加退货信息出现订单详情退货数量等于购买数量，订单详情编号为：{}", orderDetailId);
            addReturnRo.setResult(AddReturnDic.GOODS_ALREADY_RETURN_FINISH);
            addReturnRo.setMsg("该商品已退完");
            return addReturnRo;
        }
        final int newReturnCount = returnCount + returnNum;
        if (buyCount < newReturnCount) {
            _log.error("添加退货信息出现订单详情退货数量大于订单购买数量，订单详情编号为：{}", orderDetailId);
            addReturnRo.setResult(AddReturnDic.NOT_RETURN_COUNT_GR_BUY_COUNT);
            addReturnRo.setMsg("退货数量不能大于购买数量");
            return addReturnRo;
        }
        final Date date = new Date();
        final long returnCode = _idWorker.getId();
        final OrdReturnMo ordReturnMo = new OrdReturnMo();
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
        final int insertReturnresult = _mapper.insertSelective(ordReturnMo);
        _log.info("添加退货信息的返回值为：{}", insertReturnresult);
        if (insertReturnresult < 1) {
            _log.error("添加退货信息出错，返回值为：{}", insertReturnresult);
            addReturnRo.setResult(AddReturnDic.ADD_RETURN_ERROR);
            addReturnRo.setMsg("添加退货信息出错");
            return addReturnRo;
        }
        final String[] returnPics = to.getReturnImg().split(",");
        for (final String returnPic : returnPics) {
            final OrdReturnPicMo ordReturnPicMo = new OrdReturnPicMo();
            ordReturnPicMo.setId(_idWorker.getId());
            ordReturnPicMo.setReturnId(ordReturnMo.getId());
            ordReturnPicMo.setPicPath(returnPic);
            final int insertReturnPicResult = ordReturnPicSvc.add(ordReturnPicMo);
            _log.error("添加退货图片返回值：{}" + insertReturnPicResult);
            if (insertReturnPicResult < 1) {
                _log.error("添加退货图片出错，返回值为：{}", insertReturnresult);
                throw new RuntimeException("添加退货图片失败");
            }
        }
        orderDetailMo.setReturnState((byte) 1);
        orderDetailMo.setId(orderDetailId);
        orderDetailMo.setReturnCount(newReturnCount);
        final int updateOrderDetailStateResult = orderDetailSvc.modify(orderDetailMo);
        if (updateOrderDetailStateResult < 1) {
            _log.error("修改订单详情状态失败，返回值为：{}", updateOrderDetailStateResult);
            throw new RuntimeException("修改订单详情状态失败");
        }
        _log.error("暂停返佣任务");
        final TaskTo taskTo = new TaskTo();
        taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
        taskTo.setOrderDetailId(String.valueOf(orderDetailId));
        final Ro ro = afcSettleTaskSvc.suspendTask(taskTo);
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
     * 查询分页列表信息
     */
    @Override
    public PageInfo<OrdReturnTo> selectReturnPageList(final OrdReturnTo qo, final int pageNum, final int pageSize) {
        _log.info("selectReturnPageList: qo-{}; pageNum-{}; pageSize-{}", qo, pageNum, pageSize);
        final PageInfo<OrdReturnTo> result = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> _mapper.selectReturnPageList(qo));
        _log.info("查询退货信息的返回值为：{}", result.getList());

        for (final OrdReturnTo item : result.getList()) {
            final OrdReturnPicMo mo = new OrdReturnPicMo();
            mo.setReturnId(item.getId());
            _log.info("查询退货图片的参数为：{}", mo);
            final List<OrdReturnPicMo> picList = ordReturnPicSvc.list(mo);
            _log.info("查询退货图片的返回值为：{}", picList);
            item.setPicList(picList);
        }
        _log.info("查询退货信息和图片的返回值为：{}", result.getList());
        return result;
    }

    /**
     * 拒绝退货
     * 
     * 1、判断退货编号、订单编号、订单详情ID、拒绝用户编号、拒绝原因等参数是否已传过来
     * 2、查询根据退货编号和订单编号查询退货信息 ，判断该退货编号是否存在和查询退货订单的申请状态是否处于待审核状态
     * 3、查询订单信息并判断订单状态是否处于已取消状态
     * 4、根据订单编号和订单详情ID查询订单详情信息并判断该订单详情的状态是否处于已退货的状态
     * 5、修改订单详情退货数量和状态 6、修改退货订单信息
     * 
     * @param record
     * @return
     */
    @SuppressWarnings("unused")
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public RejectReturnRo rejectReturn(final OrdReturnTo record) {
        _log.info("拒绝退货的请求参数为：{}", record);
        final RejectReturnRo rejectReturnRo = new RejectReturnRo();
        final long returnCode = record.getReturnCode();
        final long orderId = record.getOrderId();
        final long orderDetailId = record.getOrderDetailId();
        final long rejectOpId = record.getRejectOpId();
        final String rejectReason = record.getRejectReason();
        if (returnCode == 0 || orderId == 0 || orderDetailId == 0 || rejectOpId == 0 || rejectReason.equals("") || rejectReason == null || rejectReason.equals("null")) {
            _log.error("拒绝退货时出现参数不正确，退货编号为：{}", returnCode);
            rejectReturnRo.setResult(RejectReturnDic.PARAM_NOT_CORRECT);
            rejectReturnRo.setMsg("参数不正确");
            return rejectReturnRo;
        }
        _log.info("拒绝退货查询退货信息的参数为：{}", record);
        final List<OrdReturnTo> returnList = _mapper.selectReturnPageList(record);
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
        final OrdOrderMo orderMo = new OrdOrderMo();
        // orderMo.setOrderCode(String.valueOf(orderId));
        _log.info("拒绝退货查询订单信息的参数为：{}", orderMo);
        final OrdOrderMo orderList = oderSvc.getById(orderId);
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
        final OrdOrderDetailMo orderDetailMo = orderDetailSvc.getById(orderDetailId);
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
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setId(orderDetailMo.getId());
        detailMo.setReturnCount(orderDetailMo.getReturnCount() - returnList.get(0).getReturnCount());
        detailMo.setReturnState((byte) 0);
        _log.info("拒绝退货修改订单详情信息的参数为：{}", detailMo);
        final int updateOrderDetailStateResult = orderDetailSvc.modify(detailMo);
        _log.info("拒绝退货修改订单详情信息的返回值为：{}", updateOrderDetailStateResult);
        if (updateOrderDetailStateResult < 1) {
            _log.error("拒绝退货修改订单详情信息出错，退货编号为{}", returnCode);
            rejectReturnRo.setResult(RejectReturnDic.MODIFY_ORDER_DETAIL_ERROR);
            rejectReturnRo.setMsg("修改订单详情信息出错");
            return rejectReturnRo;
        }
        final Date date = new Date();
        record.setRejectTime(date);
        record.setFinishOpId(rejectOpId);
        record.setFinishTime(date);
        _log.info("拒绝退货的参数为：{}", record);
        final int refusedReturnResult = _mapper.refusedReturn(record);
        _log.info("拒绝退货的返回值为：{}", refusedReturnResult);
        if (refusedReturnResult != 1) {
            _log.error("拒绝退货时出现错误，退货编号为：{}", returnCode);
            throw new RuntimeException("拒绝退货出错");
        }
        // 恢复该详情返佣任务
        final TaskTo taskTo = new TaskTo();
        taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
        taskTo.setOrderDetailId(String.valueOf(orderDetailId));
        final Ro ro = afcSettleTaskSvc.resumeTask(taskTo);
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
     * 同意退款
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro agreeRefund(final OrdOrderReturnTo to) {
        _log.info("同意退款的参数为：{}", to);
        final Ro ro = new Ro();

        _log.debug("检查参数的正确性");
        if (to.getReturnId() == null || to.getOrderId() == null || to.getOrderDetailId() == null || to.getOpId() == null //
                || (to.getRefundAmount() == null && (to.getRefundAmount1() == null || to.getRefundAmount2() == null))) {
            final String msg = "参数错误";
            _log.error("{}: {}", msg, "没有传入退货ID/订单ID/退款金额/退款金额1(余额)/退款金额2(返现金)/订单详情ID/操作人");
            ro.setResult(ResultDic.PARAM_ERROR);
            ro.setMsg(msg);
            return ro;
        }

        // 是否自动计算退款: 传入退款金额参数则为自动计算退款，否则为自定义退款
        final boolean isAutoCalcRefund = to.getRefundAmount() != null;
        _log.debug(isAutoCalcRefund ? "自动计算退款: 根据用户输入退款金额自动计算退回的返现金和余额" : "自定义退款: 用户具体指定退回返现金和余额的数字");
        // 如果是自定义退款，退款金额 = 退款金额1(余额) + 退款金额2(返现金)
        if (!isAutoCalcRefund) {
            to.setRefundAmount(to.getRefundAmount1().add(to.getRefundAmount2()));
        }

        final OrdOrderMo order = oderSvc.getById(to.getOrderId());
        if (order == null) {
            final String msg = "订单不存在";
            _log.error("{}: {}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }
        _log.info("获取到退款的订单信息：{}", order);

        if (order.getOrderState() == OrderStateDic.CANCEL.getCode() || order.getOrderState() == OrderStateDic.ORDERED.getCode()
                || order.getOrderState() == OrderStateDic.SETTLED.getCode()) {
            final String msg = "订单处于已取消/未支付/已结算三种状态之一，不能退款";
            _log.error("{}：{}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        final OrdOrderDetailMo detail = orderDetailSvc.getById(to.getOrderDetailId());
        _log.info("获取到退款的订单详情信息：{}", detail);
        if (detail == null) {
            final String msg = "订单详情不存在";
            _log.error("{}：{}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        if (ReturnStateDic.RETURNING.getCode() != detail.getReturnState()) {
            final String msg = "订单详情的状态并未在退货中";
            _log.error("{}：{}", msg, detail);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        final OrdReturnMo returnInfo = thisSvc.getById(to.getReturnId());
        _log.info("获取到退货单信息：{}", returnInfo);
        if (returnInfo == null) {
            final String msg = "退货单不存在";
            _log.error("{}：{}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        if (ReturnApplicationStateDic.PENDING_REVIEW.getCode() != returnInfo.getApplicationState()) {
            final String msg = "退货单不处于待审核状态，不能审核";
            _log.error("{}：{}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        // 订单已经退款总额
        final BigDecimal refundedTotal = order.getReturnTotal();
        // 订单退款总额 = 订单已经退款总额 + 本次退款金额 + 扣除补偿金
        final BigDecimal refundTotal = refundedTotal.add(to.getRefundAmount()).add(to.getDeductAmount());
        _log.debug("订单已经退款总额：{}, 本次退款金额: {}", refundedTotal, to.getRefundAmount());
        _log.debug("订单退款总额 = 订单已经退款总额 + 本次退款金额 + 扣除补偿金：{}", refundTotal);
        _log.debug("订单实际支付金额：{}", order.getRealMoney());
        if (refundTotal.compareTo(order.getRealMoney()) > 0) {
            final String msg = "订单退款总额大于订单实际支付金额，不能退款";
            _log.error("{}：{}", msg, to);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        // 修改订单退款金额
        // 如果订单退货总额==订单实际支付金额，修改订单状态为取消
        Byte orderState = null;
        if (refundTotal.compareTo(order.getRealMoney()) == 0) {
            _log.debug("订单退货总额==订单实际支付金额，修改订单状态为取消");
            orderState = (byte) OrderStateDic.CANCEL.getCode();
        }
        // 修改订单退款金额
        final int modifyRefundRowCount = oderSvc.modifyRefund(refundTotal, orderState, order.getId(), refundedTotal);
        if (modifyRefundRowCount != 1) {
            final String msg = "修改订单退款金额出错，可能出现并发问题";
            _log.error("{}：退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", msg, refundTotal, orderState, order.getId(), refundedTotal);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }

        // 退货数量为空，则表示仅退款，不为空，则表示退货退款
        if (to.getReturnNum() != null && to.getReturnNum() > 0) {
            // 订单详情已退货数量
            final Integer returnedCount = detail.getReturnCount();
            // 订单详情退货总数 = 已退货数量 + 本次退货数量
            final int returnTotal = returnedCount + to.getReturnNum();
            // 退货状态
            Byte returnState = null;
            // 如果退货总数==购买数量，则为已退货
            if (returnTotal == detail.getBuyCount()) {
                returnState = (byte) ReturnStateDic.RETURNED.getCode();
            }
            // 如果退货总数<购买数量，则为部分退货
            else if (returnTotal < detail.getBuyCount()) {
                returnState = (byte) ReturnStateDic.PART_RETURNED.getCode();
            } else {
                final String msg = "退货总数不能大于购买数量";
                _log.error("{}：退款金额-{} 订单状态-{} 订单ID-{} 订单已退总额-{}", msg, refundTotal, orderState, order.getId(), refundedTotal);
                throw new RuntimeException(msg);
            }
            // 之前的返现金总额
            final BigDecimal oldCashbackTotal = detail.getCashbackTotal() == null ? BigDecimal.ZERO : detail.getCashbackTotal();
            // 退货后的返现金总额 = 返现金额 * (购买数量 - 退货总数)
            final BigDecimal newCashbackTotal = detail.getCashbackAmount().multiply(BigDecimal.valueOf(detail.getBuyCount() - returnTotal));
            final int modifyReturnRowCount = orderDetailSvc.modifyReturn(returnTotal, newCashbackTotal, returnState, //
                    detail.getId(), returnedCount, oldCashbackTotal);
            if (modifyReturnRowCount != 1) {
                final String msg = "修改订单详情的退货情况出错，可能出现并发问题";
                _log.error("{}：退货总数-{}, 新的返现金总额-{}, 退货状态-{}, where-订单详情ID-{}, where-之前的已退货数量-{}, where-退货之前的返现金总额-{}", msg, returnTotal, newCashbackTotal, returnState,
                        detail.getId(), returnedCount, oldCashbackTotal);
                throw new RuntimeException(msg);
            }

            // 修改退货单
            final Date now = new Date();
            // 本次退款总额 = 本次退款金额 + 本次扣除补偿金额
            final BigDecimal currentRefundTotal = to.getRefundAmount().add(to.getDeductAmount());
            _log.info("确认退款修改退货单的参数为：本次退款总额-{} 扣除补偿金额-{} 操作人ID-{} 操作时间-{} 退货ID-{}", currentRefundTotal, //
                    to.getDeductAmount(), to.getOpId(), now, to.getReturnId());
            final int confirmRefundRowCount = _mapper.confirmRefund(currentRefundTotal, //
                    to.getDeductAmount(), (byte) ReturnApplicationStateDic.TURNED.getCode(), to.getOpId(), now, to.getReturnId());
            _log.info("同意退款修改退货信息的返回值为：{}", confirmRefundRowCount);

        }

        _log.info("获取该定单详情做为下家的购买关系记录");
        final OrdBuyRelationMo buyRelationParamMo = new OrdBuyRelationMo();
        buyRelationParamMo.setDownlineOrderDetailId(detail.getId());
        final OrdBuyRelationMo buyRelationResult = ordBuyRelationSvc.getOne(buyRelationParamMo);
        _log.info("获取该定单详情做为下家的购买关系记录为:{}", buyRelationResult);
        if (buyRelationResult == null) {
            _log.info("该定单详情做为下家的购买关系记录为空");
        } else {
            _log.info("删除该定单详情做为下家的购买关系记录");
            final int delResult = ordBuyRelationSvc.del(buyRelationResult.getId());
            _log.info("删除该定单详情的购买关系记录的返回值为：{}", delResult);
            if (delResult != 1) {
                _log.info("删除该定单详情做为下家的购买关系记录失败");
                throw new RuntimeException("删除该定单详情做为下家的购买关系记录失败");
            }
            _log.info("更新该定单详情上家的返佣名额");
            final OrdOrderDetailMo ordDetailMo = orderDetailSvc.getById(buyRelationResult.getUplineOrderDetailId());
            if (ordDetailMo == null) {
                _log.info("该定单上家的定单详情为空");
                throw new RuntimeException("该定单上家的定单详情为空");
            }
            final byte commissionSlot = ordDetailMo.getCommissionSlot();
            ordDetailMo.setCommissionSlot((byte) (commissionSlot + 1));
            final int updateUplineDetailResult = orderDetailSvc.modify(ordDetailMo);
            if (updateUplineDetailResult != 1) {
                _log.info("更新该定单详情上家的返佣名额失败");
                throw new RuntimeException("更新该定单详情上家的返佣名额失败");
            }
        }
        _log.info("获取该定单详情下家购买关系");
        final OrdBuyRelationMo buyRelationParamMo1 = new OrdBuyRelationMo();
        buyRelationParamMo1.setUplineOrderDetailId(detail.getId());
        final List<OrdBuyRelationMo> buyRelationResult1 = ordBuyRelationSvc.list(buyRelationParamMo1);
        if (buyRelationResult1.size() == 0) {
            _log.info("该定单详情下家购买关系为空");
        } else {
            for (int i = 0; i < buyRelationResult1.size(); i++) {
                _log.info("重新匹配该定单详情下家购买关系，购买关系ID：" + buyRelationResult1.get(i).getId());
                _log.info("全返商品添加购买关系");
                final long userId = buyRelationResult1.get(i).getDownlineUserId();
                final long onlineId = detail.getOnlineId();
                final BigDecimal buyPrice = detail.getBuyPrice();
                final long downLineDetailId = detail.getId();
                final long downLineOrderId = detail.getOrderId();
                final String matchBuyRelationResult = ordBuyRelationSvc.matchBuyRelation(userId, onlineId, buyPrice, downLineDetailId, downLineOrderId);
                _log.info(matchBuyRelationResult);
                _log.info("删除购买关系：" + buyRelationResult1.get(i).getId());
                final int delResult = ordBuyRelationSvc.del(buyRelationResult1.get(i).getId());
                if (delResult != 1) {
                    _log.info("删除购买关系失败：" + buyRelationResult1.get(i).getId());
                    throw new RuntimeException("删除购买关系失败");
                }
            }
        }

//        // 取消该详情返佣任务
//        _log.info("取消该详情返佣任务");
//        final TaskTo taskTo = new TaskTo();
//        taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
//        taskTo.setOrderDetailId(String.valueOf(orderDetailId));
//        final Ro cancelTaskRo = afcSettleTaskSvc.cancelTask(taskTo);
//        _log.info("取消返佣任务返回值:{}", cancelTaskRo);
//        if (cancelTaskRo.getResult().getCode() == ResultDic.FAIL.getCode()) {
//            _log.info("取消返佣任务失败");
//            throw new RuntimeException("取消返佣任务失败");
//        }

        // 退款
        final RefundTo refundTo = new RefundTo();
        refundTo.setOrderId(String.valueOf(to.getOrderId()));
        refundTo.setOrderDetailId(String.valueOf(to.getReturnId()));
        refundTo.setBuyerAccountId(order.getUserId());
        refundTo.setTradeTitle("用户退货-退款");
        refundTo.setTradeDetail(detail.getOnlineTitle());
        refundTo.setRefundAmount(to.getRefundAmount());
//        refundTo.setReturnBalanceToBuyer(returnAmount1);
//        refundTo.setReturnCashbackToBuyer(returnCashbackToBuyer);
        refundTo.setOpId(to.getOpId());
        refundTo.setIp(to.getIp());
        _log.info("退款的参数为：{}", refundTo);
        final Ro refundRo = refundSvc.refund(refundTo);
        _log.info("退款返回值为：{}", refundRo);
        if (ResultDic.SUCCESS != refundRo.getResult()) {
            _log.error("退款出错，收到错误信息：{}", refundRo);
            throw new RuntimeException(refundRo.getMsg());
        }

        ro.setResult(ResultDic.SUCCESS);
        ro.setMsg("退款成功");
        return ro;
    }

    /**
     * 同意退货
     * 1、判断参数是否齐全
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AgreeToReturnRo agreeReturn(final OrdOrderReturnTo to) {
        final AgreeToReturnRo agreeToReturnRo = new AgreeToReturnRo();
        final Long returnCode = to.getReturnCode();
        final Long orderId = to.getOrderCode();
        final Long orderDetailId = to.getOrderDetailId();
        final Long reviewOpId = to.getOpId();
        final BigDecimal bd = new BigDecimal("0");
        final BigDecimal returnAmount1 = new BigDecimal(String.valueOf(to.getReturnAmount1()));
        final BigDecimal returnAmount2 = new BigDecimal(String.valueOf(to.getReturnAmount2()));
        final BigDecimal subtractCashback = new BigDecimal(String.valueOf(to.getSubtractCashback()));
        final BigDecimal totalReturn = new BigDecimal(returnAmount1.add(returnAmount2).doubleValue());
        if (returnCode == null || orderId == null || orderDetailId == null || reviewOpId == null || subtractCashback.compareTo(bd) == -1) {
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
        final OrdReturnMo ordReturnMo = new OrdReturnMo();
        ordReturnMo.setReturnCode(returnCode);
        _log.info("同意退货查询退货信息的参数为：{}", returnCode);
        final List<OrdReturnMo> returnList = _mapper.selectSelective(ordReturnMo);
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
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setOrderCode(String.valueOf(orderId));
        _log.info("同意退货查询订单信息的参数为：{}", orderId);
        final List<OrdOrderMo> orderList = oderSvc.list(orderMo);
        _log.info("同意退货查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("同意退货查询订单信息时出现没有该订单，退货编号为：{}", returnCode);
            agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_NOT_EXIST);
            agreeToReturnRo.setMsg("没有找到该订单信息");
            return agreeToReturnRo;
        }
        if (orderList.get(0).getOrderState() == OrderStateDic.CANCEL.getCode() || orderList.get(0).getOrderState() == OrderStateDic.ORDERED.getCode()) {
            _log.error("同意退货时发现该订单未支付或已取消，退货编号为：{}", returnCode);
            agreeToReturnRo.setResult(AgreeToReturnDic.ORDER_NOT_PAY_OR_ALREADY_CANCEL);
            agreeToReturnRo.setMsg("该订单未支付或已取消");
            return agreeToReturnRo;
        }
        final OrdOrderDetailMo orderDetailMo = new OrdOrderDetailMo();
        // orderDetailMo.setOrderId(orderId);
        // orderDetailMo.setId(orderDetailId);
        _log.info("同意退货查询订单详情信息的参数为：{}", orderDetailMo.toString());
        final OrdOrderDetailMo orderDetailList = orderDetailSvc.getById(orderDetailId);
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
        final int modifyReturnAmountResult = oderSvc.modifyReturnAmountByorderCode(orderMo);
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
        final Integer newReturnCount = +orderDetailReturnCount;
        final BigDecimal newCashBackTotal = new BigDecimal(cashBackTotal.subtract(totalReturn).doubleValue());
        orderDetailMo.setReturnCount(newReturnCount);
        orderDetailMo.setCashbackTotal(newCashBackTotal);
        _log.info("同意退货修改订单详情退货数量和返现总额的参数为：{}", orderDetailMo);
        final int modifyReturnCountAndCashBackTotalResult = orderDetailSvc.modifyReturn(orderDetailMo);
        _log.info("同意退货修改订单详情退货数量和返现总额的返回值为：{}", modifyReturnCountAndCashBackTotalResult);
        if (modifyReturnCountAndCashBackTotalResult != 1) {
            _log.error("同意退货修改订单详情退货数量和返现总额时出现错误，退货编号为：{}", returnCode);
            throw new RuntimeException("修改退货数量和返现总额出错");
        }
        final Date date = new Date();
        ordReturnMo.setReviewOpId(reviewOpId);
        ordReturnMo.setReviewTime(date);
        ordReturnMo.setReturnRental(totalReturn);
        ordReturnMo.setReturnAmount1(returnAmount1);
        ordReturnMo.setReturnAmount2(returnAmount2);
        ordReturnMo.setSubtractCashback(subtractCashback);
        _log.info("同意退货修改退货信息的参数为：{}", ordReturnMo);
        final int returnApproveResult = _mapper.returnApprove(ordReturnMo);
        _log.info("同意退货修改退货信息的返回值为：{}", returnApproveResult);
        if (returnApproveResult != 1) {
            _log.error("同意退货修改退货信息时出现错误，退货编号为：{}", returnCode);
            throw new RuntimeException("修改退货信息错误");
        }
        _log.info("删除该定单详情的购买关系记录");
        final int delResult = ordBuyRelationSvc.del(orderDetailList.getId());
        _log.info("删除该定单详情的购买关系记录的返回值为：{}", delResult);
        agreeToReturnRo.setResult(AgreeToReturnDic.SUCCESS);
        agreeToReturnRo.setMsg("审核成功");
        return agreeToReturnRo;
    }

    /**
     * 已收到货并退款
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ReceivedAndRefundedRo receivedAndRefunded(final OrdOrderReturnTo to) {
        final ReceivedAndRefundedRo receivedAndRefundedRo = new ReceivedAndRefundedRo();
        Long returnCode = to.getReturnCode();
        returnCode = returnCode == null ? 0L : returnCode;
        Long opId = to.getOpId();
        opId = opId == null ? 0L : opId;
        final String ip = to.getIp();
        final String mac = to.getMac();
        if (returnCode == 0 || opId == 0 || ip == null || ip.equals("") || ip.equals("null") || mac == null || mac.equals("") || mac.equals("null")) {
            _log.error("已收到货并退款时发现参数不全");
            receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.PARAM_NOT_CORRECT);
            receivedAndRefundedRo.setMsg("参数不正确");
            return receivedAndRefundedRo;
        }
        final OrdReturnMo returnMo = new OrdReturnMo();
        returnMo.setReturnCode(returnCode);
        _log.info("已收到货并退款查询退货信息的参数为：{}", returnCode);
        final List<OrdReturnMo> returnList = _mapper.selectSelective(returnMo);
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
        final long orderId = returnList.get(0).getOrderId();
        final OrdOrderMo orderMo = new OrdOrderMo();
        orderMo.setOrderCode(String.valueOf(orderId));
        _log.info("已收到货并退款查询订单信息的参数为：{}", orderId);
        final List<OrdOrderMo> orderList = oderSvc.list(orderMo);
        _log.info("已收到货并退款查询订单信息的返回值为：{}", String.valueOf(orderList));
        if (orderList.size() == 0) {
            _log.error("已收到货并退款查询订单信息时发现没有该订单，退货编号为：{}", returnCode);
            receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ORDER_NOT_EXIST);
            receivedAndRefundedRo.setMsg("没有找到该订单信息");
            return receivedAndRefundedRo;
        }
        if (orderList.get(0).getOrderState() == OrderStateDic.CANCEL.getCode() || orderList.get(0).getOrderState() == OrderStateDic.ORDERED.getCode()) {
            _log.error("已收到货并退款时发现订单处于取消或待支付状态，退货编号为：{}", returnCode);
            receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ORDER_NOT_PAY_OR_ALREADY_CANCEL);
            receivedAndRefundedRo.setMsg("该订单已取消或未支付");
            return receivedAndRefundedRo;
        }
        BigDecimal returnCashbackToBuyer = returnList.get(0).getReturnAmount2();
        if (orderList.get(0).getOrderState() == OrderStateDic.SIGNED.getCode() || orderList.get(0).getOrderState() == OrderStateDic.SETTLED.getCode()) {
            returnCashbackToBuyer = returnList.get(0).getReturnAmount2().subtract(returnList.get(0).getSubtractCashback()).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        final long orderDetailId = returnList.get(0).getOrderDetailId();
        final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
        detailMo.setId(orderDetailId);
        detailMo.setOrderId(orderId);
        _log.info("已收到货并退款查询订单详情信息的参数为：{}", detailMo.toString());
        final List<OrdOrderDetailMo> detailList = orderDetailSvc.list(detailMo);
        _log.info("已收到货并退款查询订单详情信息的返回值为：{}", String.valueOf(detailList));
        if (detailList.size() == 0) {
            _log.error("已收到货并退款查询订单详情信息时发现订单详情信息为空，退货编号为：{}", returnCode);
            receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.USER_NOT_PURCHASE_GOODS);
            receivedAndRefundedRo.setMsg("该商品未购买");
            return receivedAndRefundedRo;
        }
        final byte returnState = detailList.get(0).getReturnState();
        if (returnState != 1) {
            _log.error("已收到货并退款查询订单详情时发现退货状态不处于退货中，退货编号为：{}", returnCode);
            receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.GOODS_NOT_APPLYFOR_OR_HAVE_FINISHED_RETURN);
            receivedAndRefundedRo.setMsg("该商品未申请或已完成退货");
            return receivedAndRefundedRo;
        }
        if (orderList.get(0).getReturnTotal().compareTo(orderList.get(0).getRealMoney()) == 0) {
            _log.info("已收到货并退款修改订单状态的参数为：{}", orderId);
            final int modifyOrderStateResult = oderSvc.modifyOrderStateByOderCode(orderId, (byte) -1);
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
        final long detailId = detailList.get(0).getId();
        _log.info("已收到货并退款修改订单详情退货状态的参数为：{}，{}", detailId, detailMo.getReturnState());
        final int modifyReturnStateResult = orderDetailSvc.modifyReturnStateById(detailId, detailMo.getReturnState());
        _log.info("已收到货并退款修改订单详情退货状态的返回值为：{}", modifyReturnStateResult);
        if (modifyReturnStateResult != 1) {
            _log.error("已收到货并退款修改订单详情退货状态时出现错误，退货编号为：{}", returnCode);
            throw new RuntimeException("修改订单详情退货状态出错");
        }
        final Date date = new Date();
        returnMo.setRefundOpId(opId);
        returnMo.setRefundTime(date);
        returnMo.setReceiveOpId(opId);
        returnMo.setReceiveTime(date);
        returnMo.setRefundState((byte) 2);
        _log.info("已收到货并退款确认收到货的参数为：{}", returnMo);
        final int confirmReceiptOfGoodsResult = _mapper.confirmReceiptOfGoods(returnMo);
        _log.info("已收到货并退款确认收到货的返回值为：{}", confirmReceiptOfGoodsResult);
        if (confirmReceiptOfGoodsResult != 1) {
            _log.error("已收到货并退款确认收到货时出现错误，退货编号为：{}", returnCode);
            throw new RuntimeException("确认收到货出错");
        }
        _log.info("已收到货并退款退款并扣减返现金额的返回值为：{}", receivedAndRefundedRo);
        final RefundTo refundTo = new RefundTo();
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
        final Ro refundRo = refundSvc.refund(refundTo);
        _log.info("已收到货并退款执行退款的返回值为：{}", refundRo);
        if (refundRo.getResult().getCode() != 1) {
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
    public List<Map<String, Object>> selectReturningInfo(final Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        _log.info("查询用户退货中订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = new ArrayList<>();
        final List<OrdReturnMo> orderReturnList = _mapper.selectReturningOrder(map);
        _log.info("查询的结果为: {}", String.valueOf(orderReturnList));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (orderReturnList.size() != 0) {
            for (int i = 0; i < orderReturnList.size(); i++) {
                final Map<String, Object> hm = new HashMap<>();
                final String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
                final Date date = simpleDateFormat.parse(l);
                final long ts = date.getTime();
                _log.info("转换时间得到的时间戳为：{}", ts);
                hm.put("dateline", ts / 1000);
                hm.put("finishDate", ts / 1000 + 86400);
                hm.put("system", System.currentTimeMillis() / 1000);
                final OrdReturnMo obj = orderReturnList.get(i);
                final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor property : propertyDescriptors) {
                    final String key = property.getName();
                    if (!key.equals("class")) {
                        final Method getter = property.getReadMethod();
                        final Object value = getter.invoke(obj);
                        hm.put(key, value);
                    }
                }
                _log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
                final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
                _log.info("查询用户退货订单获取订单详情的参数为：{}", detailMo.toString());
                final OrdOrderDetailMo orderDetailResult = orderDetailSvc.getById(detailMo.getId());
                _log.info("查询用户订单信息获取订单详情的返回值为：{}", orderDetailResult);
                final List<OrderDetailRo> orderDetailRoList = new ArrayList<>();
                _log.info("查询用户订单信息开始获取商品主图");
                final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailResult.getOnlineId(), (byte) 1);
                _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                final OrderDetailRo orderDetailRo = new OrderDetailRo();
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
    public List<Map<String, Object>> selectReturnInfo(final Map<String, Object> map)
            throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        _log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = new ArrayList<>();
        final List<OrdReturnMo> orderReturnList = _mapper.selectReturnOrder(map);
        _log.info("查询的结果为: {}", String.valueOf(orderReturnList));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (orderReturnList.size() != 0) {
            for (int i = 0; i < orderReturnList.size(); i++) {
                final Map<String, Object> hm = new HashMap<>();
                final String l = simpleDateFormat.format(orderReturnList.get(i).getApplicationTime());
                final Date date = simpleDateFormat.parse(l);
                final long ts = date.getTime();
                _log.info("转换时间得到的时间戳为：{}", ts);
                hm.put("dateline", ts / 1000);
                hm.put("finishDate", ts / 1000 + 86400);
                hm.put("system", System.currentTimeMillis() / 1000);
                final OrdReturnMo obj = orderReturnList.get(i);
                final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor property : propertyDescriptors) {
                    final String key = property.getName();
                    if (!key.equals("class")) {
                        final Method getter = property.getReadMethod();
                        final Object value = getter.invoke(obj);
                        hm.put(key, value);
                    }
                }
                _log.info("查询用户退货订单信息hm里面的值为：{}", String.valueOf(hm));
                final OrdOrderDetailMo detailMo = new OrdOrderDetailMo();
                detailMo.setId(Long.parseLong(String.valueOf(orderReturnList.get(i).getOrderDetailId())));
                _log.info("查询用户退货订单获取订单详情的参数为：{}", detailMo.toString());
                final OrdOrderDetailMo orderDetailResult = orderDetailSvc.getById(detailMo.getId());
                _log.info("查询用户订单信息获取订单详情的返回值为：{}", orderDetailResult);
                final List<OrderDetailRo> orderDetailRoList = new ArrayList<>();
                _log.info("查询用户订单信息开始获取商品主图");
                final List<OnlOnlinePicMo> onlinePicList = onlOnlinePicSvc.list(orderDetailResult.getOnlineId(), (byte) 1);
                _log.info("获取商品主图的返回值为{}", String.valueOf(onlinePicList));
                final OrderDetailRo orderDetailRo = new OrderDetailRo();
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
     * 
     * @param mo
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Ro cancelReturn(final OrdReturnMo mo) {
        _log.info("取消退货的请求参数为：{}", mo);
        final Ro ro = new Ro();
        _log.info("取消退货查询退货信息的参数为：{}", mo);
        final OrdReturnMo ordReturnMo = _mapper.selectByPrimaryKey(mo.getId());
        _log.info("取消退货查询退货信息的返回值为：{}", ordReturnMo);
        if (ordReturnMo == null) {
            _log.error("取消订单查询退货信息返回为空，退货id为: {}", mo.getId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("参数错误");
            return ro;
        }

        // 修改订单详情退货状态
        _log.info("取消退货修改订单详情退货状态的参数为：{}", ordReturnMo.getOrderDetailId());
        final int modifyReturnStateByIdResult = orderDetailSvc.modifyReturnStateById(ordReturnMo.getOrderDetailId(), (byte) 0);
        _log.info("取消退货修改订单详情退货状态的返回值为：{}", modifyReturnStateByIdResult);
        if (modifyReturnStateByIdResult != 1) {
            _log.error("取消订单修改订单详情状态失败，退货id为: {}", mo.getId());
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("修改状态失败");
            return ro;
        }

        // 恢复该详情返佣任务
        final TaskTo taskTo = new TaskTo();
        taskTo.setTradeType(TradeTypeDic.SETTLE_COMMISSION);
        taskTo.setOrderDetailId(String.valueOf(ordReturnMo.getOrderDetailId()));
        _log.info("取消退货恢复返佣任务的参数为：{}", taskTo);
        final Ro resumeTask = afcSettleTaskSvc.resumeTask(taskTo);
        _log.info("取消退货恢复返佣任务的返回值为：{}", resumeTask);
        if (resumeTask.getResult().getCode() == ResultDic.FAIL.getCode()) {
            _log.error("取消订单恢复返佣任务失败，退货id为: {}", mo.getId());
            throw new RuntimeException("恢复返佣失败");
        }

        // 修改退货信息表申请状态和取消的操作人等信息
        _log.info("取消退货信息退货信息的参数为：{}", mo);
        final int updateByPrimaryKeySelectiveResult = _mapper.updateByPrimaryKeySelective(mo);
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
