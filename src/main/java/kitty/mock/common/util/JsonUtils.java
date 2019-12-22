package kitty.mock.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * json解析器
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final Gson GSON = new Gson();
    private static final Gson GSON_RECORD = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    /**
     * 对象解析为json字符串
     *
     * @param object 待解析对象
     * @return 解析完毕的json串 string
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 为记录http请求而定义的转json方法
     * <p>
     * 不对HTML符号做转义处理；生成格式化（带换行和缩进）的字符串）
     *
     * @param object the object
     * @return the string
     */
    public static String toJson4Record(Object object) {
        return GSON_RECORD.toJson(object);
    }

    /**
     * 字符串解析为对象
     * <p>
     * 不能处理泛型对象
     *
     * @param <T>    the type parameter
     * @param json   json字符串
     * @param tClass 转换后的类型
     * @return 解析结果 object
     */
    public static <T> Object fromJson(String json, Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }

    /**
     * 从文件中读取json数据，解析为对应的类型
     *
     * @param <T>    列表中的数据类型
     * @param file   文件。必须是指定文件，不能是文件夹
     * @param tClass 解析出来的类型
     * @return 数据列表 list
     */
    public static <T> List<T> fromFile(File file, Type
            tClass) {

        List<T> result;
        try (Reader reader = new BufferedReader(new FileReader(file))) {

            log.debug("typeReference:{}", tClass);
            result = GSON.fromJson(reader, tClass);

        } catch (IOException e) {
            log.error("无法从文件中解析json数据。file:{}, tClass:{}, message:{}", file, tClass, e.getMessage());
            throw new RuntimeException("无法从文件中解析json数据。filePath:" + file
                    + ", tClass:" + tClass, e);
        }

        log.info("file:{}, tClass:{}, result:{}", file, tClass, result);
        return result;
    }
}
