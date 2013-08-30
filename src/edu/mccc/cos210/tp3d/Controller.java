package edu.mccc.cos210.tp3d;
import edu.mccc.cos210.tp3d.*;
import edu.mccc.cos210.tp3d.Model.*;
import edu.mccc.cos210.tp3d.View.*;
import com.cbthinkx.util.Debug;
import java.awt.event.*;
import javax.swing.*;
	/**
	 * Controller Class. This class controls the model and the views of tp3d.
	 * There are two views of the model, Sound, which plays sound effects, and GUI,
	 * the graphical user interface.  GUI contains a Scoreboard, a ControlPanel, and a
	 * Canvas3D which is the 3D display of the tp3d model.  In addition, Music plays
	 * music for the user to make the simmulation more engaging. FlowControl and ScoreModel
	 * are part of the model, though they are separate classes from Model.  PhysicsEngine
	 * is a separate class, but in many ways is a part of the controller.  TitleScreen and
	 * PlayerSelect each show in a separate frame from the GUI frame.  TitleScreen provides
	 * information on tp3d, and serves as an identifier to the user.  PlayerSelect lets the
	 * user select a number of players.
	 */
public class Controller implements ActionListener, Runnable {
	private Model model;
	private FlowControl fc;
	private GUI view;
	private ScoreModel score;
	private Music music;
	private Sound sound;
	private PhysicsEngine physX;
	private ControlPanel controlPanel;
	private PlayerSelect ps;
	/**
	 * Method to run when a Controller is created in its own thread
	 * of execution.
	 */
	public void run() {
		Debug.println("Controller.run()");
		TitleScreen ts = new TitleScreen();
		ts.doIt(TitleScreen.SHOW);
		try {
			SwingUtilities.invokeAndWait(ts);
		} catch (Exception e) {
		}
		music = new Music();
		new Thread(music, "musicThread").start();
		sound = new Sound();
		new Thread(sound, "soundThread").start();
		fc = new FlowControl();
		ps = new PlayerSelect(this, fc);
		model = new Model();
		score = new ScoreModel(fc);
		view = new GUI(model, this, music, sound, fc, score);
		ts.doIt(TitleScreen.HIDE);
		ps.doIt(PlayerSelect.SHOW);
		try {
			SwingUtilities.invokeAndWait(ts);
			SwingUtilities.invokeAndWait(ps);
		} catch (Exception e) {
		}
		ts = null;
		controlPanel = view.getControlPanel();
		physX = new PhysicsEngine(this, model, score, controlPanel, sound, fc);
		new Thread(physX, "physicsThread").start();
		resetPins();
	}
	/**
	 * Creates the controller.
	 */
	public Controller() {
		Debug.println("Controller.Controller()");
	}
	/**
	 * Restarts the game.
	 */
	public void restart() {
		score.reset();
		view.repaint();
		resetPins();
	}
	/**
	 * Quits the game.
	 */
	public void quit() {
		Debug.println("Controller.quit()");
		System.exit(0);
	}
	/**
	 * This is the game loop.  Deprecated.
	 */
	public void control() {
		Debug.println("Controller.control()");
	}
	/**
	 * Throws the ball.  This method adds a throw ball work order to the workQueue of the
	 * physics thread.  In that way, even if this method is called on the AWT Event Dispatch
	 * Thread, the heavy calculation load will be handled on the physics thread, and the GUI
	 * should not become unresponsive.
	 */
	public void throwBall() {
		Debug.println("Controller.throwBall()");
		physX.doIt(PhysicsEngine.THROW_BALL);
		}
	/**
	 * Resets the pins to thier initial state.  This method adds a reset pins work order to the
	 * workQueue of the physicsthread.
	 */
	public void resetPins() {
		Debug.println("Controller.resetPins()");
		physX.doIt(PhysicsEngine.RESET_PINS);
	}
	/**
	 * Pauses the game.  Deprecated.  It was decided that since the game does not change
	 * state except in response to user input, that a pauseGame method is not needed.
	 */
	public void pauseGame() {
		Debug.println("Controller.pauseGame()");
	}
	/**
	 * Repaint the GUI
	 */
	public void repaint() {
		view.repaint();
	}
	/**
	 * An Action was performed. There are three ActionEvents which the controller responds
	 * to.  These are throw ball, restart game, and quit game.  This method delegates the
	 * responibility of handling those events to separate methods.
	 * @param e The Action Event
	 */
	public void actionPerformed(ActionEvent e) {
		Debug.println("Controller.actionPerformed()");
		if (e.getActionCommand().equals("Throw Ball")) {
			throwBall();
		}
		if (e.getActionCommand().equals("Restart Game")) {
			ps.doIt(PlayerSelect.SHOW);
			ps.run();
		}
		if (e.getActionCommand().equals("Quit Game")) {
			quit();
		}
	}
}
