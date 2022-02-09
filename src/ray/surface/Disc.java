package ray.surface;

import ray.math.Point3;
import ray.math.Vector3;

/**
 * Representation of a 2D disk by a center, radius and normal.
 * 
 * @author parryrm
 *
 */
public class Disc extends Plane {

	/* The radius. */
	protected double radius = 1.0;
	public void setRadius(double radius) { this.radius = radius; }
	
	public Disc() { }

	public HitRecord hit(Vector3 direction, Point3 origin) {
		/*	t = -((origin - point) dot n) / direction dot normal */
		HitRecord planeHitRecord = super.hit(direction, origin);
		/*	If the ray intersects with the plane containing the disc, 
			then it may intersect with the disk itself. If it doesn't 
			intersect with the plane containing the disc, it can't intersect 
			with the disc itself and must return null. */
		if (planeHitRecord != null) {
			/*	So we know the ray intersects in the plane of the disk.
				Is it within the radius of the disc? */
			Point3 hitpoint = new Point3(origin); 			// copy constructor Point3 such that origin isn't changed. 
			hitpoint.scaleAdd(planeHitRecord.t, direction); // o + td
			/*	If the distance from the hitpoint is less than or equal to the 
				radius, then it's a hit. Else return null. */
			if (point.distance(hitpoint) <= radius) 
				return planeHitRecord;
		}
		/*	If the ray doesn't intersect with the disc, return */
		return null;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {	
		return "disk " + point + " " + normal + " " + radius + " " + material + " end";
	}

}
