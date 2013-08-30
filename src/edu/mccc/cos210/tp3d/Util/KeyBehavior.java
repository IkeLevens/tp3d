package edu.mccc.cos210.tp3d.Util;
import edu.mccc.cos210.tp3d.Util.*;
import java.awt.AWTEvent;
import java.awt.event.*;
import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.*;
public class KeyBehavior extends Behavior {
	private TransformGroup tg;
	private Transform3D t3d = new Transform3D();
	private WakeupCondition wake;
	private int forwardKey = KeyEvent.VK_UP;
	private int backKey = KeyEvent.VK_DOWN;
	private int leftKey = KeyEvent.VK_LEFT;
	private int rightKey = KeyEvent.VK_RIGHT;
	protected static final double NORMAL = 0.05;
	protected static final double SLOW = 0.005;
	protected double speed = NORMAL;
	protected double rotAmount = Math.PI / 120.0;
	public KeyBehavior(TransformGroup tg) {
		super();
		this.tg = tg;
	}
	public void initialize() {
		WakeupCriterion[] wca = new WakeupCriterion[2];
		wca[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		wca[1] = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
		wake = new WakeupOr(wca);
		wakeupOn(wake);
	}
	public void processStimulus(Enumeration e) {
		WakeupCriterion wc;
		AWTEvent[] awtea;
		while (e.hasMoreElements()) {
			wc = (WakeupCriterion) e.nextElement();
			if (!(wc instanceof WakeupOnAWTEvent)) {
				continue;
			}
			awtea = ((WakeupOnAWTEvent) wc).getAWTEvent();
			for (int i = 0; i < awtea.length; i++) {
				if (awtea[i].getID() == KeyEvent.KEY_PRESSED) {
					processKeyEvent((KeyEvent) awtea[i]);
				}
			}
		}
		wakeupOn(wake);
	}
	protected void processKeyEvent(KeyEvent ke) {
		int kc = ke.getKeyCode();
		if (ke.isShiftDown()) {
			speed = SLOW;
			rotAmount = Math.PI / 360.0;
		} else {
			speed = NORMAL;
			rotAmount = Math.PI / 120.0;
		}
		if (ke.isAltDown()) {
			altTransform(kc);
		} else {
			if (ke.isControlDown()) {
				controlTransform(kc);
			} else {
				standardTransform(kc);
			}
		}
	}
	protected void standardTransform(int kc) {
		if (kc == forwardKey) {
			moveForward();
		} else {
			if (kc == backKey) {
				moveBackward();
			} else {
				if (kc == leftKey) {
					rotateLeft();
				} else {
					if (kc == rightKey) {
						rotateRight();
					}
				}
			}
		}
	}
	protected void altTransform(int kc) {
		if (kc == forwardKey) {
			rotateUp();
		} else {
			if (kc == backKey) {
				rotateDown();
			} else {
				if (kc == leftKey) {
					moveLeft();
				} else {
					if (kc == rightKey) {
						moveRight();
					}
				}
			}
		}
	}
	protected void controlTransform(int kc) {
		if (kc == forwardKey) {
			moveUp();
		} else {
			if (kc == backKey) {
				moveDown();
			} else {
				if (kc == leftKey) {
					rollLeft();
				} else {
					if (kc == rightKey) {
						rollRight();
					}
				}
			}
		}
	}
	protected void moveForward() {
		move(new Vector3d(0.0, 0.0, -speed));
	}
	protected void moveBackward() {
		move(new Vector3d(0.0, 0.0, +speed));
	}
	protected void moveLeft() {
		move(new Vector3d( -speed, 0.0, 0.0));
	}
	protected void moveRight() {
		move(new Vector3d( +speed, 0.0, 0.0));
	}
	protected void moveUp() {
		move(new Vector3d( 0.0, +speed, 0.0));
	}
	protected void moveDown() {
		move(new Vector3d( 0.0, -speed, 0.0));
	}
	protected void rotateUp() {
		rotX(+rotAmount);
	}
	protected void rotateDown() {
		rotX(-rotAmount);
	}
	protected void rotateLeft() {
		rotY(+rotAmount);
	}
	protected void rotateRight() {
		rotY(-rotAmount);
	}
	protected void rollLeft() {
		rotZ(+rotAmount);
	}
	protected void rollRight() {
		rotZ(-rotAmount);
	}
	protected void move(Vector3d v3d) {
		if (!isCollision(v3d)) {
			tg.getTransform(t3d);
			Transform3D tt3d = new Transform3D();
			tt3d.setTranslation(v3d);
			t3d.mul(tt3d);
			tg.setTransform(t3d);
		}
	}
	protected void rotX(double d) {
		tg.getTransform(t3d);
		Transform3D tt3d = new Transform3D();
		tt3d.rotX(d);
		t3d.mul(tt3d);
		tg.setTransform(t3d);
	}
	protected void rotY(double d) {
		tg.getTransform(t3d);
		Transform3D tt3d = new Transform3D();
		tt3d.rotY(d);
		t3d.mul(tt3d);
		tg.setTransform(t3d);
	}
	protected void rotZ(double d) {
		tg.getTransform(t3d);
		Transform3D tt3d = new Transform3D();
		tt3d.rotZ(d);
		t3d.mul(tt3d);
		tg.setTransform(t3d);
	}
	protected boolean isCollision(Vector3d v3d) {
		return false;
	}
}
