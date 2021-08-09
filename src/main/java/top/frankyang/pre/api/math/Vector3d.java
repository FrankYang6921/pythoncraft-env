package top.frankyang.pre.api.math;

import net.minecraft.util.math.Vec3d;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class Vector3d extends DelegatedCastable<Vec3d> implements Vector3<Double> {
    public Vector3d(Vec3d delegate) {
        super(delegate);
    }

    public Vector3d(double x, double y, double z) {
        this(new Vec3d(x, y, z));
    }

    public Double getX() {
        return delegate.getX();
    }

    public Double getY() {
        return delegate.getY();
    }

    public Double getZ() {
        return delegate.getZ();
    }
}
