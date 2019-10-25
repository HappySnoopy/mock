package net.loyintea.mock.serv.impl;

import net.loyintea.mock.bean.MockConfig;
import net.loyintea.mock.bean.MockInput;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MockConfiguratorImplTest {

    private MockConfiguratorImpl configurator ;

    @Before
    public void setup(){
        configurator = new MockConfiguratorImpl();
    }

    @Test
    public void testExpression(){
        MockInput input = new MockInput();
        Map<String, String> params = new HashMap<>();
        params.put("a","[1]");
        input.setParams(params);

        MockConfig config = new MockConfig();
        config.setExpression("params.a=='[1]'");
       boolean result =  configurator.isThisConfig(input,config);
       assertTrue(result);
    }
}