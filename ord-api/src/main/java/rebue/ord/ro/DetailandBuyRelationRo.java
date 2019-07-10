package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class DetailandBuyRelationRo {

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

    /** 成本价 **/
    private BigDecimal costPrice;

    /** 购买单位 **/
    private String buyUnit;

    /** 供应商名字 **/
    private String supplierName;

    /** 退货数量 **/
    private Integer returnCount;

    /** 退货状态（0：未退货 1：退货中 2：已退货） **/
    private Byte returnState;

    /**
     * 版块类型（0：普通，1：全返）
     */
    private Byte subjectType;

    /**
     * 返现佣金名额
     */
    private Byte commissionSlot;

    /**
     * 返现佣金状态（0：匹配中，1：待返，2：已返)
     */
    private Byte commissionState;

    /**
     * 上下家的关系信息。
     */
    List<Map<String, String>> relationInfo;

}