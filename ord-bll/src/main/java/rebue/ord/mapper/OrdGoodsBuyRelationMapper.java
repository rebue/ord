package rebue.ord.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdGoodsBuyRelationMapper extends MybatisBaseMapper<OrdGoodsBuyRelationMo, Long> {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insert(OrdGoodsBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insertSelective(OrdGoodsBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    OrdGoodsBuyRelationMo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKeySelective(OrdGoodsBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKey(OrdGoodsBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdGoodsBuyRelationMo> selectAll();

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdGoodsBuyRelationMo> selectSelective(OrdGoodsBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
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

//	/**
//	 * 根据下家和上线id查询是否已存在购买关系
//	 * @param downlineUserId
//	 * @param onlineId
//	 * @return
//	 */
//	@Select("select * from ORD_GOODS_BUY_RELATION where DOWNLINE_USER_ID=#{downlineUserId,jdbcType=BIGINT} and ONLINE_ID=#{onlineId,jdbcType=BIGINT}")
//	OrdGoodsBuyRelationMo getByDownlineUserIdAndOnlineId(@Param("downlineUserId") Long downlineUserId,
//			@Param("onlineId") Long onlineId);

    /**
     * 根据下家和上线id删除已存在购买关系
     */
    @Delete("DELETE FROM ORD_GOODS_BUY_RELATION where DOWNLINE_USER_ID=#{downlineUserId,jdbcType=BIGINT} and ONLINE_ID=#{onlineId,jdbcType=BIGINT}")
    OrdGoodsBuyRelationMo deleteByDownlineUserIdAndOnlineId(@Param("downlineUserId") Long downlineUserId, @Param("onlineId") Long onlineId);

    /**
     * 根据id修改上家id
     * 
     * @param uplineUserId
     * @param id
     * @return
     */
    @Update("update ORD_GOODS_BUY_RELATION set UPLINE_USER_ID=#{uplineUserId,jdbcType=BIGINT} where ID = #{id,jdbcType=BIGINT}")
    int updateGoodsBuyRelation(@Param("uplineUserId") Long uplineUserId, @Param("id") Long id);
}
