package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单信息
 *
 * 数据库表: ORD_ORDER
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdOrderMo implements Serializable {

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    订单编号
     *
     *    数据库字段: ORD_ORDER.ORDER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String orderCode;

    /**
     *    订单标题
     *
     *    数据库字段: ORD_ORDER.ORDER_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String orderTitle;

    /**
     *    下单金额
     *
     *    数据库字段: ORD_ORDER.ORDER_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal orderMoney;

    /**
     *    实际金额
     *
     *    数据库字段: ORD_ORDER.REAL_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal realMoney;

    /**
     *    退货总额
     *
     *    数据库字段: ORD_ORDER.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal returnTotal;

    /**
     *    可退货金额1（退到返现金额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT1
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal returnAmount1;

    /**
     *    可退货总额2（退到余额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT2
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal returnAmount2;

    /**
     *    订单状态（-1：作废  1：已下单（待支付）  2：已支付（待发货）  3：已发货（待签收）  4：已签收（待结算）  5：已结算  ））
     *                -1：作废
     *                1：已下单（待支付）
     *                2：已支付（待发货）
     *                3：已发货（待签收）
     *                4：已签收（待结算）
     *                5：已结算
     *
     *    数据库字段: ORD_ORDER.ORDER_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte orderState;

    /**
     *    用户编号
     *
     *    数据库字段: ORD_ORDER.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long userId;

    /**
     *    作废-下单人姓名
     *
     *    数据库字段: ORD_ORDER.USER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String userName;

    /**
     *    下单时间
     *
     *    数据库字段: ORD_ORDER.ORDER_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    /**
     *    支付时间
     *
     *    数据库字段: ORD_ORDER.PAY_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     *    发货时间
     *
     *    数据库字段: ORD_ORDER.SEND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     *    签收时间
     *
     *    数据库字段: ORD_ORDER.RECEIVED_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receivedTime;

    /**
     *    结算时间
     *
     *    数据库字段: ORD_ORDER.CLOSE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime;

    /**
     *    作废时间
     *
     *    数据库字段: ORD_ORDER.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long logisticId;

    /**
     *    快递公司编号
     *
     *    数据库字段: ORD_ORDER.SHIPPER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String shipperCode;

    /**
     *    快递公司名称
     *
     *    数据库字段: ORD_ORDER.SHIPPER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String shipperName;

    /**
     *    快递单号
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String logisticCode;

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ORDER.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverName;

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ORDER.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverTel;

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ORDER.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverMobile;

    /**
     *    收件省
     *
     *    数据库字段: ORD_ORDER.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverProvince;

    /**
     *    收件市
     *
     *    数据库字段: ORD_ORDER.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverCity;

    /**
     *    收件区
     *
     *    数据库字段: ORD_ORDER.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverExpArea;

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ORDER.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverAddress;

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ORDER.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverPostCode;

    /**
     *    订单留言
     *
     *    数据库字段: ORD_ORDER.ORDER_MESSAGES
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String orderMessages;

    /**
     *    修改实际金额操作人ID
     *
     *    数据库字段: ORD_ORDER.MODIFY_REALVERY_MONEY_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long modifyRealveryMoneyOpId;

    /**
     *    取消订单操作人ID
     *
     *    数据库字段: ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long cancelingOrderOpId;

    /**
     *    取消发货的原因
     *
     *    数据库字段: ORD_ORDER.CANCELDELI_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String canceldeliReason;

    /**
     *    发货操作人
     *
     *    数据库字段: ORD_ORDER.SEND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long sendOpId;

    /**
     *    签收人
     *
     *    数据库字段: ORD_ORDER.RECEIVED_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long receivedOpId;

    /**
     *    作废原因
     *
     *    数据库字段: ORD_ORDER.CANCEL_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String cancelReason;

    /**
     *    支付订单ID
     *                提供给第三方支付记录的订单ID（因为有可能会多笔订单合并支付）
     *                确认订单时，默认填写为订单ID(ORDER_ID)
     *                拆分订单时，拆分后的订单的支付订单ID仍为旧订单的支付订单ID不变
     *
     *    数据库字段: ORD_ORDER.PAY_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long payOrderId;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    订单编号
     *
     *    数据库字段: ORD_ORDER.ORDER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     *    订单编号
     *
     *    数据库字段: ORD_ORDER.ORDER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     *    订单标题
     *
     *    数据库字段: ORD_ORDER.ORDER_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getOrderTitle() {
        return orderTitle;
    }

    /**
     *    订单标题
     *
     *    数据库字段: ORD_ORDER.ORDER_TITLE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    /**
     *    下单金额
     *
     *    数据库字段: ORD_ORDER.ORDER_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    /**
     *    下单金额
     *
     *    数据库字段: ORD_ORDER.ORDER_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    /**
     *    实际金额
     *
     *    数据库字段: ORD_ORDER.REAL_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getRealMoney() {
        return realMoney;
    }

    /**
     *    实际金额
     *
     *    数据库字段: ORD_ORDER.REAL_MONEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    /**
     *    退货总额
     *
     *    数据库字段: ORD_ORDER.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getReturnTotal() {
        return returnTotal;
    }

    /**
     *    退货总额
     *
     *    数据库字段: ORD_ORDER.RETURN_TOTAL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnTotal(BigDecimal returnTotal) {
        this.returnTotal = returnTotal;
    }

    /**
     *    可退货金额1（退到返现金额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT1
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getReturnAmount1() {
        return returnAmount1;
    }

    /**
     *    可退货金额1（退到返现金额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT1
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnAmount1(BigDecimal returnAmount1) {
        this.returnAmount1 = returnAmount1;
    }

    /**
     *    可退货总额2（退到余额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT2
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public BigDecimal getReturnAmount2() {
        return returnAmount2;
    }

    /**
     *    可退货总额2（退到余额）
     *
     *    数据库字段: ORD_ORDER.RETURN_AMOUNT2
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnAmount2(BigDecimal returnAmount2) {
        this.returnAmount2 = returnAmount2;
    }

    /**
     *    订单状态（-1：作废  1：已下单（待支付）  2：已支付（待发货）  3：已发货（待签收）  4：已签收（待结算）  5：已结算  ））
     *                -1：作废
     *                1：已下单（待支付）
     *                2：已支付（待发货）
     *                3：已发货（待签收）
     *                4：已签收（待结算）
     *                5：已结算
     *
     *    数据库字段: ORD_ORDER.ORDER_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getOrderState() {
        return orderState;
    }

    /**
     *    订单状态（-1：作废  1：已下单（待支付）  2：已支付（待发货）  3：已发货（待签收）  4：已签收（待结算）  5：已结算  ））
     *                -1：作废
     *                1：已下单（待支付）
     *                2：已支付（待发货）
     *                3：已发货（待签收）
     *                4：已签收（待结算）
     *                5：已结算
     *
     *    数据库字段: ORD_ORDER.ORDER_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderState(Byte orderState) {
        this.orderState = orderState;
    }

    /**
     *    用户编号
     *
     *    数据库字段: ORD_ORDER.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *    用户编号
     *
     *    数据库字段: ORD_ORDER.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *    作废-下单人姓名
     *
     *    数据库字段: ORD_ORDER.USER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getUserName() {
        return userName;
    }

    /**
     *    作废-下单人姓名
     *
     *    数据库字段: ORD_ORDER.USER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *    下单时间
     *
     *    数据库字段: ORD_ORDER.ORDER_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     *    下单时间
     *
     *    数据库字段: ORD_ORDER.ORDER_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     *    支付时间
     *
     *    数据库字段: ORD_ORDER.PAY_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     *    支付时间
     *
     *    数据库字段: ORD_ORDER.PAY_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     *    发货时间
     *
     *    数据库字段: ORD_ORDER.SEND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     *    发货时间
     *
     *    数据库字段: ORD_ORDER.SEND_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     *    签收时间
     *
     *    数据库字段: ORD_ORDER.RECEIVED_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getReceivedTime() {
        return receivedTime;
    }

    /**
     *    签收时间
     *
     *    数据库字段: ORD_ORDER.RECEIVED_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    /**
     *    结算时间
     *
     *    数据库字段: ORD_ORDER.CLOSE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     *    结算时间
     *
     *    数据库字段: ORD_ORDER.CLOSE_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    /**
     *    作废时间
     *
     *    数据库字段: ORD_ORDER.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     *    作废时间
     *
     *    数据库字段: ORD_ORDER.CANCEL_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getLogisticId() {
        return logisticId;
    }

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLogisticId(Long logisticId) {
        this.logisticId = logisticId;
    }

    /**
     *    快递公司编号
     *
     *    数据库字段: ORD_ORDER.SHIPPER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getShipperCode() {
        return shipperCode;
    }

    /**
     *    快递公司编号
     *
     *    数据库字段: ORD_ORDER.SHIPPER_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    /**
     *    快递公司名称
     *
     *    数据库字段: ORD_ORDER.SHIPPER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     *    快递公司名称
     *
     *    数据库字段: ORD_ORDER.SHIPPER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     *    快递单号
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getLogisticCode() {
        return logisticCode;
    }

    /**
     *    快递单号
     *
     *    数据库字段: ORD_ORDER.LOGISTIC_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ORDER.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ORDER.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ORDER.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverTel() {
        return receiverTel;
    }

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ORDER.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ORDER.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ORDER.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    /**
     *    收件省
     *
     *    数据库字段: ORD_ORDER.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverProvince() {
        return receiverProvince;
    }

    /**
     *    收件省
     *
     *    数据库字段: ORD_ORDER.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    /**
     *    收件市
     *
     *    数据库字段: ORD_ORDER.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     *    收件市
     *
     *    数据库字段: ORD_ORDER.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     *    收件区
     *
     *    数据库字段: ORD_ORDER.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverExpArea() {
        return receiverExpArea;
    }

    /**
     *    收件区
     *
     *    数据库字段: ORD_ORDER.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverExpArea(String receiverExpArea) {
        this.receiverExpArea = receiverExpArea;
    }

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ORDER.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ORDER.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ORDER.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ORDER.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    /**
     *    订单留言
     *
     *    数据库字段: ORD_ORDER.ORDER_MESSAGES
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getOrderMessages() {
        return orderMessages;
    }

    /**
     *    订单留言
     *
     *    数据库字段: ORD_ORDER.ORDER_MESSAGES
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderMessages(String orderMessages) {
        this.orderMessages = orderMessages;
    }

    /**
     *    修改实际金额操作人ID
     *
     *    数据库字段: ORD_ORDER.MODIFY_REALVERY_MONEY_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getModifyRealveryMoneyOpId() {
        return modifyRealveryMoneyOpId;
    }

    /**
     *    修改实际金额操作人ID
     *
     *    数据库字段: ORD_ORDER.MODIFY_REALVERY_MONEY_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setModifyRealveryMoneyOpId(Long modifyRealveryMoneyOpId) {
        this.modifyRealveryMoneyOpId = modifyRealveryMoneyOpId;
    }

    /**
     *    取消订单操作人ID
     *
     *    数据库字段: ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getCancelingOrderOpId() {
        return cancelingOrderOpId;
    }

    /**
     *    取消订单操作人ID
     *
     *    数据库字段: ORD_ORDER.CANCELING_ORDER_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCancelingOrderOpId(Long cancelingOrderOpId) {
        this.cancelingOrderOpId = cancelingOrderOpId;
    }

    /**
     *    取消发货的原因
     *
     *    数据库字段: ORD_ORDER.CANCELDELI_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getCanceldeliReason() {
        return canceldeliReason;
    }

    /**
     *    取消发货的原因
     *
     *    数据库字段: ORD_ORDER.CANCELDELI_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCanceldeliReason(String canceldeliReason) {
        this.canceldeliReason = canceldeliReason;
    }

    /**
     *    发货操作人
     *
     *    数据库字段: ORD_ORDER.SEND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getSendOpId() {
        return sendOpId;
    }

    /**
     *    发货操作人
     *
     *    数据库字段: ORD_ORDER.SEND_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSendOpId(Long sendOpId) {
        this.sendOpId = sendOpId;
    }

    /**
     *    签收人
     *
     *    数据库字段: ORD_ORDER.RECEIVED_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getReceivedOpId() {
        return receivedOpId;
    }

    /**
     *    签收人
     *
     *    数据库字段: ORD_ORDER.RECEIVED_OP_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceivedOpId(Long receivedOpId) {
        this.receivedOpId = receivedOpId;
    }

    /**
     *    作废原因
     *
     *    数据库字段: ORD_ORDER.CANCEL_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     *    作废原因
     *
     *    数据库字段: ORD_ORDER.CANCEL_REASON
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     *    支付订单ID
     *                提供给第三方支付记录的订单ID（因为有可能会多笔订单合并支付）
     *                确认订单时，默认填写为订单ID(ORDER_ID)
     *                拆分订单时，拆分后的订单的支付订单ID仍为旧订单的支付订单ID不变
     *
     *    数据库字段: ORD_ORDER.PAY_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getPayOrderId() {
        return payOrderId;
    }

    /**
     *    支付订单ID
     *                提供给第三方支付记录的订单ID（因为有可能会多笔订单合并支付）
     *                确认订单时，默认填写为订单ID(ORDER_ID)
     *                拆分订单时，拆分后的订单的支付订单ID仍为旧订单的支付订单ID不变
     *
     *    数据库字段: ORD_ORDER.PAY_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setPayOrderId(Long payOrderId) {
        this.payOrderId = payOrderId;
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
        sb.append(", orderCode=").append(orderCode);
        sb.append(", orderTitle=").append(orderTitle);
        sb.append(", orderMoney=").append(orderMoney);
        sb.append(", realMoney=").append(realMoney);
        sb.append(", returnTotal=").append(returnTotal);
        sb.append(", returnAmount1=").append(returnAmount1);
        sb.append(", returnAmount2=").append(returnAmount2);
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
        sb.append(", modifyRealveryMoneyOpId=").append(modifyRealveryMoneyOpId);
        sb.append(", cancelingOrderOpId=").append(cancelingOrderOpId);
        sb.append(", canceldeliReason=").append(canceldeliReason);
        sb.append(", sendOpId=").append(sendOpId);
        sb.append(", receivedOpId=").append(receivedOpId);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", payOrderId=").append(payOrderId);
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
        OrdOrderMo other = (OrdOrderMo) that;
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
