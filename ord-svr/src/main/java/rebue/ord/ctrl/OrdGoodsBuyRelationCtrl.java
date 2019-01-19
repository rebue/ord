package rebue.ord.ctrl;

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

import com.github.pagehelper.PageInfo;

import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.ord.svc.OrdGoodsBuyRelationSvc;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 用户商品购买关系
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdGoodsBuyRelationCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger    _log             = LoggerFactory.getLogger(OrdGoodsBuyRelationCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdGoodsBuyRelationSvc svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final String           _uniqueFilesName = "某字段内容";

    /**
     * 添加用户商品购买关系
     * 
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PostMapping("/ord/goodsbuyrelation")
    Ro add(@RequestBody final OrdGoodsBuyRelationMo mo) throws Exception {
        _log.info("add OrdGoodsBuyRelationMo: {}", mo);
        final Ro ro = new Ro();
        try {
            final int result = svc.add(mo);
            if (result == 1) {
                final String msg = "添加成功";
                _log.info("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.SUCCESS);
                return ro;
            } else {
                final String msg = "添加失败";
                _log.error("{}: mo-{}", msg, mo);
                ro.setMsg(msg);
                ro.setResult(ResultDic.FAIL);
                return ro;
            }
        } catch (final DuplicateKeyException e) {
            final String msg = "添加失败，" + _uniqueFilesName + "已存在，不允许出现重复";
            _log.error(msg + ": mo-" + mo, e);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        } catch (final RuntimeException e) {
            final String msg = "添加失败，出现运行时异常";
            _log.error(msg + ": mo-" + mo, e);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        }
    }

    /**
     * 修改用户商品购买关系
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PutMapping("/ord/goodsbuyrelation")
    Ro modify(@RequestBody final OrdGoodsBuyRelationMo mo) throws Exception {
        _log.info("modify OrdGoodsBuyRelationMo: {}", mo);
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
            _log.error(msg + ": mo-" + mo, e);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        } catch (final RuntimeException e) {
            final String msg = "修改失败，出现运行时异常";
            _log.error(msg + ": mo-" + mo, e);
            ro.setMsg(msg);
            ro.setResult(ResultDic.FAIL);
            return ro;
        }
    }

    /**
     * 删除用户商品购买关系
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/goodsbuyrelation")
    Ro del(@RequestParam("id") final java.lang.Long id) {
        _log.info("del OrdGoodsBuyRelationMo by id: {}", id);
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
     * 查询用户商品购买关系
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/goodsbuyrelation")
    PageInfo<OrdGoodsBuyRelationMo> list(final OrdGoodsBuyRelationMo mo, @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 5;
        }
        _log.info("list OrdGoodsBuyRelationMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
        if (pageSize > 50) {
            final String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        final PageInfo<OrdGoodsBuyRelationMo> result = svc.list(mo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 获取单个用户商品购买关系
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/goodsbuyrelation/getbyid")
    OrdGoodsBuyRelationMo getById(@RequestParam("id") final java.lang.Long id) {
        _log.info("get OrdGoodsBuyRelationMo by id: " + id);
        return svc.getById(id);
    }

    /**
     * 导出redis中的购买关系到数据库中
     */
    @GetMapping("/ord/goodsbuyrelation/export")
    void exportGoodsBuyRelation() {
        _log.info("开始获取商品购买关系，时间：{}", new Date());
        svc.exportGoodsBuyRelation();
    }
    
    /**
		查询某个商品购买关系是否存在
     */
    @GetMapping("/ord/goodsbuyrelation/listExistRelation")
    List<OrdGoodsBuyRelationMo> ListExistRelation(final OrdGoodsBuyRelationMo mo){
        _log.info("查询某个商品购买关系是否存在 参数为：{}", mo);
        List<OrdGoodsBuyRelationMo> result=svc.list(mo);
        _log.info("查询某个商品购买关系是否存在 结果为：{}", result);
        return result;
    }
}
