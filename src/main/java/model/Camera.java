package model;

import lombok.Getter;

@Getter
public class Camera {
    private final Point3 origin;
    private final Point3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;

    public Camera(
            Point3 lookfrom,
            Point3 lookat,
            Vector3 vup,
            double vfov, // vertical field-of-view in degrees
            double aspect_ratio
    ) {
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspect_ratio * viewportHeight;

        Vector3 w = lookfrom.subtract(lookat).unitVector();
        Vector3 u = Vector3.cross(vup, w).unitVector();
        Vector3 v = Vector3.cross(w, u);

        origin = lookfrom;
        horizontal = u.multiply(viewportWidth);
        vertical = v.multiply(viewportHeight);
        lowerLeftCorner = new Point3(origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w));
    }

    public Ray getRay(double u, double v) {
        Vector3 direction = lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v));
        return new Ray(origin, direction);
    }
};
