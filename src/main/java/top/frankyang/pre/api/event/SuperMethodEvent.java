package top.frankyang.pre.api.event;

import top.frankyang.pre.api.util.reflection.MethodContainer;

/**
 * 将默认动作委托给一个方法容器，并且可以获取
 *
 * @param <T> 返回值的类型。
 */
public abstract class SuperMethodEvent<T> implements DefaultActionEvent<MethodContainer>, ReturnableEvent<T> {
    private final MethodContainer superMethod;
    private final Object[] args;
    private boolean suppressed = false;
    private T returnValue;

    protected SuperMethodEvent(MethodContainer superMethod, Object... args) {
        this.superMethod = superMethod;
        this.args = args;
    }

    @Override
    public boolean suppressDefaultAction() {
        return suppressed = true;
    }

    @Override
    public boolean defaultActionSuppressed() {
        return suppressed;
    }

    @Override
    public MethodContainer getDefaultAction() {
        return superMethod;
    }

    @Override
    public T getReturnValue() {
        return returnValue;
    }

    @Override
    public void setReturnValue(T t) {
        returnValue = t;
    }

    @Override
    public void afterListeners() {
        if (!suppressed) {
            T value = superMethod.invokeLiteral(args);
            if (returnValue == null)
                returnValue = value;
        }
    }
}
