package ray.material;

import ray.math.Color;

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

  /**
   * The material given to all surfaces unless another is specified.
   */
  public static final Material DEFAULT_MATERIAL = new Lambertian();

}
