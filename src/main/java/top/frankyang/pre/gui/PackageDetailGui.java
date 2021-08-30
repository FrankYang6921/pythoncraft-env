package top.frankyang.pre.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.client.gui.screen.Screen;
import top.frankyang.pre.api.util.GameUtils;
import top.frankyang.pre.loader.core.PackageInfo;
import top.frankyang.pre.util.Versions;

public class PackageDetailGui extends LightweightGuiDescription {
    private PackageDetailGui(PackageInfo info) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        int i = 0;
        root.add(new WLabel("详细信息（" + info.getNameString() + '）'), 0, i++, 18, 0);
        root.add(new WLabel("模组标识符：" + info.getMetaData().getIdentifier()), 0, i++);
        root.add(new WLabel("压缩包路径：" + GameUtils.getGamePath().relativize(info.getPackageSrc())), 0, i++, 18, 0);
        root.add(new WLabel(
            "版本号：" + Versions.toString(info.getMetaData().getPackageVersion())), 0, i++
        );
        root.add(new WLabel(
            "兼容的Fabric Loader版本：" + Versions.toString(info.getMetaData().getFabricVersion())), 0, i++
        );
        root.add(new WLabel(
            "兼容的PythonCraftRE版本：" + Versions.toString(info.getMetaData().getLoaderVersion())), 0, i++
        );
        root.add(new WLabel(
            "兼容的Minecraft版本：" + Versions.toString(info.getMetaData().getGameVersion())), 0, i
        );

        root.validate(this);
    }

    public static void open(PackageInfo info) {
        GameUtils.getClient().openScreen(new PackageDetailScreen(new PackageDetailGui(info)));
    }

    private static class PackageDetailScreen extends CottonClientScreen {
        private final Screen parent = GameUtils.getClient().currentScreen;

        public PackageDetailScreen(GuiDescription description) {
            super(description);
        }

        @Override
        public void onClose() {
            GameUtils.getClient().openScreen(parent);
        }
    }
}
