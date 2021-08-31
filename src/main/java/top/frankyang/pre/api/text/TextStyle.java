package top.frankyang.pre.api.text;

import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import top.frankyang.pre.api.math.RgbColor;
import top.frankyang.pre.api.misc.conversion.CastableImpl;
import top.frankyang.pre.api.util.CopyUtils;
import top.frankyang.pre.mixin.reflect.StyleAccessor;

import java.util.Objects;

public class TextStyle extends CastableImpl<Style> {
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
        return new RgbColor(Objects.requireNonNull(casted.getColor()).getRgb());
    }

    public boolean isBold() {
        return casted.isBold();
    }

    public boolean isItalic() {
        return casted.isItalic();
    }

    public boolean isStrikethrough() {
        return casted.isStrikethrough();
    }

    public boolean isUnderlined() {
        return casted.isUnderlined();
    }

    public boolean isObfuscated() {
        return casted.isObfuscated();
    }

    public String getFontId() {
        return casted.getFont().toString();
    }

    public TextStyle color(RgbColor color) {
        return new TextStyle(casted.withColor(TextColor.fromRgb(color.getRgb())));
    }

    public TextStyle bold(boolean bold) {
        return new TextStyle(casted.withBold(bold));
    }

    public TextStyle italic(boolean italic) {
        return new TextStyle(casted.withItalic(italic));
    }

    public TextStyle strikethrough(boolean strikethrough) {
        Style delegate = CopyUtils.shallowCopy(this.casted);
        ((StyleAccessor) delegate).setStrikethrough(strikethrough);
        return new TextStyle(delegate);
    }

    public TextStyle underlined(boolean underlined) {
        Style delegate = CopyUtils.shallowCopy(this.casted);
        ((StyleAccessor) delegate).setUnderlined(underlined);
        return new TextStyle(delegate);
    }

    public TextStyle obfuscated(boolean obfuscated) {
        Style delegate = CopyUtils.shallowCopy(this.casted);
        ((StyleAccessor) delegate).setObfuscated(obfuscated);
        return new TextStyle(delegate);
    }

    public TextStyle fontId(String fontId) {
        return new TextStyle(casted.withFont(new Identifier(fontId)));
    }

    public boolean isPlain() {
        return casted.isEmpty();
    }
}
