package model.obj;

import lombok.Data;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.material.BaseMaterial;

import java.lang.Math;

import static model.Vector3.dot;

@Data
public class Sphere extends Hittable {
    private final Point3 center;
    private final double radius;
    private final BaseMaterial material;

    HitRecord hit(Ray r, double t_min, double t_max) {

        Vector3 oc = r.getOrigin().subtract(center);
        double a = r.getDirection().lengthSquared();
        double half_b = dot(oc, r.getDirection());
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) return null;
        double sqrtd = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (-half_b - sqrtd) / a;
        if (root < t_min || t_max < root) {
            root = (-half_b + sqrtd) / a;
            if (root < t_min || t_max < root)
                return null;
        }

        HitRecord hitRecord = new HitRecord();

        hitRecord.setT(root);
        hitRecord.setP(r.at(hitRecord.getT()));
        Vector3 outwardNormal = hitRecord.getP().subtract(center).divide(radius);
        hitRecord.setFaceNormal(r, outwardNormal);
        hitRecord.setMaterial(material);

        return hitRecord;
    }
}