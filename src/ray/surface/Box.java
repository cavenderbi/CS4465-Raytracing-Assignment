package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

public class Box extends Surface {
	
	/* The corner of the box with the smallest x, y, and z components. */
	protected final Point3 minPt = new Point3();
	public void setMinPt(Point3 minPt) { this.minPt.set(minPt); }

	/* The corner of the box with the largest x, y, and z components. */
	protected final Point3 maxPt = new Point3();
	public void setMaxPt(Point3 maxPt) { this.maxPt.set(maxPt); }

	public HitRecord hit(Vector3 ray, Point3 viewpoint) {
		return new HitRecord(this, 1, new Vector3());
	}

	public Box() { }

	/*	Returns whether or not a ray intersects with the surface.
		@param Vector3 ray
	 */
	public boolean intersect(Vector3 ray) {
		return true;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "box " + minPt + " " + maxPt + " " + material + " end";
	}
}
