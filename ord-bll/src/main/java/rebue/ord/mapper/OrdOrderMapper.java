package rebue.ord.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import rebue.ord.mo.OrdOrderMo;
import rebue.ord.to.ListOrderTo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdOrderMapper extends MybatisBaseMapper<OrdOrderMo, Long> {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insert(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insertSelective(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    OrdOrderMo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKeySelective(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKey(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdOrderMo> selectAll();

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdOrderMo> selectSelective(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existSelective(OrdOrderMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdOrderMo record);

    /**
     * 查询用户订单信息 Title: selectOrderInfo Description:
     *
     * @param record
     * @return
     * @date 2018年4月9日 下午4:47:12
     */
    List<OrdOrderMo> selectOrderInfo(Map<String, Object> map);

    /**
     * 用户取消订单修改订单状态 Title: cancellationOrderUpdateOrderState Description:
     *
     * @param record
     * @return
     * @date 2018年4月9日 下午7:27:48
     */
    int cancellationOrderUpdateOrderState(OrdOrderMo record);

    /**
     * 修改订单实际金额 Title: updateOrderRealMoney Description:
     *
     * @param record
     * @return
     * @date 2018年4月12日 下午2:38:57
     */
    int updateOrderRealMoney(OrdOrderMo record);

    /**
     * 设置快递公司 Title: setUpExpressCompany Description:
     *
     * @param record
     * @return
     * @date 2018年4月13日 上午11:19:14
     */
    int setUpExpressCompany(OrdOrderMo record);

    /**
     * 取消发货修改订单状态 Title: cancelDeliveryUpdateOrderState Description:
     *
     * @param record
     * @return
     * @date 2018年4月13日 下午2:56:33
     */
    int cancelDeliveryUpdateOrderState(OrdOrderMo record);

    /**
     * 确认发货并修改订单状态 Title: shipmentConfirmation Description:
     *
     * @param record
     * @return
     * @date 2018年4月13日 下午6:15:29
     */
    int shipmentConfirmation(OrdOrderMo record);

    /**
     * 订单签收 Title: orderSignIn Description:
     *
     * @param record
     * @return
     * @date 2018年4月14日 下午2:18:26
     */
    int orderSignIn(OrdOrderMo record);

    /**
     * 修改退货金额 Title: modifyReturnAmount Description:
     *
     * @param record
     * @return
     * @date 2018年5月7日 上午9:09:35
     */
    int modifyReturnAmountByorderCode(OrdOrderMo record);

    /**
     * 根据订单编号修改订单状态 Title: modifyOrderStateByOderCode Description:
     *
     * @param orderCode
     * @param orderState
     * @return
     * @date 2018年5月8日 上午10:40:39
     */
    @Update("UPDATE ORD_ORDER SET ORDER_STATE = #{orderState,jdbcType=TINYINT} WHERE ORDER_CODE=#{orderCode,jdbcType=VARCHAR}")
    int modifyOrderStateByOderCode(@Param("orderCode") Long orderCode, @Param("orderState") byte orderState);

    /**
     * 根据订单编号查询退货金额 Title: selectReturnAmountByOrderCode Description:
     *
     * @param orderCode
     * @return
     * @date 2018年5月11日 上午11:12:07
     */
    @Select("SELECT RETURN_TOTAL as returnTotal, RETURN_AMOUNT1 as returnAmount1, RETURN_AMOUNT2 as returnAmount2, REAL_MONEY as realMoney FROM ORD_ORDER WHERE ORDER_CODE=#{orderCode,jdbcType=VARCHAR}")
    OrdOrderMo selectReturnAmountByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据订单编号修改订单 Title: updateByOrderCode Description:
     *
     * @param OrdOrderMo
     * @return
     * @date 2018年5月15日
     */
    int updateByOrderCode(OrdOrderMo record);

    /**
     * 完成结算 Title: finishSettlement Description:
     *
     * @return
     * @date 2018年5月17日 下午3:07:55
     */
    @Update("UPDATE ORD_ORDER SET ORDER_STATE=5, CLOSE_TIME=#{closeTime,jdbcType=TIMESTAMP} WHERE ID=#{orderId,jdbcType=VARCHAR} AND ORDER_STATE=4")
    int finishSettlement(@Param("closeTime") Date closeTime, @Param("orderId") String orderId);

    /**
     * 订单支付完成，根据PAY_ORDER_ID修改订单状态为已支付
     */
    @Update("UPDATE ORD_ORDER SET ORDER_STATE=2, PAY_TIME=#{payTime,jdbcType=TIMESTAMP}  WHERE PAY_ORDER_ID=#{payOrderId} AND ORDER_STATE=1")
    int paidOrder(@Param("payOrderId") Long payOrderId, @Param("payTime") Date payTime);

    /**
     * 根据订单编号查询订单状态 Title: selectOrderStateByOrderCode Description:
     *
     * @param orderCode
     * @return
     * @date 2018年5月21日 下午4:57:04
     */
    @Select("select ORDER_STATE from ORD_ORDER where ID = #{id,jdbcType=VARCHAR}")
    Byte selectOrderStateByOrderCode(@Param("id") String id);

    /**
     * 分页查询订单信息
     */
    List<OrdOrderMo> listOrder(ListOrderTo to);

    /**
     * 根据支付订单ID获取订单详情列表
     */
    List<OrdOrderMo> listByPayOrderId(Long payOrderId);

    /**
     * 修改收件人信息
     */
    int updateOrderReceiverInfo(OrdOrderMo record);

    /**
     * 根据订单id修改支付订单id
     */
    @Update("update ORD_ORDER set PAY_ORDER_ID = #{payOrderId,jdbcType=BIGINT} where ID = #{id,jdbcType=BIGINT}")
    int updatePayOrderId(@Param("payOrderId") Long payOrderId, @Param("id") Long id);
    
    /**
     * 根据订单id查询订单签收时间
     * @param orderIds
     * @return
     */
    @Select("select RECEIVED_TIME from ORD_ORDER where ID in (${orderIds}) order by RECEIVED_TIME desc")
    List<OrdOrderMo> selectOrderSignTime(@Param("orderIds") String orderIds);
}
