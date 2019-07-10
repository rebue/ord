package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 创建时间：2018年4月17日 下午5:00:55 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderAndOrderDetailRo.java 类说明：
 */
@Data
public class OrderDetailRo {

    /** 订单详情ID **/
    private Long id;

    /** 订单ID **/
    private Long orderId;

    /** 上线ID **/
    private Long onlineId;

    /** 产品ID **/
    private Long productId;

    /** 上线标题 **/
    private String onlineTitle;

    /** 规格名称 **/
    private String specName;

    /** 购买数量 **/
    private Integer buyCount;

    /** 购买价格 **/
    private BigDecimal buyPrice;

    /** 返现金额 **/
    private BigDecimal cashbackAmount;

    /** 返现总额 **/
    private BigDecimal cashbackTotal;

    /** 购买单位 **/
    private String buyUnit;

    /** 退货数量 **/
    private Integer returnCount;

    /** 退货状态（0：未退货 1：退货中 2：已退货） **/
    private Byte returnState;

    /**
     * 返现佣金名额
     */
    private Byte cashbackCommissionSlot;

    /**
     * 返现佣金状态（0：匹配中，1：待返，2：已返)
     */
    private Byte cashbackCommissionState;

    /** 商品主图 **/
    private String goodsQsmm;

    /** 商品类型 **/
    private Byte subjectType;

    /**
     * 实际成交金额
     *
     * 数据库字段: ORD_ORDER_DETAIL.ACTUAL_AMOUNT
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private BigDecimal actualAmount;

    /**
     * 购买关系
     */
    private List<OrdBuyRelationRo> ordBuyRelation;

    /**
     * 购买积分
     */
    private BigDecimal buyPoint;

    /**
     * 购买总积分
     */
    private BigDecimal buyPointTotal;

    /**
     * 支付顺序
     */
    private Byte paySeq;
}
