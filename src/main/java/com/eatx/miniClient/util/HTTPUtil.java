package com.eatx.miniClient.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Marco Ma
 *
 */
public class HTTPUtil {

	private static final Logger logger = LogManager.getLogger(HTTPUtil.class);
	
	public static final int PoolingHttpClientConnectionPoolSize = 200;
	
	public static final int debugSize = 500;
	
	public static final int timeoutDuration = 120000;

	static SSLConnectionSocketFactory sslConnectionSocketFactory;
	static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	
    public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
		return poolingHttpClientConnectionManager;
	}

	public static void setPoolingHttpClientConnectionManager(
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
		HTTPUtil.poolingHttpClientConnectionManager = poolingHttpClientConnectionManager;
	}


	static {
    	System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		SSLContextBuilder builder = new SSLContextBuilder();
		try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", new PlainConnectionSocketFactory()).register("https", sslConnectionSocketFactory)
					.build();

			poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
			poolingHttpClientConnectionManager.setMaxTotal(PoolingHttpClientConnectionPoolSize);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public CloseableHttpClient createHttpClient(){
    	
    	//https connection
//		CloseableHttpClient httpclient = HttpClients.custom()
//		            .setSSLSocketFactory(sslConnectionSocketFactory)
//		            .setConnectionManager(poolingHttpClientConnectionManager)
//		            .build();
    	

		CloseableHttpClient httpclient = HttpClients.custom()
	            .setConnectionManager(poolingHttpClientConnectionManager)
	            .build();
		
		 
		return httpclient;
    }
    
    /**
     * HTTP post action
     * @param url
     * @param headers
     * @param json
     * @return json response
     * @throws IOException
     */
	public HttpEntity postHTTP(String url, Hashtable<String,String> headers, String json) throws IOException{
    	
		logger.info("url:"+url);
		logger.info("headers:"+headers);
		if (!StringUtils.isEmpty(json)) {
			if (json.length()>debugSize){
				logger.info("json:"+StringUtils.substring(json,0,debugSize)+"...");
			}else{
				logger.info("json:"+StringUtils.substring(json,0,debugSize));
			}
		}
		
		CloseableHttpClient httpclient = createHttpClient();

		HttpPost httpPost = new HttpPost(url);

		Set<String> headerKeys = headers.keySet();
		Iterator iterator = headerKeys.iterator();
		while(iterator.hasNext()){
			String key = iterator.next().toString();

			httpPost.addHeader(key, headers.get(key));
		}

		httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

		httpPost.expectContinue();
		CloseableHttpResponse response = httpclient.execute(httpPost);

		return response.getEntity();
    }
	
    /**
     * HTTP get action
     * @param url
     * @param headers
     * @return json response
     * @throws IOException
     */
	public HttpEntity getHTTP(String url, Hashtable<String,String> headers) throws IOException{

		logger.info("url:"+url);
		logger.info("headers:"+headers);
		
		CloseableHttpClient httpclient = createHttpClient();

		HttpGet httpGet = new HttpGet(url);
		RequestConfig config1 = RequestConfig.custom().setSocketTimeout(timeoutDuration).build();
		httpGet.setConfig(config1);

		if (headers != null){
			Set<String> headerKeys = headers.keySet();
			Iterator iterator = headerKeys.iterator();
			while(iterator.hasNext()){
				String key = iterator.next().toString();
	
				httpGet.addHeader(key, headers.get(key));
			}
		}
		
		CloseableHttpResponse response = httpclient.execute(httpGet);

		return response.getEntity();
    }

	/**
	 * quick method to achieve the response body of http get request
	 * @param url
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public String quickGetHTTP(String url, Hashtable<String,String> headers) throws IOException{

		HttpEntity httpEntity = getHTTP(url, headers);
		 
        if(httpEntity != null){  
            String resStr = EntityUtils.toString(httpEntity, "UTF-8");
            if ((resStr!=null)&&(!resStr.equals("[]"))){
    			if (resStr.length()>debugSize){
                	logger.debug(StringUtils.substring(resStr,0,debugSize)+"...");
    			}else{
                	logger.debug(StringUtils.substring(resStr,0,debugSize));
    			}
            }else{
                logger.debug(resStr);
            }
            return resStr;  
        }  

        return null;
	}
	
}
