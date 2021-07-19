package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PySequenceList;
import top.frankyang.pre.api.util.MagicConstants;
import top.frankyang.pre.api.util.TypedDictionary;

import java.util.function.ToIntFunction;

public final class BlockSettings {
    private static final MagicConstants<Material> materials = new MagicConstants<>(Material.class);
    private static final MagicConstants<MaterialColor> materialColors = new MagicConstants<>(MaterialColor.class);
    private static final MagicConstants<BlockSoundGroup> blockSoundGroups = new MagicConstants<>(BlockSoundGroup.class);

    private BlockSettings() {
    }

    /**
     * 将一个Python字典解析为方块设置。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>FabricBlockSettings</code>实例
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static FabricBlockSettings parse(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);

        Material material = materials.getOrDefault(
            dict.get("material", String.class),
            dict.get("material", Material.class)
        );  // Allows a magic constant or an instance.
        FabricBlockSettings fabricBlockSettings = FabricBlockSettings.of(material);

        dict.ifPresent("materialColor", String.class, s -> {
            fabricBlockSettings.materialColor(materialColors.get(s));
        });
        dict.ifPresent("materialColor", MaterialColor.class, fabricBlockSettings::materialColor);
        dict.ifPresent("materialColor", DyeColor.class, fabricBlockSettings::materialColor);
        // Allows a magic constant or an instance or an enum element
        if (dict.getOrDefault("noCollision", Boolean.class, false)) {
            fabricBlockSettings.noCollision();
        }
        if (dict.getOrDefault("nonOpaque", Boolean.class, false)) {
            fabricBlockSettings.nonOpaque();
        }
        dict.ifPresent("slipperiness", Double.class, d -> {
            fabricBlockSettings.slipperiness(d.floatValue());
        });
        dict.ifPresent("velocityMultiplier", Double.class, d -> {
            fabricBlockSettings.velocityMultiplier(d.floatValue());
        });
        dict.ifPresent("jumpVelocityMultiplier", Double.class, d -> {
            fabricBlockSettings.jumpVelocityMultiplier(d.floatValue());
        });
        dict.ifPresent("sounds", String.class, s -> {
            fabricBlockSettings.sounds(blockSoundGroups.get(s));
        });
        dict.ifPresent("sounds", BlockSoundGroup.class, fabricBlockSettings::sounds);
        // Allows a magic constant or an instance.
        dict.ifPresent("lightLevel", Integer.class, fabricBlockSettings::lightLevel);
        dict.ifPresent("lightLevel", PyFunction.class, f -> {
            //noinspection unchecked
            fabricBlockSettings.lightLevel(Py.tojava(f, ToIntFunction.class));
        });
        // Allows a literal or a lambda.
        dict.ifPresent("strength", PySequenceList.class, l -> {
            fabricBlockSettings.strength((Float) l.get(0), (Float) l.get(1));
        });
        dict.ifPresent("strength", Double.class, d -> {
            fabricBlockSettings.strength(d.floatValue());
        });
        if (dict.getOrDefault("ticksRandomly", Boolean.class, false)) {
            fabricBlockSettings.ticksRandomly();
        }
        if (dict.getOrDefault("breakInstantly", Boolean.class, false)) {
            fabricBlockSettings.breakInstantly();
        }
        if (dict.getOrDefault("dynamicBounds", Boolean.class, false)) {
            fabricBlockSettings.dynamicBounds();
        }
        if (dict.getOrDefault("dropsNothing", Boolean.class, false)) {
            fabricBlockSettings.dropsNothing();
        }
        if (dict.getOrDefault("air", Boolean.class, false)) {
            fabricBlockSettings.air();
        }
        dict.ifPresent("hardness", Double.class, d -> {
            fabricBlockSettings.hardness(d.floatValue());
        });
        dict.ifPresent("resistance", Double.class, d -> {
            fabricBlockSettings.resistance(d.floatValue());
        });
        if (dict.getOrDefault("requiresTool", Boolean.class, false)) {
            fabricBlockSettings.requiresTool();
        }
        dict.ifPresent("collidable", Boolean.class, fabricBlockSettings::collidable);
        dict.ifPresent("breakByHand", Boolean.class, fabricBlockSettings::breakByHand);

        return fabricBlockSettings;
    }
}
