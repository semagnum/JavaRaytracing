package model;

import lombok.Data;
import util.RandomUtil;

import java.lang.Math;

@Data
public class Vector3 {
    private final double x;
    private final double y;
    private final double z;

    public static Vector3 random() {
        return new Vector3(RandomUtil.randomDouble(), RandomUtil.randomDouble(), RandomUtil.randomDouble());
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v) {
        return add(v.negate());
    }

    public Vector3 multiply(Vector3 v) {
        return new Vector3(x * v.getX(), y * v.getY(), z * v.getZ());
    }

    public Vector3 multiply(double d) {
        return new Vector3(x * d, y * d, z * d);
    }

    public Vector3 divide(double d) {
        return multiply(1 / d);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public String toString() {
        return x + " " + y + " " + z;
    }

    public static double dot(Vector3 v1, Vector3 v2) {
        return v1.getX() * v2.getX() +
                v1.getY() * v2.getY() +
                v1.getZ() * v2.getZ();
    }

    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    public Vector3 unitVector() {
        return this.divide(this.length());
    }

    public boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        double s = 1e-8;

        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    public static Vector3 reflect(Vector3 v, Vector3 n) {
        return v.subtract(n.multiply(2*dot(v,n)));
    }

    public static Vector3 refract(Vector3 uv, Vector3 n, double etai_over_etat) {
        double cos_theta = Math.min(dot(uv.negate(), n), 1.0);
        Vector3 r_out_perp =  n.multiply(cos_theta).add(uv).multiply(etai_over_etat); //etai_over_etat * (uv + cos_theta*n)
        Vector3 r_out_parallel = n.multiply(-Math.sqrt(Math.abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.add(r_out_parallel);
    }
}
