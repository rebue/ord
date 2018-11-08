package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdOrderDetailDeliverMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdOrderDetailDeliverMapper extends MybatisBaseMapper<OrdOrderDetailDeliverMo, Long> {
    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdOrderDetailDeliverMo selectByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderDetailDeliverMo> selectAll();

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdOrderDetailDeliverMo> selectSelective(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdOrderDetailDeliverMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdOrderDetailDeliverMo record);
}