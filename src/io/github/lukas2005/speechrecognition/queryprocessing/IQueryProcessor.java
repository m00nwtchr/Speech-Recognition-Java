package io.github.lukas2005.speechrecognition.queryprocessing;

/**
 * 
 * @author lukas2005
 * Wanna create your own query processor? :D implement this!
 */
public interface IQueryProcessor {

	/**
	 * Here the magic happens!
	 * @param query query to process
	 */
	public QueryOutput processQuery(String query);
	
	
}
