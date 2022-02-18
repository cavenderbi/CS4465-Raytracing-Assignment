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
		Vector3 h = new Vector3(), view = new Vector3();
		Color L_r = new Color(color);
		/*	Draw a normalized vector from the hitpoint towards the camera. */
		view.sub(ray.origin, ray.evaluate(hit.t));
		view.normalize();
		/*	h = (l + v) / || l + v || */
		h.add(view, light);
		h.normalize();
		/*	L_r = diffuse + specular * max(0, (n dot h)^P) */
		L_r.scaleAdd(Math.max(0, Math.pow(hit.normal.dot(h), exponent)), specularColor);
		L_r.scale(irradiance);
		
		return L_r;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "phong " + color + " " + specularColor + " " + exponent + " end";
	}
}
