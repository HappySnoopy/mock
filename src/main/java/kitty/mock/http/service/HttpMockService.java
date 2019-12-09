package kitty.mock.http.service;

import kitty.mock.http.bean.HttpMockConfig;
import org.springframework.http.RequestEntity;

import java.util.Optional;

/**
 * DONE 需要调整这个接口
 * @author Snow White
 */
public interface HttpMockService {

    /**
     * 根据http的mock参数，找到对应的配置
     *
     * 后续可以改用SQL来处理
     *
     * @param param 入参
     * @return 与入参匹配的配置。有可能匹配不上，匹配不上时返回EMPTY
     */
    Optional<HttpMockConfig> queryConfig(RequestEntity<Object> param);
}
