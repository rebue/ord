package rebue.ord.to;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 创建时间：2018年4月19日 下午2:42:42 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdOrderReturnTo.java 类说明： 用户退货参数
 */
@Data
public class OrdOrderReturnTo {
    /**
     * 订单ID
     */
    private Long       orderId;

    /** 上线编号 */
    private Long       onlineId;

    /** 订单详情ID */
    private Long       orderDetailId;

    /** 退货ID */
    private Long       returnId;

    /** 退货数量(退货数量为空，则表示仅退款，不为空，则表示退货退款) */
    private Integer    returnNum;

    /**
     * 退款金额(自动计算退款必须填写，而自定义退款不能填写)
     * 退款金额 = 退款金额1(余额) + 退款金额2(返现金)
     */
    private BigDecimal refundAmount;
    /**
     * 退款金额1(余额)
     */
    private BigDecimal refundAmount1;
    /**
     * 退款金额2(返现金)
     */
    private BigDecimal refundAmount2;
    /**
     * 扣除补偿金额(扣除需补偿的金额，例如补偿运费)
     */
    private BigDecimal deductAmount;

    /** 规格名称 **/
//    private String     specName;

    /** 退货金额 **/
//    private BigDecimal returnPrice;

    /** 退货原因 **/
//    private String returnReason;

    /** 退货图片 **/
//    private String returnImg;

    /** 退货类型 **/
//    private Byte   returnType;

    /** 申请操作人编号 **/
//    private Long   userId;

    /** 操作人编号 **/
    private Long   opId;

    /** ip地址 **/
    private String ip;

}
