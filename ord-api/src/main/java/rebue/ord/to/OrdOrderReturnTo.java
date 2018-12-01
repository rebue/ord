package rebue.ord.to;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 创建时间：2018年4月19日 下午2:42:42 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderReturnTo.java 类说明： 用户退货参数
 */
@Data
public class OrdOrderReturnTo {
	/**
	 * 是否自动计算退款(true:自动计算退款 false:自定义退款)
	 */
	private Boolean isAutoCalcRefund;
	
	/**
	 * 订单ID
	 */
	private Long orderId;

	/** 订单详情ID */
	private Long orderDetailId;

	/** 退货ID */
	private Long returnId;

	/**
	 * 退货数量
	 */
	private Integer returnNum;

	/**
	 * 退款金额(自动计算退款必须填写，而自定义退款不能填写) 退款金额 = 退款金额1(余额) + 退款金额2(返现金)
	 */
	private BigDecimal refundAmount;
	/**
	 * 退款金额1(余额)
	 */
	private BigDecimal refundAmount1;
	/**
	 * 退款金额2(返现金)
	 */
	private BigDecimal refundAmount2;
	/**
	 * 退款补偿金额(退货退款产生的需补偿给卖家的金额，例如补偿运费)
	 */
	private BigDecimal refundCompensation;

	/** 操作人编号 **/
	private Long opId;

	/** ip地址 **/
	private String ip;

}
