package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 下午2:59:46  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：AddReturnRo.java  
* 类说明：  添加退货信息返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.AddReturnDic;

@JsonInclude(Include.NON_NULL)
public class AddReturnRo {

	/** 添加退货信息返回值字典 **/
	private AddReturnDic result;

	/** 添加退货信息返回值 **/
	private String msg;

	public AddReturnDic getResult() {
		return result;
	}

	public void setResult(AddReturnDic result) {
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
		return "AddReturnRo [result=" + result + ", msg=" + msg + "]";
	}

}
