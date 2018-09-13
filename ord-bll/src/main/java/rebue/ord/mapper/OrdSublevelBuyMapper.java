package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
     * 根据上线ID更新购买关系
     */
    @Update("UPDATE ORD_SUBLEVEL SET SUBLEVEL_USER_ID =#{sublevelUserId,jdbcType=BIGINT} WHERE ORDER_DETAIL_ID = {orderDetailId,jdbcType=BIGINT}") 
    int updateByOrderDetailId(Long orderDetailId,Long sublevelUserId);
}