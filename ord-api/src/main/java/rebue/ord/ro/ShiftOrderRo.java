package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rebue.robotech.ro.Ro;

/**
 * 转移订单返回类
 * 
 * @author lbl
 *
 */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
public class ShiftOrderRo extends Ro {

    /**
     * 总返现金
     */
    private BigDecimal totalBack;

    /**
     * 总数量
     */
    private BigDecimal totalNumber;

    /**
     * 真实价格
     */
    private BigDecimal realMoney;

    /**
     * 用户ID
     *
     * 数据库字段: ORD_ORDER_DETAIL.USER_ID
     *
     */
    private Long userId;

    /**
     * 支付订单ID
     * 提供给第三方支付记录的订单ID（因为有可能会多笔订单合并支付）
     * 确认订单时，默认填写为订单ID(ORDER_ID)
     * 拆分订单时，拆分后的订单的支付订单ID仍为旧订单的支付订单ID不变
     */
    private Long payOrderId;

    /**
     * 详情信息
     */
    List<DetailAndRelationRo> detailInfo;

}
