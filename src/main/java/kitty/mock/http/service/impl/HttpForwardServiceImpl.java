package kitty.mock.http.service.impl;

import com.google.gson.reflect.TypeToken;
import kitty.mock.http.bean.HttpForwardConfig;
import kitty.mock.http.service.HttpForwardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 查询http配置
 *
 * @author Jerry
 * @date 2019-12-09
 */
@Service
@Slf4j
class HttpForwardServiceImpl extends BaseConfigSupportImpl<HttpForwardConfig> implements HttpForwardService {

    /**
     * 构造函数注入
     *
     * @param location the location
     */
    public HttpForwardServiceImpl(@Value("${mock.http.config.forward.location}") String location) {
        super(new TypeToken<List<HttpForwardConfig>>() {
        }.getType(), location);
    }

    /**
     * 从 #configList 中遍历找出配置了对应path的配置项
     *
     * @param param the param
     * @return the optional
     */
    @Override
    public Optional<HttpForwardConfig> queryConfig(RequestEntity<Object> param) {
        Optional<HttpForwardConfig> config = configList.stream()
                .filter(c -> c.getOriginUri().equals(param.getUrl().getPath())).findFirst();
        log.info("param:{}, config:{}", param, config);
        return config;
    }

}
