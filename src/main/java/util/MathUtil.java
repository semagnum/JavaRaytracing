package util;

public class MathUtil {

    public static double clamp(double x, double min, double max) {
        if (x < min) return min;
        return Math.min(x, max);
    }
}
