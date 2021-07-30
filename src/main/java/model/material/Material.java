package model.material;

import model.Color;
import model.Point3;
import model.Ray;
import model.obj.HitRecord;

public abstract class Material {

    public abstract ScatterRecord scatter(Ray rIn, HitRecord hitRecord);

    public Color emitted(double u, double v, Point3 p) {
        return new Color(0,0,0);
    }
}
