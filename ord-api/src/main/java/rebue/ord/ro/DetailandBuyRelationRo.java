package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class DetailandBuyRelationRo {

	/** 订单详情ID **/
	private Long id;

	/** 订单ID **/
	private Long orderId;

	/** 上线ID **/
	private Long onlineId;

	/** 产品ID **/
	private Long productId;

	/** 上线标题 **/
	private String onlineTitle;

	/** 规格名称 **/
	private String specName;

	/** 购买数量 **/
	private Integer buyCount;

	/** 购买价格 **/
	private BigDecimal buyPrice;

	/** 返现金额 **/
	private BigDecimal cashbackAmount;

	/** 返现总额 **/
	private BigDecimal cashbackTotal;
	
	/** 成本价 **/
    private BigDecimal costPrice;

	/** 购买单位 **/
	private String buyUnit;

	/** 退货数量 **/
	private Integer returnCount;

	/** 退货状态（0：未退货 1：退货中 2：已退货） **/
	private Byte returnState;

	/**供应商名字**/
	private String  supplierName;
	/**
	 * 版块类型（0：普通，1：全返）
	 *
	 * 数据库字段: ORD_ORDER_DETAIL.SUBJECT_TYPE
	 *
	 * 
	 */
	private Byte subjectType;

	/**
	 * 返现佣金名额
	 */
	private Byte cashbackCommissionSlot;

	/**
	 * 返现佣金状态（0：匹配中，1：待返，2：已返)
	 */
	private Byte cashbackCommissionState;

	/**
	 * 上家用户名字
	 */
	private String uplineUserName;
	/**
	 * 上家来源
	 */
	private Byte uplineRelationSource;
	/**
	 * 上家是否签收
	 */
	private Boolean uplineIsSignIn;

	/**
	 * 第一条关系来源 关系来源（1：自己匹配自己 2：购买关系 3：注册关系 4：差一人且已有购买关系 5：差两人 6：差一人但没有购买关系）
	 */
	private Byte downlineRelationSource1;

	/**
	 * 第二条关系来源 关系来源（1：自己匹配自己 2：购买关系 3：注册关系 4：差一人且已有购买关系 5：差两人 6：差一人但没有购买关系）
	 */
	private Byte downlineRelationSource2;
	/**
	 * 买家一是否签收
	 */
	private Boolean downlineIsSignIn1;
	/**
	 * 买家二是否签收
	 */
	private Boolean downlineIsSignIn2;

	/**
	 * 下家用户名字
	 *
	 * 数据库字段: ORD_BUY_RELATION.DOWNLINE_USER_ID
	 *
	 * 
	 */
	private String downlineUserName1;

	/**
	 * 第二下家用户名字
	 *
	 * 数据库字段: ORD_BUY_RELATION.DOWNLINE_USER_ID
	 *
	 * 
	 */
	private String downlineUserName2;

	/** 第一个下家订单上线标题 **/
	private String downOnlineTitle1;

	/** 第二个下家订单上线标题 **/
	private String downOnlineTitle2;
	
	private Boolean isDeliver;

}
