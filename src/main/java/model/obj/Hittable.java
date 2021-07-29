package model.obj;

import model.Ray;
import model.aabb;

public abstract class Hittable {
    public abstract HitRecord hit(Ray r, double t_min, double t_max);
    public abstract aabb boundingBox(double time0, double time1);
}
