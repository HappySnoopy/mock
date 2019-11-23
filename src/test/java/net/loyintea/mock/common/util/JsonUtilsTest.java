package net.loyintea.mock.common.util;

import com.google.gson.reflect.TypeToken;
import net.loyintea.mock.http.bean.HttpMockConfig;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class JsonUtilsTest {

    @Test
    public void fromFile() {

        List<HttpMockConfig> httpConfigList = JsonUtils.fromFile(
                new File("/Users/linjun/git/mock/src/main/resources/HttpMockConfig.json"), new TypeToken<List<HttpMockConfig>>() {
                }.getType()
        );

        System.out.println(httpConfigList);
    }
}