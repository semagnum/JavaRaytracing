package model.material;

import model.Ray;
import model.obj.HitRecord;

public abstract class Material {

    public abstract ScatterRecord scatter(Ray rIn, HitRecord hitRecord);
}
