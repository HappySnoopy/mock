package kitty.mock.http.utils;

import org.junit.Test;

public class FileUtilsTest {

    @Test
    public void toAbsolutePath() {
        String path = FileUtils.toAbsolutePath("classpath:config");

        System.out.println(path);

    }
}