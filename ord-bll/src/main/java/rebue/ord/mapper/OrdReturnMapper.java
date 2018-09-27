package rebue.ord.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.OrdReturnRo;
import rebue.robotech.mapper.MybatisBaseMapper;

@Mapper
public interface OrdReturnMapper extends MybatisBaseMapper<OrdReturnMo, Long> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    OrdReturnMo selectByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdReturnMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdReturnMo> selectSelective(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(Long id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(OrdReturnMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<OrdReturnMo> countSelective(OrdReturnMo record);

    /**
     *  查询分页列表信息 Title: selectReturnPageList Description:
     *
     *  @param record
     *  @return
     *  @date 2018年4月21日 下午3:35:27
     */
    List<OrdReturnRo> selectReturnPageList(OrdReturnMo record);

    /**
     *  退货审核通过 Title: updateReturnApprove Description:
     *
     *  @param record
     *  @return
     *  @date 2018年4月21日 下午5:14:48
     */
    int returnApprove(OrdReturnMo record);

    /**
     *  拒绝退货 Title: refusedReturn Description:
     *
     *  @param record
     *  @return
     *  @date 2018年5月7日 上午11:22:50
     */
    int refusedReturn(OrdReturnMo record);

    /**
     *  确认退款 Title: confirmTheRefund Description:
     *
     *  @param record
     *  @return
     *  @date 2018年5月7日 下午3:50:29
     */
    int confirmTheRefund(OrdReturnMo record);

    /**
     *  退货确认收到货 Title: confirmReceiptOfGoods Description:
     *
     *  @param record
     *  @return
     *  @date 2018年5月8日 上午11:39:22
     */
    int confirmReceiptOfGoods(OrdReturnMo record);

    /**
     *  根据用户ID查询用户退货中订单
     */
    List<OrdReturnMo> selectReturningOrder(Map<String, Object> map);

    /**
     *  根据用户ID查询用户退货完成订单
     */
    List<OrdReturnMo> selectReturnOrder(Map<String, Object> map);
}
