package rebue.ord.ro;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 创建时间：2018年4月9日 上午10:11:02 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderRo.java 类说明： 订单信息
 */
@JsonInclude(Include.NON_NULL)
@Data
public class OrdOrderRo {

    /**
     * 商品ID
     */
    private long       onlineId;

    /**
     * 规格ID
     */
    private long       onlineSpecId;

    /**
     * 购物车ID
     */
    private long       cartId;

    /**
     * 上线标题
     */
    private String     onlineTitle;

    /**
     * 规格名称
     */
    private String     onlineSpec;

    /**
     * 产品ID
     */
    private long       productId;

    /**
     * 商品主图
     */
    private String     picPath;

    /**
     * 购买数量
     */
    private int        number;

    /**
     * 上线金额
     */
    private BigDecimal salePrice;

    /**
     * 返现金额
     */
    private BigDecimal cashbackAmount;

    /**
     * 订单总金额
     */
    private BigDecimal totalPrice;

    /**
     * 返现总额
     */
    private BigDecimal totalBack;

    /**
     * 订单总数量
     */
    private int        totalNumber;

    /**
     * 收货地址编号
     */
    private long       address;

    /**
     * 用户编号
     */
    private long       userId;

    /**
     * 用户名称
     */
    private String     userName;

    /**
     * 订单留言
     */
    private String     orderMessages;

    /**
     * 商品类型
     */
    private Byte       subjectType;
    
    /**
     *    订单编号
     *
     */
    private String orderCode;
    
    /**
     *    下单金额
     *
     */
    private BigDecimal orderMoney;

    /**
     *    实际金额

     */
    private BigDecimal realMoney;
    
    /**
     *    订单状态（-1：作废  1：已下单（待支付）  2：已支付（待发货）  3：已发货（待签收）  4：已签收（待结算）  5：已结算  ））
     *                -1：作废
     *                1：已下单（待支付）
     *                2：已支付（待发货）
     *                3：已发货（待签收）
     *                4：已签收（待结算）
     *                5：已结算
     *
     */
    private Byte orderState;
    
    /**
     *    下单时间
     *
     *    数据库字段: ORD_ORDER.ORDER_TIME
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

}
