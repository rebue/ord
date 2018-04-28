package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Database Table Remarks:
 *   订单详情
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table ORD_ORDER_DETAIL
 *
 * @mbg.generated do_not_delete_during_merge 2018-04-27 11:48:43
 */
@ApiModel(value = "OrdOrderDetailMo", description = "订单详情")
@JsonInclude(Include.NON_NULL)
public class OrdOrderDetailMo implements Serializable {
    /**
     * Database Column Remarks:
     *   订单详情ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "订单详情ID")
    private Long id;

    /**
     * Database Column Remarks:
     *   订单ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.ORDER_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * Database Column Remarks:
     *   上线ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.ONLINE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "上线ID")
    private Long onlineId;

    /**
     * Database Column Remarks:
     *   产品ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.PRODUCE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "产品ID")
    private Long produceId;

    /**
     * Database Column Remarks:
     *   上线标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "上线标题")
    private String onlineTitle;

    /**
     * Database Column Remarks:
     *   规格名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.SPEC_NAME
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "规格名称")
    private String specName;

    /**
     * Database Column Remarks:
     *   购买数量
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.BUY_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    /**
     * Database Column Remarks:
     *   购买价格
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.BUY_PRICE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "购买价格")
    private BigDecimal buyPrice;

    /**
     * Database Column Remarks:
     *   返现金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "返现金额")
    private BigDecimal cashbackAmount;

    /**
     * Database Column Remarks:
     *   退货数量
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.RETURN_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "退货数量")
    private Integer returnCount;

    /**
     * Database Column Remarks:
     *   购买单位
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.BUY_UNIT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "购买单位")
    private String buyUnit;

    /**
     * Database Column Remarks:
     *   退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER_DETAIL.RETURN_STATE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @ApiModelProperty(value = "退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）")
    private Byte returnState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ORD_ORDER_DETAIL
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.ID
     *
     * @return the value of ORD_ORDER_DETAIL.ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.ID
     *
     * @param id the value for ORD_ORDER_DETAIL.ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.ORDER_ID
     *
     * @return the value of ORD_ORDER_DETAIL.ORDER_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.ORDER_ID
     *
     * @param orderId the value for ORD_ORDER_DETAIL.ORDER_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.ONLINE_ID
     *
     * @return the value of ORD_ORDER_DETAIL.ONLINE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Long getOnlineId() {
        return onlineId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.ONLINE_ID
     *
     * @param onlineId the value for ORD_ORDER_DETAIL.ONLINE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.PRODUCE_ID
     *
     * @return the value of ORD_ORDER_DETAIL.PRODUCE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Long getProduceId() {
        return produceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.PRODUCE_ID
     *
     * @param produceId the value for ORD_ORDER_DETAIL.PRODUCE_ID
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setProduceId(Long produceId) {
        this.produceId = produceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     * @return the value of ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public String getOnlineTitle() {
        return onlineTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     * @param onlineTitle the value for ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setOnlineTitle(String onlineTitle) {
        this.onlineTitle = onlineTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.SPEC_NAME
     *
     * @return the value of ORD_ORDER_DETAIL.SPEC_NAME
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.SPEC_NAME
     *
     * @param specName the value for ORD_ORDER_DETAIL.SPEC_NAME
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.BUY_COUNT
     *
     * @return the value of ORD_ORDER_DETAIL.BUY_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Integer getBuyCount() {
        return buyCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.BUY_COUNT
     *
     * @param buyCount the value for ORD_ORDER_DETAIL.BUY_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.BUY_PRICE
     *
     * @return the value of ORD_ORDER_DETAIL.BUY_PRICE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.BUY_PRICE
     *
     * @param buyPrice the value for ORD_ORDER_DETAIL.BUY_PRICE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     * @return the value of ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public BigDecimal getCashbackAmount() {
        return cashbackAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     * @param cashbackAmount the value for ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setCashbackAmount(BigDecimal cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.RETURN_COUNT
     *
     * @return the value of ORD_ORDER_DETAIL.RETURN_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Integer getReturnCount() {
        return returnCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.RETURN_COUNT
     *
     * @param returnCount the value for ORD_ORDER_DETAIL.RETURN_COUNT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.BUY_UNIT
     *
     * @return the value of ORD_ORDER_DETAIL.BUY_UNIT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public String getBuyUnit() {
        return buyUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.BUY_UNIT
     *
     * @param buyUnit the value for ORD_ORDER_DETAIL.BUY_UNIT
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setBuyUnit(String buyUnit) {
        this.buyUnit = buyUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER_DETAIL.RETURN_STATE
     *
     * @return the value of ORD_ORDER_DETAIL.RETURN_STATE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public Byte getReturnState() {
        return returnState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER_DETAIL.RETURN_STATE
     *
     * @param returnState the value for ORD_ORDER_DETAIL.RETURN_STATE
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    public void setReturnState(Byte returnState) {
        this.returnState = returnState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER_DETAIL
     *
     * @mbg.generated 2018-04-27 11:48:43
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
        sb.append(", produceId=").append(produceId);
        sb.append(", onlineTitle=").append(onlineTitle);
        sb.append(", specName=").append(specName);
        sb.append(", buyCount=").append(buyCount);
        sb.append(", buyPrice=").append(buyPrice);
        sb.append(", cashbackAmount=").append(cashbackAmount);
        sb.append(", returnCount=").append(returnCount);
        sb.append(", buyUnit=").append(buyUnit);
        sb.append(", returnState=").append(returnState);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER_DETAIL
     *
     * @mbg.generated 2018-04-27 11:48:43
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
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        ;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER_DETAIL
     *
     * @mbg.generated 2018-04-27 11:48:43
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}