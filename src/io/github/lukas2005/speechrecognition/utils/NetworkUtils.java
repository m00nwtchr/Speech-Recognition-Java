package io.github.lukas2005.speechrecognition.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author lukas2005
 *
 */
public class NetworkUtils {

	
	/**
	 * Downloads file from web
	 * @param url url to download
	 * @param destFile target file (needs to be file not dir!)
	 * @return downloaded file (same as destFile)
	 * @throws IOException
	 */
	public static File downloadFile(URL url, File destFile) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16241");
		conn.connect();
		ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
		FileOutputStream fos = new FileOutputStream(destFile);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		return destFile;
	}
	
	/**
	 * Designed to work with Google SpeechToText api but i think it can work with something other too!
	 * Note: will use ffmpeg to convert to .flac if audio file is not .wav or .flac
	 * @param url
	 * @param fileIn
	 * @return result
	 * @throws IOException
	 */
	public static String uploadAudioFileAndGetResult(URL url, File fileIn) throws IOException {
		
		File file = null;
		
		if (!fileIn.exists()) throw new FileNotFoundException(fileIn.getPath());
		
		if (!fileIn.getName().endsWith(".wav") || !fileIn.getName().endsWith(".flac") && !Utils.isOnARM()) {
			File tempTarget = File.createTempFile("speech", ".flac");
			String cmd="ffmpeg -y -i \""+fileIn.getAbsolutePath()+"\" \""+tempTarget.getAbsolutePath()+"\"";
			Process pr = Runtime.getRuntime().exec(cmd);
		 
			try {
				System.out.println("Wait for ffmpeg to close");
				pr.waitFor();
			} catch (InterruptedException e) {
				
				pr.destroyForcibly();  
				
			}

			tempTarget.deleteOnExit();
			file = tempTarget;
		} else {
			file = fileIn;
		}
		
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16241");
		
		if (Utils.isOnARM() || fileIn.getName().endsWith(".wav")) {
			conn.setRequestProperty("Content-Type", "audio/l16; rate=16000");
		} else {
			conn.setRequestProperty("Content-Type", "audio/x-flac; rate=16000");
		}
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.connect();

        // Send POST output.

        OutputStream outputStream = conn.getOutputStream();



        FileInputStream fileInputStream = new FileInputStream(file);



        byte[] buffer = new byte[256];



        while ((fileInputStream.read(buffer, 0, 256)) != -1) {

            outputStream.write(buffer, 0, 256);

        }



        fileInputStream.close();

        outputStream.close();

        // Get response data.

        HttpURLConnection httpConn = (HttpURLConnection)conn;
        InputStream is;
        if (httpConn.getResponseCode() >= 400) {
            is = httpConn.getErrorStream();
        } else {
            is = httpConn.getInputStream();
        }
        
        BufferedReader br = null;
        
        List<String> completeResponse = new ArrayList<String>();
        
        try {
	        br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	
	        String response = br.readLine();
	
	        while(response != null) {
	        	//System.out.println(response);
	        	completeResponse.add(response);
		
	        	response = br.readLine();
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (br !=null) br.close();
            file.delete();
        }
        if (completeResponse.size() > 1) completeResponse.remove(0);
        return completeResponse.get(0);
	}
	
	/**
	 * Builds url from hashmap and base url
	 * @author lukas2005
	 *
	 */
	public static class URLBuilder {
		
		private String urlBase;
		public HashMap<String, String> args = new HashMap<String, String>();
		
		/**
		 * 
		 * @param urlBase base of the url to create
		 */
		public URLBuilder(String urlBase) {
			this.urlBase = urlBase;
		}
		
		/**
		 * 
		 * @return Completed url
		 * @throws MalformedURLException
		 */
		public URL buildUrl() throws MalformedURLException {
			String urlString = urlBase+'?';
			for (String key : args.keySet()) {
				urlString += key +'='+ args.get(key)+'&';
			}
			
			if (urlString.endsWith("&")) urlString = urlString.substring(0, urlString.length()-1);
			
			return new URL(urlString);
		}
		
	}
	
}
