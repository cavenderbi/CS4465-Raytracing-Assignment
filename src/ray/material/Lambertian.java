package ray.material;

/**
 * A Lambertian material scatters light equally in all directions. Reflectance coefficient is
 * a constant
 *
 * @author ags
 */
public class Lambertian extends Material {
	
	public Lambertian() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "lambertian: " + color + " end";
	}
}
