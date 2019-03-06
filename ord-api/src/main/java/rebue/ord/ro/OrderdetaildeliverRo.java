package rebue.ord.ro;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class OrderdetaildeliverRo {
	
	
	/**
	 * 发货表id
	 */
	private Long id;
	
	
	/**
	 * 上线标题
	 */
	private String onlineTitle;
	
	
	
	
	/**
	 * 物流编号
	 */
	private String  logisticCode;
	
	
}
