package ray;

import ray.light.Light;
import ray.math.Color;
import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;
import ray.surface.HitRecord;

import java.io.File;


/**
 * A simple ray tracer.
 *
 * @author ags
 */
public class RayTracer {

	/**
	 * The main method takes all the parameters an assumes they are input files
	 * for the ray tracer. It tries to render each one and write it out to a PNG
	 * file named <input_file>.png.
	 *
	 * @param args
	 */
	public static final void main(String[] args) {
        for (String inputString : args)
        {
            recursiveXML(inputString);
        }
    }
    
    public static void recursiveXML(String inputString)
    {
        File inputFile = new File(inputString);
        if (inputFile.isFile())
        {
            if (inputFile.getName().endsWith(".xml"))
            {
                runXML(inputFile.getPath());
            }
        }
        else if (inputFile.isDirectory())
        {
            File[] listing = inputFile.listFiles();
            if (listing != null)
            {
                for (File file : listing)
                {
                    recursiveXML(inputString + "/" + file.getName());
                }
            }
        }
        else
        {
            System.out.println("Input argument \"" + inputString + "\" is neither an XML file nor a directory.");
        }
    }

    public static void runXML(String inputFilename)
    {
        Parser parser = new Parser();
        String outputFilename = inputFilename + ".png";
        System.out.println(inputFilename);
        // Parse the input file
        Scene scene = (Scene) parser.parse(inputFilename, Scene.class);

        // Render the scene
        renderImage(scene);

        // Write the image out
        scene.getImage().write(outputFilename);
    }
	
	/**
	 * Compute the basis for the camera coordinate system (u, v, w)
	 * @param scene the scene
	 * @return An array containing each vector [u, v, w]
	 */
	public static Vector3[] computeBasis(Scene scene) 
	{
		// Define the u, v, and w vectors. 
		Vector3 u = new Vector3();
		Vector3 v = new Vector3();
		Vector3 w = new Vector3(scene.camera.projNormal);
		// To compute the basis, we need to get the cross product between w-> and up->. 
		u.cross(scene.camera.viewUp, w);
		// Now that u and w are orthogonal, we need to make sure that v is orthogonal to both.
		v.cross(w, u);
		// u, v, and w are all orthogonal to each other. Now we normalize for an orthonormal basis.
		u.normalize();
		v.normalize();
		w.normalize();
		// Return the array containing u, v, and w. 
		return new Vector3[]{u, v, w};
	}
	
	/**
	 * Return the ray direction (from view point to pixel location on view rectangle).
	 * @param scene the scene
	 * @param basis the basis of the camera coordinate system (u, v, w).
	 * @param i the column index of the image
	 * @param j the row index of the image
	 * @return the ray direction as a Vector3
	 */
	public static Vector3 computeRayDirection(Scene scene, Vector3[] basis, int i, int j) {
		/*	Compute ray direction:
			center + (-viewWidth/2)*u-> + viewHeight/2 * v-> gives you the top left corner. 
			viewWidth / imageWidth gives you the pixel width. 
			q_00 = tl + pixelWidth / 2 * u-> - pixelHeight / 2 * v-> gives you the pixel at coordinates 0, 0
			q_ij = g_00 + i * pixelWidth * u-> - j * pixelHeight * v->

			From the center point, it algebras out to...
			viewWidth / imageWidth = pixelWidth, viewHeight / imageHeight = pixelHeight
			center + (-viewWidth/2 + pixelWidth/2 + i * pixelWidth)u-> + (viewHeight/2 - pixelHeight/2 - j * pixelHeight)
		*/
		double pixelWidth = scene.camera.viewWidth / scene.outputImage.width;
		double pixelHeight = scene.camera.viewHeight / scene.outputImage.height;

		Point3 viewpoint = new Point3(scene.camera.viewPoint);
		Point3 point = new Point3(viewpoint);
		Vector3 direction = new Vector3(scene.camera.viewDir);
		direction.normalize();
		point.scaleAdd(scene.camera.projDistance, direction);
		point.scaleAdd(pixelWidth / 2 + i * pixelWidth - scene.camera.viewWidth / 2, basis[0]);
		point.scaleAdd(-(scene.camera.viewHeight / 2 - pixelHeight / 2 - j * pixelHeight), basis[1]);
		
		Vector3 answer = new Vector3();
		answer.sub(point, viewpoint);

		return answer;
	}
	
	/**
	 * The renderImage method renders the entire scene.
	 *
	 * @param scene The scene to be rendered
	 */
	public static void renderImage(Scene scene) {

		// Get the output image
		Image image = scene.getImage();

		// Timing counters
		long startTime = System.currentTimeMillis();

		/*	Render the image, writing the pixel values into image. */
		Vector3[] basis = computeBasis(scene);
		System.out.println("" + basis[0] + basis[1] + basis[2]);
		System.out.println(image.getHeight());
		
		Vector3 direction;
		Ray ray = new Ray();
		HitRecord hit;
		Color rgb = new Color();
		int i, j;
		// For ever pixel in thxe image, cast a ray to see what color it intersects with. 
		for (i = 0; i < scene.outputImage.width; i++) {
			for (j = 0; j < scene.outputImage.height; j++) {
				direction = computeRayDirection(scene, basis, i, j);
			    /*	What and where does the ray intersect with anything in the scene, 
					if anything at all. */
				hit = scene.group.hit(direction, scene.camera.viewPoint);
				/*	If the ray missed all of the objects in the seen, it would return null. 
					Otherwise, calculate the color of the pixel. */
				if (hit != null) {
					ray.set(scene.camera.viewPoint, direction);
					/*	Reset the color to 0 inbetween each pixel. */
					rgb.set(0, 0, 0);
					/*	Calculate how much each light in the scene contributes to the color of that pixel. */
					for (Light light : scene.lights)
						rgb.add(light.illuminate(hit, ray, scene.group));
					rgb.clamp(0, 1);
					/*	Write the calculated color to the pixel. */
					scene.outputImage.setPixelColor(rgb, i, j);
				}
			}
		}
		// Output time
		long totalTime = (System.currentTimeMillis() - startTime);
		System.out.println("Done.  Total rendering time: "
				+ (totalTime / 1000.0) + " seconds");
	}
}
