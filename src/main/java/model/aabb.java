package model;

import lombok.Data;

@Data
public class aabb {
    private final Point3 minimum;
    private final Point3 maximum;

    public boolean hit(Ray r, double tMin, double tMax) {
        for (int a = 0; a < 3; a++) {
            double invD = 1.0f / r.getDirection().at(a);
            double t0 = (minimum.at(a) - r.getOrigin().at(a)) * invD;
            double t1 = (maximum.at(a) - r.getOrigin().at(a)) * invD;
            if (invD < 0.0f) {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }
            tMin = Math.max(t0, tMin);
            tMax = Math.min(t1, tMax);
            if (tMax <= tMin)
                return false;
        }
        return true;
    }

    public static aabb surroundingBox(aabb box0, aabb box1) {
        Point3 small = new Point3(Math.min(box0.getMinimum().getX(), box1.getMinimum().getX()),
                Math.min(box0.getMinimum().getY(), box1.getMinimum().getY()),
                Math.min(box0.getMinimum().getZ(), box1.getMinimum().getZ()));

        Point3 big = new Point3(Math.max(box0.getMaximum().getX(), box1.getMaximum().getX()),
                Math.max(box0.getMaximum().getY(), box1.getMaximum().getY()),
                Math.max(box0.getMaximum().getZ(), box1.getMaximum().getZ()));

        return new aabb(small,big);
    }
}