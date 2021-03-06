package top.frankyang.pre.mixin.patch;

import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.resource.ResourceType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.frankyang.pre.main.PythonCraft;

import java.util.List;

@Mixin(value = ModResourcePackUtil.class, remap = false)
public class ResourcePackInjector {
    @Inject(method = "appendModResourcePacks", at = @At("TAIL"))
    private static void appendModResourcePacks(List<ModResourcePack> packs, ResourceType type, @Nullable String subPath, CallbackInfo ci) {
        packs.addAll(PythonCraft.getInstance().getPackageManager().getUserResourcePacks());
    }
}
