package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 下午3:20:28  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：RejectReturnRo.java  
* 类说明：  拒绝退货返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.RejectReturnDic;

@JsonInclude(Include.NON_NULL)
public class RejectReturnRo {

	/** 拒绝退货返回值字典 **/
	private RejectReturnDic result;

	/** 拒绝退货返回值 **/
	private String msg;

	public RejectReturnDic getResult() {
		return result;
	}

	public void setResult(RejectReturnDic result) {
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
		return "RejectReturnRo [result=" + result + ", msg=" + msg + "]";
	}

}
