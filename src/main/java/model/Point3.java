package model;

public class Point3 extends Vector3 {
    public Point3(double x, double y, double z) {
        super(x, y, z);
    }

    public Point3(Vector3 v) {
        super(v.getX(), v.getY(), v.getZ());
    }
}
