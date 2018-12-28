package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 订单状态字典
 * -1. 作废（取消）
 * 1. 已下单（待支付）
 * 2. 已支付（待发货）
 * 3. 已发货（待签收）
 * 4. 已签收（待结算）
 * 6. 开始结算
 * 5. 已结算
 */
public enum OrderStateDic implements EnumBase {

    /**
     * -1. 作废（取消）
     */
    CANCEL(-1),
    /**
     * 1. 已下单（待支付）
     */
    ORDERED(1),
    /**
     * 2. 已支付（待发货）
     */
    PAID(2),
    /**
     * 3. 已发货（待签收）
     */
    DELIVERED(3),
    /**
     * 4. 已签收（待结算）
     */
    SIGNED(4),
    /**
     * 6. 开始结算
     */
    START_SETTLE(6),
    /**
     * 5. 已结算
     */
    SETTLED(5);

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
    public static OrderStateDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (OrderStateDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    OrderStateDic(final int code) {
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
