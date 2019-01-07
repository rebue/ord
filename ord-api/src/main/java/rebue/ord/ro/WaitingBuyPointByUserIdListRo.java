package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WaitingBuyPointByUserIdListRo {

	/**
	 * 订单编号
	 */
	private String orderCode;
	
	/**
	 * 上线标题
	 */
	private String onlineTitle;
	
	/**
	 * 规格名称
	 */
	private String specName;
	
	/**
	 * 积分总额
	 */
	private BigDecimal buyPointTotal;
}
