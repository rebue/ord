package rebue.ord.svc;

import com.github.pagehelper.PageInfo;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import rebue.ord.jo.OrdReturnJo;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.ReturnPageListRo;
import rebue.ord.to.AddReturnTo;
import rebue.ord.to.AgreeReturnTo;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.ord.to.ReceivedAndRefundedTo;
import rebue.ord.to.RejectReturnTo;
import rebue.robotech.ro.Ro;
import rebue.robotech.svc.BaseSvc;

/**
 * 用户退货信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdReturnSvc extends BaseSvc<java.lang.Long, OrdReturnMo, OrdReturnJo> {

    /**
     * 添加用户退货信息
     */
    Ro addReturn(AddReturnTo to);

    /**
     * 查询分页列表信息
     */
    PageInfo<ReturnPageListRo> selectReturnPageList(ReturnPageListRo record, int pageNum, int pageSize);

    /**
     * 拒绝退货
     */
    Ro rejectReturn(RejectReturnTo record);

    /**
     * 同意退货
     */
    Ro agreeReturn(AgreeReturnTo mo);

    /**
     * 同意退款
     */
    Ro agreeRefund(OrdOrderReturnTo to);

    /**
     * 已收到货并退款
     */
    Ro receivedAndRefunded(ReceivedAndRefundedTo to);

    /**
     * 查询用户退货中订单信息
     */
    List<Map<String, Object>> selectReturningInfo(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     * 查询用户退货完成订单信息
     *
     * @param mo
     */
    List<Map<String, Object>> selectReturnInfo(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     * 判断订单是否有订单详情在退货中(退货状态在待审核、退货中都算)
     */
    Boolean hasReturningInOrder(Long orderId);

    /**
     * 取消退货
     *
     * @param mo
     * @return
     */
    Ro cancelReturn(OrdReturnMo mo);
}
