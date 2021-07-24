package top.frankyang.pre.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Supplier;

@Mixin(RenderSystem.class)
public class RenderSystemInjector {
    /**
     * @reason ...
     * @author kworker
     */
    @Overwrite
    public static void assertThread(Supplier<Boolean> check) {
    }
}
