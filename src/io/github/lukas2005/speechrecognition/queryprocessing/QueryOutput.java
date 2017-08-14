package io.github.lukas2005.speechrecognition.queryprocessing;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author lukas2005
 * Class for outputs form QueryProcessors
 */
public class QueryOutput {

	private String stringOutput;
	public int returnCode = 0;
	public String eventName;
	public String queryString;
	public List<String> queryArgs;
	
	/**
	 * 
	 * @param stringOutput output
	 * @param queryString original query
	 * @param queryParams everything that was spoken but is not specified in QueryProcessor
	 */
	public QueryOutput(String stringOutput, String queryString, List<String> queryParams) {
		this(stringOutput, 0, "DEFAULT_EVENT", queryString, queryParams);
	}

	/**
	 * 
	 * @param stringOutput output
	 * @param queryString original query
	 * @param queryParams everything that was spoken but is not specified in QueryProcessor
	 * @param returnCode similar to exit code
	 */
	public QueryOutput(String stringOutput, int returnCode, String queryString, List<String> queryParams) {
		this(stringOutput, returnCode, "DEFAULT_EVENT", queryString, queryParams);
	}
	
	/**
	 * 
	 * @param stringOutput output
	 * @param queryString original query
	 * @param queryParams everything that was spoken but is not specified in QueryProcessor
	 * @param returnCode similar to exit code
	 * @param event name of event that occured
	 */
	public QueryOutput(String stringOutput, int returnCode, String event, String queryString, List<String> queryArgs) {
		this.stringOutput = stringOutput;
		this.returnCode = returnCode;
		this.eventName = event;
		this.queryString = queryString;
		this.queryArgs = queryArgs;
		notifyListeners();
	}

	/**
	 * INTERNAL FUNCTION DO NOT CALL
	 */
	private void notifyListeners() {
		final QueryOutput oldThis = this;
		new Thread() {
			public void run() {
				for (final EventListener l : EventListener.listeners) {
					if (l.eventName.contains(eventName) || l.eventName.contains("ALL")) {
						new Thread() {
							public void run() {
								l.fire(oldThis, Arrays.asList(queryString.split(" ")), queryArgs);
							}
						}.start();
					}
				}
			}
		}.start();
	}

	/**
	 * @return stringOutput
	 */
	@Override
	public String toString() {
		return getStringOutput();
	}

	/**
	 * @return the stringOutput
	 */
	public synchronized String getStringOutput() {
		for (EventListener l : EventListener.getOutputStringListeners) {
			if (l.eventName.contains(eventName) || l.eventName.contains("ALL")) {
					l.fireOnGetStringOutput(this, Arrays.asList(queryString.split(" ")), queryArgs);
			}
		}
		return stringOutput;
	}

	/**
	 * ONLY FOR EVENT LISTENER PURPOSES DONT CALL THIS ANYWHERE ELSE!
	 * @return the stringOutput
	 */
	public synchronized String getStringOutputNoCallListeners() {
		return stringOutput;
	}
	
	/**
	 * @param stringOutput the stringOutput to set
	 */
	public synchronized void setStringOutput(String stringOutput) {
		this.stringOutput = stringOutput;
	}
	
}
