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
 * Walls. This class creates the four walls, the ceiling and the floor of the bowling ally.
 */
public class Walls {
	private BranchGroup bg;
	private TransformGroup tg;
	/**
	 * All and all you're just another brick within the wall..
	 */
	public Walls() {
		bg = new BranchGroup();
		tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3f(-Lane.LANE_WIDTH, -0.1524f, 9.0f));
		tg.setTransform(t3d);
		tg.addChild(createBackWall());
		tg.addChild(createFloor());
		tg.addChild(createCeiling());
		tg.addChild(createFrontWall());
		tg.addChild(createRightWall());
		tg.addChild(createLeftWall());
		bg.addChild(tg);
	}
	/**
	 * Returns a BranchGroup that contains all of the
	 */
	public BranchGroup getBG() {
		return this.bg;
	}
	/**
	 * Handles the Texture and Appearance
	 * @param fileName Texture file name.
	 * @return Appearance containing all of the texture data.
	 */
	private Appearance setAppearance(File fileName) {
		Material material = new Material(
			new Color3f(1.0f, 1.0f, 1.0f),	// AmbientColor
	 		new Color3f(0.0f, 0.0f, 0.0f),	// EmissiveColor
	 		new Color3f(0.5f, 0.5f, 0.5f),	// DiffuseColor
	 		new Color3f(1.0f, 1.0f, 1.0f),	// SepcularColor
	 		96.0f							// Shininess
	 	);
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
	 * @param qa The QuadArray you would like to apply normals to.
	 * @param vertices The number of verticies in te given QuadArray.
	 */
	private void setNormals(QuadArray qa, int vertices) {
		GeometryInfo gi = new GeometryInfo(qa);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		qa.setNormals(0, gi.getNormals());
		float[] fa = new float[vertices * 2];
		fa[1] = fa[4] = fa[6] = fa[7] = 1.0f;
		for (int i = 8; i < fa.length; i += 8) {
			fa[i + 1] = fa[i + 4] = fa[i + 6] = fa[i + 7] = 0.2f;
		}
		qa.setTextureCoordinates(0, 0, fa);
	}
	/**
	 * Creates the Left Wall.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createLeftWall() {
		QuadArray qa = new QuadArray(
			4,
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
				 5.0471,  5.0762, -13.5720,
				 5.0471,  0.0000, -13.5720,
				 5.0471,  0.0000,   9.9100,
				 5.0471,  5.0762,   9.9100
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/leftwall.jpg")));
		return s3d;
	}
	/**
	 * Creates the Back Wall.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createBackWall() {
		QuadArray qa = new QuadArray(
			4,
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
				-1.0471,  5.0762, -13.5720,
				-1.0471,  0.0000, -13.5720,
				 5.0471,  0.0000, -13.5720,
				 5.0471,  5.0762, -13.5720
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/backwall2.jpg")));
		return s3d;
	}
	/**
	 * Creates the Right Wall.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createRightWall() {
		QuadArray qa = new QuadArray(
			4,
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
				-1.0471,  5.0762,   9.9100,
				-1.0471,  0.0000,   9.9100,
				-1.0471,  0.0000, -13.5720,
				-1.0471,  5.0762, -13.5720
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/rightwall.jpg")));
		return s3d;
	}
	/**
	 * Creates the Front Wall.
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createFrontWall() {
		QuadArray qa = new QuadArray(
			4,
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
				 5.0471,  5.0762,   9.9100,
				 5.0471,  0.0000,   9.9100,
				-1.0471,  0.0000,   9.9100,
				-1.0471,  5.0762,   9.9100
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/frontwall.jpg")));
		return s3d;
	}
	/**
	 * Creates the Ceiling
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createCeiling() {
		QuadArray qa = new QuadArray(
			4,
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
				 5.0471,  5.0762, -13.5720,
				 5.0471,  5.0762,  	9.9100,
				-1.0471,  5.0762,  	9.9100,
				-1.0471,  5.0762, -13.5720
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/ceiling2.jpg")));
		return s3d;
	}
	/**
	 * Creates the Floor
	 * @return A Shape3D containing the geometry, appearance and normals.
	 */
	protected Shape3D createFloor() {
		QuadArray qa = new QuadArray(
			4,
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
				 5.0471,  0.0000,   9.9100,
				 5.0471,  0.0000, -13.5720,
				-1.0471,  0.0000, -13.5720,
				-1.0471,  0.0000,   9.9100
			}
		);
		setNormals(qa, 4);
		Shape3D s3d = new Shape3D();
		s3d.addGeometry(qa);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		s3d.setAppearance(setAppearance(new File("Textures/metal.jpg")));
		return s3d;
	}
	/**
	 * String output.
	 * @return String representation of the Walls.
	 */
	public String toString() {
		return "Why did you try to println this ?";
	}
}
