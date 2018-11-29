package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 订单任务类型字典 1：订单自动取消的任务 2：订单自动签收的任务 3: 订单开始结算的任务 4: 订单结算的任务 5：订单完成结算的任务
 */
public enum OrderTaskTypeDic implements EnumBase {

	/**
	 * 1：订单自动取消的任务
	 */
	CANCEL(1),
	/**
	 * 2：订单自动签收的任务
	 */
	SIGNED(2),
	/**
	 * 3：订单开始结算的任务 用户签收后先添加此任务，在此任务执行的时候再分别添加各订单结算任务
	 */
	START_SETTLE(3),
	/**
	 * 4：订单结算的任务 具体结算任务类型看子任务类型
	 */
	SETTLE(4),
	/**
	 * 5：订单完成结算的任务
	 */
	COMPLETE_SETTLE(5);

	/**
	 * 枚举的所有项，注意这个变量是静态单例的
	 */
	private static Map<Integer, EnumBase> valueMap;
	// 初始化map，保存枚举的所有项到map中以方便通过code查找
	static {
		valueMap = new HashMap<>();
		for (final EnumBase item : values()) {
			valueMap.put(item.getCode(), item);
		}
	}

	/**
	 * jackson反序列化时，通过code得到枚举的实例 注意：此方法必须是static的方法，且返回类型必须是本枚举类，而不能是接口EnumBase
	 * 否则jackson将调用默认的反序列化方法，而不会调用本方法
	 */
	@JsonCreator
	public static OrderTaskTypeDic getItem(final int code) {
		final EnumBase result = valueMap.get(code);
		if (result == null) {
			throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
		}
		return (OrderTaskTypeDic) result;
	}

	private int code;

	/**
	 * 构造器，传入code
	 */
	OrderTaskTypeDic(final int code) {
		this.code = code;
	}

	/**
	 * @return jackson序列化时，输出枚举实例的code
	 */
	@Override
	public int getCode() {
		return code;
	}
}
