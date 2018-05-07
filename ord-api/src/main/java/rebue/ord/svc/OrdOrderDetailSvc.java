package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;

import java.math.BigDecimal;

import rebue.ord.mo.OrdOrderDetailMo;

public interface OrdOrderDetailSvc extends MybatisBaseSvc<OrdOrderDetailMo, java.lang.Long>{

	/**
	 * 根据订单编号、详情ID修改退货数量、返现总额
	 * Title: modifyReturnCountAndCashBackTotal
	 * Description: 
	 * @param orderId
	 * @param orderDetailId
	 * @param returnCount
	 * @param cashbackTotal
	 * @return
	 * @date 2018年5月7日 上午9:54:27
	 */
	int modifyReturnCountAndCashBackTotal(long orderId, long orderDetailId, int returnCount, BigDecimal cashbackTotal);

}