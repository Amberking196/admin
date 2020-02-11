package com.server.module.system.companyManage;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.system.companyManage.aliPayConfig.AliPayConfig;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.util.JsonUtils;

public class PayConfigClient {

	/**
	 * 修改微信支付参数配置
	 * @author hebiting
	 * @date 2018年8月6日下午6:34:05
	 * @param merchantInfo
	 */
	protected static Boolean modifyConfig(MerchantInfo merchantInfo){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Boolean result = null;
		try {
//			HttpPost httpPost = new HttpPost("http://localhost:8099/wechat/updatePayConfig");
			HttpPost httpPost = new HttpPost("http://yms.youshuidaojia.com/wechat/updatePayConfig");
			httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
			String jsonParam = JsonUtils.toJson(merchantInfo);
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			httpPost.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(config);
			response = client.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				String returnValue = EntityUtils.toString(entity, "UTF-8");
				result = JsonUtils.toObject(returnValue, new TypeReference<Boolean>(){});
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
	
	
	/**
	 * 修改支付宝支付参数配置
	 * @author hebiting
	 * @date 2018年8月6日下午6:34:05
	 * @param merchantInfo
	 */
	protected static Boolean modifyAliConfig(AliPayConfig alipayConfig){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Boolean result = null;
		try {
//			HttpPost httpPost = new HttpPost("http://localhost:8099/alipay/updateAliConfig");
			HttpPost httpPost = new HttpPost("http://yms.youshuidaojia.com/alipay/updateAliConfig");
			httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
			String jsonParam = JsonUtils.toJson(alipayConfig);
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			httpPost.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(config);
			response = client.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				String returnValue = EntityUtils.toString(entity, "UTF-8");
				result = JsonUtils.toObject(returnValue, new TypeReference<Boolean>(){});
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
}
