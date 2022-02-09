package tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import ray.Camera;
import ray.Image;
import ray.RayTracer;
import ray.Scene;
import ray.math.Point2;
import ray.math.Vector3;

/**
 * JUnit test cases for the Ray1 ray tracer.
 * 
 * @author Mitch Parry
 * @version 2022-01-18
 * 
 */
public class Ray1Test extends RayHelpers
{
	
    /**
     * This tests camera coordinate system.
     */
	@Test
	public void testCameraCoordinates()
	{
		Vector3 vd, up, pn;
		ArrayList<Vector3[]> cameraParams = new ArrayList<Vector3[]>();
		ArrayList<Vector3[]> correctBasis = new ArrayList<Vector3[]>();

		// vp = z
		cameraParams.add(new Vector3[] {
			new Vector3( 0, 0,-1),
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0, 1), 
		});
		correctBasis.add(new Vector3[] {
			new Vector3( 1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0, 1),
		});

		// vp = -z
		cameraParams.add(new Vector3[] {
			new Vector3( 0, 0, 1), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0,-1), 
		});
		correctBasis.add(new Vector3[] {
			new Vector3(-1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0,-1),
		});

		// vp = x
		cameraParams.add(new Vector3[] {
			new Vector3(-1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 0, 0),
		});
		correctBasis.add(new Vector3[] {
			new Vector3( 0, 0,-1), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 0, 0),
		});

		// vp = -x
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 0),
		});
		correctBasis.add(new Vector3[] {
			new Vector3( 0, 0, 1), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 0),
		});

		// vp = (1, 1, 0)
		cameraParams.add(new Vector3[] {
			new Vector3(-1,-1, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 1, 0),
		});
		correctBasis.add(new Vector3[] {
			new Vector3( 0, 0,-1), 
			new Vector3(-.707, .707, 0),
			new Vector3( .707, .707, 0),
		});

		// vp = (-1, 0, 1)
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 0,-1), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 1),
		});
		correctBasis.add(new Vector3[] {
			new Vector3( .707, 0,  .707), 
			new Vector3(0,     1, 0),
			new Vector3(-.707, 0, .707),
		});

		// vp = (1, 1, 1)
		cameraParams.add(new Vector3[] {
			new Vector3(-1,-1,-1),
			new Vector3( 0, 1, 0),
			new Vector3( 1, 1, 1),
		});
		correctBasis.add(new Vector3[] {
			new Vector3(.707, 0,-.707),
			new Vector3(-.408, .816,-.408),
			new Vector3( .577, .577,  .577), 
		});

		// vp = (-1, -1, -1)
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 1, 1),
			new Vector3( 0, 1, 0),
			new Vector3(-1,-1,-1),
		});
		correctBasis.add(new Vector3[] {
			new Vector3(-.707, 0,.707),
			new Vector3(-.408, .816,-.408),
			new Vector3(-.577,-.577,-.577),
		});

		Scene scene = new Scene();

		for (int i = 0; i < cameraParams.size(); i++)
		{
			Vector3[] cp = cameraParams.get(i);
			Vector3[] cb = correctBasis.get(i);
			vd = cp[0]; up = cp[1]; pn = cp[2];
			
			Camera camera = new Camera();
			camera.setViewDir(vd);
			camera.setViewUp(up);
			camera.setProjNormal(pn);
			scene.setCamera(camera);
			
			Vector3[] basis = RayTracer.computeBasis(scene);
			String message = "computeBasis fails for projNormal=" + pn + "; viewDir=" + vd + "; viewUp=" + up;
			for (int k = 0; k < basis.length; k++)
			{
				assertEquals(message, cb[k].x, basis[k].x, EPS);
				assertEquals(message, cb[k].y, basis[k].y, EPS);
				assertEquals(message, cb[k].z, basis[k].z, EPS);
			}
		}
	}
	
	/**
	 * This tests the ray direction computation.
	 */
	@Test
	public void testComputeRayDirection()
	{
		Vector3 vd, up, pn;
		ArrayList<Vector3[]> cameraParams = new ArrayList<Vector3[]>();
		ArrayList<Vector3[]> bases = new ArrayList<Vector3[]>();
		ArrayList<Point2> viewSizes = new ArrayList<Point2>();
		ArrayList<Point2> imageSizes = new ArrayList<Point2>();
		ArrayList<Point2> imageCoordinates = new ArrayList<Point2>();
		ArrayList<Vector3> correctDirections = new ArrayList<Vector3>();
		ArrayList<Double> projectionDistances = new ArrayList<Double>();

		// vp = z
		cameraParams.add(new Vector3[] {
			new Vector3( 0, 0,-1),
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0, 1),
		});
		bases.add(new Vector3[] {
			new Vector3( 1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0, 1),
		});
		viewSizes.add(new Point2(1, 1));
		imageSizes.add(new Point2(2, 2));
		imageCoordinates.add(new Point2(0, 0));
		correctDirections.add(new Vector3(-.25, -.25, -1));
		projectionDistances.add(1.0);

		// vp = -z
		cameraParams.add(new Vector3[] {
			new Vector3( 0, 0, 1), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0,-1), 
		});
		bases.add(new Vector3[] {
			new Vector3(-1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 0, 0,-1),
		});
		viewSizes.add(new Point2(2, 2));
		imageSizes.add(new Point2(2, 8));
		imageCoordinates.add(new Point2(0, 7));
		correctDirections.add(new Vector3(.5, .875, 2));
		projectionDistances.add(2.0);

		// vp = x
		cameraParams.add(new Vector3[] {
			new Vector3(-1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 0, 0),
		});
		bases.add(new Vector3[] {
			new Vector3( 0, 0,-1), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 0, 0),
		});
		viewSizes.add(new Point2(1, 2));
		imageSizes.add(new Point2(8, 8));
		imageCoordinates.add(new Point2(7, 0));
		correctDirections.add(new Vector3(-1, -.875, -.4375));
		projectionDistances.add(1.0);

		// vp = -x
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 0, 0), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 0),
		});
		bases.add(new Vector3[] {
			new Vector3( 0, 0, 1), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 0),
		});
		viewSizes.add(new Point2(2, 1));
		imageSizes.add(new Point2(8, 2));
		imageCoordinates.add(new Point2(7, 1));
		correctDirections.add(new Vector3(2, .25, .875));
		projectionDistances.add(2.0);

		// vp = (1, 1, 0)
		cameraParams.add(new Vector3[] {
			new Vector3(-1,-1, 0), 
			new Vector3( 0, 1, 0),
			new Vector3( 1, 1, 0),
		});
		bases.add(new Vector3[] {
			new Vector3( 0, 0,-1), 
			new Vector3(-.707, .707, 0),
			new Vector3( .707, .707, 0),
		});
		viewSizes.add(new Point2(1, 1));
		imageSizes.add(new Point2(2, 2));
		imageCoordinates.add(new Point2(0, 0));
		correctDirections.add(new Vector3(-0.530, -0.884, .25));
		projectionDistances.add(1.0);

		// vp = (-1, 0, 1)
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 0,-1), 
			new Vector3( 0, 1, 0),
			new Vector3(-1, 0, 1),
		});
		bases.add(new Vector3[] {
			new Vector3( .707, 0,  .707), 
			new Vector3(0,     1, 0),
			new Vector3(-.707, 0, .707),
		});
		viewSizes.add(new Point2(2, 2));
		imageSizes.add(new Point2(2, 8));
		imageCoordinates.add(new Point2(0, 7));
		correctDirections.add(new Vector3(1.061, 0.875, -1.768));
		projectionDistances.add(2.0);

		// vp = (1, 1, 1)
		cameraParams.add(new Vector3[] {
			new Vector3(-1,-1,-1),
			new Vector3( 0, 1, 0),
			new Vector3( 1, 1, 1),
		});
		bases.add(new Vector3[] {
			new Vector3(.707, 0,-.707),
			new Vector3(-.408, .816,-.408),
			new Vector3( .577, .577,  .577), 
		});
		viewSizes.add(new Point2(1, 2));
		imageSizes.add(new Point2(8, 8));
		imageCoordinates.add(new Point2(7, 7));
		correctDirections.add(new Vector3(-0.625, 0.137, -1.244));
		projectionDistances.add(1.0);

		// vp = (-1, -1, -1)
		cameraParams.add(new Vector3[] {
			new Vector3( 1, 1, 1),
			new Vector3( 0, 1, 0),
			new Vector3(-1,-1,-1),
		});
		bases.add(new Vector3[] {
			new Vector3(-.707, 0,.707),
			new Vector3(-.408, .816,-.408),
			new Vector3(-.577,-.577,-.577),
		});
		viewSizes.add(new Point2(2, 1));
		imageSizes.add(new Point2(8, 2));
		imageCoordinates.add(new Point2(7, 0));
		correctDirections.add(new Vector3(.638, 0.951, 1.875));
		projectionDistances.add(2.0);

		Scene scene = new Scene();
		Image image = new Image(5, 5);
		scene.setImage(image);

		for (int k = 0; k < cameraParams.size(); k++)
		{
			Vector3[] cp = cameraParams.get(k);
			Vector3[] basis = bases.get(k);
			Point2 viewSize = viewSizes.get(k);
			Point2 imageSize = imageSizes.get(k);
			Point2 imageCoordinate = imageCoordinates.get(k);
			Vector3 correctDirection = correctDirections.get(k);
			vd = cp[0]; up = cp[1]; pn = cp[2];
			double viewWidth = viewSize.x;
			double viewHeight = viewSize.y;
			double projDistance = projectionDistances.get(k);
			int nx = (int) imageSize.x;
			int ny = (int) imageSize.y;
			int i = (int) imageCoordinate.x;
			int j = (int) imageCoordinate.y;
			
			Camera camera = new Camera();
			camera.setViewDir(vd);
			camera.setViewUp(up);
			camera.setProjNormal(pn);
			camera.setprojDistance(projDistance);
			camera.setViewWidth(viewWidth);
			camera.setViewHeight(viewHeight);
			scene.setCamera(camera);
			image.setSize(nx, ny);
			
			Vector3 d = RayTracer.computeRayDirection(scene, basis, i, j);
//			System.out.println(d);
			String message = "computeRayDirection fails for" +
			        " viewWidth=" + viewWidth + 
					"; viewHeight=" + viewHeight + 
					"; projDistance=" + projDistance +
					"; image.width=" + nx + 
					"; image.height=" + ny + 
					"; i=" + i + 
					"; j=" + j +
					"; viewDir=" + vd + 
					"; projNormal=" + pn + 
					"; u=" + basis[0] + 
					"; v=" + basis[1] + 
					"; w=" + basis[2];
			assertEquals(message, correctDirection.x, d.x, EPS);
			assertEquals(message, correctDirection.y, d.y, EPS);
			assertEquals(message, correctDirection.z, d.z, EPS);
		}
	}
	
    /**
     * This tests planes with constant color.
     */
    @Test
    public void testPlaneConstant()
    {
    	runTests("scenes1/plane-constant");
    }

    /**
     * This tests planes shading the surface normals.
     */
    @Test
    public void testPlaneNormal()
    {
    	runTests("scenes1/plane-normal");
    }

    /**
     * This tests discs with constant color.
     */
    @Test
    public void testDiscConstant()
    {
    	runTests("scenes1/disc-constant");
    }

    /**
     * This tests planes shading the surface normals.
     */
    @Test
    public void testDiscNormal()
    {
    	runTests("scenes1/disc-normal");
    }

    /**
     * This tests two planes with constant color.
     */
    @Test
    public void testMultiplePlanes()
    {
    	runTests("scenes1/multiple-planes-constant");
    	runTests("scenes1/multiple-planes-normal");
    }

    /**
     * This tests two discs with constant color.
     */
    @Test
    public void testMultipleDiscs()
    {
    	runTests("scenes1/multiple-discs-constant");
    	runTests("scenes1/multiple-discs-normal");
    }

    /**
     * This tests overlapping discs
     */
    @Test
    public void testMultipleObjects()
    {
    	runTests("scenes1/multiple-objects");
    }

    /**
     * This tests the rays generated using Mario.
     */
    @Test
    public void testMario()
    {
    	runTests("scenes1/mario");
    }
}