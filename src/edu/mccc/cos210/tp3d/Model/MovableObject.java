package edu.mccc.cos210.tp3d.Model;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * <<MovableObject>>
 * All objects which can move in tp3d will be implementors of this interface.
 */
public interface MovableObject {
	/**
	 * set the rotation about the y axis.
	 * @param theta theta.
	 */
	public void setTheta(float theta);
	/**
	 * get the rotation about the y axis.
	 * @return theta.
	 */
	public float getTheta();
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getThetaTG();
	/**
	 * set the rotation about the x axis.
	 * @param alpha alpha.
	 */
	public void setAlpha(float alpha);
	/**
	 * get the rotation about the x axis.
	 * @return alpha.
	 */
	public float getAlpha();
	/**
	 * get the transform group representing rotation about the y axis.
	 * @return transform group representing rotation about the y axis.
	 */
	public TransformGroup getAlphaTG();
	/**
	 * set the position in 3D space.
	 * @param position position.
	 */
	public void setPosition(Vector3f position);
	/**
	 * get the position in 3D space.
	 * @return postion.
	 */
	public Vector3f getPosition();
	/**
	 * get the transform group representing the position of this object.
	 * @return the transform group representing this object's position.
	 */
	public TransformGroup getPosTG();
	/**
	 * set the mass.
	 * @param mass mass.
	 */
	public void setMass(float mass);
	/**
	 * get the mass.
	 * @return mass.
	 */
	public float getMass();
	/**
	 * set the horizontal angular velocity.
	 * @param thetaV horizontal angular velocity.
	 */
	public void setThetaV(float thetaV);
	/**
	 * get the horizontal angular velocity.
	 * @return horizontal angular velocity.
	 */
	public float getThetaV();
	/**
	 * set the vertical angular velocity.
	 * @param alphaV vertical angular velocity.
	 */
	public void setAlphaV(float alphaV);
	/**
	 * get the vertical angular velocity.
	 * @return vertical angular velocity.
	 */
	public float getAlphaV();
	/**
	 * set the velocity.
	 * @param velocity velocity.
	 */
	public void setVelocity(Vector3f velocity);
	/**
	 * get the velocity.
	 * @return velocity.
	 */
	public Vector3f getVelocity();
	/**
	 * get a BranchGroup to add this object to a scene graph
	 * @return a BranchGroup representation of the movable object to be added to the scenegraph of tp3d.
	 */
	public BranchGroup getBG();
}
