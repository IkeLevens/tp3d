package edu.mccc.cos210.tp3d.Model;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * Score Model. The score model holds scoring data for the players in tp3d.
 */
public class ScoreModel {
	private Scores[] scores;
	private FlowControl fc;
	/**
	 * constructor which takes a FlowControl as a paramater.  This is the only constructor provided.
	 * @param fc The FlowControl holds state variables about the bowling game being simmulated.
	 */
	public ScoreModel(FlowControl fc) {
		this.fc = fc;
		scores = new Scores[4];
		for (int i = 0; i < 4; i++) {
			scores[i] = new Scores();
		}
	}
	/**
	 * resets the score information.  This method is called when the game is restarted.
	 */
	public void reset() {
		for (int i = 0; i < 4; i++) {
			scores[i].reset();
		}
	}
	/**
	 * Get the Scores object for a player.
	 */
	public Scores getScores(int player) {
		return scores[player - 1];
	}
}
