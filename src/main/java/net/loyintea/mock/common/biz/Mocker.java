package net.loyintea.mock.common.biz;

import java.util.Optional;

public interface Mocker<I, O> {

    /**
     * 根据入参param，返回一个mock结果
     *
     * @param param 请求参数
     * @return mock的结果。有可能为null
     */
    Optional<O> mock(I param);
}
