package model.texture;

import model.Color;
import model.Point3;

public abstract class Texture {

    public abstract Color value(double u, double v, Point3 p);
}
