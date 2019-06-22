package rebue.ord;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.Mapper;
import rebue.afc.dic.PayAndRefundTypeDic;
import rebue.afc.msg.PayDoneMsg;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.OrderRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.ord.to.OrderDetailTo;
import rebue.ord.to.OrderTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.wheel.OkhttpUtils;

/**
 * 订单测试
 */
public class OrdOrderTests {
	private final static Logger _log = LoggerFactory.getLogger(OrdOrderTests.class);

	private final String hostUrl = "http://localhost:20180";

	private ObjectMapper _objectMapper = new ObjectMapper();

	/**
	 * 测试下单
	 */
//    @Test
	public void test01() throws IOException {
		final List<OrderDetailTo> details = new LinkedList<>();
		final OrderTo orderTo = new OrderTo();
		orderTo.setUserId(515488916007157761L); // 下单的用户ID

		Ro ro = order(orderTo);
		Assert.assertEquals("参数错误", ro.getMsg());

		orderTo.setDetails(details);
		ro = order(orderTo);
		Assert.assertEquals("参数错误", ro.getMsg());

		orderTo.setAddrId(1L); // 错误的收货地址ID
		details.clear();
		OrderDetailTo orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(1L);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("参数错误", ro.getMsg());

		details.clear();
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(1L);
		orderDetailTo.setOnlineSpecId(1L);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("参数错误", ro.getMsg());

		details.clear();
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(1L);
		orderDetailTo.setOnlineSpecId(1L);
		orderDetailTo.setBuyCount(3);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("找不到上线的信息", ro.getMsg());

		details.clear();
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523717218609922183L);
		orderDetailTo.setOnlineSpecId(1L);
		orderDetailTo.setBuyCount(3);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("找不到上线规格的信息", ro.getMsg());

		details.clear();
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523717218609922183L);
		orderDetailTo.setOnlineSpecId(523717218643476617L);
		orderDetailTo.setBuyCount(Integer.MAX_VALUE);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("商品库存不足", ro.getMsg());

		details.clear();
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523717218609922183L);
		orderDetailTo.setOnlineSpecId(523717218643476617L);
		orderDetailTo.setBuyCount(3);
		details.add(orderDetailTo);
		ro = order(orderTo);
		Assert.assertEquals("找不到下单的收货地址信息", ro.getMsg());

		orderTo.setAddrId(521992662694232064L); // 正确的收货地址ID

		orderTo.setOrderMessages("订单留言"); // 订单留言
		details.clear();
		// 第一个订单详情
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523717218609922183L);
		orderDetailTo.setOnlineSpecId(523717218643476617L);
		orderDetailTo.setBuyCount(3);
		details.add(orderDetailTo);
		// 第二个订单详情
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523055292770942981L);
		orderDetailTo.setOnlineSpecId(523055292825468935L);
		orderDetailTo.setBuyCount(6);
		details.add(orderDetailTo);
		// 第三个订单详情
		orderDetailTo = new OrderDetailTo();
		orderDetailTo.setOnlineId(523055292770942981L);
		orderDetailTo.setOnlineSpecId(523055293202956297L);
		orderDetailTo.setBuyCount(9);
		details.add(orderDetailTo);
		// 下单
		ro = order(orderTo);
		Assert.assertEquals(ResultDic.SUCCESS, ro.getResult());
	}

	/**
	 * 请求下单
	 */
	private OrderRo order(final OrderTo to) throws IOException {
		_log.info("下单的参数为：{}", to);
		final OrderRo ro = _objectMapper.readValue(OkhttpUtils.postByJsonParams(hostUrl + "/ord/order", to),
				OrderRo.class);
		_log.info("获取到返回值: {}", ro);
		return ro;
	}

	// @Test
	public void modifyPayOrderIdTest() throws IOException {
		final String orderId = "520129105039982599";
		_log.info("修改支付订单Id的参数为: {}", orderId);
		final String results = OkhttpUtils.put(hostUrl + "/ord/order/modifypayorderid?id=" + orderId);
		_log.info("修改支付订单id的返回值为: {}", results);
	}

	@Resource
	private OrdOrderDetailSvc svc;

	@Resource
	private Mapper dozerMapper;

//	@Test
	public void updateOrderTime() throws IOException {
		_log.info("修改订单详情下单时间戳");
		String url = hostUrl + "/ord/detailList";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderTimestamp", 0L);
		// List<OrdOrderDetailMo> result = _objectMapper.readValue(OkhttpUtils.get(url),
		// List.class);

		OrdOrderDetailMo[] result = _objectMapper.readValue(OkhttpUtils.get(hostUrl + "/ord/detailList", map),
				OrdOrderDetailMo[].class);
		Long i = 0l;
		url = hostUrl + "/ord/orderdetail";
		for (OrdOrderDetailMo DetailMo : result) {
			_log.info("订单下单时间戳为0的详情: {}", DetailMo);
			i++;
			OrdOrderDetailMo modify = new OrdOrderDetailMo();
			modify.setId(DetailMo.getId());
			modify.setOrderTimestamp(i);
			String ro = OkhttpUtils.putByJsonParams(url, modify);
			_log.info("修改详情的结果为: {}", ro);
		}
	}

	//@Test
	public void shiftOrderTest() throws IOException {
		String url = hostUrl + "/ord/order/shift?orderId=560764839904018433&oldUserId=560723287034822657";
		String str = OkhttpUtils.get(url);
		System.out.println(str);
	}
	
	// 测试根据用户id查询订单状态不为退货和未支付且支付时间为最新的订单信息
	//@Test
	public void latestOneByUserIdTest() throws IOException {
		String url = hostUrl + "/ord/order/getLatestOne?userId=560723287034822657";
		String orderMo =OkhttpUtils.get(url);
		System.out.println(orderMo);
	}
	
	
	/**
	 * 支付完成通知
	 * @throws IOException
	 */
	@Test
	public void handleOrderPaidNotify()  throws IOException  {
			String url = hostUrl + "/ord/order/handleOrderPaidNotify";
			PayDoneMsg payDoneMsg=new PayDoneMsg();
			payDoneMsg.setOrderId("561030684311945221");
			payDoneMsg.setUserId(561030054302187520l);
			payDoneMsg.setPayTime(new Date());
			payDoneMsg.setPayType(PayAndRefundTypeDic.VPAY);
			payDoneMsg.setPayAmount(new BigDecimal("44"));
			 OkhttpUtils.putByJsonParams(url, payDoneMsg);
	}	
	
	
	/**
	 *  1:app支付成功后将订单的用户id转换为扫码支付的用户的id
	 *  2:将购买关系中下家订单id是当前订单id的下家用户id改为扫码支付的用户id
	 *  
	 * @throws IOException
	 */
//	@Test
	public void shiftOrder() throws IOException {
		String url = hostUrl + "/ord/order/shift?payOrderId=561030684311945221&oldUserId=561030054302187520&newUserId=560723287034822657";
		String orderMo =OkhttpUtils.get(url);
		System.out.println(orderMo);
	}
	

}