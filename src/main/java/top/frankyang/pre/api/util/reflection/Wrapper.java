package top.frankyang.pre.api.util.reflection;

@FunctionalInterface
public interface Wrapper {
    Wrapper DUMMY = args -> null;

    @SuppressWarnings("unchecked")
    default <T> T invoke(Object... args) {
        return (T) invoke0(args);
    }

    Object invoke0(Object[] args);
}
