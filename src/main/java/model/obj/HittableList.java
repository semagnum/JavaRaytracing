package model.obj;

import lombok.Getter;
import model.Ray;
import model.aabb;

import java.util.ArrayList;
import java.util.List;

import static model.aabb.surroundingBox;

@Getter
public class HittableList extends Hittable {
    private final List<Hittable> world;

    public HittableList() {
        world = new ArrayList<>();
    }

    public HittableList(Hittable h) {
        world = new ArrayList<>();
        world.add(h);
    }

    public void add(Hittable h) {
        world.add(h);
    }

    public HitRecord hit(Ray r, double tMin, double tMax) {
        HitRecord hitRecord = null;
        HitRecord tempRec;
        double closest_so_far = tMax;

        for (Hittable object : world) {
            if ((tempRec = object.hit(r, tMin, closest_so_far)) != null) {
                closest_so_far = tempRec.getT();
                hitRecord = tempRec;
            }
        }

        return hitRecord;

    }

    public aabb boundingBox(double time0, double time1) {
        aabb outputBox = null;
        boolean first_box = true;

        for (Hittable object : world) {
            aabb tempBox = object.boundingBox(time0, time1);
            if (tempBox == null) return null;
            outputBox = first_box ? tempBox : surroundingBox(outputBox, tempBox);
            first_box = false;
        }

        return outputBox;
    }
}
