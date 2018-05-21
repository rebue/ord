package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Database Table Remarks:
 *   用户收货地址
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table ORD_ADDR
 *
 * @mbg.generated do_not_delete_during_merge 2018-05-21 11:37:34
 */
@ApiModel(value = "OrdAddrMo", description = "用户收货地址")
@JsonInclude(Include.NON_NULL)
public class OrdAddrMo implements Serializable {
    /**
     * Database Column Remarks:
     *   收货地址ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收货地址ID")
    private Long id;

    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.USER_ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * Database Column Remarks:
     *   收件人名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_NAME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件人名称")
    private String receiverName;

    /**
     * Database Column Remarks:
     *   收件人电话
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_TEL
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件人电话")
    private String receiverTel;

    /**
     * Database Column Remarks:
     *   收件人手机
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件人手机")
    private String receiverMobile;

    /**
     * Database Column Remarks:
     *   收件省
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件省")
    private String receiverProvince;

    /**
     * Database Column Remarks:
     *   收件市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_CITY
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件市")
    private String receiverCity;

    /**
     * Database Column Remarks:
     *   收件区/直辖镇
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件区/直辖镇")
    private String receiverExpArea;

    /**
     * Database Column Remarks:
     *   收件人详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件人详细地址")
    private String receiverAddress;

    /**
     * Database Column Remarks:
     *   收件地邮编
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "收件地邮编")
    private String receiverPostCode;

    /**
     * Database Column Remarks:
     *   是否为默认收货地址（0：否 1：默认）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.IS_DEF
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "是否为默认收货地址（0：否 1：默认）")
    private Boolean isDef;

    /**
     * Database Column Remarks:
     *   操作时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORD_ADDR.OP_TIME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @ApiModelProperty(value = "操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ORD_ADDR
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.ID
     *
     * @return the value of ORD_ADDR.ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.ID
     *
     * @param id the value for ORD_ADDR.ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.USER_ID
     *
     * @return the value of ORD_ADDR.USER_ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.USER_ID
     *
     * @param userId the value for ORD_ADDR.USER_ID
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_NAME
     *
     * @return the value of ORD_ADDR.RECEIVER_NAME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_NAME
     *
     * @param receiverName the value for ORD_ADDR.RECEIVER_NAME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_TEL
     *
     * @return the value of ORD_ADDR.RECEIVER_TEL
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverTel() {
        return receiverTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_TEL
     *
     * @param receiverTel the value for ORD_ADDR.RECEIVER_TEL
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_MOBILE
     *
     * @return the value of ORD_ADDR.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_MOBILE
     *
     * @param receiverMobile the value for ORD_ADDR.RECEIVER_MOBILE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_PROVINCE
     *
     * @return the value of ORD_ADDR.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverProvince() {
        return receiverProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_PROVINCE
     *
     * @param receiverProvince the value for ORD_ADDR.RECEIVER_PROVINCE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_CITY
     *
     * @return the value of ORD_ADDR.RECEIVER_CITY
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_CITY
     *
     * @param receiverCity the value for ORD_ADDR.RECEIVER_CITY
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_EXP_AREA
     *
     * @return the value of ORD_ADDR.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverExpArea() {
        return receiverExpArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_EXP_AREA
     *
     * @param receiverExpArea the value for ORD_ADDR.RECEIVER_EXP_AREA
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverExpArea(String receiverExpArea) {
        this.receiverExpArea = receiverExpArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_ADDRESS
     *
     * @return the value of ORD_ADDR.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_ADDRESS
     *
     * @param receiverAddress the value for ORD_ADDR.RECEIVER_ADDRESS
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.RECEIVER_POST_CODE
     *
     * @return the value of ORD_ADDR.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.RECEIVER_POST_CODE
     *
     * @param receiverPostCode the value for ORD_ADDR.RECEIVER_POST_CODE
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.IS_DEF
     *
     * @return the value of ORD_ADDR.IS_DEF
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public Boolean getIsDef() {
        return isDef;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.IS_DEF
     *
     * @param isDef the value for ORD_ADDR.IS_DEF
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setIsDef(Boolean isDef) {
        this.isDef = isDef;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORD_ADDR.OP_TIME
     *
     * @return the value of ORD_ADDR.OP_TIME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public Date getOpTime() {
        return opTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORD_ADDR.OP_TIME
     *
     * @param opTime the value for ORD_ADDR.OP_TIME
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ADDR
     *
     * @mbg.generated 2018-05-21 11:37:34
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
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ADDR
     *
     * @mbg.generated 2018-05-21 11:37:34
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
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        ;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORD_ADDR
     *
     * @mbg.generated 2018-05-21 11:37:34
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}