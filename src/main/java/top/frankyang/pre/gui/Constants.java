package top.frankyang.pre.gui;

import top.frankyang.pre.loader.exceptions.RuntimeIOException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

public final class Constants {
    public static final int PADDING_WIDTH = 16;
    public static final int PADDING_HEIGHT = 16;
    private static ImageIcon icon;

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ImageIcon getIcon() {
        return icon = requireNonNullElseGet(icon, () -> new ImageIcon(Objects.requireNonNull(Constants.class.getResource("/assets/pre/icon_trans.png"))));
    }

    private static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier) {
        return (obj != null) ? obj
            : Objects.requireNonNull(Objects.requireNonNull(supplier, "supplier").get(), "supplier.get()");
    }
}
