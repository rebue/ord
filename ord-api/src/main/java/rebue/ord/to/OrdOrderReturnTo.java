package rebue.ord.to;

import java.math.BigDecimal;

/**
 * 创建时间：2018年4月19日 下午2:42:42 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderReturnTo.java 类说明： 用户退货参数
 */
public class OrdOrderReturnTo {

	/** 订单编号 **/
	private long orderCode;

	/** 上线编号 **/
	private long onlineId;

	/** 订单详情ID **/
	private long orderDetailId;

	/** 退货数量 **/
	private int returnNum;

	/** 规格名称 **/
	private String specName;

	/** 退货金额 **/
	private BigDecimal returnPrice;

	/** 退货原因 **/
	private String returnReason;

	/** 退货图片 **/
	private String returnImg;

	/** 退货类型 **/
	private Byte returnType;

	/** 申请操作人编号 **/
	private long userId;

	/** 退货编号 **/
	private long returnCode;

	/** 操作人编号 **/
	private long opId;

	/** 退货金额（余额） **/
	private double returnAmount1;

	/** 退货金额（返现金） **/
	private double returnAmount2;

	/** 扣减返现金额 **/
	private double subtractCashback;

	/** ip地址 **/
	private String ip;

	/** mac地址 **/
	private String mac;

	public OrdOrderReturnTo() {
		super();
	}

	public OrdOrderReturnTo(long orderCode, long onlineId, long orderDetailId, int returnNum, String specName,
			BigDecimal returnPrice, String returnReason, String returnImg, Byte returnType, long userId,
			long returnCode, long opId, double returnAmount1, double returnAmount2, double subtractCashback, String ip,
			String mac) {
		super();
		this.orderCode = orderCode;
		this.onlineId = onlineId;
		this.orderDetailId = orderDetailId;
		this.returnNum = returnNum;
		this.specName = specName;
		this.returnPrice = returnPrice;
		this.returnReason = returnReason;
		this.returnImg = returnImg;
		this.returnType = returnType;
		this.userId = userId;
		this.returnCode = returnCode;
		this.opId = opId;
		this.returnAmount1 = returnAmount1;
		this.returnAmount2 = returnAmount2;
		this.subtractCashback = subtractCashback;
		this.ip = ip;
		this.mac = mac;
	}

	public long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(long orderCode) {
		this.orderCode = orderCode;
	}

	public long getOnlineId() {
		return onlineId;
	}

	public void setOnlineId(long onlineId) {
		this.onlineId = onlineId;
	}

	public long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}

	public BigDecimal getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getReturnImg() {
		return returnImg;
	}

	public void setReturnImg(String returnImg) {
		this.returnImg = returnImg;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public long getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(long returnCode) {
		this.returnCode = returnCode;
	}

	public long getOpId() {
		return opId;
	}

	public void setOpId(long opId) {
		this.opId = opId;
	}

	public double getReturnAmount1() {
		return returnAmount1;
	}

	public void setReturnAmount1(double returnAmount1) {
		this.returnAmount1 = returnAmount1;
	}

	public double getReturnAmount2() {
		return returnAmount2;
	}

	public void setReturnAmount2(double returnAmount2) {
		this.returnAmount2 = returnAmount2;
	}

	public double getSubtractCashback() {
		return subtractCashback;
	}

	public void setSubtractCashback(double subtractCashback) {
		this.subtractCashback = subtractCashback;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Byte getReturnType() {
		return returnType;
	}

	public void setReturnType(Byte returnType) {
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return "OrdOrderReturnTo [orderCode=" + orderCode + ", onlineId=" + onlineId + ", orderDetailId="
				+ orderDetailId + ", returnNum=" + returnNum + ", specName=" + specName + ", returnPrice=" + returnPrice
				+ ", returnReason=" + returnReason + ", returnImg=" + returnImg + ", returnType=" + returnType
				+ ", userId=" + userId + ", returnCode=" + returnCode + ", opId=" + opId + ", returnAmount1="
				+ returnAmount1 + ", returnAmount2=" + returnAmount2 + ", subtractCashback=" + subtractCashback
				+ ", ip=" + ip + ", mac=" + mac + "]";
	}

}
