package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
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
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdBuyRelationMo> countSelective(OrdBuyRelationMo record);

    /**
     * 根据上家ID更新购买关系表
     * @param mo
     * @return
     */
    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);
    
    /**
     * 根据oderId来获取订单详情
     * @param id
     * @return
     */
    List<OrdOrderDetailMo> getDetailByOrderId(Long orderId);
    
    /**
     * 获取上家名字
     * @param id
     * @return
     */
    @Select("SELECT LOGIN_NAME uplineUserName FROM suc.SUC_USER  WHERE ID=#{id,jdbcType=TINYINT}")
    DetailandBuyRelationRo getUplineUserName(@Param("id") Long id);
    /**
     * 获取下家名字
     * @param id
     * @return
     */
    @Select("SELECT LOGIN_NAME downlineUserName FROM suc.SUC_USER  WHERE ID=#{id,jdbcType=TINYINT}")
    DetailandBuyRelationRo getDownlineUserName(@Param("id") Long id);
    
    /**
     * 获取下家订单详情
     * @param id
     * @return
     */
    @Select("SELECT ONLINE_TITLE downOnlineTitle  FROM ORD_ORDER_DETAIL  WHERE ID=#{id,jdbcType=TINYINT}")
    DetailandBuyRelationRo getDownlineOrdDetail(@Param("id") Long id);
    

    
    

    
}
