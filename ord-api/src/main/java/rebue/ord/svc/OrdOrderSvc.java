package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;
import rebue.ord.mo.OrdOrderMo;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface OrdOrderSvc extends MybatisBaseSvc<OrdOrderMo, java.lang.Long> {

	/**
	 * 用户下订单 Title: placeOrder Description:
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @date 2018年4月9日 上午10:54:22
	 */
	Map<String, Object> placeOrder(String orderJson) throws JsonParseException,
			JsonMappingException, IOException;

	/**
	 * 查询用户订单信息 Title: selectOrderInfo Description:
	 * 
	 * @param mo
	 * @return
	 * @throws ParseException
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date 2018年4月9日 下午4:48:40
	 */
	List<Map<String, Object>> selectOrderInfo(Map<String, Object> map)
			throws ParseException, IntrospectionException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException;

	/**
	 * 取消订单 Title: cancellationOfOrder Description:
	 * 
	 * @return
	 * @date 2018年4月9日 下午6:49:19
	 */
	Map<String, Object> cancellationOfOrder(OrdOrderMo mo);

	/**
	 * 修改订单实际金额 Title: updateOrderRealMoney Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月12日 下午2:59:53
	 */
	Map<String, Object> updateOrderRealMoney(OrdOrderMo mo);

	/**
	 * 设置快递公司 Title: setUpExpressCompany Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月13日 上午11:23:14
	 */
	Map<String, Object> setUpExpressCompany(OrdOrderMo mo);

	/**
	 * 取消发货 Title: cancelDeliveryUpdateOrderState Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月13日 下午2:59:19
	 */
	Map<String, Object> cancelDeliveryUpdateOrderState(OrdOrderMo mo);

	/**
	 * 确认发货 Title: sendAndPrint Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月13日 下午6:19:09
	 */
	Map<String, Object> shipmentConfirmation(OrdOrderMo mo);

	/**
	 * 订单签收 Title: orderSignIn Description:
	 * 
	 * @param mo
	 * @return
	 * @date 2018年4月14日 下午2:29:58
	 */
	Map<String, Object> orderSignIn(OrdOrderMo mo);

}