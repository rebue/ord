package rebue.ord;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


import rebue.wheel.OkhttpUtils;

/**  
* 创建时间：2018年4月21日 下午4:01:31  
* 项目名称：ord-svr  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：OrdReturnTest.java  
* 类说明：  退货测试
*/
public class OrdReturnTest {

	private String hostUrl = "http://localhost:20180";
	
	/*@Test
	public void selectReturnPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", 0);
		map.put("pageSize", 20);
		String result = OkhttpUtils.get(hostUrl + "/ord/return", map);
		System.out.println("非原始分页获取到的数据为：" + result);
		System.err.println("===================================================");
		String results = OkhttpUtils.get(hostUrl + "/ord/return/init", map);
		System.out.println("原始分页获取到的数据为：" + results);
	}*/
	
	@Test
	public void cancellationOfOrder() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 519010270442422272L);
		map.put("userId", 479952536770445317L);
		map.put("cancelReason", "买家取消订单");
		map.put("cancelingOrderOpId", 451273803712954379L);
		System.out.println("取消订单的参数为：{}" + String.valueOf(map));
		// 取消订单
		String results = OkhttpUtils.putByJsonParams(hostUrl + "/ord/order/cancel", map);
		System.out.println("取消订单的返回值为：" + results);
	}
}
  

