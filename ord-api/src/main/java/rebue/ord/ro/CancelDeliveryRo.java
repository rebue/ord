package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午10:26:40  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：CancelDeliveryRo.java  
* 类说明：  取消发货返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.CancelDeliveryDic;

@JsonInclude(Include.NON_NULL)
public class CancelDeliveryRo {

	/** 取消发货返回值字典 **/
	private CancelDeliveryDic result;

	/** 取消发货返回值 **/
	private String msg;

	public CancelDeliveryDic getResult() {
		return result;
	}

	public void setResult(CancelDeliveryDic result) {
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
		return "CancelDeliveryRo [result=" + result + ", msg=" + msg + "]";
	}

}
