package net.loyintea.mock.http.service;

import net.loyintea.mock.http.bean.MockHttpConfig;

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
    List<MockHttpConfig> queryConfigList();
}
