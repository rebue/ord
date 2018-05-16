package rebue.ord.ro;
/**  
* 创建时间：2018年5月16日 上午10:50:27  
* 项目名称：ord-api  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：ShipmentConfirmationRo.java  
* 类说明：  确认发货返回值
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.ShipmentConfirmationDic;

@JsonInclude(Include.NON_NULL)
public class ShipmentConfirmationRo {

	/** 确认发货返回值字典 **/
	private ShipmentConfirmationDic result;

	/** 确认发货返回值 **/
	private String msg;

	/**
	 * 物流订单ID (下单成功才会返回)
	 */
	private Long logisticId;

	/**
	 * 快递单号 (下单成功才会返回)
	 */
	private String logisticCode;

	/**
	 * 打印页面 (下单成功才会返回)
	 */
	private String printPage;

	/**
	 * 失败原因 (下单失败才会返回)
	 */
	private String failReason;

	public ShipmentConfirmationDic getResult() {
		return result;
	}

	public void setResult(ShipmentConfirmationDic result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getLogisticId() {
		return logisticId;
	}

	public void setLogisticId(Long logisticId) {
		this.logisticId = logisticId;
	}

	public String getLogisticCode() {
		return logisticCode;
	}

	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}

	public String getPrintPage() {
		return printPage;
	}

	public void setPrintPage(String printPage) {
		this.printPage = printPage;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Override
	public String toString() {
		return "ShipmentConfirmationRo [result=" + result + ", msg=" + msg + ", logisticId=" + logisticId
				+ ", logisticCode=" + logisticCode + ", printPage=" + printPage + ", failReason=" + failReason + "]";
	}

}
