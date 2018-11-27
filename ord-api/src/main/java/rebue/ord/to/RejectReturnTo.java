package rebue.ord.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class RejectReturnTo {

	/**
	 * 退货id
	 */
	private Long id;

	/**
	 * 拒绝操作人
	 *
	 * 数据库字段: ORD_RETURN.REJECT_OP_ID
	 */
	private Long rejectOpId;

	/**
	 * 拒绝原因
	 *
	 * 数据库字段: ORD_RETURN.REJECT_REASON
	 */
	private String rejectReason;
}
