package model.obj;

import model.Ray;
import model.aabb;

public abstract class Hittable {
    public abstract HitRecord hit(Ray r, double tMin, double tMax);
    public abstract aabb boundingBox(double time0, double time1);
}
