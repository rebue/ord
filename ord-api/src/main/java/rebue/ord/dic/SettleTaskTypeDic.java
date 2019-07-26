package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 结算任务类型字典
 * 1: 结算-结算供应商(将成本打到供应商的余额)
 * 2: 结算-结算返现金给买家
 * 3: 结算-释放卖家的已占用保证金
 * 4: 结算-将利润打到卖家的余额
 * 5: 结算-结算平台服务费
 * 6: 结算-结算返佣金
 * 7: 结算-结算购买所得积分给买家
 * 8: 结算-结算利润给平台
 */
public enum SettleTaskTypeDic implements EnumBase {

    /**
     * 1: 结算-结算成本给供应商(余额+)
     */
    SETTLE_COST_TO_SUPPLIER(1),
    /**
     * 2: 结算-结算返现金给买家
     */
    SETTLE_CASHBACK_TO_BUYER(2),
    /**
     * 3: 结算-释放卖家的已占用保证金
     */
    FREE_DEPOSIT_USED_OF_SELLER(3),
    /**
     * 4: 结算-结算利润给卖家(余额+)
     */
    SETTLE_PROFIT_TO_SELLER(4),
    /**
     * 5: 结算-结算平台服务费
     */
    SETTLE_PLATFORM_SERVICE_FEE(5),
    /**
     * 6: 结算-结算返佣金
     */
    SETTLE_COMMISSION(6),
    /**
     * 7: 结算-结算购买所得积分给买家
     */
    SETTLE_POINT_TO_BUYER(7),

    /**
     * 结算-结算利润给平台
     */
    SETTLE_PROFIT_TO_PLATFORM(8);

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
    public static SettleTaskTypeDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (SettleTaskTypeDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    SettleTaskTypeDic(final int code) {
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
