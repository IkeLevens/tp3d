package edu.mccc.cos210.tp3d.View;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.vecmath.*;
import javax.swing.*;
/**
 * The Lane. This class is responsible for render the lane.
 */
public class Lane {
	public static final float LANE_WIDTH = 2.1f;
	private BranchGroup bg;
	private TransformGroup tg;
	/**
	 * Constructs the lane object.
	 */
	public Lane() {
		bg = new BranchGroup();
		tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3f(0.0f, -0.1524f, 9.0f));
		tg.setTransform(t3d);
		tg.addChild(createLane());
		tg.addChild(createLaneBase());
		tg.addChild(createLaneApproach());
		tg.addChild(createPinDeck());
		tg.addChild(createLeftDivider());
		tg.addChild(createRightDivider());
		bg.addChild(tg);
	}
	/**
	 * Returns the BranchGroup containing all of the lane's components.
	 */
	public BranchGroup getBG() {
		return this.bg;
	}
	/**
	 * String output.
	 * @return String representation of the Lane.
	 */
	public String toString() {
		return "Why did you try to println this ?";
	}
	/**
	 * Handles the Texture and Appearance
	 * @param fileName The File name of the texture.
	 * @param whatMaterial Used to select which material to apply.
	 */
	private Appearance setAppearance(File fileName, int whatMaterial) {
		Material material = null;
		if (whatMaterial == 1) {
			material = new Material(
				new Color3f(1.0f, 1.0f, 1.0f),	// AmbientColor
	 			new Color3f(0.0f, 0.0f, 0.0f),	// EmissiveColor
	 			new Color3f(0.5f, 0.5f, 0.5f),	// DiffuseColor
	 			new Color3f(1.0f, 1.0f, 1.0f),	// SepcularColor
	 			96.0f							// Shininess
	 		);
		} else {
			material = new Material(
				new Color3f(1.0f, 1.0f, 1.0f),	// AmbientColor
	 			new Color3f(0.0f, 0.0f, 0.0f),	// EmissiveColor
	 			new Color3f(0.5f, 0.5f, 0.5f),	// DiffuseColor
	 			new Color3f(1.0f, 1.0f, 1.0f),	// SepcularColor
	 			96.0f							// Shininess
	 		);
		}
		Appearance appearance = new Appearance();
		appearance.setMaterial(material);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(fileName);
		} catch (Exception e) {
			System.out.println("Can not find the file!");
		}
		TextureLoader textureLoader = new TextureLoader(bi);
		Texture texture = textureLoader.getTexture();
		texture.setBoundaryModeS(Texture.CLAMP);
		texture.setBoundaryModeT(Texture.CLAMP);
		appearance.setTexture(texture);
		return appearance;
	}
	/**
	 * Sets the normals.
	 * @param qa Sets the normals to the given QuadArray.
	 */
	private void setNormals(QuadArray qa) {
		GeometryInfo gi = new GeometryInfo(qa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		qa.setNormals(0, gi.getNormals());
		float[] fa = new float[48];
		fa[1] = fa[4] = fa[6] = fa[7] = 1.0f;
		for (int i = 8; i < fa.length; i += 8) {
			fa[i + 1] = fa[i + 4] = fa[i + 6] = fa[i + 7] = 0.2f;
		}
		qa.setTextureCoordinates(0, 0, fa);
	}
	/**
	 * Creates the hardwood floor for the lane.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createLane() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				-0.5271,  0.1524, -9.0000,
				-0.5271,  0.1524,  9.0000,
				 0.5271,  0.1524,  9.0000,
				 0.5271,  0.1524, -9.0000,

				-0.5271,  0.0762,  9.0000,
				-0.5271,  0.0762, -9.0000,
				 0.5271,  0.0762, -9.0000,
				 0.5271,  0.0762,  9.0000,

				-0.5271,  0.1524,  9.0000,
				-0.5271,  0.0762,  9.0000,
				 0.5271,  0.0762,  9.0000,
				 0.5271,  0.1524,  9.0000,

				 0.5271,  0.1524, -9.0000,
				 0.5271,  0.0762, -9.0000,
				-0.5271,  0.0762, -9.0000,
				-0.5271,  0.1524, -9.0000,

				-0.5271,  0.1524, -9.0000,
				-0.5271,  0.0762, -9.0000,
				-0.5271,  0.0762,  9.0000,
				-0.5271,  0.1524,  9.0000,

				 0.5271,  0.1524,  9.0000,
				 0.5271,  0.0762,  9.0000,
				 0.5271,  0.0762, -9.0000,
				 0.5271,  0.1524, -9.0000
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/wood.jpg"),1));
		return s3d;
	}
	/**
	 * Creates the bottom of the lane.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createLaneBase() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				-1.0471,  0.0762, -13.5720,
				-1.0471,  0.0762,  	9.9100,
				 1.0471,  0.0762,  	9.9100,
				 1.0471,  0.0762, -13.5720,

				-1.0471,  0.0000,   9.9100,
				-1.0471,  0.0000, -13.5720,
				 1.0471,  0.0000, -13.5720,
				 1.0471,  0.0000,   9.9100,

				-1.0471,  0.0762,   9.9100,
				-1.0471,  0.0000,   9.9100,
				 1.0471,  0.0000,   9.9100,
				 1.0471,  0.0762,   9.9100,

				 1.0471,  0.0762, -13.5720,
				 1.0471,  0.0000, -13.5720,
				-1.0471,  0.0000, -13.5720,
				-1.0471,  0.0762, -13.5720,

				-1.0471,  0.0762, -13.5720,
				-1.0471,  0.0000, -13.5720,
				-1.0471,  0.0000,   9.9100,
				-1.0471,  0.0762,   9.9100,

				 1.0471,  0.0762,   9.9100,
				 1.0471,  0.0000,   9.9100,
				 1.0471,  0.0000, -13.5720,
				 1.0471,  0.0762, -13.5720
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/metal.jpg"),2));
		return s3d;
	}
	/**
	 * Creates the lane Approach.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createLaneApproach() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				-1.0471,  0.1524, -13.5720,
				-1.0471,  0.1524,  -9.0000,
				 1.0471,  0.1524,  -9.0000,
				 1.0471,  0.1524, -13.5720,

				-1.0471,  0.0762,  -9.0000,
				-1.0471,  0.0762, -13.5720,
				 1.0471,  0.0762, -13.5720,
				 1.0471,  0.0762,  -9.0000,

				-1.0471,  0.1524,  -9.0000,
				-1.0471,  0.0762,  -9.0000,
				 1.0471,  0.0762,  -9.0000,
				 1.0471,  0.1524,  -9.0000,

				 1.0471,  0.1524, -13.5720,
				 1.0471,  0.0762, -13.5720,
				-1.0471,  0.0762, -13.5720,
				-1.0471,  0.1524, -13.5720,

				-1.0471,  0.1524, -13.5720,
				-1.0471,  0.0762, -13.5720,
				-1.0471,  0.0762,  -9.0000,
				-1.0471,  0.1524,  -9.0000,

				 1.0471,  0.1524,  -9.0000,
				 1.0471,  0.0762,  -9.0000,
				 1.0471,  0.0762, -13.5720,
				 1.0471,  0.1524, -13.5720
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/foulline.jpg"),1));
		return s3d;
	}
	/**
	 * Creates the Pin deck.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createPinDeck() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				-0.5271,  0.1524,  9.0000,
				-0.5271,  0.1524,  9.9100,
				 0.5271,  0.1524,  9.9100,
				 0.5271,  0.1524,  9.0000,

				-0.5271,  0.0762,  9.9100,
				-0.5271,  0.0762,  9.0000,
				 0.5271,  0.0762,  9.0000,
				 0.5271,  0.0762,  9.9100,

				-0.5271,  0.1524,  9.9100,
				-0.5271,  0.0762,  9.9100,
				 0.5271,  0.0762,  9.9100,
				 0.5271,  0.1524,  9.9100,

				 0.5271,  0.1524,  9.0000,
				 0.5271,  0.0762,  9.0000,
				-0.5271,  0.0762,  9.0000,
				-0.5271,  0.1524,  9.0000,

				-0.5271,  0.1524,  9.0000,
				-0.5271,  0.0762,  9.0000,
				-0.5271,  0.0762,  9.9100,
				-0.5271,  0.1524,  9.9100,

				 0.5271,  0.1524,  9.9100,
				 0.5271,  0.0762,  9.9100,
				 0.5271,  0.0762,  9.0000,
				 0.5271,  0.1524,  9.0000
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/holes.jpg"),1));
		return s3d;
	}
	/**
	 * Creates the left lane divider.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createLeftDivider() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				-1.0471,  0.3048,  -9.0000,
				-1.0471,  0.3048,   9.9100,
				-0.7471,  0.3048,   9.9100,
				-0.7471,  0.3048,  -9.0000,

				-1.0471,  0.0762,   9.9100,
				-1.0471,  0.0762,  -9.0000,
				-0.7471,  0.0762,  -9.0000,
				-0.7471,  0.0762,   9.9100,

				-1.0471,  0.3048,   9.9100,
				-1.0471,  0.0762,   9.9100,
				-0.7471,  0.0762,   9.9100,
				-0.7471,  0.3048,   9.9100,

				-0.7471,  0.3048,  -9.0000,
				-0.7471,  0.0762,  -9.0000,
				-1.0471,  0.0762,  -9.0000,
				-1.0471,  0.3048,  -9.0000,

				-1.0471,  0.3048,  -9.0000,
				-1.0471,  0.0762,  -9.0000,
				-1.0471,  0.0762,   9.9100,
				-1.0471,  0.3048,   9.9100,

				-0.7471,  0.3048,   9.9100,
				-0.7471,  0.0762,   9.9100,
				-0.7471,  0.0762,  -9.0000,
				-0.7471,  0.3048,  -9.0000,
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/diamond.jpg"),1));
		return s3d;
	}
	/**
	 * Creates the right lane divider.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createRightDivider() {
		QuadArray qa = new QuadArray(
			24,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2
		);
 		qa.setCapability(Geometry.ALLOW_INTERSECT);
 		qa.setCapability(GeometryArray.ALLOW_COUNT_READ);
 		qa.setCapability(GeometryArray.ALLOW_FORMAT_READ);
 		qa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		qa.setCoordinates(
			0,
			new double[] {
				 0.7471,  0.3048,  -9.0000,
				 0.7471,  0.3048,   9.9100,
				 1.0471,  0.3048,   9.9100,
				 1.0471,  0.3048,  -9.0000,

				 0.7471,  0.0762,   9.9100,
				 0.7471,  0.0762,  -9.0000,
				 1.0471,  0.0762,  -9.0000,
				 1.0471,  0.0762,   9.9100,

				 0.7471,  0.3048,   9.9100,
				 0.7471,  0.0762,   9.9100,
				 1.0471,  0.0762,   9.9100,
				 1.0471,  0.3048,   9.9100,

				 1.0471,  0.3048,  -9.0000,
				 1.0471,  0.0762,  -9.0000,
				 0.7471,  0.0762,  -9.0000,
				 0.7471,  0.3048,  -9.0000,

				 0.7471,  0.3048,  -9.0000,
				 0.7471,  0.0762,  -9.0000,
				 0.7471,  0.0762,   9.9100,
				 0.7471,  0.3048,   9.9100,

				 1.0471,  0.3048,   9.9100,
				 1.0471,  0.0762,   9.9100,
				 1.0471,  0.0762,  -9.0000,
				 1.0471,  0.3048,  -9.0000,
			}
		);
		setNormals(qa);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/diamond.jpg"),1));
		return s3d;
	}
}
