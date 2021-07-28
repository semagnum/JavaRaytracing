package model.obj;

import lombok.Getter;
import model.Ray;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HittableList extends Hittable {
    private final List<Hittable> world;

    public HittableList() {
        world = new ArrayList<>();
    }

    public void add(Hittable h) {
        world.add(h);
    }

    public HitRecord hit(Ray r, double t_min, double t_max) {
        HitRecord hitRecord = null;
        HitRecord tempRec;
        double closest_so_far = t_max;

        for (Hittable object : world) {
            if ((tempRec = object.hit(r, t_min, closest_so_far)) != null) {
                closest_so_far = tempRec.getT();
                hitRecord = tempRec;
            }
        }

        return hitRecord;

    }
}
