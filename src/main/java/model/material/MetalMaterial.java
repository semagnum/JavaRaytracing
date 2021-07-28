package model.material;

import lombok.Getter;
import model.Color;
import model.Ray;
import model.Vector3;
import model.obj.HitRecord;
import util.RandomUtil;

import static model.Vector3.dot;

@Getter
public class MetalMaterial extends BaseMaterial {
    private final Color albedo;
    private final double fuzz;

    public MetalMaterial(Color color, double fuzz) {
        albedo = color;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        Vector3 reflected = Vector3.reflect(rIn.getDirection().unitVector(), hitRecord.getNormal());
        Ray scattered = new Ray(hitRecord.getP(), reflected.add(RandomUtil.randomInUnitSphere().multiply(fuzz)));
        boolean isScattered = (dot(scattered.getDirection(), hitRecord.getNormal()) > 0);
        return new ScatterRecord(isScattered, albedo, scattered);
    }


}
