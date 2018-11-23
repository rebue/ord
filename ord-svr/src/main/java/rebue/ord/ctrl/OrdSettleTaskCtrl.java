package rebue.ord.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.svc.OrdSettleTaskSvc;
import rebue.ord.to.CancelSettleTaskTo;
import rebue.ord.to.ResumeSettleTaskTo;
import rebue.ord.to.SuspendSettleTaskTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 结算任务
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdSettleTaskCtrl {
    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final static Logger _log             = LoggerFactory.getLogger(OrdSettleTaskCtrl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Resource
    private OrdSettleTaskSvc    svc;

    /**
     * 有唯一约束的字段名称
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final String        _uniqueFilesName = "某字段内容";

    /**
     * 添加结算任务
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @PostMapping("/ord/settletask")
    Ro add(@RequestBody final OrdSettleTaskMo mo) throws Exception {
        _log.info("add OrdSettleTaskMo: {}", mo);
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
     * 暂停结算任务
     * 
     * @param to
     * @return
     */
    @PutMapping("/ord/settletask/suspend")
    Ro suspendSettleTask(@RequestBody final SuspendSettleTaskTo to) {
        _log.info("暂停结算任务的请求参数为：{}", to);
        return svc.suspendSettleTask(to);
    }

    /**
     * 恢复结算任务
     * 
     * @param to
     * @return
     */
    @PutMapping("/ord/settletask/resume")
    Ro resumeSettleTask(@RequestBody final ResumeSettleTaskTo to) {
        _log.info("恢复结算任务的请求参数为：{}", to);
        return svc.resumeSettleTask(to);
    }

    /**
     * 取消结算任务
     * 
     * @param to
     * @return
     */
    @PutMapping("/ord/settletask/cancel")
    Ro cancelSettleTask(@RequestBody final CancelSettleTaskTo to) {
        _log.info("取消结算任务的请求参数为：{}", to);
        return svc.cancelSettleTask(to);
    }

    /**
     * 获取结算任务
     * 
     * @param to
     * @return
     */
    @GetMapping("/ord/settletasks")
    List<Long> getTaskIdsThatShouldExecute() {
        return svc.getTaskIdsThatShouldExecute();
    }

    /**
     * 执行结算任务
     * 
     * @param id
     * @return
     */
    @PostMapping("/ord/settletask/execute")
    Ro executeSettleTask(@RequestParam("id") final Long id) {
        _log.info("执行结算任务的参数为：{}", id);
        return svc.executeSettleTask(id);
    }
}
