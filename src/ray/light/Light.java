package ray.light;

import ray.math.Color;
import ray.math.Ray;
import ray.surface.Group;
import ray.surface.HitRecord;

/**
 * This is the abstract parent class which all lights are child of.
 * You will probably want to add an "illuminate" method to it.
 *
 * @author parryrm
 */
public abstract class Light {
    public abstract Color illuminate(HitRecord hit, Ray ray, Group group);
}