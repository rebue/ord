package rebue.ord.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import rebue.ord.mo.OrdReturnPicMo;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.IdRo;
import rebue.robotech.ro.Ro;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;

/**
 * 退货图片
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public class OrdReturnPicTests {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final ObjectMapper _objectMapper = new ObjectMapper();

    /**
     *  测试基本的增删改查
     *
     *  @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Test
    public void testCrud() throws IOException, ReflectiveOperationException {
        OrdReturnPicMo mo = null;
        for (int i = 0; i < 20; i++) {
            mo = (OrdReturnPicMo) RandomEx.randomPojo(OrdReturnPicMo.class);
            mo.setId(null);
            System.out.println("添加退货图片的参数为：" + mo);
            final String addResult = OkhttpUtils.postByJsonParams(hostUrl + "/ord/returnpic", mo);
            System.out.println("添加退货图片的返回值为：" + addResult);
            final IdRo idRo = _objectMapper.readValue(addResult, IdRo.class);
            System.out.println(idRo);
            Assert.assertEquals(ResultDic.SUCCESS, idRo.getResult());
            mo.setId(Long.valueOf(idRo.getId()));
        }
        final String listResult = OkhttpUtils.get(hostUrl + "/ord/returnpic?pageNum=1&pageSize=5");
        System.out.println("查询退货图片的返回值为：" + listResult);
        System.out.println("获取单个退货图片的参数为：" + mo.getId());
        final String getByIdResult = OkhttpUtils.get(hostUrl + "/ord/returnpic/getbyid?id=" + mo.getId());
        System.out.println("获取单个退货图片的返回值为：" + getByIdResult);
        System.out.println("修改退货图片的参数为：" + mo);
        final String modifyResult = OkhttpUtils.putByJsonParams(hostUrl + "/ord/returnpic", mo);
        System.out.println("修改积分日志类型的返回值为：" + modifyResult);
        final Ro modifyRo = _objectMapper.readValue(modifyResult, Ro.class);
        System.out.println(modifyRo);
        Assert.assertEquals(ResultDic.SUCCESS, modifyRo.getResult());
        System.out.println("删除退货图片的参数为：" + mo.getId());
        final String deleteResult = OkhttpUtils.delete(hostUrl + "/ord/returnpic?id=" + mo.getId());
        System.out.println("删除退货图片的返回值为：" + deleteResult);
        final Ro deleteRo = _objectMapper.readValue(deleteResult, Ro.class);
        System.out.println(deleteRo);
        Assert.assertEquals(ResultDic.SUCCESS, deleteRo.getResult());
    }

    private final String hostUrl = "http://127.0.0.1:9009";
}
