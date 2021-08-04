package top.frankyang.pre.api.block.type;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import top.frankyang.pre.api.misc.Convertable;
import top.frankyang.pre.api.util.reflection.DynamicOverride;
import top.frankyang.pre.api.util.reflection.DynamicOverrider;
import top.frankyang.pre.api.util.reflection.Wrapper;

public class BaseBlockType extends DynamicOverrider<Block> {
    public BaseBlockType(Object settings) {
        this(Block.class, settings);
    }

    public BaseBlockType(Class<? extends Block> targetClass, Object settings) {
        super(
            targetClass,
            new Class[]{AbstractBlock.Settings.class},
            Convertable.convert(
                settings, AbstractBlock.Settings.class
            )
        );
    }

    @DynamicOverride
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state, Wrapper superMethod) {
        superMethod.invoke(world, pos, state);
    }

    @DynamicOverride
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, Wrapper superMethod) {
        superMethod.invoke(world, pos, explosion);
    }

    @DynamicOverride
    public void onSteppedOn(World world, BlockPos pos, Entity entity, Wrapper superMethod) {
        superMethod.invoke(world, pos, entity);
    }

    @DynamicOverride
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Wrapper superMethod) {
        superMethod.invoke(world, pos, state, placer, itemStack);
    }

    @DynamicOverride
    public void onLandedUpon(World world, BlockPos pos, Entity entity, Float distance, Wrapper superMethod) {
        superMethod.invoke(world, pos, entity, distance);
    }

    @DynamicOverride
    public void onEntityLand(BlockView world, Entity entity, Wrapper superMethod) {
        superMethod.invoke(world, entity);
    }

    @DynamicOverride
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, Wrapper superMethod) {
        superMethod.invoke(world, pos, state, player);
    }

    @DynamicOverride
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, Wrapper superMethod) {
        superMethod.invoke(world, player, pos, state, blockEntity, stack);
    }

    @DynamicOverride
    public ActionResult onUse(World world, PlayerEntity player, Hand hand, BlockHitResult hit, Wrapper superMethod) {
        System.out.println("Overriding!! On use!!");
        return superMethod.invoke(world, player, hand, hit);
    }
}
