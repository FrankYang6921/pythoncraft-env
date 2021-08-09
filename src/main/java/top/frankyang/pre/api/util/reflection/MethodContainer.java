package top.frankyang.pre.api.util.reflection;

import top.frankyang.pre.api.misc.Castable;

/**
 * 方法容器。该类用于包装一个
 */
public interface MethodContainer {
    MethodContainer DUMMY = new MethodContainer() {
        @Override
        public Object invoke0(Object[] args) {
            return null;
        }

        @Override
        public Class<?>[] getParamTypes() {
            return new Class[0];
        }

        @Override
        public Class<?> getReturnType() {
            return Object.class;
        }
    };

    /**
     * 使用所传入的实参调用上游函数，尝试将每一个实参使用{@link Castable#infer(Object, Class)}转型到上游函数所需要的类型。该方法特别适合Python使用。
     *
     * @param args 调用函数所需的参数。
     * @param <T>  类似于语法糖的泛型参数，允许隐式转型到任意一个类型。
     * @return 函数的返回值。
     */
    @SuppressWarnings("unchecked")
    default <T> T invokeInferred(Object... args) {
        Class<?>[] classes = getParamTypes();
        if (classes.length != args.length) {
            throw new IllegalArgumentException("Argument mismatch: " + classes.length + " != " + args.length + " .");
        }
        Object[] args0 = new Object[args.length];
        for (int i = 0; i < classes.length; i++) {
            args0[i] = Castable.infer(args[i], classes[i]);
        }

        return (T) invoke0(args0);
    }

    /**
     * 直接使用所传入的实参调用上游函数。
     *
     * @param args 调用函数所需的参数。
     * @param <T>  类似于语法糖的泛型参数，允许隐式转型到任意一个类型。
     * @return 函数的返回值。
     */
    @SuppressWarnings("unchecked")
    default <T> T invokeLiteral(Object... args) {
        return (T) invoke0(args);
    }

    Object invoke0(Object[] args);

    Class<?>[] getParamTypes();

    Class<?> getReturnType();
}
