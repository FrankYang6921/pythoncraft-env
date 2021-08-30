package top.frankyang.pre.api.misc;

import top.frankyang.pre.api.util.CopyUtils;

/**
 * 表明一个对象是可复制的。
 *
 * @param <T> 该对象的类型。
 */
@SuppressWarnings("unchecked")
public interface Copyable<T extends Copyable<T>> {
    default T shallowCopy() {
        return (T) CopyUtils.shallowCopy(this);
    }

    default T deepCopy() {
        return (T) CopyUtils.deepCopy(this);
    }
}
