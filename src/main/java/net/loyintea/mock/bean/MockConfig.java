package net.loyintea.mock.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class MockConfig implements Comparable<MockConfig>{

    public static MockConfig build4Query(MockInput input){
        MockConfig config = new MockConfig();
        config.setClientIp(input.getClientIp());
        config.setUri(input.getUri());
        return config;
    }


    /**
     * 调用者的IP
     */
    private String clientIp;


    /**
     * 请求的uri
     */
    private String uri;

    /**
     * 表达式
     *
     * 满足此表达式时，表示将返回指定的结果
     */
    private String expression;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 响应header
     *
     * 默认情况下，应该是按request的header进行返回。
     *
     * 如果需要返回其它的东西（新增或覆盖，目前似乎不需要删除），可以在这里配置
     */
    private Map<String, String> headerMap;

    /**
     * 返回的http状态码
     */
    private int httpStatusCode;

    /**
     * 排序字段
     * <p>
     * 为了方便向中间插入配置，用float
     */
    private float sort;

    @Override
    public int compareTo(MockConfig o) {
        if(o == null){
            return 0;
        }else{
            return Float.compare(this.sort,o.sort);
        }
    }
}
