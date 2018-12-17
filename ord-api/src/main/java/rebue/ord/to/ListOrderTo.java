package rebue.ord.to;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询订单列表的传输对象
 */
@JsonInclude(Include.NON_NULL)
@Data
public class ListOrderTo {
	
	private Long id;	
    /**
     * 所属组织ID
     */
    private Long   orgId;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 订单状态
     */
    private Byte   orderState;

    /**
     * 下单时间段 开始
     */
    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date   orderTimeStart;
    /**
     * 下单时间段 结束
     */
    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date   orderTimeEnd;

}
