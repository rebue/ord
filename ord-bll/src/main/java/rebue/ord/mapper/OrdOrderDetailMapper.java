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
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insert(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insertSelective(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    OrdOrderDetailMo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKeySelective(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKey(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdOrderDetailMo> selectAll();

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdOrderDetailMo> selectSelective(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existSelective(OrdOrderDetailMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdOrderDetailMo record);

    /**
     * 
     */
    int updateReturn(@Param("returnTotal") Integer returnTotal, @Param("newCashbackTotal") BigDecimal newCashbackTotal, @Param("returnState") Byte returnState,
            @Param("id") Long whereDetailId, @Param("returnedCount") Integer whereReturnedCount, @Param("oldCashbackTotal") BigDecimal whereOldCashbackTotal);

    /**
     * 根据详情ID修改退货状态
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET RETURN_STATE = #{returnState,jdbcType=TINYINT} WHERE ID = #{id,jdbcType=BIGINT}")
    int modifyReturnStateById(@Param("returnState") byte returnState, @Param("id") long id);
    
    /**
     * 根据详情ID修改退货状态
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET IS_DELIVERED = 1 WHERE ORDER_ID = #{orderId,jdbcType=BIGINT}")
    int modifyIsDeliverByOrderId(@Param("orderId") long orderId);

    int updateCashbackSlot(OrdOrderDetailMo mo);

    int updateCommissionSlotForBuyRelation(OrdOrderDetailMo mo);

    OrdOrderDetailMo getOrderDetailForBuyRelation(Map<String, Object> map);

    OrdOrderDetailMo getOrderDetailForOneCommissonSlot(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByInvite(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByFour(Map<String, Object> map);

    List<OrdOrderDetailMo> getDetailByOrderId(Long orderId);

    /**
     * 得到买家已下单指定上线规格商品的数量(以此来限制买家购买)
     *
     * @param userId
     *            购买用户的用户ID
     * @param onlineSpecId
     *            上线规格ID
     */
    @Select("SELECT " //
            + "    IFNULL(SUM(a.BUY_COUNT - a.RETURN_COUNT),0) "//
            + " FROM" //
            + "    ORD_ORDER_DETAIL AS a," //
            + "    ORD_ORDER AS b " //
            + " WHERE" //
            + "    a.ORDER_ID = b.ID" //
            + "        AND a.USER_ID = #{userId}" //
            + "        AND a.ONLINE_SPEC_ID = #{onlineSpecId}" //
            + "        AND b.ORDER_STATE > 0")
    int getBuyerOrderedCount(Long userId, Long onlineSpecId);
}
