package top.frankyang.pre.mixin;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface ClientWindowAccessor {
    @Accessor("windowedX")
    void setWindowedX(int value);

    @Accessor("windowedY")
    void setWindowedY(int value);

    @Invoker("updateWindowRegion")
    void callUpdateWindowRegion();
}
