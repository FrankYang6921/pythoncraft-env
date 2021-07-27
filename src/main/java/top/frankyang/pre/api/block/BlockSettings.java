package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PySequenceList;
import top.frankyang.pre.api.misc.DelegatedConvertable;
import top.frankyang.pre.api.util.TypedDictionary;

import java.util.function.ToIntFunction;

/**
 * 包装类，包装原版类<code>AbstractBlock.Settings</code>。
 */
public class BlockSettings extends DelegatedConvertable<FabricBlockSettings> {
    protected BlockSettings(FabricBlockSettings delegate) {
        super(delegate);
    }

    /**
     * 将一个Python字典解析为方块设置。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>BlockSettings</code>实例
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static BlockSettings of(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);

        FabricBlockSettings fabricBlockSettings = FabricBlockSettings.of(
            dict.getOrCompute("material", BlockMaterial.class, () ->
                BlockMaterial.of(dict.get("material", PyDictionary.class))
            ).convert()
        );

        dict.ifPresent("sounds", BlockSounds.class, p -> {
            fabricBlockSettings.sounds(p.convert());
        });
        dict.ifPresent("sounds", PyDictionary.class, p -> {
            fabricBlockSettings.sounds(BlockSounds.of(p).convert());
        });
        dict.ifPresent("lightLevel", Integer.class, fabricBlockSettings::lightLevel);
        dict.ifPresent("lightLevel", PyFunction.class, f -> {
            //noinspection unchecked
            fabricBlockSettings.lightLevel(Py.tojava(f, ToIntFunction.class));
        });
        dict.ifPresent("strength", PySequenceList.class, l -> {
            fabricBlockSettings.strength(
                ((Number) l.get(0)).floatValue(), ((Number) l.get(1)).floatValue()
            );
        });
        dict.ifTrue("air", fabricBlockSettings::air);
        dict.ifTrue("ticksRandomly", fabricBlockSettings::ticksRandomly);
        dict.ifTrue("breakInstantly", fabricBlockSettings::breakInstantly);
        dict.ifTrue("dynamicBounds", fabricBlockSettings::dynamicBounds);
        dict.ifTrue("dropsNothing", fabricBlockSettings::dropsNothing);
        dict.ifTrue("requiresTool", fabricBlockSettings::requiresTool);
        dict.ifTrue("noCollision", fabricBlockSettings::noCollision);
        dict.ifTrue("nonOpaque", fabricBlockSettings::nonOpaque);
        dict.ifPresent("collidable", Boolean.class, fabricBlockSettings::collidable);
        dict.ifPresent("breakByHand", Boolean.class, fabricBlockSettings::breakByHand);
        dict.ifPresent("hardness", Number.class, n -> {
            fabricBlockSettings.hardness(n.floatValue());
        });
        dict.ifPresent("resistance", Number.class, n -> {
            fabricBlockSettings.resistance(n.floatValue());
        });
        dict.ifPresent("strength", Number.class, n -> {
            fabricBlockSettings.strength(n.floatValue());
        });
        dict.ifPresent("slipperiness", Number.class, n -> {
            fabricBlockSettings.slipperiness(n.floatValue());
        });
        dict.ifPresent("velocityMultiplier", Number.class, n -> {
            fabricBlockSettings.velocityMultiplier(n.floatValue());
        });
        dict.ifPresent("jumpVelocityMultiplier", Number.class, n -> {
            fabricBlockSettings.jumpVelocityMultiplier(n.floatValue());
        });

        return new BlockSettings(fabricBlockSettings);
    }
}
