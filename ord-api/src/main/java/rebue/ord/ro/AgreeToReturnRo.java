package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 下午4:17:48  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：AgreeToReturnRo.java  
* 类说明：  同意退货返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.AgreeToReturnDic;

@JsonInclude(Include.NON_NULL)
public class AgreeToReturnRo {

	/** 同意退货返回值字典 **/
	private AgreeToReturnDic result;

	/** 同意退货返回值 **/
	private String msg;

	public AgreeToReturnDic getResult() {
		return result;
	}

	public void setResult(AgreeToReturnDic result) {
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
		return "AgreeToReturnRo [result=" + result + ", msg=" + msg + "]";
	}

}
