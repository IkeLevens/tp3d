package edu.mccc.cos210.tp3d.Model;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Primitive.*;
import com.cbthinkx.util.Debug;
/**
 * Ball
 */
public class Ball implements MovableObject {
	private int ball;
	private float theta;
	private float alpha;
	//private Vector3f position;
	private float mass;
	private float thetaV;
	private float alphaV;
	private Vector3f velocity;
	private TransformGroup thetaTG;
	private TransformGroup alphaTG;
	private TransformGroup posTG;
	private BranchGroup ballBG;
	/**
	 * constructor with no paramaters
	 */
	public Ball() {
		Debug.println("Ball.Ball()");
		ball = 0;
		Sphere ballS3D = createBall();
		ballBG = new BranchGroup();
		thetaTG = new TransformGroup();
		alphaTG = new TransformGroup();
		posTG = new TransformGroup();
		mass = 7.0f;
		Debug.println("created TGs");
		thetaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		thetaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		alphaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		alphaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		posTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		posTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Debug.println("set Capabilities");
		ballBG.addChild(posTG);
		posTG.addChild(thetaTG);
		thetaTG.addChild(alphaTG);
		alphaTG.addChild(ballS3D);
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
	public float getTheta(){
		return theta;
	}
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getThetaTG() {
		return thetaTG;
	}
	/**
	 * set the rotation about the x axis.
	 * @param alpha alpha.
	 */
	public void setAlpha(float alpha) {
		if (alpha >= 0) {
			if (alpha <= Math.PI / 2) {
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
		return alpha;
	}
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getAlphaTG() {
		return alphaTG;
	}
	/**
	 * set the position in 3D space.
	 * @param position position.
	 */
	public void setPosition(Vector3f position) {
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
		return posTG;
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
		return mass;
	}
	/**
	 * set the horizontal angular velocity.
	 * @param thetaV horizontal angular velocity.
	 */
	public void setThetaV(float thetaV) {
		this.thetaV = thetaV;
	}
	/**
	 * get the horizontal angular velocity.
	 * @return horizontal angular velocity.
	 */
	public float getThetaV() {
		return thetaV;
	}
	/**
	 * set the vertical angular velocity.
	 * @param alphaV vertical angular velocity.
	 */
	public void setAlphaV(float alphaV) {
		this.alphaV = alphaV;
	}
	/**
	 * get the vertical angular velocity.
	 * @return vertical angular velocity.
	 */
	public float getAlphaV() {
		return alphaV;
	}
	/**
	 * set the velocity.
	 * @param velocity velocity.
	 */
	public void setVelocity(Vector3f velocity) {
		this.velocity = new Vector3f(velocity);
	}
	/**
	 * get the velocity.
	 * @return velocity.
	 */
	public Vector3f getVelocity() {
		return new Vector3f(velocity);
	}
	/**
	 * get the ball's BranchGroup
	 * @return ballBG
	 */
	public BranchGroup getBG() {
		Debug.println("Ball.getBG()");
		return ballBG;
	}
	/**
	 * set the ball
	 * @param selection selection.
	 */
	public void setBall(int selection) {
		this.ball = selection;
	}
	/**
	 * creates a ball.
	 * @return ballS3D.
	 */
	private Sphere createBall() {
		Debug.println("Ball.createBall()");
		Material ballMat = new Material(
			new Color3f(0.3f, 0.0f, 0.0f),
			new Color3f(0.0f, 0.0f, 0.0f),
			new Color3f(1.0f, 0.0f, 0.0f),
			new Color3f(1.0f, 0.6f, 0.8f),
			106.0f
		);
		Appearance ballApp = new Appearance();
		ballApp.setMaterial(ballMat);
		Sphere ballS3D = new Sphere(
			0.11f,
			Primitive.GENERATE_NORMALS,
			50,
			ballApp
		);
		return ballS3D;
	}
}
