package kitty.mock.http.service.impl;

import kitty.mock.http.bean.RequestEntity4Mock;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRecordServiceImplTest {

    private HttpRecordServiceImpl service = new HttpRecordServiceImpl();

    @Test
    public void test() {

        ReflectionTestUtils.setField(service, "location", "/Users/linjun/git/mock/src/main/resources/config/");

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("msg", "SUCCESS");
        body.put("data", new HashMap<>());
        RequestEntity4Mock<Object> req = new RequestEntity4Mock<>(body, new HttpHeaders(), HttpMethod.POST,
                URI.create("http://www.google.com:8080/image/abc"), Object.class);

        Map<String, String> param = new HashMap<>();
        param.put("a", "1");
        param.put("b", "1,2");
        req.setParam(param);

        ResponseEntity<Object> resp = new ResponseEntity<>(HttpStatus.OK);
        service.record(req, resp);

    }

}