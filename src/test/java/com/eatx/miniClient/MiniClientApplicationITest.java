package com.eatx.miniClient;

import org.junit.jupiter.api.Test;

import com.eatx.miniClient.util.PropertiesLoader;

class MiniClientApplicationITest {

	/**
	 * test MiniClientApplication main method
	 */
	@Test
	void testMain() {
		ClassLoader classLoader = getClass().getClassLoader();
		String csvFile = classLoader.getResource("customerInformation.csv").getFile();
		String[] args = { "local", csvFile };

		MiniClientApplication.main(args);
	}

}
