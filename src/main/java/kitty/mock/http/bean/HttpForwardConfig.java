package kitty.mock.http.bean;

import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * Http请求转发的配置
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Data
public class HttpForwardConfig {

    /** 转发的url */
    private String forwardUrl;

    /** 转发的方法 */
    private HttpMethod httpMethod;
}
