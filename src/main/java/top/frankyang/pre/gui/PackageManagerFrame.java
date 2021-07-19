package top.frankyang.pre.gui;

import top.frankyang.pre.gui.controls.MineButton;
import top.frankyang.pre.gui.controls.MineLabel;
import top.frankyang.pre.gui.controls.MinePanel;
import top.frankyang.pre.gui.controls.MineVBox;
import top.frankyang.pre.loader.core.PackageInfo;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.stream.Stream;

public final class PackageManagerFrame extends CloseWaitingFrame {
    private static PackageManagerFrame instance;
    private final MineButton enable;
    private final MineButton disable;
    private final MineButton config;
    private final MineButton folder;
    private ModEntry selection;

    private PackageManagerFrame(Stream<PackageInfo> packageInfoStream) {
        super();

        setSize(800, 600);
        setResizable(false);

        MineLabel label = new MineLabel("模组管理");

        JPanel inner = new JPanel();
        inner.setBackground(Color.BLACK);
        inner.setOpaque(true);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JScrollPane outer = new JScrollPane(inner);
        packageInfoStream.map(ModEntry::new).forEach(inner::add);

        enable = new MineButton("启用");
        disable = new MineButton("禁用");
        config = new MineButton("配置");
        folder = new MineButton("打开文件夹");
        enable.setEnabled(false);
        disable.setEnabled(false);
        config.setEnabled(false);
        folder.setEnabled(false);
        MineButton close = new MineButton("关闭");
        close.addActionListener(e -> closeWindow());
        enable.addActionListener(e -> {
            selection.updateInfo(selection.info.enable());
            updateSelection();
        });
        disable.addActionListener(e -> {
            selection.updateInfo(selection.info.disable());
            updateSelection();
        });
        folder.addActionListener(ev -> {
            try {
                Desktop.getDesktop().open(selection.info.getPackageDir());
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        });

        MinePanel buttons = new MinePanel(enable, disable, config, folder, close);

        add(new MineVBox(
            new Dimension(Constants.PADDING_WIDTH, Constants.PADDING_HEIGHT),
            label, outer, buttons
        ));

        setVisible(true);
    }

    public static void open(Stream<PackageInfo> packageInfoList) {
        (instance = new PackageManagerFrame(packageInfoList)).waitForClose();
    }

    public static void raise() {
        if (instance != null && instance.isVisible()) instance.requestFocus();
    }

    private void setSelection(ModEntry selection) {
        if (selection == this.selection) return;
        if (this.selection != null)
            this.selection.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selection.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.selection = selection;
        updateSelection();
    }

    private void updateSelection() {
        enable.setEnabled(selection.info.isDisabled());
        disable.setEnabled(!selection.info.isDisabled());
        config.setEnabled(!selection.info.isDisabled());
        folder.setEnabled(true);
    }

    private static class FillingLabel extends MineLabel {
        public FillingLabel(String text) {
            super(text);
            setMaximumSize(
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
            );
            getLabel().setMaximumSize(
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
            );
            getLabel().setHorizontalTextPosition(SwingConstants.LEFT);
            getLabel().setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    private class ModEntry extends MinePanel {
        public PackageInfo info;

        public ModEntry(PackageInfo info) {
            super();

            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            setMinimumSize(new Dimension(0, 96));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setSelection(ModEntry.this);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            updateInfo(info);
        }

        public void updateInfo(PackageInfo info) {
            this.info = info;
            removeAll();
            JLabel thumbnail = new JLabel(
                new ImageIcon(info
                    .getThumbnail()
                    .getImage()
                    .getScaledInstance(
                        84, 84, Image.SCALE_FAST
                    )
                )
            );

            thumbnail.setMinimumSize(new Dimension(84, 84));
            thumbnail.setPreferredSize(new Dimension(84, 84));
            thumbnail.setMaximumSize(new Dimension(84, 84));
            add(thumbnail, new GridBagConstraints() {{
                gridx = 0;
                gridy = 0;
                gridwidth = RELATIVE;
                anchor = WEST;
                fill = NONE;
            }});

            MinePanel inner = new MinePanel();
            inner.setLayout(new GridBagLayout() {{
                columnWeights = new double[]{1d};
            }});

            GridBagConstraints constraints = new GridBagConstraints() {{
                gridx = 0;
                gridy = 0;
                gridwidth = REMAINDER;
                anchor = WEST;
                fill = HORIZONTAL;
            }};

            FillingLabel label = new FillingLabel(info.getFullName());
            inner.add(label, constraints);
            constraints.gridy++;

            label = new FillingLabel("描述：" + info.getDescription());
            label.getLabel().setForeground(new Color(170, 170, 170));
            inner.add(label, constraints);
            constraints.gridy++;

            label = new FillingLabel("来源：" + info.getFileName());
            label.getLabel().setForeground(new Color(170, 170, 170));
            inner.add(label, constraints);

            add(inner, new GridBagConstraints() {{
                gridx = 1;
                gridy = 0;
                gridwidth = REMAINDER;
                anchor = WEST;
                fill = HORIZONTAL;
            }});

            inner.setMinimumSize(new Dimension(624, 96));
            inner.setPreferredSize(new Dimension(624, 96));
            inner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
            validate();
        }
    }
}
