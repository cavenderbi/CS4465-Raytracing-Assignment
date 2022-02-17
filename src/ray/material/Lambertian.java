package ray.material;

import ray.math.Color;
import ray.math.Ray;
import ray.math.Vector3;
import ray.surface.HitRecord;

/**
 * A Lambertian material scatters light equally in all directions. Reflectance coefficient is
 * a constant
 *
 * @author ags
 */
public class Lambertian extends Material {
	
	public Lambertian() { }

	public Color evaluate(HitRecord hit, Color irradiance, Vector3 light, Ray ray) {
		double r, g, b; 
		r = color.r * irradiance.r;
		g = color.g * irradiance.g;
		b = color.b * irradiance.b;
		
		return new Color(r, g, b);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "lambertian: " + color + " end";
	}
}
