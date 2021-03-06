package rebue.ord.to;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
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

}
