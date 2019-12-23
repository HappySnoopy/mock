package kitty.mock.http.service.impl;

import com.google.gson.reflect.TypeToken;
import kitty.mock.http.bean.HttpForwardConfig;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BaseConfigSupportImplTest {


    private BaseConfigSupportImpl<HttpForwardConfig> support = new BaseConfigSupportImpl<HttpForwardConfig>(
            new TypeToken<List<HttpForwardConfig>>() {
            }.getType(), "classpath:HttpForwardConfig_test.json") {
    };

    @Test
    public void test() {
        support.initConfigList();
        assertEquals(2, support.configList.size());

        HttpForwardConfig config;
        config = support.configList.get(0);
        assertEquals("/testparam", config.getOriginUri());
        assertEquals("http://localhost:8081", config.getForwardUrl());


        config = support.configList.get(1);
        assertEquals("/test/forward", config.getOriginUri());
        assertEquals("http://localhost:8081", config.getForwardUrl());
    }
}