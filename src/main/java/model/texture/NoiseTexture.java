package model.texture;

import model.Color;
import model.Point3;
import util.Perlin;

public class NoiseTexture extends Texture {
    private final Perlin noise;
    private final double scale;

    public NoiseTexture(double scale) {
        noise = new Perlin();
        this.scale = scale;
    }

    @Override
    public Color value(double u, double v, Point3 p) {
        double n = 0.5 * (1 + Math.sin(scale*p.getZ() + 10*noise.turb(p)));
        return new Color(new Color (1,1,1).multiply(n));
    }
}
