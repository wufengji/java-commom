package com.wfj.common.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author wfj
 * @since 2020/10/20
 */
public class LambdaUtils {

    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }
}
