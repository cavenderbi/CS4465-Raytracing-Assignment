
package ray.light;

import ray.math.Color;
import ray.surface.HitRecord;

/**
 * This class represents a constant light source which reveals the surface color.
 *
 * @author parryrm
 */
public class ConstantLight extends Light {
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public ConstantLight() { }

	public Color illuminate(HitRecord hit) {
		return new Color(hit.surface.getMaterial().getColor());
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "constant light end";
	}
}