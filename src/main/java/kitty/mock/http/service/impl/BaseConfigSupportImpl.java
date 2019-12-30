package kitty.mock.http.service.impl;

import kitty.mock.common.util.JsonUtils;
import kitty.mock.http.utils.FileUtils;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 处理配置加载的基础类
 *
 * @param <T> 配置类的实例
 * @author Les papillons
 * @date 2019 -12-10
 */
abstract class BaseConfigSupportImpl<T> {
    /** The Logger. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 解析得到的配置类 */
    List<T> configList;
    /** 配置类的反射用类型 */
    @Setter(AccessLevel.PROTECTED)
    private Type configType;
    /** 配置文件位置 */
    @Setter(AccessLevel.PROTECTED)
    private String location;

    /**
     * Instantiates a new Base config support.
     *
     * @param configType the config type
     * @param location   the location
     */
    protected BaseConfigSupportImpl(Type configType, String location) {
        this.configType = configType;
        this.location = location;
    }

    /**
     * 注入完毕后，给它赋值
     */
    @PostConstruct
    public void initConfigList() {

        logger.info("初始化配置列表. configType:{}, location:{}", configType, location);

        // 注意顺序
        List<T> tempConfigList = new LinkedList<>();

        // DONE 考虑classpath
        File start = readFile(location);

        // 如果没有读到文件，可能是没有配置。
        if (start != null) {
            parseConfig(start, tempConfigList);

            // 如果有文件、但是解析不了数据，就抛异常
            if (CollectionUtils.isEmpty(tempConfigList)) {
                throw new RuntimeException(location + " 路径下没有HttpMock的配置！");
            }
        }
        this.configList = tempConfigList;
    }

    /**
     * Read file file.
     *
     * @param path the path
     * @return the file
     */
    private File readFile(String path) {
        // 如果在classpath下，则用classpath来获取
        if (StringUtils.isBlank(path)) {
            // 如果没有配置，正常处理
            // 例如，有可能没有做mock配置，全部forward
            logger.warn("没有配置config文件路径。");
            return null;
        } else {
            // 解析文件
            return new File(FileUtils.toAbsolutePath(location));
        }
    }

    /**
     * @param file       the file
     * @param configList the config list
     */
    private void parseConfig(File file, List<T> configList) {
        if (file.isDirectory() && file.listFiles() != null) {
            // 如果是文件夹，且文件夹非空，则迭代文件夹内容
            Stream.of(Objects.requireNonNull(file.listFiles())).filter(Objects::nonNull)
                    .forEach(f -> parseConfig(f, configList));
        } else if (file.isFile()) {
            // 如果是文件，则解析文件内容
            configList.addAll(JsonUtils.fromFile(file, configType));
        }
        // 如果不满足以上条件（如文件不存在、空文件夹），则直接返回不做处理。
        // 此时，configList会保持入参数据
    }
}
