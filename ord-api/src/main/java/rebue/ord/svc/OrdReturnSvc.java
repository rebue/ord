package rebue.ord.svc;

import com.github.pagehelper.PageInfo;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.AddReturnRo;
import rebue.ord.ro.AgreeToARefundRo;
import rebue.ord.ro.AgreeToReturnRo;
import rebue.ord.ro.OrdReturnRo;
import rebue.ord.ro.ReceivedAndRefundedRo;
import rebue.ord.ro.RejectReturnRo;
import rebue.ord.to.OrdOrderReturnTo;
import rebue.robotech.svc.MybatisBaseSvc;

/**
 * 用户退货信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface OrdReturnSvc extends MybatisBaseSvc<OrdReturnMo, java.lang.Long> {

    /**
     *  添加用户退货信息 Title: addEx Description:
     *
     *  @param to
     *  @return
     *  @date 2018年4月19日 下午2:53:00
     */
    AddReturnRo addReturn(OrdOrderReturnTo to);

    /**
     *  查询分页列表信息 Title: selectReturnPageList Description:
     *
     *  @param record
     *  @return
     *  @date 2018年4月21日 下午3:35:27
     */
    PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnRo record, int pageNum, int pageSize);

    /**
     *  拒绝退货 Title: rejectReturn Description:
     *
     *  @param record
     *  @return
     *  @date 2018年4月27日 下午3:10:47
     */
    RejectReturnRo rejectReturn(OrdReturnRo record);

    /**
     *  同意退货 Title: agreeToReturn Description:
     *
     *  @param mo
     *  @return
     *  @date 2018年5月11日 下午2:45:13
     */
    AgreeToReturnRo agreeToReturn(OrdOrderReturnTo mo);

    /**
     *  同意退款 Title: agreeToARefund Description:
     *
     *  @param to
     *  @return
     *  @date 2018年5月11日 下午2:45:36
     */
    AgreeToARefundRo agreeToARefund(OrdOrderReturnTo to);

    /**
     *  已收到货并退款 Title: receivedAndRefunded Description:
     *
     *  @param to
     *  @return
     *  @date 2018年5月11日 下午3:02:40
     */
    ReceivedAndRefundedRo receivedAndRefunded(OrdOrderReturnTo to);

    /**
     *  查询用户退货中订单信息 Title: selectOrderInfo Description:
     *  @param mo
     */
    List<Map<String, Object>> selectReturningInfo(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     *  查询用户退货完成订单信息 Title: selectOrderInfo Description:
     *  @param mo
     */
    List<Map<String, Object>> selectReturnInfo(Map<String, Object> map) throws ParseException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
