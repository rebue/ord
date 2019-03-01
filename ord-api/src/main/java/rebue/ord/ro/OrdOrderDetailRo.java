package rebue.ord.ro;



import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 订单详情ro，
 * @author jjl
 *
 */

@JsonInclude(Include.NON_NULL)
@Data
public class OrdOrderDetailRo {
		

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ID
     *
     */
    private Long id;

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ORDER_ID
     *
     */
    private Long orderId;

    /**
     *    上线规格ID(限购时获取用户购买数量时使用)
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_SPEC_ID
     *
     */
    private Long onlineSpecId;

    /**
     *    上线ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_ID
     *
     */
    private Long onlineId;




    /**
     *    版块类型（0：普通，1：全返）
     *
     *    数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
     *
     */
    private Byte subjectType;



    /**
     *    上线标题
     *
     *    数据库字段: ORD_ORDER_DETAIL.ONLINE_TITLE
     *
     */
    private String onlineTitle;

    /**
     *    规格名称
     *
     *    数据库字段: ORD_ORDER_DETAIL.SPEC_NAME
     *
     */
    private String specName;


    /**
     *    实际成交金额
     *
     *    数据库字段: ORD_ORDER_DETAIL.ACTUAL_AMOUNT
     *
     */
    private BigDecimal actualAmount;

    /**
     *    购买数量(实际数量=购买数量-退货数量)
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_COUNT
     *
     */
    private Integer buyCount;

    /**
     *    退货数量
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_COUNT
     *
     */
    private Integer returnCount;

    /**
     *    购买价格（单价）
     *
     *    数据库字段: ORD_ORDER_DETAIL.BUY_PRICE
     *
     */
    private BigDecimal buyPrice;

    /**
     *    成本价格（单个）
     *
     *    数据库字段: ORD_ORDER_DETAIL.COST_PRICE
     *
     */
    private BigDecimal costPrice;




    /**
     *    退货状态（0：未退货  1：退货中  2：已退货  3：部分已退）
     *
     *    数据库字段: ORD_ORDER_DETAIL.RETURN_STATE
     */
    private Byte returnState;

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ORDER_DETAIL.USER_ID
     */
    private Long userId;

    /**
     *    是否已发货
     *
     *    数据库字段: ORD_ORDER_DETAIL.IS_DELIVERED
     *
     */
    private Boolean isDelivered;

    
    /**
     * 	物流id
     */
    private Long logisticId;

    /**
     * 	物流id
     */
    private String logisticCode;
    
    /**
     * 快递公司名字
     */
    private String shipperName;
}
