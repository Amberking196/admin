package com.server.module.system.machineManage.machinesWayItem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machineCode.VendingMachinesCodeDao;
import com.server.module.system.machineManage.machinesWayItem.restart.OpenDoorVo;
import com.server.redis.RedisClient;
import com.server.util.JsonUtils;
import com.server.util.ReviseUtil;
import com.server.util.StringUtil;
@Component
public class MachinesClient {
	
	private final static Logger log = LogManager.getLogger(MachinesClient.class);
	
	@Value("${machines.restartHost}")
	private String restartHost;
	
	
	@Value("${machines.restartVisionHost}")
	private String restartVisionHost;
	
	@Autowired
	private VendingMachinesCodeDao vendingMachinesCodeDao;
	@Autowired
	private RedisClient redisClient;
	
	public String sendCommand(String factNum,String command,String fromClient){
		String returnValue = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		OpenDoorVo openDoorVo = new OpenDoorVo();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(restartHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		openDoorVo.setFromClient(fromClient);
		openDoorVo.setMessage(command);
		openDoorVo.setToClient(factNum);
		HttpPost httpPost = new HttpPost(uri);
		String str = JSON.toJSONString(openDoorVo);// 将json对象转换为字符串
		StringEntity requestEntity = new StringEntity(str, "utf-8");
		requestEntity.setContentEncoding("UTF-8");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			if(response != null && 200 == response.getStatusLine().getStatusCode()){
				String responseEntity = EntityUtils.toString(response.getEntity());
				returnValue = JsonUtils.toObject(responseEntity, new TypeReference<String>(){});
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(httpclient!=null){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	public String sendVisionCommand(String factNum,String command,String fromClient){
		String returnValue = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		OpenDoorVo openDoorVo = new OpenDoorVo();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(restartVisionHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		openDoorVo.setFromClient(fromClient);
		openDoorVo.setMessage(command);
		openDoorVo.setToClient(factNum);
		HttpPost httpPost = new HttpPost(uri);
		String str = JSON.toJSONString(openDoorVo);// 将json对象转换为字符串
		StringEntity requestEntity = new StringEntity(str, "utf-8");
		requestEntity.setContentEncoding("UTF-8");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			if(response != null && 200 == response.getStatusLine().getStatusCode()){
				String responseEntity = EntityUtils.toString(response.getEntity());
				returnValue = JsonUtils.toObject(responseEntity, new TypeReference<String>(){});
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(httpclient!=null){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	/**
	 * 机器重启
	 * @author hebiting
	 * @date 2018年10月8日下午3:50:40
	 */
	public String restartMachines(String vmCode){
		String returnValue = null;
		//获取重启机器命令
		String restartCommand = ReviseUtil.restartCommand();
		String factoryNumber = vendingMachinesCodeDao.getFactoryNumByVmCode(vmCode);
		if(StringUtil.isBlank(factoryNumber)){
			log.info(vmCode+"：该机器出厂编号为空，无法向机器发送命令操作,开门用户信息：");
			return returnValue;
		}
		String reString = redisClient.get("HM-"+factoryNumber.trim());
		if(!reString.contains("s:0")) {
			log.info(vmCode+"机器非空闲"+reString);
			return null;//1失败
		}
		factoryNumber = factoryNumber.trim();
		returnValue = sendCommand(factoryNumber,restartCommand,"adminSystem");
		return returnValue;
	}
	
	public String restartVisionMachines(String vmCode){
		String returnValue = null;
		//获取重启机器命令
		String factoryNumber = vendingMachinesCodeDao.getFactoryNumByVmCode(vmCode);
		if(StringUtil.isBlank(factoryNumber)){
			log.info(vmCode+"：该机器出厂编号为空，无法向机器发送命令操作,开门用户信息：");
			return returnValue;
		}
		String reString = redisClient.get("HM-"+factoryNumber.trim());
		if(!reString.contains("s:0")) {
			log.info(vmCode+"机器非空闲"+reString);
			return null;//1失败
		}
		factoryNumber = factoryNumber.trim();
		//String restartCommand = ReviseUtil.genRebootCommandStr(factoryNumber,1);
		String restartCommand = ReviseUtil.restartCommand();

		returnValue = sendVisionCommand(factoryNumber,restartCommand,"adminSystem");
		return returnValue;
	}
}
