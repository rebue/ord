package rebue.ord.ctrl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import rebue.ord.dic.AddReturnDic;
import rebue.ord.dic.AgreeToReturnDic;
import rebue.ord.dic.ReceivedAndRefundedDic;
import rebue.ord.dic.RejectReturnDic;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrdReturnRo2;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.ord.to.OrdReturnTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;
import rebue.wheel.AgentUtils;
import rebue.wheel.turing.JwtUtils;

/**
 * 用户退货信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdReturnCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log             = LoggerFactory.getLogger(OrdReturnCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdReturnSvc        svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final String        _uniqueFilesName = "某字段内容";

    /**
     * 是否测试模式（测试模式下不用从Cookie中获取用户ID）
     */
    @Value("${debug:false}")
    private Boolean             isDebug;

    /**
     * 前面经过的代理
     */
    @Value("${afc.passProxy:noproxy}")
    private String              passProxy;

    /**
     * 修改用户退货信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PutMapping("/ord/return")
    Ro modify(@RequestBody final OrdReturnMo mo) throws Exception {
        _log.info("modify OrdReturnMo: {}", mo);
        final Ro ro = new Ro();
        try {
            if (svc.modify(mo) == 1) {
                final String msg = "修改成功";
                _log.info("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.SUCCESS);
                return ro;
            } else {
                final String msg = "修改失败";
                _log.error("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.FAIL);
                return ro;
            }
        } catch (final DuplicateKeyException e) {
            final String msg = "修改失败，" + _uniqueFilesName + "已存在，不允许出现重复";
            _log.error("{}: mo-{}", msg, mo);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        } catch (final RuntimeException e) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String msg = "修改失败，出现运行时异常(" + sdf.format(new Date()) + ")";
            _log.error("{}: mo-{}", msg, mo);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        }
    }

    /**
     * 删除用户退货信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/return")
    Ro del(@RequestParam("id") final java.lang.Long id) {
        _log.info("del OrdReturnMo by id: {}", id);
        final int result = svc.del(id);
        final Ro ro = new Ro();
        if (result == 1) {
            final String msg = "删除成功";
            _log.info("{}: id-{}", msg, id);
            ro.setMsg(msg);
            ro.setResult(ResultDic.SUCCESS);
            return ro;
        } else {
            final String msg = "删除失败，找不到该记录";
            _log.error("{}: id-{}", msg, id);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        }
    }

    /**
     * 获取单个用户退货信息
     */
    @GetMapping("/ord/return/getbyid")
    OrdReturnRo2 getById(@RequestParam("id") final java.lang.Long id) {
        _log.info("get OrdReturnMo by id: " + id);
        final OrdReturnRo2 result = new OrdReturnRo2();
        final OrdReturnMo record = svc.getById(id);
        if (record == null) {
            result.setMsg("获取失败，找不到该记录");
            result.setRecord(record);
            result.setResult((byte) -1);
            return result;
        } else {
            result.setMsg("获取成功");
            result.setRecord(record);
            result.setResult((byte) 1);
            return result;
        }
    }

    /**
     * 添加用户退货信息 Title: add Description:
     *
     * @param vo
     * @return
     * @throws Exception
     * @date 2018年4月19日 下午2:53:39
     */
    @SuppressWarnings("finally")
    @PostMapping("/ord/return")
    AddReturnRo addReturn(final OrdOrderReturnTo vo) throws Exception {
        AddReturnRo addReturnRo = new AddReturnRo();
        try {
            addReturnRo = svc.addReturn(vo);
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            _log.info("添加退货信息出错：", e);
            if (msg.equals("添加退货图片失败")) {
                addReturnRo.setResult(AddReturnDic.ADD_RETURN_PIC);
                addReturnRo.setMsg(msg);
            } else if (msg.equals("修改订单详情状态失败")) {
                addReturnRo.setResult(AddReturnDic.MODIFY_ORDER_DETAIL_STATE_ERROR);
                addReturnRo.setMsg(msg);
            } else {
                e.printStackTrace();
                addReturnRo.setResult(AddReturnDic.ERROR);
                addReturnRo.setMsg("提交失败");
            }
        } catch (final Exception e) {
            _log.info("添加退货信息出错：", e);
        } finally {
            return addReturnRo;
        }
    }

    /**
     * 查询退货信息 Title: selectReturnPageList Description:
     *
     * @param mo
     * @param pageNum
     * @param pageSize
     * @return
     * @date 2018年4月21日 下午3:59:07
     */
    @GetMapping("/ord/return")
    PageInfo<OrdReturnTo> selectReturnPageList(final OrdReturnTo mo, @RequestParam("pageNum") final int pageNum, @RequestParam("pageSize") final int pageSize) {
        _log.info("list OrdReturnMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
        if (pageSize > 50) {
            final String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        final PageInfo<OrdReturnTo> result = svc.selectReturnPageList(mo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 拒绝退货 Title: rejectReturn Description:
     *
     * @param mo
     * @return
     * @date 2018年4月27日 下午3:31:38
     */
    @PutMapping("/ord/return/reject")
    RejectReturnRo rejectReturn(@RequestBody final OrdReturnTo mo) {
        _log.info("拒绝退货的参数为：{}", mo.toString());
        final RejectReturnRo rejectReturnRo = new RejectReturnRo();
        try {
            return svc.rejectReturn(mo);
        } catch (final RuntimeException e) {
            rejectReturnRo.setResult(RejectReturnDic.ERROR);
            rejectReturnRo.setMsg("操作失败");
            return rejectReturnRo;
        }
    }

    /**
     * 同意退款
     */
    @PostMapping("/ord/return/agreetoarefund")
    Ro agreeRefund(@RequestBody final OrdOrderReturnTo to, final HttpServletRequest req) throws NumberFormatException, ParseException {
        Ro ro = new Ro();

        if (!isDebug || to.getOpId() == null) {
            to.setOpId(JwtUtils.getJwtUserIdInCookie(req));
            to.setIp(AgentUtils.getIpAddr(req, passProxy));
        }
        _log.debug("获取当前用户ID: {}", to.getOpId());

        try {
            _log.info("同意退款的请求参数为：{}", to);
            ro = svc.agreeRefund(to);
            _log.info("同意退款的返回值为：{}", ro);
            return ro;
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }
    }

    /**
     * 同意退货 Title: agreeToReturn Description:
     *
     * @return
     * @date 2018年5月11日 下午3:29:33
     */
    @PostMapping("/ord/return/agreetoreturn")
    AgreeToReturnRo agreeToReturn(final OrdOrderReturnTo to) {
        AgreeToReturnRo agreeToReturnRo = new AgreeToReturnRo();
        try {
            _log.info("同意退货的请求参数为：{}", to.toString());
            agreeToReturnRo = svc.agreeReturn(to);
            _log.info("同意退货的返回值为：{}", agreeToReturnRo.toString());
            return agreeToReturnRo;
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            if (msg.equals("修改退货数量和返现总额出错")) {
                agreeToReturnRo.setResult(AgreeToReturnDic.MODIFY_RETURN_COUNT_AND_CASHBACK_TOTAL_AMOUNT_ERROR);
                agreeToReturnRo.setMsg(msg);
            } else if (msg.equals("修改退货信息错误")) {
                agreeToReturnRo.setResult(AgreeToReturnDic.MODIFY_RETURN_ERROR);
                agreeToReturnRo.setMsg(msg);
            } else {
                agreeToReturnRo.setResult(AgreeToReturnDic.ERROR);
                agreeToReturnRo.setMsg("操作失败");
            }
            return agreeToReturnRo;
        }
    }

    /**
     * 已收到货并退款 Title: receivedAndRefunded Description:
     *
     * @param to
     * @return
     * @date 2018年5月11日 下午3:01:21
     */
    @PostMapping("/ord/return/receivedandrefunded")
    ReceivedAndRefundedRo receivedAndRefunded(final OrdOrderReturnTo to) {
        ReceivedAndRefundedRo receivedAndRefundedRo = new ReceivedAndRefundedRo();
        try {
            _log.info("已收到货并退款的请求参数为：{}", to.toString());
            receivedAndRefundedRo = svc.receivedAndRefunded(to);
            _log.info("已收到货并退款的返回值为：{}", receivedAndRefundedRo.toString());
            return receivedAndRefundedRo;
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            if (msg.equals("修改订单详情退货状态出错")) {
                receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.MODIFY_ORDER_DETAIL_ERROR);
                receivedAndRefundedRo.setMsg(msg);
            } else if (msg.equals("确认收到货出错")) {
                receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.CONFIRM_RECEIPT_OF_GOODS_ERROR);
                receivedAndRefundedRo.setMsg(msg);
            } else if (msg.equals("v支付出错，退款失败")) {
                receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.V_PAY_ERROR);
                receivedAndRefundedRo.setMsg(msg);
            } else {
                receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ERROR);
                receivedAndRefundedRo.setMsg("退款失败");
            }
            return receivedAndRefundedRo;
        }
    }

    /**
     * 查询用户退货中订单信息
     * 
     * @param map
     * @return
     * @throws ParseException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    @GetMapping("/ord/order/returningInfo")
    List<Map<String, Object>> getReturningInfo(@RequestParam final Map<String, Object> map)
            throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询用户退货中订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = svc.selectReturningInfo(map);
        _log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     * 查询用户退货完成订单
     * 
     * @param map
     * @return
     * @throws ParseException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    @GetMapping("/ord/order/returnInfo")
    List<Map<String, Object>> getReturnInfo(@RequestParam final Map<String, Object> map)
            throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = svc.selectReturnInfo(map);
        _log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     * 取消退货
     * 
     * @param mo
     * @return
     */
    @PutMapping("/ord/return/cancel")
    Ro cancelReturn(@RequestBody final OrdReturnMo mo) {
        mo.setCancelTime(new Date());
        _log.info("取消退货的参数为：{}", mo);
        try {
            return svc.cancelReturn(mo);
        } catch (final Exception e) {
            _log.error("取消退货出错", e);
            final String msg = e.getMessage();
            final Ro ro = new Ro();
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }
    }
}
