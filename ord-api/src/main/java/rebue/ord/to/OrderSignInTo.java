package rebue.ord.to;

/**
 * 创建时间：2018年5月16日 上午11:59:23 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrderSignInTo.java 类说明： 订单签收请求参数
 */
public class OrderSignInTo {

	/** 订单编号 **/
	private String orderCode;

	/** 用户编号 **/
	private long userId;

	/** 用户ip地址 **/
	private String ip;

	/** 用户mac地址 **/
	private String mac;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toString() {
		return "OrderSignInTo [orderCode=" + orderCode + ", userId=" + userId + ", ip=" + ip + ", mac=" + mac + "]";
	}

}
