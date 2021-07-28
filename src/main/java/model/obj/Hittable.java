package model.obj;

import model.Ray;
import model.aabb;

public class Hittable {
    public HitRecord hit(Ray r, double t_min, double t_max) {
        throw new UnsupportedOperationException();
    }
    public aabb boundingBox(double time0, double time1) {
        throw new UnsupportedOperationException();
    }
}
