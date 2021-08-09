package top.frankyang.pre.api.math;

import net.minecraft.util.math.Vec3i;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class Vector3i extends DelegatedCastable<Vec3i> implements Vector3<Integer> {
    public Vector3i(Vec3i delegate) {
        super(delegate);
    }

    public Vector3i(int x, int y, int z) {
        this(new Vec3i(x, y, z));
    }

    @Override
    public Integer getX() {
        return delegate.getX();
    }

    @Override
    public Integer getY() {
        return delegate.getY();
    }

    @Override
    public Integer getZ() {
        return delegate.getZ();
    }
}
