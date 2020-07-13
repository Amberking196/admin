package com.server.util.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.module.system.machineManage.machinesWayItem.ReviseForm;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;

public class SmsClient {

	public static ReturnDataUtil sendCode(String phone){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		ReturnDataUtil returnData = null;
		try {
			HttpPost httpPost = new HttpPost("http://yms.youshuidaojia.com/alisms/send");
			httpPost.setHeader("content-type","application/json;charset=UTF-8");
			Map<String,String> param = new HashMap<String,String>();
			param.put("phone", phone);
			HttpEntity paramEntity = new StringEntity(JsonUtils.toJson(param));
			httpPost.setEntity(paramEntity);
			response = client.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				String entityValue = EntityUtils.toString(entity, "UTF-8");
				returnData = JsonUtils.toObject(entityValue, new TypeReference<ReturnDataUtil>(){});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnData;
	}
	
	public static ReturnDataUtil sendMessage(String phone,String msg){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		ReturnDataUtil returnData = null;
		try {
//			HttpPost httpPost = new HttpPost("http://localhost:8099/alisms/send");
			HttpPost httpPost = new HttpPost("http://yms.youshuidaojia.com/alisms/sendMessage");
			List<BasicNameValuePair> nvpList = new ArrayList<BasicNameValuePair>();
			nvpList.add(new BasicNameValuePair("phone", phone));
			nvpList.add(new BasicNameValuePair("msg", msg));
			httpPost.setEntity(new UrlEncodedFormEntity(nvpList,"UTF-8"));
			response = client.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				String entityValue = EntityUtils.toString(entity, "UTF-8");
				returnData = JsonUtils.toObject(entityValue, new TypeReference<ReturnDataUtil>(){});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnData;
	}
	
	public static ReturnDataUtil sendOnceReviseTest(ReviseForm form){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		ReturnDataUtil result = null;
		try {
//			HttpPost httpPost = new HttpPost("http://localhost:8099/wechat/updatePayConfig");
        	HttpPost httpPost = new HttpPost("http://119.23.233.123:6662/ys_mms/mms/revise/onceReviseTest");
			//HttpPost httpPost = new HttpPost("http://localhost:8101/mms/revise/onceReviseTest");

			httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
			String jsonParam = JsonUtils.toJson(form);
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			httpPost.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(config);
			response = client.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				String returnValue = EntityUtils.toString(entity, "UTF-8");
				result = JsonUtils.toObject(returnValue, new TypeReference<ReturnDataUtil>(){});
				System.out.println("returnValue-----"+returnValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(response!=null){
					response.close();
				}
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
//	public static void main(String[] args) {
//		ReturnDataUtil sendCode = sendCode("13142360746");
//		System.out.println("sendCode--"+JsonUtils.toJson(sendCode));
//	}
}
