package rebue.ord;

import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Test;

import rebue.ord.to.AddReturnTo;
import rebue.ord.to.AgreeReturnTo;
import rebue.ord.to.ReceivedAndRefundedTo;
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
	
	/**
	 * 添加退货
	 * @throws IOException
	 */
//	@Test
	public void addReturnTest() throws IOException {
		AddReturnTo to = new AddReturnTo();
		to.setOrderId(525964777592324104L);
		to.setOrderDetailId(525964777642655754L);
		to.setReturnCount(1);
		to.setReturnType((byte) 2);
		to.setReturnReason("单元测试");
		to.setApplicationOpId(525616558689484801L);
		String result = OkhttpUtils.postByJsonParams(hostUrl + "/ord/return", to);
		System.out.println(result);
	}
	
	/**
	 * 同意退货测试
	 * @throws IOException 
	 */
//	@Test
	public void agreeToReturnTest() throws IOException {
		AgreeReturnTo to = new AgreeReturnTo();
		to.setId(542126533406490624L);
		to.setReviewOpId(520469568947224576L);
		String result = OkhttpUtils.postByJsonParams(hostUrl + "/ord/return/agreereturn", to);
		System.out.println(result);
	}
	
	/**
	 * 已收到货并退款测试
	 * @throws IOException
	 */
	@Test
	public void receivedAndRefundedTest() throws IOException {
		ReceivedAndRefundedTo to = new ReceivedAndRefundedTo();
		to.setId(542126533406490624L);
		to.setIp("192.168.1.40");
		to.setOpId(520469568947224576L);
		to.setRefundAmount(new BigDecimal("10"));
		to.setReturnCompensationToSeller(new BigDecimal("0"));
		String result = OkhttpUtils.postByJsonParams(hostUrl + "/ord/return/receivedandrefunded", to);
		System.out.println(result);
	}
}
  

