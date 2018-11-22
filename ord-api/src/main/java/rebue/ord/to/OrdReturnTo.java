package rebue.ord.to;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import rebue.ord.mo.OrdReturnPicMo;

/**
 * 查询和拒绝退货传递的参数(TODO 为什么这两个方法会共用这个TO类???)
 */
@JsonInclude(Include.NON_NULL)
@Data
public class OrdReturnTo {

    /** 退货ID **/
    private Long                 id;

    /** 退货编号 **/
    private Long                 returnCode;

    /** 订单ID **/
    private Long                 orderId;

    /** 订单详情ID **/
    private Long                 orderDetailId;

    /** 退货数量 **/
    private Integer              returnCount;

    /** 退货总额 **/
    private BigDecimal           returnRental;

    /** 退货金额（余额） **/
    private BigDecimal           returnAmount1;

    /** 退货金额（返现金） **/
    private BigDecimal           returnAmount2;

    /** 退款类型（1：仅退款 2：退货并退款） **/
    private Byte                 returnType;

    /** 申请状态（-1：已取消 1：待审核 2：退款中 3：完成 4：已拒绝） **/
    private Byte                 applicationState;

    /** 退款状态（1：未退款 2、已退款） **/
    private Byte                 refundState;

    /** 退货原因 **/
    private String               returnReason;

    /** 申请操作人 **/
    private Long                 applicationOpId;

    /** 申请时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 applicationTime;

    /** 取消操作人 **/
    private Long                 cancelOpId;

    /** 取消时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 cancelTime;

    /** 审核操作人 **/
    private Long                 reviewOpId;

    /** 审核时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 reviewTime;

    /** 退款操作人 **/
    private Long                 refundOpId;

    /** 退款时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 refundTime;

    /** 拒绝操作人 **/
    private Long                 rejectOpId;

    /** 拒绝原因 **/
    private String               rejectReason;

    /** 用户名字 **/
    private String               userName;

    /** 拒绝时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 rejectTime;

    /** 完成操作人 **/
    private Long                 finishOpId;

    /** 完成时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 finishTime;

    /** 上线ID **/
    private Long                 onlineId;

    /** 产品ID **/
    private Long                 productId;

    /** 上线标题 **/
    private String               onlineTitle;

    /** 规格名称 **/
    private String               specName;

    /** 购买数量 **/
    private Integer              buyCount;

    /** 购买价格 **/
    private BigDecimal           buyPrice;

    /** 返现金额 **/
    private BigDecimal           cashbackAmount;

    /** 购买单位 **/
    private String               buyUnit;

    /** 退货状态（0：未退货 1：退货中 2：已退货） **/
    private Byte                 returnState;

    /**
     * 订单编号
     *
     */
    private String               orderCode;

    /** 签收时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                 receivedTime;

    /**
     * 订单状态（-1：作废 1：已下单（待支付） 2：已支付（待发货） 3：已发货（待签收） 4：已签收（待结算） 5：已结算 ））
     *
     */
    private Byte                 orderState;

    /**
     * 版块类型（0：普通，1：全返）
     *
     * 
     */
    private Byte                 subjectType;

    /**
     * 返佣金名额
     *
     */
    private Byte                 commissionSlot;

    /**
     * 返佣金状态（0：匹配中，1：待返，2：已返）
     */
    private Byte                 commissionState;

    private List<OrdReturnPicMo> picList;

}
