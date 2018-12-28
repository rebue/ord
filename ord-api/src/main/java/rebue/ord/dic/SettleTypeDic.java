package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 1: 待结算（用户签收后添加，与其他结算任务无关）
 * 50: 结算-结算供应商(将成本打到供应商的余额)
 * 51: 结算-结算返现金(将返现金打到买家的返现金)
 * 52: 结算-结算已占用保证金(释放卖家的已占用保证金相应金额)
 * 53: 结算-结算卖家(将利润打到卖家的余额)
 * 54: 结算-结算返现中金额(打到买家的返现中金额)
 * 55: 结算-结算平台服务费
 * 56: 结算-结算返佣金(将返佣中的金额移到余额，注意是上家在本次交易中应获得的返佣金金额，而不是上家的全部返佣中的金额)
 * 57: 结算-结算返佣中金额(打到上家的返佣中金额)
 *
 */
public enum SettleTypeDic implements EnumBase {

    /**
     * 1：待结算（用户签收后添加，默认配置7日+1小时后执行，添加其它子结算任务）
     */
    WAIT_SETTLE(1),
    /**
     * 50: 结算-结算供应商(将成本打到供应商的余额)
     */
    SETTLE_SUPPLIER(50),
    /**
     * 51: 结算-结算返现金(将返现中的金额移到返现金，注意是买家在本次交易中应获得的返现金金额，而不是买家的全部返现中的返现金)
     */
    SETTLE_CASHBACK(51),
    /**
     * 52: 结算-结算已占用保证金(释放卖家的已占用保证金相应金额)
     */
    SETTLE_DEPOSIT_USED(52),
    /**
     * 53: 结算-结算卖家(将利润打到卖家的余额)
     */
    SETTLE_SELLER(53),
    /**
     * 54: 结算-结算返现中金额(打到买家的返现中金额)
     */
    SETTLE_CASHBACKING(54),
    /**
     * 55: 结算-结算平台服务费
     */
    SETTLE_PLATFORM_SERVICE_FEE(55),
    /**
     * 56: 结算-结算返佣金(将返佣中的金额移到余额，注意是上家在本次交易中应获得的返佣金金额，而不是上家的全部返佣中的金额)
     */
    SETTLE_COMMISSION(56),
    /**
     * 57: 结算-结算返佣中金额(打到上家的返佣中金额)
     */
    SETTLE_COMMISSIONING(57);

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
    public static SettleTypeDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (SettleTypeDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    SettleTypeDic(final int code) {
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
