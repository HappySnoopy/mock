package kitty.mock.http.web;

import kitty.mock.common.biz.Mocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    private Mocker<RequestEntity<Object>, ResponseEntity<Object>> mocker;


    /**
     * 从request中把数据解析出来，交给biz去处理并生成一个响应结果
     * <p>
     * 不配置任何映射，意为处理所有请求、方法、参数等
     *入参大概是这个样子的
     * <pre>
     *  {
     *      "headers": {
     *          "sec-fetch-mode": ["navigate"],
     *          "accept-language": ["zh-CN,zh;q=0.9,en;q=0.8"],
     *          "sec-fetch-site": ["none"],
     *          "connection": ["keep-alive"],
     *          "upgrade-insecure-requests": ["1"],
     *          "accept": ["text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,* /*;q=0.8,application/signed-exchange;v=b3"],
     *          "host": ["localhost:1986"],
     *          "user-agent": ["Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36"],
     *          "sec-fetch-user": ["?1"],
     *          "accept-encoding": ["gzip, deflate, br"]
     *      },
     *      "body": null,
     *      "method": "GET",
     *      "url": "http://localhost:1986/test",
     *      "type": null
     *  }
     * </pre>
     *
     *
     * @param input 原始http请求，
     * @return http响应结果
     */
    @RequestMapping
    public ResponseEntity<Object> mock(RequestEntity<Object> input) {
        // 上传、下载、responseBody等，通过扩展这个工厂方法、并生成不同的MockInput来处理
        log.info("input:{}", input);

        ResponseEntity<Object> output = mocker.mock(input)
                .orElse(ResponseEntity.status(500).body("mock失败，请检查mock服务日志"));
        log.info("output:{}", output);

        return output;
    }

    @PostMapping("/test")
    public ResponseEntity<Object> test(String token, RequestEntity<Object> input) {
        log.info("token:{}, token in request:{}", token, input);
        return ResponseEntity.ok(input);
    }
}
