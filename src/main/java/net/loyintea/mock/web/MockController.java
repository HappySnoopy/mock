package net.loyintea.mock.web;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.bean.MockInput;
import net.loyintea.mock.biz.MockBiz;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 入口
 * <p>
 * 匹配所有请求
 */
@RestController
@RequestMapping("/**")
@Slf4j
public class MockController {

    /**
     * mock处理器
     */
    @Resource
    private MockBiz mockBiz;

    /**
     * 从request中把数据解析出来，交给biz去处理并生成一个响应结果
     * <p>
     *
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> mock(HttpServletRequest request) {
        return doMock(request);
    }

    private ResponseEntity<Object> doMock(HttpServletRequest request) {

        // 上传、下载、responseBody等，通过扩展这个工厂方法、并生成不同的MockInput来处理
        MockInput input = MockInput.parse(request);
        log.info("input:{}", input);

        ResponseEntity<Object> output = mockBiz.mock(input);
        log.info("output:{}", output);

        return output;
    }
}
