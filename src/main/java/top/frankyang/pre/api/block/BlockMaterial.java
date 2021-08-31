package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;
import top.frankyang.pre.api.block.color.ColorLike;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link Material}。
 */
public class BlockMaterial extends CastableImpl<Material> {
    protected BlockMaterial(Material delegate) {
        super(delegate);
    }

    /**
     * 获取一种方块材质。
     *
     * @param color 方块材质的颜色。
     */
    public static Builder of(ColorLike color) {
        return new Builder(new FabricMaterialBuilder(color.cast()));
    }

    public boolean isLiquid() {
        return casted.isLiquid();
    }

    public boolean isSolid() {
        return casted.isSolid();
    }

    public boolean blocksMovement() {
        return casted.blocksMovement();
    }

    public boolean isBurnable() {
        return casted.isBurnable();
    }

    public boolean isReplaceable() {
        return casted.isReplaceable();
    }

    public boolean blocksLight() {
        return casted.blocksLight();
    }

    public String getPistonBehavior() {
        return casted.getPistonBehavior().name();
    }

    public ColorLike getColor() {
        return casted::getColor;
    }

    public static class Builder {
        private final FabricMaterialBuilder fabricMaterialBuilder;

        private Builder(FabricMaterialBuilder fabricMaterialBuilder) {
            this.fabricMaterialBuilder = fabricMaterialBuilder;
        }

        public Builder burnable() {
            fabricMaterialBuilder.burnable();
            return this;
        }

        public Builder pistonBehavior(String behavior) {
            fabricMaterialBuilder.pistonBehavior(PistonBehavior.valueOf(behavior));
            return this;
        }

        public Builder lightPassesThrough() {
            fabricMaterialBuilder.lightPassesThrough();
            return this;
        }

        public Builder destroyedByPiston() {
            fabricMaterialBuilder.destroyedByPiston();
            return this;
        }

        public Builder blocksPistons() {
            fabricMaterialBuilder.blocksPistons();
            return this;
        }

        public Builder allowsMovement() {
            fabricMaterialBuilder.allowsMovement();
            return this;
        }

        public Builder liquid() {
            fabricMaterialBuilder.liquid();
            return this;
        }

        public Builder notSolid() {
            fabricMaterialBuilder.notSolid();
            return this;
        }

        public Builder replaceable() {
            fabricMaterialBuilder.replaceable();
            return this;
        }

        public BlockMaterial build() {
            return new BlockMaterial(fabricMaterialBuilder.build());
        }
    }
}
