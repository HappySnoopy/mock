package kitty.mock.http.service;

import kitty.mock.http.bean.HttpForwardConfig;
import org.springframework.http.RequestEntity;

import java.util.Optional;

/**
 * Http请求的转发配置
 *
 * @author Jerry
 * @date 2019-12-09
 */
public interface HttpForwardService {

    /**
     * 查询转发配置
     *
     * @param param http mock的入参
     * @return 转发配置。
     */
    Optional<HttpForwardConfig> queryConfig(RequestEntity<Object> param);
}
