package ray.surface;
import java.util.ArrayList;
import ray.math.*;
/**
 * A group of surfaces.
 * @author parryrm
 *
 */
public class Group extends Surface {
	protected ArrayList<Surface> surfaces = new ArrayList<Surface>();
	
	public void add(Surface toAdd) {
		this.surfaces.add(toAdd);
	}

	public HitRecord hit(Vector3 ray, Point3 viewpoint) {
		HitRecord hit;
		HitRecord closestHit = surfaces.get(0).hit(ray, viewpoint);
		double smallest = Double.POSITIVE_INFINITY;
		/*	For every surface in the group of surfaces, see 
			which intersection is closest and return that color. */
		for (Surface surface : surfaces) {
			hit = surface.hit(ray, viewpoint);
			if (hit != null && hit.t > 0 && hit.t < smallest) {
				closestHit = new HitRecord(hit);
				smallest = closestHit.t;
			}
		}
		return closestHit;
	}
}