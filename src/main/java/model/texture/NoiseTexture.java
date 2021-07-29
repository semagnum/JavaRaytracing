package model.texture;

import lombok.AllArgsConstructor;
import model.Color;
import model.Point3;
import util.Perlin;

@AllArgsConstructor
public class NoiseTexture extends Texture {
    private final Perlin noise;
    private final boolean smoothNoise;

    @Override
    public Color value(double u, double v, Point3 p) {
        double n = smoothNoise ? noise.smoothNoise(p) : noise.noise(p);
        return new Color(new Color(1,1,1).multiply(n));
    }
}
