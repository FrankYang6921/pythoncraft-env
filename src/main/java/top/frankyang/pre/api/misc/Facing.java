package top.frankyang.pre.api.misc;

import net.minecraft.util.math.Direction;

/**
 * 包装类，包装原版类{@link Direction}
 */
public enum Facing implements Castable<Direction> {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    private final Direction delegate;

    Facing(Direction delegate) {
        this.delegate = delegate;
    }

    @Override
    public Direction cast() {
        return delegate;
    }
}
