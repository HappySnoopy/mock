package net.loyintea.mock.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.internal.Engine;

import java.util.Map;

/**
 * jexl表达式解析类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JexlUtils {

    /**
     * JEXL的表达式解析引擎
     */
    private static final JexlEngine JEXL_ENGINE = new Engine();

    /**
     * 计算将入参param带入表达式expression中后的结果
     * <p>
     * 结果必须是true/false。
     *
     * @param expression 表达式。其中的变量必须来自入参param，其结果必须是布尔值
     * @param param      数据。可以是Map，也可以是其他对象。建议实现toString方法，以方便查看日志
     * @return expression的计算结果
     */
    public static boolean isMatched(String expression, Object param) {
        JexlExpression jexlExpression = JEXL_ENGINE.createExpression(expression);

        JexlContext context;
        if (param instanceof Map) {
            context = new MapContext((Map<String, Object>) param);
        } else {
            context = new ObjectContext<>(JEXL_ENGINE, param);
        }

        Object result = jexlExpression.evaluate(context);

        log.info("exprsion:{}, param:{}, jexlExpresion:{}, jexlContext:{}, result:{}", expression, param, jexlExpression, context, result);


        return (boolean) result;
    }
}
