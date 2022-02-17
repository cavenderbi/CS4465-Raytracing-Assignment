package ray.light;

import ray.math.Color;
import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;
import ray.surface.Group;
import ray.surface.HitRecord;

/**
 * This class represents a basic point light which is infinitely small and emits
 * a constant power in all directions. This is a useful idealization of a small
 * light emitter.
 *
 * @author ags
 */
public class PointLight extends Light {
	
	/** Where the light is located in space. */
	public final Point3 position = new Point3();
	public void setPosition(Point3 position) { this.position.set(position); }
	
	/** How bright the light is. */
	public final Color intensity = new Color(1, 1, 1);
	public void setIntensity(Color intensity) { this.intensity.set(intensity); }
	
	/**
	 * Default constructor.  Produces a unit intensity light at the origin.
	 */
	public PointLight() { }

	/**	light dot normal / distance^2 */
	public Color illuminate(HitRecord hit, Ray ray, Group group) {
		Point3 hitpoint = ray.evaluate(hit.t);
		Vector3 light = new Vector3();
		light.sub(position, hitpoint);
		light.normalize();
		/*	{vec{ l } cdot vec { n }} over d^2 */
		double geometric = Math.max(light.dot(hit.normal) / position.distanceSquared(hitpoint), 0);
		/*	Use the offset and the intensity to calculate the color. */
		Color point = hit.surface.getMaterial().evaluate(hit, new Color(geometric * intensity.r, geometric * intensity.g, geometric * intensity.b), light, ray);
		/*	Shadows: 
			hitpoint + t * light
			remember to normalize light if not already
			if light ray intersects with groups and 1e-4 < t < 1, is shadow. */
		HitRecord shadow_hit = group.hit(light, hitpoint);
		if (shadow_hit != null && 1e-4 < shadow_hit.t) 
			return new Color(0, 0, 0);
		else return point;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "point light: " + position + " " + intensity + " end";
	}
}