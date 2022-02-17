package ray.material;

import ray.math.Color;
import ray.math.Ray;
import ray.math.Vector3;
import ray.surface.HitRecord;

/**
 * A Phong material. Uses the Blinn-Phong model from the textbook.
 *
 * @author ags
 */
public class Phong extends Material {
	
	/** The color of the surface (specular reflection). */
	protected final Color specularColor = new Color(1, 1, 1);
	public void setSpecularColor(Color specularColor) { this.specularColor.set(specularColor); }
	
	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }
	
	public Phong() { }

	public Color evaluate(HitRecord hit, Color irradiance, Vector3 light, Ray ray) { 
		double r, g, b, geometric;
		Vector3 h = new Vector3(), view = new Vector3();
		/*	Draw a normalized vector from the hitpoint towards the camera. */
		view.sub(ray.origin, ray.evaluate(hit.t));
		/*	h = (l + v) / || l + v || */
		h.add(view, light);
		h.normalize();
		/*	L_r = diffuse + specular * max(0, (n dot h)^P) */
		geometric = Math.max(0, Math.pow(hit.normal.dot(h), exponent));

		r = (color.r + specularColor.r * geometric) * irradiance.r;
		g = (color.g + specularColor.g * geometric) * irradiance.g;
		b = (color.b + specularColor.b * geometric) * irradiance.b;
		
		return new Color(r, g, b);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "phong " + color + " " + specularColor + " " + exponent + " end";
	}
}
