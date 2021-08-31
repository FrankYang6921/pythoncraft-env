package top.frankyang.pre.api.math;

import net.minecraft.util.math.Vec3i;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class Vector3i extends CastableImpl<Vec3i> implements Vector3<Integer> {
    public Vector3i(Vec3i delegate) {
        super(delegate);
    }

    public Vector3i(int x, int y, int z) {
        this(new Vec3i(x, y, z));
    }

    @Override
    public Integer getX() {
        return casted.getX();
    }

    @Override
    public Integer getY() {
        return casted.getY();
    }

    @Override
    public Integer getZ() {
        return casted.getZ();
    }
}
