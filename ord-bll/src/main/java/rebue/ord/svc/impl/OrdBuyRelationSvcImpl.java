package rebue.ord.svc.impl;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rebue.ord.mapper.OrdBuyRelationMapper;
import rebue.ord.mo.OrdBuyRelationMo;
import rebue.ord.mo.OrdOrderDetailMo;
import rebue.ord.ro.DetailandBuyRelationRo;
import rebue.ord.svc.OrdBuyRelationSvc;
import rebue.robotech.svc.impl.MybatisBaseSvcImpl;

/**
 * 订单购买关系
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
public class OrdBuyRelationSvcImpl extends MybatisBaseSvcImpl<OrdBuyRelationMo, java.lang.Long, OrdBuyRelationMapper> implements OrdBuyRelationSvc {
	
    
    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final Logger _log = LoggerFactory.getLogger(OrdBuyRelationSvcImpl.class);

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(OrdBuyRelationMo mo) {
        _log.info("添加订单购买关系");
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId() == 0) {
            mo.setId(_idWorker.getId());
        }
        return super.add(mo);
    }

    @Override
    public int updateByUplineOrderDetailId(OrdBuyRelationMo mo) {
        return _mapper.updateByUplineOrderDetailId(mo);
    }
    
    /**
     * 根据orderId或许购买关系
     */
	@Override
	public List<DetailandBuyRelationRo> getBuyRelationByOrderId(long orderId) {
		List<DetailandBuyRelationRo> result=new ArrayList<DetailandBuyRelationRo>();
		
        _log.info("根据orderId获取购买关系参数为： {}",orderId);
        _log.info("先查询订单详情参数为： {}",orderId);
        //查询回来的订单详情列表
        List<OrdOrderDetailMo>  detailList= _mapper.getDetailByOrderId(orderId);
        _log.info("查询订单详情的返回值： {}",detailList);
        //根据订单详情列表中的id去获取购买关系
        OrdBuyRelationMo mo=new OrdBuyRelationMo();
        for (int i = 0; i < detailList.size(); i++) {
        	//映射当前详情的所有字段
        	DetailandBuyRelationRo item= new DetailandBuyRelationRo();
        	item.setId(detailList.get(i).getId());
        	item.setOrderId(detailList.get(i).getOrderId());
        	item.setProductId(detailList.get(i).getProductId());
        	item.setOnlineTitle(detailList.get(i).getOnlineTitle());
        	item.setSpecName(detailList.get(i).getSpecName());
        	item.setBuyCount(detailList.get(i).getBuyCount());
        	item.setBuyPrice(detailList.get(i).getBuyPrice());
        	item.setCashbackAmount(detailList.get(i).getCashbackAmount());;
        	item.setBuyUnit(detailList.get(i).getBuyUnit());
        	item.setReturnCount(detailList.get(i).getReturnCount());
        	item.setReturnState(detailList.get(i).getReturnState());
        	item.setCashbackCommissionSlot(detailList.get(i).getCommissionSlot());
        	item.setCashbackCommissionState(detailList.get(i).getCommissionState());
        	item.setSubjectType(detailList.get(i).getSubjectType());
        	mo.setUplineOrderDetailId(detailList.get(i).getId());
            _log.info("查询购买关系的参数为： {}",mo);
            //已经获取到第一条订单详情的所有购买关系
        	List<OrdBuyRelationMo> list = super.list(mo);
            _log.info("查询购买关系的结果为： {}",list);
        	for (int j = 0; j < list.size(); j++) {
        		//先根据当前条购买关系去查询上家名字
        		if(j==1) {
            		Long dId=list.get(j).getDownlineUserId();
            		Long DetailId=list.get(j).getDownlineOrderDetailId();
            		//当前条购买关系的第二个下家名字和购买关系
                    _log.info("开始获取第二个下家名字id为： {}",dId);
            		DetailandBuyRelationRo dUserName2=_mapper.getDownlineUserName(dId);
                    _log.info("获取第二个下家的结果为： {}",dUserName2.getDownlineUserName());
                    _log.info("开始获取第二个下家订单上线标题DetailId为： {}",DetailId);
                    DetailandBuyRelationRo downlineOrdDetail=_mapper.getDownlineOrdDetail(DetailId);
                    _log.info("开始获取第二个下家订单上线标题结果： {}",downlineOrdDetail);
                    item.setDownOnlineTitle2(downlineOrdDetail.getDownOnlineTitle());
            		item.setDownlineUserName2(dUserName2.getDownlineUserName());
            		item.setRelationSource2(list.get(j).getRelationSource());
        		}else {
            		Long uId=list.get(j).getUplineUserId();
            		Long dId=list.get(j).getDownlineUserId();
            		Long DetailId=list.get(j).getDownlineOrderDetailId();
            		//当前条购买关系的上家名字
                    _log.info("开始获取第一个上家名字id为： {}",uId);
            		DetailandBuyRelationRo uUserName=_mapper.getUplineUserName(uId);
                    _log.info("获取第一上家的结果为： {}",uUserName.getUplineUserName());
            		//当前条购买关系的下家名字
                    _log.info("开始获取第一个下家名字id为： {}",dId);
            		DetailandBuyRelationRo dUserName=_mapper.getDownlineUserName(dId);
                    _log.info("获取第一个下家的结果为： {}",dUserName.getDownlineUserName());
                    _log.info("开始获取第一个下家订单上线标题DetailId为： {}",DetailId);
                    DetailandBuyRelationRo downlineOrdDetail=_mapper.getDownlineOrdDetail(DetailId);
                    _log.info("开始获取第一个下家订单上线标题结果： {}",downlineOrdDetail);
                    item.setDownOnlineTitle(downlineOrdDetail.getDownOnlineTitle());
            		item.setUplineUserName(uUserName.getUplineUserName());
            		item.setDownlineUserName(dUserName.getDownlineUserName());
            		item.setRelationSource(list.get(j).getRelationSource());
            		
        		}
        		
			}
        	result.add(item);
		}
        return result;
		
	}
}
