package util;

import model.Vector3;

import java.util.Random;

public class RandomUtil {

    private static Random getRandom() {
        return new Random();
    }

    public static double randomDouble() {
        // Returns a random real in [0,1).
        return getRandom().nextDouble();
    }

    public static double randomDouble(double min, double max) {
        return min + (max-min)*randomDouble();
    }

    public static Vector3 randomInUnitDisk() {
        while (true) {
            Vector3 p = new Vector3(randomDouble(-1,1), randomDouble(-1,1), 0);
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vector3 randomInUnitSphere() {
        while (true) {
            Vector3 p = Vector3.random();
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vector3 randomUnitVector() {
        return randomInUnitSphere().unitVector();
    }

}
