package util;

import model.Point3;
import model.Vector3;

import static model.Vector3.dot;
import static util.RandomUtil.randomDouble;
import static util.RandomUtil.randomInt;

public class Perlin {
    private static final int POINT_COUNT = 256;
    private final Vector3[] ranVec;
    private final int[] permX;
    private final int[] permY;
    private final int[] permZ;

    public Perlin() {
        ranVec = new Vector3[POINT_COUNT];
        for (int i = 0; i < POINT_COUNT; ++i) {
            ranVec[i] = Vector3.random(-1, 1).unitVector();
        }

        permX = perlinGeneratePerm();
        permY = perlinGeneratePerm();
        permZ = perlinGeneratePerm();
    }

    public double noise(Point3 p) {
        double u = p.getX() - Math.floor(p.getX());
        double v = p.getY() - Math.floor(p.getY());
        double w = p.getZ() - Math.floor(p.getZ());

        int i = (int) Math.floor(p.getX());
        int j = (int) Math.floor(p.getY());
        int k = (int) Math.floor(p.getZ());
        Vector3[][][] c = new Vector3[2][2][2];

        for (int di=0; di < 2; di++)
            for (int dj=0; dj < 2; dj++)
                for (int dk=0; dk < 2; dk++)
                    c[di][dj][dk] = ranVec[
                            permX[Math.abs((i+di) % 255)] ^
                                    permY[Math.abs((j+dj) % 255)] ^
                                    permZ[Math.abs((k+dk) % 255)]
                            ];

        return perlinInterp(c, u, v, w);
    }

    public double turb(Point3 p) {
        return turb(p, 7);
    }

    public double turb(Point3 p, int depth) {
        double accum = 0.0;
        Point3 temp_p = p;
        double weight = 1.0;

        for (int i = 0; i < depth; i++) {
            accum += weight*noise(temp_p);
            weight *= 0.5;
            temp_p = new Point3(temp_p.multiply(2));
        }

        return Math.abs(accum);
    }

    static double perlinInterp(Vector3[][][] c, double u, double v, double w) {
        double uu = u*u*(3-2*u);
        double vv = v*v*(3-2*v);
        double ww = w*w*(3-2*w);
        double accum = 0.0;

        for (int i=0; i < 2; i++)
            for (int j=0; j < 2; j++)
                for (int k=0; k < 2; k++) {
                    Vector3 weightV = new Vector3(u-i, v-j, w-k);
                    accum += (i*uu + (1-i)*(1-uu))
                            * (j*vv + (1-j)*(1-vv))
                            * (k*ww + (1-k)*(1-ww))
                            * dot(c[i][j][k], weightV);
                }

        return accum;
    }

    static int[] perlinGeneratePerm() {
        int[] p = new int[POINT_COUNT];

        for (int i = 0; i < POINT_COUNT; i++)
            p[i] = i;

        return permute(p);
    }

    static int[] permute(int[] p) {
        for (int i = Perlin.POINT_COUNT -1; i > 0; i--) {
            int target = randomInt(0, i);
            int tmp = p[i];
            p[i] = p[target];
            p[target] = tmp;
        }
        return p;
    }
}
