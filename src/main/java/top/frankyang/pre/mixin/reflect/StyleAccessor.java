package top.frankyang.pre.mixin.reflect;

import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Style.class)
public interface StyleAccessor {
    @Accessor
    void setStrikethrough(Boolean value);

    @Accessor
    void setUnderlined(Boolean value);

    @Accessor
    void setObfuscated(Boolean value);
}
