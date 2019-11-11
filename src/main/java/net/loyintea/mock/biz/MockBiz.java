package net.loyintea.mock.biz;

import net.loyintea.mock.http.bean.MockInput4Http;
import org.springframework.http.ResponseEntity;

/**
 * mock主业务
 */
public interface MockBiz {

    /**
     * 将input转换为http响应的数据
     *
     * @param input 请求数据
     * @return 响应结果
     */
    ResponseEntity<Object> mock(MockInput4Http input);
}
