package model.obj;

import lombok.Data;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.material.BaseMaterial;

import static model.Vector3.dot;

@Data
public class HitRecord {
    Point3 p;
    Vector3 normal;
    BaseMaterial material;
    double t;
    boolean frontFace;

    void setFaceNormal(Ray r, Vector3 outwardNormal) {
        frontFace = dot(r.getDirection(), outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.negate();
    }
}
