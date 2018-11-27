package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import rebue.ord.mo.OrdReturnPicMo;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnPageListRo {

	/**
	 * 退货ID
	 *
	 * 数据库字段: ORD_RETURN.ID
	 */
	private Long id;

	/**
	 * 退货编号
	 *
	 * 数据库字段: ORD_RETURN.RETURN_CODE
	 */
	private Long returnCode;

	/**
	 * 订单ID
	 *
	 * 数据库字段: ORD_RETURN.ORDER_ID
	 */
	private Long orderId;

	/**
	 * 订单详情ID
	 *
	 * 数据库字段: ORD_RETURN.ORDER_DETAIL_ID
	 */
	private Long orderDetailId;

	/**
	 * 退货数量
	 *
	 * 数据库字段: ORD_RETURN.RETURN_COUNT
	 */
	private Integer returnCount;

	/**
	 * 退款总额（退款总额=退款余额+退款返现金+退款补偿金）
	 *
	 * 数据库字段: ORD_RETURN.REFUND_TOTAL
	 */
	private BigDecimal refundTotal;

	/**
	 * 退款补偿金额(退货退款产生的需补偿给卖家的金额，例如补偿运费)
	 *
	 * 数据库字段: ORD_RETURN.REFUND_COMPENSATION
	 */
	private BigDecimal refundCompensation;

	/**
	 * 退货类型（1：仅退款 2：退货并退款）
	 *
	 * 数据库字段: ORD_RETURN.RETURN_TYPE
	 */
	private Byte returnType;

	/**
	 * 申请状态（-1：已取消 1：待审核 2：退货中 3：已退货 4：已拒绝）
	 *
	 * 数据库字段: ORD_RETURN.APPLICATION_STATE
	 */
	private Byte applicationState;

	/**
	 * 退货原因
	 *
	 * 数据库字段: ORD_RETURN.RETURN_REASON
	 */
	private String returnReason;

	/**
	 * 用户ID
	 *
	 * 数据库字段: ORD_RETURN.USER_ID
	 */
	private Long userId;

	/**
	 * 申请操作人
	 *
	 * 数据库字段: ORD_RETURN.APPLICATION_OP_ID
	 */
	private Long applicationOpId;

	/**
	 * 申请时间
	 *
	 * 数据库字段: ORD_RETURN.APPLICATION_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date applicationTime;

	/**
	 * 取消操作人
	 *
	 * 数据库字段: ORD_RETURN.CANCEL_OP_ID
	 */
	private Long cancelOpId;

	/**
	 * 取消时间
	 *
	 * 数据库字段: ORD_RETURN.CANCEL_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date cancelTime;

	/**
	 * 审核操作人
	 *
	 * 数据库字段: ORD_RETURN.REVIEW_OP_ID
	 */
	private Long reviewOpId;

	/**
	 * 审核时间
	 *
	 * 数据库字段: ORD_RETURN.REVIEW_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date reviewTime;

	/**
	 * 退款操作人
	 *
	 * 数据库字段: ORD_RETURN.REFUND_OP_ID
	 */
	private Long refundOpId;

	/**
	 * 退款时间
	 *
	 * 数据库字段: ORD_RETURN.REFUND_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundTime;

	/**
	 * 拒绝操作人
	 *
	 * 数据库字段: ORD_RETURN.REJECT_OP_ID
	 */
	private Long rejectOpId;

	/**
	 * 拒绝原因
	 *
	 * 数据库字段: ORD_RETURN.REJECT_REASON
	 */
	private String rejectReason;

	/**
	 * 拒绝时间
	 *
	 * 数据库字段: ORD_RETURN.REJECT_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date rejectTime;

	/**
	 * 完成操作人
	 *
	 * 数据库字段: ORD_RETURN.FINISH_OP_ID
	 */
	private Long finishOpId;

	/**
	 * 完成时间
	 *
	 * 数据库字段: ORD_RETURN.FINISH_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date finishTime;

	/**
	 * 确认收到货操作人
	 *
	 * 数据库字段: ORD_RETURN.RECEIVE_OP_ID
	 */
	private Long receiveOpId;

	/**
	 * 确认收到货时间
	 *
	 * 数据库字段: ORD_RETURN.RECEIVE_TIME
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date receiveTime;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 上线id
	 */
	private Long onlineId;

	/**
	 * 产品id
	 */
	private Long productId;

	/**
	 * 上线标题
	 */
	private String onlineTitle;

	/**
	 * 上线规格id
	 */
	private String specName;

	/**
	 * 购买数量
	 */
	private Integer buyCount;

	/**
	 * 购买价格
	 */
	private BigDecimal buyPrice;

	/**
	 * 返现金额
	 */
	private BigDecimal cashbackAmount;

	/**
	 * 退货状态（0：未退货 1：退货中 2：已退货 3：部分已退）
	 */
	private Byte returnState;

	/**
	 * 版块类型（0：普通，1：全返）
	 *
	 * 数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
	 */
	private Byte subjectType;

	/**
	 * 返佣金名额
	 *
	 * 数据库字段: ORD_ORDER_DETAIL.COMMISSION_SLOT
	 */
	private Byte commissionSlot;

	/**
	 * 返佣金状态（0：匹配中，1：待返，2：已返）
	 *
	 * 数据库字段: ORD_ORDER_DETAIL.COMMISSION_STATE
	 */
	private Byte commissionState;

	/**
	 * 购买单位
	 */
	private String buyUnit;

	/**
	 * 订单编号
	 */
	private String orderCode;

	/**
	 * 签收时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date receivedTime;

	/**
	 * 订单状态（-1：作废 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算 ）
	 */
	private Byte orderState;
	
	/**
	 * 退货金额
	 */
	private BigDecimal returnRental;

	/**
	 * 退货图片
	 */
	private List<OrdReturnPicMo> picList;
}
