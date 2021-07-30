package model.obj;

import lombok.AllArgsConstructor;
import model.Point3;
import model.Ray;
import model.Vector3;
import model.aabb;
import model.material.Material;
import model.texture.UV;

@AllArgsConstructor
public class XYRect extends Hittable {
    double x0, x1, y0, y1, k;
    Material material;
    private final static double PADDING = 0.0001;

    @Override
    public HitRecord hit(Ray r, double tMin, double tMax) {
        double t = (k-r.getOrigin().getZ()) / r.getDirection().getZ();
        if (t < tMin || t > tMax)
            return null;
        double x = r.getOrigin().getX() + t*r.getDirection().getX();
        double y = r.getOrigin().getY() + t*r.getDirection().getY();
        if (x < x0 || x > x1 || y < y0 || y > y1)
            return null;
        HitRecord hitRecord = new HitRecord();
        UV uv = new UV((x-x0)/(x1-x0), (y-y0)/(y1-y0));
        hitRecord.setUv(uv);
        hitRecord.setT(t);
        Vector3 outwardNormal = new Vector3(0, 0, 1);
        hitRecord.setFaceNormal(r, outwardNormal);
        hitRecord.setMaterial(material);
        hitRecord.setP(r.at(t));
        return hitRecord;
    }

    @Override
    public aabb boundingBox(double time0, double time1) {
        return new aabb(new Point3(x0,y0, k-PADDING), new Point3(x1, y1, k+PADDING));
    }
}
