package net.loyintea.mock.http.biz.impl;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.common.biz.impl.MockerAsSkeleton;
import net.loyintea.mock.common.util.JexlUtils;
import net.loyintea.mock.common.util.JsonUtils;
import net.loyintea.mock.http.bean.MockHttpConfig;
import net.loyintea.mock.http.bean.MockInput4Http;
import net.loyintea.mock.http.service.HttpMockService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

        /* 先获取完整的一套Mock配置(List)
         然后再从中匹配第一个配置。只取第一个
         把第一个配置转换为ResponseEntity。*/
        ResponseEntity<Object> result = httpMockService.queryConfigList().stream().filter(config -> isConfigMatched(config, param))
                .findFirst().map(this::toResponse).orElse(ResponseEntity.unprocessableEntity().build());

        log.info("Http mock. result:{}", result);
        return result;
    }

    /**
     * 判断当前请求是否能匹配对应的配置项
     *
     * @param config http请求/响应的配置
     * @param param  http请求
     * @return 本次http请求是否符合指定config的配置。符合，则返回true；否则返回false
     */
    private boolean isConfigMatched(MockHttpConfig config, MockInput4Http param) {
        // 首先，uri和method要相同
        // 其次，检查表达式是否匹配
        return StringUtils.equals(config.getUri(), param.getUri())
                && config.getMethod() == param.getMethod() && JexlUtils.isMatched(config.getExpression(), param);
    }

    /**
     * 把配置项转换为返回结果
     *
     * @param config http请求/响应的配置
     * @return 将配置数据转换为响应结果
     */
    private ResponseEntity<Object> toResponse(MockHttpConfig config) {
        // 这里后续需要扩展：根据mockConfig中的Content-type来生成不同的ResponseEntity和body
        // 考虑把headers放进来
        return new ResponseEntity<>(JsonUtils.fromJson(config.getResponseBody()),
                ObjectUtils.defaultIfNull(HttpStatus.resolve(
                        config.getHttpStatusCode()), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
