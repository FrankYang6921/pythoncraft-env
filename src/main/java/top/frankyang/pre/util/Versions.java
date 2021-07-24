package top.frankyang.pre.util;

import net.fabricmc.loader.util.version.SemanticVersionImpl;

public final class Versions {
    public static final SemanticVersionImpl ANY = of("x.x.x");

    private Versions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static SemanticVersionImpl of(String string) {
        try {
            return new SemanticVersionImpl(string, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SemanticVersionImpl ofNullable(String string) {
       if (string == null)
           return ANY;
       return of(string);
    }

    public static String toString(SemanticVersionImpl version) {
        if (version == ANY) {
            return "不可用";
        }
        return version.getFriendlyString();
    }
}
