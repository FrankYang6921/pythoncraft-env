package top.frankyang.pre.gui;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import top.frankyang.pre.loader.core.PackageInfo;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.util.EasterEggs;

import java.awt.*;
import java.nio.file.Paths;

class PackageInfoEntry extends WGridPanel {
    private static final PackageInfo INFO =
        new PackageInfo("PythonCraft API", "为绝大多数包提供API。", false, Paths.get("不可用"), new Identifier("pre:icon_white.png"), null);

    public PackageInfoEntry(PackageInfo info) {
        super();
        update(info);
    }

    public PackageInfoEntry() {
        super();
        update(INFO);
    }

    public void update(PackageInfo info) {
        children.clear();

        add(new WSprite(info.getThumbnail()), 0, 0, 3, 3);
        WLabel label1 = new WLabel("名称：" + info.getNameString());
        label1.setVerticalAlignment(VerticalAlignment.CENTER);
        WLabel label2 = new WLabel("简介：" + info.getInfoString());
        label2.setVerticalAlignment(VerticalAlignment.CENTER);
        WLabel label3 = new WLabel("文件名：" + info.getFileName());
        label3.setVerticalAlignment(VerticalAlignment.CENTER);
        add(label1, 3, 0);
        add(label2, 3, 1);
        add(label3, 3, 2);

        add(new WToggleButton(Text.of("启用")) {{
            setToggle(!info.isDisabled());
            setOnToggle(on -> {
                if (info == INFO) {
                    setLabel(Text.of("禁用？"));
                    EasterEggs.playRedZone();
                    setToggle(!on);
                    return;
                }
                PackageInfo newInfo = null;
                try {
                    if (on && info.isDisabled()) {
                        newInfo = info.enable();
                    }
                    if (!(on || info.isDisabled())) {
                        newInfo = info.disable();
                    }
                } catch (Exception e) {
                    PythonCraft.getInstance().getLogger().warn(e);
                    setToggle(!on);
                    return;
                }
                if (newInfo != null) {
                    update(newInfo);
                }
            });
        }}, 14, 0);

        add(new WButton(Text.of("打开目录")) {{
            if (info == INFO) setEnabled(false);
            setOnClick(() -> {
                try {
                    Desktop.getDesktop().open(info.getPackageDir());
                } catch (Exception e) {
                    PythonCraft.getInstance().getLogger().warn(e);
                    setEnabled(false);
                }
            });
        }}, 14, 1, 3, 1);

        add(new WButton(Text.of("详细信息")) {{
            if (info.isPlaceholder()) setEnabled(false);
            setOnClick(() -> PackageDetailGui.open(info));
        }}, 14, 2, 3, 1);
    }
}
