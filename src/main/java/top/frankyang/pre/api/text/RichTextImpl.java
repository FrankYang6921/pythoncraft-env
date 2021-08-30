package top.frankyang.pre.api.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class RichTextImpl extends DelegatedCastable<MutableText> implements RichText {
    public RichTextImpl(Text delegate) {
        super((MutableText) delegate);  // All `Text` instances are `MutableText` instances. There's no immutable text.
    }

    @Override
    public String toString() {
        return delegate.getString();
    }
}
