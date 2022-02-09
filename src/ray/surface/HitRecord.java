package ray.surface;

import ray.math.Vector3;

public class HitRecord {
    public Surface surface;
    public double t;
    public Vector3 normal;

    public HitRecord(Surface surface, double t, Vector3 normal) {
        this.surface = surface; this.t = t; this.normal = normal;
    }
    public HitRecord(HitRecord copyMe) {
        this.surface = copyMe.surface;
        this.t = copyMe.t;
        this.normal = copyMe.normal;
    }
}
