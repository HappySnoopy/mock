package kitty.mock.http.service.impl;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 把String当做Object来解析
 *
 * @author Pluto
 * @date 2019 -12-10
 */

public class StringObjectHttpMessageConverter extends StringHttpMessageConverter {

    /**
     * 默认编码格式为UTF-8；
     * <p>
     * 支持多种字符串
     */
    public StringObjectHttpMessageConverter() {
        super(StandardCharsets.UTF_8);
        setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
    }

    /**
     * 支持Object的处理
     *
     * @param clazz the clazz
     * @return the boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Object.class == clazz;
    }
}