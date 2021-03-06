package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdGoodsBuyRelationMapper extends MybatisBaseMapper<OrdGoodsBuyRelationMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdGoodsBuyRelationMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdGoodsBuyRelationMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdGoodsBuyRelationMo> selectSelective(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdGoodsBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdGoodsBuyRelationMo> countSelective(OrdGoodsBuyRelationMo record);

    OrdGoodsBuyRelationMo getBuyRelation(OrdGoodsBuyRelationMo mo);
    
    /**
     * 查询是否存在商品购买关系
     * @param record
     * @return
     */
    int countGoodsBuyRelation(OrdGoodsBuyRelationMo record);
    
    /**
     * 修改商品推广关系时间
     * @param record
     * @return
     */
    int updateGoodsBuyRelationTime(OrdGoodsBuyRelationMo record);
}
