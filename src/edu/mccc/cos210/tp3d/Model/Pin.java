package edu.mccc.cos210.tp3d.Model;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import com.cbthinkx.util.Debug;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * Pin
 */
public class Pin implements MovableObject {
	private int pin;
	private float theta;
	private float alpha;
	private float mass;
	private float thetaV;
	private float alphaV;
	private Vector3f velocity;
	private TransformGroup thetaTG;
	private TransformGroup alphaTG;
	private TransformGroup posTG;
	private BranchGroup pinBG;
	/**
	 * Constructor with no paramaters
	 * Will load the pin with an object loader and place it in a TransformGroup.
	 */
	public Pin() {
		Debug.println("Pin.Pin()");
		Scene s = null;
		try {
			ObjectFile of = new ObjectFile();
			of.setFlags(ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
			s = of.load("Textures/pinobject2.obj");
		} catch(java.io.FileNotFoundException ex) {
			System.out.println("Pin not found");
			System.exit(-1);
		}
		pinBG = new BranchGroup();
		thetaTG = new TransformGroup();
		alphaTG = new TransformGroup();
		posTG = new TransformGroup();
		mass = 1.5f;
		thetaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		thetaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		alphaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		alphaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		posTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		posTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		pinBG.addChild(posTG);
		posTG.addChild(thetaTG);
		thetaTG.addChild(alphaTG);
		BranchGroup bg = s.getSceneGroup();
		Shape3D s3d = (Shape3D) bg.getChild(0);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		GeometryArray g = (GeometryArray) s3d.getGeometry();
		s3d.setGeometry(g);
		s3d.setAppearance(setAppearance(new File("Textures/pin3.jpg")));
		s3d = (Shape3D) bg.getChild(1);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		g = (GeometryArray) s3d.getGeometry();
		s3d.setGeometry(g);
		s3d.setAppearance(setAppearance(new File("Textures/pin3.jpg")));
		s3d = (Shape3D) bg.getChild(2);
		s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		g = (GeometryArray) s3d.getGeometry();
		s3d.setGeometry(g);
		s3d.setAppearance(setAppearance(new File("Textures/pin2.jpg")));
		alphaTG.addChild(bg);
	}
	/**
	 * Handles the Texture and Appearance
	 * @param fileName TFile to load the texture from.
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
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		appearance.setTexture(texture);
		return appearance;
	}
	/**
	 * set the rotation about the y axis.
	 * @param theta theta.
	 */
	public void setTheta(float theta) {
		int x;
		if (theta <= Math.PI) {
			if (thetaV >= -Math.PI) {
				this.theta = theta;
			} else {
				x = (int)(theta /(2 * (float)Math.PI));
				theta += 2 * (float)Math.PI * x;
				this.theta = theta;
			}
		} else {
			x = (int)(theta /(2 * (float)Math.PI));
			theta -= 2 * (float)Math.PI * x;
			this.theta = theta;
		}
	}
	/**
	 * get the rotation about the y axis.
	 * @return theta.
	 */
	public float getTheta() {
		return this.theta;
	}
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getThetaTG() {
		return this.thetaTG;
	}
	/**
	 * set the rotation about the x axis.
	 * @param alpha alpha.
	 */
	public void setAlpha(float alpha) {
		if (alpha >= 0) {
			if (alpha <= (float)Math.PI / 2) {
				this.alpha = alpha;
			} else {
				this.alpha = (float)Math.PI / 2;
				this.alphaV = 0.0f;
			}
		} else {
			this.alpha = 0.0f;
		}
	}
	/**
	 * get the rotation about the x axis.
	 * @return alpha.
	 */
	public float getAlpha() {
		return this.alpha;
	}
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getAlphaTG() {
		return this.alphaTG;
	}
	/**
	 * set the position in 3D space.
	 * @param position position.
	 */
	public void setPosition(Vector3f position) {
		Debug.println("Pin.setPosition");
		Transform3D newPosition = new Transform3D();
		newPosition.set(position);
		this.posTG.setTransform(newPosition);
		//this.position = new Vector3f(position);
	}
	/**
	 * get the position in 3D space.
	 * @return postion.
	 */
	public Vector3f getPosition() {
		Transform3D posT = new Transform3D();
		Vector3f pos = new Vector3f();
		this.posTG.getTransform(posT);
		posT.get(pos);
		return pos;
	}
	/**
	 * get the transform group representing the position of this object.
	 * @return the transform group representing this object's position.
	 */
	public TransformGroup getPosTG() {
		return this.posTG;
	}
	/**
	 * set the mass.
	 * @param mass mass.
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}
	/**
	 * get the mass.
	 * @return mass.
	 */
	public float getMass() {
		return this.mass;
	}
	/**
	 * set the horizontal angular velocity.
	 * @param thetaV horizontal angular velocity.
	 */
	public void setThetaV(float thetaV) {
		this.thetaV = thetaV;
	}
	/**
	 * Get the horizontal angular velocity.
	 * @return horizontal angular velocity.
	 */
	public float getThetaV() {
		return this.thetaV;
	}
	/**
	 * Set the vertical angular velocity.
	 * @param alphaV vertical angular velocity.
	 */
	public void setAlphaV(float alphaV) {
		this.alphaV = alphaV;
	}
	/**
	 * Get the vertical angular velocity.
	 * @return vertical angular velocity.
	 */
	public float getAlphaV() {
		return this.alphaV;
	}
	/**
	 * Set the velocity.
	 * @param velocity velocity.
	 */
	public void setVelocity(Vector3f velocity) {
		this.velocity = new Vector3f(velocity);
	}
	/**
	 * Get the velocity.
	 * @return velocity.
	 */
	public Vector3f getVelocity() {
		return new Vector3f(velocity);
	}
	/**
	 * Get the pin's BranchGroup.
	 * @return pinBG. The BranchGroup containing the pin.
	 */
	public BranchGroup getBG() {
		Debug.println("Pin.getBG()");
		return pinBG;
	}
}
