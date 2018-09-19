package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdBuyRelationMapper extends MybatisBaseMapper<OrdBuyRelationMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdBuyRelationMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdBuyRelationMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdBuyRelationMo> selectSelective(OrdBuyRelationMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdBuyRelationMo record);

    /**
     * 根据上家ID更新购买关系表
     * @param mo
     * @return
     */
    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);
}
