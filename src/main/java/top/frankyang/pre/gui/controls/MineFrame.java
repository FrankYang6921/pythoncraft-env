package top.frankyang.pre.gui.controls;

import top.frankyang.pre.gui.Constants;

import javax.swing.*;
import java.awt.*;

public class MineFrame extends JFrame {
    public MineFrame() {
        setContentPane(new PanelWithBackground());
        setIconImage(Constants.getIcon().getImage());
        setTitle("PythonCraft Runtime Environment");
    }

    private static class PanelWithBackground extends MinePanel {
        private static final int BG_SIZE = 64;

        public PanelWithBackground(Component... components) {
            super(components);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        @Override
        protected void paintComponent(Graphics g) {
            int xParts = getWidth() / BG_SIZE + 1;
            int yParts = getHeight() / BG_SIZE + 1;
            for (int i = 0; i < xParts; i++) {
                for (int j = 0; j < yParts; j++) {
                    g.drawImage(Constants.getBackground().getImage(), BG_SIZE * i, BG_SIZE * j, BG_SIZE, BG_SIZE, null);
                }
            }
            super.paintComponent(g);
        }
    }
}
