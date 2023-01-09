package com.eatx.miniClient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CsvToJsonTest {

	File csvFile;
	
	@BeforeEach
	public void setup() {
		ClassLoader classLoader = getClass().getClassLoader();
		csvFile = new File(classLoader.getResource("customerInformation.csv").getFile());
	}
	
	/**
	 * convert csv file to json
	 * @throws IOException
	 */
	@Test
	public void csvToJson() throws IOException {
		List<String> jsons = CsvToJson.csvToJson(csvFile);
		
		assertEquals(5, jsons.size());
		assertEquals("{" + 
				"\"customerRef\":\"CR1\"," + 
				"\"customerName\":\"Marco Ma\"," + 
				"\"addressLine1\":\"Middleton Lane\"," + 
				"\"addressLine2\":\"\"," + 
				"\"town\":\"Tamworth\"," + 
				"\"county\":\"Staffordshire\"," + 
				"\"country\":\"United Kingdom\"," + 
				"\"postCode\":\"B78 2BL\"" + 
				"}", jsons.get(0));
	}
}
