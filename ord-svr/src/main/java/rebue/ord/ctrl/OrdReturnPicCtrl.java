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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.mo.OrdReturnPicMo;
import rebue.ord.svc.OrdReturnPicSvc;
import com.github.pagehelper.PageInfo;

@RestController
public class OrdReturnPicCtrl {
    /**
     * @mbg.generated
     */
    private final static Logger _log = LoggerFactory.getLogger(OrdReturnPicCtrl.class);

    /**
     * @mbg.generated
     */
	@Resource
    private OrdReturnPicSvc svc;

    /**
     * 添加退货图片
     * @mbg.generated
     */
    @PostMapping("/ord/returnpic")
    Map<String, Object> add(@RequestBody OrdReturnPicMo vo) throws Exception {
        _log.info("add OrdReturnPicMo:" + vo);
        svc.add(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", vo.getId());
        _log.info("add OrdReturnPicMo success!");
        return result;
    }

    /**
     * 修改退货图片
     * @mbg.generated
     */
    @PutMapping("/ord/returnpic")
    Map<String, Object> modify(@RequestBody OrdReturnPicMo vo) throws Exception {
        _log.info("modify OrdReturnPicMo:" + vo);
        svc.modify(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("modify OrdReturnPicMo success!");
        return result;
    }

    /**
     * 删除退货图片
     * @mbg.generated
     */
    @DeleteMapping("/ord/returnpic/{id}")
    Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
        _log.info("save OrdReturnPicMo:" + id);
        svc.del(id);		
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("delete OrdReturnPicMo success!");
        return result;
    }

    /**
     * 查询退货图片
     * @mbg.generated
     */
    @GetMapping("/ord/returnpic")
    PageInfo<OrdReturnPicMo> list(@RequestBody OrdReturnPicMo qo, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
    		_log.info("list OrdReturnPicMo:" + qo+", pageNum = " + pageNum + ", pageSize = " + pageSize);

        if (pageSize > 50) {
            String msg = "pageSize不能大于50";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        PageInfo<OrdReturnPicMo> result = svc.list(qo, pageNum, pageSize);
        _log.info("result: " + result);
        return result;
    }

    /**
     * 获取单个退货图片
     * @mbg.generated
     */
    @GetMapping("/ord/returnpic/{id}")
    OrdReturnPicMo get(@PathVariable("id") java.lang.Long id) {
        _log.info("get OrdReturnPicMo by id: " + id);
        OrdReturnPicMo result = svc.getById(id);
        _log.info("get: " + result);
        return result;
    }

}
