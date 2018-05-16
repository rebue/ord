package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午10:00:31  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：CancellationOfOrderRo.java  
* 类说明：  取消订单返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.CancellationOfOrderDic;

@JsonInclude(Include.NON_NULL)
public class CancellationOfOrderRo {

	/** 取消订单返回值字典 **/
	private CancellationOfOrderDic result;

	/** 取消订单返回值 **/
	private String msg;

	public CancellationOfOrderDic getResult() {
		return result;
	}

	public void setResult(CancellationOfOrderDic result) {
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
		return "CancellationOfOrderRo [result=" + result + ", msg=" + msg + "]";
	}

}
