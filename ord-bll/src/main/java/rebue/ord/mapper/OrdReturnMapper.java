package rebue.ord.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.ord.mo.OrdReturnMo;
import rebue.robotech.mapper.MybatisBaseMapper;
import rebue.ord.ro.OrdReturnRo;

@Mapper
public interface OrdReturnMapper extends MybatisBaseMapper<OrdReturnMo, Long> {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	int insert(OrdReturnMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	int insertSelective(OrdReturnMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	OrdReturnMo selectByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	int updateByPrimaryKeySelective(OrdReturnMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	int updateByPrimaryKey(OrdReturnMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	List<OrdReturnMo> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	List<OrdReturnMo> selectSelective(OrdReturnMo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	boolean existByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table ORD_RETURN
	 *
	 * @mbg.generated 2018-05-10 17:42:37
	 */
	boolean existSelective(OrdReturnMo record);

	/**
	 * 查询分页列表信息 Title: selectReturnPageList Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年4月21日 下午3:35:27
	 */
	List<OrdReturnRo> selectReturnPageList(OrdReturnMo record);

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
	int refusedReturn(OrdReturnMo record);

	/**
	 * 确认退款 Title: confirmTheRefund Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年5月7日 下午3:50:29
	 */
	int confirmTheRefund(OrdReturnMo record);

	/**
	 * 退货确认收到货 Title: confirmReceiptOfGoods Description:
	 * 
	 * @param record
	 * @return
	 * @date 2018年5月8日 上午11:39:22
	 */
	int confirmReceiptOfGoods(OrdReturnMo record);
}