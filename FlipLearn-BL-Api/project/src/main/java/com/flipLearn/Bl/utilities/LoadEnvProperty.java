package com.flipLearn.BL.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Satya on 26/8/16.
 */
public class LoadEnvProperty extends LoadProperty{

	public String readProperty(String key){
		String currentEnv = (String) getProperty("project.env");
		Properties prop = new Properties();
		InputStream input = null;
		try {
			//input = new FileInputStream(System.getProperty("user.dir")+ "/src/config/"+ currentEnv +"/config.properties");
			input = new FileInputStream(System.getProperty("user.dir")+ "/src/config/"+ "prod" +"/config.properties");
			prop.load(input);
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
