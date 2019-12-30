package kitty.mock.http.config;

import kitty.mock.http.service.impl.HttpEntity4MockProcessorImpl;
import kitty.mock.http.service.impl.StringObjectHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * http mock的一些配置项
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Slf4j
@Configuration
public class Configurations implements WebMvcConfigurer {

    @Resource
    private HttpEntity4MockProcessorImpl httpEntity4MockProcessor;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {

        /*
         * 这里涉及一个知识点，记录一下。
         *
         * 如果直接使用最基础的RestTemplate，当http接口中返回了Content-Encoding:gzip时，会报错：
         * java.util.zip.ZipException: Not in GZIP format
         *
         * 这是因为服务端为了降低网络流量，采用了压缩格式来返回结果。
         * 解决问题有几种方式，可以参见：https://www.jianshu.com/p/2ed17552d0c3
         * 1. 自己从InputStream/GZIPInputStream中获取byte[]，然后转成String
         * 2. 先用ResponseEntity<byte[]>来接收数据，然后再用GZIPInputStream转成String
         * 3. 使用Apache Commons提供的辅助方法
         * 4. 使用Apache HttpClient。主要是使用HttpComponentsClientHttpRequestFactory。
         * 4.1 显式的为restTemplate配置一个HttpComponentsClientHttpRequestFactory
         * 4.1 使用RestTemplateBuilder来隐式配置一个HttpComponentsClientHttpRequestFactory
         *
         * 所以，请合理使用工具类库
         * */

        RestTemplate restTemplate = restTemplateBuilder.build();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().addAll(Arrays.asList(converter, new StringObjectHttpMessageConverter()));

        log.info("restTemplateBuilder:{}, restTemplate:{}", restTemplateBuilder, restTemplate);
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
