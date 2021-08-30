package top.frankyang.pre.api.util;

import sun.misc.Unsafe;
import top.frankyang.pre.api.util.ReflectUtils;

/**
 * 一个宽松的{@link Runnable}。它可以通过{@link Unsafe}抛出任意异常。黑魔法！去你的受检异常！
 *
 * @see Runnable
 */
@FunctionalInterface
public interface LooseRunnable extends Runnable {
    void run0() throws Throwable;

    @Override
    default void run() {
        try {
            run0();
        } catch (Throwable throwable) {
            ReflectUtils.forceThrow(throwable);
        }
    }
}
