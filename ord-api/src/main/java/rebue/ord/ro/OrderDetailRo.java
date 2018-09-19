package rebue.ord.ro;

import java.math.BigDecimal;

/**
 * 创建时间：2018年4月17日 下午5:00:55 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderAndOrderDetailRo.java 类说明：
 */
public class OrderDetailRo {

	/** 订单详情ID **/
	private Long id;

	/** 订单ID **/
	private Long orderId;

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

	/** 返现总额 **/
	private BigDecimal cashbackTotal;

	/** 购买单位 **/
	private String buyUnit;

	/** 退货数量 **/
	private Integer returnCount;

	/** 退货状态（0：未退货 1：退货中 2：已退货） **/
	private Byte returnState;

	/**
	 * 返现佣金名额
	 */
	private Byte cashbackCommissionSlot;

	 /**
     *    返现佣金状态（0：匹配中，1：待返，2：已返)
     */
	private Byte cashbackCommissionState;

	public Byte getCashbackCommissionSlot() {
		return cashbackCommissionSlot;
	}

	public void setCashbackCommissionSlot(Byte cashbackCommissionSlot) {
		this.cashbackCommissionSlot = cashbackCommissionSlot;
	}

	public Byte getCashbackCommissionState() {
		return cashbackCommissionState;
	}

	public void setCashbackCommissionState(Byte cashbackCommissionState) {
		this.cashbackCommissionState = cashbackCommissionState;
	}

	/** 商品主图 **/
	private String goodsQsmm;

	/** 商品类型 **/
	private Byte subjectType;

	public Byte getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Byte subjectType) {
		this.subjectType = subjectType;
	}

	public OrderDetailRo() {
		super();
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public BigDecimal getCashbackTotal() {
		return cashbackTotal;
	}

	public void setCashbackTotal(BigDecimal cashbackTotal) {
		this.cashbackTotal = cashbackTotal;
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

	public String getGoodsQsmm() {
		return goodsQsmm;
	}

	public void setGoodsQsmm(String goodsQsmm) {
		this.goodsQsmm = goodsQsmm;
	}

	@Override
	public String toString() {
		return "OrderDetailRo [id=" + id + ", orderId=" + orderId + ", onlineId=" + onlineId + ", produceId="
				+ produceId + ", onlineTitle=" + onlineTitle + ", specName=" + specName + ", buyCount=" + buyCount
				+ ", buyPrice=" + buyPrice + ", cashbackAmount=" + cashbackAmount + ", cashbackTotal=" + cashbackTotal
				+ ", buyUnit=" + buyUnit + ", returnCount=" + returnCount + ", returnState=" + returnState
				+ ", cashbackCommissionSlot=" + cashbackCommissionSlot + ", cashbackCommissionState="
				+ cashbackCommissionState + ", goodsQsmm=" + goodsQsmm + ", subjectType=" + subjectType + "]";
	}


}
