package model;

import lombok.Data;

@Data
public class Ray {
    private final Point3 origin;
    private final Vector3 direction;

    public Point3 at(double t) {
        return new Point3(origin.add(direction.multiply(t)));
    }
}
