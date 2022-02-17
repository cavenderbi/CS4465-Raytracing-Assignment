package ray.material;

import ray.light.Light;
import ray.math.Color;
import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;
import ray.surface.HitRecord;

/**
 * This abstract class specifies what is necessary for an object to be a material.
 * You will probably want to add an "evaluate" method to it.
 * @author ags
 */
public abstract class Material {
  /** The base color of the surface (diffuse and ambient color). */
  protected final Color color = new Color(1, 1, 1);
  public Color getColor() { return new Color(color); };
  public void setColor(Color color) { this.color.set(color); }
  
  public abstract Color evaluate(HitRecord hit, Color irradiance, Vector3 light, Ray ray);

  /**
   * The material given to all surfaces unless another is specified.
   */
  public static final Material DEFAULT_MATERIAL = new Lambertian();

}
