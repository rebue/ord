package rebue.ord.to;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class UpdateOrgTo {
	
    /** 订单ID **/
    private Long                 id;
    
    /**供应商id**/
    private Long                 supplierId;
    
    
    /**发货组织id**/
    private Long                 deliverOrgId;
    
    
}