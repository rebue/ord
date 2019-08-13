package rebue.ord.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ExportDataRo {

    private Long uplineUserId;
    private Long downlineUserId;
    private Long parentNodeId;
    private Long childrenNodeId;
    private Long groupId;
    private Long payTime;
    private Long parentOrderId;
    private byte relationSource;

}
