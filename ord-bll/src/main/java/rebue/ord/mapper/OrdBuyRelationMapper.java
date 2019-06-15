package rebue.ord.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import rebue.ord.dic.ReturnStateDic;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdBuyRelationMapper extends MybatisBaseMapper<OrdBuyRelationMo, Long> {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insert(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insertSelective(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    OrdBuyRelationMo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKeySelective(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKey(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdBuyRelationMo> selectAll();

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdBuyRelationMo> selectSelective(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existSelective(OrdBuyRelationMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdBuyRelationMo record);

    /**
     * 根据上家ID更新购买关系表
     */
    int updateByUplineOrderDetailId(OrdBuyRelationMo mo);

    /**
     * 根据oderId来获取订单详情
     */
    List<OrdOrderDetailMo> getDetailByOrderId(Long orderId);

    /**
     * 获取下家订单详情
     */
    @Select("SELECT ONLINE_TITLE downOnlineTitle  FROM ORD_ORDER_DETAIL  WHERE ID=#{id,jdbcType=TINYINT}")
    DetailandBuyRelationRo getDownlineOrdDetail(@Param("id") Long id);

    /**
     * 统计所有已签收超过7天的上下家订单详情的数量
     * 
     * @param uplineOrderDetailId
     *            上家的订单详情
     * @param lastTime
     *            最晚签收时间
     * @param noneReturnState
     *            未退货的退货状态
     * @return 已签收超过7天的订单详情的数量，如果!=3，不符合返佣条件
     */
    @Select("SELECT " + //
            "    COUNT(b.ID)" + //
            " FROM" + //
            "    ORD_ORDER_DETAIL a" + //
            "        INNER JOIN" + //
            "    ORD_ORDER b ON a.ORDER_ID = b.ID" + //
            " WHERE" + //
            "    a.ID IN (SELECT " + //
            "            DOWNLINE_ORDER_DETAIL_ID" + //
            "        FROM" + //
            "            ORD_BUY_RELATION" + //
            "        WHERE" + //
            "            UPLINE_ORDER_DETAIL_ID = #{uplineOrderDetailId} UNION SELECT #{uplineOrderDetailId})" + //
            "        AND a.RETURN_STATE = #{noneReturnState}" + //
            "        AND b.RECEIVED_TIME IS NOT NULL" + //
            "        AND b.RECEIVED_TIME <= #{lastTime,jdbcType=TIMESTAMP}")
    int countSettledOfRelations(@Param("uplineOrderDetailId") Long uplineOrderDetailId, @Param("lastTime") Date lastTime, @Param("noneReturnState") ReturnStateDic noneReturnState);

    
    /**
     * 根据下家订单详情id将下家是否签收改为已签收
     * @param detailId
     * @return
     */
	@Update("UPDATE ORD_BUY_RELATION a,ORD_ORDER b SET a.IS_SIGN_IN = true WHERE a.DOWNLINE_ORDER_ID = b.ID AND a.DOWNLINE_ORDER_DETAIL_ID = #{downlineDetailId,jdbcType=TINYINT} and b.IS_NOW_RECEIVED=true ")
    int updateIsSignInByDownlineDetailId(@Param("downlineDetailId") Long downlineDetailId );
	
}
