package edu.mccc.cos210.tp3d.View;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Graphics.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import com.cbthinkx.util.Debug;
import edu.mccc.cos210.tp3d.*;
/**
 * PlayerSelect.  This class is used to display a player select menu as an independent frame.
 */
public class PlayerSelect extends JFrame implements Runnable {
	public static final int SHOW = 0;
	public static final int HIDE = 1;
	public static final int Y_OFFSET = 40;
	private Queue<Integer> workQueue;
	private Controller controller;
	private FlowControl fc;
	private BufferedImage bi;
	private Dimension d;
	/**
	 * allows calling on a thread.  This method is meant to be called on the AWT Event Dispatch Thread.
	 * Swing is not thread safe, and as a result it is intended that this menu be shown (and hidden) on
	 * the event thread.
	 */
	public void run() {
		Debug.println("TitleScreen.run()");
		synchronized (workQueue) {
			int work = workQueue.poll();
			switch(work) {
				case SHOW:
					showPlayerMenu();
					break;
				case HIDE:
					hidePlayerMenu();
					break;
			}
		}
	}
	/**
	 * allows queueing for this class' work. This method is called to place a work order on the workQueue
	 * for this class before the run method is called on the event thread.  The workQueue is synchronized
	 * because it is used by different threads.
	 */
	public void doIt(int flag) {
		Debug.println("PlayerSelect.doIt()");
		synchronized (workQueue) {
			workQueue.offer(flag);
		}
	}
	/**
	 * Constructor with two paramaters. This is the only constructor provided.  Because these paramaters
	 * are of object types, no default values are possible.
	 * @param controller the controller of tp3d.
	 * @param fc a FlowControl object in which this class will set the number of players.
	 */
	public PlayerSelect(Controller controller, FlowControl fc) {
		this.controller = controller;
		this.fc = fc;
		this.workQueue = new LinkedList<Integer>();
				try {
			File file = new File("Textures/PlayerSelect.jpg");
			bi = ImageIO.read(file);
		} catch (IOException e) {
			Debug.println("cannot find TitleScreen.jpg");
		}
		d = new Dimension(
			bi.getWidth(),
			bi.getHeight() + Y_OFFSET
		);
		this.setSize(d);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension ssize = toolkit.getScreenSize();
		int x = ((int)ssize.width - (int)d.width) / 2;
		int y = ((int)ssize.height - (int)d.width) / 2;
		this.setLocation(x, y);
		this.setResizable(true);
		this.setUndecorated(true);
		MyJPanel mjp = new MyJPanel();
		this.setContentPane(mjp);
		this.pack();
	}
	/**
	 * Shows the player menu.
	 */
	public void showPlayerMenu() {
		Debug.println("PlayerSelect.showPlayerMenu()");
		this.setVisible(true);
		this.toFront();
	}
	/**
	 * hides the player menue.
	 */
	public void hidePlayerMenu() {
		Debug.println("PlayerSelect.hidePlayerMenu()");
		this.setVisible(false);
	}
	/**
	 * Sring output.
	 * @return String representation of the player menu.
	 */
	public String toString() {
		return null;
	}
	/**
	 * this JPanel is specialized to show the player select menu.
	 */
	private class MyJPanel extends JPanel implements ActionListener {
		private JRadioButton one;
		private JRadioButton two;
		private JRadioButton three;
		private JRadioButton four;
		private JButton select;
		private JButton cancel;
		/**
		 * A constructor with no paramaters taken.
		 */
		MyJPanel() {
			Debug.println("PlayerSelect.MyJPanel.MyJPanel()");
			one = new JRadioButton("One Player", false);
			one.setVisible(true);
			two = new JRadioButton("Two Players", false);
			two.setVisible(true);
			three = new JRadioButton("Tree Players", false);
			three.setVisible(true);
			four = new JRadioButton("Four Players", true);
			four.setVisible(true);
			ButtonGroup group = new ButtonGroup();
			group.add(one);
			group.add(two);
			group.add(three);
			group.add(four);
			this.add(one);
			this.add(two);
			this.add(three);
			this.add(four);
			select = new JButton("Select");
			select.addActionListener(this);
			select.setVisible(true);
			this.add(select);
			cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			cancel.setVisible(true);
			this.add(cancel);
			this.setBorder(new MyBorder());
		}
		/**
		 * paintComponent is called to paint this JPanel whenever the parent is repainted.
		 */
		public void paintComponent(Graphics g) {
			Debug.println("PlayerSelect.MyJPanel.paintComponent()");
			super.paintComponent(g);
			one.repaint();
			two.repaint();
			three.repaint();
			four.repaint();
			select.repaint();
			cancel.repaint();
		}
		/**
		 * this method handles events of button presses on this JPanel.  The PlayerSelect screen is shown when the restart
		 * game button is pressed, in addition to when the simmulation is first begun.  Because of this, there is a cancel
		 * button in case the restart button was pressed in error.  When the select button is pressed, the game is restarted
		 * (or started) with the selected number of players.
		 * @param e the ActionEvent which this method is handling.
		 */
		public void actionPerformed(ActionEvent e) {
			Debug.println("PlayerSelect.MyJPanel.actionPerformed()");
			if (e.getActionCommand().equals("Select")) {
				if (one.isSelected()) {
					fc.setNumPlayers(1);
				}
				if (two.isSelected()) {
					fc.setNumPlayers(2);
				}
				if (three.isSelected()) {
					fc.setNumPlayers(3);
				}
				if (four.isSelected()) {
					fc.setNumPlayers(4);
				}
				fc.setFrame(1);
				fc.setBall(1);
				fc.setPlayer(1);
				controller.restart();
				hidePlayerMenu();
			}
			if (e.getActionCommand().equals("Cancel")) {
				hidePlayerMenu();
			}
		}
		/**
		 * returns a Dimension of the preferred size for this JPanel.
		 * @return A dimension representing the preferred size of this panel.
		 */
		public Dimension getPreferredSize() {
			return d;
		}
	}
	/**
	 * MyBorder. This class is a border for a JComponent or subclass thereof, and paints a buffered image in the
	 * center of that component.
	 */
	private class MyBorder implements Border {
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			int x0 = x + (width - bi.getWidth()) / 2;
			int y0 = y + (height - bi.getHeight()) / 2;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi, null, 0, Y_OFFSET);
			g2d.dispose();
		}
		/**
		 * The instets to be used for this border.
		 * @return the insets to be used for this border.
		 */
		public Insets getBorderInsets(Component c) {
			return new Insets(0,0,0,0);
		}
		/**
		 * This method declares whether the border is opaque.
		 * @return The method always returns true, this border is always opaque.
		 */
		public boolean isBorderOpaque() {
			return true;
		}
	}
}
