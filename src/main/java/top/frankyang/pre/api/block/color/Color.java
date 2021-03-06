package top.frankyang.pre.api.block.color;

import net.minecraft.block.MaterialColor;
import top.frankyang.pre.api.math.RgbColor;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link MaterialColor}。
 */
public class Color extends CastableImpl<MaterialColor> implements ColorLike {
    protected Color(MaterialColor delegate) {
        super(delegate);
    }

    /**
     * 从Minecraft可用的64种地图颜色中挑选出最合适的一种。
     *
     * @param r 红色值。
     * @param g 绿色值。
     * @param b 蓝色值。
     * @return 最接近的颜色。
     */
    private static MaterialColor findNearest(int r, int g, int b) {
        MaterialColor bestColor = null;
        int bestDiff = Integer.MAX_VALUE;

        for (MaterialColor color : MaterialColor.COLORS) {
            if (color == null) continue;
            int thisR = (color.color & 0xff0000) >> 16;
            int thisG = (color.color & 0x00ff00) >> 8;
            int thisB = color.color & 0x0000ff;
            int diff = (Math.abs(r - thisR) + Math.abs(g - thisG) + Math.abs(b - thisB)) / 3;
            if (diff < bestDiff) {
                bestColor = color;
                bestDiff = diff;
            }
        }

        return bestColor;
    }

    /**
     * 通过一个可解析为十六进制颜色的字符串构造颜色对象。
     *
     * @param string 十六进制颜色字符串。
     * @return 所构造的颜色对象。
     * @throws IllegalArgumentException 如果任一颜色值超出了[0, 255]的范围，或者字符串无法被解析为十六进制颜色。
     */
    public static Color of(String string) {
        return of(RgbColor.of(string).getRgb());
    }

    /**
     * 通过三个0~255的颜色值构造颜色对象。
     *
     * @param r 红色值。
     * @param g 绿色值。
     * @param b 蓝色值。
     * @return 所构造的颜色对象。
     * @throws IllegalArgumentException 如果任一颜色值超出了[0, 255]的范围。
     */
    public static Color of(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("Invalid color: must be in range [0, 255].");
        }

        return new Color(findNearest(r, g, b));
    }

    public static Color of(int rawColor) {
        int r = rawColor >> 16;
        int g = (rawColor & 0x00ffff) >> 8;
        int b = rawColor & 0xff;
        return of(r, g, b);
    }
}
