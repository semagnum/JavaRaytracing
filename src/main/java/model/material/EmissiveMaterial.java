package model.material;

import lombok.AllArgsConstructor;
import model.Color;
import model.Point3;
import model.Ray;
import model.obj.HitRecord;
import model.texture.SolidTexture;
import model.texture.Texture;

@AllArgsConstructor
public class EmissiveMaterial extends Material  {
    private final Texture emitTexture;

    public EmissiveMaterial(Color c) {
        emitTexture = new SolidTexture(c);
    }

    @Override
    public ScatterRecord scatter(Ray rIn, HitRecord hitRecord) {
        return new ScatterRecord(false, null, null);
    }

    @Override
    public Color emitted(double u, double v, Point3 p) {
        return emitTexture.value(u, v, p);
    }
}