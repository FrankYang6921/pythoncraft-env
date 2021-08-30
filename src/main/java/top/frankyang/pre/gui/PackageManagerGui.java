package top.frankyang.pre.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.fabricmc.fabric.api.util.TriState;
import top.frankyang.pre.api.util.GameUtils;
import top.frankyang.pre.loader.core.PackageInfo;

import java.util.stream.Stream;

public final class PackageManagerGui extends LightweightGuiDescription {
    private PackageManagerGui(Stream<PackageInfo> stream) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        WBox main = new WBox(Axis.VERTICAL);
        main.add(new PackageInfoEntry());
        stream.forEach(
            info -> main.add(new PackageInfoEntry(info))
        );

        WScrollPanel mainWrapper = new WScrollPanel(main);
        mainWrapper.setScrollingHorizontally(TriState.FALSE);

        root.add(mainWrapper, 0, 0, 18, 12);
        root.validate(this);
    }

    public static void open(Stream<PackageInfo> stream) {
        GameUtils.getClient().openScreen(new PackageManagerScreen(new PackageManagerGui(stream)));
    }

    private static class PackageManagerScreen extends CottonClientScreen {
        public PackageManagerScreen(GuiDescription description) {
            super(description);
        }
    }
}
