package rebue.ord.ctrl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rebue.ord.mo.OrdAddrMo;
import rebue.ord.svc.OrdAddrSvc;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 用户收货地址
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdAddrCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdAddrCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdAddrSvc svc;


    /**
     * 删除用户收货地址
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/addr")
    Ro del(@RequestParam("id") java.lang.Long id) {
        _log.info("del OrdAddrMo by id: {}", id);
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
     * 获取单个用户收货地址
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/addr/getbyid")
    OrdAddrMo getById(@RequestParam("id") java.lang.Long id) {
        _log.info("get OrdAddrMo by id: " + id);
        return svc.getById(id);
    }

    /**
     *  添加用户收货地址
     */
    @PostMapping("/ord/addr")
    Map<String, Object> add(OrdAddrMo vo) throws Exception {
        _log.info("添加用户收货地址的参数为：{}", vo.toString());
        int result = svc.add(vo);
        _log.info("{}添加用户收货地址的返回值为：{}", vo.getUserId(), result);
        Map<String, Object> resultMap = new HashMap<>();
        if (result < 1) {
            _log.error("{}添加用户收货地址失败！", vo.getUserId());
            resultMap.put("result", result);
            resultMap.put("msg", "添加失败");
        } else {
            _log.info("{}添加用户收货地址成功！", vo.getUserId());
            resultMap.put("result", 1);
            resultMap.put("msg", "添加成功");
        }
        return resultMap;
    }

    /**
     *  修改用户默认收货地址
     */
    @SuppressWarnings("finally")
    @PutMapping("/ord/addr/def")
    Map<String, Object> modifyDef(OrdAddrMo vo) throws Exception {
        _log.info("修改用户默认收货地址的参数为：{}", vo.toString());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = svc.updateDef(vo);
            _log.info("修改用户默认收货地址的返回值为：{}", String.valueOf(resultMap));
        } catch (RuntimeException e) {
            resultMap.put("result", -1);
            resultMap.put("msg", "设置失败");
        } finally {
            return resultMap;
        }
    }

    /**
     *  修改用户收货地址 Title: modify Description:
     *
     *  @param vo
     *  @return
     *  @date 2018年4月8日 下午4:25:03
     */
    @PutMapping("/ord/addr")
    Map<String, Object> modify(OrdAddrMo vo) {
        _log.info("修改用户收货地址信息的参数为：{}", vo.toString());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = svc.update(vo);
            _log.info("修改用户收货地址信息的返回值为：{}", String.valueOf(resultMap));
            return resultMap;
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals("修改原默认收货地址失败")) {
                resultMap.put("result", -1);
                resultMap.put("msg", msg);
            } else {
                resultMap.put("result", -2);
                resultMap.put("msg", "修改失败");
            }
            return resultMap;
        }
    }

    /**
     *  查询用户收货地址
     */
    @GetMapping("/ord/addr")
    List<OrdAddrMo> list(OrdAddrMo qo) {
        _log.info("list OrdAddrMo:" + qo);
        List<OrdAddrMo> result = svc.list(qo);
        _log.info("result: " + result);
        return result;
    }
}
