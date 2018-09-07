package rebue.ord.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.mo.OrdSublevelBuyMo;
import rebue.ord.svc.OrdSublevelBuySvc;
import com.github.pagehelper.PageInfo;

@RestController
public class OrdSublevelBuyCtrl {
    /**
     * @mbg.generated
     */
    private final static Logger _log = LoggerFactory.getLogger(OrdSublevelBuyCtrl.class);

    /**
     * @mbg.generated
     */
	@Resource
    private OrdSublevelBuySvc svc;

    /**
     * 添加下级购买信息
     * @mbg.generated
     */
    @PostMapping("/ord/sublevelbuy")
    Map<String, Object> add(OrdSublevelBuyMo vo) throws Exception {
        _log.info("add OrdSublevelBuyMo:" + vo);
        svc.add(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", vo.getId());
        _log.info("add OrdSublevelBuyMo success!");
        return result;
    }

    /**
     * 修改下级购买信息
     * @mbg.generated
     */
    @PutMapping("/ord/sublevelbuy")
    Map<String, Object> modify(OrdSublevelBuyMo vo) throws Exception {
        _log.info("modify OrdSublevelBuyMo:" + vo);
        svc.modify(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("modify OrdSublevelBuyMo success!");
        return result;
    }

    /**
     * 删除下级购买信息
     * @mbg.generated
     */
    @DeleteMapping("/ord/sublevelbuy/{id}")
    Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
        _log.info("save OrdSublevelBuyMo:" + id);
        svc.del(id);		
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("delete OrdSublevelBuyMo success!");
        return result;
    }

    /**
     * 查询下级购买信息
     * @mbg.generated
     */
    @GetMapping("/ord/sublevelbuy")
    PageInfo<OrdSublevelBuyMo> list(OrdSublevelBuyMo qo, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
    		_log.info("list OrdSublevelBuyMo:" + qo+", pageNum = " + pageNum + ", pageSize = " + pageSize);

        if (pageSize > 50) {
            String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        PageInfo<OrdSublevelBuyMo> result = svc.list(qo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 获取单个下级购买信息
     * @mbg.generated
     */
    @GetMapping("/ord/sublevelbuy/{id}")
    OrdSublevelBuyMo get(@PathVariable("id") java.lang.Long id) {
        _log.info("get OrdSublevelBuyMo by id: " + id);
        OrdSublevelBuyMo result = svc.getById(id);
        _log.info("get: " + result);
        return result;
    }

}
