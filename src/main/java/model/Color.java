package model;

import util.MathUtil;

public class Color extends Vector3 {
    public Color(double r, double g, double b) {
        super(r, g, b);
    }

    public Color() {
        super(0.0, 0.0, 0.0);
    }

    public Color(Vector3 v) {
        super(v.getX(), v.getY(), v.getZ());
    }

    public String write(int samples) {


        double scale = 1.0 / samples;
        double r = Math.sqrt(getX() * scale);
        double g = Math.sqrt(getY() * scale);
        double b = Math.sqrt(getZ() * scale);


        int ir = (int) (255.999 * MathUtil.clamp(r, 0.0, 0.999));
        int ig = (int) (255.999 * MathUtil.clamp(g, 0.0, 0.999));
        int ib = (int) (255.999 * MathUtil.clamp(b, 0.0, 0.999));

        return ir + " " + ig + " " + ib;
    }

    public static Color skyColor(double t) {
        Color white = new Color(1.0, 1.0, 1.0);
        Color blue = new Color(0.5, 0.7, 1.0);
        return new Color(white.multiply(1.0-t).add(blue.multiply(t)));
    }
}
