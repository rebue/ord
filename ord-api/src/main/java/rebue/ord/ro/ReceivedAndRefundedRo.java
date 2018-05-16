package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 下午5:14:46  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：ReceivedAndRefundedRo.java  
* 类说明： 已收到货并退款返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.ReceivedAndRefundedDic;

@JsonInclude(Include.NON_NULL)
public class ReceivedAndRefundedRo {

	/** 已收到货并退款返回值字典 **/
	private ReceivedAndRefundedDic result;

	/** 已收到货并退款返回值 **/
	private String msg;

	public ReceivedAndRefundedDic getResult() {
		return result;
	}

	public void setResult(ReceivedAndRefundedDic result) {
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
		return "ReceivedAndRefundedRo [result=" + result + ", msg=" + msg + "]";
	}

}
