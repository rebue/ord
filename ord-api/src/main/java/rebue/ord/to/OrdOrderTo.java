package rebue.ord.to;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class OrdOrderTo {
	
	
	/**
	 * 定单id
	 */
    private  String    orderCode;
    
	/**
	 * 用户名
	 */
    private  String    userName;
    
    /**
     * 订单状态
     */
    private  Byte  orderState;
    
	/**
	 * 下单时间段 开始
	 */
    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTimeStart;
	/**
	 * 下单时间段 结束
	 */
    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTimeEnd;


}
