package edu.mccc.cos210.tp3d.Model;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * Frames
 */
public class Frames {
	private int ball1;
	private int ball2;
	private int ball3;
	private boolean isStrike;
	private boolean isSpare;
	private int total;
	/**
	 * Constructor with no paramaters.
	 */
	public Frames() {
		reset();
	}
	/**
	 * Resets the score information for this frame.
	 */
	public void reset() {
		ball1 = -1;
		ball2 = -1;
		ball3 = -1;
		isStrike = false;
		isSpare = false;
		total = -1;
	}
	/**
	 * Get the value of ball1
	 * @return the value ball one of this frame.
	 */
	public int getBall1() {
		return ball1;
	}
	/**
	 * Returns an iterable LinkedList<Integer> of the scores in this frame.
	 * @return a linked list of ball1, ball2, and ball3.
	 */
	public LinkedList<Integer> getScoreList() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(ball1);
		list.add(ball2);
		list.add(ball3);
		return list;
	}
	/**
	 * Set the value of ball1
	 * @param score the score for ball one of this frame.
	 */
	public void setBall1(int score) {
		ball1 = score;
	}
	/**
	 * Get the value of ball2
	 * @return the value of ball two of this frame.
	 */
	public int getBall2() {
		return ball2;
	}
	/**
	 * Set the value of ball2
	 * @param score the score for ball two of this frame.
	 */
	public void setBall2(int score) {
		ball2 = score;
	}
	/**
	 * Get the value of ball3
	 * @return the value of ball three of this frame.
	 */
	public int getBall3() {
		return ball3;
	}
	/**
	 * Set the value of ball3
	 * @param score the score for ball three of this frame.
	 */
	public void setBall3(int score) {
		ball3 = score;
	}
	/**
	 * Set the isStrike flag.
	 * @param flag the value to be set to isStrike.
	 */
	public void setStrike(boolean flag) {
		isStrike = flag;
	}
	/**
	 * Get the value of the isStrike flag.
	 * @return the value of the isStrike flag.
	 */
	public boolean getisStrike() {
		return isStrike;
	}
	/**
	 * Set the isSpare flag.
	 * @param flag the value to be set to isSpare.
	 */
	public void setSpare(boolean flag) {
		isSpare = flag;
	}
	/**
	 * Get the value of the isSpare flag.
	 * @return the value of the isSpare flag.
	 */
	public boolean getisSpare() {
		return isSpare;
	}
	/**
	 * Get the value of total.
	 * @return the total score of this player up to this frame.
	 */
	public int getTotal() {
		return total;
	}
	
	/**
	 * Set the value of total.
	 * @param newTotal the total score of this player up to this frame.
	 */
	public void setTotal(int newTotal) {
		total = newTotal;
	}
}
