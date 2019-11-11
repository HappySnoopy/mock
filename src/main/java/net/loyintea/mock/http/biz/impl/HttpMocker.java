package net.loyintea.mock.http.biz.impl;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.common.biz.impl.MockerAsSkeleton;
import net.loyintea.mock.http.bean.MockHttpConfig;
import net.loyintea.mock.http.bean.MockInput4Http;
import net.loyintea.mock.http.service.HttpMockService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提供一个Http的mocker
 */
@Service
@Slf4j
class HttpMocker extends MockerAsSkeleton<MockInput4Http, ResponseEntity<Object>> {


    @Resource
    private HttpMockService httpMockService;

    /**
     * 给Http做一个处理
     *
     * @param param
     * @return
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

    private boolean isConfigMatched(MockHttpConfig config, MockInput4Http param) {
        return false;
    }

    private ResponseEntity<Object> toResponse(MockHttpConfig config) {
        return null;
    }
}
