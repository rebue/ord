package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午9:42:12  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：UsersToPlaceTheOrderRo.java  
* 类说明：  用户下订单返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rebue.robotech.ro.Ro;

/**
 * 下单返回的对象
 */
@JsonInclude(Include.NON_NULL)
@ToString(callSuper = true)
@Getter
@Setter
public class OrderRo extends Ro {

//    /** 用户下订单返回值字典 **/
//    private OrderResultDic result;
//
//    /** 用户下订单返回值 **/
//    private String         msg;

    /** 订单编号 */
    private Long orderId;

}
