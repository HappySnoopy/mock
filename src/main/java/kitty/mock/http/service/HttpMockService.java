package kitty.mock.http.service;

import kitty.mock.http.bean.HttpMockConfig;

import java.util.List;

/**
 * TODO 需要调整这个接口
 */
public interface HttpMockService {

    /**
     * 获取mock配置
     *
     * @return 全套的http mock配置。有序。非null
     */
    List<HttpMockConfig> queryConfigList();
}
