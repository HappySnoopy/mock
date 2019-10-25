package net.loyintea.mock.serv.impl;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.bean.MockConfig;
import net.loyintea.mock.bean.MockInput;
import net.loyintea.mock.serv.MockConfigurator;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.ObjectContext;
import org.apache.commons.jexl3.internal.Engine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class MockConfiguratorImpl implements MockConfigurator {


    /**
     * 获取一个配置列表，后续可扩展为从数据库查
     *
     * @param queryParam
     * @return
     */
    private List<MockConfig> queryConfigList(MockConfig queryParam) {

        // 注意顺序
        List<MockConfig> configList = new ArrayList<>();
        MockConfig result;

        result = new MockConfig();
        result.setClientIp(queryParam.getClientIp());
        result.setUri(queryParam.getUri());
        // 这个表达式的配置需要注意，遵守JEXL3的规定
        result.setExpression("params.a=='[1]' && params.b=='[1,2]'");
        result.setHttpStatusCode(200);
        result.setResponseBody("{\"code\":\"0000\"}");
        result.setSort(-1f);
        configList.add(result);

        result = new MockConfig();
        result.setClientIp(queryParam.getClientIp());
        result.setUri(queryParam.getUri());
        result.setExpression("params.a=='[1]'");
        result.setHttpStatusCode(500);
        result.setResponseBody("{\"code\":\"1001\",\"message\":\"错误信息\"}");
        result.setSort(0f);
        configList.add(result);


        if (CollectionUtils.isEmpty(configList)) {
            throw new RuntimeException("Mock没有配置！");
        }
        return configList;
    }

    /**
     * 查出配置列表，按顺序，依次匹配，找到第一条配置数据，将其转换为输出数据
     *
     * @param input
     * @return
     */
    @Override
    public ResponseEntity<Object> config(MockInput input) {

        List<MockConfig> mockConfigs = this.queryConfigList(MockConfig.build4Query(input));

        MockConfig mockConfig = mockConfigs.stream().sorted()
                .filter(config -> isThisConfig(input, config)).findFirst()
                .orElseThrow(() -> new RuntimeException("无法匹配到合适的mock配置"));

        // 这里后续需要扩展：根据mockConfig中的Content-type来生成不同的ResponseEntity和body
        // 考虑把headers放进来
        ResponseEntity<Object> result = new ResponseEntity<>(mockConfig.getResponseBody(),
                HttpStatus.resolve(
                        mockConfig.getHttpStatusCode()));

        return result;
    }

    /**
     * 这个方法可以优化下，有些东西可以简化。可以通过测试类来测试
     */
    boolean isThisConfig(MockInput input, MockConfig config) {
        log.info("input:{}, config:{}", input, config);
        JexlEngine engine = new Engine();
        JexlExpression expression = engine.createExpression(config.getExpression());
        return (boolean) expression.evaluate(new ObjectContext<>(engine, input));
    }
}
