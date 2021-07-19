package top.frankyang.pre.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.frankyang.pre.main.PythonCraft;

@Mixin(TitleScreen.class)
public abstract class TitleScreenInjector extends Screen {
    protected TitleScreenInjector(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo info) {
        buttons.get(buttons.size() - 1).setWidth(98);
        addButton(new ButtonWidget(
            this.width / 2 + 2,
            y + spacingY * (buttons.size() - 1),
            98,
            20,
            new LiteralText("PythonCraft 模组"),
            button -> PythonCraft.getInstance().getPackageManager().openManagerFrame()
        ));
    }
}
