package ray.math;

public class Ray {
    public Point3 origin;
    public Vector3 direction;

    public Ray(Point3 origin, Vector3 direction) { this.origin = origin; this.direction = direction; }

    /**
     * o + td = hitpoint
     */
    public Point3 evaluate(double t) {
        Point3 answer = new Point3(origin);
        answer.scaleAdd(t, direction);
        return answer;
    }
}
