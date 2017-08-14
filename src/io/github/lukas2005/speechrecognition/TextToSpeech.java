package io.github.lukas2005.speechrecognition;

import java.io.File;
import java.net.URLEncoder;

import io.github.lukas2005.speechrecognition.audio.Speech;
import io.github.lukas2005.speechrecognition.queryprocessing.QueryOutput;
import io.github.lukas2005.speechrecognition.utils.NetworkUtils;
import io.github.lukas2005.speechrecognition.utils.NetworkUtils.URLBuilder;

/**
 * 
 * @author lukas2005
 * Uses Google web api to convert text to speech
 */
public class TextToSpeech {

	public String lang;
	
	/**
	 * @param lang language
	 */
	public TextToSpeech(String lang) {
		this.lang = lang;
	}
	
	/**
	 * Use this for final response to user for example "Playing What Is Love"
	 * @param text text to speak
	 * @return mp3 file with spoken text wrapped in Speech class
	 */
	public Speech textToSpeech(String text, QueryOutput out) {
		try {
			URLBuilder urlB = new URLBuilder("http://translate.google.com/translate_tts");
			
			urlB.args.put("tl", lang); // language
			urlB.args.put("q", URLEncoder.encode(text, "UTF-8")); // text to speech
			urlB.args.put("client", "tw-ob"); // to fool google :D
			urlB.args.put("ie", "UTF-8");
			
			return new Speech(NetworkUtils.downloadFile(urlB.buildUrl(), File.createTempFile("speech", ".mp3")).getAbsolutePath(), out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Use this for generating messages for use for example "You can speak now"
	 * @param text text to speak
	 * @return mp3 file with spoken text wrapped in Speech class
	 */
	public Speech textToSpeech(String text) {
		return textToSpeech(text, new QueryOutput("ALL", 0, text, null));
	}
	
}
