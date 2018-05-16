package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 下午3:10:20 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：RejectReturnDic.java 
 * 类说明： 拒绝退货返回值字典
 */
public enum RejectReturnDic implements EnumBase {

	/** 添加成功 **/
	SUCCESS(1),

	/** 参数不正确 **/
	PARAM_NOT_CORRECT(-1),
	
	/** 退货信息不存在 **/
	RETURN_NOT_EXIST(-2),
	
	/** 当前状态不允许拒绝 **/
	CURRENT_STATE_NOT_EXIST_REJECT(-3),
	
	/** 订单不存在 **/
	ORDER_NOT_EXIST(-4),
	
	/** 订单已取消 **/
	ORDER_ALREADY_CANCEL(-5),
	
	/** 订单已退货或未退货 **/
	ORDER_ALREADY_RETURN_OR_NOT_RETURN(-6),
	
	/** 修改订单详情出错 **/
	MODIFY_ORDER_DETAIL_ERROR(-7),
	
	/** 添加失败 **/
	ERROR(-8);

	/**
	 * 枚举的所有项，注意这个变量是静态单例的
	 */
	private static Map<Integer, EnumBase> valueMap;
	// 初始化map，保存枚举的所有项到map中以方便通过code查找
	static {
		valueMap = new HashMap<>();
		for (EnumBase item : values()) {
			valueMap.put(item.getCode(), item);
		}
	}

	/**
	 * jackson反序列化时，通过code得到枚举的实例 注意：此方法必须是static的方法，且返回类型必须是本枚举类，而不能是接口EnumBase
	 * 否则jackson将调用默认的反序列化方法，而不会调用本方法
	 */
	@JsonCreator
	public static RejectReturnDic getItem(int code) {
		EnumBase result = valueMap.get(code);
		if (result == null) {
			throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
		}
		return (RejectReturnDic) result;
	}

	private int code;

	/**
	 * 构造器，传入code
	 */
	RejectReturnDic(int code) {
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
