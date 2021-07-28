package model.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Color;
import model.Ray;
import model.Vector3;
import model.obj.HitRecord;
import util.RandomUtil;

@Getter
@AllArgsConstructor
public class DiffuseMaterial extends BaseMaterial {
    private final Color albedo;

    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        Vector3 scatter_direction = hitRecord.getNormal().add(RandomUtil.randomUnitVector());

        // Catch degenerate scatter direction
        if (scatter_direction.nearZero())
            scatter_direction = hitRecord.getNormal();

        Ray scattered = new Ray(hitRecord.getP(), scatter_direction);

        return new ScatterRecord(true, albedo, scattered);
    }
}
