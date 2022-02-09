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

	public HitRecord hit(Vector3 ray, Point3 viewpoint) {
		/*	(ray dot ray)t^2 + (2viewpoint dot ray)*t + (viewpoint dot viewpoint - 2viewpoint dot center + center dot center - radius^2) 
			(-B +/- sqrt(B^2-4AC))/2A

	new 		((2viewpoint dot ray) +/- sqrt((2viewpoint dot ray)^2 - 4(ray dot ray)(viewpoint dot viewpoint - 2viewpoint dot center + center dot center - radius^2))/2(ray dot ray)
			Returns NaN if ray is perpendicular to itself (not possible) or the ray misses the surface completely. 

			Normal: 2(q-c)
		*/
		Vector3 oc = new Vector3();
		oc.sub(viewpoint, center);

		double A = ray.dot(ray);
		double B = 2 * ray.dot(oc);
		double C = oc.dot(oc) - Math.pow(radius, 2);

		double t_plus = (-B + Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
		double t_minus = (-B - Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
		double t = (t_plus > t_minus) ? t_minus : t_plus;

		Vector3 normal = new Vector3();
		viewpoint.scaleAdd(t, ray); // o + td = q
		normal.sub(viewpoint /* Now the intersection point. */, center); // (q - c)
		normal.scale(2); // 2 * (q - c) is the normal. 

		return new HitRecord(this, t, normal);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "sphere " + center + " " + radius + " " + material + " end";
	}
	
}
