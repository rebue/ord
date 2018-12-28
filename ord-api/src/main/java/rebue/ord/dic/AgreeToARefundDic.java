package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 下午4:27:16 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：AgreeToARefundDic.java
 *        类说明： 同意退款返回值字典
 */
public enum AgreeToARefundDic implements EnumBase {

    /** 审核成功 **/
    SUCCESS(1),

    /** 参数不正确 **/
    PARAM_NOT_CORRECT(-1),

    /** 退款金额不能为空 **/
    REFUND_AMOUNT_NOT_NULL(-2),

    /** 订单不存在 **/
    ORDER_NOT_EXIST(-3),

    /** 订单未支付或已取消 **/
    ORDER_NOT_PAY_OR_ALREADY_CANCEL(-4),

    /** 用户未购买该商品 **/
    USER_NOT_PURCHASE_THE_GOODS(-5),

    /** 用户未申请退款 **/
    USER_NOT_APPLYFOR_REFUND(-6),

    /** 退货信息不存在 **/
    RETURN_NOT_EXIST(-7),

    /** 订单已退款或已取消申请 **/
    ORDER_ALREADY_REFUND_OR_ALREADY_CANCEL_APPLYFOR(-8),

    /** 退货单已退款 **/
    RETURN_ALREADY_REFUND(-9),

    /** 修改订单退货金额出错 **/
    MODIFY_ORDER_RETURN_AMOUNT_ERROR(-10),

    /** 退货已审核 **/
    RETURN_ALREADY_APPROVE(-11),

    /** 当前状态不允许拒绝 **/
    CURRENT_STATE_NOT_EXIST_REJECT(-12),

    /** 修改订单详情出错 **/
    MODIFY_ORDER_DETAIL_ERROR(-13),

    /** 修改退货信息出错 **/
    MODIFY_RETURN_ERROR(-14),

    /** v支付出错 **/
    V_PAY_ERROR(-15),

    /** 审核失败 **/
    ERROR(-16);

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
    public static AgreeToARefundDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (AgreeToARefundDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    AgreeToARefundDic(final int code) {
        this.code = code;
    }

    /**
     * @return jackson序列化时，输出枚举实例的code
     */
    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name();
    }

}
