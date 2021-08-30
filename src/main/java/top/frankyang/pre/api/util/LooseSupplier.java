package top.frankyang.pre.api.util;

import sun.misc.Unsafe;
import top.frankyang.pre.api.util.ReflectUtils;

import java.util.function.Supplier;

/**
 * 一个宽松的生产者。它可以通过{@link Unsafe}抛出任意异常。黑魔法！去你的受检异常！
 *
 * @see Supplier
 */
@FunctionalInterface
public interface LooseSupplier<T> extends Supplier<T> {
    T get0() throws Throwable;

    default T get() {
        try {
            return get0();
        } catch (Throwable throwable) {
            ReflectUtils.forceThrow(throwable);
            return null;  // Impossible
        }
    }
}
