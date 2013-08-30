package edu.mccc.cos210.tp3d;
import edu.mccc.cos210.tp3d.*;
/**
 * FlowControl. This class holds state data for tp3d.
 */
public class FlowControl {
	public static final int[] ballsPerFrame =
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
	private int numPlayers;
	private int frame;
	private int player;
	private int ball;
	/**
	 * default constructor with no paramaters.
	 */
	public FlowControl() {
		numPlayers = 4;
		frame = 1;
		player = 1;
		ball = 1;
	}
	/**
	 * Get the number of players.
	 * @return the number of players as an integer.
	 */
	public int getNumPlayers() {
		return numPlayers;
	}
	/** 
	 * set the number of players.
	 * @param num an integer value, the number of players to be set.
	 */
	public void setNumPlayers(int num) {
		numPlayers = num;
	}
	/**
	 * Get the current frame.
	 * @return the number of the current frame as an integer.
	 */
	public int getFrame() {
		return frame;
	}
	/**
	 * Set the new current frame.
	 * @param newFrame the number of the frame as an integer.
	 */
	public void setFrame(int newFrame) {
		frame = newFrame;
	}
	/**
	 * Get the ball of the frame.
	 * @return the number of the current ball of this frame as an integer.
	 */
	public int getBall() {
		return ball;
	}
	/**
	 * Set the ball of the frame.
	 * @param ball the number of the ball as an integer.
	 */
	public void setBall(int ball) {
		this.ball = ball;
	}
	/**
	 * Get the player who's turn it is.
	 * @return the number of the current player as an integer.
	 */
	public int getPlayer() {
		return player;
	}
	/**
	 * Set the player who's turn it is.
	 * @param player the number of the player as an integer.
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	/**
	 * Move to the next ball.
	 */
	public void nextBall() {
		if (ball == ballsPerFrame[frame - 1]) {
			nextFrame();
		} else {
			ball++;
		}
	}
	/**
	 * Move to the next frame.
	 */
	public void nextFrame() {
		if (player == numPlayers) {
			player = 1;
			frame++;
			ball = 1;
		} else {
			player++;
			ball = 1;
		}
	}
}
