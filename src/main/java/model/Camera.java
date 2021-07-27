package model;

import lombok.Getter;

@Getter
public class Camera {
    private final Point3 origin;
    private final Point3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;

    public Camera(
            Point3 lookFrom,
            Point3 lookAt,
            Vector3 vup,
            double vFov, // vertical field-of-view in degrees
            double aspectRatio
    ) {
        double theta = Math.toRadians(vFov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        Vector3 w = lookFrom.subtract(lookAt).unitVector();
        Vector3 u = Vector3.cross(vup, w).unitVector();
        Vector3 v = Vector3.cross(w, u);

        origin = lookFrom;
        horizontal = u.multiply(viewportWidth);
        vertical = v.multiply(viewportHeight);
        lowerLeftCorner = new Point3(origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w));
    }

    public Ray getRay(double u, double v) {
        Vector3 direction = lowerLeftCorner
                .add(horizontal.multiply(u))
                .add(vertical.multiply(v))
                .subtract(origin);
        return new Ray(origin, direction);
    }
};
