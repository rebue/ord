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

import rebue.ord.dic.UsersToPlaceTheOrderDic;

@JsonInclude(Include.NON_NULL)
public class UsersToPlaceTheOrderRo {

	/** 用户下订单返回值字典 **/
	private UsersToPlaceTheOrderDic result;

	/** 用户下订单返回值 **/
	private String msg;

	/** 订单编号 **/
	private long orderId;

	public UsersToPlaceTheOrderDic getResult() {
		return result;
	}

	public void setResult(UsersToPlaceTheOrderDic result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "UsersToPlaceTheOrderRo [result=" + result + ", msg=" + msg + ", orderId=" + orderId + "]";
	}

}
