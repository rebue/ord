/**
 * 
 */
package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 订单退货状态枚举
 *
 */
public enum OrderReturnStateDic implements EnumBase {
    /** 作废（取消） **/
    CANCEL(-1),
    /** 待审核 **/
    PENDING_APPROVAL(1),
    /** 退货中 **/
    RETURNING(2),
    /** 已退货 **/
    ALREADY_RETURN(3),
    /** 已拒绝 **/
    ALREADY_REJECT(4);
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
    public static OrderReturnStateDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (OrderReturnStateDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    OrderReturnStateDic(final int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return code;
    }

}
