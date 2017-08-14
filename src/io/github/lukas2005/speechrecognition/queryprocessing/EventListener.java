package io.github.lukas2005.speechrecognition.queryprocessing;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lukas2005
 * Class for listening on various events for example when user wants to play music the place of config of event names depends on what QueryProcessor you are using
 */
public class EventListener {

	public String eventName;
	public boolean onGetStringOutput = false;
	
	public static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	public static ArrayList<EventListener> getOutputStringListeners = new ArrayList<EventListener>();
	public static ArrayList<EventListener> speechEndedListeners = new ArrayList<EventListener>();
	
	/**
	 * @param eventName desired event
	 * @param onGetStringOutput will this get executed when QueryOutput#getStringOutput is called and the desired event happened?
	 * @param onSpeechEnded will this get executed on the end of Speech#speak and the desired event happened?
	 */
	public EventListener(String eventName, boolean onGetStringOutput, boolean onSpeechEnded) {
		this.eventName = eventName;
		this.onGetStringOutput = onGetStringOutput;
		listeners.add(this);
		if (onGetStringOutput) getOutputStringListeners.add(this);
	}
	
	/**
	 * This will set eventname to ALL thus executing on all events
	 * @param onGetStringOutput will this get executed always when QueryOutput#getStringOutput is called?
	 * @param onSpeechEnded will this get executed always on the end of Speech#speak?
	 */
	public EventListener(boolean onGetStringOutput, boolean onSpeechEnded) {
		this.eventName = "ALL";
		this.onGetStringOutput = onGetStringOutput;
		listeners.add(this);
		if (onGetStringOutput) getOutputStringListeners.add(this);
		if (onGetStringOutput) speechEndedListeners.add(this);
	}

	/**
	 * Will get executed if the event name of this EventListener matches current event
	 */
	public void fire(QueryOutput queryOutput, List<String> query, List<String> queryArgs, Object... data) {}

	/**
	 * Will get executed if the event name of this EventListener matches current event and EventListener#onGetStringOutput is true
	 * If you want to change response depending on the query do it here
	 */
	public void fireOnGetStringOutput(QueryOutput queryOutputt, List<String> query, List<String> queryArgs, Object... data) {}

	/**
	 * Will get executed if the event name of this EventListener matches current event and EventListener#onSpeechEnded is true
	 * If you want to do something when playing response ended do this here (good for playing music)
	 */
	public void fireOnSpeechEnd(QueryOutput queryOutputt, List<String> query, List<String> queryArgs, Object... data) {}
	
}
