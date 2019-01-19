package rebue.ord.svr.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import rebue.ord.mo.OrdGoodsBuyRelationMo;
import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "ord-svr", configuration = FeignConfig.class)
public interface OrdGoodsBuyRelationSvc {
	
    /**
		查询某个商品购买关系是否存在
     */
    @GetMapping("/ord/goodsbuyrelation/listExistRelation")
    List<OrdGoodsBuyRelationMo> ListExistRelation(final OrdGoodsBuyRelationMo mo);

}
