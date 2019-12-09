package kitty.mock.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * http mock的一些配置项
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Configuration
public class HttpMockConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
