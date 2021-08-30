package top.frankyang.pre.api.util;

import sun.misc.Unsafe;
import top.frankyang.pre.api.util.ReflectUtils;

import java.util.function.Consumer;

/**
 * 一个宽松的消费者。它可以通过{@link Unsafe}抛出任意异常。黑魔法！去你的受检异常！
 *
 * @see Consumer
 */
@FunctionalInterface
public interface LooseConsumer<T> extends Consumer<T> {
    T accept0() throws Throwable;

    default void accept(T t) {
        try {
            accept0();
        } catch (Throwable throwable) {
            ReflectUtils.forceThrow(throwable);
        }
    }
}
