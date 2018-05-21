package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.robotech.mapper.MybatisBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrdOrderDetailMapper
		extends
			MybatisBaseMapper<OrdOrderDetailMo, Long> {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	int insert(OrdOrderDetailMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	int insertSelective(OrdOrderDetailMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	OrdOrderDetailMo selectByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	int updateByPrimaryKeySelective(OrdOrderDetailMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	int updateByPrimaryKey(OrdOrderDetailMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	List<OrdOrderDetailMo> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	List<OrdOrderDetailMo> selectSelective(OrdOrderDetailMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	boolean existByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_ORDER_DETAIL
	 *
	 * @mbg.generated 2018-05-21 11:37:33
	 */
	boolean existSelective(OrdOrderDetailMo record);

	/**
	 * 根据订单编号订单详情ID修改退货数量和返现总额 Title: modifyReturnCountAndCashBackTotal
	 * Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年5月7日 上午9:46:55
	 */
	int modifyReturnCountAndCashBackTotal(OrdOrderDetailMo record);

	/**
	 * 根据详情ID修改退货状态 Title: modifyReturnStateById Description:
	 * 
	 * @param returnState
	 * @param id
	 * @return
	 * @date 2018年5月8日 上午10:56:51
	 */
	@Update("UPDATE ORD_ORDER_DETAIL SET RETURN_STATE = #{returnState,jdbcType=TINYINT} WHERE ID = #{id,jdbcType=BIGINT}")
	int modifyReturnStateById(@Param("returnState") byte returnState,
			@Param("id") long id);
}