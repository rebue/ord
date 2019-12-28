package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GetOderPointRo {
    
  
    private BigDecimal totalIncome;
    private BigDecimal point;
    private BigDecimal thisPoint; 
    
}
