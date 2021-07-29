package util;

import model.Point3;

import static util.RandomUtil.randomDouble;
import static util.RandomUtil.randomInt;

public class Perlin {
    private static final int POINT_COUNT = 256;
    private final double[] ranFloat;
    private final int[] permX;
    private final int[] permY;
    private final int[] permZ;

    public Perlin() {
        ranFloat = new double[POINT_COUNT];
        for (int i = 0; i < POINT_COUNT; ++i) {
            ranFloat[i] = randomDouble();
        }

        permX = perlinGeneratePerm();
        permY = perlinGeneratePerm();
        permZ = perlinGeneratePerm();
    }

    public double smoothNoise(Point3 p) {
        double u = p.getX() - Math.floor(p.getX());
        double v = p.getY() - Math.floor(p.getY());
        double w = p.getZ() - Math.floor(p.getZ());

        int i = (int) Math.floor(p.getX());
        int j = (int) Math.floor(p.getY());
        int k = (int) Math.floor(p.getZ());
        double[][][] c = new double[2][2][2];

        for (int di=0; di < 2; di++)
            for (int dj=0; dj < 2; dj++)
                for (int dk=0; dk < 2; dk++)
                    c[di][dj][dk] = ranFloat[
                            permX[Math.abs((i+di) % 255)] ^
                                    permY[Math.abs((j+dj) % 255)] ^
                                    permZ[Math.abs((k+dk) % 255)]
                            ];

        return trilinearInterp(c, u, v, w);
    }

    static double trilinearInterp(double[][][] c, double u, double v, double w) {
        double accum = 0.0;
        for (int i=0; i < 2; i++)
            for (int j=0; j < 2; j++)
                for (int k=0; k < 2; k++)
                    accum += (i*u + (1-i)*(1-u))*
                            (j*v + (1-j)*(1-v))*
                            (k*w + (1-k)*(1-w))*c[i][j][k];

        return accum;
    }

    public double noise(Point3 p) {
        int i = Math.abs((int) (4 * p.getX()) % POINT_COUNT);
        int j = Math.abs((int) (4 * p.getY()) % POINT_COUNT);
        int k = Math.abs((int) (4 * p.getZ()) % POINT_COUNT);

        return ranFloat[permX[i] ^ permY[j] ^ permZ[k]];
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
