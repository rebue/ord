package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * 订单购买关系
 *
 * 数据库表: ORD_BUY_RELATION
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdBuyRelationMo implements Serializable {

    /**
     *    订单购买关系ID
     *
     *    数据库字段: ORD_BUY_RELATION.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    上家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long uplineUserId;

    /**
     *    上家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long uplineOrderDetailId;

    /**
     *    下家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long downlineUserId;

    /**
     *    下家订单ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long downlineOrderId;

    /**
     *    下家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long downlineOrderDetailId;

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_BUY_RELATION.IS_SIGN_IN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Boolean isSignIn;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    订单购买关系ID
     *
     *    数据库字段: ORD_BUY_RELATION.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    订单购买关系ID
     *
     *    数据库字段: ORD_BUY_RELATION.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    上家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUplineUserId() {
        return uplineUserId;
    }

    /**
     *    上家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUplineUserId(Long uplineUserId) {
        this.uplineUserId = uplineUserId;
    }

    /**
     *    上家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUplineOrderDetailId() {
        return uplineOrderDetailId;
    }

    /**
     *    上家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.UPLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUplineOrderDetailId(Long uplineOrderDetailId) {
        this.uplineOrderDetailId = uplineOrderDetailId;
    }

    /**
     *    下家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getDownlineUserId() {
        return downlineUserId;
    }

    /**
     *    下家用户ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setDownlineUserId(Long downlineUserId) {
        this.downlineUserId = downlineUserId;
    }

    /**
     *    下家订单ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getDownlineOrderId() {
        return downlineOrderId;
    }

    /**
     *    下家订单ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setDownlineOrderId(Long downlineOrderId) {
        this.downlineOrderId = downlineOrderId;
    }

    /**
     *    下家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getDownlineOrderDetailId() {
        return downlineOrderDetailId;
    }

    /**
     *    下家订单详情ID
     *
     *    数据库字段: ORD_BUY_RELATION.DOWNLINE_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setDownlineOrderDetailId(Long downlineOrderDetailId) {
        this.downlineOrderDetailId = downlineOrderDetailId;
    }

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_BUY_RELATION.IS_SIGN_IN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Boolean getIsSignIn() {
        return isSignIn;
    }

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_BUY_RELATION.IS_SIGN_IN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setIsSignIn(Boolean isSignIn) {
        this.isSignIn = isSignIn;
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrdBuyRelationMo other = (OrdBuyRelationMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    private Long uplineOrderId;

    public Long getUplineOrderId() {
        return uplineOrderId;
    }

    public void setUplineOrderId(Long uplineOrderId) {
        this.uplineOrderId = uplineOrderId;
    }

    @Override
    public String toString() {
        return "OrdBuyRelationMo [id=" + id + ", uplineUserId=" + uplineUserId + ", uplineOrderDetailId=" + uplineOrderDetailId + ", downlineUserId=" + downlineUserId + ", downlineOrderDetailId=" + downlineOrderDetailId + ", isSignIn=" + isSignIn + ", uplineOrderId=" + uplineOrderId + "]";
    }
}
