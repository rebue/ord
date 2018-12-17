package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class OrdSettleRo {
	
	/**
	 * 已经结算的订单总额
	 */
	private BigDecimal AlreadySettle;
	
	/**
	 * 没有结算
	 */
	private BigDecimal notSettle;
		
	
}
