package rebue.ord.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     *  根据订单编号订单详情ID修改退货数量和返现总额 Title: modifyReturnCountAndCashBackTotal
     *  Description:
     *
     *  @param record
     *  @return
     *  @date 2018年5月7日 上午9:46:55
     */
    int modifyReturnCountAndCashBackTotal(OrdOrderDetailMo record);

    /**
     *  根据详情ID修改退货状态 Title: modifyReturnStateById Description:
     *
     *  @param returnState
     *  @param id
     *  @return
     *  @date 2018年5月8日 上午10:56:51
     */
    @Update("UPDATE ORD_ORDER_DETAIL SET RETURN_STATE = #{returnState,jdbcType=TINYINT} WHERE ID = #{id,jdbcType=BIGINT}")
    int modifyReturnStateById(@Param("returnState") byte returnState, @Param("id") long id);

    int updateCashbackSlot(OrdOrderDetailMo mo);

    int updateCommissionSlotForBuyRelation(OrdOrderDetailMo mo);

    OrdOrderDetailMo getOrderDetailForBuyRelation(Map<String, Object> map);

    OrdOrderDetailMo getOrderDetailForOneCommissonSlot(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByInvite(Map<String, Object> map);

    OrdOrderDetailMo getAndUpdateBuyRelationByFour(Map<String, Object> map);

    List<OrdOrderDetailMo> getDetailByOrderId(Long orderId);
}
