package net.loyintea.mock.http.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.loyintea.mock.http.bean.MockHttpConfig;
import net.loyintea.mock.http.bean.MockInput4Http;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpMockUtils {


    /**
     * 把http请求解析为一个mock入参
     *
     * @param request http请求
     * @return mock参数
     */
    public static MockInput4Http parse(HttpServletRequest request) {
        MockInput4Http input = new MockInput4Http();
        input.setClientIp(request.getRemoteAddr());
        input.setUri(request.getRequestURI());

        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            params.put(key, Arrays.toString(request.getParameterValues(key)));
        }
        input.setParams(params);

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            headers.put(key, request.getHeader(key));
        }
        input.setHeaders(headers);

        input.setHttpRequest(request);
        return input;
    }

    public static MockHttpConfig build4Query(MockInput4Http input) {
        MockHttpConfig config = new MockHttpConfig();
        config.setClientIp(input.getClientIp());
        config.setUri(input.getUri());
        return config;
    }

}
