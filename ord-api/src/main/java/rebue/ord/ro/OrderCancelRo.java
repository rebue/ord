package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午11:29:37  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：OrderSignInRo.java  
* 类说明：  订单签收返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.OrderSignInDic;

@JsonInclude(Include.NON_NULL)
public class OrderCancelRo {

	/** 订单取消返回值字典 **/
	private OrderSignInDic result;

	/** 订单取消返回值 **/
	private String msg;

	public OrderSignInDic getResult() {
		return result;
	}

	public void setResult(OrderSignInDic result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "OrderSignInRo [result=" + result + ", msg=" + msg + "]";
	}

}
