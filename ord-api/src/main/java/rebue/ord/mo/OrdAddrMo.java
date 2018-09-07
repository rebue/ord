package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户收货地址
 *
 * 数据库表: ORD_ADDR
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdAddrMo implements Serializable {

    /**
     *    收货地址ID
     *
     *    数据库字段: ORD_ADDR.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ADDR.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long userId;

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ADDR.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverName;

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ADDR.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverTel;

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ADDR.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverMobile;

    /**
     *    收件省
     *
     *    数据库字段: ORD_ADDR.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverProvince;

    /**
     *    收件市
     *
     *    数据库字段: ORD_ADDR.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverCity;

    /**
     *    收件区/直辖镇
     *
     *    数据库字段: ORD_ADDR.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverExpArea;

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ADDR.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverAddress;

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ADDR.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String receiverPostCode;

    /**
     *    是否为默认收货地址（0：否 1：默认）
     *
     *    数据库字段: ORD_ADDR.IS_DEF
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Boolean isDef;

    /**
     *    操作时间
     *
     *    数据库字段: ORD_ADDR.OP_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opTime;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    收货地址ID
     *
     *    数据库字段: ORD_ADDR.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    收货地址ID
     *
     *    数据库字段: ORD_ADDR.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ADDR.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *    用户ID
     *
     *    数据库字段: ORD_ADDR.USER_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ADDR.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     *    收件人名称
     *
     *    数据库字段: ORD_ADDR.RECEIVER_NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ADDR.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverTel() {
        return receiverTel;
    }

    /**
     *    收件人电话
     *
     *    数据库字段: ORD_ADDR.RECEIVER_TEL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ADDR.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     *    收件人手机
     *
     *    数据库字段: ORD_ADDR.RECEIVER_MOBILE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    /**
     *    收件省
     *
     *    数据库字段: ORD_ADDR.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverProvince() {
        return receiverProvince;
    }

    /**
     *    收件省
     *
     *    数据库字段: ORD_ADDR.RECEIVER_PROVINCE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    /**
     *    收件市
     *
     *    数据库字段: ORD_ADDR.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     *    收件市
     *
     *    数据库字段: ORD_ADDR.RECEIVER_CITY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     *    收件区/直辖镇
     *
     *    数据库字段: ORD_ADDR.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverExpArea() {
        return receiverExpArea;
    }

    /**
     *    收件区/直辖镇
     *
     *    数据库字段: ORD_ADDR.RECEIVER_EXP_AREA
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverExpArea(String receiverExpArea) {
        this.receiverExpArea = receiverExpArea;
    }

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ADDR.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     *    收件人详细地址
     *
     *    数据库字段: ORD_ADDR.RECEIVER_ADDRESS
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ADDR.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    /**
     *    收件地邮编
     *
     *    数据库字段: ORD_ADDR.RECEIVER_POST_CODE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    /**
     *    是否为默认收货地址（0：否 1：默认）
     *
     *    数据库字段: ORD_ADDR.IS_DEF
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Boolean getIsDef() {
        return isDef;
    }

    /**
     *    是否为默认收货地址（0：否 1：默认）
     *
     *    数据库字段: ORD_ADDR.IS_DEF
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setIsDef(Boolean isDef) {
        this.isDef = isDef;
    }

    /**
     *    操作时间
     *
     *    数据库字段: ORD_ADDR.OP_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Date getOpTime() {
        return opTime;
    }

    /**
     *    操作时间
     *
     *    数据库字段: ORD_ADDR.OP_TIME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setOpTime(Date opTime) {
        this.opTime = opTime;
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
        sb.append(", userId=").append(userId);
        sb.append(", receiverName=").append(receiverName);
        sb.append(", receiverTel=").append(receiverTel);
        sb.append(", receiverMobile=").append(receiverMobile);
        sb.append(", receiverProvince=").append(receiverProvince);
        sb.append(", receiverCity=").append(receiverCity);
        sb.append(", receiverExpArea=").append(receiverExpArea);
        sb.append(", receiverAddress=").append(receiverAddress);
        sb.append(", receiverPostCode=").append(receiverPostCode);
        sb.append(", isDef=").append(isDef);
        sb.append(", opTime=").append(opTime);
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
        OrdAddrMo other = (OrdAddrMo) that;
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
