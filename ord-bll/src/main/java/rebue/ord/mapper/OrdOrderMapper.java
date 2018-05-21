package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdOrderMo;
import rebue.robotech.mapper.MybatisBaseMapper;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.Map;

@Mapper
public interface OrdOrderMapper extends MybatisBaseMapper<OrdOrderMo, Long> {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	int insert(OrdOrderMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	int insertSelective(OrdOrderMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	OrdOrderMo selectByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	int updateByPrimaryKeySelective(OrdOrderMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	int updateByPrimaryKey(OrdOrderMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	List<OrdOrderMo> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	List<OrdOrderMo> selectSelective(OrdOrderMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	boolean existByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER
	 *
	 * @mbg.generated 2018-05-21 11:37:34
	 */
	boolean existSelective(OrdOrderMo record);

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
	int modifyOrderStateByOderCode(@Param("orderCode") Long orderCode,
			@Param("orderState") byte orderState);

	/**
	 * 根据订单编号查询退货金额 Title: selectReturnAmountByOrderCode Description:
	 * 
	 * @param orderCode
	 * @return
	 * @date 2018年5月11日 上午11:12:07
	 */
	@Select("SELECT RETURN_TOTAL as returnTotal, RETURN_AMOUNT1 as returnAmount1, RETURN_AMOUNT2 as returnAmount2 FROM ORD_ORDER WHERE ORDER_CODE=#{orderCode,jdbcType=VARCHAR}")
	OrdOrderMo selectReturnAmountByOrderCode(
			@Param("orderCode") String orderCode);

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
	@Update("UPDATE ORD_ORDER SET ORDER_STATE=5, CLOSE_TIME=#{closeTime,jdbcType=TIMESTAMP} WHERE ORDER_CODE=#{orderCode,jdbcType=VARCHAR} AND ORDER_STATE=4")
	int finishSettlement(@Param("closeTime") Date closeTime,
			@Param("orderCode") String orderCode);

	/**
	 * 订单支付 Title: orderPay Description:
	 * 
	 * @param orderCode
	 * @param payTime
	 * @return
	 * @date 2018年5月18日 上午11:20:12
	 */
	@Update("UPDATE ORD_ORDER SET ORDER_STATE=2, PAY_TIME=#{payTime,jdbcType=TIMESTAMP}  WHERE ORDER_CODE=#{orderCode,jdbcType=VARCHAR} AND ORDER_STATE=1")
	int orderPay(@Param("orderCode") String orderCode,
			@Param("payTime") Date payTime);
}