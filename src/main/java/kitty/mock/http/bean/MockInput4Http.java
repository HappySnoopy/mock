package kitty.mock.http.bean;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 这个input可以考虑替换为 @RequestEntity
 */
@Data
public class MockInput4Http {

    /**
     * 调用者的IP
     */
    private String clientIp;


    /**
     * 请求的uri
     */
    private String uri;

    /**
     * HTTP请求方法
     */
    private HttpMethod method;

    /**
     * 请求的参数
     */
    private Map<String, String> params;

    /**
     * 请求header
     */
    private Map<String, String> headers;

    /**
     * 预留原始请求
     */
    @ToString.Exclude
    private HttpServletRequest httpRequest;

}
