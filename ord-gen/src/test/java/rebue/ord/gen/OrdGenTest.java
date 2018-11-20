package rebue.ord.gen;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import rebue.mbgx.MybatisGeneratorWrap;

/**
 * 自动生成Mybatis
 */
public class OrdGenTest {

    @Test
    public void test() throws IOException, SQLException, InterruptedException, XMLParserException, InvalidConfigurationException {
        MybatisGeneratorWrap.gen(true, "conf/mbg-ord.properties");
    }
}
