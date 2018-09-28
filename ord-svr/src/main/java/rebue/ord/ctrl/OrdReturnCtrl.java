package rebue.ord.ctrl;

import com.github.pagehelper.PageInfo;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import rebue.ord.dic.AddReturnDic;
import rebue.ord.dic.AgreeToARefundDic;
import rebue.ord.dic.AgreeToReturnDic;
import rebue.ord.dic.ReceivedAndRefundedDic;
import rebue.ord.dic.RejectReturnDic;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToARefundRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import rebue.ord.svc.OrdReturnSvc;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.Ro;

/**
 * 用户退货信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@RestController
public class OrdReturnCtrl {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private static final Logger _log = LoggerFactory.getLogger(OrdReturnCtrl.class);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Resource
	private OrdReturnSvc svc;

	/**
	 * 有唯一约束的字段名称
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private String _uniqueFilesName = "某字段内容";

	// /**
	// * 添加用户退货信息
	// *
	// * @mbg.generated 自动生成，如需修改，请删除本行
	// */
	// @PostMapping("/ord/return")
	// Ro add(@RequestBody OrdReturnMo mo) throws Exception {
	// _log.info("add OrdReturnMo: {}", mo);
	// Ro ro = new Ro();
	// try {
	// int result = svc.add(mo);
	// if (result == 1) {
	// String msg = "添加成功";
	// _log.info("{}: mo-{}", msg, mo);
	// ro.setMsg(msg);
	// ro.setResult(ResultDic.SUCCESS);
	// return ro;
	// } else {
	// String msg = "添加失败";
	// _log.error("{}: mo-{}", msg, mo);
	// ro.setMsg(msg);
	// ro.setResult(ResultDic.FAIL);
	// return ro;
	// }
	// } catch (DuplicateKeyException e) {
	// String msg = "添加失败，" + _uniqueFilesName + "已存在，不允许出现重复";
	// _log.error("{}: mo-{}", msg, mo);
	// ro.setMsg(msg);
	// ro.setResult(ResultDic.FAIL);
	// return ro;
	// } catch (RuntimeException e) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// String msg = "修改失败，出现运行时异常(" + sdf.format(new Date()) + ")";
	// _log.error("{}: mo-{}", msg, mo);
	// ro.setMsg(msg);
	// ro.setResult(ResultDic.FAIL);
	// return ro;
	// }
	// }

	/**
	 * 修改用户退货信息
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@PutMapping("/ord/return")
	Ro modify(@RequestBody OrdReturnMo mo) throws Exception {
		_log.info("modify OrdReturnMo: {}", mo);
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
	 * 删除用户退货信息
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@DeleteMapping("/ord/return")
	Ro del(@RequestParam("id") java.lang.Long id) {
		_log.info("del OrdReturnMo by id: {}", id);
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

	// /**
	// * 查询用户退货信息
	// *
	// * @mbg.generated 自动生成，如需修改，请删除本行
	// */
	// @GetMapping("/ord/return")
	// PageInfo<OrdReturnMo> list(OrdReturnMo mo, @RequestParam(value = "pageNum",
	// required = false) Integer pageNum, @RequestParam(value = "pageSize", required
	// = false) Integer pageSize) {
	// if (pageNum == null)
	// pageNum = 1;
	// if (pageSize == null)
	// pageSize = 5;
	// _log.info("list OrdReturnMo:" + mo + ", pageNum = " + pageNum + ", pageSize =
	// " + pageSize);
	// if (pageSize > 50) {
	// String msg = "pageSize不能大于50";
	// _log.error(msg);
	// throw new IllegalArgumentException(msg);
	// }
	// PageInfo<OrdReturnMo> result = svc.list(mo, pageNum, pageSize);
	// _log.info("result: " + result);
	// return result;
	// }

	/**
	 * 获取单个用户退货信息
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@GetMapping("/ord/return/getbyid")
	OrdReturnMo getById(@RequestParam("id") java.lang.Long id) {
		_log.info("get OrdReturnMo by id: " + id);
		return svc.getById(id);
	}

	/**
	 * 添加用户退货信息 Title: add Description:
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 * @date 2018年4月19日 下午2:53:39
	 */
	@SuppressWarnings("finally")
	@PostMapping("/ord/return")
	AddReturnRo addReturn(OrdOrderReturnTo vo) throws Exception {
		AddReturnRo addReturnRo = new AddReturnRo();
		try {
			addReturnRo = svc.addReturn(vo);
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			_log.info("添加退货信息出错：", e);
			if (msg.equals("添加退货图片失败")) {
				addReturnRo.setResult(AddReturnDic.ADD_RETURN_PIC);
				addReturnRo.setMsg(msg);
			} else if (msg.equals("修改订单详情状态失败")) {
				addReturnRo.setResult(AddReturnDic.MODIFY_ORDER_DETAIL_STATE_ERROR);
				addReturnRo.setMsg(msg);
			} else {
				e.printStackTrace();
				addReturnRo.setResult(AddReturnDic.ERROR);
				addReturnRo.setMsg("提交失败");
			}
		} catch (Exception e) {
			_log.info("添加退货信息出错：", e);
		} finally {
			return addReturnRo;
		}
	}

	/**
	 * 查询退货信息 Title: selectReturnPageList Description:
	 *
	 * @param mo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @date 2018年4月21日 下午3:59:07
	 */
	@GetMapping("/ord/return")
	PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnMo mo, @RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize) {
		_log.info("list OrdReturnMo:" + mo + ", pageNum = " + pageNum + ", pageSize = " + pageSize);
		if (pageSize > 50) {
			String msg = "pageSize不能大于50";
			_log.error(msg);
			throw new IllegalArgumentException(msg);
		}
		PageInfo<OrdReturnRo> result = svc.selectReturnPageList(mo, pageNum, pageSize);
		_log.info("result: " + result);
		return result;
	}

	/**
	 * 拒绝退货 Title: rejectReturn Description:
	 *
	 * @param mo
	 * @return
	 * @date 2018年4月27日 下午3:31:38
	 */
	@PutMapping("/ord/return/reject")
	RejectReturnRo rejectReturn(OrdReturnMo mo) {
		_log.info("拒绝退货的参数为：{}", mo.toString());
		RejectReturnRo rejectReturnRo = new RejectReturnRo();
		try {
			return svc.rejectReturn(mo);
		} catch (RuntimeException e) {
			rejectReturnRo.setResult(RejectReturnDic.ERROR);
			rejectReturnRo.setMsg("操作失败");
			return rejectReturnRo;
		}
	}

	/**
	 * 同意退款 Title: agreeToARefund Description:
	 *
	 * @param to
	 * @return
	 * @date 2018年5月11日 下午2:59:49
	 */
	@PostMapping("/ord/return/agreetoarefund")
	AgreeToARefundRo agreeToARefund(OrdOrderReturnTo to) {
		AgreeToARefundRo agreeToARefundRo = new AgreeToARefundRo();
		try {
			_log.info("同意退款的请求参数为：{}", to.toString());
			agreeToARefundRo = svc.agreeToARefund(to);
			_log.info("同意退款的返回值为：{}", agreeToARefundRo.toString());
			return agreeToARefundRo;
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("修改订单详情出错")) {
				agreeToARefundRo.setResult(AgreeToARefundDic.MODIFY_ORDER_DETAIL_ERROR);
				agreeToARefundRo.setMsg(msg);
			} else if (msg.equals("修改退货信息出错")) {
				agreeToARefundRo.setResult(AgreeToARefundDic.MODIFY_RETURN_ERROR);
				agreeToARefundRo.setMsg(msg);
			} else if (msg.equals("v支付出错，退款失败")) {
				agreeToARefundRo.setResult(AgreeToARefundDic.V_PAY_ERROR);
				agreeToARefundRo.setMsg(msg);
			} else {
				agreeToARefundRo.setResult(AgreeToARefundDic.ERROR);
				agreeToARefundRo.setMsg("同意退款失败");
			}
			return agreeToARefundRo;
		}
	}

	/**
	 * 同意退货 Title: agreeToReturn Description:
	 *
	 * @return
	 * @date 2018年5月11日 下午3:29:33
	 */
	@PostMapping("/ord/return/agreetoreturn")
	AgreeToReturnRo agreeToReturn(OrdOrderReturnTo to) {
		AgreeToReturnRo agreeToReturnRo = new AgreeToReturnRo();
		try {
			_log.info("同意退货的请求参数为：{}", to.toString());
			agreeToReturnRo = svc.agreeToReturn(to);
			_log.info("同意退货的返回值为：{}", agreeToReturnRo.toString());
			return agreeToReturnRo;
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("修改退货数量和返现总额出错")) {
				agreeToReturnRo.setResult(AgreeToReturnDic.MODIFY_RETURN_COUNT_AND_CASHBACK_TOTAL_AMOUNT_ERROR);
				agreeToReturnRo.setMsg(msg);
			} else if (msg.equals("修改退货信息错误")) {
				agreeToReturnRo.setResult(AgreeToReturnDic.MODIFY_RETURN_ERROR);
				agreeToReturnRo.setMsg(msg);
			} else {
				agreeToReturnRo.setResult(AgreeToReturnDic.ERROR);
				agreeToReturnRo.setMsg("操作失败");
			}
			return agreeToReturnRo;
		}
	}

	/**
	 * 已收到货并退款 Title: receivedAndRefunded Description:
	 *
	 * @param to
	 * @return
	 * @date 2018年5月11日 下午3:01:21
	 */
	@PostMapping("/ord/return/receivedandrefunded")
	ReceivedAndRefundedRo receivedAndRefunded(OrdOrderReturnTo to) {
		ReceivedAndRefundedRo receivedAndRefundedRo = new ReceivedAndRefundedRo();
		try {
			_log.info("已收到货并退款的请求参数为：{}", to.toString());
			receivedAndRefundedRo = svc.receivedAndRefunded(to);
			_log.info("已收到货并退款的返回值为：{}", receivedAndRefundedRo.toString());
			return receivedAndRefundedRo;
		} catch (RuntimeException e) {
			String msg = e.getMessage();
			if (msg.equals("修改订单详情退货状态出错")) {
				receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.MODIFY_ORDER_DETAIL_ERROR);
				receivedAndRefundedRo.setMsg(msg);
			} else if (msg.equals("确认收到货出错")) {
				receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.CONFIRM_RECEIPT_OF_GOODS_ERROR);
				receivedAndRefundedRo.setMsg(msg);
			} else if (msg.equals("v支付出错，退款失败")) {
				receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.V_PAY_ERROR);
				receivedAndRefundedRo.setMsg(msg);
			} else {
				receivedAndRefundedRo.setResult(ReceivedAndRefundedDic.ERROR);
				receivedAndRefundedRo.setMsg("退款失败");
			}
			return receivedAndRefundedRo;
		}
	}

	@GetMapping("/ord/order/returningInfo")
	List<Map<String, Object>> getReturningInfo(@RequestParam Map<String, Object> map) throws ParseException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		_log.info("查询用户退货中订单信息的参数为：{}", map.toString());
		List<Map<String, Object>> list = svc.selectReturningInfo(map);
		_log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
		return list;
	}

	@GetMapping("/ord/order/returnInfo")
	List<Map<String, Object>> getReturnInfo(@RequestParam Map<String, Object> map) throws ParseException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		_log.info("查询用户退货完成订单信息的参数为：{}", map.toString());
		List<Map<String, Object>> list = svc.selectReturnInfo(map);
		_log.info("查询退货订单信息的返回值：{}", String.valueOf(list));
		return list;
	}
}
