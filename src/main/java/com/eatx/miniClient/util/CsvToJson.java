package com.eatx.miniClient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class CsvToJson {
	
	/**
	 * convert CSV file content into json file<br>
	 * key of json file comes from CSV header
	 * @param csvFile
	 * @return list of json string
	 * @throws IOException 
	 */
	public static List<String> csvToJson(File csvFile) throws IOException{

		List<String> jsons = new ArrayList<String>();
		
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile)))  {
            String line;
            boolean flag = true; 
            List<String> columns = null; 
            while ((line = bufferedReader.readLine()) != null) {
               if (flag) {
                   flag = false; 
                   //process header 
                   columns = Arrays.asList(line.split(","));
               } else {
                   //to store the object temporarily
                   JsonObject obj = new JsonObject(); 
                   List<String> chunks = Arrays.asList(line.split(","));

                   for(int i = 0; i < columns.size(); i++) {
                       obj.addProperty(columns.get(i), chunks.get(i));
                   }
                   

                   Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
                   jsons.add(gson.toJson(obj));
               } 
            }
        } catch(IOException io) {
        	throw io;
        }
        
        return jsons;
	}
}
