package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Database Table Remarks:
 *   订单信息
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table ORD_ORDER
 *
 * @mbg.generated do_not_delete_during_merge 2018-04-13 18:12:16
 */
@ApiModel(value = "OrdOrderMo", description = "订单信息")
@JsonInclude(Include.NON_NULL)
public class OrdOrderMo implements Serializable {
    /**
     * Database Column Remarks:
     *   订单ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "订单ID")
    private Long id;

    /**
     * Database Column Remarks:
     *   订单编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * Database Column Remarks:
     *   订单标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_TITLE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "订单标题")
    private String orderTitle;

    /**
     * Database Column Remarks:
     *   下单金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "下单金额")
    private BigDecimal orderMoney;

    /**
     * Database Column Remarks:
     *   实际金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.REAL_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "实际金额")
    private BigDecimal realMoney;

    /**
     * Database Column Remarks:
     *   订单状态
     *               -1：作废
     *               1：已下单（待支付）
     *               2：已支付（待发货）
     *               3：已发货（待签收）
     *               4：已签收（待结算）
     *               5：已结算
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_STATE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "订单状态\n"
             +"            -1：作废\n"
             +"            1：已下单（待支付）\n"
             +"            2：已支付（待发货）\n"
             +"            3：已发货（待签收）\n"
             +"            4：已签收（待结算）\n"
             +"            5：已结算")
    private Byte orderState;

    /**
     * Database Column Remarks:
     *   用户编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.USER_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "用户编号")
    private Long userId;

    /**
     * Database Column Remarks:
     *   用户姓名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.USER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    /**
     * Database Column Remarks:
     *   下单时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    /**
     * Database Column Remarks:
     *   支付时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.PAY_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * Database Column Remarks:
     *   发货时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.SEND_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * Database Column Remarks:
     *   签收时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVED_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "签收时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receivedTime;

    /**
     * Database Column Remarks:
     *   结算时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.CLOSE_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "结算时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime;

    /**
     * Database Column Remarks:
     *   作废时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.CANCEL_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "作废时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;

    /**
     * Database Column Remarks:
     *   物流订单ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.LOGISTIC_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "物流订单ID")
    private Long logisticId;

    /**
     * Database Column Remarks:
     *   快递公司编号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.SHIPPER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "快递公司编号")
    private String shipperCode;

    /**
     * Database Column Remarks:
     *   快递公司名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.SHIPPER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "快递公司名称")
    private String shipperName;

    /**
     * Database Column Remarks:
     *   快递单号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.LOGISTIC_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "快递单号")
    private String logisticCode;

    /**
     * Database Column Remarks:
     *   收件人名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件人名称")
    private String receiverName;

    /**
     * Database Column Remarks:
     *   收件人电话
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_TEL
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件人电话")
    private String receiverTel;

    /**
     * Database Column Remarks:
     *   收件人手机
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件人手机")
    private String receiverMobile;

    /**
     * Database Column Remarks:
     *   收件省
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件省")
    private String receiverProvince;

    /**
     * Database Column Remarks:
     *   收件市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_CITY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件市")
    private String receiverCity;

    /**
     * Database Column Remarks:
     *   收件区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件区")
    private String receiverExpArea;

    /**
     * Database Column Remarks:
     *   收件人详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件人详细地址")
    private String receiverAddress;

    /**
     * Database Column Remarks:
     *   收件地邮编
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "收件地邮编")
    private String receiverPostCode;

    /**
     * Database Column Remarks:
     *   订单留言
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.ORDER_MESSAGES
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "订单留言")
    private String orderMessages;

    /**
     * Database Column Remarks:
     *   修改实际金额操作人ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.MODIFY_REAL_MONEY_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "修改实际金额操作人ID")
    private Long modifyRealMoneyOpId;

    /**
     * Database Column Remarks:
     *   取消订单操作人ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "取消订单操作人ID")
    private Long cancelingOrderOpId;

    /**
     * Database Column Remarks:
     *   取消发货的原因
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.CANCEL_REASON
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "取消发货的原因")
    private String cancelReason;

    /**
     * Database Column Remarks:
     *   发货操作人
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.SEND_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "发货操作人")
    private Long sendOpId;

    /**
     * Database Column Remarks:
     *   签收人
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ORDER.RECEIVED_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @ApiModelProperty(value = "签收人")
    private Long receivedOpId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ORD_ORDER
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ID
     *
     * @return the value of ORD_ORDER.ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ID
     *
     * @param id the value for ORD_ORDER.ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_CODE
     *
     * @return the value of ORD_ORDER.ORDER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_CODE
     *
     * @param orderCode the value for ORD_ORDER.ORDER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_TITLE
     *
     * @return the value of ORD_ORDER.ORDER_TITLE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getOrderTitle() {
        return orderTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_TITLE
     *
     * @param orderTitle the value for ORD_ORDER.ORDER_TITLE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_MONEY
     *
     * @return the value of ORD_ORDER.ORDER_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_MONEY
     *
     * @param orderMoney the value for ORD_ORDER.ORDER_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.REAL_MONEY
     *
     * @return the value of ORD_ORDER.REAL_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public BigDecimal getRealMoney() {
        return realMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.REAL_MONEY
     *
     * @param realMoney the value for ORD_ORDER.REAL_MONEY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_STATE
     *
     * @return the value of ORD_ORDER.ORDER_STATE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Byte getOrderState() {
        return orderState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_STATE
     *
     * @param orderState the value for ORD_ORDER.ORDER_STATE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderState(Byte orderState) {
        this.orderState = orderState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.USER_ID
     *
     * @return the value of ORD_ORDER.USER_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.USER_ID
     *
     * @param userId the value for ORD_ORDER.USER_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.USER_NAME
     *
     * @return the value of ORD_ORDER.USER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.USER_NAME
     *
     * @param userName the value for ORD_ORDER.USER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_TIME
     *
     * @return the value of ORD_ORDER.ORDER_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_TIME
     *
     * @param orderTime the value for ORD_ORDER.ORDER_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.PAY_TIME
     *
     * @return the value of ORD_ORDER.PAY_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.PAY_TIME
     *
     * @param payTime the value for ORD_ORDER.PAY_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.SEND_TIME
     *
     * @return the value of ORD_ORDER.SEND_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.SEND_TIME
     *
     * @param sendTime the value for ORD_ORDER.SEND_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVED_TIME
     *
     * @return the value of ORD_ORDER.RECEIVED_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getReceivedTime() {
        return receivedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVED_TIME
     *
     * @param receivedTime the value for ORD_ORDER.RECEIVED_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.CLOSE_TIME
     *
     * @return the value of ORD_ORDER.CLOSE_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.CLOSE_TIME
     *
     * @param closeTime the value for ORD_ORDER.CLOSE_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.CANCEL_TIME
     *
     * @return the value of ORD_ORDER.CANCEL_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.CANCEL_TIME
     *
     * @param cancelTime the value for ORD_ORDER.CANCEL_TIME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.LOGISTIC_ID
     *
     * @return the value of ORD_ORDER.LOGISTIC_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getLogisticId() {
        return logisticId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.LOGISTIC_ID
     *
     * @param logisticId the value for ORD_ORDER.LOGISTIC_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setLogisticId(Long logisticId) {
        this.logisticId = logisticId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.SHIPPER_CODE
     *
     * @return the value of ORD_ORDER.SHIPPER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getShipperCode() {
        return shipperCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.SHIPPER_CODE
     *
     * @param shipperCode the value for ORD_ORDER.SHIPPER_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.SHIPPER_NAME
     *
     * @return the value of ORD_ORDER.SHIPPER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.SHIPPER_NAME
     *
     * @param shipperName the value for ORD_ORDER.SHIPPER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.LOGISTIC_CODE
     *
     * @return the value of ORD_ORDER.LOGISTIC_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getLogisticCode() {
        return logisticCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.LOGISTIC_CODE
     *
     * @param logisticCode the value for ORD_ORDER.LOGISTIC_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_NAME
     *
     * @return the value of ORD_ORDER.RECEIVER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_NAME
     *
     * @param receiverName the value for ORD_ORDER.RECEIVER_NAME
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_TEL
     *
     * @return the value of ORD_ORDER.RECEIVER_TEL
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverTel() {
        return receiverTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_TEL
     *
     * @param receiverTel the value for ORD_ORDER.RECEIVER_TEL
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_MOBILE
     *
     * @return the value of ORD_ORDER.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_MOBILE
     *
     * @param receiverMobile the value for ORD_ORDER.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_PROVINCE
     *
     * @return the value of ORD_ORDER.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverProvince() {
        return receiverProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_PROVINCE
     *
     * @param receiverProvince the value for ORD_ORDER.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_CITY
     *
     * @return the value of ORD_ORDER.RECEIVER_CITY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_CITY
     *
     * @param receiverCity the value for ORD_ORDER.RECEIVER_CITY
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_EXP_AREA
     *
     * @return the value of ORD_ORDER.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverExpArea() {
        return receiverExpArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_EXP_AREA
     *
     * @param receiverExpArea the value for ORD_ORDER.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverExpArea(String receiverExpArea) {
        this.receiverExpArea = receiverExpArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_ADDRESS
     *
     * @return the value of ORD_ORDER.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_ADDRESS
     *
     * @param receiverAddress the value for ORD_ORDER.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVER_POST_CODE
     *
     * @return the value of ORD_ORDER.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVER_POST_CODE
     *
     * @param receiverPostCode the value for ORD_ORDER.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.ORDER_MESSAGES
     *
     * @return the value of ORD_ORDER.ORDER_MESSAGES
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getOrderMessages() {
        return orderMessages;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.ORDER_MESSAGES
     *
     * @param orderMessages the value for ORD_ORDER.ORDER_MESSAGES
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setOrderMessages(String orderMessages) {
        this.orderMessages = orderMessages;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.MODIFY_REAL_MONEY_OP_ID
     *
     * @return the value of ORD_ORDER.MODIFY_REAL_MONEY_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getModifyRealMoneyOpId() {
        return modifyRealMoneyOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.MODIFY_REAL_MONEY_OP_ID
     *
     * @param modifyRealMoneyOpId the value for ORD_ORDER.MODIFY_REAL_MONEY_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setModifyRealMoneyOpId(Long modifyRealMoneyOpId) {
        this.modifyRealMoneyOpId = modifyRealMoneyOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     * @return the value of ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getCancelingOrderOpId() {
        return cancelingOrderOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     * @param cancelingOrderOpId the value for ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setCancelingOrderOpId(Long cancelingOrderOpId) {
        this.cancelingOrderOpId = cancelingOrderOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.CANCEL_REASON
     *
     * @return the value of ORD_ORDER.CANCEL_REASON
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.CANCEL_REASON
     *
     * @param cancelReason the value for ORD_ORDER.CANCEL_REASON
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.SEND_OP_ID
     *
     * @return the value of ORD_ORDER.SEND_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getSendOpId() {
        return sendOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.SEND_OP_ID
     *
     * @param sendOpId the value for ORD_ORDER.SEND_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setSendOpId(Long sendOpId) {
        this.sendOpId = sendOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ORDER.RECEIVED_OP_ID
     *
     * @return the value of ORD_ORDER.RECEIVED_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public Long getReceivedOpId() {
        return receivedOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ORDER.RECEIVED_OP_ID
     *
     * @param receivedOpId the value for ORD_ORDER.RECEIVED_OP_ID
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    public void setReceivedOpId(Long receivedOpId) {
        this.receivedOpId = receivedOpId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderCode=").append(orderCode);
        sb.append(", orderTitle=").append(orderTitle);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", realMoney=").append(realMoney);
        sb.append(", orderState=").append(orderState);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", payTime=").append(payTime);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", receivedTime=").append(receivedTime);
        sb.append(", closeTime=").append(closeTime);
        sb.append(", cancelTime=").append(cancelTime);
        sb.append(", logisticId=").append(logisticId);
        sb.append(", shipperCode=").append(shipperCode);
        sb.append(", shipperName=").append(shipperName);
        sb.append(", logisticCode=").append(logisticCode);
        sb.append(", receiverName=").append(receiverName);
        sb.append(", receiverTel=").append(receiverTel);
        sb.append(", receiverMobile=").append(receiverMobile);
        sb.append(", receiverProvince=").append(receiverProvince);
        sb.append(", receiverCity=").append(receiverCity);
        sb.append(", receiverExpArea=").append(receiverExpArea);
        sb.append(", receiverAddress=").append(receiverAddress);
        sb.append(", receiverPostCode=").append(receiverPostCode);
        sb.append(", orderMessages=").append(orderMessages);
        sb.append(", modifyRealMoneyOpId=").append(modifyRealMoneyOpId);
        sb.append(", cancelingOrderOpId=").append(cancelingOrderOpId);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", sendOpId=").append(sendOpId);
        sb.append(", receivedOpId=").append(receivedOpId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER
     *
     * @mbg.generated 2018-04-13 18:12:16
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
        OrdOrderMo other = (OrdOrderMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        ;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ORDER
     *
     * @mbg.generated 2018-04-13 18:12:16
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}