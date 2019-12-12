package kitty.mock.http.service;


import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 * The interface Http record service.
 */
public interface HttpRecordService {

    /**
     * 把一次http操作的请求和响应数据全都记录下来，供后续使用
     *
     * @param req  请求数据
     * @param resp 响应数据
     */
    void record(RequestEntity<Object> req, ResponseEntity<Object> resp);
}
