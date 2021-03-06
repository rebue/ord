package rebue.ord.ctrl;

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
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svc.OrdTaskSvc;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 创建时间：2018年5月21日 下午3:22:06 项目名称：ord-svr
 *
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：OrdTaskCtrl.java 类说明： 订单任务
 */
@RestController
public class OrdTaskCtrl {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdTaskSvc svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String _uniqueFilesName = "某字段内容";

    /**
     * 添加订单任务
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PostMapping("/ord/task")
    Ro add(@RequestBody OrdTaskMo mo) throws Exception {
        _log.info("add OrdTaskMo: {}", mo);
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
     * 修改订单任务
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PutMapping("/ord/task")
    Ro modify(@RequestBody OrdTaskMo mo) throws Exception {
        _log.info("modify OrdTaskMo: {}", mo);
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
     * 删除订单任务
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @DeleteMapping("/ord/task")
    Ro del(@RequestParam("id") java.lang.Long id) {
        _log.info("del OrdTaskMo by id: {}", id);
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
     * 获取单个订单任务
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @GetMapping("/ord/task/getbyid")
    OrdTaskMo getById(@RequestParam("id") java.lang.Long id) {
        _log.info("get OrdTaskMo by id: " + id);
        return svc.getById(id);
    }

    private static final Logger _log = LoggerFactory.getLogger(OrdTaskCtrl.class);

    @Resource
    private OrdTaskSvc ordTaskSvc;

    /**
     *  查询订单任务数量
     *  Title: getByExecutePlanTimeBeforeNow
     *  Description:
     *  @param executeState
     *  @param taskType
     *  @return
     *  @date 2018年5月28日 上午11:00:40
     */
    @GetMapping(value = "/ord/task")
    List<Long> getByExecutePlanTimeBeforeNow(@RequestParam("executeState") byte executeState, @RequestParam("taskType") byte taskType) {
        _log.info("查询订单任务数量的参数为：{}， {}", executeState, taskType);
        return ordTaskSvc.getByExecutePlanTimeBeforeNow(executeState, taskType);
    }

    /**
     *  执行订单签收任务 Title: executeSignInOrderTask Description:
     *
     *  @param executeFactTime
     *  @param id
     *  @param doneState
     *  @param noneState
     *  @return
     *  @date 2018年5月21日 下午3:30:46
     */
    @PostMapping("/ord/task/signin")
    void executeSignInOrderTask(@RequestParam("id") long id) {
        ordTaskSvc.executeSignInOrderTask(id);
    }

    /**
     *  执行取消订单任务 Title: executeSignInOrderTask Description:
     *
     *  @param executeFactTime
     *  @param id
     *  @param doneState
     *  @param noneState
     *  @return
     *  @date 2018年5月21日 下午3:30:46
     */
    @PostMapping("/ord/task/cancleOrder")
    void executeCancelOrderTask(@RequestParam("id") long id) {
        ordTaskSvc.executeCancelOrderTask(id);
    }
}
