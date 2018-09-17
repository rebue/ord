package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * 下级购买信息
 *
 * 数据库表: ORD_SUBLEVEL_BUY
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdSublevelBuyMo implements Serializable {

    /**
     *    下级购买信息ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderDetailId;

    /**
     *    本级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long userId;

    /**
     *    下级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long sublevelUserId;

    /**
     *    下级订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long sublevelOrderDetailId;

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.IS_SIGN_IN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Boolean isSignIn;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    下级购买信息ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    下级购买信息ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderDetailId() {
        return orderDetailId;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     *    本级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *    本级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *    下级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getSublevelUserId() {
        return sublevelUserId;
    }

    /**
     *    下级用户ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSublevelUserId(Long sublevelUserId) {
        this.sublevelUserId = sublevelUserId;
    }

    /**
     *    下级订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getSublevelOrderDetailId() {
        return sublevelOrderDetailId;
    }

    /**
     *    下级订单详情ID
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.SUBLEVEL_ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSublevelOrderDetailId(Long sublevelOrderDetailId) {
        this.sublevelOrderDetailId = sublevelOrderDetailId;
    }

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.IS_SIGN_IN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Boolean getIsSignIn() {
        return isSignIn;
    }

    /**
     *    是否已签收
     *
     *    数据库字段: ORD_SUBLEVEL_BUY.IS_SIGN_IN
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderDetailId=").append(orderDetailId);
        sb.append(", userId=").append(userId);
        sb.append(", sublevelUserId=").append(sublevelUserId);
        sb.append(", sublevelOrderDetailId=").append(sublevelOrderDetailId);
        sb.append(", isSignIn=").append(isSignIn);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
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
        OrdSublevelBuyMo other = (OrdSublevelBuyMo) that;
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
}
