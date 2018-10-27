package rebue.ord.ro;

import lombok.Data;

@Data
public class OrdBuyRelationRo {
	/**
	 * 上家用户ID
	 */
	private Long uplineUserId;
	/**
	 * 上家订单ID
	 */
	private Long uplineOrderId;

	/**
	 * 上家订单详情ID
	 */
	private Long uplineOrderDetailId;

	/**
	 * 下家用户ID
	 */
	private Long downlineUserId;

	/**
	 * 下家订单ID
	 */
	private Long downlineOrderId;

	/**
	 * 下家订单详情ID
	 */
	private Long downlineOrderDetailId;

	/**
	 * 是否已签收
	 */
	private Boolean isSignIn;

	/**
	 * 关系来源（1：自己匹配自己 2：购买关系 3：注册关系 4：差一人且已有购买关系 5：差两人 6：差一人但没有购买关系）
	 */
	private Byte relationSource;
	
	/**
	 * 下家用户昵称
	 */
	private String downlineUserNickName;
	
	/**
	 * 下家用户头像
	 */
	private String downlineUserWxFace;
}
