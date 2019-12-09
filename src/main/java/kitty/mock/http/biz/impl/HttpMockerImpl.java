package kitty.mock.http.biz.impl;

import kitty.mock.common.biz.impl.MockerAsSkeleton;
import kitty.mock.common.util.JsonUtils;
import kitty.mock.http.bean.HttpForwardConfig;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.service.HttpForwardService;
import kitty.mock.http.service.HttpMockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;

/**
 * 提供一个Http的mocker
 * <p>
 * TODO 后续要处理上传/下载类的mock。
 */
@Service
@Slf4j
class HttpMockerImpl extends MockerAsSkeleton<RequestEntity<Object>, ResponseEntity<Object>> {

    /** 加号，编码前的明文 */
    private static final String SYMBOL_PLUS = "+";
    /** 加号，编码后的字符串 */
    private static final String SYMBOL_PLUS_ENCODED = "%2B";
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

        ResponseEntity<Object> result = new ResponseEntity<>(JsonUtils.fromJson(config.getResponseBody(), Object.class),
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
     * @param config the config
     * @param param  the param
     * @return the response entity
     */
    private ResponseEntity<Object> toResponse(HttpForwardConfig config, RequestEntity<Object> param) {

       /* StringBuilder urlBuilder = new StringBuilder(1024).append(config.getForwardUrl()).append('/')
                .append(param.getUrl().getRawPath()).append('?').append(param.getUrl().getQuery());



        while (urlBuilder.indexOf(SYMBOL_PLUS) > -1) {
            *//*
                Url中的+会被编码/解码为空格。需要做编码处理
                目前看其他字符不需要处理
                参见：https://muchstudy.com/2017/12/06/%E5%AD%97%E7%AC%A6%E8%A7%A3%E7%A0%81%E6%97%B6%E5%8A%A0%E5%8F%B7%E8%A7%A3%E7%A0%81%E4%B8%BA%E7%A9%BA%E6%A0%BC%E9%97%AE%E9%A2%98%E6%8E%A2%E7%A9%B6/
             *//*
            int index = urlBuilder.indexOf(SYMBOL_PLUS);
            urlBuilder.replace(index, index + 1, SYMBOL_PLUS_ENCODED);
        }

        RequestEntity request = new RequestEntity(param.getBody(), param.getHeaders(), config.getHttpMethod(),
                URI.create(urlBuilder.toString()), param.getType());

        log.info("url:{}", urlBuilder);

        return restTemplate.exchange(urlBuilder.toString(), config.getHttpMethod(), request, Object.class);*/

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.add("Cache-Control", "no-cache");
        headers.add("Content-Type", "application/json;charset=utf-8");


        RequestEntity request = new RequestEntity(null, headers, HttpMethod.POST,
                URI.create("http://localhost:8081/yrdas-master//user/flowStatusRequest"));

        StringBuilder urlBuilder = new StringBuilder(1024)
                .append("http://localhost:1986/test?" + "system=eyJhbmRyb2lkSU1FSSI6Ijg2NjE5NzAzNTA0MDYxNCIsImFuZHJvaWRJZCI6ImY4OWU3MWQ2MTg3ODA5OGYiLCJhbmRyb2lkTWFjIjoiMzg6NmU6YTI6ZjQ6OGQ6YjUiLCJhcHBWZXJzaW9uQ29kZSI6MjUxMCwiYXBwVmVyc2lvbk5hbWUiOiI1LjkuNCIsImFwcGxpY2F0aW9uSWQiOiJjb20ueWlyZW5kYWkiLCJnb29nbGVsYXQiOiIzOS45MDk5MDk5MDk5MDk5MDYiLCJnb29nbGVsbmciOiIxMTYuNDYyODcwNDYyNjkzNTkiLCJpc0VtdWxhdG9yIjoiMCIsImxhdCI6IjM5LjkxMzIzNCIsImxuZyI6IjExNi40NzcxMyIsInBpZCI6IjMwMDUzIiwic2luZ2xlQ29kZSI6Ijg2NjE5NzAzNTA0MDYxNCIsInN5c3RlbU1vZGVsIjoidml2byBYMjBQbHVzIEEiLCJzeXN0ZW1QaG9uZSI6ImFuZHJvaWQiLCJzeXN0ZW1WZXJzaW9uIjoiOC4xLjAiLCJ0ZENoYW5uZWxJRCI6InQiLCJ0ZElEIjoiMzQ5OWY3MTBlMWU3ZTUzMDQyYmMwYzAyZjQ4NTlkYTc3IiwidWlkIjoiMTA0MjgifQ==" + "&json=RS5pe8JkkRhlvmnl/DIUxB+l42k4m2PQwm7Q7FkiHCY=" + "&token=eutc5GH3OITCSQxBlC3iGUdvZnf2zgeBtj8LJ7ufoYK4Gu8+Gt8cbwN6i5+dU2TKXSOIPDjIz075W5HamKU8XG+JcOIlHVmukkuLpTPBqFAQ05tbm64dLyTwb5d4CuX6dfe86793d819b65da2c702f9d7c8bf75" + "&ucode=AAZXMQNiAWQHaFBmV2QIZgc2UjBSYFY1CzULPV1tVTULZAJn");
        // while (urlBuilder.indexOf(SYMBOL_PLUS) > -1) {
        //     /* TODO 这个编码/解码有点问题：传“+”，服务端会解析为空格；传“%2B”，服务端不会解析为加号
        //     Url中的+会被编码/解码为空格。需要做编码处理
        //             目前看其他字符不需要处理
        //     参见：https://muchstudy.com/2017/12/06/%E5%AD%97%E7%AC%A6%E8%A7%A3%E7%A0%81%E6%97%B6%E5%8A%A0%E5%8F%B7%E8%A7%A3%E7%A0%81%E4%B8%BA%E7%A9%BA%E6%A0%BC%E9%97%AE%E9%A2%98%E6%8E%A2%E7%A9%B6/
        //      */
        //     int index = urlBuilder.indexOf(SYMBOL_PLUS);
        //     urlBuilder.replace(index, index + 1, SYMBOL_PLUS_ENCODED);
        // }

        log.info("param:{}, request:{}, url:{}", param, request, urlBuilder);

        // request在这个方法里，只用了getBody/getHeader/getType
        // TODO 这里可能有问题，各种content-type转不了Object
        return restTemplate.exchange(urlBuilder.toString(), HttpMethod.POST, request, Object.class);
    }
}
