package top.frankyang.pre.api.block.color;

import net.minecraft.block.MaterialColor;
import top.frankyang.pre.api.misc.conversion.Castable;

/**
 * 包装类接口，包装原版类{@link MaterialColor}。
 */
public interface ColorLike extends Castable<MaterialColor> {
    /**
     * 获取这个颜色在Minecraft中的原生ID。应当是0~63的整数。
     *
     * @return 原生ID。
     */
    default int getRawId() {
        return cast().id;
    }

    /**
     * 获取这个颜色的粗略颜色值，表示为一个24位的整数，其中最高8位是红色值，中间8位是绿色值，最低8位是蓝色值。
     *
     * @return 粗略颜色。
     */
    default int getColor() {
        return cast().color;
    }
}
