package rebue.ord.to;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class ShipmentConfirmationTo {

	/**
	 * 订单ID
	 */
	private Long id;

	/**
	 * 订单编号
	 */
	private String orderCode;

	/**
	 * 订单标题
	 */
	private String orderTitle;

	/**
	 * 下单金额
	 */
	private BigDecimal orderMoney;

	/**
	 * 实际金额
	 */
	private BigDecimal realMoney;

	/**
	 * 订单状态（-1：作废 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算 ）） -1：作废
	 * 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算
	 */
	private Byte orderState;

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 下单时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;

	/**
	 * 支付时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;

	/**
	 * 收件人名称
	 */
	private String receiverName;

	/**
	 * 收件人电话
	 */
	private String receiverTel;

	/**
	 * 收件人手机
	 */
	private String receiverMobile;

	/**
	 * 收件省
	 */
	private String receiverProvince;

	/**
	 * 收件市
	 */
	private String receiverCity;

	/**
	 * 收件区
	 */
	private String receiverExpArea;

	/**
	 * 收件人详细地址
	 */
	private String receiverAddress;

	/**
	 * 收件地邮编
	 */
	private String receiverPostCode;

	/**
	 * 订单留言
	 */
	private String orderMessages;

	/**
	 * 快递公司ID
	 */
	private Long shipperId;
	
	/**
	 * 快递公司编码
	 */
	private String shipperCode;

	/**
	 * 发货操作人
	 */
	private Long sendOpId;
	

    @ApiModelProperty(value = "发件人名称")
    private String senderName;


    @ApiModelProperty(value = "发件人电话")
    private String senderTel;


    @ApiModelProperty(value = "发件人手机")
    private String senderMobile;


    @ApiModelProperty(value = "发件省")
    private String senderProvince;


    @ApiModelProperty(value = "发件市")
    private String senderCity;


    @ApiModelProperty(value = "发件区")
    private String senderExpArea;


    @ApiModelProperty(value = "发件人详细地址")
    private String senderAddress;


    @ApiModelProperty(value = "发件地邮编")
    private String senderPostCode;
	
	/**
	 * 组织ID
	 */
    @ApiModelProperty(value = "组织id")
	private Long orgId;
    
    /**
	 * 物流单号，手工录入物流单号使用
	 */
    @ApiModelProperty(value = "物流单号")
	private Long logisticCode;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getSenderProvince() {
		return senderProvince;
	}

	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getSenderExpArea() {
		return senderExpArea;
	}

	public void setSenderExpArea(String senderExpArea) {
		this.senderExpArea = senderExpArea;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderPostCode() {
		return senderPostCode;
	}

	public void setSenderPostCode(String senderPostCode) {
		this.senderPostCode = senderPostCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(BigDecimal realMoney) {
		this.realMoney = realMoney;
	}

	public Byte getOrderState() {
		return orderState;
	}

	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverExpArea() {
		return receiverExpArea;
	}

	public void setReceiverExpArea(String receiverExpArea) {
		this.receiverExpArea = receiverExpArea;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverPostCode() {
		return receiverPostCode;
	}

	public void setReceiverPostCode(String receiverPostCode) {
		this.receiverPostCode = receiverPostCode;
	}

	public String getOrderMessages() {
		return orderMessages;
	}

	public void setOrderMessages(String orderMessages) {
		this.orderMessages = orderMessages;
	}

	public Long getShipperId() {
		return shipperId;
	}

	public void setShipperId(Long shipperId) {
		this.shipperId = shipperId;
	}

	public Long getSendOpId() {
		return sendOpId;
	}

	public void setSendOpId(Long sendOpId) {
		this.sendOpId = sendOpId;
	}
	
	

	public String getShipperCode() {
		return shipperCode;
	}

	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}

	@Override
	public String toString() {
		return "ShipmentConfirmationTo [id=" + id + ", orderCode=" + orderCode + ", orderTitle=" + orderTitle
				+ ", orderMoney=" + orderMoney + ", realMoney=" + realMoney + ", orderState=" + orderState + ", userId="
				+ userId + ", userName=" + userName + ", orderTime=" + orderTime + ", payTime=" + payTime
				+ ", receiverName=" + receiverName + ", receiverTel=" + receiverTel + ", receiverMobile="
				+ receiverMobile + ", receiverProvince=" + receiverProvince + ", receiverCity=" + receiverCity
				+ ", receiverExpArea=" + receiverExpArea + ", receiverAddress=" + receiverAddress
				+ ", receiverPostCode=" + receiverPostCode + ", orderMessages=" + orderMessages + ", shipperId="
				+ shipperId + ", shipperCode=" + shipperCode + ", sendOpId=" + sendOpId + ", senderName=" + senderName
				+ ", senderTel=" + senderTel + ", senderMobile=" + senderMobile + ", senderProvince=" + senderProvince
				+ ", senderCity=" + senderCity + ", senderExpArea=" + senderExpArea + ", senderAddress=" + senderAddress
				+ ", senderPostCode=" + senderPostCode + ", orgId=" + orgId + "]";
	}



	



}
