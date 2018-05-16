package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 上午9:25:22 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：UsersToPlaceTheOrderDic.java 类说明： 用户下订单返回值字典
 */
public enum UsersToPlaceTheOrderDic implements EnumBase {

	/** 下单成功 **/
	SUCCESS(1),
	
	/** 收货地址不能为空 **/
	DELIVERY_ADDRESS_NOT_NULL(-1),
	
	/** 生成订单出错 **/
	CREATE_ORDER_ERROR(-2),
	
	/** 生成订单详情出错 **/
	CREATE_ORDER_DETAIL_ERROR(-3),
	
	/** 删除购物车和修改上线数量出错 **/
	DELETE_CART_AND_MODIFY_ONLINE_COUNT_ERROR(-4),
	
	/** 商品不存在购物车 **/
	GOODS_NOT_EXIST_CART(-5),
	
	/** 商品未上线 **/
	GOODS_NOT_ONLINE(-6),
	
	/** 扣减上线数量失败 **/
	SUBTRACT_ONLINE_COUNT_ERROR(-7),
	
	/** 删除购物车失败 **/
	DELETE_CART_ERROR(-8),
	
	/** 库存不足 **/
	INSUFFICIENT_INVENTORY(-9),
	
	/** 下单失败 **/
	ERROR(-10);

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
	public static UsersToPlaceTheOrderDic getItem(int code) {
		EnumBase result = valueMap.get(code);
		if (result == null) {
			throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
		}
		return (UsersToPlaceTheOrderDic) result;
	}

	private int code;

	/**
	 * 构造器，传入code
	 */
	UsersToPlaceTheOrderDic(int code) {
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
