package model.material;

import model.Color;
import model.Ray;
import model.obj.HitRecord;

public class BaseMaterial {

    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        throw new UnsupportedOperationException();
    }
}
