package net.loyintea.mock.biz.impl;

import net.loyintea.mock.biz.MockBiz;
import net.loyintea.mock.http.bean.MockInput4Http;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
class MockBizImpl implements MockBiz {
    @Override
    public ResponseEntity<Object> mock(MockInput4Http input) {

        Map<String, String> map = new HashMap<>();
        map.put("code", "0000");
        map.put("message", "success");

        return ResponseEntity.of(Optional.of(map));
    }
}
