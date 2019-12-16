package kitty.mock.http.service.impl;

import kitty.mock.common.util.JsonUtils;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.bean.RequestEntity4Mock;
import kitty.mock.http.service.HttpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 记录请求数据
 */
@Service
@Slf4j
class HttpRecordServiceImpl implements HttpRecordService {

    /** 记录数据的路径 */
    @Value("${mock.http.config.record.location}")
    private String location;

    /**
     * 把请求和响应记录下来
     * <p>
     * 并自动生成HttpConfig的配置
     */
    @Override
    public void record(RequestEntity<Object> req, ResponseEntity<Object> resp) {

        // 用请求的path当做文件名
        // path中的"/"用下划线替换
        StringBuilder fileNameBuilder = new StringBuilder(req.getUrl().getPath());
        // 只要有值就替换
        while (fileNameBuilder.indexOf("/") > -1) {
            fileNameBuilder.replace(fileNameBuilder.indexOf("/"), fileNameBuilder.indexOf("/") + 1, "_");
        }

        HttpMockConfig mockConfig = new HttpMockConfig();
        mockConfig.setUri(req.getUrl().getPath());
        mockConfig.setMethod(req.getMethod());
        if (req instanceof RequestEntity4Mock) {
            RequestEntity4Mock mockReq = (RequestEntity4Mock) req;
            mockConfig.setClientIp(mockReq.getClientIp());
            // TODO 解析为JEXL表达式
            mockConfig.setExpression(mockReq.getParam().toString());
        }

        // 这里是返回值
        mockConfig.setResponseBody(resp.getBody());
        // TODO 解析返回值的header
        mockConfig.setHeaderMap(resp.getHeaders().toSingleValueMap());
        mockConfig.setHttpStatusCode(resp.getStatusCodeValue());

        // TODO 转json后写入文件中
        try {
            Files.write(Paths.get(
                    location + File.pathSeparator + fileNameBuilder.toString() + File.pathSeparator + ".json"),
                    JsonUtils.toJson(mockConfig).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("保存http mock的转发结果出错。req:{}, resp:{}, mockConfig:{}", req, resp, mockConfig, e);
        }
    }
}
