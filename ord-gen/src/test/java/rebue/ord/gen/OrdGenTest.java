package rebue.ord.gen;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import rebue.mbgx.MybatisGeneratorWrap;

/**  
* 创建时间：2018年4月8日 上午11:09:38  
* 项目名称：ord-gen  
* @author daniel  
* @version 1.0   
* @since JDK 1.8  
* 文件名称：OrdGenTest.java  
* 类说明：  自动生成Mybatis
*/
public class OrdGenTest {

	@Test
    public void test() throws IOException, SQLException, InterruptedException, XMLParserException, InvalidConfigurationException {
        MybatisGeneratorWrap.gen(true, "conf/mbg-ord.properties");
    }
}
  

