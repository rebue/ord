package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import rebue.ord.mo.OrdSettleTaskMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdSettleTaskMapper extends MybatisBaseMapper<OrdSettleTaskMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdSettleTaskMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdSettleTaskMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdSettleTaskMo> selectSelective(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdSettleTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdSettleTaskMo record);

    /**
     *  改变任务执行状态
     *
     *  @param tradeType   交易类型
     *  @param orderId     订单ID
     *  @param beforeState 执行之前的状态
     *  @param afterState  执行之后的状态
     *  @return
     */
    @Update("update ORD_SETTLE_TASK set EXECUTE_STATE=#{afterState} where TRADE_TYPE=#{tradeType} and ORDER_ID=#{orderId} and EXECUTE_STATE=#{beforeState}")
    int updateSettleExecuteState(@Param("tradeType") Byte tradeType, @Param("orderId") Long orderId, @Param("beforeState") Integer beforeState, @Param("afterState") Integer afterState);

    /**
     *  取消任务
     *
     *  @param tradeType   交易类型
     *  @param id          结算任务ID
     *  @param beforeState 执行之前的状态
     *  @param cancelState 取消状态
     *  @return
     */
    @Update("update ORD_SETTLE_TASK set EXECUTE_STATE=#{cancelState} where ID=#{id} and EXECUTE_STATE=#{beforeState}")
    int cancelSettleTask(@Param("id") Long id, @Param("beforeState") Byte beforeState, @Param("cancelState") Integer cancelState);

    /**
     * 获取计划执行时间在当前时间之前的任务列表
     */
    @Select("select ID from ORD_SETTLE_TASK where EXECUTE_STATE=0 and EXECUTE_PLAN_TIME<=SYSDATE()")
    List<Long> selectByExecutePlanTimeBeforeNow();
}
