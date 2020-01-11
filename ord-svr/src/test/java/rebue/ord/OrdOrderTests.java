package rebue.ord;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import rebue.ord.to.DownLineOrderTo;
import rebue.ord.to.ModifyInviteIdTo;
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

    @Resource
    private OrdOrderDetailSvc svc;

    @Resource
    private Mapper dozerMapper;

    /**
     * 测试下单
     */
   // @Test
    public void test01() throws IOException {
        final List<OrderDetailTo> details = new LinkedList<>();
        final OrderTo orderTo = new OrderTo();
        orderTo.setUserId(1L); // 下单的用户ID
        orderTo.setDetails(details);
        orderTo.setAddrId(521997679778070533l);
        OrderDetailTo orderDetailTo = new OrderDetailTo();
        orderDetailTo.setOnlineId(675167520256294912L);
        orderDetailTo.setOnlineSpecId(675167522361835522L);
        orderDetailTo.setBuyCount(new BigDecimal("1"));
        orderDetailTo.setCartId(678400380287057920l);
        
        // 两个以上的详情进行拆单
        // orderDetailTo.setBuyCount(new BigDecimal("3"));
        
        // 错误的收货地址
        // orderTo.setAddrId(3l);
        
        // 错误的上线信息
        // orderDetailTo.setOnlineId(2L);
        
        // 错误的上线规格信息
        // orderDetailTo.setOnlineSpecId(2L);
        
        // 库存不足,修改了数据库的当前上线数量测试
        
        // 有邀请人的
        // orderDetailTo.setInviteId(123456l);
        
        // 两个以上不同订单详情
//        OrderDetailTo orderDetailTo2 = new OrderDetailTo();
//        orderDetailTo2.setOnlineId(676598832129245184L);
//        orderDetailTo2.setOnlineSpecId(676598833022631938L);
//        orderDetailTo2.setBuyCount(new BigDecimal("1"));
//        orderDetailTo2.setCartId(678400380287057920l);
        // 两个以上订单，其中一个是要拆分详情的
        // orderDetailTo2.setBuyCount(new BigDecimal("2"));
        
        // 两个以上不同订单详情,其中一个是普通商品
//        OrderDetailTo orderDetailTo2 = new OrderDetailTo();
//        orderDetailTo2.setOnlineId(676602966609887238L);
//        orderDetailTo2.setOnlineSpecId(676602967071260680L);
//        orderDetailTo2.setBuyCount(new BigDecimal("1"));
//        orderDetailTo2.setCartId(678400380287057920l);
        // 两个以上不同订单详情,其中一个是普通商品,且普通商品数量在2个以上
        // orderDetailTo2.setBuyCount(new BigDecimal("2"));
        
        
        details.add(orderDetailTo);
//        details.add(orderDetailTo2);
       

        // 下单
        order(orderTo);
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

    /**
     * 测试下单
     * 
     * @throws IOException
     */
    // @Test
    public void testOrder() throws IOException {

        final OrderTo orderTo = new OrderTo();
        orderTo.setOpId(581703841586741249l);
        orderTo.setIsNowReceived(true);
        orderTo.setIsSgjz(true);

        final List<OrderDetailTo> details = new LinkedList<>();

        OrderDetailTo orderDetailTo3 = new OrderDetailTo();
        orderDetailTo3.setGoodName("临时名称3");
        orderDetailTo3.setBuyCount(new BigDecimal("1"));
        orderDetailTo3.setBuyPrice(new BigDecimal("10"));
        orderDetailTo3.setIsTempGood(true);
        orderDetailTo3.setIsTempGood(true);

        OrderDetailTo orderDetailTo1 = new OrderDetailTo();
        orderDetailTo1.setGoodName("临时名称1");
        orderDetailTo1.setBuyCount(new BigDecimal("1"));
        orderDetailTo1.setBuyPrice(new BigDecimal("5"));
        orderDetailTo1.setIsTempGood(true);

        details.add(orderDetailTo1);
        details.add(orderDetailTo3);
        orderTo.setDetails(details);
        Ro ro = order(orderTo);
        _log.info("下单的结果为-{}", ro);
    }

    /**
     * 测试线下下单
     * 
     * @throws IOException
     */
  //   @Test
    public void testDownLineOrder() throws IOException {

        final DownLineOrderTo DownLineOrderTo = new DownLineOrderTo();
        DownLineOrderTo.setOpId(581703841586741249l);
        DownLineOrderTo.setIsNowReceived(true);
        DownLineOrderTo.setIsSgjz(false);
        final List<OrderDetailTo> details = new LinkedList<>();

        OrderDetailTo orderDetailTo3 = new OrderDetailTo();
        orderDetailTo3.setGoodName("临时名称3");
        orderDetailTo3.setBuyCount(new BigDecimal("1"));
        orderDetailTo3.setBuyPrice(new BigDecimal("10.5"));
        orderDetailTo3.setIsTempGood(true);

        OrderDetailTo orderDetailTo1 = new OrderDetailTo();
        orderDetailTo1.setGoodName("临时名称1");
        orderDetailTo1.setBuyCount(new BigDecimal("1"));
        orderDetailTo1.setBuyPrice(new BigDecimal("5.5"));
        orderDetailTo1.setIsTempGood(true);

        OrderDetailTo orderDetailTo2 = new OrderDetailTo();
        orderDetailTo2.setGoodName("线上商品");
        orderDetailTo2.setOnlineId(675167520256294912l);
        orderDetailTo2.setOnlineSpecId(675167522361835522l);
        orderDetailTo2.setProductId(675159114405511168l);
        orderDetailTo2.setProductSpecId(675159129106546689l);
        orderDetailTo2.setBuyCount(new BigDecimal("1"));
        orderDetailTo2.setBuyPrice(new BigDecimal("23"));
        orderDetailTo2.setIsTempGood(false);

        details.add(orderDetailTo1);
        details.add(orderDetailTo2);
        details.add(orderDetailTo3);
        DownLineOrderTo.setDetails(details);
        final OrderRo ro = _objectMapper.readValue(
                OkhttpUtils.postByJsonParams(hostUrl + "/ord/order/down-line-order", DownLineOrderTo), OrderRo.class);
        _log.info("下单的结果为-{}", ro);
    }

    // @Test
    public void modifyPayOrderIdTest() throws IOException {
        final String orderId = "520129105039982599";
        _log.info("修改支付订单Id的参数为: {}", orderId);
        final String results = OkhttpUtils.put(hostUrl + "/ord/order/modifypayorderid?id=" + orderId);
        _log.info("修改支付订单id的返回值为: {}", results);
    }
        
    
    /**
     * 测试线下下单
     * 这里和上面方法最大的区别是参数类型不一样，
     * 为了不改收银机前台的url而和线上下单公用一个方法。
     * @throws IOException
     */
   //  @Test
    public void testDownLineOrder2() throws IOException {

        final OrderTo DownLineOrderTo = new OrderTo();
        DownLineOrderTo.setOpId(581703841586741249l);
        DownLineOrderTo.setIsNowReceived(true);
        DownLineOrderTo.setIsSgjz(false);
        final List<OrderDetailTo> details = new LinkedList<>();

        OrderDetailTo orderDetailTo3 = new OrderDetailTo();
        orderDetailTo3.setGoodName("临时名称3");
        orderDetailTo3.setBuyCount(new BigDecimal("2"));
        orderDetailTo3.setBuyPrice(new BigDecimal("10"));
        orderDetailTo3.setIsTempGood(true);

        OrderDetailTo orderDetailTo1 = new OrderDetailTo();
        orderDetailTo1.setGoodName("临时名称1");
        orderDetailTo1.setBuyCount(new BigDecimal("1"));
        orderDetailTo1.setBuyPrice(new BigDecimal("5"));
        orderDetailTo1.setIsTempGood(true);

        OrderDetailTo orderDetailTo2 = new OrderDetailTo();
        orderDetailTo2.setGoodName("线上商品");
        orderDetailTo2.setOnlineId(675167520256294912l);
        orderDetailTo2.setOnlineSpecId(675167522361835522l);
        orderDetailTo2.setProductId(675159114405511168l);
        orderDetailTo2.setProductSpecId(675159129106546689l);
        orderDetailTo2.setBuyCount(new BigDecimal("2"));
        orderDetailTo2.setBuyPrice(new BigDecimal("23"));
        orderDetailTo2.setIsTempGood(false);

        details.add(orderDetailTo1);
        details.add(orderDetailTo2);
        details.add(orderDetailTo3);
        DownLineOrderTo.setDetails(details);
        final OrderRo ro = _objectMapper.readValue(
                OkhttpUtils.postByJsonParams(hostUrl + "/ord/order", DownLineOrderTo), OrderRo.class);
        _log.info("下单的结果为-{}", ro);
    }
     
    
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

    // @Test
    public void shiftOrderTest() throws IOException {
        String url = hostUrl + "/ord/order/shift?orderId=560764839904018433&oldUserId=560723287034822657";
        String str = OkhttpUtils.get(url);
        System.out.println(str);
    }

    // 测试根据用户id查询订单状态不为退货和未支付且支付时间为最新的订单信息
    // @Test
    public void latestOneByUserIdTest() throws IOException {
        String url = hostUrl + "/ord/order/getLatestOne?userId=560723287034822657";
        String orderMo = OkhttpUtils.get(url);
        System.out.println(orderMo);
    }

    /**
     * 支付完成通知
     * 
     * @throws IOException
     */
    // @Test
    public void handleOrderPaidNotify() throws IOException {
        String url = hostUrl + "/ord/order/handleOrderPaidNotify";
        PayDoneMsg payDoneMsg = new PayDoneMsg();
        payDoneMsg.setOrderId("679198097947099150");
        payDoneMsg.setUserId(-1l);
        payDoneMsg.setPayTime(new Date());
        payDoneMsg.setPayType(PayAndRefundTypeDic.VPAY);
        payDoneMsg.setPayAmount(new BigDecimal("200"));
        OkhttpUtils.putByJsonParams(url, payDoneMsg);
    }

    /**
     * 1:app支付成功后将订单的用户id转换为扫码支付的用户的id
     * 2:将购买关系中下家订单id是当前订单id的下家用户id改为扫码支付的用户id
     * 
     * @throws IOException
     */
    // @Test
    public void shiftOrder() throws IOException {
        String url = hostUrl
                + "/ord/order/shift?payOrderId=628468490273161218&oldUserId=515488916007157761&newUserId=621980963517366273";
        String orderMo = OkhttpUtils.get(url);
        System.out.println(orderMo);
    }

    // @Test
    public void getOrderDetailByOrderId() throws IOException {
        System.out.println("根据订单id获取订单详情");
        OrdOrderDetailMo[] result = _objectMapper.readValue(
                OkhttpUtils.get(hostUrl + "/ord/orderdetail/info?orderId=561030684311945221"),
                OrdOrderDetailMo[].class);
        System.out.println(result);
    }

    // @Test
    public void modifyInviteId() throws IOException {
        String url = hostUrl + "/ord/order-detail/modify-invite-id";
        final List<ModifyInviteIdTo> modifyInviteIdList = new LinkedList<>();
        ModifyInviteIdTo modifyInviteIdTo = new ModifyInviteIdTo();
        modifyInviteIdTo.setId(165465465435163541l);
        modifyInviteIdTo.setInviterId(46547647647l);
        modifyInviteIdList.add(modifyInviteIdTo);
        String result = OkhttpUtils.putByJsonParams(url, modifyInviteIdList);
        System.out.println(result);
    }

    // @Test
    public void exportData() throws IOException {
        String url = hostUrl + "/ord/export-data";
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        OkhttpUtils.postByJsonParams(url, paramsMap);

    }

    // @Test
    public void exportData２() throws IOException {
        String url = hostUrl + "/ord/export-data２";
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        OkhttpUtils.postByJsonParams(url, paramsMap);

    }

    // @Test
    public void TestMapper() {
        final Map<Long, Ro> onlineOrgs = new LinkedHashMap<>();

        Ro mo = new Ro();
        mo.setMsg("第一个");
        mo.setResult(ResultDic.SUCCESS);
        onlineOrgs.put(1l, mo);
        mo = dozerMapper.map(mo, Ro.class);
        mo.setMsg("第二个");
        mo.setResult(ResultDic.SUCCESS);
        onlineOrgs.put(2l, mo);
        System.out.println(onlineOrgs);

    }
    
    
    @Test
    public void TestBigDecimal() {
        BigDecimal b=new BigDecimal("19.4");
        BigDecimal  a =    b.multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println(a);

    }

}