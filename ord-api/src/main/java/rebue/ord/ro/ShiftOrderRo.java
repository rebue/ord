package rebue.ord.ro;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rebue.robotech.ro.Ro;

/**
 * 转移订单返回类
 * @author lbl
 *
 */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
public class ShiftOrderRo extends Ro {

	/**
	 * 订单实际金额
	 */
	private BigDecimal realMoney;
}
