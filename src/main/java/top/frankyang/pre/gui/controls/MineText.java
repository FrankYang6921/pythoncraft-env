package top.frankyang.pre.gui.controls;

import top.frankyang.pre.gui.Constants;

import javax.swing.*;
import java.awt.*;

public class MineText extends JPanel {
    private final JTextArea textArea = new JTextArea();

    public MineText(String text) {
        setOpaque(false);
        setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        textArea.setOpaque(false);
        textArea.setSelectedTextColor(new Color(255, 255, 255, 255));
        textArea.setSelectionColor(new Color(255, 255, 255, 0));
        textArea.setEditable(false);
        textArea.setForeground(new Color(255, 255, 255, 255));
        textArea.setBackground(new Color(255, 255, 255, 0));
        textArea.setFont(Constants.getFont());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(text);
        add(textArea, BorderLayout.NORTH);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);
    }
}
