package top.frankyang.pre.api.math;

import net.minecraft.util.math.Direction;
import top.frankyang.pre.api.misc.Castable;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

/**
 * 包装类，包装原版类{@link Direction}。
 */
public enum Facing implements Castable<Direction> {
    UP(Direction.UP),
    DOWN(Direction.DOWN),
    NORTH(Direction.NORTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST),
    SOUTH(Direction.SOUTH);

    private final Direction delegate;

    Facing(Direction delegate) {
        this.delegate = delegate;
    }

    public static Facing of(Direction direction) {
        switch (direction) {
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case WEST:
                return WEST;
            case SOUTH:
                return SOUTH;
            default:
                throw new ImpossibleException();
        }
    }

    @Override
    public Direction cast() {
        return delegate;
    }
}
