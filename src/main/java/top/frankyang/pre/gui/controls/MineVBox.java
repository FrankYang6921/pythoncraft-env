package top.frankyang.pre.gui.controls;

import javax.swing.*;
import java.awt.*;

public class MineVBox extends Box {
    private final Box inner = Box.createVerticalBox();
    private final Dimension padding;

    public MineVBox(Component... components) {
        this(new Dimension(0, 0), components);
    }

    public MineVBox(Dimension padding, Component... components) {
        super(BoxLayout.X_AXIS);
        this.padding = padding;
        setOpaque(false);
        inner.setOpaque(false);

        inner.add(Box.createVerticalStrut((int) padding.getHeight()));
        for (Component i : components) {
            inner.add(i);
            inner.add(Box.createVerticalStrut((int) padding.getHeight()));
        }
        super.add(Box.createHorizontalStrut((int) padding.getWidth()));
        super.add(inner);
        super.add(Box.createHorizontalStrut((int) padding.getWidth()));
    }

    @Override
    public Component add(Component component) {
        inner.add(component);
        inner.add(Box.createVerticalStrut((int) padding.getHeight()));
        return component;
    }
}
