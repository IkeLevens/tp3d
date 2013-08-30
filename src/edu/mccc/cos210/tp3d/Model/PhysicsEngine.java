package edu.mccc.cos210.tp3d.Model;
import com.cbthinkx.util.Debug;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.View.*;
import edu.mccc.cos210.tp3d.Model.*;
import java.util.*;
import javax.media.j3d.Alpha;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.*;
/**
 * PhysicsEngine.  The physics engine handles only objects which are implemetors
 * of the MovableObject interface.
 */
public class PhysicsEngine implements Runnable {
	public static final float BALL_PATH_LENGTH = 19.0f;
	public static final float BALL_PATH_WIDTH = 1.25f;
	public static final float COLLISION_DISTANCE = 0.18f;
	public static final Vector3f[] PIN_POSITIONS = {
		new Vector3f( 0.00f, 0.110f,  00.00f),
		new Vector3f( 0.00f, 0.154f, 18.00f),
		new Vector3f( 0.1524f, 0.154f, 18.2640f),
		new Vector3f(-0.1524f, 0.154f, 18.2640f),
		new Vector3f( 0.3048f, 0.154f, 18.5280f),
		new Vector3f( 0.00f, 0.154f, 18.5280f),
		new Vector3f(-0.3048f, 0.154f, 18.5280f),
		new Vector3f( 0.4572f, 0.154f, 18.792f),
		new Vector3f( 0.1524f, 0.154f, 18.792f),
		new Vector3f(-0.1524f, 0.154f, 18.792f),
		new Vector3f(-0.4572f, 0.154f, 18.792f),
	};
	public static final Vector3f HIDDEN = new Vector3f(0.0f, -100.0f, 0.0f);
	public static final int THROW_BALL = 0;
	public static final int RESET_PINS = 1;
	private boolean[] moving;
	private boolean[] hide;
	private int power;
	private int direction;
	private int spin;
	private Controller controller;
	private Model model;
	private ScoreModel score;
	private ControlPanel controlPanel;
	private Sound sound;
	private FlowControl fc;
	private Queue<Integer> workQueue;
	/**
	 * constructor with six paramaters.  this is the only constructor available for this class because all
	 * paramaters are of object types, and thus cannot have default values.
	 * @param controller A Controller which controls tp3d.
	 * @param model A model of the MovableObject implementors in tp3d.
	 * @param score A ScoreModel which is used to maintain player score data.
	 * @param controlPanel A control panel in the GUI.
	 * @param sound An instance of the Sound class.  The sound class generates sound effects in tp3d.
	 * @param fc The flowcontrol holds state data for tp3d.
	 */
	public PhysicsEngine(Controller controller, Model model, ScoreModel score, ControlPanel controlPanel, Sound sound, FlowControl fc) {
		Debug.println("PhysicsEngine.PhysicsEngine()");
		this.controller = controller;
		this.model = model;
		this.score = score;
		this.controlPanel = controlPanel;
		this.sound = sound;
		this.fc = fc;
		this.workQueue = new LinkedList<Integer>();
		this.moving = new boolean[11];
		this.hide = new boolean[11];
	}
	/**
	 * adds work to the work queue.  The workQueue serves the function of providing a synchronized pass of information between
	 * other threads and the physics thread.
	 */
	public void doIt(int it) {
		Debug.println("PhysicsEngine.doIt()");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		synchronized (workQueue) {
			workQueue.offer(it);
			workQueue.notify();
		}
	}
	/**
	 * This method is the basis for the physics thread in tp3d.  This method spends most of its time either waiting for work to
	 * be added to workQueue, or performing physics calculations for MovableObject implementors.  The physics engine runs on its
	 * own thread because it does large quantities of complex calculations that should not be handled on the AWT Event Dispatch
	 * Thread.  By using multi-threading the physics engine can perform these calculations without affecting GUI repsonsiveness.
	 */
	public void run() {
		Debug.println("PhysicsEngine.run()");
		synchronized (workQueue) {
			while (true) {
				if (workQueue.peek() == null) {
					try {
						workQueue.wait();
					} catch (InterruptedException e) {
						continue;
					}
				} else {
					int doWhat = workQueue.poll();
					switch (doWhat) {
						case 0:
							controlPanel.updateSliders();
							power = controlPanel.getPower();
							direction = -controlPanel.getDirection();
							spin = -controlPanel.getSpin();
							throwBall();
							break;
						case 1:
							resetPins();
							break;
					}
				}
			}
		}
	}
	/**
	 * handles the physics of resetting the pins.  All pins are moved to the posistions specified for them in the
	 * PIN_POSITIONS constant.
	 */
	private void resetPins() {
		Debug.println("PhysicsEngine.resetPins()");
		MovableObject[] pins = model.getPins();
		for (int i = 0; i < pins.length; i++) {
			MovableObject pin = pins[i];
			moving[i] = false;
			hide[i] = false;
			TransformGroup tg = pin.getThetaTG();
			Transform3D t3d = new Transform3D();
			tg.setTransform(t3d);
			tg = pin.getAlphaTG();
			tg.setTransform(t3d);
			tg = pin.getPosTG();
			t3d.set(PIN_POSITIONS[i]);
			tg.setTransform(t3d);
			pin.setTheta(0.0f);
			pin.setAlpha(0.0f);
			pin.setPosition(PIN_POSITIONS[i]);
			pin.setThetaV(0.0f);
			pin.setAlphaV(0.0f);
			pin.setVelocity(
				new Vector3f(0.0f, 0.0f, 0.0f)
			);
		}
	}
	/**
	 * handles the physics of resetting the ball.  The ball is moved to the position specified for it in the
	 * PIN_POSITIONS constant.
	 */
	private void resetBall() {
		Debug.println("PhysicsEngine.resetBall()");
		MovableObject[] pins = model.getPins();
		MovableObject pin;
		for (int i = 0; i < pins.length; i++) {
			pin = pins[i];
			pin.setVelocity(new Vector3f());
			moving[i] = false;
		}
		pins[0].setPosition(PIN_POSITIONS[0]);
	}
	/**
	 * handles the physics of a ball throw.  Power and direction as set by the user using sliders on the
	 * ControlPanel determine the initial velocity of the ball.  Spin is handled as a constant acceleration.
	 * The physics engine handles movement in discrete segments of approximately 11ms, dependent on system
	 * time scheduling.  Time is measured by the Alpha timer.  During each segment, the method doCollisonCheck
	 * will be called to assess collision checks between implementors of MovableObject.  collisons with other
	 * objects are handled differently.
	 */
	private void throwBall() {
		Debug.println("PhysicsEngine.throwBall()");
		sound.doIt(Sound.BALL_ROLL);
		MovableObject[]pins = model.getPins();
		MovableObject ball = pins[0];
		Vector3f position;
		float[] pos = new float[3];
		float theta = (float)Math.atan(BALL_PATH_WIDTH / BALL_PATH_LENGTH);
		theta *= direction / 10.0f;
		float vz0 = 25.0f + 4.5f * power;
		vz0 *= (float)Math.cos(theta);
		float vx0 = 25.0f + 4.5f * power;
		vx0 *= (float)Math.sin(theta);
		float vy = 0;
		float vx = vx0;
		float vz = vz0;
		float[] ballV = {vx0, vy, vz0};
		Vector3f ballVelocity = new Vector3f(ballV);
		ball.setVelocity(ballVelocity);
		Alpha timer = new Alpha(1, 0, 0, 7000, 0, 0);
		timer.setStartTime(System.currentTimeMillis());
		boolean rolling = true;
		boolean leftGutter = false;
		boolean rightGutter = false;
		boolean clatter = false;
		boolean lastFrame = false;
		float lastTime = 0.0f;
		float time = 0.0f;
		float ballX;
		float ballY = 0.11f;
		float ballZ;
		moving[0] = true;
		while (rolling) {
			lastTime = time;
			time = timer.value();
			clatter = doCollisionCheck(clatter);
			update(time, lastTime);
			position = ball.getPosition();
			ballZ = vz * time;
			if (rightGutter) {
				position = new Vector3f(
					BALL_PATH_WIDTH / 2,
					0.0f,
					ballZ
				);
				ball.setPosition(position);
			} else {
				if (leftGutter) {
					position = new Vector3f(
						-(BALL_PATH_WIDTH / 2),
						0.0f,
						ballZ
					);
					ball.setPosition(position);
				} else {
					ballVelocity = ball.getVelocity();
					ballVelocity.get(ballV);
					vx = ballV[0];
					vx = vx + (25.0f + 4.5f * power) * spin / 10.0f * time  * time;
					ballV[0] = vx;
//					Debug.println("ballVelocity: " + ballVelocity);
					ballVelocity.set(ballV);
					ballVelocity.scale(time - lastTime);
					position.add(ballVelocity);
					position.get(pos);
					ballX = pos[0];
					ballZ = pos[2];
					ball.setPosition(position);
					if (ballX > BALL_PATH_WIDTH / 2 && ballZ < BALL_PATH_LENGTH) {
						rightGutter = true;
						sound.doIt(Sound.GUTTER);
					} else {
						if (ballX < -(BALL_PATH_WIDTH / 2) && ballZ < BALL_PATH_LENGTH) {
							leftGutter = true;
							sound.doIt(Sound.GUTTER);
						}
					}
				}
			}
			if (timer.value() >= 1.0f) {
				rolling = false;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Debug.println(e.getMessage());
				continue;
			}
		}
		int fallen = 0;
		int hidden = 0;
		for (int i = 1; i < moving.length; i++) {
			if (moving[i]) {
				pins[i].setPosition(HIDDEN);
				hide[i] = true;
				fallen++;
			} else {
				if (hide[i]) {
					hidden++;
				}
			}
		}
		int player = fc.getPlayer();
		int ballC = fc.getBall();
		int frame = fc.getFrame();
		if (fallen + hidden == 10) {
			if (ballC == 1 && frame != 10) {
				//strike
				score.getScores(player).getFrames(frame).setBall1(10);
				score.getScores(player).getFrames(frame).setStrike(true);
				fc.nextFrame();
				resetPins();
			} else {
				if (ballC == 2 && frame != 10) {
					//spare
					score.getScores(player).getFrames(frame).setBall2(fallen);
					score.getScores(player).getFrames(frame).setSpare(true);
					fc.nextFrame();
					resetPins();
				} else {
					//can only happen on tenth frame
					if (ballC == 1) {
						score.getScores(player).getFrames(frame).setBall1(fallen);
						score.getScores(player).getFrames(frame).setStrike(true);
						fc.nextBall();
						resetPins();
					} else {
						if (ballC == 2) {
							score.getScores(player).getFrames(frame).setBall2(fallen);
							if (hidden != 0) {
								score.getScores(player).getFrames(frame).setSpare(true);
							}
							fc.nextBall();
							resetPins();
						} else {
							//ball3
							score.getScores(player).getFrames(frame).setBall3(fallen);
							if (fc.getPlayer() != fc.getNumPlayers()) {
								fc.nextFrame();
								resetPins();
							} else {
								lastFrame = true;
							}
						}
					}
				}
			}
		} else {
			//less than 10 pins are down
			if (ballC == 1) {
				score.getScores(player).getFrames(frame).setBall1(fallen);
				fc.nextBall();
				resetBall();
			} else {
				if (ballC == 2 && frame != 10) {
					score.getScores(player).getFrames(frame).setBall2(fallen);
					fc.nextBall();
					resetPins();
				} else {
					if (ballC == 2) {
						score.getScores(player).getFrames(frame).setBall2(fallen);
						if (score.getScores(player).getFrames(frame).getisStrike()) {
							fc.nextBall();
							resetBall();
						} else {
							if (fc.getPlayer() != fc.getNumPlayers()) {
								fc.nextFrame();
								resetPins();
							} else {
								lastFrame = true;
							}
						}
					} else {
						//ball 3 frame 10
						score.getScores(player).getFrames(frame).setBall3(fallen);
						if (fc.getPlayer() != fc.getNumPlayers()) {
							fc.nextFrame();
							resetPins();
						} else {
							lastFrame = true;
						}
					}
				}
			}
		}
		ScoreCalc.calculate(score, fc);
		if (!lastFrame) {
			controlPanel.enableButton();
		}
		controller.repaint();
	}
	/**
	 * Check for a collison between movable objects.  Collisons are simlified by performing only collision avoidance based
	 * on a fixed COLLISION_DISTANCE constant.
	 * @param clatter a boolean flagging whether the clatter sound has been played for this throw.
	 */
	private boolean doCollisionCheck(boolean clatter) {
		Debug.println("PhysicsEngine.doCollisionCheck()");
		Vector3f here;
		Vector3f there;
		for (int i = 0; i < moving.length; i++) {
			if (moving[i]) {
				MovableObject[] pins = model.getPins();
				here = pins[i].getPosition();
				for (int j = 1; j < pins.length; j++) {
					if (((i != j && !moving[j]) || i < j) && !hide[j]) {
						there = pins[j].getPosition();
						//Debug.println("there: " + there + " here: " + here);
						there.sub(here);
//						Debug.println("distance: " + there.length());
						if (there.length() < COLLISION_DISTANCE) {
							//a collision occured
							doCollision(i, j, there, clatter);
							clatter = true;
						}
					}
				}
			}
		}
		return clatter;
	}
	/**
	 * Handlde a collision between movable objects.  Collions are handled as a simplified form of newtonian physics
	 * for ellastic collisions between balls.  This is then overlaid with an approximation of spin and falling.
	 * @param pin1 integer identifying the first object colliding.
	 * @param pin2 integer identifying the second object colliding.
	 * @param distance Vector3f representing the distance between the two colliding objects.
	 */
	private void doCollision(int pin1, int pin2, Vector3f distance, boolean clatter) {
		Debug.println("PhysicsEngine.doCollision()");
		MovableObject[] pins = model.getPins();
		MovableObject p1 = pins[pin1];
		MovableObject p2 = pins[pin2];
		Vector3f p1v = p1.getVelocity();
		Vector3f p2v;
		float m1 = p1.getMass();
		float m2 = p2.getMass();
		if (!clatter) {
			sound.doIt(Sound.CLATTER);
		}
		if (moving[pin2]) {
			p2v = p2.getVelocity();
			p1v.sub(p2v);
		} else {
			p2v = new Vector3f();
		}
//		Debug.println("p1v: " + p1v + " p2v: " + p2v);
		float[] p1Array= new float[3];
		p1v.get(p1Array);
		float x1 = p1Array[0];
		float y1 = p1Array[1];
		float z1 = p1Array[2];
		float[] p2Array = new float[3];
		p2v.get(p2Array);
		float x2 = 0;
		float y2 = 0;
		float z2 = 0;
		float[] distArray = new float[3];
		distance.get(distArray);
		float xd = distArray[0];
		float yd = distArray[1];
		float zd = distArray[2];
		float theta = (float)Math.atan(zd / xd);
		float alpha = (float)Math.atan(yd / xd);
		float dx = p1v.length() * (float)Math.cos(theta);
		float dz = p1v.length() * (float)Math.sin(theta);
		x2 = dx * (m1 / m2);
		y2 = 0;
		z2 = dz * (m1 / m2);
		x1 = x1 - dx * (m2 / m1);
		y1 = 0;
		z1 = z1 - dz * (m2 / m1);
		p1Array[0] = x1;
		p1Array[1] = y1;
		p1Array[2] = z1;
		p2Array[0] = x2;
		p2Array[1] = y2;
		p2Array[2] = z2;
		Vector3f p1v2 = new Vector3f(p1Array);
		Vector3f p2v2 = new Vector3f(p2Array);
		p1v2.add(p2v);
		p2v2.add(p2v);
//		Debug.println("p1v2: " + p1v2 + " p2v2: " + p2v2);
		p1.setVelocity(p1v2);
		p2.setVelocity(p2v2);
		if (moving[pin2]) {
			p2.setThetaV(p2.getTheta() - theta);
			p1.setThetaV(p1.getTheta() - theta);
		} else {
			p2.setTheta(theta);
			p2.setAlphaV(0.05f);
		}
		moving[pin2] = true;
	}
	/**
	 * update the positions of the pins by one tick.  The pins are moved and rotated based on the velocities
	 * they have been assigned by doCollision.  Entropy is then enacted.
	 * @param time a float value representing time between 0.0 and 1.0.
	 * @param lastTime the previous value of time.
	 */
	private void update(float time, float lastTime) {
		Debug.println("PhysicsEngine.update()");
		MovableObject[] pins = model.getPins();
		MovableObject pin;
		Vector3f pinVelocity;
		Vector3f position;
		Transform3D t3d = new Transform3D();
		Transform3D temp;
		float newTheta;
		float newAlpha;
		for (int i = 1; i < pins.length; i++) {
			if (moving[i]) {
				pin = pins[i];
				position = pin.getPosition();
				pinVelocity = pin.getVelocity();
				pinVelocity.scale(time - lastTime);
				position.add(pinVelocity);
				pin.setPosition(position);
				pinVelocity = pin.getVelocity();
				pinVelocity.scale(0.6f);
				pin.setVelocity(pinVelocity);
				newTheta = pin.getTheta();
				newTheta += pin.getThetaV() * time / lastTime;
				float thetaV = pin.getThetaV();
				Debug.println("thetaV: " + thetaV);
				thetaV *= 0.95f;
				pin.setThetaV(thetaV);
				pin.setTheta(newTheta);
				pin.getThetaTG().getTransform(t3d);
				temp = new Transform3D();
				temp.rotY(newTheta);
				t3d.mul(temp);
//				pin.getThetaTG().setTransform(t3d);
				newAlpha = pin.getAlpha();
				newAlpha += pin.getAlphaV() * time / lastTime;
				pin.setAlpha(newAlpha);
//				pin.getAlphaTG().getTransform(t3d);
				temp = new Transform3D();
				temp.rotZ(newAlpha);
				t3d.mul(temp);
				pin.getAlphaTG().setTransform(t3d);
			}
		}
	}
}
