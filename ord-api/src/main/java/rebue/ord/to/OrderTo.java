package rebue.ord.to;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rebue.afc.sgjz.dic.SgjzPayWayDic;

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
    private Long userId;

    /**
     * 是否是测试用户
     */
    private Boolean isTester;

    /**
     * 订单留言
     *
     */
    private String orderMessages;

    /**
     * 收货地址ID
     */
    private Long addrId;

    /**
     * 订单详情
     */
    private List<OrderDetailTo> details;

    /**
     * 是否是当场签收，数据库默认为false，如果是true的话在支付成功后将订单状态
     * 改为已签收。
     */
    private Boolean isNowReceived;

    /**
     * 是否是手工记账方式，是的话需要发布手工记账消息
     */
    private Boolean isSgjz;

    /**
     * 手工记账方式
     */
    private SgjzPayWayDic payWay;

}
