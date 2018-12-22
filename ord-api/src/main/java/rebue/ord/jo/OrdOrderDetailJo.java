package rebue.ord.jo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the ORD_ORDER_DETAIL database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_ORDER_DETAIL")
@Getter
@Setter
@ToString
public class OrdOrderDetailJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  订单详情ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  上线ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ONLINE_ID", nullable = false, length = 19)
    private Long onlineId;

    /**
     *  产品ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID", nullable = false, length = 19)
    private Long productId;

    /**
     *  产品规格ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "PRODUCT_SPEC_ID", nullable = true, length = 19)
    private Long productSpecId;

    /**
     *  版块类型
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "SUBJECT_TYPE", nullable = false, length = 3)
    private Byte subjectType;

    /**
     *  返佣金名额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "COMMISSION_SLOT", nullable = true, length = 3)
    private Byte commissionSlot;

    /**
     *  返佣金状态
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "COMMISSION_STATE", nullable = true, length = 3)
    private Byte commissionState;

    /**
     *  上线标题
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ONLINE_TITLE", nullable = false, length = 200)
    private String onlineTitle;

    /**
     *  规格名称
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "SPEC_NAME", nullable = false, length = 50)
    private String specName;

    /**
     *  购买数量
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "BUY_COUNT", nullable = false, length = 10)
    private Integer buyCount;

    /**
     *  购买价格
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "BUY_PRICE", nullable = false, precision = 18, scale = 4)
    private BigDecimal buyPrice;

    /**
     *  成本价格
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "COST_PRICE", nullable = true, precision = 18, scale = 4)
    private BigDecimal costPrice;

    /**
     *  供应商ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "SUPPLIER_ID", nullable = true, length = 19)
    private Long supplierId;

    /**
     *  发货组织ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "DELIVER_ORG_ID", nullable = false, length = 19)
    private Long deliverOrgId;

    /**
     *  返现金额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "CASHBACK_AMOUNT", nullable = false, precision = 18, scale = 4)
    private BigDecimal cashbackAmount;

    /**
     *  退货数量
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_COUNT", nullable = false, length = 10)
    private Integer returnCount;

    /**
     *  返现总额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "CASHBACK_TOTAL", nullable = false, precision = 18, scale = 4)
    private BigDecimal cashbackTotal;

    /**
     *  购买单位
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "BUY_UNIT", nullable = true, length = 10)
    private String buyUnit;

    /**
     *  退货状态
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RETURN_STATE", nullable = false, length = 3)
    private Byte returnState;

    /**
     *  用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false, length = 19)
    private Long userId;

    /**
     *  是否结算给买家
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "IS_SETTLE_BUYER", nullable = true, length = 3)
    private Boolean isSettleBuyer;

    /**
     *  实际成交金额
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "ACTUAL_AMOUNT", nullable = true, precision = 18, scale = 4)
    private BigDecimal actualAmount;

    /**
     *  是否已发货
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "IS_DELIVERED", nullable = true, length = 3)
    private Boolean isDelivered;

    /**
     *  上线规格ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ONLINE_SPEC_ID", nullable = false, length = 19)
    private Long onlineSpecId;

    /**
     *  下单时间戳
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "ORDER_TIMESTAMP", nullable = false, length = 19)
    private Long orderTimestamp;

    /**
     *  购买积分
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "BUY_POINT", nullable = true, precision = 18, scale = 4)
    private BigDecimal buyPoint;

    /**
     *  购买总积分
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "BUY_POINT_TOTAL", nullable = true, precision = 18, scale = 4)
    private BigDecimal buyPointTotal;

    /**
     *  支付顺序
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = true)
    @Column(name = "PAY_SEQ", nullable = true, length = 3)
    private Byte paySeq;

    /**
     *  订单
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private OrdOrderJo order;

    /**
     *  上家订单详情列表
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uplineOrderDetail")
    private List<OrdBuyRelationJo> ordBuyRelationList;

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
        OrdOrderDetailJo other = (OrdOrderDetailJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
