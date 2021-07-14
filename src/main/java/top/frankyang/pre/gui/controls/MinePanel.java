package top.frankyang.pre.gui.controls;

import javax.swing.*;
import java.awt.*;

public class MinePanel extends JPanel {
    public MinePanel(Component... components) {
        super();
        setOpaque(false);
        setBackground(new Color(255, 255, 255, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        for (Component i : components) {
            add(i);
        }
    }
}
