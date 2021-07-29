package model.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.aabb;
import model.material.BaseMaterial;
import model.texture.UV;

import static model.Vector3.dot;

@Getter
@AllArgsConstructor
public class Sphere extends Hittable {
    private final Point3 center;
    private final double radius;
    private final BaseMaterial material;

    public HitRecord hit(Ray r, double t_min, double t_max) {

        Vector3 oc = r.getOrigin().subtract(center);
        double a = r.getDirection().lengthSquared();
        double half_b = dot(oc, r.getDirection());
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) return null;
        double sqrtD = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (-half_b - sqrtD) / a;
        if (root < t_min || t_max < root) {
            root = (-half_b + sqrtD) / a;
            if (root < t_min || t_max < root)
                return null;
        }

        HitRecord hitRecord = new HitRecord();

        hitRecord.setT(root);
        hitRecord.setP(r.at(hitRecord.getT()));
        Vector3 outwardNormal = hitRecord.getP().subtract(center).divide(radius);
        hitRecord.setFaceNormal(r, outwardNormal);
        hitRecord.setUv(Sphere.getUv(new Point3(outwardNormal)));
        hitRecord.setMaterial(material);

        return hitRecord;
    }

    public aabb boundingBox(double time0, double time1) {
        Vector3 r = new Vector3(radius, radius, radius);
        return new aabb(new Point3(center.subtract(r)), new Point3(center.add(r)));
    }

    /**
     * u: returned value [0,1] of angle around the Y axis from X=-1.
     * v: returned value [0,1] of angle from Y=-1 to Y=+1.
     * <1 0 0> yields <0.50 0.50>
     * <-1 0 0> yields <0.00 0.50>
     * <0 1 0> yields <0.50 1.00>
     * <0 -1 0> yields <0.50 0.00>
     * <0 0 1> yields <0.25 0.50>
     * <0 0 -1> yields <0.75 0.50>
     * @param p: a given point on the sphere of radius one, centered at the origin.
     * @return UV coordinates containing u and v
     */
    public static UV getUv(Point3 p) {

        double theta = Math.acos(-p.getY());
        double phi = Math.atan2(-p.getZ(), p.getX()) + Math.PI;

        double u = phi / (2 * Math.PI);
        double v = theta / Math.PI;
        return new UV(u, v);
    }
}
