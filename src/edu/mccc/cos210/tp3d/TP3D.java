package edu.mccc.cos210.tp3d;
import edu.mccc.cos210.tp3d.*;
import com.cbthinkx.util.Debug;

/**
 * Ten Pin 3D. Bowling Simulator. Pure Zen.
 * Ten Pin 3D is written using the Java 3D API.
 *
 * @author Isaac Yochelson
 * @author Daniel Selmon
 *
 * @version 1.0
 */
public class TP3D {
	/**
	 * main method.  This method creates a Controller object in its own non-daemon thread.
	 * @param sa The command line arguments
	 */
	public static void main(String[] sa) {
		Debug.println("tp3d.main()");
		Controller controller = new Controller();
		new Thread(controller).start();
	}
}
