package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 创建时间：2018年4月21日 下午3:03:51 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdReturnRo.java 类说明：
 */
public class OrdReturnRo {

	/** 退货ID **/
	private Long id;

	/** 退货编号 **/
	private Long returnCode;

	/** 订单ID **/
	private Long orderId;

	/** 订单详情ID **/
	private Long orderDetailId;

	/** 退货数量 **/
	private Integer returnCount;

	/** 退货总额 **/
	private BigDecimal returnRental;

	/** 退货金额（余额） **/
	private BigDecimal returnAmount1;

	/** 退货金额（返现金） **/
	private BigDecimal returnAmount2;

	/** 退款类型（1：仅退款 2：退货并退款） **/
	private Byte returnType;

	/** 申请状态（-1：已取消 1：待审核 2：退款中 3：完成 4：已拒绝） **/
	private Byte applicationState;

	/** 退货原因 **/
	private String returnReason;

	/** 申请操作人 **/
	private Long applicationOpId;

	/** 申请时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date applicationTime;

	/** 取消操作人 **/
	private Long cancelOpId;

	/** 取消时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date cancelTime;

	/** 审核操作人 **/
	private Long reviewOpId;

	/** 审核时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date reviewTime;

	/** 退款操作人 **/
	private Long refundOpId;

	/** 退款时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundTime;

	/** 拒绝操作人 **/
	private Long rejectOpId;

	/** 拒绝原因 **/
	private String rejectReason;

	/** 拒绝时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date rejectTime;

	/** 完成操作人 **/
	private Long finishOpId;

	/** 完成时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date finishTime;

	/** 上线ID **/
	private Long onlineId;

	/** 产品ID **/
	private Long produceId;

	/** 上线标题 **/
	private String onlineTitle;

	/** 规格名称 **/
	private String specName;

	/** 购买数量 **/
	private Integer buyCount;

	/** 购买价格 **/
	private BigDecimal buyPrice;

	/** 返现金额 **/
	private BigDecimal cashbackAmount;

	/** 购买单位 **/
	private String buyUnit;

	/** 退货状态（0：未退货 1：退货中 2：已退货） **/
	private Byte returnState;

	public OrdReturnRo() {
		super();
	}

	public OrdReturnRo(Long id, Long returnCode, Long orderId, Long orderDetailId, Integer returnCount,
			BigDecimal returnRental, BigDecimal returnAmount1, BigDecimal returnAmount2, Byte returnType,
			Byte applicationState, String returnReason, Long applicationOpId, Date applicationTime, Long cancelOpId,
			Date cancelTime, Long reviewOpId, Date reviewTime, Long refundOpId, Date refundTime, Long rejectOpId,
			String rejectReason, Date rejectTime, Long finishOpId, Date finishTime, Long onlineId, Long produceId,
			String onlineTitle, String specName, Integer buyCount, BigDecimal buyPrice, BigDecimal cashbackAmount,
			String buyUnit, Byte returnState) {
		super();
		this.id = id;
		this.returnCode = returnCode;
		this.orderId = orderId;
		this.orderDetailId = orderDetailId;
		this.returnCount = returnCount;
		this.returnRental = returnRental;
		this.returnAmount1 = returnAmount1;
		this.returnAmount2 = returnAmount2;
		this.returnType = returnType;
		this.applicationState = applicationState;
		this.returnReason = returnReason;
		this.applicationOpId = applicationOpId;
		this.applicationTime = applicationTime;
		this.cancelOpId = cancelOpId;
		this.cancelTime = cancelTime;
		this.reviewOpId = reviewOpId;
		this.reviewTime = reviewTime;
		this.refundOpId = refundOpId;
		this.refundTime = refundTime;
		this.rejectOpId = rejectOpId;
		this.rejectReason = rejectReason;
		this.rejectTime = rejectTime;
		this.finishOpId = finishOpId;
		this.finishTime = finishTime;
		this.onlineId = onlineId;
		this.produceId = produceId;
		this.onlineTitle = onlineTitle;
		this.specName = specName;
		this.buyCount = buyCount;
		this.buyPrice = buyPrice;
		this.cashbackAmount = cashbackAmount;
		this.buyUnit = buyUnit;
		this.returnState = returnState;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Long returnCode) {
		this.returnCode = returnCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public BigDecimal getReturnRental() {
		return returnRental;
	}

	public void setReturnRental(BigDecimal returnRental) {
		this.returnRental = returnRental;
	}

	public BigDecimal getReturnAmount1() {
		return returnAmount1;
	}

	public void setReturnAmount1(BigDecimal returnAmount1) {
		this.returnAmount1 = returnAmount1;
	}

	public BigDecimal getReturnAmount2() {
		return returnAmount2;
	}

	public void setReturnAmount2(BigDecimal returnAmount2) {
		this.returnAmount2 = returnAmount2;
	}

	public Byte getReturnType() {
		return returnType;
	}

	public void setReturnType(Byte returnType) {
		this.returnType = returnType;
	}

	public Byte getApplicationState() {
		return applicationState;
	}

	public void setApplicationState(Byte applicationState) {
		this.applicationState = applicationState;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public Long getApplicationOpId() {
		return applicationOpId;
	}

	public void setApplicationOpId(Long applicationOpId) {
		this.applicationOpId = applicationOpId;
	}

	public Date getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
	}

	public Long getCancelOpId() {
		return cancelOpId;
	}

	public void setCancelOpId(Long cancelOpId) {
		this.cancelOpId = cancelOpId;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Long getReviewOpId() {
		return reviewOpId;
	}

	public void setReviewOpId(Long reviewOpId) {
		this.reviewOpId = reviewOpId;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Long getRefundOpId() {
		return refundOpId;
	}

	public void setRefundOpId(Long refundOpId) {
		this.refundOpId = refundOpId;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Long getRejectOpId() {
		return rejectOpId;
	}

	public void setRejectOpId(Long rejectOpId) {
		this.rejectOpId = rejectOpId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}

	public Long getFinishOpId() {
		return finishOpId;
	}

	public void setFinishOpId(Long finishOpId) {
		this.finishOpId = finishOpId;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Long getOnlineId() {
		return onlineId;
	}

	public void setOnlineId(Long onlineId) {
		this.onlineId = onlineId;
	}

	public Long getProduceId() {
		return produceId;
	}

	public void setProduceId(Long produceId) {
		this.produceId = produceId;
	}

	public String getOnlineTitle() {
		return onlineTitle;
	}

	public void setOnlineTitle(String onlineTitle) {
		this.onlineTitle = onlineTitle;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getCashbackAmount() {
		return cashbackAmount;
	}

	public void setCashbackAmount(BigDecimal cashbackAmount) {
		this.cashbackAmount = cashbackAmount;
	}

	public String getBuyUnit() {
		return buyUnit;
	}

	public void setBuyUnit(String buyUnit) {
		this.buyUnit = buyUnit;
	}

	public Byte getReturnState() {
		return returnState;
	}

	public void setReturnState(Byte returnState) {
		this.returnState = returnState;
	}

	@Override
	public String toString() {
		return "OrdReturnRo [id=" + id + ", returnCode=" + returnCode + ", orderId=" + orderId + ", orderDetailId="
				+ orderDetailId + ", returnCount=" + returnCount + ", returnRental=" + returnRental + ", returnAmount1="
				+ returnAmount1 + ", returnAmount2=" + returnAmount2 + ", returnType=" + returnType
				+ ", applicationState=" + applicationState + ", returnReason=" + returnReason + ", applicationOpId="
				+ applicationOpId + ", applicationTime=" + applicationTime + ", cancelOpId=" + cancelOpId
				+ ", cancelTime=" + cancelTime + ", reviewOpId=" + reviewOpId + ", reviewTime=" + reviewTime
				+ ", refundOpId=" + refundOpId + ", refundTime=" + refundTime + ", rejectOpId=" + rejectOpId
				+ ", rejectReason=" + rejectReason + ", rejectTime=" + rejectTime + ", finishOpId=" + finishOpId
				+ ", finishTime=" + finishTime + ", onlineId=" + onlineId + ", produceId=" + produceId
				+ ", onlineTitle=" + onlineTitle + ", specName=" + specName + ", buyCount=" + buyCount + ", buyPrice="
				+ buyPrice + ", cashbackAmount=" + cashbackAmount + ", buyUnit=" + buyUnit + ", returnState="
				+ returnState + "]";
	}

}
