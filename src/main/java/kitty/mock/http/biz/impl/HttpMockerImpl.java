package kitty.mock.http.biz.impl;

import kitty.mock.common.biz.impl.MockerAsSkeleton;
import kitty.mock.common.util.JsonUtils;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.bean.MockInput4Http;
import kitty.mock.http.service.HttpMockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提供一个Http的mocker
 *
 * TODO 后续要处理上传/下载类的mock。
 */
@Service
@Slf4j
class HttpMockerImpl extends MockerAsSkeleton<MockInput4Http, ResponseEntity<Object>> {


    @Resource
    private HttpMockService httpMockService;

    /**
     * 给Http做一个处理
     *
     * @param param http请求
     * @return http的响应数据
     */
    @Override
    protected ResponseEntity<Object> doMock(MockInput4Http param) {
        log.info("Http mock. param:{}", param);

        // TODO 如果没有mock配置，需要走转发、记录的逻辑。这个可以后续处理。
        ResponseEntity<Object> result = httpMockService.queryConfig(param).map(this::toResponse)
                .orElse(ResponseEntity.unprocessableEntity().build());

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

        log.info("body:{}", JsonUtils.fromJson(config.getResponseBody(), Object.class));

        return new ResponseEntity<>(JsonUtils.fromJson(config.getResponseBody(), Object.class),
                ObjectUtils.defaultIfNull(HttpStatus.resolve(
                        config.getHttpStatusCode()), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
