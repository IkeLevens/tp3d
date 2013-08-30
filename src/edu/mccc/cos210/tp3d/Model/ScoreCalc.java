package edu.mccc.cos210.tp3d.Model;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * ScoreCalc is a utility class which is not meant to be instantiated.  Its methods are static and meant to be used as utilities by other classes.
 */
public class ScoreCalc {
	/**
	 * A method to calculate the totals in the scoremodel.
	 * @param sm A ScoreModel object which this method will use to send and retrieve data.
	 * @param fc A FlowControl holding state values about this bowling game.
	 */
	public static void calculate(ScoreModel sm, FlowControl fc) {
		int players = fc.getNumPlayers();
		Frames f = null;
		for (int player = 1; player <= players; player++) {
			for (int frame = 1; frame <= fc.getFrame(); frame++) {
				f = sm.getScores(player).getFrames(frame);
				if (frame == 10) {
					if (f.getisStrike() || f.getisSpare()) {
						if (f.getBall3() != -1) {
							f.setTotal(
								sm.getScores(player).getFrames(frame - 1).getTotal() +
								f.getBall1() +
								f.getBall2() +
								f.getBall3()
							);
						}
					} else {
						if (f.getBall2() != -1) {
							f.setTotal(
								sm.getScores(player).getFrames(frame - 1).getTotal() +
								f.getBall1() +
								f.getBall2()
							);
						}
					}
				} else {
					int total;
					if (frame == 1) {
						total = 0;
					} else {
						total = sm.getScores(player).getFrames(frame - 1).getTotal();
					}
					if (f.getisStrike()) {
						int added = 0;
						int next = 0;
						total += 10;
						LinkedList<Integer> iter = new LinkedList<Integer>();
						for (int i = frame + 1; i <= fc.getFrame(); i++) {
							iter.addAll(iter.size(), sm.getScores(player).getFrames(i).getScoreList());
						}
						while (added < 2 && !iter.isEmpty()) {
							next = iter.remove();
							if (next != -1) {
								total += next;
								added++;
							}
						}
						if (added == 2) {
							f.setTotal(total);
						}
					} else {
						if (f.getisSpare()) {
							int added = 0;
							int next = 0;
							total += 10;
							LinkedList<Integer> iter = new LinkedList<Integer>();
							for (int i = frame + 1; i <= fc.getFrame(); i++) {
								iter.addAll(iter.size(), sm.getScores(player).getFrames(i).getScoreList());
							}
							while (added < 1 && !iter.isEmpty()) {
								next = iter.remove();
								if (next != -1) {
									total += next;
									added++;
								}
							}
							if (added == 1) {
								f.setTotal(total);
							} 
						} else {
							if (f.getBall2() != -1) {
								total += f.getBall1() + f.getBall2();
								f.setTotal(total);
							}
						}
					}
				}
			}
		}
	}
}
