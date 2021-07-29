package model.texture;

import lombok.AllArgsConstructor;
import model.Color;
import model.Point3;

@AllArgsConstructor
public class CheckerTexture extends Texture {
    private final Texture odd;
    private final Texture even;

    public CheckerTexture(Color odd, Color even) {
        this.odd = new SolidTexture(odd);
        this.even = new SolidTexture(even);
    }

    @Override
    public Color value(double u, double v, Point3 p) {
        double sines = Math.sin(10*p.getX())*Math.sin(10*p.getY())*Math.sin(10*p.getZ());
        if (sines < 0)
            return odd.value(u, v, p);
        else
            return even.value(u, v, p);
    }
}