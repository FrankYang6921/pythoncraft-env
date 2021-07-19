package top.frankyang.pre.misc;

public class LambdaContainer<T> {
    private volatile T value;

    public LambdaContainer() {
    }

    public LambdaContainer(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
