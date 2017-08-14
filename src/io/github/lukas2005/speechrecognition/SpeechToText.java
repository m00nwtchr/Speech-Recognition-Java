package io.github.lukas2005.speechrecognition;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lukas2005.speechrecognition.utils.NetworkUtils;
import io.github.lukas2005.speechrecognition.utils.NetworkUtils.URLBuilder;

/**
 * 
 * @author lukas2005
 * Uses Google web api to convert speech to text
 */
public class SpeechToText {

	public String lang;
	private String apiKey;
	
	/**
	 * @param lang language
	 */
	public SpeechToText(String lang, String apiKey) {
		this.lang = lang;
		this.apiKey = apiKey;
	}
	
	/**
	 * 
	 * @param speech audio file to transcribe
	 * @return text
	 */
	public String speechToText(File speech) {
		URLBuilder urlb = new URLBuilder("http://www.google.com/speech-api/v2/recognize");
		
		urlb.args.put("key", apiKey);
		urlb.args.put("lang", lang);
		
		String transcript = null;
		try {
			String resultJsonString = NetworkUtils.uploadAudioFileAndGetResult(urlb.buildUrl(), speech);
			System.out.println(resultJsonString);
			JSONObject resultJson = new JSONObject(resultJsonString);
			
			JSONArray resultArray = resultJson.getJSONArray("result");
			JSONArray alternativeArray = resultArray.getJSONObject(0).getJSONArray("alternative");
			
			double hiestConfidence = -1.0;
			for (int i = 0; i < alternativeArray.length(); i++) {
				JSONObject obj = alternativeArray.getJSONObject(i);

				double currentConf = obj.getDouble("confidence");
				if (currentConf > hiestConfidence) {
					hiestConfidence = currentConf; 
					transcript = obj.getString("transcript");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transcript;
	}
}
