package rebue.ord.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.mo.OrdOrderMo;
import rebue.ord.ro.OrdSettleRo;
import rebue.ord.to.ListOrderTo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdOrderMapper extends MybatisBaseMapper<OrdOrderMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdOrderMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderMo> selectSelective(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdOrderMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
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

    // 
    int updateRefund(@Param("returnTotal") BigDecimal refundTotal, @Param("orderState") Byte orderState, @Param("id") Long whereOrderId, @Param("returnedTotal") BigDecimal whereRefundedTotal);

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
     * 设置订单结算完成
     */
    @Update("UPDATE ORD_ORDER SET ORDER_STATE=5, CLOSE_TIME=#{closeTime,jdbcType=TIMESTAMP} WHERE ID=#{orderId,jdbcType=VARCHAR} AND ORDER_STATE=4")
    int completeSettle(@Param("closeTime") Date closeTime, @Param("orderId") String orderId);

    /**
     * 设置订单状态为已支付，根据PAY_ORDER_ID修改订单状态为已支付
     */
    @Update("UPDATE ORD_ORDER SET ORDER_STATE=2, PAY_TIME=#{payTime,jdbcType=TIMESTAMP} WHERE PAY_ORDER_ID=#{payOrderId} AND ORDER_STATE=1")
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
     * 供应商分页查询订单信息
     */
    List<OrdOrderMo> listOrderSupplier(ListOrderTo to);



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
     *
     * @param orderIds
     * @return
     */
    @Select("select RECEIVED_TIME from ORD_ORDER where ID in (${orderIds}) order by RECEIVED_TIME desc")
    List<OrdOrderMo> selectOrderSignTime(@Param("orderIds") String orderIds);

    @// 
    Update("UPDATE ORD_ORDER a " + "SET " + "    a.ORDER_MONEY = (SELECT " + "            SUM(b.BUY_COUNT * b.BUY_PRICE)" + "        FROM" + "            ORD_ORDER_DETAIL b" + "        WHERE" + "            b.ORDER_ID = #{orderId})," + "    a.REAL_MONEY = (SELECT " + "            SUM(c.ACTUAL_AMOUNT)" + "        FROM" + "            ORD_ORDER_DETAIL c" + "        WHERE" + "            c.ORDER_ID = #{orderId})" + " WHERE" + "    a.ID = #{orderId}")
    void updateAmountAfterSplitOrder(@Param("orderId") Long orderId);

    /**
     * 根据供应商Id获取未结算的详情总额
     *
     * @param orgId
     * @param orderState
     * @return
     */
    @Select("SELECT  SUM(COST_PRICE * BUY_COUNT - RETURN_COUNT * COST_PRICE ) AS notSettle FROM  ORD_ORDER_DETAIL WHERE ORDER_ID IN (SELECT DISTINCT Id FROM ORD_ORDER WHERE  ORDER_STATE   in (2,3,4)) and SUPPLIER_ID=${supplierId}")
    OrdSettleRo getNotSettleTotal(@Param("supplierId") Long supplierId);

    /**
     * 根据供应商Id获取结算的详情总额
     *
     * @param orgId
     * @param orderState
     * @return
     */
    @Select("SELECT  SUM(COST_PRICE * BUY_COUNT - RETURN_COUNT * COST_PRICE ) AS AlreadySettle FROM  ORD_ORDER_DETAIL WHERE ORDER_ID IN (SELECT DISTINCT Id FROM ORD_ORDER WHERE  ORDER_STATE   in (5)) and SUPPLIER_ID=${supplierId} ")
    OrdSettleRo getSettleTotal(@Param("supplierId") Long supplierId);
    
  
    /**
     * 根据订单id修改发货组织
     */
    @Update("update ORD_ORDER set DELIVER_ORG_ID = #{deliverOrgId,jdbcType=BIGINT} where ID = #{id,jdbcType=BIGINT}")
    int updateOrg(@Param("deliverOrgId") Long deliverOrgId, @Param("id") Long id);
    /**
     * 根据用户id来获取已支付，已发货，已签收的订单详情待全返金额
     * @param userId
     * @return
     */
    @Select("SELECT \n" + 
    		"    SUM(BUY_PRICE * (BUY_COUNT - RETURN_COUNT))\n" + 
    		"FROM\n" + 
    		"    ord.ORD_ORDER_DETAIL\n" + 
    		"WHERE\n" + 
    		"    ORDER_ID IN (SELECT \n" + 
    		"            id\n" + 
    		"        FROM\n" + 
    		"            ord.ORD_ORDER \n" + 
    		"        WHERE\n" + 
    		"            USER_ID = #{userId,jdbcType=BIGINT}\n" + 
    		"                AND order_State > 1 )\n" + 
    		"        AND COMMISSION_STATE IN (0 , 1)\n" + 
    		"        AND SUBJECT_TYPE = 1\n" + 
    		"        AND RETURN_STATE != 2")
    BigDecimal getCommissionTotal(@Param("userId") Long userId);
}
