package kitty.mock.http.service.impl;

import com.google.gson.reflect.TypeToken;
import kitty.mock.common.util.JexlUtils;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.service.HttpMockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Http Mock的配置服务
 * <p>
 * 改用RequestEntity之后，这里的处理也需要修改
 */
@Service("httpMockService")
@Slf4j
class HttpMockServiceImpl extends BaseConfigSupportImpl<HttpMockConfig> implements HttpMockService {

    /**
     * 构造函数注入
     *
     * @param location the location
     */
    public HttpMockServiceImpl(@Value("${mock.http.config.mock.location}") String location) {
        super(new TypeToken<List<HttpMockConfig>>() {
        }.getType(), location);
    }

    /**
     * 查找配置列表中是否有与入参匹配的配置项
     *
     * @param req 用户发起的http请求
     * @return 与req相匹配的mock配置
     */
    @Override
    public Optional<HttpMockConfig> queryConfig(RequestEntity<Object> req) {
        return configList.stream().filter(config -> isConfigMatched(config, req)).findFirst();
    }

    /**
     * 判断当前请求是否能匹配对应的配置项
     * <p>
     * DONE requestEntity中的QueryString格式有点问题：使用自定义的param/header
     *
     * @param config http请求/响应的配置
     * @param req    http请求
     * @return 本次http请求是否符合指定config的配置。符合，则返回true；否则返回false
     */
    private boolean isConfigMatched(HttpMockConfig config, RequestEntity<Object> req) {
        // 首先，uri和method要相同
        // 其次，检查表达式是否匹配
        boolean isMatched = StringUtils.equals(config.getUri(), req.getUrl().getPath()) && config.getMethod() == req
                .getMethod() && JexlUtils.isMatched(config.getExpression(), req);

        log.info("config:{}, param:{}, isMatched:{}", config, req, isMatched);

        return isMatched;
    }
}
