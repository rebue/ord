package rebue.ord.svc;

import rebue.robotech.svc.MybatisBaseSvc;
import rebue.ord.mo.OrdReturnMo;
import rebue.ord.ro.OrdReturnRo;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import rebue.ord.to.OrdOrderReturnTo;

public interface OrdReturnSvc extends MybatisBaseSvc<OrdReturnMo, java.lang.Long> {

	/**
	 * 添加用户退货信息 Title: addEx Description:
	 * 
	 * @param to
	 * @return
	 * @date 2018年4月19日 下午2:53:00
	 */
	Map<String, Object> addEx(OrdOrderReturnTo to);

	/**
     * 查询分页列表信息
     * Title: selectReturnPageList
     * Description: 
     * @param record
     * @return
     * @date 2018年4月21日 下午3:35:27
     */
    PageInfo<OrdReturnRo> selectReturnPageList(OrdReturnMo record, int pageNum, int pageSize);
    
    /**
     * 拒绝退货
     * Title: rejectReturn
     * Description: 
     * @param record
     * @return
     * @date 2018年4月27日 下午3:10:47
     */
	Map<String, Object> rejectReturn(OrdReturnMo record);
}