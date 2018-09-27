package rebue.ord.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import rebue.ord.mo.OrdTaskMo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdTaskMapper extends MybatisBaseMapper<OrdTaskMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdTaskMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdTaskMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdTaskMo> selectSelective(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdTaskMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdTaskMo> countSelective(OrdTaskMo record);

    /**
     *  查询订单任务数量 Title: selectByExecutePlanTimeBeforeNow Description:
     *
     *  @param executeState
     *  @param taskType
     *  @return
     *  @date 2018年5月28日 上午10:40:02
     */
    @Select("select ID from ORD_TASK where EXECUTE_STATE=${executeState} and TASK_TYPE=${taskType} and EXECUTE_PLAN_TIME <= SYSDATE()")
    List<Long> selectByExecutePlanTimeBeforeNow(@Param("executeState") byte executeState, @Param("taskType") byte taskType);

    /**
     *  执行订单签收任务 Title: executeSignInOrderTask Description:
     *
     *  @return
     *  @date 2018年5月21日 下午2:11:18
     */
    @Update("update ORD_TASK set EXECUTE_STATE=#{doneState}, EXECUTE_FACT_TIME=#{executeFactTime,jdbcType=TIMESTAMP} where ID=#{id} and EXECUTE_STATE=#{noneState}")
    int executeSignInOrderTask(@Param("executeFactTime") Date executeFactTime, @Param("id") long id, @Param("doneState") Byte doneState, @Param("noneState") Byte noneState);
}
