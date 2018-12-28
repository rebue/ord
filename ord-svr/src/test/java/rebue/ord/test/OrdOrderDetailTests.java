package rebue.ord.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.afc.dic.TradeTypeDic;
import rebue.afc.mo.AfcTradeMo;
import rebue.ord.dic.CommissionStateDic;
import rebue.ord.dic.ReturnStateDic;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.svc.impl.OrdSettleTaskSvcImpl;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.IdRo;
import rebue.robotech.ro.Ro;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;
import rebue.wheel.exception.RuntimeExceptionX;

/**
 * 订单详情
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public class OrdOrderDetailTests {
	
    private static final Logger      _log = LoggerFactory.getLogger(OrdOrderDetailTests.class);


	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private final ObjectMapper _objectMapper = new ObjectMapper();

	/**
	 * 测试基本的增删改查
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
//	@Test
	public void testCrud() throws IOException, ReflectiveOperationException {
		OrdOrderDetailMo mo = null;
		for (int i = 0; i < 20; i++) {
			mo = (OrdOrderDetailMo) RandomEx.randomPojo(OrdOrderDetailMo.class);
			mo.setId(null);
			System.out.println("添加订单详情的参数为：" + mo);
			final String addResult = OkhttpUtils.postByJsonParams(hostUrl + "/ord/orderdetail", mo);
			System.out.println("添加订单详情的返回值为：" + addResult);
			final IdRo idRo = _objectMapper.readValue(addResult, IdRo.class);
			System.out.println(idRo);
			Assert.assertEquals(ResultDic.SUCCESS, idRo.getResult());
			mo.setId(Long.valueOf(idRo.getId()));
		}
		final String listResult = OkhttpUtils.get(hostUrl + "/ord/orderdetail?pageNum=1&pageSize=5");
		System.out.println("查询订单详情的返回值为：" + listResult);
		System.out.println("获取单个订单详情的参数为：" + mo.getId());
		final String getByIdResult = OkhttpUtils.get(hostUrl + "/ord/orderdetail/getbyid?id=" + mo.getId());
		System.out.println("获取单个订单详情的返回值为：" + getByIdResult);
		System.out.println("修改订单详情的参数为：" + mo);
		final String modifyResult = OkhttpUtils.putByJsonParams(hostUrl + "/ord/orderdetail", mo);
		System.out.println("修改积分日志类型的返回值为：" + modifyResult);
		final Ro modifyRo = _objectMapper.readValue(modifyResult, Ro.class);
		System.out.println(modifyRo);
		Assert.assertEquals(ResultDic.SUCCESS, modifyRo.getResult());
		System.out.println("删除订单详情的参数为：" + mo.getId());
		final String deleteResult = OkhttpUtils.delete(hostUrl + "/ord/orderdetail?id=" + mo.getId());
		System.out.println("删除订单详情的返回值为：" + deleteResult);
		final Ro deleteRo = _objectMapper.readValue(deleteResult, Ro.class);
		System.out.println(deleteRo);
		Assert.assertEquals(ResultDic.SUCCESS, deleteRo.getResult());
	}

	private final String hostUrl = "http://127.0.0.1:20180";

	@Test
	public void selectCommission() throws IOException, ReflectiveOperationException {
		OkhttpUtils.setReadTimeout(30);
		final String result = OkhttpUtils.get(hostUrl + "/ord/order/commission");
		System.out.println(result);
	}
	
}
