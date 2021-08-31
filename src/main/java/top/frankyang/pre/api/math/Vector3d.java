package top.frankyang.pre.api.math;

import net.minecraft.util.math.Vec3d;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class Vector3d extends CastableImpl<Vec3d> implements Vector3<Double> {
    public Vector3d(Vec3d delegate) {
        super(delegate);
    }

    public Vector3d(double x, double y, double z) {
        this(new Vec3d(x, y, z));
    }

    public Double getX() {
        return casted.getX();
    }

    public Double getY() {
        return casted.getY();
    }

    public Double getZ() {
        return casted.getZ();
    }
}
