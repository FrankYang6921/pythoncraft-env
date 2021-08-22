package top.frankyang.pre.api.block.state.property;

import net.minecraft.state.property.Property;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class UserProperty<T extends Comparable<T>> extends Property<T> {
    private final Set<T> values;

    @SafeVarargs
    protected UserProperty(String name, Class<T> clazz, T... values) {
        super(name, clazz);
        this.values = Collections.unmodifiableSet(Arrays.stream(values).collect(Collectors.toSet()));
    }

    public static <T extends Comparable<T>> UserProperty<T> of(
        String name,
        Class<T> clazz,
        Function<T, String> writer,
        Function<String, T> reader,
        T[] values
    ) {
        return new UserProperty<T>(name, clazz, values) {
            @Override
            public String name(T value) {
                return writer.apply(value);
            }

            @Override
            public Optional<T> parse(String name) {
                return Optional.ofNullable(reader.apply(name));
            }
        };
    }

    public Collection<T> getValues() {
        return this.values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserProperty<?> that = (UserProperty<?>) o;
        return values.equals(that.values);
    }

    public int computeHashCode() {
        return 31 * super.computeHashCode() + this.values.hashCode();
    }
}
