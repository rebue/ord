package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 下单时订单详情的参数
 */
@JsonInclude(Include.NON_NULL)
@ToString
@Getter
@Setter
public class OrderDetailTo {
    /**
     * 上线ID
     */
    private Long    onlineId;

    /**
     * 上线规格名称
     */
    private Long    onlineSpecId;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 购物车ID
     */
    private Long    cartId;
    
    /**
     * 邀请人id
     */
    private Long  inviteId;

}
