package rebue.ord.jo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the ORD_ORDER database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_ORDER")
@Getter
@Setter
@ToString
public class OrdOrderJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  订单编号
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_CODE", nullable = false, length = 50)
    private String orderCode;

    /**
     *  订单标题
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_TITLE", nullable = false, length = 200)
    private String orderTitle;

    /**
     *  上线组织ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "ONLINE_ORG_ID", nullable = true, length = 19)
    private Long onlineOrgId;

    /**
     *  发货组织ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "DELIVER_ORG_ID", nullable = true, length = 19)
    private Long deliverOrgId;

    /**
     *  下单金额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_MONEY", nullable = false, precision = 50, scale = 4)
    private BigDecimal orderMoney;

    /**
     *  实际金额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "REAL_MONEY", nullable = false, precision = 50, scale = 4)
    private BigDecimal realMoney;

    /**
     *  退货总额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_TOTAL", nullable = false, precision = 50, scale = 4)
    private BigDecimal returnTotal;

    /**
     *  可退货金额1
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RETURN_AMOUNT1", nullable = true, precision = 50, scale = 4)
    private BigDecimal returnAmount1;

    /**
     *  可退货总额2
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RETURN_AMOUNT2", nullable = true, precision = 50, scale = 4)
    private BigDecimal returnAmount2;

    /**
     *  订单状态
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_STATE", nullable = false, length = 3)
    private Byte orderState;

    /**
     *  下单人用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false, length = 19)
    private Long userId;

    /**
     *  作废-下单人姓名
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "USER_NAME", nullable = true, length = 50)
    private String userName;

    /**
     *  下单时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_TIME", nullable = false, length = 19)
    @Temporal(TemporalType.DATE)
    private Date orderTime;

    /**
     *  支付订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "PAY_ORDER_ID", nullable = false, length = 19)
    private Long payOrderId;

    /**
     *  支付时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "PAY_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date payTime;

    /**
     *  发货时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "SEND_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date sendTime;

    /**
     *  签收时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVED_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date receivedTime;

    /**
     *  结算时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CLOSE_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date closeTime;

    /**
     *  作废时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCEL_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date cancelTime;

    /**
     *  作废-物流订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "LOGISTIC_ID", nullable = true, length = 19)
    private Long logisticId;

    /**
     *  作废-快递公司编号
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "SHIPPER_CODE", nullable = true, length = 10)
    private String shipperCode;

    /**
     *  作废-快递公司名称
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "SHIPPER_NAME", nullable = true, length = 100)
    private String shipperName;

    /**
     *  作废-快递单号
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "LOGISTIC_CODE", nullable = true, length = 30)
    private String logisticCode;

    /**
     *  收件人名称
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_NAME", nullable = true, length = 30)
    private String receiverName;

    /**
     *  收件人电话
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_TEL", nullable = true, length = 20)
    private String receiverTel;

    /**
     *  收件人手机
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_MOBILE", nullable = true, length = 20)
    private String receiverMobile;

    /**
     *  收件省
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_PROVINCE", nullable = true, length = 20)
    private String receiverProvince;

    /**
     *  收件市
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_CITY", nullable = true, length = 20)
    private String receiverCity;

    /**
     *  收件区
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_EXP_AREA", nullable = true, length = 20)
    private String receiverExpArea;

    /**
     *  收件人详细地址
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_ADDRESS", nullable = true, length = 100)
    private String receiverAddress;

    /**
     *  收件地邮编
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVER_POST_CODE", nullable = true, length = 6)
    private String receiverPostCode;

    /**
     *  订单留言
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "ORDER_MESSAGES", nullable = true, length = 500)
    private String orderMessages;

    /**
     *  修改实际金额操作人ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "MODIFY_REALVERY_MONEY_OP_ID", nullable = true, length = 19)
    private Long modifyRealveryMoneyOpId;

    /**
     *  取消订单操作人ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCELING_ORDER_OP_ID", nullable = true, length = 19)
    private Long cancelingOrderOpId;

    /**
     *  取消发货的原因
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCELDELI_REASON", nullable = true, length = 500)
    private String canceldeliReason;

    /**
     *  发货操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "SEND_OP_ID", nullable = true, length = 19)
    private Long sendOpId;

    /**
     *  签收人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVED_OP_ID", nullable = true, length = 19)
    private Long receivedOpId;

    /**
     *  作废原因
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCEL_REASON", nullable = true, length = 300)
    private String cancelReason;

    /**
     *  订单列表
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrdOrderDetailJo> ordOrderDetailList;

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrdOrderJo other = (OrdOrderJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
