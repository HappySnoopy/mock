package kitty.mock.http.config;

import kitty.mock.http.service.impl.HttpEntity4MockProcessorImpl;
import kitty.mock.http.service.impl.StringObjectHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * http mock的一些配置项
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Configuration
public class Configurations implements WebMvcConfigurer {

    @Resource
    private HttpEntity4MockProcessorImpl httpEntity4MockProcessor;

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
     * 增加解析器
     *
     * @param resolvers the resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(httpEntity4MockProcessor);
    }
}
