package model;

import model.obj.HitRecord;
import model.obj.Hittable;
import model.obj.HittableList;

import java.util.Comparator;
import java.util.List;

import static model.aabb.surroundingBox;
import static util.RandomUtil.randomInt;

public class bvhNode extends Hittable {
    private final Hittable left;
    private final Hittable right;
    private final aabb box;

    public bvhNode(HittableList list) {
        this(list.getWorld(), 0, list.getWorld().size(), 0, 0);
    }

    public bvhNode(HittableList list, double time0, double time1) {
        this(list.getWorld(), 0, list.getWorld().size(), time0, time1);
    }

    int boxCompare(Hittable a, Hittable b, int axis) {
        aabb boxA = a.boundingBox(0,0);
        aabb boxB = b.boundingBox(0,0);

        if (boxA == null || boxB == null) {
            throw new IllegalStateException("No bounding box in bvh_node constructor.");
        }

        return Double.compare(boxA.getMinimum().at(axis), boxB.getMinimum().at(axis));
    }

    public bvhNode(
            List<Hittable> objects,
                int start, int end, double time0, double time1) {

        int axis = randomInt(0,2);
        Comparator<Hittable> comparator = (a, b) -> boxCompare(a, b, axis);

        int object_span = end - start;

        switch (object_span) {
            case 1:
                left = right = objects.get(start);
                break;
            case 2:
                if (comparator.compare(objects.get(start), objects.get(start + 1)) < 0) {
                    left = objects.get(start);
                    right = objects.get(start + 1);
                } else {
                    left = objects.get(start + 1);
                    right = objects.get(start);
                }
                break;
            default:
                objects.subList(start, end).sort(comparator);

                int mid = start + object_span/2;
                left = new bvhNode(objects, start, mid, time0, time1);
                right = new bvhNode(objects, mid, end, time0, time1);
        }

        aabb boxLeft = left.boundingBox(time0, time1);
        aabb boxRight = right.boundingBox(time0, time1);

        if (  boxLeft == null || boxRight == null) {
            throw new IllegalStateException("No bounding box in bvh_node constructor.");
        }

        box = surroundingBox(boxLeft, boxRight);
    }

    public HitRecord hit(Ray r, double tMin, double tMax) {
        boolean rec = box.hit(r, tMin, tMax);
        if (!rec)
            return null;

        HitRecord hitLeft = left.hit(r, tMin, tMax);
        double rtMax = (hitLeft != null) ? hitLeft.getT() : tMax;
        HitRecord hitRight = right.hit(r, tMin, rtMax);

        if (hitRight != null) {
            return hitRight;
        }
        return hitLeft;
    }

    public aabb boundingBox(double time0, double time1) {
        return box;
    }
}
