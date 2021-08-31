package top.frankyang.pre.api.misc.conversion;

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
        if (object instanceof PyObject && !PyObject.class.isAssignableFrom(clazz)) {  // Not asking for a Python object
            object = ((PyObject) object).__tojava__(Object.class);
        }

        if (clazz.isInstance(object))
            return clazz.cast(object);

        if (object instanceof Castable<?>) {
            Castable<?> c = (Castable<?>) object;
            final Object o;
            if (clazz.isAssignableFrom(Castable.class)) {
                return clazz.cast(c);
            } else if (clazz.isInstance(o = c.cast())) {
                return clazz.cast(o);
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
