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

import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.svc.OrdOrderDetailSvc;
import com.github.pagehelper.PageInfo;
import java.util.List;

@RestController
public class OrdOrderDetailCtrl {
	/**
	 * @mbg.generated
	 */
	private final static Logger _log = LoggerFactory
			.getLogger(OrdOrderDetailCtrl.class);

	/**
	 * @mbg.generated
	 */
	@Resource
	private OrdOrderDetailSvc svc;

	/**
	 * 添加订单详情
	 * 
	 * @mbg.generated
	 */
	@PostMapping("/ord/orderdetail")
	Map<String, Object> add(OrdOrderDetailMo vo) throws Exception {
		_log.info("add OrdOrderDetailMo:" + vo);
		svc.add(vo);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("id", vo.getId());
		_log.info("add OrdOrderDetailMo success!");
		return result;
	}

	/**
	 * 修改订单详情
	 * 
	 * @mbg.generated
	 */
	@PutMapping("/ord/orderdetail")
	Map<String, Object> modify(OrdOrderDetailMo vo) throws Exception {
		_log.info("modify OrdOrderDetailMo:" + vo);
		svc.modify(vo);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		_log.info("modify OrdOrderDetailMo success!");
		return result;
	}

	/**
	 * 删除订单详情
	 * 
	 * @mbg.generated
	 */
	@DeleteMapping("/ord/orderdetail/{id}")
	Map<String, Object> del(@PathVariable("id") java.lang.Long id) {
		_log.info("save OrdOrderDetailMo:" + id);
		svc.del(id);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		_log.info("delete OrdOrderDetailMo success!");
		return result;
	}

	/**
	 * 查询订单详情
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/orderdetail")
	PageInfo<OrdOrderDetailMo> list(OrdOrderDetailMo qo,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize) {
		_log.info("list OrdOrderDetailMo:" + qo + ", pageNum = " + pageNum
				+ ", pageSize = " + pageSize);

		if (pageSize > 50) {
			String msg = "pageSize不能大于50";
			_log.error(msg);
			throw new IllegalArgumentException(msg);
		}

		PageInfo<OrdOrderDetailMo> result = svc.list(qo, pageNum, pageSize);
		_log.info("result: " + result);
		return result;
	}

	/**
	 * 获取单个订单详情
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/orderdetail/{id}")
	OrdOrderDetailMo get(@PathVariable("id") java.lang.Long id) {
		_log.info("get OrdOrderDetailMo by id: " + id);
		OrdOrderDetailMo result = svc.getById(id);
		_log.info("get: " + result);
		return result;
	}

	/**
	 * 查询订单详情信息 Title: orderDetailInfo Description:
	 * 
	 * @param qo
	 * @return
	 * @date 2018年4月9日 下午5:02:41
	 */
	@GetMapping("/ord/orderdetail/info")
	List<OrdOrderDetailMo> orderDetailInfo(OrdOrderDetailMo qo) {
		_log.info("获取订单详情的参数为：{}", qo.toString());
		List<OrdOrderDetailMo> list = svc.list(qo);
		_log.info("获取到的订单详情为：{}", String.valueOf(list));
		return list;
	}

}
