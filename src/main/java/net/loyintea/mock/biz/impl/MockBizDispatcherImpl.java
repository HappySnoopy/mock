package net.loyintea.mock.biz.impl;

import net.loyintea.mock.bean.MockInput;
import net.loyintea.mock.biz.MockBiz;
import net.loyintea.mock.serv.MockConfigurator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * mock业务的分发类，指明子类
 */
@Service("mockBiz")
class MockBizDispatcherImpl implements MockBiz {

    /**
     * mock配置服务
     */
    @Resource
    private MockConfigurator configurator;

    /**
     * 可以在这里根据input中的不同数据（如不同的Content-Type），把请求交给不同的配置服务来处理）
     *
     * @param input 入参
     * @return 返回结果
     */
    @Override
    public ResponseEntity<Object> mock(MockInput input) {

        // 看看是在这里来根据input中的content-type做分发处理，还是在configurator中做分发
        ResponseEntity<Object> result = configurator.config(input);

        return result;
    }
}
