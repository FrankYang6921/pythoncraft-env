package top.frankyang.pre.api.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class RichTextImpl extends CastableImpl<MutableText> implements RichText {
    public RichTextImpl(Text delegate) {
        super((MutableText) delegate);  // All `Text` instances are `MutableText` instances. There's no immutable text.
    }

    @Override
    public String toString() {
        return casted.getString();
    }
}
