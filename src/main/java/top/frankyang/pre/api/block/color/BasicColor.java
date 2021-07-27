package top.frankyang.pre.api.block.color;

import net.minecraft.block.MaterialColor;

/**
 * 枚举类，表示原版的16种颜色，分别是：白色、橙色、品红色、浅蓝色、黄色、浅绿色、粉色、灰色、浅灰色、青色、紫色、蓝色、棕色、绿色、红色、黑色。
 */
public enum BasicColor implements ColorLike {
    WHITE(MaterialColor.WHITE),
    ORANGE(MaterialColor.ORANGE),
    MAGENTA(MaterialColor.MAGENTA),
    LIGHT_BLUE(MaterialColor.LIGHT_BLUE),
    YELLOW(MaterialColor.YELLOW),
    LIME(MaterialColor.LIME),
    PINK(MaterialColor.PINK),
    GRAY(MaterialColor.GRAY),
    LIGHT_GRAY(MaterialColor.LIGHT_GRAY),
    CYAN(MaterialColor.CYAN),
    PURPLE(MaterialColor.PURPLE),
    BLUE(MaterialColor.BLUE),
    BROWN(MaterialColor.BROWN),
    GREEN(MaterialColor.GREEN),
    RED(MaterialColor.RED),
    BLACK(MaterialColor.BLACK);

    private final MaterialColor delegate;

    BasicColor(MaterialColor delegate) {
        this.delegate = delegate;
    }

    @Override
    public MaterialColor convert() {
        return delegate;
    }
}
