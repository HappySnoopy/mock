package net.loyintea.mock.common.biz.impl;

import lombok.extern.slf4j.Slf4j;
import net.loyintea.mock.common.biz.Mocker;

import java.util.Optional;

/**
 * mocker的模板类。
 * <p>
 * 实现mock服务的三个基本步骤。
 *
 * @param <I>
 * @param <O>
 */
@Slf4j
public class MockerAsSkeleton<I, O> implements Mocker<I, O> {
    @Override
    public Optional<O> mock(I input) {
        log.info("mock服务开始。param:{}", input);

        // 首先尝试mock
        Optional<I> inputOp = Optional.of(input);

        // @formatter:off
        O output = inputOp
                .map(this::doMock)
                .orElseGet(() -> inputOp
                        .map(this::forward)
                        .map(o -> {
                            this.record(input, o);
                            return o;
                        })
                        .orElse(null)
                );

        log.info("mock服务完成。result:{}", output);
        return Optional.ofNullable(output);
    }
    // @formatter:on

    private void record(I i, O o) {
        log.debug("mock服务：记录下入参和出参。input:{}, output:{}", i, o);

    }

    private O forward(I param) {
        log.warn("mock服务：默认没有做任何转发，直接返回null。param:{}", param);
        return null;
    }

    private O doMock(I param) {
        log.warn("mock服务：默认没有做任何mock，直接返回null。param:{}", param);
        return null;
    }
}
