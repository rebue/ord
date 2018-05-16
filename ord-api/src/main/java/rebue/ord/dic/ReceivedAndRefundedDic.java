package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 下午4:55:02 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：ReceivedAndRefundedDic.java 类说明： 已收到货并退款返回值字典
 */
public enum ReceivedAndRefundedDic implements EnumBase {

	/** 退款成功 **/
	SUCCESS(1),

	/** 参数不正确 **/
	PARAM_NOT_CORRECT(-1),
	
	/** 退货信息不能为空 **/
	RETURN_NOT_NULL(-2),
	
	/** 当前状态不允许退款**/
	CURRENT_STATE_NOT_EXIST_REFUND(-3),
	
	/** 订单不存在 **/
	ORDER_NOT_EXIST(-4),
	
	/** 订单未支付或已取消 **/
	ORDER_NOT_PAY_OR_ALREADY_CANCEL(-5),
	
	/** 用户未购买该商品 **/
	USER_NOT_PURCHASE_GOODS(-6),
	
	/** 退款金额不能为空 **/
	REFUND_AMOUNT_NOT_NULL(-7),

	/** 商品未申请或已完成退货 **/
	GOODS_NOT_APPLYFOR_OR_HAVE_FINISHED_RETURN(-8),
	
	/** 修改订单状态出错 **/
	MODIFY_ORDER_STATE_ERROR(-9),
	
	/** 修改订单详情出错 **/
	MODIFY_ORDER_DETAIL_ERROR(-10),
	
	/** 确认收到货出错 **/
	CONFIRM_RECEIPT_OF_GOODS_ERROR(-11),
	
	/** v支付出错 **/
	V_PAY_ERROR(-12),
	
	/** 退款失败 **/
	ERROR(-13);

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
	public static ReceivedAndRefundedDic getItem(int code) {
		EnumBase result = valueMap.get(code);
		if (result == null) {
			throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
		}
		return (ReceivedAndRefundedDic) result;
	}

	private int code;

	/**
	 * 构造器，传入code
	 */
	ReceivedAndRefundedDic(int code) {
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
