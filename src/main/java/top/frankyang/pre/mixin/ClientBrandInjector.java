package top.frankyang.pre.mixin;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandInjector {
    /**
     * @reason ...
     * @author kworker
     */
    @Overwrite
    public static String getClientModName() {
        return "fabric & pythoncraft";
    }
}
