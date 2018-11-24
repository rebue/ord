package rebue.ord.to;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ReceivedAndRefundedTo {

	/**
	 * 退货id
	 */
	private Long id;

	/**
	 * 退款金额(自动计算退款) (单位为“元”，精确到小数点后2位)
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 退回到买家余额的金额(自定义退款) (单位为“元”，精确到小数点后2位)
	 * 
	 * @deprecated
	 */
	@Deprecated
	private BigDecimal returnBalanceToBuyer;
	
	/**
	 * 退回到买家返现金的金额(自定义退款) (单位为“元”，精确到小数点后2位)
	 * 
	 * @deprecated
	 */
	@Deprecated
	private BigDecimal returnCashbackToBuyer;
	
	/**
	 * 返回到卖家的补偿金(退货退款产生的需补偿给卖家的金额，例如补偿运费)
	 */
	private BigDecimal returnCompensationToSeller;
	
	/**
	 * 操作人id
	 */
	private Long opId;
	
	/**
	 * 操作人ip地址
	 */
	private String ip;
}
