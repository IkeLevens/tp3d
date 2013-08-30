package edu.mccc.cos210.tp3d.View;
import com.cbthinkx.util.Debug;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
/**
 * TitleScreen. This class is shown as a splash when tp3d is first started.  This screen provides the name
 * of the program and the names of the autors.  It also displays a bowling themed image.
 */
public class TitleScreen extends JFrame implements Runnable {
	public static final int SHOW = 0;
	public static final int HIDE = 1;
	private BufferedImage bi;
	private Dimension d;
	private Queue<Integer> workQueue;
	/**
	 * Constuctor with no paramaters.  Creates the titlescreen.
	 */
	public TitleScreen() {
		Debug.println("TitleScreen.TitleScreen()");
		try {
			File file = new File("Textures/TitleScreen.jpg");
			bi = ImageIO.read(file);
		} catch (IOException e) {
			Debug.println("cannot find TitleScreen.jpg");
		}
		Debug.println(bi);
		d = new Dimension(
			bi.getWidth(),
			bi.getHeight()
		);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(d);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension ssize = toolkit.getScreenSize();
		int x = ((int)ssize.width - (int)d.width) / 2;
		int y = ((int)ssize.height - (int)d.width) / 2;
		this.setLocation(x, y);
		this.setUndecorated(true);
		JPanel jp = new MyJPanel();
		jp.setSize(d);
		this.add(jp);
		this.workQueue = new LinkedList<Integer>();
	}
	/**
	 * allows calling on a thread
	 */
	public void run() {
		Debug.println("TitleScreen.run()");
		synchronized (workQueue) {
			int work = workQueue.poll();
			switch(work) {
				case SHOW:
					showTitleScreen();
					break;
				case HIDE:
					hideTitleScreen();
					break;
			}
		}
	}
	/**
	 * allows queueing for this class' work.  This workQueue allows for data to be passed between threads safely.
	 * WorkQueue is synchronized because it is used by multiple threads.
	 * @param flag An integer flag specifying the work that is to be done.
	 */
	public void doIt(int flag) {
		Debug.println("TitlesScreen.doIt()");
		synchronized (workQueue) {
			workQueue.offer(flag);
		}
	}
	/**
	 * Shows the title screen.
	 */
	public void showTitleScreen() {
		Debug.println("TitleScreen.showTitleScreen()");
		this.setVisible(true);
		this.toFront();
	}
	/**
	 * Hides the title screen.
	 */
	public void hideTitleScreen() {
		Debug.println("TitleScreen.hideTitleScreen()");
		this.setVisible(false);
	}
	/**
	 * String output.
	 * @return String representation of the Title screen.
	 */
	public String toString() {
		return null;
	}
	/**
	 * JPanel specialized to show the BufferedImage bi
	 */
	private class MyJPanel extends JPanel {
		/**
		 * paintComponent is called to draw this JPanel when its parent is repainted.
		 */
		public void paintComponent(Graphics g) {
			Debug.println("TitleScreen.MyJPanel.paintComponent()");
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			if (g2d == null) {
				Debug.println("g2d == null");
			}
			g2d.drawImage(bi, null, 0, 0);
			g2d.dispose();
		}
		/**
		 * returns a Dimension of the preferred size for this JPanel.
		 * @return A Dimension representing the preferred size of this JPanel.
		 */
		public Dimension getPreferredSize() {
			return d;
		}
	}
}
