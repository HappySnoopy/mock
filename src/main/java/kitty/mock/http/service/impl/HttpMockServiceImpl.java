package kitty.mock.http.service.impl;

import com.google.gson.reflect.TypeToken;
import kitty.mock.http.bean.HttpMockConfig;
import kitty.mock.http.service.HttpMockService;
import lombok.extern.slf4j.Slf4j;
import kitty.mock.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Http Mock的配置服务
 */
@Service("httpMockService")
@Slf4j
class HttpMockServiceImpl implements HttpMockService {

    /**
     * 配置列表的类型（带泛型）
     */
    private static final Type CONFIG_LIST_TYPE = new TypeToken<List<HttpMockConfig>>() {
    }.getType();

    /**
     * 配置文件的路径
     */
    @Value("${mock.http.config.location}")
    private String location;

    /**
     * 遍历{@link #location}下所有的文件和文件夹，把里面的配置解析出来，放到一个大list里
     * <p>
     * TODO 放到缓存或者内存里。
     *
     * @return 返回配置文件列表
     */
    @Override
    public List<HttpMockConfig> queryConfigList() {
        // 注意顺序
        List<HttpMockConfig> configList = new LinkedList<>();

        // TODO 考虑classpath的问题
        File start = new File(location);

        parseConfig(start, configList);


        if (CollectionUtils.isEmpty(configList)) {
            throw new RuntimeException(location + " 路径下没有HttpMock的¬配置！");
        }
        return configList;
    }

    private void parseConfig(File file, List<HttpMockConfig> configList) {

        if (file.isDirectory() && file.listFiles() != null) {
            // 如果是文件夹，且文件夹非空，则迭代文件夹内容
            Stream.of(file.listFiles()).filter(Objects::nonNull).forEach(f -> parseConfig(f, configList));
        } else if (file.isFile()) {
            // 如果是文件，则解析文件内容
            configList.addAll(JsonUtils.fromFile(file, CONFIG_LIST_TYPE));
        }
        // 如果不满足以上条件（如文件不存在、空文件夹），则之额吉返回不做处理
    }
}
