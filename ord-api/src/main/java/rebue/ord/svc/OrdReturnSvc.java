package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToARefundRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import com.github.pagehelper.PageInfo;
import rebue.ord.to.OrdOrderReturnTo;

public interface OrdReturnSvc
		extends
			MybatisBaseSvc<OrdReturnMo, java.lang.Long> {

	/**
	 * 添加用户退货信息 Title: addEx Description:
	 * 
	 * @param to
	 * @return
	 * @date 2018年4月19日 下午2:53:00
	 */
	AddReturnRo addReturn(OrdOrderReturnTo to);

	/**
	 * 查询分页列表信息 Title: selectReturnPageList Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年4月21日 下午3:35:27
	 */
	PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnMo record, int pageNum,
			int pageSize);

	/**
	 * 拒绝退货 Title: rejectReturn Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年4月27日 下午3:10:47
	 */
	RejectReturnRo rejectReturn(OrdReturnMo record);

	/**
	 * 同意退货 Title: agreeToReturn Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年5月11日 下午2:45:13
	 */
	AgreeToReturnRo agreeToReturn(OrdOrderReturnTo mo);

	/**
	 * 同意退款 Title: agreeToARefund Description:
	 * 
	 * @param to
	 * @return
	 * @date 2018年5月11日 下午2:45:36
	 */
	AgreeToARefundRo agreeToARefund(OrdOrderReturnTo to);

	/**
	 * 已收到货并退款 Title: receivedAndRefunded Description:
	 * 
	 * @param to
	 * @return
	 * @date 2018年5月11日 下午3:02:40
	 */
	ReceivedAndRefundedRo receivedAndRefunded(OrdOrderReturnTo to);

}