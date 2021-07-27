package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.piston.PistonBehavior;
import org.jetbrains.annotations.Nullable;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.block.color.BasicColor;
import top.frankyang.pre.api.block.color.ColorLike;
import top.frankyang.pre.api.misc.DelegatedConvertable;
import top.frankyang.pre.api.util.TypedDictionary;

/**
 * 包装类，包装原版类<code>Material</code>。
 */
public class BlockMaterial extends DelegatedConvertable<Material> {
    public static final BlockMaterial DEFAULT = new BlockMaterial(Material.STONE);

    protected BlockMaterial(Material delegate) {
        super(delegate);
    }

    /**
     * 将一个Python字典解析为方块材质。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>BlockMaterial</code>实例。
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static BlockMaterial of(@Nullable PyDictionary dictionary) {
        if (dictionary == null) return DEFAULT;

        TypedDictionary dict = new TypedDictionary(dictionary);
        FabricMaterialBuilder fabricMaterialBuilder = new FabricMaterialBuilder(
            dict.getOrCompute("color", ColorLike.class, () ->
                BasicColor.valueOf(dict.getRequired("color", String.class))
            ).convert()
        );
        dict.ifTrue("burnable", fabricMaterialBuilder::burnable);
        dict.ifTrue("lightPassesThrough", fabricMaterialBuilder::lightPassesThrough);
        dict.ifTrue("destroyedByPiston", fabricMaterialBuilder::destroyedByPiston);
        dict.ifTrue("blocksPistons", fabricMaterialBuilder::blocksPistons);
        dict.ifTrue("allowsMovement", fabricMaterialBuilder::allowsMovement);
        dict.ifTrue("liquid", fabricMaterialBuilder::liquid);
        dict.ifTrue("notSolid", fabricMaterialBuilder::notSolid);
        dict.ifTrue("replaceable", fabricMaterialBuilder::replaceable);
        dict.ifPresent("pistonBehavior", String.class, s -> {
            fabricMaterialBuilder.pistonBehavior(PistonBehavior.valueOf(s));
        });

        return new BlockMaterial(fabricMaterialBuilder.build());
    }

    public boolean isLiquid() {
        return delegate.isLiquid();
    }

    public boolean isSolid() {
        return delegate.isSolid();
    }

    public boolean blocksMovement() {
        return delegate.blocksMovement();
    }

    public boolean isBurnable() {
        return delegate.isBurnable();
    }

    public boolean isReplaceable() {
        return delegate.isReplaceable();
    }

    public boolean blocksLight() {
        return delegate.blocksLight();
    }

    public String getPistonBehavior() {
        return delegate.getPistonBehavior().name();
    }

    public MaterialColor getColor() {
        return delegate.getColor();
    }
}
