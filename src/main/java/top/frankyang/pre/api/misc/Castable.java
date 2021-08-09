package top.frankyang.pre.api.misc;

import org.python.core.Py;
import org.python.core.PyObject;

/**
 * 表明一个类的实例可以被转型为指定类的实例。
 *
 * @param <T> 可转型的类。
 */
@FunctionalInterface
public interface Castable<T> {
    /**
     * 转型一个对象到指定的类。如果它是指定的类及其子类的实例，直接返回它本身。如果它是{@link Castable}实例，并且可以转型到指定的类及其子类，返回{@link Castable#cast()}。如果它是一个Python对象，将它转型成一个Java对象使用，然后再进行后续的转型。
     *
     * @param object 要转型的对象。
     * @param clazz  要转型为的类实例。
     * @param <T>    要转型为的类。
     * @return 转型后的对象。
     * @throws IllegalArgumentException 如果要转型的对象不能被转型为指定的类。
     */
    static <T> T infer(Object object, Class<T> clazz) {
        if (!PyObject.class.isAssignableFrom(clazz) && object instanceof PyObject) {  // Not asking for a Python object
            object = Py.tojava((PyObject) object, Object.class);
        }

        if (clazz.isInstance(object))
            return clazz.cast(object);

        if (object instanceof Castable<?>) {
            Castable<?> castable = (Castable<?>) object;
            final Object o, p;
            if (clazz.isAssignableFrom(Castable.class) &&
                clazz.isInstance(o = castable.cast())) {
                return clazz.cast((Castable<?>) () -> o);
            }
            if (clazz.isInstance(p = castable.cast())) {
                return clazz.cast(p);
            }
            throw new IllegalArgumentException(
                "The object is castable, but not to an instance of the specified class."
            );
        } else {
            throw new IllegalArgumentException(
                "The object is neither castable nor an instance of the specified class."
            );
        }
    }

    /**
     * 将该实例转型为可转型的实例。该方法应当每次都返回同一个实例，而且最好能够缓存这个实例。
     *
     * @return 转型后的实例。
     */
    T cast();
}
