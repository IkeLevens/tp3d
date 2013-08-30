package edu.mccc.cos210.tp3d.View;
import com.cbthinkx.util.Debug;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.sound.sampled.*;
/**
 * Sound.  This class runs on its own thread.  It provides an audio view for the player of the model.
 */
public class Sound implements ActionListener, Runnable {
	public static final int BALL_ROLL = 0;
	public static final int GUTTER = 1;
	public static final int CLATTER = 2;
	private final URL ROLL_SOUND = getClass().getClassLoader().getResource("Sounds/ROLL_SOUND.wav");
	private final URL GUTTER_SOUND = getClass().getClassLoader().getResource("Sounds/GUTTER_SOUND.wav");
	private final URL CLATTER_SOUND = getClass().getClassLoader().getResource("Sounds/CLATTER_SOUND.wav");
	private Queue<Integer> fxQueue;
	private boolean muted;
	private Clip roll;
	private Clip gutter;
	private Clip clatter;
	/**
	 * Constructor with no paramaters.  Creates the Sound effects player.
	 */
	public Sound() {
		Debug.println("Sound.Sound()");
		muted = false;
		fxQueue = new LinkedList<Integer>();
		AudioInputStream as = null;
        try {
            as = AudioSystem.getAudioInputStream(ROLL_SOUND);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
			roll = (Clip) AudioSystem.getClip();
			roll.open(as);
		} catch (Exception e) {
		}
		try {
            as = AudioSystem.getAudioInputStream(GUTTER_SOUND);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
			gutter = (Clip) AudioSystem.getClip();
			gutter.open(as);
		} catch (Exception e) {
		}
		try {
            as = AudioSystem.getAudioInputStream(CLATTER_SOUND);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
			clatter = (Clip) AudioSystem.getClip();
			clatter.open(as);
		} catch (Exception e) {
		}
	}
	/**
	 * Plays the Sound.
	 * @param sound An integer value defining what sound the sound thread is to play.
	 */
	public void playSound(int sound) {
		Debug.println("Sound.playSound()");
		switch (sound) {
			case 0:
				if (!muted) {
					roll.setFramePosition(0);
					gutter.setFramePosition(0);
					clatter.setFramePosition(0);
					roll.start();
				}
				break;
			case 1:
				if (!muted) {
					gutter.start();
				}
				break;
			case 2:
				if (!muted) {
					clatter.start();
				}
				break;
		}
	}
	/**
	 * Sound Effects player thread of execution.
	 */
	public void run() {
		Debug.println("Sound.run()");
		while(true) {
			synchronized (fxQueue) {
				if (fxQueue.peek() == null) {
					try{
						fxQueue.wait();
					} catch (InterruptedException e) {
						continue;
					}
				} else {
					int sound = fxQueue.poll();
					playSound(sound);
				}
			}
		}
	}
	/**
	 * places sound effects on the fxQueue to be played.
	 * @param fx the effect to be played.
	 */
	public void doIt (int fx) {
		Debug.println("Sound.doIt()");
		synchronized (fxQueue) {
			fxQueue.offer(fx);
			fxQueue.notify();
		}
	}
	/**
	 * Handles user generated events.  Toggle muted when the mute button is pressed on the control panel.
	 * @param e The ActionEvent to be processed by this method.
	 */
	public void actionPerformed(ActionEvent e) {
		Debug.println("Sound.actionPerformed()");
		if (muted) {
			muted = false;
		} else {
			muted = true;
		}
	}
	/**
	 * String output.
	 * @return String representation of the sound file.
	 */
	public String toString() {
		return null;
	}
}
