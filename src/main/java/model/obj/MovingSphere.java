package model.obj;

import lombok.Getter;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.material.BaseMaterial;

import static model.Vector3.dot;

@Getter
public class MovingSphere extends Sphere {
    private final Point3 center2; // where sphere arrives at timeEnd
    private final double timeStart; // time sphere starts moving
    private final double timeEnd; // time sphere ends moving

    public MovingSphere(Point3 center, double radius, BaseMaterial material, Point3 center2, double timeStart, double timeEnd) {
        super(center, radius, material);
        this.center2 = center2;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    // center0 + ((time - time0) / (time1 - time0))*(center1 - center0);
    public Point3 getCenter(double time) {
        return new Point3((center2
                .subtract(getCenter()))
                .multiply((time - timeStart) / (timeEnd - timeStart))
                .add(getCenter()));
    }

    HitRecord hit(Ray r, double t_min, double t_max) {

        Vector3 oc = r.getOrigin().subtract(getCenter(r.getTime()));
        double a = r.getDirection().lengthSquared();
        double half_b = dot(oc, r.getDirection());
        double c = oc.lengthSquared() - getRadius() * getRadius();

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
        Vector3 outwardNormal = hitRecord.getP().subtract(getCenter(r.getTime())).divide(getRadius());
        hitRecord.setFaceNormal(r, outwardNormal);
        hitRecord.setMaterial(getMaterial());

        return hitRecord;
    }
}
