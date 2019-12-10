package kitty.mock.http.service.impl;

import kitty.mock.http.bean.HttpForwardConfig;
import kitty.mock.http.service.HttpForwardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 查询http配置
 *
 * @author Jerry
 * @date 2019-12-09
 */
@Service
@Slf4j
class HttpForwardServiceImpl implements HttpForwardService {
    /**
     * Query config optional.
     *
     * @param param the param
     * @return the optional
     */
    @Override
    public Optional<HttpForwardConfig> queryConfig(RequestEntity<Object> param) {
        // test TODO
        HttpForwardConfig config = new HttpForwardConfig();
        config.setForwardUrl("http://localhost:1986/test/");
        config.setHttpMethod(HttpMethod.POST);

        log.info("param:{}, config:{}", param, config);
        return Optional.of(config);
    }
}
