package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


@Data
@JsonInclude(Include.NON_NULL)
public class SuspendSettleTaskTo {

	/**
     *    结算类型(交易类型中的几种结算类型)
     *
     *    数据库字段: ORD_SETTLE_TASK.TRADE_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte tradeType;
	
    /**
     * 订单id
     */
	private Long orderId;
}
