package rebue.ord.dic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import rebue.wheel.baseintf.EnumBase;

/**
 * 创建时间：2018年5月16日 下午2:45:23 项目名称：ord-api
 * 
 * @author daniel
 * @version 1.0
 * @since JDK 1.8 文件名称：AddReturnDic.java
 *        类说明： 添加退货信息返回值字典
 */
public enum AddReturnDic implements EnumBase {

    /** 添加成功 **/
    SUCCESS(1),

    /** 订单不存在 **/
    ORDER_NOT_EXIST(-1),

    /** 订单已取消 **/
    ORDER_ALREADY_CANCEL(-2),

    /** 订单未支付 **/
    ORDER_NOT_PAY(-3),

    /** 订单退货完成 **/
    ORDER_ALREADY_RETURN_FINISH(-4),

    /** 当前状态不允许退货 **/
    CURRENT_STATE_NOT_EXIST_RETURN(-5),

    /** 订单商品退货完成 **/
    GOODS_ALREADY_RETURN_FINISH(-6),

    /** 退货数量不能大于购买数量 **/
    NOT_RETURN_COUNT_GR_BUY_COUNT(-7),

    /** 添加退货信息出错 **/
    ADD_RETURN_ERROR(-8),

    /** 添加退货图片出错 **/
    ADD_RETURN_PIC(-9),

    /** 修改订单详情状态失败 **/
    MODIFY_ORDER_DETAIL_STATE_ERROR(-10),

    /** 添加失败 **/
    ERROR(-5);

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
    public static AddReturnDic getItem(final int code) {
        final EnumBase result = valueMap.get(code);
        if (result == null) {
            throw new IllegalArgumentException("输入的code" + code + "不在枚举的取值范围内");
        }
        return (AddReturnDic) result;
    }

    private int code;

    /**
     * 构造器，传入code
     */
    AddReturnDic(final int code) {
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
