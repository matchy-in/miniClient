package com.eatx.miniClient.util;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

public class HTTPUtilITest {
	
	PropertiesLoader propertiesLoader = new PropertiesLoader("dev");
	
    @Test
    public void quickGetHTTP(){
    	
    	String mailboxesUrl = propertiesLoader.getProperties().getProperty("miniRest.url")+"?customerRef=CR8";

    	Hashtable<String,String> headers = new Hashtable<String,String>();
    	headers.put("Accept", "application/json");
    	
    	try {
        	HTTPUtil httpUtil = new HTTPUtil();

    		System.out.println(httpUtil.quickGetHTTP(mailboxesUrl, headers));
    		System.out.println("done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

    @Test
    public void postHTTP(){

    	Hashtable<String,String> headers = new Hashtable<String,String>();
    	headers.put("Accept", "application/json");

    	
    	try {
        	HTTPUtil httpUtil = new HTTPUtil();
        	
	    	HttpEntity httpEntity = httpUtil.postHTTP(propertiesLoader.getProperties().getProperty("miniRest.url"), headers, "{" + 
	    			"    \"customer_ref\": \"CR7\"," + 
	    			"    \"customer_name\": \"Jackson Morgan\"," + 
	    			"    \"address_line_1\": \"22 Albert Rd\"," + 
	    			"    \"address_line_2\": \"\"," + 
	    			"    \"town\": \"Tamworth\"," + 
	    			"    \"county\": \"Staffordshire\"," + 
	    			"    \"country\": \"United Kingdom\"," + 
	    			"    \"postcode\": \"B79 7JS\"" + 
	    			"}");
	    	
			System.out.println(EntityUtils.toString(httpEntity, "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
