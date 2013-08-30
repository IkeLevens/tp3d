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
/**
* Control Panel.  The control panel contains the main menu buttons that control the game along with the sliders
* that the player uses to control the ball.
*/
public class ControlPanel  extends JPanel implements ActionListener {
	private Controller controller;
	private Music music;
	private Sound sound;
	private FlowControl fc;
	private ScoreModel score;
	private int power;
	private int direction;
	private int spin;
	private JSlider powerSlider;
	private JSlider directionSlider;
	private JSlider spinSlider; 
	private JButton ball;
	/**
	* Constructor with five paramaters.  This is the only constructor provided.  Because the paramaters are all
	* of object types there can be no default values.
	* @param controller The tp3d controller.
	* @param music a reference to the music thread.
	* @param sound a reference to the sound thread.
	* @param fc the flow control holds state data for tp3d.
	* @param score the scoremodel holds scoring data for tp3d.
	*/
	public ControlPanel(Controller controller, Music music, Sound sound, FlowControl fc, ScoreModel score) {
		super();
		Debug.println("ControlPanel.ControlPanel()");
		this.controller = controller;
		this.music = music;
		this.sound = sound;
		this.fc = fc;
		this.score = score;
		this.setLayout(new BorderLayout());
		Box west = Box.createVerticalBox();
		powerSlider = new JSlider(1, 10, 7);
		directionSlider = new JSlider(-10, 10, 0);
		spinSlider = new JSlider(-10, 10, 0);
		Hashtable hash = new Hashtable<Integer, JLabel>();
		hash.put(0, new JLabel("slow"));
		hash.put(10, new JLabel("fast"));
		powerSlider.setLabelTable(hash);
		powerSlider.setFocusable(false);
		powerSlider.setPaintLabels(true);
		hash = new Hashtable<Integer, JLabel>();
		hash.put(-10, new JLabel("left"));
		hash.put(0, new JLabel("center"));
		hash.put(10, new JLabel("right"));
		directionSlider.setLabelTable(hash);
		directionSlider.setFocusable(false);
		directionSlider.setPaintLabels(true);
		spinSlider.setLabelTable(hash);
		spinSlider.setFocusable(false);
		spinSlider.setPaintLabels(true);
		JLabel power = new JLabel("power");
		JLabel direction = new JLabel("direction");
		JLabel spin = new JLabel("spin");
		west.add(power);
		west.createVerticalGlue();
		west.add(powerSlider);
		west.createVerticalGlue();
		west.add(direction);
		west.createVerticalGlue();
		west.add(directionSlider);
		west.createVerticalGlue();
		west.add(spin);
		west.createVerticalGlue();
		west.add(spinSlider);
		this.add(west, BorderLayout.WEST);
		Box center = Box.createVerticalBox();
		JButton pause = new JButton("Pause");
		pause.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		pause.addActionListener(music);
		pause.setFocusable(false);
		JButton mute = new JButton("Mute");
		mute.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		mute.addActionListener(sound);
		mute.setFocusable(false);
		ball = new JButton("Throw Ball");
		ball.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		ball.addActionListener(controller);
		ball.addActionListener(this);
		ball.setFocusable(false);
		JButton restart = new JButton("Restart Game");
		restart.setFocusable(false);
		restart.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		restart.addActionListener(controller);
		JButton quit = new JButton("Quit Game");
		quit.setFocusable(false);
		quit.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		quit.addActionListener(controller);
		center.add(pause);
		center.createVerticalGlue();
		center.add(mute);
		center.createVerticalGlue();
		center.add(ball);
		center.createVerticalGlue();
		center.add(restart);
		center.createVerticalGlue();
		center.add(quit);
		this.add(center, BorderLayout.EAST);
	}
	/**
	 * The preferred size of this window.
	 * @return a Dimension object representing the preferred size of the panel.
	 */
	public Dimension getPreferredSize() {
		Debug.println("ControlPanel.getPreferredSize");
		return new Dimension(350, 200);
	}
	/**
	 * responds to button presses. This method disables the throw ball button after each ball is thrown until the physics
	 * engine has finished processing that throw and has the button re-enabled.
	 * @param e The ActionEvent for which this method is called.
	 */
	public void actionPerformed(ActionEvent e) {
		Debug.println("ControlPanel.actionPerformed()");
		if (e.getActionCommand().equals("Throw Ball")) {
			ball.setEnabled(false);
		}
	}
	/**
	 * gets the values from the sliders.  This method sets the values of power, direction, and spin in the class variables
	 * based on selections the user makes using the sliders.
	 */
	public void updateSliders() {
		Debug.println("ControlPanel.getSliders()");
		power = powerSlider.getValue();
		direction = directionSlider.getValue();
		spin = spinSlider.getValue();
	}
	/**
	 * gets the value of power.
	 * @return power
	 */
	public int getPower() {
		return power;
	}
	/**
	 * gets the value of direction.
	 * @return direction
	 */
	public int getDirection() {
		return direction;
	}
	/**
	 * gets the value of spin.
	 * @return spin
	 */
	public int getSpin() {
		return spin;
	}
	/**
	 * enable the throw ball button.
	 */
	public void enableButton() {
		ball.setEnabled(true);
	}
	/**
	* Shows the Control Panel.  Deprecated.  The decision was made to have the control panel be a part of the GUI frame.
	*/
	public void showControlPanel() {
	}
}
