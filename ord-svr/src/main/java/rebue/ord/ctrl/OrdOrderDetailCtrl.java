package rebue.ord.ctrl;

import com.github.pagehelper.PageInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 订单详情
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdOrderDetailCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdOrderDetailCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdOrderDetailSvc svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String _uniqueFilesName = "某字段内容";

    /**
     * 添加订单详情
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PostMapping("/ord/orderdetail")
    Ro add(@RequestBody OrdOrderDetailMo mo) throws Exception {
        _log.info("add OrdOrderDetailMo: {}", mo);
        Ro ro = new Ro();
        try {
            int result = svc.add(mo);
            if (result == 1) {
                String msg = "添加成功";
                _log.info("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.SUCCESS);
                return ro;
            } else {
                String msg = "添加失败";
                _log.error("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.FAIL);
                return ro;
            }
        } catch (DuplicateKeyException e) {
            String msg = "添加失败，" + _uniqueFilesName + "已存在，不允许出现重复";
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
     * 修改订单详情
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PutMapping("/ord/orderdetail")
    Ro modify(@RequestBody OrdOrderDetailMo mo) throws Exception {
        _log.info("modify OrdOrderDetailMo: {}", mo);
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
     * 删除订单详情
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/orderdetail")
    Ro del(@RequestParam("id") java.lang.Long id) {
        _log.info("del OrdOrderDetailMo by id: {}", id);
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
     * 查询订单详情
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/orderdetail")
    PageInfo<OrdOrderDetailMo> list(OrdOrderDetailMo mo, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null)
            pageNum = 1;
        if (pageSize == null)
            pageSize = 5;
        _log.info("list OrdOrderDetailMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
        if (pageSize > 50) {
            String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        PageInfo<OrdOrderDetailMo> result = svc.list(mo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 获取单个订单详情
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/orderdetail/getbyid")
    OrdOrderDetailMo getById(@RequestParam("id") java.lang.Long id) {
        _log.info("get OrdOrderDetailMo by id: " + id);
        return svc.getById(id);
    }

    /**
     *  根据订单id获取详情 Title: orderDetailInfo Description:
     *
     *  @param qo
     *  @return
     *  @date 2018年4月9日 下午5:02:41
     */
    @GetMapping("/ord/orderdetail/info")
    List<DetailandBuyRelationRo> orderDetailInfo(@RequestParam("orderId") java.lang.Long orderId) {
        _log.info("获取订单详情的参数为：{}", orderId);
        List<DetailandBuyRelationRo> list = svc.listByOrderId(orderId);
        _log.info("获取到的订单详情为：{}", String.valueOf(list));
        return list;
    }
}
