package net.loyintea.mock.serv;

import net.loyintea.mock.http.bean.MockInput4Http;
import org.springframework.http.ResponseEntity;

/**
 * 配置服务
 */
public interface MockConfigurator {


    /**
     * 这里似乎就不应该返回ResponseEntity了，也许应该再声明一个MockOutput，用于隔离Controller的实体
     * <p>
     * 后续扩展：不同类型的返回结果（一般以Content-type为区分）的，需要不同的子类来实现。
     *
     * @param input
     * @return
     */
    ResponseEntity<Object> config(MockInput4Http input);
}
