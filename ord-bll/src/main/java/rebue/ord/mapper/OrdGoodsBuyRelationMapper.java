package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdGoodsBuyRelationMapper extends MybatisBaseMapper<OrdGoodsBuyRelationMo, Long> {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int insert(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int insertSelective(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	OrdGoodsBuyRelationMo selectByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int updateByPrimaryKeySelective(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int updateByPrimaryKey(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	List<OrdGoodsBuyRelationMo> selectAll();

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	List<OrdGoodsBuyRelationMo> selectSelective(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	boolean existByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	boolean existSelective(OrdGoodsBuyRelationMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int countSelective(OrdGoodsBuyRelationMo record);

	OrdGoodsBuyRelationMo getBuyRelation(OrdGoodsBuyRelationMo mo);

	/**
	 * 查询是否存在商品购买关系
	 * 
	 * @param record
	 * @return
	 */
	int countGoodsBuyRelation(OrdGoodsBuyRelationMo record);

	/**
	 * 修改商品推广关系时间
	 * 
	 * @param record
	 * @return
	 */
	int updateGoodsBuyRelationTime(OrdGoodsBuyRelationMo record);

	/**
	 * 根据下家和上线id查询是否已存在购买关系
	 * @param downlineUserId
	 * @param onlineId
	 * @return
	 */
	@Select("select * from ORD_GOODS_BUY_RELATION where DOWNLINE_USER_ID=#{downlineUserId,jdbcType=BIGINT} and ONLINE_ID=#{onlineId,jdbcType=BIGINT}")
	OrdGoodsBuyRelationMo getByDownlineUserIdAndOnlineId(@Param("downlineUserId") Long downlineUserId,
			@Param("onlineId") Long onlineId);

	/**
	 * 根据id修改上家id
	 * @param uplineUserId
	 * @param id
	 * @return
	 */
	@Update("update ORD_GOODS_BUY_RELATION set UPLINE_USER_ID=#{uplineUserId,jdbcType=BIGINT} where ID = #{id,jdbcType=BIGINT}")
	int updateGoodsBuyRelation(@Param("uplineUserId") Long uplineUserId, @Param("id") Long id);
}
