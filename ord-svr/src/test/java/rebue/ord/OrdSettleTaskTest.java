package rebue.ord;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.ord.dic.SettleTaskExecuteStateDic;
import rebue.ord.dic.SettleTypeDic;
import rebue.ord.mo.OrdSettleTaskMo;
import rebue.ord.to.SuspendSettleTaskTo;
import rebue.wheel.OkhttpUtils;

public class OrdSettleTaskTest {

	private final static Logger _log = LoggerFactory.getLogger(OrdOrderTests.class);

	private final String hostUrl = "http://localhost:20180";

	//	@Test
	public void addTest() throws IOException {
		// 立即执行的时间
		final Date now = new Date();
		OrdSettleTaskMo mo = new OrdSettleTaskMo();
		mo.setExecuteState((byte) SettleTaskExecuteStateDic.NOT_PERFORMED.getCode());
		mo.setExecutePlanTime(new Date(now.getTime() + 60000));
		mo.setTradeType((byte) SettleTypeDic.WAIT_SETTLE.getCode());
		mo.setOrderId("123456789");
		mo.setIp("192.168.1.40");
		_log.info("添加结算任务的参数为：{}", mo);
		String result = OkhttpUtils.postByJsonParams(hostUrl + "/ord/settletask", mo);
		_log.info("添加结算任务的返回值为：{}", result);
	}

//	@Test
	public void suspendSettleTaskTest() throws IOException {
		SuspendSettleTaskTo to = new SuspendSettleTaskTo();
		to.setTradeType((byte) SettleTypeDic.WAIT_SETTLE.getCode());
		to.setOrderId(123456789L);
		_log.info("暂停结算任务的参数为：{}", to);
		String result = OkhttpUtils.putByJsonParams(hostUrl + "/ord/settletask/suspend", to);
		_log.info("暂停结算任务的返回值为：{}", result);
	}

//	@Test
	public void resumeSettleTaskTest() throws IOException {
		SuspendSettleTaskTo to = new SuspendSettleTaskTo();
		to.setTradeType((byte) SettleTypeDic.WAIT_SETTLE.getCode());
		to.setOrderId(123456789L);
		_log.info("恢复结算任务的参数为：{}", to);
		String result = OkhttpUtils.putByJsonParams(hostUrl + "/ord/settletask/resume", to);
		_log.info("恢复结算任务的返回值为：{}", result);
	}

//	@Test
	public void cancelSettleTaskTest() throws IOException {
		SuspendSettleTaskTo to = new SuspendSettleTaskTo();
		to.setTradeType((byte) SettleTypeDic.WAIT_SETTLE.getCode());
		to.setOrderId(123456789L);
		_log.info("取消结算任务的参数为：{}", to);
		String result = OkhttpUtils.putByJsonParams(hostUrl + "/ord/settletask/cancel", to);
		_log.info("取消结算任务的返回值为：{}", result);
	}

//	@Test
	public void getTaskIdsThatShouldExecuteTest() throws IOException {
		String result = OkhttpUtils.get(hostUrl + "/ord/settletask/getid");
		System.out.println(result);
	}
	
	@Test
	public void dateTest() {
		Long date = new Date().getTime();
		System.out.println(date);
		System.out.println(date + 86400000 * 7);
		Long thisTimestamp = System.currentTimeMillis();
		System.out.println(thisTimestamp);
		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(dd.format(date));
		System.out.println(dd.format(date + 86400000 * 8));
	}
}
