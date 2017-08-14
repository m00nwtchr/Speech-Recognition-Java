package io.github.lukas2005.speechrecognition.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import io.github.lukas2005.speechrecognition.queryprocessing.EventListener;
import io.github.lukas2005.speechrecognition.queryprocessing.QueryOutput;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * 
 * @author lukas2005
 * Wrapper for file
 *
 */
public class Speech extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3263015122562047353L;
	private QueryOutput out;

	public Speech(String pathname, QueryOutput out) {
		super(pathname);
		this.out = out;
		deleteOnExit();
	}

	/**
	 * Use this method to play the speech!!
	 * @throws JavaLayerException 
	 * @throws FileNotFoundException 
	 */
	public void speak() throws FileNotFoundException, JavaLayerException {
		Player pl = new Player(new FileInputStream(this));
		pl.play();
		new Thread() {
			public void run() {
				for (final EventListener l : EventListener.listeners) {
					if (l.eventName.contains(out.eventName) || l.eventName.contains("ALL")) {
						new Thread() {
							public void run() {
								l.fireOnSpeechEnd(out, Arrays.asList(out.queryString.split(" ")), out.queryArgs);
							}
						}.start();
					}
				}
			}
		}.start();
	}
	
	/**
	 * Use this method to play the speech and delete the mp3 file
	 * @throws JavaLayerException 
	 * @throws FileNotFoundException 
	 */
	public void speakAndDeleteFile() throws FileNotFoundException, JavaLayerException {
		speak();
		delete();
	}
	
}
