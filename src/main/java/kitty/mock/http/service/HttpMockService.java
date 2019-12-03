package kitty.mock.http.service;

import kitty.mock.http.bean.HttpMockConfig;

import java.util.List;

/**
 *
 */
public interface HttpMockService {

    /**
     * 获取mock配置
     *
     * @return 全套的http mock配置。有序。非null
     */
    List<HttpMockConfig> queryConfigList();
}
