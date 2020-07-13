package com.server.module.system.region;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lly835.bestpay.utils.JsonUtil;
import com.server.module.commonBean.KeyValueBean;
import com.server.util.JsonUtils;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.SceneEnum;

@RestController
@RequestMapping("/region")
public class RegionController {
	
	//@Value("${sms.address}")
	private String smsAddress;

	@Autowired
	private RegionService regionService;
	
	/**
	 * 获取地址信息
	 * @author hebiting
	 * @date 2018年8月1日下午3:46:36
	 * @param pid
	 * @return
	 */
	@PostMapping("/info")
	public ReturnDataUtil regionInfo(Integer pid){
		if(pid == null){
			pid = 0;
		}
		List<RegionBean> byParentId = regionService.getByParentId(pid);
		if(byParentId!=null && byParentId.size()>0){
			return ResultUtil.success(byParentId);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 获取场景信息
	 * @author hebiting
	 * @date 2018年8月1日下午3:46:28
	 * @param scene
	 * @return
	 */
	@PostMapping("/sceneInfo")
	public ReturnDataUtil sceneInfo(String scene){
		if(scene == null){
			scene = "";
		}
		 List<KeyValueBean> findSonNode = SceneEnum.findSonNode(scene);
		if(findSonNode!=null && findSonNode.size()>0){
			return ResultUtil.success(findSonNode);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 入驻阿里
	 * @author hebiting
	 * @date 2018年8月1日下午4:33:17
	 * @return
	 */
	@PostMapping("/enterAli")
	public ReturnDataUtil enterAli(@RequestBody EnterAliBean enterAli){
		if(enterAli == null){
			return ResultUtil.error(0, "入驻失败，无参数", null);
		}
		String vmCode = enterAli.getTerminal_id();
		if(StringUtil.isBlank(vmCode) || !regionService.canEnterAli(vmCode)){
			return ResultUtil.error(0, "参数有误或机器暂无入驻权限", null);
		}
		if(enterAli.getDelivery_address() == null){
			enterAli.setDelivery_address(enterAli.getPoint_position());
		}
		System.out.println(JsonUtil.toJson(enterAli));
		ReturnDataUtil returnData = null;
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpPost post = new HttpPost(smsAddress + "/aliMachine/enterAli");
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		String json = JsonUtils.toJson(enterAli);
		HttpEntity entity = new StringEntity(json,"UTF-8");
		post.setEntity(entity);
		RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectionRequestTimeout(2000).build();
		post.setConfig(config);
		try {
			response = client.execute(post);
			if(response != null && response.getStatusLine().getStatusCode() == 200){
				HttpEntity returnEntity = response.getEntity();
				String returnJson = EntityUtils.toString(returnEntity, "UTF-8");
				returnData = JsonUtils.toObject(returnJson, new TypeReference<ReturnDataUtil>(){});
				if(returnData == null){
					returnData = ResultUtil.error(0,"入驻失败",null);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null){
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		String delivery = regionService.getNameById(Integer.valueOf(deliveryAddress.getProvince_code()),
//				Integer.valueOf(deliveryAddress.getCity_code()), Integer.valueOf(deliveryAddress.getArea_code()));
		return returnData;
	}
	
	
	/**
	 * 修改入驻阿里信息
	 * @author hebiting
	 * @date 2018年8月1日下午4:33:17
	 * @return
	 */
	@PostMapping("/modifyEnterAli")
	public ReturnDataUtil modifyEnterAli(@RequestBody EnterAliBean enterAli){
		if(enterAli == null){
			return ResultUtil.error(0, "修改入驻信息失败，无参数", null);
		}
		String vmCode = enterAli.getTerminal_id();
		if(StringUtil.isBlank(vmCode) || !regionService.canEnterAli(vmCode)){
			return ResultUtil.error(0, "参数有误或机器暂无入驻权限", null);
		}
		if(enterAli.getDelivery_address() == null){
			enterAli.setDelivery_address(enterAli.getPoint_position());
		}
		ReturnDataUtil returnData = null;
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpPost post = new HttpPost(smsAddress + "/aliMachine/modifyEnterAli");
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		String json = JsonUtils.toJson(enterAli);
		HttpEntity entity = new StringEntity(json,"UTF-8");
		post.setEntity(entity);
		RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectionRequestTimeout(2000).build();
		post.setConfig(config);
		try {
			response = client.execute(post);
			if(response != null && response.getStatusLine().getStatusCode() == 200){
				HttpEntity returnEntity = response.getEntity();
				String returnJson = EntityUtils.toString(returnEntity, "UTF-8");
				returnData = JsonUtils.toObject(returnJson, new TypeReference<ReturnDataUtil>(){});
				if(returnData == null){
					returnData = ResultUtil.error(0,"修改入驻信息失败",null);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null){
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		String delivery = regionService.getNameById(Integer.valueOf(deliveryAddress.getProvince_code()),
//				Integer.valueOf(deliveryAddress.getCity_code()), Integer.valueOf(deliveryAddress.getArea_code()));
		return returnData;
	}
}
