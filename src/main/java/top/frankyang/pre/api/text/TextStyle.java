package top.frankyang.pre.api.text;

import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import top.frankyang.pre.api.math.RgbColor;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.util.CopyUtils;
import top.frankyang.pre.mixin.reflect.StyleAccessor;

import java.util.Objects;

public class TextStyle extends DelegatedCastable<Style> {
    private static final TextStyle PLAIN = new TextStyle(Style.EMPTY);

    private TextStyle(Style delegate) {
        super(delegate);
    }  // TODO implement it!

    public static TextStyle ofPlain() {
        return PLAIN;
    }

    public static TextStyle of(Style delegate) {
        return delegate.isEmpty() ? PLAIN : new TextStyle(delegate);
    }

    public RgbColor getColor() {
        return new RgbColor(Objects.requireNonNull(delegate.getColor()).getRgb());
    }

    public boolean isBold() {
        return delegate.isBold();
    }

    public boolean isItalic() {
        return delegate.isItalic();
    }

    public boolean isStrikethrough() {
        return delegate.isStrikethrough();
    }

    public boolean isUnderlined() {
        return delegate.isUnderlined();
    }

    public boolean isObfuscated() {
        return delegate.isObfuscated();
    }

    public String getFontId() {
        return delegate.getFont().toString();
    }

    public TextStyle color(RgbColor color) {
        return new TextStyle(delegate.withColor(TextColor.fromRgb(color.getRgb())));
    }

    public TextStyle bold(boolean bold) {
        return new TextStyle(delegate.withBold(bold));
    }

    public TextStyle italic(boolean italic) {
        return new TextStyle(delegate.withItalic(italic));
    }

    public TextStyle strikethrough(boolean strikethrough) {
        Style delegate = CopyUtils.shallowCopy(this.delegate);
        ((StyleAccessor) delegate).setStrikethrough(strikethrough);
        return new TextStyle(delegate);
    }

    public TextStyle underlined(boolean underlined) {
        Style delegate = CopyUtils.shallowCopy(this.delegate);
        ((StyleAccessor) delegate).setUnderlined(underlined);
        return new TextStyle(delegate);
    }

    public TextStyle obfuscated(boolean obfuscated) {
        Style delegate = CopyUtils.shallowCopy(this.delegate);
        ((StyleAccessor) delegate).setObfuscated(obfuscated);
        return new TextStyle(delegate);
    }

    public TextStyle fontId(String fontId) {
        return new TextStyle(delegate.withFont(new Identifier(fontId)));
    }

    public boolean isPlain() {
        return delegate.isEmpty();
    }
}
