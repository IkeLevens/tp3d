package edu.mccc.cos210.tp3d.View;
import com.cbthinkx.util.Debug;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
/**
 * Music.  This class is meant to run in a thread.  It creates an auto clip, and plays it.  It provides
 * the necessary controls to pause and resume that clip.  The music class is meant to add player engagement.
 */
public class Music implements ActionListener, Runnable {
	private boolean paused;
	private Clip music;
	/**
	 * Constructor with no paramaters.
	 */
	public Music() {
		Debug.println("Music.Music()");
		paused = false;
		AudioInputStream as = null;
		URL MUSIC = getClass().getClassLoader().getResource("Sounds/music.wav");
        try {
            as = AudioSystem.getAudioInputStream(MUSIC);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
			music = (Clip) AudioSystem.getClip();
			music.open(as);
		} catch (Exception e) {
		}
	}
	/**
	 * Plays the Music.
	 */
	public void playMusic() {
		Debug.println("Music.playMusic()");
		music.start();
		music.setLoopPoints(0, -1);
		music.loop(Clip.LOOP_CONTINUOUSLY);
	}
	/**
	 * Music player thread of execution.
	 */
	public void run() {
		Debug.println("Music.run()");
		playMusic();
	}
	/**
	 * Responds to AWT events.  Toggles paused when the pause button is pressed on the control panel. Based on'
	 * the value of boolean paused the clip is either started or stopped in response to the pause button on the
	 * control panel.  When the music is not paused it loops continuously.
	 */
	public void actionPerformed(ActionEvent e) {
		Debug.println("Music.actionPerformed()");
		if (paused) {
			paused = false;
			music.start();
			music.setLoopPoints(0, -1);
			music.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			paused = true;
			music.stop();
		}
	}
	/**
	 * String output.
	 * @return String representation of the Music.
	 */
	public String toString() {
		return null;
	}
	/**
	 * A test driver for the music player.
	 */
	public static void main(String[] sa) {
		Music music = new Music();
		new Thread(music).start();
	}
}
