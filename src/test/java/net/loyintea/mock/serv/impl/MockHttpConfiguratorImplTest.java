package net.loyintea.mock.serv.impl;

import net.loyintea.mock.http.bean.MockHttpConfig;
import net.loyintea.mock.http.bean.MockInput4Http;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MockHttpConfiguratorImplTest {

    private MockConfiguratorImpl configurator ;

    @Before
    public void setup(){
        configurator = new MockConfiguratorImpl();
    }

    @Test
    public void testExpression(){
        MockInput4Http input = new MockInput4Http();
        Map<String, String> params = new HashMap<>();
        params.put("a","[1]");
        input.setParams(params);

        MockHttpConfig config = new MockHttpConfig();
        config.setExpression("params.a=='[1]'");
       boolean result =  configurator.isThisConfig(input,config);
       assertTrue(result);
    }
}