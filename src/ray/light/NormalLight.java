package ray.light;

import ray.math.Color;
import ray.math.Ray;
import ray.surface.Group;
import ray.surface.HitRecord;

/**
 * This class represents a "normal" light which reveals the normal vector on the surface.
 *
 * @author parryrm
 */
public class NormalLight extends Light {
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public NormalLight() { }

	public Color illuminate(HitRecord hit, Ray ray, Group group) {
		double r, g, b;
		r = (hit.normal.x + 1) / 2;
		g = (hit.normal.y + 1) / 2;
		b = (hit.normal.z + 1) / 2;
		return new Color(r, g, b);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "normal light end";
	}
}