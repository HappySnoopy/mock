package kitty.mock.http.service.impl;

import kitty.mock.common.util.JsonUtils;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.bean.RequestEntity4Mock;
import kitty.mock.http.service.HttpRecordService;
import kitty.mock.http.utils.FileUtils;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 记录请求数据
 */
@Service
@Slf4j
class HttpRecordServiceImpl implements HttpRecordService {

    /** The Separator. */
    private static final char SEPARATOR = '_';
    /** The Separator str. */
    private static final String SEPARATOR_STR = "_";
    /**
     * 记录数据的路径
     */
    @Value("${mock.http.config.record.location}")
    private String location;

    /**
     * 把请求和响应记录下来
     * <p>
     * 并自动生成HttpConfig的配置
     */
    @Override
    public void record(RequestEntity<Object> req, ResponseEntity<Object> resp) {

        log.info("http-record-start. req:{}, resp:{}", req, resp);


        HttpMockConfig mockConfig = new HttpMockConfig();
        mockConfig.setUri(req.getUrl().getPath());
        mockConfig.setMethod(req.getMethod());
        if (req instanceof RequestEntity4Mock) {
            RequestEntity4Mock mockReq = (RequestEntity4Mock) req;
            mockConfig.setClientIp(mockReq.getClientIp());
            // DONE 解析为JEXL表达式
            mockConfig.setExpression(toJexlExpression(mockReq.getParam()));
        }

        // 这里是返回值
        mockConfig.setResponseBody(resp.getBody());
        // TODO 解析返回值的header
        mockConfig.setHeaderMap(resp.getHeaders().toSingleValueMap());
        mockConfig.setHttpStatusCode(resp.getStatusCodeValue());

        // DONE 转json后写入文件中
        try {
            // 这里需要一个自定义的JsonUtils，因为在JSON序列化/反序列化处理上有特定配置
            Files.write(Paths.get(generateFileName(req)),
                    JsonUtils.toJson4Record(mockConfig).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("保存http mock的转发结果出错。req:{}, resp:{}, mockConfig:{}", req, resp, mockConfig, e);
        }
    }

    /**
     * 把请求参数解析为jexl表达式
     * <p>
     * key==value&&key1==value1
     *
     * @param param 请求参数
     * @return jexl表达式。表达式最终返回结果是true
     */
    private String toJexlExpression(Map<String, String> param) {
        return param.entrySet().stream().map(e -> "param." + e.getKey() + "==" + "'" + e.getValue() + "'")
                .collect(Collectors.joining("&&"));
    }

    /**
     * 生成记录文件的文件名
     * <p>
     * 生成规则：
     * protocol_host_port/URI.json
     * <p>
     * 路径名和文件名中的“/”统一替换为下划线"_"
     * <p>
     * 例如，req访问的是http://www.google.com/image/abc
     * <p>
     * 那么生成的文件名就是:
     * location/http_www_google_com/image_abc.json
     *
     * @param req 请求参数
     * @return 最后生成的文件名。注意这里会创建路径，但并不创建配置文件
     */
    private String generateFileName(RequestEntity<Object> req) {
        log.debug("location:{}, req:{}", location, req);

        StringBuilder fileNameBuilder = new StringBuilder(1024).append(FileUtils.toAbsolutePath(location))
                .append(File.separatorChar)
                // http or https
                .append(req.getUrl().getScheme())
                .append(SEPARATOR)
                // 127_0_0_1
                .append(req.getUrl().getHost().replaceAll("\\.", SEPARATOR_STR));

        if (req.getUrl().getPort() > 0) {
            // 8080_。如果没有指定端口号，可能是-1。-1会导致无法生成文件，因此不做处理
            fileNameBuilder.append(SEPARATOR).append(req.getUrl().getPort());
        }
        fileNameBuilder.append(File.separatorChar);

        // 保证这个路径存在
        File file = new File(fileNameBuilder.toString());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }

        // image_abc.json 由于path前一定有一个/，所以这里要去掉第一个/
        fileNameBuilder.append(req.getUrl().getPath().substring(1)
                .replaceAll("/", SEPARATOR_STR))
                .append(".json");

        log.info("http recode file is: {}", fileNameBuilder);

        return fileNameBuilder.toString();
    }
}
