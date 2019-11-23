package net.loyintea.mock.http.web;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.common.biz.Mocker;
import net.loyintea.mock.http.bean.MockInput4Http;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static net.loyintea.mock.http.utils.HttpMockUtils.parse;

/**
 * 入口
 * <p>
 * 匹配所有请求
 */
@RestController
@RequestMapping("/**")
@Slf4j
public class HttpMockController {

    /**
     * mock处理器
     */
    @Resource(name = "httpMockerImpl")
    private Mocker<MockInput4Http, ResponseEntity<Object>> mocker;

    /**
     * 从request中把数据解析出来，交给biz去处理并生成一个响应结果
     * <p>
     * 不配置任何映射，意为处理所有请求、方法、参数等
     *
     * @param request 原始http请求
     * @return http响应结果
     */
    @RequestMapping
    public ResponseEntity<Object> mock(HttpServletRequest request) {
        // 上传、下载、responseBody等，通过扩展这个工厂方法、并生成不同的MockInput来处理
        MockInput4Http input = parse(request);
        log.info("input:{}", input);


        Optional<ResponseEntity<Object>> mockResult = mocker.mock(input);

        ResponseEntity<Object> output = mockResult.orElse(ResponseEntity.status(500).build());
        log.info("output:{}", output);

        return output;
    }
}
