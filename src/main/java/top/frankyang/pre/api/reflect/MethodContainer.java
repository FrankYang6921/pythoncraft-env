package top.frankyang.pre.api.reflect;

/**
 * 方法容器。该类用于包装一个方法调用。
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
     * 调用上游函数。
     *
     * @param args 调用函数所需的参数。
     * @param <T>  允许转型到任意类型。
     * @return 函数的返回值。
     */
    @SuppressWarnings("unchecked")
    default <T> T invoke(Object... args) {
        return (T) invoke0(args);
    }

    Object invoke0(Object[] args);

    Class<?>[] getParamTypes();

    Class<?> getReturnType();
}
