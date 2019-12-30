package kitty.mock.http.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * The type FileUtils.
 *
 * @author 林俊 <junlin8@creditease.cn>
 * @date 2019 -12-30
 */
@Slf4j
public class FileUtils {
    /** classpath的配置前缀 */
    public static final String CLASSPATH = "classpath:";

    /**
     * 根据path来获取一个文件路径
     *
     * @param path the path
     * @return the string
     */
    public static String toAbsolutePath(String path) {
        if (StringUtils.startsWith(path, CLASSPATH)) {
            // 从classpath下解析文件
            String classpathFile = path.substring(path.indexOf(CLASSPATH) + CLASSPATH.length());
            log.debug("path:{}, classpathFile:{}", path, classpathFile);
            classpathFile = FileUtils.class.getClassLoader().getResource(classpathFile).getFile();
            if (classpathFile.startsWith("/")) {
                return classpathFile.substring(1);
            } else {
                return classpathFile;
            }
        } else {
            return path;
        }
    }
}
