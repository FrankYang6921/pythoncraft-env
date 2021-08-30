package top.frankyang.pre.api.math;

public class RgbColor {
    private final int rgb;

    public RgbColor(int rgb) {
        if (rgb > 0xffffff) {
            throw new IllegalArgumentException("Invalid color: must be in range [0, 16777215].");
        }

        this.rgb = rgb;
    }

    public static RgbColor of(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("Invalid color: must be in range [0, 255].");
        }

        return new RgbColor((r << 16) + (g << 8) + b);
    }

    public static RgbColor of(String string) {
        // Strip leading '0x' or '#'
        if (string.startsWith("0x")) {
            if (string.length() != 8) {
                throw new IllegalArgumentException("Invalid hex color string: " + string);
            }
            string = string.substring(2, 8);
        } else if (string.startsWith("#")) {
            if (string.length() != 7) {
                throw new IllegalArgumentException("Invalid hex color string: " + string);
            }
            string = string.substring(1, 7);
        } else if (string.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color string: " + string);
        }

        return new RgbColor(Integer.parseInt(string, 16));
    }

    public int getRgb() {
        return rgb;
    }

    public int getRed() {
        return rgb >> 16;
    }

    public int getGreen() {
        return (rgb & 0xffff) >> 8;
    }

    public int getBlue() {
        return rgb & 0xff;
    }
}
