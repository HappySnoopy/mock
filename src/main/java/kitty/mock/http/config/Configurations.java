package kitty.mock.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * http mock的一些配置项
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Configuration
public class Configurations {

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringObjectHttpMessageConverter());
        return restTemplate;
    }

    /**
     * 把String当做Object来解析
     *
     * @author 林俊 <junlin8@creditease.cn>
     * @date 2019 -12-10
     */
    class StringObjectHttpMessageConverter extends StringHttpMessageConverter {

        /**
         * 默认编码格式为UTF-8；
         * <p>
         * 支持多种字符串
         */
        StringObjectHttpMessageConverter() {
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
}
