package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AgreeReturnTo {

	/**
	 * 退货id
	 */
	private Long id;

	/**
	 * 审核操作人
	 *
	 * 数据库字段: ORD_RETURN.REVIEW_OP_ID
	 *
	 * @mbg.generated 自动生成，如需修改，请删除本行
	 */
	private Long reviewOpId;

}
