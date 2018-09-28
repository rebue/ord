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
	 * 订单ID
	 */
	private long orderId;

	/** 订单编号 **/
	private long orderCode;

	/** 上线编号 **/
	private long onlineId;

	/** 订单详情ID **/
	private long orderDetailId;

	/** 退货数量 **/
	private int returnNum;

	/** 规格名称 **/
	private String specName;

	/** 退货金额 **/
	private BigDecimal returnPrice;

	/** 退货原因 **/
	private String returnReason;

	/** 退货图片 **/
	private String returnImg;

	/** 退货类型 **/
	private Byte returnType;

	/** 申请操作人编号 **/
	private long userId;

	/** 退货编号 **/
	private long returnCode;

	/** 操作人编号 **/
	private long opId;

	/** 退货金额（余额） **/
	private double returnAmount1;

	/** 退货金额（返现金） **/
	private double returnAmount2;

	/** 扣减返现金额 **/
	private double subtractCashback;

	/** ip地址 **/
	private String ip;

	/** mac地址 **/
	private String mac;

}
