package com.eatx.miniClient;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.internal.util.io.IOUtil;

import com.eatx.miniClient.util.CsvToJson;
import com.eatx.miniClient.util.HTTPUtil;
import com.eatx.miniClient.util.PropertiesLoader;

public class MiniClientApplication {

	private static final Logger logger = LogManager.getLogger(MiniClientApplication.class);
	
	/**
	 * Loop through the rows of the CSV file and send each row to a REST API POST endpoint, in JSON format
	 * @param args[0]: miniRest url, arg[1]: csv file path
	 */
	public static void main(String[] args) {

		if ((args.length < 2)||(!args[0].matches("dev|local"))) {
			System.out.println("please input platform[local|dev] and path of CSV");
			System.exit(1);
		}
		
		PropertiesLoader propertiesLoader = new PropertiesLoader(args[0]);

		logger.info("step 1: read CSV file, convert into JSON format");
		File csvFile = new File(args[1]);
		List<String> jsons = null;
		try {
			jsons = CsvToJson.csvToJson(csvFile);
		} catch (IOException e) {
			logger.error(e);
			System.out.println("invalid CSV file");
			System.exit(1);
		}

		logger.info("step 2: make HTTP POST connection");

		HTTPUtil httpUtil = new HTTPUtil();

		Hashtable<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "application/json");

		jsons.parallelStream().forEach((json) -> {
			try {
				HttpEntity httpEntity = httpUtil.postHTTP(propertiesLoader.getProperties().getProperty("miniRest.url"), headers, json);

				IOUtil.readLines(httpEntity.getContent()).stream().forEach(System.out::println);
			} catch (IOException e) {
				logger.error(e);
				System.out.println("HTTP connection error");
				System.exit(1);
			}

		});

		logger.info("done");
	}

}
