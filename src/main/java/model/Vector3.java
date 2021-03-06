package model;

import lombok.Data;
import util.RandomUtil;

import java.lang.Math;

@Data
public class Vector3 {
    private final double x;
    private final double y;
    private final double z;

    public double at(int axis) {
        switch(axis) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public static Vector3 random() {
        return new Vector3(RandomUtil.randomDouble(), RandomUtil.randomDouble(), RandomUtil.randomDouble());
    }

    public static Vector3 random(double min, double max) {
        return new Vector3(RandomUtil.randomDouble(min, max), RandomUtil.randomDouble(min, max), RandomUtil.randomDouble(min, max));
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(x - v.getX(), y - v.getY(), z - v.getZ());
    }

    public Vector3 multiply(Vector3 v) {
        return new Vector3(x * v.getX(), y * v.getY(), z * v.getZ());
    }

    public Vector3 multiply(double d) {
        return new Vector3(x * d, y * d, z * d);
    }

    public Vector3 divide(double d) {
        return this.multiply(1 / d);
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
        double s = 1e-7;

        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    public static Vector3 reflect(Vector3 v, Vector3 n) {
        return v.subtract(n.multiply(2*dot(v,n)));
    }

    public static Vector3 refract(Vector3 uv, Vector3 n, double etaiOverEtat) {
        double cos_theta = Math.min(dot(uv.negate(), n), 1.0);
        Vector3 rOutPerp =  n.multiply(cos_theta).add(uv).multiply(etaiOverEtat); //etai_over_etat * (uv + cos_theta*n)
        Vector3 rOutParallel = n.multiply(-Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared())));
        return rOutPerp.add(rOutParallel);
    }
}
