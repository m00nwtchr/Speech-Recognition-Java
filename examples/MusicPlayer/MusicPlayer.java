package MusicPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import io.github.lukas2005.speechrecognition.SpeechToText;
import io.github.lukas2005.speechrecognition.TextToSpeech;
import io.github.lukas2005.speechrecognition.audio.SoundRecordingUtil;
import io.github.lukas2005.speechrecognition.audio.Speech;
import io.github.lukas2005.speechrecognition.queryprocessing.ConfigurableQueryProcessor;
import io.github.lukas2005.speechrecognition.queryprocessing.EventListener;
import io.github.lukas2005.speechrecognition.queryprocessing.IQueryProcessor;
import io.github.lukas2005.speechrecognition.queryprocessing.QueryOutput;
import io.github.lukas2005.speechrecognition.utils.Utils;
import javazoom.jl.player.Player;

/**
 * 
 * @author lukas2005
 *
 */
public class MusicPlayer {

	// Google Speech APi Key HERE
	public static String googleSpeechApiKey = "API-KEY";
	
	//Instance of query processor
	public static IQueryProcessor queryp = new ConfigurableQueryProcessor(new File("commands.ini"));
	//Instance of TextToSpeech converter
	public static TextToSpeech tts = new TextToSpeech("pl-pl");
	//Instance of SpeechToText converter
	public static SpeechToText stt = new SpeechToText("pl-pl", googleSpeechApiKey);
	
	public static void main(String[] args) {
		//Event listener to add song name at the end of response and play music
		new EventListener("PLAY_MUSIC", true, true) {
			
			@Override
			public void fireOnSpeechEnd(QueryOutput queryOutputt, List<String> query, List<String> queryArgs, Object... data) {
				//Play music
				try {
					Player pl = new Player(new FileInputStream(new File(Utils.listIntoString(queryArgs))+".mp3"));
					pl.play();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void fireOnGetStringOutput(QueryOutput queryOutput, List<String> query, List<String> queryArgs, Object... data) {
				String originalStringOutput = queryOutput.getStringOutputNoCallListeners();
				List<String> originalStringOutputSplit = Arrays.asList(originalStringOutput.split(" "));

				if (!originalStringOutputSplit.containsAll(queryArgs)) {
					String toAdd = " ";
					for (String arg : queryArgs) {
						toAdd += arg + " ";
					}
					queryOutput.setStringOutput(originalStringOutput + toAdd);
				}
			}
		};

		// Some debug info
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.arch"));
		System.out.println(System.getProperty("os.version"));
		
		//Define recording file
		File recording = new File("mic.wav");
		recording.deleteOnExit();
		
		//Create instance of sound recorder
		final SoundRecordingUtil sru = new SoundRecordingUtil();
		
		//Start recording users voice Note: SoundRecordingUtil#start() blocks current thread until SoundRecordingUtil#stop is called
		new Thread() {
			public void run() {
				try {
					sru.start();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		try {
			//Give the microphone time to intialize
			Thread.sleep(100);
			
			//Generate audio to notify user to speak
			Speech speech = tts.textToSpeech("Tak?");
			
			//Notify users that they can speak now
			try {
				speech.speakAndDeleteFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//After 5 secs...
			Thread.sleep(5000);
			//Recording is stopped and file is saved
			sru.stop();
			sru.save(recording);
		} catch (InterruptedException | IOException e1) {}
		
		//Then convert speech to text using Googles wonderful api
		String result = stt.speechToText(recording);
		//And make sure to delete temp file
		recording.delete();
		
		//Debug info
		System.out.println(result);
		
		//Then process query by checking it with config file
		QueryOutput output = queryp.processQuery(result);
		
		//Debug info
		System.out.println(output.getStringOutput());
		
		//Generate response using TextToSpeech api also from Google
		Speech speech = tts.textToSpeech(output.getStringOutput(), output);
		
		//Play generated audio file using JLayer and make sure to delete temp files
		try {
			speech.speakAndDeleteFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}