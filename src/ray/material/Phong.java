package ray.material;

import ray.math.Color;

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
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "phong " + color + " " + specularColor + " " + exponent + " end";
	}
}
