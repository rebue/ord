package rebue.ord.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import rebue.ord.mo.OrdReturnMo;
import rebue.ord.to.OrdReturnTo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdReturnMapper extends MybatisBaseMapper<OrdReturnMo, Long> {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insert(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int insertSelective(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    OrdReturnMo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKeySelective(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    int updateByPrimaryKey(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdReturnMo> selectAll();

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    List<OrdReturnMo> selectSelective(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existByPrimaryKey(Long id);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    boolean existSelective(OrdReturnMo record);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(OrdReturnMo record);

    /**
     * 查询分页列表信息 Title: selectReturnPageList Description:
     *
     * @param record
     * @return
     * @date 2018年4月21日 下午3:35:27
     */
    List<OrdReturnTo> selectReturnPageList(OrdReturnTo record);

    /**
     * 退货审核通过 Title: updateReturnApprove Description:
     *
     * @param record
     * @return
     * @date 2018年4月21日 下午5:14:48
     */
    int returnApprove(OrdReturnMo record);

    /**
     * 拒绝退货 Title: refusedReturn Description:
     *
     * @param record
     * @return
     * @date 2018年5月7日 上午11:22:50
     */
    int refusedReturn(OrdReturnTo record);

    /**
     * 确认退款
     * 
     * @param refundTotal
     *            退款总额
     * @param deductAmount
     *            扣除补偿金额
     * @param applicationState
     *            申请状态为已完成
     * @param opId
     *            操作人ID
     * @param opTime
     *            操作时间
     * @param returnId
     *            退货单ID
     * @return
     */
    int confirmRefund(@Param("returnTotal") BigDecimal refundTotal, @Param("deductAmount") BigDecimal deductAmount, //
            @Param("applicationState") Byte applicationState, @Param("opId") Long opId, //
            @Param("opTime") Date opTime, @Param("id") Long returnId);

    /**
     * 退货确认收到货 Title: confirmReceiptOfGoods Description:
     *
     * @param record
     * @return
     * @date 2018年5月8日 上午11:39:22
     */
    int confirmReceiptOfGoods(OrdReturnMo record);

    /**
     * 根据用户ID查询用户退货中订单
     */
    List<OrdReturnMo> selectReturningOrder(Map<String, Object> map);

    /**
     * 根据用户ID查询用户退货完成订单
     */
    List<OrdReturnMo> selectReturnOrder(Map<String, Object> map);
}
