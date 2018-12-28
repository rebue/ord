package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

public enum BuyRelationDic implements EnumBase {

    /**
     * 匹配自己规则匹配购买关系
     */
    BuyRelationByOwn(1),
    /**
     * 购买规则匹配购买关系
     */
    BuyRelationByPromote(2),
    /**
     * 邀请关系匹配购买关系
     */
    BuyRelationByInvite(3),
    /**
     * 匹配差一人，且邀请一人（包括关系来源是购买关系的或者是自己匹配自己的）
     */
    BuyRelationByFour(4),
    /**
     * 差两人的规则匹配购买关系
     */
    BuyRelationByFive(5),
    /**
     * 匹配差一人的规则匹配购买关系
     */
    BuyRelationBySix(6);

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
    public static BuyRelationDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (BuyRelationDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    BuyRelationDic(final int code) {
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
