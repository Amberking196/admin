package com.server.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class HttpUtil {

	private static final Logger log = LogManager.getLogger(HttpUtil.class);
	
	public static String post(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpPost post = new HttpPost(uri.build());
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}else{
				log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String post(String url,String jsonParam){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpPost post = new HttpPost(uri.build());
			post.setHeader("Content-Type","application/json;charset=UTF-8");
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			post.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	public static String get(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpGet get = new HttpGet(uri.build());
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			get.setConfig(config);
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}else{
				log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String get(URIBuilder url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpGet get = new HttpGet(url.build());
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}else{
				log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String post(URIBuilder url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpPost post = new HttpPost(url.build());
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}else{
				log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String get(String url,int times){
		String result = null;
		try(CloseableHttpClient client = HttpClients.createDefault();){
			HttpGet get = new HttpGet(url);
			for(int i=0;i<times;i++){
				try(CloseableHttpResponse response = client.execute(get);){
					if(response.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = response.getEntity();
						result = EntityUtils.toString(entity,"UTF-8");
						break;
					}else{
						log.info(url+"--请求失败--response:"+JsonUtils.toJson(response));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送 POST 请求（HTTP），参数为 Key-Value 形式
	 *
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPost(String apiUrl, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			//httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
						.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			response = httpClient.execute(httpPost);
			System.out.println(response.toString());
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}
}
