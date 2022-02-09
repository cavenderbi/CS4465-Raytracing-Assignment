package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import ray.Image;
import ray.Parser;
import ray.RayTracer;
import ray.Scene;
import ray.math.Color;

/**
 * JUnit test cases for the Ray1 ray tracer.
 * 
 * @author Mitch Parry
 * @version 2022-01-18
 * 
 */
public class RayHelpers
{
    final static double EPS = 1e-2;
    final static double GAMMA = 2.2;
    final static int LOW_8_BITS = 0xFF;
    final static double MAX_BYTE = 255.0;
    private static Pattern importPattern;
    private static Pattern ignoreFilePattern;
    private static String[] allowedImports;
    private static String importsError;
    
    public RayHelpers()
    {
    	importPattern = Pattern.compile("(^|\\s)import\\s+([A-Za-z0-9]+(\\s*\\.\\s*[A-Za-z0-9]+)+\\s*;)");
    	// ignore test files: Ray1Test.java, Ray1Test1.java, Ray1Test2.java
    	ignoreFilePattern = Pattern.compile("^Ray(?:[12]Test|Helpers)\\.java$");
    	allowedImports = new String[] {
        		"java.awt.Transparency",
        		"java.awt.color.ColorSpace",
        		"java.awt.image.BufferedImage",
        		"java.awt.image.ComponentColorModel",
        		"java.awt.image.DataBufferFloat",
        		"java.awt.image.PixelInterleavedSampleModel",
        		"java.awt.image.WritableRaster",
        		"java.awt.image.Raster",
        		"java.io.File",
        		"java.lang.reflect.Array",
        		"java.lang.reflect.Constructor",
        		"java.lang.reflect.Method",
        		"java.lang.Math",
        		"java.util.ArrayList",
        		"java.util.HashMap",
        		"java.util.StringTokenizer",
        		"javax.imageio.ImageIO",
        		"javax.xml.parsers.DocumentBuilder",
        		"org.w3c.dom.Document",
        		"org.w3c.dom.Element",
        		"org.w3c.dom.NamedNodeMap",
        		"org.w3c.dom.Node",
        		"ray.",
        		"solution."
        };
    	importsError = null;
    }
    
    /**
     * Wrapper for checkImports
     * 
     */
    public static void importsPass()
    {
    	if (importsError == null)
    	{
    		importsError = checkImports(".");
    		// System.out.println("detected: " + importsError);
    	}
    	
    	if (importsError != "pass")
    	{
    		System.out.println("failed: " + importsError);
    		fail(importsError);
    	}
    	
    }
    
    /**
     * Check imports
     */
    public static String checkImports(String folderPath)
    {
		// System.out.println("Checking imports");
    	final File folder = new File(folderPath);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                String s = checkImports(folderPath + "/" + fileEntry.getName());
                if (s != "pass")
                {
                	return s;
                }
            } else if (fileEntry.isFile() & fileEntry.getName().toLowerCase().endsWith("java")
            		& !ignoreFilePattern.matcher(fileEntry.getName()).matches()) {
            	String filePath = folderPath + "/" + fileEntry.getName();
                // System.out.println(filePath);
                try
                {
                    String java = new String(Files.readAllBytes(Paths.get(filePath)));
                    Matcher importMatcher = importPattern.matcher(java);
                    while (importMatcher.find()) 
                    {
                    	String match = importMatcher.group();
                    	match = match.replaceAll("\\s", "");
                    	match = match.replace("import", "");
                    	// System.out.println(match);
                    	boolean allowed = false;
                    	for (String allowedImport : allowedImports)
                    	{
                    		if (match.startsWith(allowedImport))
                    		{
                    			allowed = true;
                    			break;
                    		}
                    	}
                    	if (!allowed)
                    	{
                    		System.out.println("Disallowed");
                    		return "The import " + match + " is not allowed in the file " + fileEntry.getName();
                    	}
                    }
                }
                catch (IOException e)
                {
                	e.printStackTrace();
                	return "Import code threw IOException.";
                }
            }
        }
        return "pass";
    }
    
    /**
     * Helper method that runs a specific test.
     * 
     * @param xmlFile
     *            The filename for the test.
     */
    protected static void runTest(String xmlFile)
    {
    	importsPass();
        System.out.println(xmlFile);
        String pngFile = xmlFile + ".correct.png";
        String[] tokens = xmlFile.split("/");
        String testName = tokens[tokens.length - 1];
        Parser parser = new Parser();
        Scene scene = (Scene) parser.parse(xmlFile, Scene.class);
        RayTracer.renderImage(scene);
        Image result = scene.getImage();
        writeRead(result);
        Image correct = new Image(result.getWidth(), result.getHeight());
        read(pngFile, correct);
        double rmse = difference(result, correct);
        System.out.println(xmlFile + " : rmse = " + rmse);
        if (rmse > EPS) {
            result.write(xmlFile + ".png");
        }
        assertEquals(testName + " example fails with rmse = " + rmse,
                0, rmse, EPS);
    }
    
    protected static void runTests(String test)
    {
    	assertTrue("Failed to find a test in " + test, runTestsRecursive(test));
    }
    
    protected static boolean runTestsRecursive(String test) 
    {
		File file = new File(test);
		boolean foundTest = false;
		if (file.isDirectory())
		{
			File[] listing = file.listFiles();
			if (listing != null) 
			{
				for (File f : listing) 
				{
					foundTest |= runTestsRecursive(f.getPath());
				}
			}
		}
		else if (file.getName().endsWith(".xml"))
		{
			runTest(file.getPath());
			foundTest = true;			
		}
		return foundTest;
    }

    /**
     * Read this image from the filename. The output is always read as a PNG
     * regardless of the extension on the filename given.
     * 
     * @param fileName
     *            the input filename
     * @param image
     *            The output image
     */
    public static void read(String fileName, Image image)
    {
        BufferedImage bufferedImage;
        try
        {
            bufferedImage = ImageIO.read(new File(fileName));
        }
        catch (Exception e)
        {
        	String message = "Error occured while attempting to read file: " + fileName;
            System.out.println(message);
            System.err.println(e);
            e.printStackTrace();
            fail(message);
            return;
        }
        
        boolean allBlack = true;
        Color pixelColor = new Color();
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int rgb = bufferedImage.getRGB(x, (image.getHeight() - 1 - y));
                double blue = Math.pow((rgb & LOW_8_BITS) / MAX_BYTE, GAMMA);
                double green = Math.pow(((rgb >> 8) & LOW_8_BITS) / MAX_BYTE,
                        GAMMA);
                double red = Math.pow(((rgb >> 16) & LOW_8_BITS) / MAX_BYTE,
                        GAMMA);
                pixelColor.set(red, green, blue);
                image.setPixelColor(pixelColor, x, y);
                if (pixelColor.toInt() != 0)
                {
                	allBlack = false;
                }
            }
        }
        assertEquals("read image that is all black.", allBlack, false);
    }

    /**
     * Computes the root mean square difference between the two images. If they
     * have different sizes it returns infinity.
     * 
     * @param im1
     *            The first image.
     * @param im2
     *            The second image.
     * @return The root mean square difference.
     */
    private static double difference(Image im1, Image im2)
    {
        if (im1.getWidth() != im2.getWidth()
                || im1.getHeight() != im2.getHeight())
        {
            System.out.println("Error: Images have different sizes");
            return Double.POSITIVE_INFINITY;
        }
        int width = im1.getWidth();
        int height = im1.getHeight();
        Color color1 = new Color();
        Color color2 = new Color();
        double mse = 0.0;
        double diff = 0.0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                im1.getPixelColor(color1, x, y);
                im2.getPixelColor(color2, x, y);
                diff = color1.r - color2.r;
                mse += diff * diff;
                diff = color1.g - color2.g;
                mse += diff * diff;
                diff = color1.b - color2.b;
                mse += diff * diff;

            }
        }
        mse /= (width * height);
        return Math.sqrt(mse);
    }

    /**
     * Simulates the process of writing a PNG and then reading it back in again.
     * 
     * @param image
     *            The image to change.
     */
    private static void writeRead(Image image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        boolean allBlack = true;
        Color pixelColor = new Color();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                image.getPixelColor(pixelColor, x, y);
                pixelColor.gammaCorrect(GAMMA);
                int rgb = pixelColor.toInt();
                double blue = Math.pow((rgb & LOW_8_BITS) / MAX_BYTE, GAMMA);
                double green = Math.pow(((rgb >> 8) & LOW_8_BITS) / MAX_BYTE,
                        GAMMA);
                double red = Math.pow(((rgb >> 16) & LOW_8_BITS) / MAX_BYTE,
                        GAMMA);
                pixelColor.set(red, green, blue);
                image.setPixelColor(pixelColor, x, y);
                if (pixelColor.toInt() != 0)
                {
                	allBlack = false;
                }
            }
        }
        assertEquals("writeRead image is all black.", allBlack, false);
    }

}
