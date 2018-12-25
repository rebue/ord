package rebue.ord.to;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class GetSettleTotalTo {
		
    /**
     * 订单状态
     */
    private Byte   orderState;
	
    /**
     *    供应商ID
     *
     */
    private Long supplierId;
}
