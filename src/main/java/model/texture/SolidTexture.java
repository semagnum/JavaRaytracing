package model.texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Color;
import model.Point3;

@Getter
@AllArgsConstructor
public class SolidTexture extends Texture{
    private final Color colorValue;

    public SolidTexture(double r, double g, double b) {
        this(new Color(r,g,b));
    }

    @Override
    public Color value(double u, double v, Point3 p) {
        return colorValue;
    }
}
