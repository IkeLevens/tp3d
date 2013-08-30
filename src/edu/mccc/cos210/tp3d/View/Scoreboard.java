package edu.mccc.cos210.tp3d.View;
import com.cbthinkx.util.Debug;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import edu.mccc.cos210.tp3d.View.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
public class Scoreboard extends JPanel{
	private BufferedImage bi;
	private ScoreModel score;
	private FlowControl fc;
	/**
	 * Creates the scoreboard.
	 */
	public Scoreboard(FlowControl fc, ScoreModel score) {
		super();
		this.score = score;
		this.fc = fc;
		bi = new BufferedImage(450, 200, BufferedImage.TYPE_BYTE_BINARY);
		makeBI();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(bi, null, 0, 0);
		writeDigits(g2d);
		markPlayer(g2d);
	}
	public Dimension getPreferredSize() {
		return new Dimension(450, 200);
	}
	/**
	 * String output.
	 * @return String representation of the score board.
	 */
	public String toString() {
		return null;
	}
	/**
	 * Updates the scoreboard.
	 */
	public void update() {
		this.repaint();
	}
	/**
	 * creates a BufferedImage of the basic scoreboard to be used for faster repaints.
	 */
	private void makeBI() {
		Graphics2D g2d = bi.createGraphics();
		g2d.setPaint(Color.WHITE);
		Rectangle2D.Double rect = new Rectangle2D.Double(0.0 ,0.0 , 450.0 , 200.0);
		g2d.fill(rect);
		g2d.setPaint(Color.BLACK);
		for (int row = 1; row <= 4; row++) {
			for (int col = 1; col <= 4; col++) {
				rect = new Rectangle2D.Double(
					5.0 + 70.0 * col,
					-45.0 + 50.0 * row,
					44.0,
					44.0
				);
				g2d.draw(rect);
				rect = new Rectangle2D.Double(
					5.0 + 70.0 * col,
					-45.0 + 50.0 * row,
					21.5,
					21.5
				);
				g2d.draw(rect);
				rect = new Rectangle2D.Double(
					27.5 + 70.0 * col,
					-45.0 + 50.0 * row,
					21.5,
					21.5
				);
				g2d.draw(rect);
			}
		}
	}
	/**
	 * Places text and scoring marks on the scoreboard.
	 * @param g2d A Graphics2D object which serves as the graphical context for painting on the scoreboard.
	 */
	private void writeDigits(Graphics2D g2d) {
		Debug.println("Scoreboard.writeDigits()");
		int frame = fc.getFrame();
		int offset;
		if (frame < 5) {
			offset = 0;
		} else {
			offset = frame - 4;
		}
		for (int row = 1; row <= fc.getNumPlayers(); row++) {
			for (int col = 1; col <= 4; col++) {
				Debug.println("row: " + row + " col: " + col);
				if (score == null) {
					Debug.println("score is null");
				}
				int ball1 = score.getScores(row).getFrames(col + offset).getBall1();
				int ball2 = score.getScores(row).getFrames(col + offset).getBall2();
				int total = score.getScores(row).getFrames(col + offset).getTotal();
				if (ball1 >= 0) {
					g2d.drawString(Integer.toString(ball1), 10.0f + 70.0f * col, -30.0f + 50.0f * row);
				}
				if (ball2 >= 0) {
					g2d.drawString(Integer.toString(ball2), 32.5f + 70.0f * col, -30.0f + 50.0f * row);
				}
				if (total >= 0) {
					g2d.drawString(Integer.toString(total), 20.0f + 70.0f * col, -10.0f + 50.0f * row);
				}
				g2d.drawString(Integer.toString(col + offset), -5.0f + 70.0f * col, -10.0f + 50.0f * row);
				if (frame == 10) {
					Rectangle2D.Double rect = new Rectangle2D.Double(
						330.0,
						-45.0 + 50.0 * col,
						21.5,
						21.5
					);
					g2d.setStroke(new BasicStroke(1));
					g2d.draw(rect);
					int ball3 = score.getScores(row).getFrames(10).getBall3();
					if (ball3 != -1) {
						g2d.drawString(Integer.toString(ball3), 330.0f, -30.0f+ 50.0f * row);
					}
				}
				g2d.setStroke(new BasicStroke(3));
				if (score.getScores(row).getFrames(col + offset).getisStrike()) {
					g2d.draw(new Line2D.Double(
						5.0 + 70.0 * col,
						-45.0 + 50.0 * row,
						27.5 + 70.0 * col,
						-22.5 + 50.0 * row
					));
					g2d.draw(new Line2D.Double(
						27.5 + 70.0 * col,
						-45.0 + 50.0 * row,
						5.0 + 70.0 * col,
						-22.5 + 50.0 * row
					));
				} else {
					if (score.getScores(row).getFrames(col + offset).getisSpare()) {
						g2d.draw(new Line2D.Double(
							50.0 + 70.0 * col,
							-45.0 + 50.0 * row,
							27.5 + 70.0 * col,
							-22.5 + 50.0 * row
						));
					}
				}
			}
		}
	}
	/**
	 * Creates a Red and Black circle to mark the current player.
	 * @param g2d A Graphics2D object which serves as the graphical context for painting on the scoreboard.
	 */
	private void markPlayer(Graphics2D g2d) {
		Debug.println("Scoreboard.markPlayer()");
		Ellipse2D circle = new Ellipse2D.Double(
			5,
			-45 + 50 * fc.getPlayer(),
			40,
			40
		);
		g2d.setPaint(Color.RED);
		g2d.fill(circle);
		g2d.setPaint(Color.BLACK);
		g2d.draw(circle);
	}
}
