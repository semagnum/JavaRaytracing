import model.*;
import model.material.*;
import model.obj.*;
import model.texture.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import static util.RandomUtil.randomDouble;

public class Raytracer {

    private static HittableList earth() {
        ImageTexture earth_texture = new ImageTexture("earthmap.jpg");
        DiffuseMaterial earth_surface = new DiffuseMaterial(earth_texture);
        Sphere globe = new Sphere(new Point3(0,0,0), 2, earth_surface);

        return new HittableList(globe);
    }

    private static Hittable createScene() {
        HittableList world = new HittableList();

        CheckerTexture checker = new CheckerTexture(new Color(0.2, 0.3, 0.1), new Color(0.9, 0.9, 0.9));
        DiffuseMaterial ground_material = new DiffuseMaterial(checker);
        world.add(new Sphere(new Point3(0,-1000,0), 1000, ground_material));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = randomDouble();
                Point3 center = new Point3(a + 0.9*randomDouble(), 0.2, b + 0.9*randomDouble());
                Point3 p = new Point3(4, 0.2, 0);
                if ((center.subtract(p)).length() > 0.9) {
                    Material sphere_material;

                    if (chooseMat < 0.8) {
                        // diffuse
                        Color albedo = new Color(Color.random().multiply(Color.random()));
                        sphere_material = new DiffuseMaterial(albedo);
                        Point3 center2 = new Point3(new Point3(0, randomDouble(0,.5), 0).add(center));
                        world.add(new MovingSphere(center, 0.2, sphere_material, center2, 0.0, 1.0));
                    } else if (chooseMat < 0.95) {
                        // metal
                        Color albedo = new Color(Color.random(0.5, 1));
                        double fuzz = randomDouble(0, 0.5);
                        sphere_material = new MetalMaterial(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphere_material));
                    } else {
                        // glass
                        sphere_material = new DielectricMaterial(1.5);
                        world.add(new Sphere(center, 0.2, sphere_material));
                    }
                }
            }
        }

        DielectricMaterial material1 = new DielectricMaterial(1.5);
        world.add(new Sphere(new Point3(0, 1, 0), 1.0, material1));

        DiffuseMaterial material2 = new DiffuseMaterial(new Color(0.4, 0.2, 0.1));
        world.add(new Sphere(new Point3(-4, 1, 0), 1.0, material2));

        MetalMaterial material3 = new MetalMaterial(new Color(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Point3(4, 1, 0), 1.0, material3));

        return new bvhNode(world, 0.0, 1.0);
    }

    private static Color rayColor(Ray r, Hittable world, int depth) {
        if (depth <= 0) {
            return new Color();
        }

        HitRecord hitRecord;
        if ((hitRecord = world.hit(r, 0.001, 1000.0)) != null) {
            ScatterRecord sr = hitRecord.getMaterial().scatter(r, hitRecord);
            if (sr.isScattered()) {
                Ray scattered = sr.getScatterRay();
                Color attenuation = sr.getAttenuation();
                return new Color(rayColor(scattered, world, depth-1).multiply(attenuation));
            }
            return new Color();
        }

        // if nothing hit, get sky
        Vector3 unit_direction = r.getDirection().unitVector();
        double t = 0.5 * (unit_direction.getY() + 1.0);
        return Color.skyColor(t);
    }

    public static void main(String[] args) {

        double aspectRatio = 3.0 / 2.0;
        int imageWidth = 600;
        int imageHeight = (int) (imageWidth / aspectRatio);

        // World
        Hittable world = earth();

        Point3 lookFrom = new Point3(13,2,3);
        Point3 lookAt = new Point3(0,0,0);
        Vector3 vUp = new Vector3(0,1,0);
        double distToFocus = 10.0;
        double aperture = 0.1;

        Camera camera = new Camera(lookFrom, lookAt, vUp, 20, aspectRatio, aperture, distToFocus, 0.0, 1.0);


        // Render
        int samplesPerPixel = 50;
        int maxDepth = 15;
        PrintWriter writer = null;
        Color[][] image = new Color[imageHeight][imageWidth];

        long startTime = System.nanoTime();

        for (int j = imageHeight - 1; j >= 0; --j) {
            System.out.print("\r" + j + " pixel rows remaining  ");
            for (int i = 0; i < imageWidth; ++i) {
                Color pixelColor = new Color(0.0, 0.0, 0.0);

                for (int s = 0; s < samplesPerPixel; ++s) {
                    double u = (i + randomDouble()) / (imageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray r = camera.getRay(u, v);
                    pixelColor = new Color(pixelColor.add(rayColor(r, world, maxDepth)));
                }
                image[j][i] = pixelColor;
            }
        }
        long endTime = System.nanoTime();

        System.out.println();
        System.out.println("Time elapsed: " + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + "s");

        try {
            System.out.print("Writing to file...");
            //view with http://www.cs.rhodes.edu/welshc/COMP141_F16/ppmReader.html
            writer = new PrintWriter("image.ppm", "UTF-8");
            writer.println("P3");
            writer.println(imageWidth + " " + imageHeight);
            writer.println("255");

            for (int j = imageHeight - 1; j >= 0; --j) {
                for (int i = 0; i < imageWidth; ++i) {
                    writer.println(image[j][i].write(samplesPerPixel));
                }
            }

        }
        catch (FileNotFoundException | UnsupportedEncodingException f) {
            f.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        System.out.println("Done!");
    }
}