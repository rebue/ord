package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午10:19:02  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：SetUpExpressCompanyRo.java  
* 类说明：  设置快递公司返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.SetUpExpressCompanyDic;

@JsonInclude(Include.NON_NULL)
public class SetUpExpressCompanyRo {

	/** 设置快递公司返回值字典 **/
	private SetUpExpressCompanyDic result;

	/** 设置快递公司返回值 **/
	private String msg;

	public SetUpExpressCompanyDic getResult() {
		return result;
	}

	public void setResult(SetUpExpressCompanyDic result) {
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
		return "SetUpExpressCompanyRo [result=" + result + ", msg=" + msg + "]";
	}

}
