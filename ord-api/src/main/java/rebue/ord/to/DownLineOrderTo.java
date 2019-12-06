package rebue.ord.to;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rebue.afc.sgjz.dic.SgjzPayWayDic;

@JsonInclude(Include.NON_NULL)
@ToString
@Getter
@Setter
public class DownLineOrderTo {

    /**
     * 操作人id，使用这个操作id来获取他的组织来作为临时商品的上线组织
     * ，还有使用这个操作人id来查询这个订单是属于那个店铺的,注意公众号里面
     * 下单的话这个id是null。
     */
    private Long opId;
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
     * 订单详情
     */
    private List<OrderDetailTo> details;

    /**
     * 是否是手工记账方式，是的话需要发布手工记账消息
     */
    private Boolean isSgjz;

    /**
     * 手工记账方式
     */
    private SgjzPayWayDic payWay;
    
    /**
     * 是否当场签收
     */
    private Boolean       isNowReceived;

}
