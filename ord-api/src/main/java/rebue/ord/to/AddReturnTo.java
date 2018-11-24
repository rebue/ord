package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AddReturnTo {

	/**
	 * 订单id
	 */
	private Long orderId;
	
	/**
	 * 订单详情id
	 */
	private Long orderDetailId;
	
	/**
	 * 退货数量
	 */
	private Integer returnCount;
	
	/**
	 * 退货类型
	 */
	private Byte returnType;
	
	/**
	 * 退货原因
	 */
	private String returnReason;
	
	/**
	 * 申请操作人id
	 */
	private Long applicationOpId;
	
	/**
	 * 退货图片路径
	 */
	private String returnPicPath;
}
