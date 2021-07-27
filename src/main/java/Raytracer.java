import model.*;
import model.material.DielectricMaterial;
import model.material.DiffuseMaterial;
import model.material.MetalMaterial;
import model.material.ScatterRecord;
import model.obj.HitRecord;
import model.obj.HittableList;
import model.obj.Sphere;
import util.RandomUtil;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Raytracer {

    private static Color rayColor(Ray r, HittableList world, int depth) {
        if (depth <= 0) {
            return new Color();
        }

        HitRecord hitRecord;
        if ((hitRecord = world.hit(r, 0.001, Double.MAX_VALUE)) != null) {
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

        double aspectRatio = 16.0 / 9.0;
        int imageWidth = 400;
        int imageHeight = (int) (imageWidth / aspectRatio);

        // World
        HittableList world = new HittableList();
        double R = Math.cos(Math.PI/4);

        DiffuseMaterial material_left  = new DiffuseMaterial(new Color(0,0,1));
        DiffuseMaterial material_right = new DiffuseMaterial(new Color(1,0,0));

        world.add(new Sphere(new Point3(-R, 0, -1), R, material_left));
        world.add(new Sphere(new Point3( R, 0, -1), R, material_right));

        Camera camera = new Camera(new Point3(0,0,0), new Point3(0,0,-1), new Vector3(0,1,0), 90, aspectRatio);


        // Render
        int samplesPerPixel = 100;
        int maxDepth = 5;
        PrintWriter writer = null;
        Color[][] image = new Color[imageHeight][imageWidth];

        for (int j = imageHeight - 1; j >= 0; --j) {
            System.out.print("\r" + j + " pixel rows remaining  ");
            for (int i = 0; i < imageWidth; ++i) {
                Color pixelColor = new Color(0.0, 0.0, 0.0);

                for (int s = 0; s < samplesPerPixel; ++s) {
                    double u = (i + RandomUtil.randomDouble()) / (imageWidth - 1);
                    double v = (j + RandomUtil.randomDouble()) / (imageHeight - 1);
                    Ray r = camera.getRay(u, v);
                    pixelColor = new Color(pixelColor.add(rayColor(r, world, maxDepth)));
                }
                image[j][i] = pixelColor;
            }
        }
        System.out.println();

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