package kitty.mock.http.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

/**
 * 为Mock定制的一个RequestEntity
 * <p>
 * DONE toString()方法可以手动定制一个
 *
 * @param <T> the type parameter
 * @author Pluto
 * @date 2019 -12-10
 */
@Getter
@Setter
public class RequestEntity4Mock<T> extends RequestEntity<T> {

    /** 把父类的headers解析出来 */
    private Map<String, String> header;
    /** 把query中的参数解析出来 */
    private Map<String, String> param;

    /**
     * 匹配父类的构造方法，设定一些初始值
     *
     * @param body      the body
     * @param headers   the headers
     * @param method    the method
     * @param url       the url
     * @param paramType the param type
     */
    public RequestEntity4Mock(T body, MultiValueMap<String, String> headers, HttpMethod method, URI url,
                              Type paramType) {
        super(body, headers, method, url, paramType);
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "RequestEntity4Mock = {URI = [" + getUrl()
                .getPath() + "]; " + "METHOD = [" + getMethod() + "]; PARAM = [" + getParam() + "]; HEADER = [" + getHeader() + "]}";
    }
}
