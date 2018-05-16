package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 下午4:43:58  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：AgreeToARefundRo.java  
* 类说明：  同意退款
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.AgreeToARefundDic;

@JsonInclude(Include.NON_NULL)
public class AgreeToARefundRo {

	/** 同意退款返回值字典 **/
	private AgreeToARefundDic result;

	/** 同意退款返回值 **/
	private String msg;

	public AgreeToARefundDic getResult() {
		return result;
	}

	public void setResult(AgreeToARefundDic result) {
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
		return "AgreeToARefundRo [result=" + result + ", msg=" + msg + "]";
	}

}
