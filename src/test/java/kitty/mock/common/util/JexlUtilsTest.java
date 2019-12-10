package kitty.mock.common.util;

import kitty.mock.http.bean.RequestEntity4Mock;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JexlUtilsTest {

    @Test
    public void test() {
        Object result = JexlUtils.isMatched("true", new Object());
        assertEquals(true, result);
    }


    @Test
    public void test1() {

        RequestEntity4Mock<Object> req = new RequestEntity4Mock<Object>(null, new HttpHeaders(), HttpMethod.GET,
                URI.create("http://localhost:1986/query?type=yuantong&postid=11111111111"), Object.class);


        Map<String, String> param = new HashMap<>();
        param.put("type", "yuantong");
        param.put("postid", "11111111111");
        req.setParam(param);

        assertEquals("yuantong", req.getParam().get("type"));
        assertEquals("11111111111", req.getParam().get("postid"));

        Object result = JexlUtils.isMatched("param.type=='yuantong' && param.postid=='11111111111'", req);
        assertEquals(true, result);
    }
}