package kitty.mock.http.bean;

import lombok.Data;

/**
 * Http请求转发的配置
 * <p>
 * 如：#originUri = /test/forward，#forwardUrl = http://localhost:8081
 * <p>
 * 则：所有访问 http://当前服务/test/forward的请求，都会转发哦大 http://localhost:8081/test/forward上
 *
 * TODO 这个config里面也需要有expression、clientIp等
 *
 * @author Jerry
 * @date 2019 -12-09
 */
@Data
public class HttpForwardConfig {

    /**
     * 转发的URL
     * <p>
     * 注意是URL，不是URI
     */
    private String forwardUrl;

    /**
     * 初始URI
     * <p>
     * 注意是URI不是
     */
    private String originUri;
}
