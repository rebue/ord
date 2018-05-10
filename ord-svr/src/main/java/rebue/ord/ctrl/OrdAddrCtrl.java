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
import org.springframework.web.bind.annotation.RestController;

import rebue.ord.mo.OrdAddrMo;
import rebue.ord.svc.OrdAddrSvc;
import java.util.List;

@RestController
public class OrdAddrCtrl {
	/**
	 * @mbg.generated
	 */
	private final static Logger _log = LoggerFactory
			.getLogger(OrdAddrCtrl.class);

	/**
	 * @mbg.generated
	 */
	@Resource
	private OrdAddrSvc svc;

	/**
	 * 删除用户收货地址
	 * 
	 * @mbg.generated
	 */
	@DeleteMapping("/ord/addr/{id}")
	Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
		_log.info("save OrdAddrMo:" + id);
		svc.del(id);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		_log.info("delete OrdAddrMo success!");
		return result;
	}

	/**
	 * 获取单个用户收货地址
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/addr/{id}")
	OrdAddrMo get(@PathVariable("id") java.lang.Long id) {
		_log.info("get OrdAddrMo by id: " + id);
		OrdAddrMo result = svc.getById(id);
		_log.info("get: " + result);
		return result;
	}

	/**
	 * 添加用户收货地址
	 */
	@PostMapping("/ord/addr")
	Map<String, Object> add(@RequestBody OrdAddrMo vo) throws Exception {
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
	 * 修改用户默认收货地址
	 */
	@PutMapping("/ord/addr/def")
	Map<String, Object> modifyDef(@RequestBody OrdAddrMo vo) throws Exception {
		_log.info("修改用户默认收货地址的参数为：{}", vo.toString());
		return svc.updateDef(vo);
	}

	/**
	 * 修改用户收货地址 Title: modify Description:
	 * 
	 * @param vo
	 * @return
	 * @date 2018年4月8日 下午4:25:03
	 */
	@PutMapping("/ord/addr")
	Map<String, Object> modify(@RequestBody OrdAddrMo vo) {
		_log.info("修改用户收货地址信息的参数为：{}", vo.toString());
		return svc.update(vo);
	}

	/**
	 * 查询用户收货地址
	 */
	@GetMapping("/ord/addr")
	List<OrdAddrMo> list(@RequestBody OrdAddrMo qo) {
		_log.info("list OrdAddrMo:" + qo);
		List<OrdAddrMo> result = svc.list(qo);
		_log.info("result: " + result);
		return result;
	}

}
