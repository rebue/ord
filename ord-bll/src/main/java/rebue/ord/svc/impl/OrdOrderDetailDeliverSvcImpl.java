package rebue.ord.svc.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rebue.kdi.ro.KdiLogisticRo;
import rebue.kdi.svr.feign.KdiSvc;
import rebue.ord.mapper.OrdOrderDetailDeliverMapper;
import rebue.ord.mo.OrdOrderDetailDeliverMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.OrderdetaildeliverRo;
import rebue.ord.svc.OrdOrderDetailDeliverSvc;
import rebue.ord.svc.OrdOrderDetailSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 订单详情发货信息
 *
 * 在单独使用不带任何参数的 @Transactional 注释时，
 * propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED，
 * 而且事务不会针对受控异常（checked exception）回滚。
 *
 * 注意：
 * 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class OrdOrderDetailDeliverSvcImpl extends MybatisBaseSvcImpl<OrdOrderDetailDeliverMo, java.lang.Long, OrdOrderDetailDeliverMapper> implements OrdOrderDetailDeliverSvc {
	
	
	@Resource
	private OrdOrderDetailSvc ordOrderDetailSvc;
	
	
	@Resource
	private KdiSvc kdiSvc;
    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
	private static final Logger _log = LoggerFactory.getLogger(OrdOrderDetailDeliverSvcImpl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(OrdOrderDetailDeliverMo mo) {
    	_log.info("添加订单详情发货信息");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }
    
	@Override
	public List<OrderdetaildeliverRo> listOrderdetaildeliver(Long orderId) {
		List<OrderdetaildeliverRo> ListDeliverRo=new ArrayList<OrderdetaildeliverRo>();

		OrdOrderDetailDeliverMo mo=new OrdOrderDetailDeliverMo();
		mo.setOrderId(orderId);
    	_log.info("根据订单id获取订单详情发货信息的参数是： {}",orderId);
    	List<OrdOrderDetailDeliverMo> deliverMo=super.list(mo);
    	_log.info("根据订单id获取订单详情发货信息的结果是： {}",deliverMo);
    	for (OrdOrderDetailDeliverMo ordOrderDetailDeliverMo : deliverMo) {
    		OrderdetaildeliverRo deliverRo=new OrderdetaildeliverRo();
    		deliverRo.setId(ordOrderDetailDeliverMo.getId());
        	_log.info("循环获取订单详情和物流信息开始：--------------------------");
        	_log.info("根据详情id订单详情的参数是： {}",ordOrderDetailDeliverMo.getOrderDetailId());
        	OrdOrderDetailMo detailMo=ordOrderDetailSvc.getById(ordOrderDetailDeliverMo.getOrderDetailId());
        	_log.info("根据详情id订单详情的结果是： {}",detailMo);
        	if(detailMo !=null) {
        		deliverRo.setOnlineTitle(detailMo.getOnlineTitle());
        	}
        	_log.info("根据物流id获取物流单号的参数是： {}",ordOrderDetailDeliverMo.getLogisticId());
			KdiLogisticRo logisticRo = kdiSvc.getLogisticById(ordOrderDetailDeliverMo.getLogisticId());
        	_log.info("根据物流id获取物流单号的结果是： {}",logisticRo);
        	if(logisticRo !=null) {
        		deliverRo.setLogisticCode(logisticRo.getRecord().getLogisticCode());
        	}
        	
        	_log.info("循环获取订单详情和物流信息结束：++++++++++++++++++++++++++");
        	ListDeliverRo.add(deliverRo);
		}

		return ListDeliverRo;
	}

}
