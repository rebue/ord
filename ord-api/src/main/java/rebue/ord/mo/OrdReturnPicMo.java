package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * 退货图片
 *
 * 数据库表: ORD_RETURN_PIC
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class OrdReturnPicMo implements Serializable {

    /**
     *    退货图片ID
     *
     *    数据库字段: ORD_RETURN_PIC.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long id;

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN_PIC.RETURN_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private Long returnId;

    /**
     *    图片路径
     *
     *    数据库字段: ORD_RETURN_PIC.PIC_PATH
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String picPath;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    退货图片ID
     *
     *    数据库字段: ORD_RETURN_PIC.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getId() {
        return id;
    }

    /**
     *    退货图片ID
     *
     *    数据库字段: ORD_RETURN_PIC.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN_PIC.RETURN_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public Long getReturnId() {
        return returnId;
    }

    /**
     *    退货ID
     *
     *    数据库字段: ORD_RETURN_PIC.RETURN_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    /**
     *    图片路径
     *
     *    数据库字段: ORD_RETURN_PIC.PIC_PATH
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getPicPath() {
        return picPath;
    }

    /**
     *    图片路径
     *
     *    数据库字段: ORD_RETURN_PIC.PIC_PATH
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
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
        sb.append(", returnId=").append(returnId);
        sb.append(", picPath=").append(picPath);
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
        OrdReturnPicMo other = (OrdReturnPicMo) that;
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
