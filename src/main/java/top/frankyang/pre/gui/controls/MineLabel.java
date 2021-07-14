package top.frankyang.pre.gui.controls;

import top.frankyang.pre.gui.Constants;

import javax.swing.*;
import java.awt.*;

public class MineLabel extends MinePanel {
    private final JLabel label;

    public MineLabel(String text) {
        super();
        label = new JLabel(text);
        label.setOpaque(false);
        label.setBackground(new Color(255, 255, 255, 0));
        label.setForeground(new Color(255, 255, 255, 255));
        label.setFont(Constants.getFont());
        add(label);
    }

    public JLabel getLabel() {
        return label;
    }
}
