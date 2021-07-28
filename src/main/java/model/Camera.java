package model;

import lombok.Getter;
import util.RandomUtil;

@Getter
public class Camera {
    private final Point3 origin;
    private final Point3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;
    private final Vector3 u;
    private final Vector3 v;
    private final Vector3 w;
    private final double lensRadius;

    public Camera(
            Point3 lookFrom,
            Point3 lookAt,
            Vector3 vup,
            double vFov, // vertical field-of-view in degrees
            double aspectRatio,
            double aperture,
            double focusDist
    ) {
        double theta = Math.toRadians(vFov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        w = lookFrom.subtract(lookAt).unitVector();
        u = Vector3.cross(vup, w).unitVector();
        v = Vector3.cross(w, u);

        origin = lookFrom;
        horizontal = u.multiply(viewportWidth * focusDist);
        vertical = v.multiply(viewportHeight * focusDist);
        lowerLeftCorner = new Point3(origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w.multiply(focusDist)));

        lensRadius = aperture / 2;
    }

    public Ray getRay(double s, double t) {
        Vector3 rd = RandomUtil.randomInUnitDisk().multiply(lensRadius);
        Vector3 offset = u.multiply(rd.getX()).add(v.multiply(rd.getY()));

        Vector3 direction = lowerLeftCorner
                .add(horizontal.multiply(s))
                .add(vertical.multiply(t))
                .subtract(origin).subtract(offset);
        Point3 o = new Point3(origin.add(offset));
        return new Ray(o, direction);
    }
}
