package model.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Color;
import model.Ray;
import model.Vector3;
import model.obj.HitRecord;
import model.texture.SolidTexture;
import model.texture.Texture;
import util.RandomUtil;

@Getter
@AllArgsConstructor
public class DiffuseMaterial extends BaseMaterial {
    private final Texture albedo;

    public DiffuseMaterial(Color color) {
        albedo = new SolidTexture(color);
    }

    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        Vector3 scatter_direction = hitRecord.getNormal().add(RandomUtil.randomUnitVector());

        // Catch degenerate scatter direction
        if (scatter_direction.nearZero())
            scatter_direction = hitRecord.getNormal();

        Color attenuation = albedo.value(hitRecord.getUv().getU(), hitRecord.getUv().getV(), hitRecord.getP());

        Ray scattered = new Ray(hitRecord.getP(), scatter_direction, rIn.getTime());

        return new ScatterRecord(true, attenuation, scattered);
    }
}
