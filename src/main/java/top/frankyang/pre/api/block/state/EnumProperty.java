package top.frankyang.pre.api.block.state;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.state.property.Property;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class EnumProperty<T extends Enum<T>> extends Property<T> {
    private final ImmutableSet<T> values;
    private final Map<String, T> byName = new HashMap<>();

    protected EnumProperty(String name, Class<T> type, Collection<T> values) {
        super(name, type);
        this.values = ImmutableSet.copyOf(values);

        for (T t : values) {
            String string = minecraftNameOf(t);
            if (this.byName.containsKey(string)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + string + "'");
            }

            this.byName.put(string, t);
        }
    }

    public static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> type) {
        return of(name, type, t -> true);
    }

    public static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> type, Predicate<T> filter) {
        return of(name, type, Arrays.stream(type.getEnumConstants()).filter(filter).collect(Collectors.toList()));
    }

    @SafeVarargs
    public static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> type, T... values) {
        return of(name, type, Lists.newArrayList(values));
    }

    public static <T extends Enum<T>> EnumProperty<T> of(String name, Class<T> type, Collection<T> values) {
        return new EnumProperty<>(name, type, values);
    }

    private static String minecraftNameOf(Enum<?> e) {
        String s = e.toString();
        Pattern p = Pattern.compile("([A-Z])([a-z])");
        Matcher m = p.matcher(s);
        while (m.find()) {
            s = s.replace(m.group(0), '_' + m.group(1).toLowerCase() + m.group(2));
        }
        p = Pattern.compile("([a-z])([A-Z])");
        m = p.matcher(s);
        while (m.find()) {
            s = s.replace(m.group(0), m.group(1) + '_' + m.group(2).toLowerCase());
        }
        s = s
            .toLowerCase()
            .replaceAll("_+", "_")
            .replaceAll("^_", "")
            .replaceAll("_$", "")
            .replace("$", "__dollar__")
            .replace("1", "__one__")
            .replace("2", "__two__")
            .replace("3", "__thr__")
            .replace("4", "__four__")
            .replace("5", "__five__")
            .replace("6", "__six__")
            .replace("7", "__seven__")
            .replace("8", "__eight__")
            .replace("9", "__nine__")
            .replace("0", "__zero__")
            .replaceAll("___+", "__");
        return s;
    }

    public Collection<T> getValues() {
        return this.values;
    }

    public Optional<T> parse(String name) {
        return Optional.ofNullable(this.byName.get(name));
    }

    public String name(T t) {
        return minecraftNameOf(t);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof EnumProperty && super.equals(object)) {
            EnumProperty<?> enumProperty = (EnumProperty<?>) object;
            return this.values.equals(enumProperty.values) && this.byName.equals(enumProperty.byName);
        } else {
            return false;
        }
    }

    public int computeHashCode() {
        int i = super.computeHashCode();
        i = 31 * i + this.values.hashCode();
        i = 31 * i + this.byName.hashCode();
        return i;
    }
}
