package model.obj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Ray;
import model.Vector3;

import java.util.ArrayList;
import java.util.List;

import static model.Vector3.dot;

@Data
public class HittableList extends Hittable {
    private List<Hittable> world;

    public HittableList(Hittable h) {
        world = new ArrayList<>();
        add(h);
    }

    public HittableList() {
        world = new ArrayList<>();
    }

    public void clear() {
        world.clear();
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
