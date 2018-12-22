package rebue.ord.jo;

import java.io.Serializable;
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
 * The persistent class for the ORD_ADDR database table.
 * @mbg.generated 自动生成，如需修改，请删除本行
 */
@Entity
@Table(name = "ORD_ADDR")
@Getter
@Setter
@ToString
public class OrdAddrJo implements Serializable {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *  收货地址ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false, length = 19)
    private Long id;

    /**
     *  用户ID
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false, length = 19)
    private Long userId;

    /**
     *  收件人名称
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RECEIVER_NAME", nullable = false, length = 30)
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
    @Basic(optional = false)
    @Column(name = "RECEIVER_PROVINCE", nullable = false, length = 20)
    private String receiverProvince;

    /**
     *  收件市
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RECEIVER_CITY", nullable = false, length = 20)
    private String receiverCity;

    /**
     *  收件区/直辖镇
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RECEIVER_EXP_AREA", nullable = false, length = 50)
    private String receiverExpArea;

    /**
     *  收件人详细地址
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "RECEIVER_ADDRESS", nullable = false, length = 100)
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
     *  是否为默认收货地址
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "IS_DEF", nullable = false, length = 3)
    private Boolean isDef;

    /**
     *  操作时间
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Basic(optional = false)
    @Column(name = "OP_TIME", nullable = false, length = 19)
    @Temporal(TemporalType.DATE)
    private Date opTime;

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
        OrdAddrJo other = (OrdAddrJo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
