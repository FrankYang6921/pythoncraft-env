package top.frankyang.pre.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Version extends Number implements Comparable<Version> {
    public static final Version ANY = new Version("*.*.*.*");
    private final long a, b, c, d;

    public Version(String versionString) {
        String[] strings = versionString.split("\\.");
        a = strings[0].equals("*") ? -1 : Short.parseShort(strings[0]);
        if (strings.length > 1)
            b = strings[1].equals("*") ? -1 : Short.parseShort(strings[1]);
        else
            b = 0;
        if (strings.length > 2)
            c = strings[2].equals("*") ? -1 : Short.parseShort(strings[2]);
        else
            c = 0;
        if (strings.length > 3)
            d = strings[3].equals("*") ? -1 : Short.parseShort(strings[3]);
        else
            d = 0;
    }

    @Override
    public String toString() {
        if (b == 0 && c == 0 && d == 0)
            return "v" + (a < 0 ? "*" : a);
        else if (c == 0 && d == 0)
            return "v" + (a < 0 ? "*" : a) + '.' + (b < 0 ? "*" : b);
        else if (d == 0)
            return "v" + (a < 0 ? "*" : a) + '.' + (b < 0 ? "*" : b) + '.' + (c < 0 ? "*" : c);
        else
            return "v" + (a < 0 ? "*" : a) + '.' + (b < 0 ? "*" : b) + '.' + (c < 0 ? "*" : c) + '.' + (d < 0 ? "*" : d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return compareTo(version) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d);
    }

    @Override
    public int compareTo(@NotNull Version o) {
        if (a >= 0 && o.a >= 0) {
            return Long.compare(a, o.a);
        }
        if (b >= 0 && o.b >= 0) {
            return Long.compare(b, o.b);
        }
        if (c >= 0 && o.c >= 0) {
            return Long.compare(c, o.c);
        }
        if (d >= 0 && o.d >= 0) {
            return Long.compare(d, o.d);
        }
        return 0;
    }

    @Override
    public int intValue() {
        return (int) ((a << 16) + b);
    }

    @Override
    public long longValue() {
        return (a << 48) + (b << 32) + (c << 16) + d;
    }

    @Override
    public float floatValue() {
        return longValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }
}
