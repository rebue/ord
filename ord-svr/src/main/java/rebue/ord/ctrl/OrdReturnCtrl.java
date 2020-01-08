package rebue.ord.ctrl;

import com.github.pagehelper.PageInfo;
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
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.OrdReturnRo2;
import rebue.ord.ro.ReturnPageListRo;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.AddReturnTo;
import rebue.ord.to.AgreeReturnTo;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.ord.to.ReceivedAndRefundedTo;
import rebue.ord.to.RejectReturnTo;
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
    private static final Logger _log = LoggerFactory.getLogger(OrdReturnCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdReturnSvc svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String _uniqueFilesName = "某字段内容";

    /**
     * 修改用户退货信息
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PutMapping("/ord/return")
    Ro modify(@RequestBody OrdReturnMo mo) throws Exception {
        _log.info("modify OrdReturnMo: {}", mo);
        Ro ro = new Ro();
        try {
            if (svc.modify(mo) == 1) {
                String msg = "修改成功";
                _log.info("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.SUCCESS);
                return ro;
            } else {
                String msg = "修改失败";
                _log.error("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.FAIL);
                return ro;
            }
        } catch (DuplicateKeyException e) {
            String msg = "修改失败，" + _uniqueFilesName + "已存在，不允许出现重复";
            _log.error("{}: mo-{}", msg, mo);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        } catch (RuntimeException e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String msg = "修改失败，出现运行时异常(" + sdf.format(new Date()) + ")";
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
    Ro del(@RequestParam("id") java.lang.Long id) {
        _log.info("del OrdReturnMo by id: {}", id);
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
     * 是否测试模式（测试模式下不用从Cookie中获取用户ID）
     */
    @Value("${debug:false}")
    private Boolean isDebug;

    /**
     * 前面经过的代理
     */
    @Value("${afc.passProxy:noproxy}")
    private String passProxy;

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
     * 添加用户退货信息
     *
     * @mbg.overrideByMethodName
     */
    @PostMapping("/ord/return")
    Ro add(@RequestBody final AddReturnTo to) throws Exception {
        final Ro ro = new Ro();
        try {
            return svc.addReturn(to);
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            _log.info("添加退货信息出错：{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        } catch (final Exception e) {
            _log.info("添加退货信息出错：{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("出现非运行时异常，添加出错");
            return ro;
        }
    }

    /**
     * 查询退货信息
     *
     * @mbg.overrideByMethodName
     */
    @GetMapping("/ord/return")
    PageInfo<ReturnPageListRo> list(final ReturnPageListRo mo, @RequestParam("pageNum") final int pageNum,
            @RequestParam("pageSize") final int pageSize) {
        _log.info("list OrdReturnMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
        if (pageSize > 50) {
            final String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        final PageInfo<ReturnPageListRo> result = svc.selectReturnPageList(mo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 拒绝退货
     */
    @PutMapping("/ord/return/reject")
    Ro rejectReturn(@RequestBody final RejectReturnTo to, final HttpServletRequest req)
            throws NumberFormatException, ParseException {
        _log.info("拒绝退货的参数为：{}", to);
        final Ro ro = new Ro();
        Long loginId = 520469568947224576L;
        if (!isDebug) {
            loginId = JwtUtils.getJwtUserIdInCookie(req);
        }
        to.setRejectOpId(loginId);
        try {
            return svc.rejectReturn(to);
        } catch (final RuntimeException e) {
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(e.getMessage());
            return ro;
        }
    }

    /**
     * 同意退款
     */
    @PostMapping("/ord/return/agreetoarefund")
    Ro agreeRefund(@RequestBody final OrdOrderReturnTo to, final HttpServletRequest req)
            throws NumberFormatException, ParseException {
        Ro ro = new Ro();
        if (!isDebug) {
            to.setOpId(JwtUtils.getJwtUserIdInCookie(req));
            to.setIp(AgentUtils.getIpAddr(req, passProxy));
        } else {
            to.setOpId(520469568947224576L);
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
     * 同意退货
     */
    @PostMapping("/ord/return/agreereturn")
    Ro agreeReturn(@RequestBody final AgreeReturnTo to, final HttpServletRequest req)
            throws NumberFormatException, ParseException {
        final Ro ro = new Ro();
        Long loginId = 520469568947224576L;
        if (!isDebug) {
            loginId = JwtUtils.getJwtUserIdInCookie(req);
        }
        to.setReviewOpId(loginId);
        try {
            _log.info("同意退货的请求参数为：{}", to.toString());
            return svc.agreeReturn(to);
        } catch (final RuntimeException e) {
            _log.error("同意退货出现运行时异常，{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(e.getMessage());
            return ro;
        } catch (final Exception e) {
            _log.error("同意退货出现非运行时异常，{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("出现非运行时异常，操作失败");
            return ro;
        }
    }

    /**
     * 已收到货并退款
     */
    @PostMapping("/ord/return/receivedandrefunded")
    Ro receivedAndRefunded(@RequestBody final ReceivedAndRefundedTo to, final HttpServletRequest req)
            throws NumberFormatException, ParseException {
        _log.info("已收到货并退款的请求参数为：{}", to);
        final Ro ro = new Ro();
        Long loginId = 520469568947224576L;
        if (!isDebug) {
            loginId = JwtUtils.getJwtUserIdInCookie(req);
        }
        to.setOpId(loginId);
        try {
            return svc.receivedAndRefunded(to);
        } catch (final RuntimeException e) {
            _log.error("已收到货并退款出现运行时异常，{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(e.getMessage());
            return ro;
        } catch (final Exception e) {
            _log.error("已收到货并退款出现非运行时异常，{}", e);
            ro.setResult(ResultDic.FAIL);
            ro.setMsg("出现非运行时异常，操作失败");
            return ro;
        }
    }

    /**
     * 查询用户退货中订单信息
     */
    @GetMapping("/ord/order/returningInfo")
    List<Map<String, Object>> getReturningInfo(@RequestParam final Map<String, Object> map) throws ParseException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询用户退货中订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = svc.selectReturningInfo(map);
        _log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     * 查询用户退货完成订单
     */
    @GetMapping("/ord/order/returnInfo")
    List<Map<String, Object>> getReturnInfo(@RequestParam final Map<String, Object> map) throws ParseException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        _log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
        final List<Map<String, Object>> list = svc.selectReturnInfo(map);
        _log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
        return list;
    }

    /**
     * 取消退货
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

    /**
     * 收银机退款(因为是当场处理的，页面为退货，实际是退款)
     */
    @PostMapping("/ord/return/posAgreetoarefund")
    Ro posAgreetoarefund(@RequestBody final OrdOrderReturnTo to) throws NumberFormatException, ParseException {
        Ro ro = new Ro();
        try {
            _log.info("收银机同意退款的请求参数为：{}", to);
            ro = svc.posAgreetoarefund(to);
            _log.info("收银机同意退款的返回值为：{}", ro);
            return ro;
        } catch (final RuntimeException e) {
            final String msg = e.getMessage();
            ro.setResult(ResultDic.FAIL);
            ro.setMsg(msg);
            return ro;
        }
    }

}
