package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 结算任务
 *
 * 数据库表: ORD_SETTLE_TASK
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdSettleTaskMo implements Serializable {

    /**
     *    任务ID
     *
     *    数据库字段: ORD_SETTLE_TASK.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    执行状态(-1:取消；0:未执行；1:已执行；暂停)
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte executeState;

    /**
     *    计划执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_PLAN_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executePlanTime;

    /**
     *    实际执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_FACT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeFactTime;

    /**
     *    结算类型(交易类型中的几种结算类型)
     *
     *    数据库字段: ORD_SETTLE_TASK.TRADE_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Byte tradeType;

    /**
     *    订单ID(销售订单ID)
     *
     *    数据库字段: ORD_SETTLE_TASK.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String orderId;

    /**
     *    IP地址
     *
     *    数据库字段: ORD_SETTLE_TASK.IP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String ip;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    任务ID
     *
     *    数据库字段: ORD_SETTLE_TASK.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    任务ID
     *
     *    数据库字段: ORD_SETTLE_TASK.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    执行状态(-1:取消；0:未执行；1:已执行；暂停)
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getExecuteState() {
        return executeState;
    }

    /**
     *    执行状态(-1:取消；0:未执行；1:已执行；暂停)
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_STATE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setExecuteState(Byte executeState) {
        this.executeState = executeState;
    }

    /**
     *    计划执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_PLAN_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getExecutePlanTime() {
        return executePlanTime;
    }

    /**
     *    计划执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_PLAN_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setExecutePlanTime(Date executePlanTime) {
        this.executePlanTime = executePlanTime;
    }

    /**
     *    实际执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_FACT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getExecuteFactTime() {
        return executeFactTime;
    }

    /**
     *    实际执行时间
     *
     *    数据库字段: ORD_SETTLE_TASK.EXECUTE_FACT_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setExecuteFactTime(Date executeFactTime) {
        this.executeFactTime = executeFactTime;
    }

    /**
     *    结算类型(交易类型中的几种结算类型)
     *
     *    数据库字段: ORD_SETTLE_TASK.TRADE_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Byte getTradeType() {
        return tradeType;
    }

    /**
     *    结算类型(交易类型中的几种结算类型)
     *
     *    数据库字段: ORD_SETTLE_TASK.TRADE_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    /**
     *    订单ID(销售订单ID)
     *
     *    数据库字段: ORD_SETTLE_TASK.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     *    订单ID(销售订单ID)
     *
     *    数据库字段: ORD_SETTLE_TASK.ORDER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     *    IP地址
     *
     *    数据库字段: ORD_SETTLE_TASK.IP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getIp() {
        return ip;
    }

    /**
     *    IP地址
     *
     *    数据库字段: ORD_SETTLE_TASK.IP
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setIp(String ip) {
        this.ip = ip;
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
        sb.append(", executeState=").append(executeState);
        sb.append(", executePlanTime=").append(executePlanTime);
        sb.append(", executeFactTime=").append(executeFactTime);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", orderId=").append(orderId);
        sb.append(", ip=").append(ip);
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
        OrdSettleTaskMo other = (OrdSettleTaskMo) that;
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
