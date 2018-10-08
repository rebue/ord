package rebue.ord.mo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
用户商品购买关系

数据库表: ORD_GOODS_BUY_RELATION

@mbg.generated 自动生成的注释，如需修改本注释，请删除本行
*/
@JsonInclude(Include.NON_NULL)
public class OrdGoodsBuyRelationMo implements Serializable {
    /**
    商品购买关系ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private Long id;

    /**
    上家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.UPLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private Long uplineUserId;

    /**
    下家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.DOWNLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private Long downlineUserId;

    /**
    上线ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ONLINE_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private Long onlineId;

    /**
    销售价格
    
    数据库字段: ORD_GOODS_BUY_RELATION.SALE_PRICE
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private BigDecimal salePrice;

    /**
    创建时间
    
    数据库字段: ORD_GOODS_BUY_RELATION.CREATE_TIME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
    商品购买关系ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public Long getId() {
        return id;
    }

    /**
    商品购买关系ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setId(Long id) {
        this.id = id;
    }

    /**
    上家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.UPLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public Long getUplineUserId() {
        return uplineUserId;
    }

    /**
    上家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.UPLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setUplineUserId(Long uplineUserId) {
        this.uplineUserId = uplineUserId;
    }

    /**
    下家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.DOWNLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public Long getDownlineUserId() {
        return downlineUserId;
    }

    /**
    下家用户ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.DOWNLINE_USER_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setDownlineUserId(Long downlineUserId) {
        this.downlineUserId = downlineUserId;
    }

    /**
    上线ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ONLINE_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public Long getOnlineId() {
        return onlineId;
    }

    /**
    上线ID
    
    数据库字段: ORD_GOODS_BUY_RELATION.ONLINE_ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    /**
    销售价格
    
    数据库字段: ORD_GOODS_BUY_RELATION.SALE_PRICE
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
    销售价格
    
    数据库字段: ORD_GOODS_BUY_RELATION.SALE_PRICE
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
    创建时间
    
    数据库字段: ORD_GOODS_BUY_RELATION.CREATE_TIME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public Date getCreateTime() {
        return createTime;
    }

    /**
    创建时间
    
    数据库字段: ORD_GOODS_BUY_RELATION.CREATE_TIME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uplineUserId=").append(uplineUserId);
        sb.append(", downlineUserId=").append(downlineUserId);
        sb.append(", onlineId=").append(onlineId);
        sb.append(", salePrice=").append(salePrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
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
        OrdGoodsBuyRelationMo other = (OrdGoodsBuyRelationMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        ;
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}