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
 * The persistent class for the ORD_ORDER_DETAIL_DELIVER database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_ORDER_DETAIL_DELIVER")
@Getter
@Setter
@ToString
public class OrdOrderDetailDeliverJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  发货ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  物流订单ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "LOGISTIC_ID", nullable = false, length = 19)
    private Long logisticId;

    /**
     *  订单
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private OrdOrderJo order;

    /**
     *  订单详情
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @JoinColumn(name = "ORDER_DETAIL_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private OrdOrderDetailJo orderDetail;

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
        OrdOrderDetailDeliverJo other = (OrdOrderDetailDeliverJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
