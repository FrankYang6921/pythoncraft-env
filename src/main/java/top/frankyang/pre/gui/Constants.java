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
    private static Font font;
    private static ImageIcon btnDisabled;
    private static ImageIcon btnEnabled;
    private static ImageIcon background;

    public static Font getFont() {
        if (font == null) {
            InputStream stream = Constants.class.getResourceAsStream("/assets/pre/default_font.ttf");
            try {
                return font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(16f);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeIOException(e);
            }
        }
        return font;
    }

    public static ImageIcon getIcon() {
        return icon = requireNonNullElseGet(icon, () -> new ImageIcon(Constants.class.getResource("/assets/pre/icon_trans.png")));
    }

    public static ImageIcon getBtnDisabled() {
        return btnDisabled = requireNonNullElseGet(btnDisabled, () -> new ImageIcon(Constants.class.getResource("/assets/pre/btn_disabled.png")));
    }

    public static ImageIcon getBtnEnabled() {
        return btnEnabled = requireNonNullElseGet(btnEnabled, () -> new ImageIcon(Constants.class.getResource("/assets/pre/btn_enabled.png")));
    }

    public static ImageIcon getBackground() {
        return background = requireNonNullElseGet(background, () -> new ImageIcon(Constants.class.getResource("/assets/pre/background.png")));
    }

    private static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier) {
        return (obj != null) ? obj
            : Objects.requireNonNull(Objects.requireNonNull(supplier, "supplier").get(), "supplier.get()");
    }
}
