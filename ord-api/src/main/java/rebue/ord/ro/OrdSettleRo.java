package rebue.ord.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class OrdSettleRo {
	
	/**
	 * 已经结算的订单总额
	 */
	private Long AlreadySettle;
	
	/**
	 * 没有结算
	 */
	private Long notSettle;
		
	
}
