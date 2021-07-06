package top.frankyang.pre.mixin;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.frankyang.pre.main.PythonCraft;

import java.util.List;

@Mixin(ModResourcePackUtil.class)
public class ResourcePackMixin {
    @Inject(method = "appendModResourcePacks", at = @At("TAIL"), remap = false)
    private static void appendModResourcePacks(List<ResourcePack> packList, ResourceType type, CallbackInfo info) {
        packList.addAll(PythonCraft.getInstance().getPackageManager().getUserResourcePacks());
    }
}
