package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午10:13:16  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：ModifyOrderRealMoneyRo.java  
* 类说明：  修改订单实际金额返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.ModifyOrderRealMoneyDic;

@JsonInclude(Include.NON_NULL)
public class ModifyOrderRealMoneyRo {

	/** 修改订单实际金额返回值字典 **/
	private ModifyOrderRealMoneyDic result;

	/** 修改订单实际金额返回值 **/
	private String msg;

	public ModifyOrderRealMoneyDic getResult() {
		return result;
	}

	public void setResult(ModifyOrderRealMoneyDic result) {
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
		return "ModifyOrderRealMoneyRo [result=" + result + ", msg=" + msg + "]";
	}

}
