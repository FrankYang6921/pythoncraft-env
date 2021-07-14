package top.frankyang.pre.gui.controls;

import top.frankyang.pre.gui.Constants;

import javax.swing.*;
import java.awt.*;

import static top.frankyang.pre.gui.Constants.getBtnDisabled;
import static top.frankyang.pre.gui.Constants.getBtnEnabled;

public class MineButton extends JButton {
    public MineButton(String text) {
        super(text);
        setOpaque(true);
        setBackground(new Color(255, 255, 255, 0));
        setForeground(new Color(255, 255, 255, 255));
        setFont(Constants.getFont());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isEnabled()) {
            g.drawImage(getBtnEnabled().getImage(), 0, 0, getWidth(), getHeight(), null);
        } else {
            g.drawImage(getBtnDisabled().getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
