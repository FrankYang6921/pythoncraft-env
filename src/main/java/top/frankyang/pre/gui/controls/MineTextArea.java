package top.frankyang.pre.gui.controls;

import top.frankyang.pre.gui.Constants;

import javax.swing.*;
import java.awt.*;

public class MineTextArea extends JTextArea {
    public MineTextArea() {
        super();
        setFont(Constants.getFont());
        setLineWrap(false);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }
}
