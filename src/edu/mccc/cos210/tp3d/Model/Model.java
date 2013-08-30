package edu.mccc.cos210.tp3d.Model;
import edu.mccc.cos210.tp3d.Model.*;
import com.cbthinkx.util.Debug;
/**
 *Model.  The model holds references to all objects in tp3d which are capable of movement.
 */
public class Model {
	private MovableObject[] pins;
	/**
	 * constructor with no paramaters.
	 */
	public Model() {
		Debug.println("Model.Model()");
		pins = new MovableObject[11];
		pins[0] = new Ball();
		for (int i = 1; i <= 10; i++) {
			pins[i] = new Pin();
		}
	}
	/**
	 * Returns an array of implementors of the interface MovableObject.
	 * @return an array of 11 movable objects.
	 * pins[1] through pins[10] are the bowling pins.
	 * pins[0] is the ball.
	 */
	public MovableObject[] getPins() {
		Debug.println("Model.getPins()");
		return pins;
	}
}
