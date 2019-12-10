package kitty.mock.http.biz.impl;

import kitty.mock.common.biz.impl.MockerAsSkeleton;
import kitty.mock.http.bean.HttpForwardConfig;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.service.HttpForwardService;
import kitty.mock.http.service.HttpMockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 提供一个Http的mocker
 * <p>
 * TODO 后续要处理上传/下载类的mock。
 */
@Service
@Slf4j
class HttpMockerImpl extends MockerAsSkeleton<RequestEntity<Object>, ResponseEntity<Object>> {

    @Resource
    private RestTemplate restTemplate;
    /** The Http mock service. */
    @Resource
    private HttpMockService httpMockService;
    /** The Http forward service. */
    @Resource
    private HttpForwardService httpForwardService;

    /**
     * 给Http做一个处理
     *
     * @param param http请求
     * @return http的响应数据
     */
    @Override
    protected ResponseEntity<Object> doMock(RequestEntity<Object> param) {
        log.info("Http mock. param:{}", param);

        // 如果没有mock配置，应当返回null
        ResponseEntity<Object> result = httpMockService.queryConfig(param).map(this::toResponse).orElse(null);

        log.info("Http mock. result:{}", result);
        return result;
    }

    /**
     * 把配置项转换为返回结果
     *
     * @param config http请求/响应的配置
     * @return 将配置数据转换为响应结果
     */
    private ResponseEntity<Object> toResponse(HttpMockConfig config) {
        // 这里后续需要扩展：根据mockConfig中的Content-type来生成不同的ResponseEntity和body
        // 考虑把headers放进来
        log.info("Http mock toResponse. config:{}", config);

        ResponseEntity<Object> result = new ResponseEntity<>(config.getResponseBody(),
                ObjectUtils.defaultIfNull(HttpStatus.resolve(config.getHttpStatusCode()),
                        HttpStatus.INTERNAL_SERVER_ERROR));
        log.info("Http mock toResponse. config:{}, result:{}", config, result);
        return result;
    }

    /**
     * 尝试解析转发配置，根据转发配置转发请求并获取返回结果
     * <p>
     * 如果没有转发配置，则返回null
     *
     * @param param the param
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> forward(RequestEntity<Object> param) {
        log.info("http forward. param:{}", param);
        ResponseEntity<Object> result = httpForwardService.queryConfig(param).map(config -> toResponse(config, param))
                .orElse(null);
        log.info("http forward. param:{}, result:{}", param, result);
        return result;
    }

    /**
     * 使用RestTemplate
     * <p>
     * 也可以使用feign
     *
     * 出现Content-Type不能解析时，请前往{@link kitty.mock.http.config.Configurations}去配置对应的解析类
     * @param config the config
     * @param param  the param
     * @return 转发服务的返回结果
     */
    private ResponseEntity<Object> toResponse(HttpForwardConfig config, RequestEntity<Object> param) {
        // request在这个方法里，只用了getBody/getHeader/getType
        return restTemplate
                .exchange(config.getForwardUrl() + "?" + param.getUrl().getQuery(), config.getHttpMethod(), param,
                        Object.class);
    }
}
