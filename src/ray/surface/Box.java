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

	public HitRecord hit(Vector3 direction, Point3 origin) {
		// Calculate all 6 possible intersection points.
		double tx1 = (maxPt.x - origin.x) / direction.x;
		double tx2 = (minPt.x - origin.x) / direction.x;
		double ty1 = (maxPt.y - origin.y) / direction.y;
		double ty2 = (minPt.y - origin.y) / direction.y;
		double tz1 = (maxPt.z - origin.z) / direction.z;
		double tz2 = (minPt.z - origin.z) / direction.z;
		// Find the minimum of each axis.
		double tmin[] = {(tx1 < tx2) ? tx1 : tx2, 
						 (ty1 < ty2) ? ty1 : ty2, 
						 (tz1 < tz2) ? tz1 : tz2};
		//	Find the maximum of each axis.
		double tmax[] = {(tx1 > tx2) ? tx1 : tx2, 
						 (ty1 > ty2) ? ty1 : ty2, 
						 (tz1 > tz2) ? tz1 : tz2};				 
		//	Max of the min is your enter point. 
		double t_enter = Double.NEGATIVE_INFINITY;
		for (double num : tmin)
			if (num > t_enter)
				t_enter = num;
		//	Min of the max is your enter point. 
		double t_exit = Double.POSITIVE_INFINITY;
		for (double num: tmax)
			if (num < t_exit)
				t_exit = num;
		// If your enter t value is greater than your exit, you missed the cube. 
		if (t_enter > t_exit)
			return null;
		//	We know that we have hit the box. Now to find which normal we have. 
		Vector3 normal = null;
		if      (t_enter == tx1) normal = new Vector3( 1,  0,  0);
		else if (t_enter == tx2) normal = new Vector3(-1,  0,  0);
		else if (t_enter == ty1) normal = new Vector3( 0,  1,  0);
		else if (t_enter == ty2) normal = new Vector3( 0, -1,  0);
		else if (t_enter == tz1) normal = new Vector3( 0,  0,  1);
		else if (t_enter == tz2) normal = new Vector3( 0,  0, -1);
		// We know our normal, t value, and normal. Return the HitRecord. 
		return new HitRecord(this, t_enter, normal);
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
