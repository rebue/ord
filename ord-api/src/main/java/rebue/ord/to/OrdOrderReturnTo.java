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

	/** 规格名称 **/
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

	/** 申请操作人编号 **/
	private long userId;

	public OrdOrderReturnTo() {
		super();
	}

	public OrdOrderReturnTo(long orderCode, long onlineId, long orderDetailId, int returnNum, String specName,
			BigDecimal returnPrice, String returnReason, String returnImg, long userId) {
		super();
		this.orderCode = orderCode;
		this.onlineId = onlineId;
		this.orderDetailId = orderDetailId;
		this.returnNum = returnNum;
		this.specName = specName;
		this.returnPrice = returnPrice;
		this.returnReason = returnReason;
		this.returnImg = returnImg;
		this.userId = userId;
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

	@Override
	public String toString() {
		return "OrdOrderReturnTo [orderCode=" + orderCode + ", onlineId=" + onlineId + ", orderDetailId="
				+ orderDetailId + ", returnNum=" + returnNum + ", specName=" + specName + ", returnPrice=" + returnPrice
				+ ", returnReason=" + returnReason + ", returnImg=" + returnImg + ", userId=" + userId + "]";
	}

}
