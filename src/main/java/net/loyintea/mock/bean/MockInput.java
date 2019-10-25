package net.loyintea.mock.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个input可以考虑替换为 @RequestEntity
 */
@Getter
@Setter
@ToString
public class MockInput {

    /**
     * 调用者的IP
     */
    private String clientIp;


    /**
     * 请求的uri
     */
    private String uri;

    /**
     * 请求的参数
     */
    private Map<String, String> params;

    /**
     * 请求header
     */
    private Map<String, String> headers;

    public MockInput() {
        super();
    }

    public static MockInput parse(HttpServletRequest request) {
        MockInput input = new MockInput();
        input.clientIp = request.getRemoteAddr();
        input.uri = request.getRequestURI();

        input.params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            input.params.put(key, Arrays.toString(request.getParameterValues(key)));
        }

        input.headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            input.headers.put(key, request.getHeader(key));
        }
        return input;
    }

}
