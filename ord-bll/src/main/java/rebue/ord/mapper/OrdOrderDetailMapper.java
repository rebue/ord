package rebue.ord.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdOrderDetailMapper extends MybatisBaseMapper<OrdOrderDetailMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdOrderDetailMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderDetailMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderDetailMo> selectSelective(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdOrderDetailMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdOrderDetailMo record);

    /**
     *  修改订单详情的退货情况(根据订单详情ID、已退货数量、旧的返现金总额，修改退货总数、返现金总额以及退货状态)
     */
    int updateReturn(@Param("returnTotal") Integer returnTotal, @Param("newCashbackTotal") BigDecimal newCashbackTotal, @Param("returnState") Byte returnState, @Param("id") Long whereDetailId, @Param("returnedCount") Integer whereReturnedCount, @Param("oldCashbackTotal") BigDecimal whereOldCashbackTotal);

    /**
     *  根据详情ID修改退货状态
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET RETURN_STATE = #{returnState,jdbcType=TINYINT} WHERE ID = #{id,jdbcType=BIGINT}")
    int modifyReturnStateById(@Param("returnState") byte returnState, @Param("id") long id);

    /**
     *  根据详情ID修改发货状态
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET IS_DELIVERED = 1 WHERE ORDER_ID = #{orderId,jdbcType=BIGINT}")
    int modifyIsDeliverByOrderId(@Param("orderId") long orderId);

    /**
     *  根据订单id修改发货组织id和供应商id
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET  DELIVER_ORG_ID= #{deliverOrgId,jdbcType=BIGINT} , SUPPLIER_ID= #{supplierId,jdbcType=BIGINT}  WHERE ORDER_ID = #{orderId,jdbcType=BIGINT}")
    int modifyOrg(@Param("supplierId") long supplierId, @Param("deliverOrgId") long deliverOrgId, @Param("orderId") long orderId);

    /**
     *  修改订单详情实际购买金额
     *
     *  @param id
     *  @param newActualAmount
     *  @param oldActualAmount
     *  @return
     */
    @Update("update ORD_ORDER_DETAIL set ACTUAL_AMOUNT=#{newActualAmount,jdbcType=DECIMAL}, RETURN_STATE = #{returnState,jdbcType=TINYINT} where ID=#{id,jdbcType=BIGINT} and ACTUAL_AMOUNT=#{oldActualAmount,jdbcType=DECIMAL} and RETURN_STATE = #{returnedState,jdbcType=TINYINT}")
    int updateActualAmountANDReturnState(@Param("id") Long id, @Param("newActualAmount") BigDecimal newActualAmount, @Param("oldActualAmount") BigDecimal oldActualAmount, @Param("returnState") Byte returnState, @Param("returnedState") Byte returnedState);

    int updateCashbackSlot(OrdOrderDetailMo mo);

    int updateCommissionSlotForBuyRelation(OrdOrderDetailMo mo);

    OrdOrderDetailMo getOrderDetailForBuyRelation(Map<String, Object> map);

    OrdOrderDetailMo getOrderDetailForOneCommissonSlot(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByInvite(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByFour(Map<String, Object> map);

    List<OrdOrderDetailMo> getDetailByOrderId(Long orderId);

    /**
     *  得到买家已下单指定上线规格商品的数量(以此来限制买家购买)
     *
     *  @param userId       购买用户的用户ID
     *  @param onlineSpecId 上线规格ID
     */
    @// 
    Select("SELECT " + "    IFNULL(SUM(a.BUY_COUNT - a.RETURN_COUNT),0) " + " FROM" + "    ORD_ORDER_DETAIL AS a," + "    ORD_ORDER AS b " + " WHERE" + "    a.ORDER_ID = b.ID" + "        AND a.USER_ID = #{userId}" + "        AND a.ONLINE_SPEC_ID = #{onlineSpecId}" + "        AND b.ORDER_STATE > 0")
    int getBuyerOrderedCount(Long userId, Long onlineSpecId);

    /**
     *  修改返现总额、退货数量
     *
     *  @param id
     *  @param oldCashbackTotal
     *  @param newCashbackTotal
     *  @param returnedCount
     *  @param returnTotal
     *  @return
     */
    @Update("update ORD_ORDER_DETAIL set CASHBACK_TOTAL=#{newCashbackTotal,jdbcType=DECIMAL}, RETURN_COUNT = #{returnTotal,jdbcType=INTEGER} where ID=#{id,jdbcType=BIGINT} and CASHBACK_TOTAL=#{oldCashbackTotal,jdbcType=DECIMAL} and RETURN_COUNT = #{returnedCount,jdbcType=INTEGER}")
    int updateReturnNumAndCashbackTotal(@Param("id") Long id, @Param("oldCashbackTotal") BigDecimal oldCashbackTotal, @Param("newCashbackTotal") BigDecimal newCashbackTotal, @Param("returnedCount") Integer returnedCount, @Param("returnTotal") Integer returnTotal);

    /**
     *  根据id修改供应商id
     *  @param id 订单详情id
     *  @param newSupplierId 新供应商id
     *  @param oldSupplierId 旧供应商id
     *  @return
     */
    @Update("update ORD_ORDER_DETAIL set SUPPLIER_ID=#{newSupplierId,jdbcType=BIGINT} where ID=#{id,jdbcType=BIGINT} and SUPPLIER_ID=#{oldSupplierId,jdbcType=BIGINT}")
    int updateSupplierIdById(@Param("id") Long id, @Param("newSupplierId") Long newSupplierId, @Param("oldSupplierId") Long oldSupplierId);
}
