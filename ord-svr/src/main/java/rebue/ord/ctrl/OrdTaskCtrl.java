package rebue.ord.ctrl;

import java.util.HashMap;
import java.util.List;
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
import rebue.ord.mo.OrdTaskMo;
import rebue.ord.svc.OrdTaskSvc;

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
     * @mbg.generated
     */
    @Resource
    private OrdTaskSvc svc;

    /**
     * 添加订单任务
     * @mbg.generated
     */
    @PostMapping("/ord/task")
    Map<String, Object> add(OrdTaskMo vo) throws Exception {
        _log.info("add OrdTaskMo:" + vo);
        svc.add(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", vo.getId());
        _log.info("add OrdTaskMo success!");
        return result;
    }

    /**
     * 修改订单任务
     * @mbg.generated
     */
    @PutMapping("/ord/task")
    Map<String, Object> modify(OrdTaskMo vo) throws Exception {
        _log.info("modify OrdTaskMo:" + vo);
        svc.modify(vo);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("modify OrdTaskMo success!");
        return result;
    }

    /**
     * 删除订单任务
     * @mbg.generated
     */
    @DeleteMapping("/ord/task/{id}")
    Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
        _log.info("save OrdTaskMo:" + id);
        svc.del(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        _log.info("delete OrdTaskMo success!");
        return result;
    }

    /**
     * 获取单个订单任务
     * @mbg.generated
     */
    @GetMapping("/ord/task/{id}")
    OrdTaskMo get(@PathVariable("id") java.lang.Long id) {
        _log.info("get OrdTaskMo by id: " + id);
        OrdTaskMo result = svc.getById(id);
        _log.info("get: " + result);
        return result;
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
