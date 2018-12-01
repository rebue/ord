package rebue.ord.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import rebue.ord.dic.OrderTaskTypeDic;
import rebue.ord.mo.OrdTaskMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdTaskMapper extends MybatisBaseMapper<OrdTaskMo, Long> {

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	int deleteByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	int insert(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	int insertSelective(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	OrdTaskMo selectByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	int updateByPrimaryKeySelective(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	int updateByPrimaryKey(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	List<OrdTaskMo> selectAll();

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	List<OrdTaskMo> selectSelective(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	boolean existByPrimaryKey(Long id);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	@Override
	boolean existSelective(OrdTaskMo record);

	/**
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	int countSelective(OrdTaskMo record);

	/**
	 * 获取需要执行任务的ID列表(根据任务类型)
	 */
	@Select("select ID from ORD_TASK where EXECUTE_STATE=0 and EXECUTE_PLAN_TIME<=SYSDATE() and TASK_TYPE=#{taskType}")
	List<Long> selectThatShouldExecute(@Param("taskType") Byte taskType);

	/**
	 * 判断订单是否存在仍未完成的任务（包括未执行的和暂停执行的）
	 */
	@Select("select count(*)>0 from ORD_TASK where ORDER_ID=#{orderId} and TASK_TYPE=#{orderSettleTaskType} and (EXECUTE_STATE=0 or EXECUTE_STATE=2)")
	Boolean existUnfinished(@Param("orderId") String orderId,
			@Param("orderSettleTaskType") Byte orderSettleTaskType);

	/**
	 * 执行任务完成
	 *
	 * @param now       当前时间
	 * @param id        任务ID
	 * @param doneState 已执行状态
	 * @param noneState 未执行状态
	 */
	@Update("update ORD_TASK set EXECUTE_STATE=#{doneState}, EXECUTE_FACT_TIME=#{now} where ID=#{id} and EXECUTE_STATE=#{noneState}")
	int done(@Param("now") Date now, @Param("id") Long id, @Param("doneState") Byte doneState,
			@Param("noneState") Byte noneState);

	/**
	 * 改变任务执行状态
	 *
	 * @param tradeType     交易类型
	 * @param orderDetailId 订单详情ID
	 * @param beforeState   执行之前的状态
	 * @param afterState    执行之后的状态
	 */
	@Update("update ORD_TASK set EXECUTE_STATE=#{afterState} where TRADE_TYPE=#{tradeType} and ORDER_DETAIL_ID=#{orderDetailId} and EXECUTE_STATE=#{beforeState}")
	int updateExecuteState(@Param("tradeType") Integer tradeType, @Param("orderDetailId") String orderDetailId,
			@Param("beforeState") Integer beforeState, @Param("afterState") Integer afterState);

	/**
	 * 取消任务
	 *
	 * @param tradeType     交易类型
	 * @param orderDetailId 订单详情ID
	 * @param beforeState   执行之前的状态
	 * @param cancelState   取消状态
	 */
	@Update("update ORD_TASK set EXECUTE_STATE=#{cancelState} where ID=#{id} and EXECUTE_STATE=#{beforeState}")
	int cancelTask(@Param("id") Long id, @Param("beforeState") Byte beforeState,
			@Param("cancelState") Integer cancelState);

	/**
	 * 执行订单签收任务
	 */
	@Update("update ORD_TASK set EXECUTE_STATE=#{doneState}, EXECUTE_FACT_TIME=#{executeFactTime,jdbcType=TIMESTAMP} where ID=#{id} and EXECUTE_STATE=#{noneState}")
	int executeSignInOrderTask(@Param("executeFactTime") Date executeFactTime, @Param("id") long id,
			@Param("doneState") Byte doneState, @Param("noneState") Byte noneState);
}
