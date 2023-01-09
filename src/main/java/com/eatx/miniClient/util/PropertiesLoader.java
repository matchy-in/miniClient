package com.eatx.miniClient.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	Properties properties = new Properties();
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public PropertiesLoader(String platform){
		/**
		 * load config file
		 */
		ClassLoader classLoader = getClass().getClassLoader();
		String configProperties = classLoader.getResource("config-"+platform+".properties").getFile();
		
		try (InputStream input = new FileInputStream(configProperties)) {
			properties = new Properties();

			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
}
