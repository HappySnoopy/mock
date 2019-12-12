package kitty.mock.http.service.impl;

import kitty.mock.http.service.HttpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 记录请求数据
 */
@Service
@Slf4j
class HttpRecordServiceImpl implements HttpRecordService {
    /**
     * 把请求和响应记录下来
     * <p>
     * 并自动生成HttpConfig的配置
     */
    @Override
    public void record(RequestEntity<Object> req, ResponseEntity<Object> resp) {

    }
}
