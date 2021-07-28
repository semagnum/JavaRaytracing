package model.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Color;
import model.Ray;
import model.Vector3;
import model.obj.HitRecord;
import util.RandomUtil;

import static model.Vector3.*;

@Getter
@AllArgsConstructor
public class DielectricMaterial extends BaseMaterial {
    private final double indexOfRefraction;

    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        Color attenuation = new Color(1.0, 1.0, 1.0);
        double refraction_ratio = hitRecord.isFrontFace() ? (1.0 / indexOfRefraction ) : indexOfRefraction;

        Vector3 unit_direction = rIn.getDirection().unitVector();
        double cos_theta = Math.min(dot(unit_direction.negate(), hitRecord.getNormal()), 1.0);
        double sin_theta = Math.sqrt(1.0 - cos_theta*cos_theta);

        boolean cannot_refract = refraction_ratio * sin_theta > 1.0;
        Vector3 direction;

        if (cannot_refract || reflectance(cos_theta, refraction_ratio) > RandomUtil.randomDouble())
            direction = reflect(unit_direction, hitRecord.getNormal());
        else
            direction = refract(unit_direction, hitRecord.getNormal(), refraction_ratio);

        Ray scattered = new Ray(hitRecord.getP(), direction, rIn.getTime());
        return new ScatterRecord(true, attenuation, scattered);
    }

    private static double reflectance(double cosine, double ref_idx) {
        // Use Schlick's approximation for reflectance.
        double r0 = (1-ref_idx) / (1+ref_idx);
        r0 = r0*r0;
        return r0 + (1-r0) * Math.pow((1 - cosine),5);
    }
}
