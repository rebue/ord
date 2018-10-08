package rebue.ord.ctrl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.pagehelper.PageInfo;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rebue.ord.dic.CancelDeliveryDic;
import rebue.ord.dic.CancellationOfOrderDic;
import rebue.ord.dic.OrderSignInDic;
import rebue.ord.dic.ShipmentConfirmationDic;
import rebue.ord.dic.UsersToPlaceTheOrderDic;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.CancelDeliveryRo;
import rebue.ord.ro.CancellationOfOrderRo;
import rebue.ord.ro.ModifyOrderRealMoneyRo;
import rebue.ord.ro.OrderSignInRo;
import rebue.ord.ro.SetUpExpressCompanyRo;
import rebue.ord.ro.ShipmentConfirmationRo;
import rebue.ord.ro.UsersToPlaceTheOrderRo;
import rebue.ord.svc.OrdOrderSvc;
import rebue.ord.to.OrderSignInTo;
import rebue.ord.to.ShipmentConfirmationTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 订单信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdOrderCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdOrderCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdOrderSvc svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String _uniqueFilesName = "某字段内容";

    /**
     * 删除订单信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/order")
    Ro del(@RequestParam("id") java.lang.Long id) {
        _log.info("del OrdOrderMo by id: {}", id);
        int result = svc.del(id);
        Ro ro = new Ro();
        if (result == 1) {
            String msg = "删除成功";
            _log.info("{}: id-{}", msg, id);
            ro.setMsg(msg);
            ro.setResult(ResultDic.SUCCESS);
            return ro;
        } else {
            String msg = "删除失败，找不到该记录";
            _log.error("{}: id-{}", msg, id);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        }
    }

    /**
     * 查询订单信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/order")
    PageInfo<OrdOrderMo> list(OrdOrderMo mo, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null)
            pageNum = 1;
        if (pageSize == null)
            pageSize = 5;
        _log.info("list OrdOrderMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
        if (pageSize > 50) {
            String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        PageInfo<OrdOrderMo> result = svc.list(mo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 获取单个订单信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/order/getbyid")
    OrdOrderMo getById(@RequestParam("id") java.lang.Long id) {
        _log.info("get OrdOrderMo by id: " + id);
        return svc.getById(id);
    }

    /**
     *  修改订单实际金额信息 2018年4月12日14:51:59
     */
    @PutMapping("/ord/order")
    ModifyOrderRealMoneyRo modifyOrderRealMoney(@RequestBody OrdOrderMo vo) throws Exception {
        _log.info("修改订单实际金额的参数为：{}", vo);
        return svc.modifyOrderRealMoney(vo);
    }

    /**
     *  查询订单信息 Title: orderInfo Description:
     *
     *  @param qo
     *  @return
     *  @throws ParseException
     *  @throws IntrospectionException
     *  @throws InvocationTargetException
     *  @throws IllegalArgumentException
     *  @throws IllegalAccessException
     *  @date 2018年4月9日 下午3:06:37
     */
    @GetMapping("/ord/order/info")
    List<Map<String, Object>> orderInfo(@RequestParam Map<String, Object> map) throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询订单信息的参数为：{}", map.toString());
        List<Map<String, Object>> list = svc.selectOrderInfo(map);
        _log.info("查询订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     *  用户下订单 Title: placeOrder Description:
     *
     *  @param ro
     *  @return
     *  @throws IOException
     *  @throws JsonMappingException
     *  @throws JsonParseException
     *  @date 2018年4月9日 上午10:55:18
     */
    @SuppressWarnings("finally")
    @PostMapping("/ord/order")
    UsersToPlaceTheOrderRo usersToPlaceTheOrder(String orderJson) throws JsonParseException, JsonMappingException, IOException {
        _log.info("用户下订单的参数为：{}", orderJson);
        UsersToPlaceTheOrderRo placeTheOrderRo = new UsersToPlaceTheOrderRo();
        try {
            placeTheOrderRo = svc.usersToPlaceTheOrder(orderJson);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals("生成订单详情出错")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.CREATE_ORDER_DETAIL_ERROR);
                placeTheOrderRo.setMsg(msg);
            } else if (msg.contains("未上线")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.GOODS_NOT_ONLINE);
                placeTheOrderRo.setMsg(msg);
            } else if (msg.contains("购物车中找不到")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.GOODS_NOT_EXIST_CART);
                placeTheOrderRo.setMsg(msg);
            } else if (msg.contains("扣减上线数量失败")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.SUBTRACT_ONLINE_COUNT_ERROR);
                placeTheOrderRo.setMsg(msg);
            } else if (msg.equals("删除购物车失败")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.DELETE_CART_ERROR);
                placeTheOrderRo.setMsg(msg);
            } else if (msg.contains("库存不足")) {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.INSUFFICIENT_INVENTORY);
                placeTheOrderRo.setMsg(msg);
            } else {
                _log.error(msg);
                placeTheOrderRo.setResult(UsersToPlaceTheOrderDic.ERROR);
                placeTheOrderRo.setMsg("下订单失败");
            }
        } finally {
            _log.info("返回值为：{}", placeTheOrderRo);
            return placeTheOrderRo;
        }
    }

    /**
     *  用户取消订单 Title: cancellationOfOrder Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月9日 下午7:37:13
     */
    @SuppressWarnings("finally")
    @PutMapping("/ord/order/cancel")
    CancellationOfOrderRo cancellationOfOrder(@RequestBody OrdOrderMo qo) {
        _log.info("用户取消订单的参数为：{}", qo);
        CancellationOfOrderRo cancellationOfOrderRo = new CancellationOfOrderRo();
        try {
            cancellationOfOrderRo = svc.cancellationOfOrder(qo);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals("修改订单状态失败")) {
                cancellationOfOrderRo.setResult(CancellationOfOrderDic.MODIFY_ORDER_STATE_ERROR);
                cancellationOfOrderRo.setMsg(msg);
            } else {
                cancellationOfOrderRo.setResult(CancellationOfOrderDic.ERROR);
                cancellationOfOrderRo.setMsg("取消订单失败");
            }
        } finally {
            return cancellationOfOrderRo;
        }
    }

    /**
     *  取消发货 Title: cancellationOfOrder Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月9日 下午7:37:13
     */
    @SuppressWarnings("finally")
    @PutMapping("/ord/order/canceldelivery")
    CancelDeliveryRo cancelDelivery(@RequestBody OrdOrderMo qo) {
        _log.info("用户取消订单的参数为：{}", qo);
        CancelDeliveryRo cancelDeliveryRo = new CancelDeliveryRo();
        try {
            cancelDeliveryRo = svc.cancelDelivery(qo);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals("修改订单状态失败")) {
                cancelDeliveryRo.setResult(CancelDeliveryDic.MODIFY_ORDER_STATE_ERROR);
                cancelDeliveryRo.setMsg(msg);
            } else {
                cancelDeliveryRo.setResult(CancelDeliveryDic.ERROR);
                cancelDeliveryRo.setMsg("取消发货失败");
            }
        } finally {
            return cancelDeliveryRo;
        }
    }

    /**
     *  设置快递公司 Title: setUpExpressCompany Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月13日 上午11:24:17
     */
    @PutMapping("/ord/order/setupexpresscompany")
    SetUpExpressCompanyRo setUpExpressCompany(OrdOrderMo qo) {
        _log.info("设置快递公司的参数为：{}", qo);
        return svc.setUpExpressCompany(qo);
    }

    /**
     *  确认发货 Title: sendAndPrint Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月13日 下午6:23:46
     */
    @SuppressWarnings("finally")
    @PutMapping("/ord/order/shipmentconfirmation")
    ShipmentConfirmationRo shipmentConfirmation(@RequestBody ShipmentConfirmationTo qo) {
        _log.info("确认发货的参数为：{}", qo);
        ShipmentConfirmationRo confirmationRo = new ShipmentConfirmationRo();
        try {
            confirmationRo = svc.shipmentConfirmation(qo);
            _log.info("确认发货的返回值为：{}", confirmationRo);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals("调用快递电子面单参数错误")) {
                confirmationRo.setResult(ShipmentConfirmationDic.PARAN_ERROR);
                confirmationRo.setMsg(msg);
                _log.error(msg);
            } else if (msg.equals("该订单已发货")) {
                confirmationRo.setResult(ShipmentConfirmationDic.ORDER_ALREADY_SHIPMENTS);
                confirmationRo.setMsg(msg);
                _log.error(msg);
            } else if (msg.equals("调用快递电子面单失败")) {
                confirmationRo.setResult(ShipmentConfirmationDic.INVOKE_ERROR);
                confirmationRo.setMsg(msg);
                _log.error(msg);
            } else {
                confirmationRo.setResult(ShipmentConfirmationDic.ERROR);
                confirmationRo.setMsg("确认发货失败");
                _log.error(msg);
            }
        } finally {
            return confirmationRo;
        }
    }

    /**
     *  订单签收 Title: orderSignIn Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月14日 下午2:30:53
     */
    @SuppressWarnings("finally")
    @PutMapping("/ord/order/ordersignin")
    OrderSignInRo orderSignIn(OrderSignInTo qo) {
        _log.info("订单签收的参数为：{}", qo.toString());
        OrderSignInRo orderSignInRo = new OrderSignInRo();
        try {
            orderSignInRo = svc.orderSignIn(qo);
        } catch (RuntimeException e) {
            orderSignInRo.setResult(OrderSignInDic.ERROR);
            orderSignInRo.setMsg("签收失败");
        } finally {
            return orderSignInRo;
        }
    }

    /**
     *  获取用户待返订单:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月14日 下午2:30:53
     */
    @GetMapping("/ord/order/getCashBackOrders")
    List<Map<String, Object>> getCashBackOrders(@RequestParam Map<String, Object> map) throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询订单信息的参数为：{}", map.toString());
        List<Map<String, Object>> list = svc.getCashBackOrder(map);
        _log.info("查询订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     * 根据定单编号获取单个订单信息
     */
    @GetMapping("/ord/getByOrderCode/{orderCode}")
    OrdOrderMo getByOrderCode(@PathVariable("orderCode") java.lang.Long orderCode) {
        _log.info("根据定单编号查找定单: " + orderCode);
        String orderCodeStr = String.valueOf(orderCode);
        OrdOrderMo mo = new OrdOrderMo();
        mo.setOrderCode(orderCodeStr);
        OrdOrderMo result = svc.getOne(mo);
        _log.info("get: " + result);
        return result;
    }
}
