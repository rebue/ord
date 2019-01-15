package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 创建时间：2018年4月9日 上午10:11:02 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderRo.java 类说明： 订单信息
 */
@JsonInclude(Include.NON_NULL)
@Data
public class OrdOrderRo {
    /**
     * ID
     */
    private long       Id;

    /**
     * 上线ID
     */
    private long       onlineId;

    /**
     * 规格ID
     */
    private long       onlineSpecId;

    /**
     * 购物车ID
     */
    private long       cartId;

    /**
     * 上线标题
     */
    private String     onlineTitle;

    /**
     * 规格名称
     */
    private String     onlineSpec;

    /**
     * 产品ID
     */
    private long       productId;

    /**
     * 商品主图
     */
    private String     picPath;

    /**
     * 购买数量
     */
    private int        number;

    /**
     * 上线金额
     */
    private BigDecimal salePrice;

    /**
     * 成本价格（单个）
     *
     * 数据库字段: ORD_ORDER_DETAIL.COST_PRICE
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal costPrice;

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
    private int        totalNumber;

    /**
     * 收货地址编号
     */
    private long       address;

    /**
     * 商品类型
     */
    private Byte       subjectType;

    /**
     * 订单编号
     *
     */
    private String     orderCode;

    /**
     * 订单标题
     *
     * 
     */
    private String     orderTitle;

    /**
     * 下单金额
     *
     */
    private BigDecimal orderMoney;

    /**
     * 实际金额
     * 
     */
    private BigDecimal realMoney;

    /**
     * 供应商ID
     *
     * 数据库字段: ORD_ORDER_DETAIL.SUPPLIER_ID
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long       supplierId;

    /**
     * 押货类型（1：押货 2：供应商发货）
     *
     * 数据库字段: ORD_ORDER_DETAIL.PLEDGE_TYPE
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte       pledgeType;

    /**
     * 订单状态（-1：作废 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算 ）） -1：作废
     * 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算
     *
     * 数据库字段: ORD_ORDER.ORDER_STATE
     *
     */
    private Byte       orderState;

    /**
     * 用户编号
     *
     * 数据库字段: ORD_ORDER.USER_ID
     *
     */
    private Long       userId;

    /**
     * 用户姓名
     *
     * 数据库字段: ORD_ORDER.USER_NAME
     *
     */
    private String     userName;

    /**
     * 下单时间
     *
     * 数据库字段: ORD_ORDER.ORDER_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       orderTime;

    /**
     * 支付时间
     *
     * 数据库字段: ORD_ORDER.PAY_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       payTime;

    /**
     * 发货时间
     *
     * 数据库字段: ORD_ORDER.SEND_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       sendTime;

    /**
     * 签收时间
     *
     * 数据库字段: ORD_ORDER.RECEIVED_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       receivedTime;

    /**
     * 结算时间
     *
     * 数据库字段: ORD_ORDER.CLOSE_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       closeTime;

    /**
     * 作废时间
     *
     * 数据库字段: ORD_ORDER.CANCEL_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date       cancelTime;

    /**
     * 物流订单ID
     *
     *
     */
    private Long       logisticId;

    /**
     * 快递公司编号
     *
     *
     */
    private String     shipperCode;

    /**
     * 快递公司名称
     *
     *
     */
    private String     shipperName;

    /**
     * 快递单号
     *
     *
     */
    private String     logisticCode;

    /**
     * 收件人名称
     *
     */
    private String     receiverName;

    /**
     * 收件人电话
     *
     */
    private String     receiverTel;

    /**
     * 收件人手机
     *
     */
    private String     receiverMobile;

    /**
     * 收件省
     *
     */
    private String     receiverProvince;

    /**
     * 收件市
     */
    private String     receiverCity;

    /**
     * 收件区
     *
     */
    private String     receiverExpArea;

    /**
     * 收件人详细地址
     *
     */
    private String     receiverAddress;

    /**
     * 收件地邮编
     *
     */
    private String     receiverPostCode;

    /**
     * 订单留言
     *
     */
    private String     orderMessages;

    /**
     * 修改实际金额操作人ID
     */
    private Long       modifyRealveryMoneyOpId;

    /**
     * 取消订单操作人ID
     *
     */
    private Long       cancelingOrderOpId;

    /**
     * 取消发货的原因
     *
     */
    private String     canceldeliReason;

    /**
     * 发货操作人
     *
     */
    private Long       sendOpId;

    /**
     * 签收人
     */
    private Long       receivedOpId;
    
    /**
     * 上线组织id
     */
    private Long              onlineOrgId;
    
    /**
     * 上线组织名字
     */
    private String  onlineOrgName;
    
    /**
     * 发货组织ID(默认填入上线组织ID，可变更为供应商的ID)
     *
     */
    private Long              deliverOrgId;
    
    /**
     * 发货组织名字
     */
    private String deliverOrgName;
    
    /**
     * 购买总成本价格
     */
   private  BigDecimal costPriveTotal;
    
    
}
