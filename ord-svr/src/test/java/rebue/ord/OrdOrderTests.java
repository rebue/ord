package rebue.ord;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.ord.ro.OrderRo;
import rebue.ord.to.OrderDetailTo;
import rebue.ord.to.OrderTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.wheel.OkhttpUtils;

/**
 * 订单测试
 */
public class OrdOrderTests {
    private final static Logger _log          = LoggerFactory.getLogger(OrdOrderTests.class);

    private final String        hostUrl       = "http://localhost:20180";

    private final ObjectMapper  _objectMapper = new ObjectMapper();

    /**
     * 测试下单
     */
//    @Test
    public void test01() throws IOException {
        final List<OrderDetailTo> details = new LinkedList<>();
        final OrderTo orderTo = new OrderTo();
        orderTo.setUserId(515488916007157761L);                  // 下单的用户ID

        Ro ro = order(orderTo);
        Assert.assertEquals("参数错误", ro.getMsg());

        orderTo.setDetails(details);
        ro = order(orderTo);
        Assert.assertEquals("参数错误", ro.getMsg());

        orderTo.setAddrId(1L);                                   // 错误的收货地址ID
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

        orderTo.setAddrId(521992662694232064L);                              // 正确的收货地址ID

        orderTo.setOrderMessages("订单留言");                                 // 订单留言
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
        final OrderRo ro = _objectMapper.readValue(OkhttpUtils.postByJsonParams(hostUrl + "/ord/order", to), OrderRo.class);
        _log.info("获取到返回值: {}", ro);
        return ro;
    }
    
    @Test
    public void modifyPayOrderIdTest() throws IOException {
    	String orderId = "520129105039982599";
    	_log.info("修改支付订单Id的参数为: {}", orderId);
		String results = OkhttpUtils.put(hostUrl + "/ord/order/modifypayorderid?id=" + orderId);
		_log.info("修改支付订单id的返回值为: {}", results);
    }
}
