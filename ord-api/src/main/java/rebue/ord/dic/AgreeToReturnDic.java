package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 下午3:58:25 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：AgreeToReturnDic.java 类说明： 同意退货返回值字典
 */
public enum AgreeToReturnDic implements EnumBase {

	/** 审核成功 **/
	SUCCESS(1),

	/** 参数不正确 **/
	PARAM_NOT_CORRECT(-1),

	/** 退货信息不存在 **/
	RETURN_NOT_EXIST(-2),
	
	/** 退货已审核 **/
	RETURN_ALREADY_APPROVE(-3),

	/** 当前状态不允许拒绝 **/
	CURRENT_STATE_NOT_EXIST_REJECT(-3),

	/** 订单不存在 **/
	ORDER_NOT_EXIST(-4),
	
	/** 订单未支付或已取消 **/
	ORDER_NOT_PAY_OR_ALREADY_CANCEL(-5),
	
	/** 订单详情不能为空 **/
	ORDER_DETAIL_NOT_NULL(-6),

	/** 商品未申请退货或已完成退货 **/
	GOODS_NOT_APPLYFOR_RETURN_OR_ALREADY_FINISH(-7),
	
	/** 修改退货金额出错 **/
	MODIFY_ORDER_RETURN_AMOUNT_ERROR(-8),
	
	/** 修改退货数量和返现总额出错 **/
	MODIFY_RETURN_COUNT_AND_CASHBACK_TOTAL_AMOUNT_ERROR(-9),
	
	/** 修改退货信息出错 **/
	MODIFY_RETURN_ERROR(-10),

	/** 审核失败 **/
	ERROR(-11);

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
	public static AgreeToReturnDic getItem(int code) {
		EnumBase result = valueMap.get(code);
		if (result == null) {
			throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
		}
		return (AgreeToReturnDic) result;
	}

	private int code;

	/**
	 * 构造器，传入code
	 */
	AgreeToReturnDic(int code) {
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
