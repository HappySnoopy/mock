package kitty.mock.http.service.impl;

import kitty.mock.http.bean.RequestEntity4Mock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 继承父类，用来解析 RequestEntity4Mock类型
 *
 * @author Pluto
 * @date 2019 -12-10
 */
@Component
@Slf4j
public class HttpEntity4MockProcessorImpl extends HttpEntityMethodProcessor {

    public HttpEntity4MockProcessorImpl(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    /**
     * Supports parameter boolean.
     *
     * @param parameter the parameter
     * @return the boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RequestEntity4Mock.class == parameter.getParameterType();
    }

    /**
     * Resolve argument object.
     *
     * @param parameter     the parameter
     * @param mavContainer  the mav container
     * @param webRequest    the web request
     * @param binderFactory the binder factory
     * @return the object
     * @throws IOException                        the io exception
     * @throws HttpMediaTypeNotSupportedException the http media type not supported exception
     */
    @Override
    @Nullable
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) throws IOException, HttpMediaTypeNotSupportedException {

        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        Type paramType = getHttpEntityType(parameter);
        if (paramType == null) {
            throw new IllegalArgumentException(
                    "HttpEntity parameter '" + parameter.getParameterName() + "' in method " + parameter
                            .getMethod() + " is not parameterized");
        }

        Object body = readWithMessageConverters(webRequest, parameter, paramType);
        RequestEntity4Mock entity = new RequestEntity4Mock<>(body, inputMessage.getHeaders(), inputMessage.getMethod(),
                inputMessage.getURI(), paramType);

        // 增加两个操作：header和param处理
        entity.setHeader(parseHeader(inputMessage.getHeaders()));
        entity.setParam(parseParam(inputMessage.getServletRequest()));

        log.info("解析 RequestEntity4Mock 完毕。entity:{}", entity);
        return entity;
    }

    /**
     * Parse param map.
     *
     * @param servletRequest the servlet request
     * @return the map
     */
    private Map<String, String> parseParam(HttpServletRequest servletRequest) {
        return servletRequest.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(e.getValue(), ',')));
    }

    /**
     * Parse header map.
     *
     * @param headers the headers
     * @return the map
     */
    private Map<String, String> parseHeader(HttpHeaders headers) {
        return headers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(e.getValue(), ',')));
    }

    /**
     * Gets httpEntityType.
     *
     * @param parameter the parameter
     * @return 入参泛型参数
     */
    private Type getHttpEntityType(MethodParameter parameter) {
        Assert.isAssignable(HttpEntity.class, parameter.getParameterType());
        Type parameterType = parameter.getGenericParameterType();
        if (parameterType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) parameterType;
            if (type.getActualTypeArguments().length != 1) {
                throw new IllegalArgumentException("Expected single generic parameter on '" + parameter
                        .getParameterName() + "' in method " + parameter.getMethod());
            }
            return type.getActualTypeArguments()[0];
        } else if (parameterType instanceof Class) {
            return Object.class;
        } else {
            return null;
        }
    }
}
