package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ModifyInviteIdTo {

    /**
     * 订单详情id
     */
    private Long id;

    /**
     * 邀请人id，用户id
     */
    private Long inviterId;

}
