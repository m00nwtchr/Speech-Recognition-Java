package io.github.lukas2005.speechrecognition.queryprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ini4j.Ini;

/**
 * 
 * @author lukas2005
 * Class for processing queries
 */
public class ConfigurableQueryProcessor implements IQueryProcessor {

	File configFile;
	
	/**
	 * 
	 * @param configFile file to grab config for commands if not exists gonna be created
	 */
	public ConfigurableQueryProcessor(File configFile) {
		this.configFile = configFile;
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Here the magic happens!
	 * @param query query to process
	 */
	@Override
	public QueryOutput processQuery(String query) {
		try {
			Ini ini = new Ini(configFile);
			for (String key : ini.get("Commands").keySet()) {
				String[] keySplit;
				if (key.contains("|")) {
					keySplit = key.split("\\|");
				} else {
					keySplit = new String[1];
					keySplit[0] = key;
				}
				for (String k : keySplit) {
					List<String> keySplit2 = Arrays.asList(k.split(" "));
					List<String> querySplit = Arrays.asList(query.split(" "));
					for (String q : querySplit) {
						String toSearch = null;
						try {
							toSearch = keySplit2.get(querySplit.indexOf(q));
						} catch(ArrayIndexOutOfBoundsException e) {}
						if (toSearch != null) {
							if (q.matches(".*"+toSearch+".*")) {
								String output = ini.get("Commands").get(key);
								String[] out = output.split("\\|");
								ArrayList<String> queryArgs = new ArrayList<String>();
								queryArgs.addAll(Arrays.asList(query.split(" ")));
								queryArgs.removeAll(keySplit2);
								
								return new QueryOutput(out[0], 0, out[1], query, queryArgs);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new QueryOutput("ERROR", 1, "ERROR", null, null);
		}
		return new QueryOutput("NOT_FOUND_EXCEPTION", 2, "ERROR", null, null);
	}

}
