package net.loyintea.mock.http.service.impl;

import net.loyintea.mock.http.bean.MockHttpConfig;
import net.loyintea.mock.http.service.HttpMockService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("httpMockService")
public class HttpMockServiceImpl implements HttpMockService {
    @Override
    public List<MockHttpConfig> queryConfigList() {
        // 注意顺序
        List<MockHttpConfig> configList = new ArrayList<>();
        MockHttpConfig result;

        result = new
                MockHttpConfig();
        result.setClientIp("localhost");
        result.setUri("/test");
        // 这个表达式的配置需要注意，遵守JEXL3的规定
        result.setExpression("params.a=='[1]' && params.b=='[1, 2]'");
        result.setHttpStatusCode(200);
        result.setResponseBody("{\"code\":\"0000\"}");
        result.setSort(-1f);
        configList.add(result);

        result = new
                MockHttpConfig();
        result.setClientIp("localhost");
        result.setUri("/test");
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
}
