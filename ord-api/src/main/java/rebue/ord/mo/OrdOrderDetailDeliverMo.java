package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * 订单详情发货信息
 *
 * 数据库表: ORD_ORDER_DETAIL_DELIVER
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdOrderDetailDeliverMo implements Serializable {

    /**
     *    发货ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderId;

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long orderDetailId;

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long logisticId;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    发货ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    发货ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     *    订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getOrderDetailId() {
        return orderDetailId;
    }

    /**
     *    订单详情ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.ORDER_DETAIL_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getLogisticId() {
        return logisticId;
    }

    /**
     *    物流订单ID
     *
     *    数据库字段: ORD_ORDER_DETAIL_DELIVER.LOGISTIC_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLogisticId(Long logisticId) {
        this.logisticId = logisticId;
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
        sb.append(", orderId=").append(orderId);
        sb.append(", orderDetailId=").append(orderDetailId);
        sb.append(", logisticId=").append(logisticId);
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
        OrdOrderDetailDeliverMo other = (OrdOrderDetailDeliverMo) that;
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
