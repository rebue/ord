package rebue.ord.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ord.dic.ShipmentConfirmationDic;

@JsonInclude(Include.NON_NULL)
public class BulkShipmentRo {
	/** 确认发货返回值字典 **/
	private ShipmentConfirmationDic result;

	/** 确认发货返回值 **/
	private String msg;

	/**
	 * 打印页面 (下单成功才会返回)
	 */
	private String printPage;

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

	public String getPrintPage() {
		return printPage;
	}

	public void setPrintPage(String printPage) {
		this.printPage = printPage;
	}

	@Override
	public String toString() {
		return "ShipmentConfirmationRo [result=" + result + ", msg=" + msg + ", printPage=" + printPage +  "]";
	}


}
