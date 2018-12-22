package rebue.ord.jo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the ORD_BUY_RELATION database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_BUY_RELATION")
@Getter
@Setter
@ToString
public class OrdBuyRelationJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  订单购买关系ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  上家用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "UPLINE_USER_ID", nullable = false, length = 19)
    private Long uplineUserId;

    /**
     *  上家订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "UPLINE_ORDER_ID", nullable = false, length = 19)
    private Long uplineOrderId;

    /**
     *  下家用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "DOWNLINE_USER_ID", nullable = false, length = 19)
    private Long downlineUserId;

    /**
     *  下家订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "DOWNLINE_ORDER_ID", nullable = false, length = 19)
    private Long downlineOrderId;

    /**
     *  下家订单详情ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "DOWNLINE_ORDER_DETAIL_ID", nullable = false, length = 19)
    private Long downlineOrderDetailId;

    /**
     *  是否已签收
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "IS_SIGN_IN", nullable = false, length = 3)
    private Boolean isSignIn;

    /**
     *  关系来源
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RELATION_SOURCE", nullable = false, length = 3)
    private Byte relationSource;

    /**
     *  上家订单详情
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @JoinColumn(name = "UPLINE_ORDER_DETAIL_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private OrdOrderDetailJo uplineOrderDetail;

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
        OrdBuyRelationJo other = (OrdBuyRelationJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
