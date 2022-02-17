package ray.light;

import ray.math.Color;
import ray.math.Ray;
import ray.surface.Group;
import ray.surface.HitRecord;

/**
 * This class represents an ambient light which illuminates every surface the same 
 * regardless of position or orientation.
 *
 * @author parryrm
 */
public class AmbientLight extends Light {
	
	/** How bright the light is. */
	public final Color intensity = new Color(1, 1, 1);
	public void setIntensity(Color intensity) { this.intensity.set(intensity); }
	
	/**
	 * Default constructor.
	 */
	public AmbientLight() { }


	public Color illuminate(HitRecord hit, Ray ray, Group group) {
		/*	intensity * hit.surface.material.color
			element-wise multiply */
		double r, g, b;
		r = intensity.r * hit.surface.getMaterial().getColor().r;
		g = intensity.g * hit.surface.getMaterial().getColor().g;
		b = intensity.b * hit.surface.getMaterial().getColor().b;
		return new Color(r, g, b);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "ambient light: " + intensity + " end";
	}

}