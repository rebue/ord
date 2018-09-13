package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdSublevelBuyMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdSublevelBuyMapper extends MybatisBaseMapper<OrdSublevelBuyMo, Long> {
    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdSublevelBuyMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdSublevelBuyMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdSublevelBuyMo selectByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdSublevelBuyMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdSublevelBuyMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdSublevelBuyMo> selectAll();

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdSublevelBuyMo> selectSelective(OrdSublevelBuyMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdSublevelBuyMo record);
    
    /**
     * 根据上级ID更新购买关系
     */
    int updateByOrderDetailId(OrdSublevelBuyMo mo);
}