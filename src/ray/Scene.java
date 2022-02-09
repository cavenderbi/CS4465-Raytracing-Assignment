package ray;

import java.util.ArrayList;

import ray.light.Light;
import ray.material.Material;
import ray.math.Color;
import ray.surface.Group;
import ray.surface.Surface;


/**
 * The scene is just a collection of objects that compose a scene. The camera,
 * the list of lights, and the list of surfaces.
 *
 * @author ags
 * @author rmp
 */
public class Scene {
	
	/** The camera for this scene. */
	protected Camera camera;
	public void setCamera(Camera camera) { this.camera = camera; }
	public Camera getCamera() { return this.camera; }
	
	/** The list of lights for the scene. */
	protected ArrayList<Light> lights = new ArrayList<Light>();
	public void addLight(Light toAdd) { lights.add(toAdd); }
	public ArrayList<Light> getLights() { return this.lights; }
	
	/** The group of surfaces for the scene. */
	protected Group group = new Group();
	public void addSurface(Surface toAdd) { group.add(toAdd); }
	public Group getGroup() { return this.group; }
	
	/** The list of materials in the scene . */
	protected ArrayList<Material> materials = new ArrayList<Material>();
	public void addMaterial(Material toAdd) { materials.add(toAdd); }
	
	/** Image to be produced by the renderer **/
	protected Image outputImage;
	public Image getImage() { return this.outputImage; }
	public void setImage(Image outputImage) { this.outputImage = outputImage; }
	
	/** Ambient Light Source **/
	protected static Color ambientColor = new Color(1.0, 1.0, 1.0);
	public static Color getAmbientIntensity() { return ambientColor; }
	public static void setAmbientIntensity(Color inAmbientColor) { ambientColor = inAmbientColor; }	
}
