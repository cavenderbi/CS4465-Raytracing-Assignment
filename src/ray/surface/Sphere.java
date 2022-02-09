package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
	
	/** The center of the sphere. */
	protected final Point3 center = new Point3();
	public void setCenter(Point3 center) { this.center.set(center); }
	
	/** The radius of the sphere. */
	protected double radius = 1.0;
	public void setRadius(double radius) { this.radius = radius; }
	
	public Sphere() { }

	public HitRecord hit(Vector3 direction, Point3 origin) {
		Vector3 oc = new Vector3();
		oc.sub(origin, center);

		double a, b, c, t;
		a = direction.dot(direction);
		b = 2.0 * oc.dot(direction);
		c = oc.dot(oc) - radius * radius;
		
		double discr = b * b - 4 * a * c;
		if (discr < 0) return null;
		else {
			double numer = -b - Math.sqrt(discr);
			if (numer > 0) 
				t = numer / (2 * a);
			else {
				numer = -b + Math.sqrt(discr);
				if (numer > 0) 
					t = numer / (2 * a);
				else return null;
			}
		}

		Vector3 normal = new Vector3(); 
		Point3 hitpoint = new Point3(origin); // o + td
		hitpoint.scaleAdd(t, direction);
		normal.sub(hitpoint, center); // Vector from center to hitpoint. 
		normal.normalize();

		if (t > 0) 
			return new HitRecord(this, t, normal);
		else return null;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {

		return "sphere " + center + " " + radius + " " + material + " end";
	}	
}
