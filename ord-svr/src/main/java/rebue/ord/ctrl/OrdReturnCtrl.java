package rebue.ord.ctrl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import com.github.pagehelper.PageInfo;
import rebue.ord.to.OrdOrderReturnTo;

@RestController
public class OrdReturnCtrl {
	/**
	 * @mbg.generated
	 */
	private final static Logger _log = LoggerFactory.getLogger(OrdReturnCtrl.class);

	/**
	 * @mbg.generated
	 */
	@Resource
	private OrdReturnSvc svc;

	/**
	 * 修改用户退货信息
	 * 
	 * @mbg.generated
	 */
	@PutMapping("/ord/return")
	Map<String, Object> modify(OrdReturnMo vo) throws Exception {
		_log.info("modify OrdReturnMo:" + vo);
		svc.modify(vo);
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		_log.info("modify OrdReturnMo success!");
		return result;
	}

	/**
	 * 获取单个用户退货信息
	 * 
	 * @mbg.generated
	 */
	@GetMapping("/ord/return/{id}")
	OrdReturnMo get(@PathVariable("id") java.lang.Long id) {
		_log.info("get OrdReturnMo by id: " + id);
		OrdReturnMo result = svc.getById(id);
		_log.info("get: " + result);
		return result;
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
			if (msg.equals("添加退货图片失败")) {
				_log.info(msg);
				addReturnRo.setResult(AddReturnDic.ADD_RETURN_PIC);
				addReturnRo.setMsg(msg);
			} else if (msg.equals("修改订单详情状态失败")) {
				_log.info(msg);
				addReturnRo.setResult(AddReturnDic.MODIFY_ORDER_DETAIL_STATE_ERROR);
				addReturnRo.setMsg(msg);
			} else {
				e.printStackTrace();
				addReturnRo.setResult(AddReturnDic.ERROR);
				addReturnRo.setMsg("提交失败");
			}
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
