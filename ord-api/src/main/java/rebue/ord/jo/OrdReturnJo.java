package rebue.ord.jo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the ORD_RETURN database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_RETURN")
@Getter
@Setter
@ToString
public class OrdReturnJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  退货ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  退货编号
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_CODE", nullable = false, length = 19)
    private Long returnCode;

    /**
     *  订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_ID", nullable = false, length = 19)
    private Long orderId;

    /**
     *  订单详情ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_DETAIL_ID", nullable = false, length = 19)
    private Long orderDetailId;

    /**
     *  退货数量
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_COUNT", nullable = false, length = 10)
    private Integer returnCount;

    /**
     *  退款总额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REFUND_TOTAL", nullable = true, precision = 18, scale = 4)
    private BigDecimal refundTotal;

    /**
     *  退款补偿金额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REFUND_COMPENSATION", nullable = true, precision = 18, scale = 4)
    private BigDecimal refundCompensation;

    /**
     *  退货类型
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_TYPE", nullable = false, length = 3)
    private Byte returnType;

    /**
     *  申请状态
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "APPLICATION_STATE", nullable = false, length = 3)
    private Byte applicationState;

    /**
     *  退货原因
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_REASON", nullable = false, length = 500)
    private String returnReason;

    /**
     *  用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false, length = 19)
    private Long userId;

    /**
     *  申请操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "APPLICATION_OP_ID", nullable = false, length = 19)
    private Long applicationOpId;

    /**
     *  申请时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "APPLICATION_TIME", nullable = false, length = 19)
    @Temporal(TemporalType.DATE)
    private Date applicationTime;

    /**
     *  取消操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCEL_OP_ID", nullable = true, length = 19)
    private Long cancelOpId;

    /**
     *  取消时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "CANCEL_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date cancelTime;

    /**
     *  审核操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REVIEW_OP_ID", nullable = true, length = 19)
    private Long reviewOpId;

    /**
     *  审核时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REVIEW_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date reviewTime;

    /**
     *  退款操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REFUND_OP_ID", nullable = true, length = 19)
    private Long refundOpId;

    /**
     *  退款时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REFUND_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date refundTime;

    /**
     *  拒绝操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REJECT_OP_ID", nullable = true, length = 19)
    private Long rejectOpId;

    /**
     *  拒绝原因
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REJECT_REASON", nullable = true, length = 200)
    private String rejectReason;

    /**
     *  拒绝时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "REJECT_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date rejectTime;

    /**
     *  完成操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "FINISH_OP_ID", nullable = true, length = 19)
    private Long finishOpId;

    /**
     *  完成时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "FINISH_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date finishTime;

    /**
     *  确认收到货操作人
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVE_OP_ID", nullable = true, length = 19)
    private Long receiveOpId;

    /**
     *  确认收到货时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "RECEIVE_TIME", nullable = true, length = 19)
    @Temporal(TemporalType.DATE)
    private Date receiveTime;

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
        OrdReturnJo other = (OrdReturnJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
