package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a plane as a point on the plane and the normal to the plane.
 * 
 * @author parryrm
 *
 */
public class Plane extends Surface {

	/* A point on the plane. */
	protected final Point3 point = new Point3();
	public void setPoint(Point3 point) { this.point.set(point); }

	/* The normal vector. */
	protected final Vector3 normal = new Vector3();
	public void setNormal(Vector3 normal) { this.normal.set(normal); }
	
	public Plane() {}

	public HitRecord hit(Vector3 direction, Point3 origin) {
		/*	t = -((origin - point) dot n) / direction dot normal */
		Vector3 op = new Vector3(); // new blank Vector3, no other referencess
		op.sub(origin, point);		// op gets set to o - p
		double t = -op.dot(normal) / direction.dot(normal);
		return new HitRecord(this, t, new Vector3(normal));
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "plane " + point + " " + normal + " " + material + " end";
	}

}
