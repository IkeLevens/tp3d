package edu.mccc.cos210.tp3d.Model;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * Scores.  This class holds the 10 objects of type Frames that hold the score data for a single player.
 */
public class Scores {
	private Frames[] frames;
	public Scores() {
		frames = new Frames[10];
		for (int i = 0; i < 10; i++) {
			frames[i] = new Frames();
		}
	}
	/**
	 * Resets the score information.  This method is called by the rese method of the ScoreModel.
	 */
	public void reset() {
		for (int i = 0; i < 10; i++) {
			frames[i].reset();
		}
	}
	/**
	 * Get the Frames object for a frame.
	 */
	public Frames getFrames(int frame) {
		return frames[frame - 1];
	}
}
