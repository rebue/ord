package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户退货信息
 *
 * 数据库表: ORD_RETURN
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdReturnMo implements Serializable {

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    退货编号
     *
     *    数据库字段: ORD_RETURN.RETURN_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long returnCode;

    /**
     *    订单ID
     *
     *    数据库字段: ORD_RETURN.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderId;

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_RETURN.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderDetailId;

    /**
     *    退货数量
     *
     *    数据库字段: ORD_RETURN.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Integer returnCount;

    /**
     *    退款总额（退款总额=退款余额+退款返现金+扣除补偿金）
     *
     *    数据库字段: ORD_RETURN.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal returnTotal;

    /**
     *    扣除补偿金额(扣除需补偿的金额，例如补偿运费)
     *
     *    数据库字段: ORD_RETURN.DEDUCT_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal deductAmount;

    /**
     *    退款类型（1：仅退款  2：退货并退款）
     *
     *    数据库字段: ORD_RETURN.RETURN_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte returnType;

    /**
     *    申请状态（-1：已取消  1：待审核  2：退货中  3：已完成   4：已拒绝）
     *
     *    数据库字段: ORD_RETURN.APPLICATION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte applicationState;

    /**
     *    退货原因
     *
     *    数据库字段: ORD_RETURN.RETURN_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String returnReason;

    /**
     *    用户ID
     *
     *    数据库字段: ORD_RETURN.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long userId;

    /**
     *    申请操作人
     *
     *    数据库字段: ORD_RETURN.APPLICATION_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long applicationOpId;

    /**
     *    申请时间
     *
     *    数据库字段: ORD_RETURN.APPLICATION_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationTime;

    /**
     *    取消操作人
     *
     *    数据库字段: ORD_RETURN.CANCEL_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long cancelOpId;

    /**
     *    取消时间
     *
     *    数据库字段: ORD_RETURN.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;

    /**
     *    审核操作人
     *
     *    数据库字段: ORD_RETURN.REVIEW_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long reviewOpId;

    /**
     *    审核时间
     *
     *    数据库字段: ORD_RETURN.REVIEW_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    /**
     *    退款操作人
     *
     *    数据库字段: ORD_RETURN.REFUND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long refundOpId;

    /**
     *    退款时间
     *
     *    数据库字段: ORD_RETURN.REFUND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;

    /**
     *    拒绝操作人
     *
     *    数据库字段: ORD_RETURN.REJECT_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long rejectOpId;

    /**
     *    拒绝原因
     *
     *    数据库字段: ORD_RETURN.REJECT_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String rejectReason;

    /**
     *    拒绝时间
     *
     *    数据库字段: ORD_RETURN.REJECT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rejectTime;

    /**
     *    完成操作人
     *
     *    数据库字段: ORD_RETURN.FINISH_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long finishOpId;

    /**
     *    完成时间
     *
     *    数据库字段: ORD_RETURN.FINISH_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     *    确认收到货操作人
     *
     *    数据库字段: ORD_RETURN.RECEIVE_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long receiveOpId;

    /**
     *    确认收到货时间
     *
     *    数据库字段: ORD_RETURN.RECEIVE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    退货编号
     *
     *    数据库字段: ORD_RETURN.RETURN_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getReturnCode() {
        return returnCode;
    }

    /**
     *    退货编号
     *
     *    数据库字段: ORD_RETURN.RETURN_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnCode(Long returnCode) {
        this.returnCode = returnCode;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_RETURN.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_RETURN.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_RETURN.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderDetailId() {
        return orderDetailId;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_RETURN.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     *    退货数量
     *
     *    数据库字段: ORD_RETURN.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Integer getReturnCount() {
        return returnCount;
    }

    /**
     *    退货数量
     *
     *    数据库字段: ORD_RETURN.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    /**
     *    退款总额（退款总额=退款余额+退款返现金+扣除补偿金）
     *
     *    数据库字段: ORD_RETURN.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getReturnTotal() {
        return returnTotal;
    }

    /**
     *    退款总额（退款总额=退款余额+退款返现金+扣除补偿金）
     *
     *    数据库字段: ORD_RETURN.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnTotal(BigDecimal returnTotal) {
        this.returnTotal = returnTotal;
    }

    /**
     *    扣除补偿金额(扣除需补偿的金额，例如补偿运费)
     *
     *    数据库字段: ORD_RETURN.DEDUCT_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getDeductAmount() {
        return deductAmount;
    }

    /**
     *    扣除补偿金额(扣除需补偿的金额，例如补偿运费)
     *
     *    数据库字段: ORD_RETURN.DEDUCT_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setDeductAmount(BigDecimal deductAmount) {
        this.deductAmount = deductAmount;
    }

    /**
     *    退款类型（1：仅退款  2：退货并退款）
     *
     *    数据库字段: ORD_RETURN.RETURN_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getReturnType() {
        return returnType;
    }

    /**
     *    退款类型（1：仅退款  2：退货并退款）
     *
     *    数据库字段: ORD_RETURN.RETURN_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnType(Byte returnType) {
        this.returnType = returnType;
    }

    /**
     *    申请状态（-1：已取消  1：待审核  2：退货中  3：已完成   4：已拒绝）
     *
     *    数据库字段: ORD_RETURN.APPLICATION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getApplicationState() {
        return applicationState;
    }

    /**
     *    申请状态（-1：已取消  1：待审核  2：退货中  3：已完成   4：已拒绝）
     *
     *    数据库字段: ORD_RETURN.APPLICATION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setApplicationState(Byte applicationState) {
        this.applicationState = applicationState;
    }

    /**
     *    退货原因
     *
     *    数据库字段: ORD_RETURN.RETURN_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     *    退货原因
     *
     *    数据库字段: ORD_RETURN.RETURN_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_RETURN.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_RETURN.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *    申请操作人
     *
     *    数据库字段: ORD_RETURN.APPLICATION_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getApplicationOpId() {
        return applicationOpId;
    }

    /**
     *    申请操作人
     *
     *    数据库字段: ORD_RETURN.APPLICATION_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setApplicationOpId(Long applicationOpId) {
        this.applicationOpId = applicationOpId;
    }

    /**
     *    申请时间
     *
     *    数据库字段: ORD_RETURN.APPLICATION_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getApplicationTime() {
        return applicationTime;
    }

    /**
     *    申请时间
     *
     *    数据库字段: ORD_RETURN.APPLICATION_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    /**
     *    取消操作人
     *
     *    数据库字段: ORD_RETURN.CANCEL_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getCancelOpId() {
        return cancelOpId;
    }

    /**
     *    取消操作人
     *
     *    数据库字段: ORD_RETURN.CANCEL_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCancelOpId(Long cancelOpId) {
        this.cancelOpId = cancelOpId;
    }

    /**
     *    取消时间
     *
     *    数据库字段: ORD_RETURN.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     *    取消时间
     *
     *    数据库字段: ORD_RETURN.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     *    审核操作人
     *
     *    数据库字段: ORD_RETURN.REVIEW_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getReviewOpId() {
        return reviewOpId;
    }

    /**
     *    审核操作人
     *
     *    数据库字段: ORD_RETURN.REVIEW_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReviewOpId(Long reviewOpId) {
        this.reviewOpId = reviewOpId;
    }

    /**
     *    审核时间
     *
     *    数据库字段: ORD_RETURN.REVIEW_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getReviewTime() {
        return reviewTime;
    }

    /**
     *    审核时间
     *
     *    数据库字段: ORD_RETURN.REVIEW_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    /**
     *    退款操作人
     *
     *    数据库字段: ORD_RETURN.REFUND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getRefundOpId() {
        return refundOpId;
    }

    /**
     *    退款操作人
     *
     *    数据库字段: ORD_RETURN.REFUND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRefundOpId(Long refundOpId) {
        this.refundOpId = refundOpId;
    }

    /**
     *    退款时间
     *
     *    数据库字段: ORD_RETURN.REFUND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getRefundTime() {
        return refundTime;
    }

    /**
     *    退款时间
     *
     *    数据库字段: ORD_RETURN.REFUND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    /**
     *    拒绝操作人
     *
     *    数据库字段: ORD_RETURN.REJECT_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getRejectOpId() {
        return rejectOpId;
    }

    /**
     *    拒绝操作人
     *
     *    数据库字段: ORD_RETURN.REJECT_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRejectOpId(Long rejectOpId) {
        this.rejectOpId = rejectOpId;
    }

    /**
     *    拒绝原因
     *
     *    数据库字段: ORD_RETURN.REJECT_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getRejectReason() {
        return rejectReason;
    }

    /**
     *    拒绝原因
     *
     *    数据库字段: ORD_RETURN.REJECT_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    /**
     *    拒绝时间
     *
     *    数据库字段: ORD_RETURN.REJECT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getRejectTime() {
        return rejectTime;
    }

    /**
     *    拒绝时间
     *
     *    数据库字段: ORD_RETURN.REJECT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    /**
     *    完成操作人
     *
     *    数据库字段: ORD_RETURN.FINISH_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getFinishOpId() {
        return finishOpId;
    }

    /**
     *    完成操作人
     *
     *    数据库字段: ORD_RETURN.FINISH_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setFinishOpId(Long finishOpId) {
        this.finishOpId = finishOpId;
    }

    /**
     *    完成时间
     *
     *    数据库字段: ORD_RETURN.FINISH_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     *    完成时间
     *
     *    数据库字段: ORD_RETURN.FINISH_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     *    确认收到货操作人
     *
     *    数据库字段: ORD_RETURN.RECEIVE_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getReceiveOpId() {
        return receiveOpId;
    }

    /**
     *    确认收到货操作人
     *
     *    数据库字段: ORD_RETURN.RECEIVE_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiveOpId(Long receiveOpId) {
        this.receiveOpId = receiveOpId;
    }

    /**
     *    确认收到货时间
     *
     *    数据库字段: ORD_RETURN.RECEIVE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     *    确认收到货时间
     *
     *    数据库字段: ORD_RETURN.RECEIVE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", returnCode=").append(returnCode);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderDetailId=").append(orderDetailId);
        sb.append(", returnCount=").append(returnCount);
        sb.append(", returnTotal=").append(returnTotal);
        sb.append(", deductAmount=").append(deductAmount);
        sb.append(", returnType=").append(returnType);
        sb.append(", applicationState=").append(applicationState);
        sb.append(", returnReason=").append(returnReason);
        sb.append(", userId=").append(userId);
        sb.append(", applicationOpId=").append(applicationOpId);
        sb.append(", applicationTime=").append(applicationTime);
        sb.append(", cancelOpId=").append(cancelOpId);
        sb.append(", cancelTime=").append(cancelTime);
        sb.append(", reviewOpId=").append(reviewOpId);
        sb.append(", reviewTime=").append(reviewTime);
        sb.append(", refundOpId=").append(refundOpId);
        sb.append(", refundTime=").append(refundTime);
        sb.append(", rejectOpId=").append(rejectOpId);
        sb.append(", rejectReason=").append(rejectReason);
        sb.append(", rejectTime=").append(rejectTime);
        sb.append(", finishOpId=").append(finishOpId);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", receiveOpId=").append(receiveOpId);
        sb.append(", receiveTime=").append(receiveTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrdReturnMo other = (OrdReturnMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}
