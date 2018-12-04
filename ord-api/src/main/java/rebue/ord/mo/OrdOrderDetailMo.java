package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详情
 *
 * 数据库表: ORD_ORDER_DETAIL
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdOrderDetailMo implements Serializable {

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderId;

    /**
     *    上线ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long onlineId;

    /**
     *    产品ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long productId;

    /**
     *    产品规格ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long productSpecId;

    /**
     *    版块类型（0：普通，1：全返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte subjectType;

    /**
     *    返佣金名额
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_SLOT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte commissionSlot;

    /**
     *    返佣金状态（0：匹配中，1：待返，2：已返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte commissionState;

    /**
     *    上线标题
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String onlineTitle;

    /**
     *    规格名称
     *
     *    数据库字段: ORD_ORDER_DETAIL.SPEC_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String specName;

    /**
     *    购买数量(实际数量=购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Integer buyCount;

    /**
     *    购买价格（单价）
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal buyPrice;

    /**
     *    成本价格（单个）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COST_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal costPrice;

    /**
     *    供应商ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUPPLIER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long supplierId;

    /**
     *    发货组织ID(默认填入上线组织ID，可变更为供应商的ID)
     *
     *    数据库字段: ORD_ORDER_DETAIL.DELIVER_ORG_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long deliverOrgId;

    /**
     *    返现金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal cashbackAmount;

    /**
     *    退货数量
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Integer returnCount;

    /**
     *    返现总额(返现总额=返现金额 * (购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal cashbackTotal;

    /**
     *    购买单位
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_UNIT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String buyUnit;

    /**
     *    退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte returnState;

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long userId;

    /**
     *    是否结算给买家
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_SETTLE_BUYER
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Boolean isSettleBuyer;

    /**
     *    实际成交金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.ACTUAL_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal actualAmount;

    /**
     *    是否已发货
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_DELIVERED
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Boolean isDelivered;

    /**
     *    上线规格ID(限购时获取用户购买数量时使用)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long onlineSpecId;

    /**
     *    下单时间戳(用于排序)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_TIMESTAMP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderTimestamp;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     *    上线ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOnlineId() {
        return onlineId;
    }

    /**
     *    上线ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    /**
     *    产品ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getProductId() {
        return productId;
    }

    /**
     *    产品ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     *    产品规格ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getProductSpecId() {
        return productSpecId;
    }

    /**
     *    产品规格ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.PRODUCT_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setProductSpecId(Long productSpecId) {
        this.productSpecId = productSpecId;
    }

    /**
     *    版块类型（0：普通，1：全返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getSubjectType() {
        return subjectType;
    }

    /**
     *    版块类型（0：普通，1：全返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSubjectType(Byte subjectType) {
        this.subjectType = subjectType;
    }

    /**
     *    返佣金名额
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_SLOT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getCommissionSlot() {
        return commissionSlot;
    }

    /**
     *    返佣金名额
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_SLOT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCommissionSlot(Byte commissionSlot) {
        this.commissionSlot = commissionSlot;
    }

    /**
     *    返佣金状态（0：匹配中，1：待返，2：已返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getCommissionState() {
        return commissionState;
    }

    /**
     *    返佣金状态（0：匹配中，1：待返，2：已返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COMMISSION_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCommissionState(Byte commissionState) {
        this.commissionState = commissionState;
    }

    /**
     *    上线标题
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getOnlineTitle() {
        return onlineTitle;
    }

    /**
     *    上线标题
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOnlineTitle(String onlineTitle) {
        this.onlineTitle = onlineTitle;
    }

    /**
     *    规格名称
     *
     *    数据库字段: ORD_ORDER_DETAIL.SPEC_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getSpecName() {
        return specName;
    }

    /**
     *    规格名称
     *
     *    数据库字段: ORD_ORDER_DETAIL.SPEC_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     *    购买数量(实际数量=购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Integer getBuyCount() {
        return buyCount;
    }

    /**
     *    购买数量(实际数量=购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    /**
     *    购买价格（单价）
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     *    购买价格（单价）
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     *    成本价格（单个）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COST_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    /**
     *    成本价格（单个）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COST_PRICE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    /**
     *    供应商ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUPPLIER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getSupplierId() {
        return supplierId;
    }

    /**
     *    供应商ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUPPLIER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     *    发货组织ID(默认填入上线组织ID，可变更为供应商的ID)
     *
     *    数据库字段: ORD_ORDER_DETAIL.DELIVER_ORG_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getDeliverOrgId() {
        return deliverOrgId;
    }

    /**
     *    发货组织ID(默认填入上线组织ID，可变更为供应商的ID)
     *
     *    数据库字段: ORD_ORDER_DETAIL.DELIVER_ORG_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setDeliverOrgId(Long deliverOrgId) {
        this.deliverOrgId = deliverOrgId;
    }

    /**
     *    返现金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getCashbackAmount() {
        return cashbackAmount;
    }

    /**
     *    返现金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCashbackAmount(BigDecimal cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    /**
     *    退货数量
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Integer getReturnCount() {
        return returnCount;
    }

    /**
     *    退货数量
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_COUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    /**
     *    返现总额(返现总额=返现金额 * (购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getCashbackTotal() {
        return cashbackTotal;
    }

    /**
     *    返现总额(返现总额=返现金额 * (购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.CASHBACK_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCashbackTotal(BigDecimal cashbackTotal) {
        this.cashbackTotal = cashbackTotal;
    }

    /**
     *    购买单位
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_UNIT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getBuyUnit() {
        return buyUnit;
    }

    /**
     *    购买单位
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_UNIT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    /**
     *    退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getReturnState() {
        return returnState;
    }

    /**
     *    退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnState(Byte returnState) {
        this.returnState = returnState;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *    是否结算给买家
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_SETTLE_BUYER
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Boolean getIsSettleBuyer() {
        return isSettleBuyer;
    }

    /**
     *    是否结算给买家
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_SETTLE_BUYER
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setIsSettleBuyer(Boolean isSettleBuyer) {
        this.isSettleBuyer = isSettleBuyer;
    }

    /**
     *    实际成交金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.ACTUAL_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    /**
     *    实际成交金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.ACTUAL_AMOUNT
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    /**
     *    是否已发货
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_DELIVERED
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Boolean getIsDelivered() {
        return isDelivered;
    }

    /**
     *    是否已发货
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_DELIVERED
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    /**
     *    上线规格ID(限购时获取用户购买数量时使用)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOnlineSpecId() {
        return onlineSpecId;
    }

    /**
     *    上线规格ID(限购时获取用户购买数量时使用)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_SPEC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOnlineSpecId(Long onlineSpecId) {
        this.onlineSpecId = onlineSpecId;
    }

    /**
     *    下单时间戳(用于排序)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_TIMESTAMP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderTimestamp() {
        return orderTimestamp;
    }

    /**
     *    下单时间戳(用于排序)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_TIMESTAMP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderTimestamp(Long orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
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
        sb.append(", orderId=").append(orderId);
        sb.append(", onlineId=").append(onlineId);
        sb.append(", productId=").append(productId);
        sb.append(", productSpecId=").append(productSpecId);
        sb.append(", subjectType=").append(subjectType);
        sb.append(", commissionSlot=").append(commissionSlot);
        sb.append(", commissionState=").append(commissionState);
        sb.append(", onlineTitle=").append(onlineTitle);
        sb.append(", specName=").append(specName);
        sb.append(", buyCount=").append(buyCount);
        sb.append(", buyPrice=").append(buyPrice);
        sb.append(", costPrice=").append(costPrice);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", deliverOrgId=").append(deliverOrgId);
        sb.append(", cashbackAmount=").append(cashbackAmount);
        sb.append(", returnCount=").append(returnCount);
        sb.append(", cashbackTotal=").append(cashbackTotal);
        sb.append(", buyUnit=").append(buyUnit);
        sb.append(", returnState=").append(returnState);
        sb.append(", userId=").append(userId);
        sb.append(", isSettleBuyer=").append(isSettleBuyer);
        sb.append(", actualAmount=").append(actualAmount);
        sb.append(", isDelivered=").append(isDelivered);
        sb.append(", onlineSpecId=").append(onlineSpecId);
        sb.append(", orderTimestamp=").append(orderTimestamp);
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
        OrdOrderDetailMo other = (OrdOrderDetailMo) that;
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
