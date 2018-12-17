package rebue.ord.to;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 下单时的参数
 */
@JsonInclude(Include.NON_NULL)
@ToString
@Getter
@Setter
public class OrderTo {
    /**
     * 下单的用户ID
     */
    private Long                userId;

    /**
     * 是否是测试用户
     */
    private Boolean             isTester;

    /**
     * 订单留言
     *
     */
    private String              orderMessages;

    /**
     * 收货地址ID
     */
    private Long                addrId;

    /**
     * 订单详情
     */
    private List<OrderDetailTo> details;

}
