package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 获取订单详情和订单详情的邀请人关系
 * 
 * @author xym
 *
 */
@JsonInclude(Include.NON_NULL)
@Data
public class DetailAndRelationRo {

    /**
     * 订单详情id
     */
    private Long id;

    /**
     * 购买数量(实际数量=购买数量-退货数量)
     *
     */
    private Integer buyCount;

    /**
     * 返现金额
     *
     * 数据库字段: ORD_ORDER_DETAIL.CASHBACK_AMOUNT
     *
     */
    private BigDecimal cashbackAmount;

    /**
     * 规格名称
     *
     * 数据库字段: ORD_ORDER_DETAIL.SPEC_NAME
     *
     */
    private String specName;

    /**
     * 上线标题
     *
     * 数据库字段: ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     */
    private String onlineTitle;

    /**
     * 上线图片
     */
    private String picPath;

    /**
     * 购买价格（单价）
     *
     */
    private BigDecimal buyPrice;

    /**
     * 版块类型（0：普通，1：全返）
     *
     */
    private Byte subjectType;

    /**
     * 详情中的邀请人是否已经存在
     */
    private boolean inviterExist;

    /**
     * 邀请人名字，用户名字
     */
    private String inviterName;

    /**
     * 邀请人id，用户id
     */
    private Long inviterId;

    /**
     * 邀请人微信头像
     */
    private String inviterWxFace;

}
