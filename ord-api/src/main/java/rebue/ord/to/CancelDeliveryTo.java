package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CancelDeliveryTo {

	/**
	 * 订单ID
	 *
	 * 数据库字段: ORD_ORDER.ID
	 */
	private Long id;

	/**
	 * 取消订单操作人ID
	 *
	 * 数据库字段: ORD_ORDER.CANCELING_ORDER_OP_ID
	 */
	private Long cancelingOrderOpId;

	/**
	 * 取消发货的原因
	 *
	 * 数据库字段: ORD_ORDER.CANCELDELI_REASON
	 */
	private String canceldeliReason;
	
	/**
	 * 操作人ip
	 */
	private String opIp;
}
