package rebue.ord.ro;

import java.math.BigDecimal;

/**
 * 创建时间：2018年4月9日 上午10:11:02 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderRo.java 类说明： 订单信息
 */
public class OrdOrderRo {

	/**
	 * 商品ID
	 */
	private long onlineId;

	/**
	 * 规格ID
	 */
	private long onlineSpecId;

	/**
	 * 购物车ID
	 */
	private long cartId;

	/**
	 * 上线标题
	 */
	private String onlineTitle;

	/**
	 * 规格名称
	 */
	private String onlineSpec;

	/**
	 * 产品ID
	 */
	private long produceId;

	/**
	 * 商品主图
	 */
	private String picPath;

	/**
	 * 购买数量
	 */
	private int number;

	/**
	 * 上线金额
	 */
	private BigDecimal salePrice;

	/**
	 * 返现金额
	 */
	private BigDecimal cashbackAmount;

	/**
	 * 订单总金额
	 */
	private BigDecimal totalPrice;

	/**
	 * 返现总额
	 */
	private BigDecimal totalBack;

	/**
	 * 订单总数量
	 */
	private int totalNumber;

	/**
	 * 收货地址编号
	 */
	private long address;

	/**
	 * 用户编号
	 */
	private long userId;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 订单留言
	 */
	private String orderMessages;
	
	/**
	 * 商品类型
	 */
	private Byte subjectType;

	public Byte getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Byte subjectType) {
		this.subjectType = subjectType;
	}

	public OrdOrderRo() {
		super();
	}

	public OrdOrderRo(long onlineId, long onlineSpecId, long cartId, String onlineTitle, String onlineSpec,
			long produceId, String picPath, int number, BigDecimal salePrice, BigDecimal cashbackAmount,
			BigDecimal totalPrice, BigDecimal totalBack, int totalNumber, long address, long userId, String userName,
			String orderMessages) {
		super();
		this.onlineId = onlineId;
		this.onlineSpecId = onlineSpecId;
		this.cartId = cartId;
		this.onlineTitle = onlineTitle;
		this.onlineSpec = onlineSpec;
		this.produceId = produceId;
		this.picPath = picPath;
		this.number = number;
		this.salePrice = salePrice;
		this.cashbackAmount = cashbackAmount;
		this.totalPrice = totalPrice;
		this.totalBack = totalBack;
		this.totalNumber = totalNumber;
		this.address = address;
		this.userId = userId;
		this.userName = userName;
		this.orderMessages = orderMessages;
	}

	public long getOnlineId() {
		return onlineId;
	}

	public void setOnlineId(long onlineId) {
		this.onlineId = onlineId;
	}

	public long getOnlineSpecId() {
		return onlineSpecId;
	}

	public void setOnlineSpecId(long onlineSpecId) {
		this.onlineSpecId = onlineSpecId;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public String getOnlineTitle() {
		return onlineTitle;
	}

	public void setOnlineTitle(String onlineTitle) {
		this.onlineTitle = onlineTitle;
	}

	public String getOnlineSpec() {
		return onlineSpec;
	}

	public void setOnlineSpec(String onlineSpec) {
		this.onlineSpec = onlineSpec;
	}

	public long getProduceId() {
		return produceId;
	}

	public void setProduceId(long produceId) {
		this.produceId = produceId;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getCashbackAmount() {
		return cashbackAmount;
	}

	public void setCashbackAmount(BigDecimal cashbackAmount) {
		this.cashbackAmount = cashbackAmount;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalBack() {
		return totalBack;
	}

	public void setTotalBack(BigDecimal totalBack) {
		this.totalBack = totalBack;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public long getAddress() {
		return address;
	}

	public void setAddress(long address) {
		this.address = address;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderMessages() {
		return orderMessages;
	}

	public void setOrderMessages(String orderMessages) {
		this.orderMessages = orderMessages;
	}

	@Override
	public String toString() {
		return "OrdOrderRo [onlineId=" + onlineId + ", onlineSpecId=" + onlineSpecId + ", cartId=" + cartId
				+ ", onlineTitle=" + onlineTitle + ", onlineSpec=" + onlineSpec + ", produceId=" + produceId
				+ ", picPath=" + picPath + ", number=" + number + ", salePrice=" + salePrice + ", cashbackAmount="
				+ cashbackAmount + ", totalPrice=" + totalPrice + ", totalBack=" + totalBack + ", totalNumber="
				+ totalNumber + ", address=" + address + ", userId=" + userId + ", userName=" + userName
				+ ", orderMessages=" + orderMessages + ", subjectType=" + subjectType + "]";
	}


}
