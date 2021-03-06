package model.obj;

import lombok.Getter;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.aabb;
import model.material.Material;

import static model.Vector3.dot;
import static model.aabb.surroundingBox;

@Getter
public class MovingSphere extends Sphere {
    private final Point3 center2; // where sphere arrives at timeEnd
    private final double timeStart; // time sphere starts moving
    private final double timeEnd; // time sphere ends moving

    public MovingSphere(Point3 center, double radius, Material material, Point3 center2, double timeStart, double timeEnd) {
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

    public HitRecord hit(Ray r, double tMin, double tMax) {

        Vector3 oc = r.getOrigin().subtract(getCenter(r.getTime()));
        double a = r.getDirection().lengthSquared();
        double half_b = dot(oc, r.getDirection());
        double c = oc.lengthSquared() - getRadius() * getRadius();

        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) return null;
        double sqrtD = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (-half_b - sqrtD) / a;
        if (root < tMin || tMax < root) {
            root = (-half_b + sqrtD) / a;
            if (root < tMin || tMax < root)
                return null;
        }

        HitRecord hitRecord = new HitRecord();

        hitRecord.setT(root);
        hitRecord.setP(r.at(hitRecord.getT()));
        Vector3 outwardNormal = hitRecord.getP().subtract(getCenter(r.getTime())).divide(getRadius());
        hitRecord.setFaceNormal(r, outwardNormal);
        hitRecord.setUv(Sphere.getUv(new Point3(outwardNormal)));
        hitRecord.setMaterial(getMaterial());

        return hitRecord;
    }

    public aabb boundingBox(double time0, double time1) {
        Vector3 r = new Vector3(getRadius(), getRadius(), getRadius());
        aabb box0 = new aabb(new Point3(getCenter(time0).subtract(r)), new Point3(getCenter(time0).add(r)));
        aabb box1 = new aabb(new Point3(getCenter(time1).subtract(r)), new Point3(getCenter(time1).add(r)));
        return surroundingBox(box0, box1);
    }
}
