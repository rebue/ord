package rebue.ord.to;

import java.math.BigDecimal;

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
    private Long onlineId;

    /**
     * 上线规格名称
     */
    private Long onlineSpecId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品规格ID
     */
    private Long productSpecId;

    /**
     * 购买数量
     */
    private BigDecimal buyCount;

    /**
     * 购物车ID
     */
    private Long cartId;

    /**
     * 邀请人id
     */
    private Long inviteId;

    /**
     * 购买价格
     */
    private BigDecimal buyPrice;

    /**
     * 购买单位
     */
    private String buyUnit;

    /**
     * 是否是临时商品
     */
    private boolean isTempGood;

}
