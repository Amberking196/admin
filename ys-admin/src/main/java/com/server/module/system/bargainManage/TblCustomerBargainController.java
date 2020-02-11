package com.server.module.system.bargainManage;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.alibaba.fastjson.JSON;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerController;
import com.server.module.system.shoppingManager.customerOrder.UsersConsumptionRecordForm;
import com.server.module.system.shoppingManager.customerOrder.UsersConsumptionRecordVo;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.SmsSendRequest;
import com.server.util.SmsSendResponse;
import com.server.util.SmsSendUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * author name: hjc
 * create time: 2018-12-24 16:20:26
 */ 
@Api(value ="TblCustomerBargainController",description="砍价")
@RestController
@RequestMapping("/tblCustomerBargain")
public class  TblCustomerBargainController{

	private static Logger log=LogManager.getLogger(TblCustomerBargainController.class);
	@Autowired
	private TblCustomerBargainService tblCustomerBargainServiceImpl;

	@Value("${spring.profiles.active}")
	private String active;
    
	@ApiOperation(value = "砍价订单列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false)TblCustomerBargainForm form){
   	 	if (form == null) {
   	 		form = new TblCustomerBargainForm();
   	 	}
		return tblCustomerBargainServiceImpl.listPage(form);
	}
	
	@ApiOperation(value = "部分发送短信(提醒类短信)",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/sendMessage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil sendMessage(@RequestBody(required = false)TblCustomerBargainForm form){
		log.info("<TblCustomerBargainController>--<sendMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	for(int i=0;i<form.getIdList().size();i++) {
    		tblCustomerBargainServiceImpl.updateSendMessage(form.getPhoneList().get(i), form.getIdList().get(i));
    	}
        if(!active.equals("dev")) {
        	if(form.getPhoneList()!=null) {
    			if(form.getContent()!=null) {
    				log.info("<TblCustomerBargainController--sendMessage--start>");
    				// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
    				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    				// 手机号码
    				SmsSendRequest smsSingleRequest = new SmsSendRequest(form.getContent(), StringUtils.strip(form.getPhoneList().toString(),"[]"));
    				smsSingleRequest.setAccount("N1445247");
    				smsSingleRequest.setPassword("31oVBqdpPF9ec1");

    				String requestJson = JSON.toJSONString(smsSingleRequest);

    				log.info("before request string is: " + requestJson);

    				String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				if (response == null) {
    					response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
    				}
    				log.info("response after request result is :" + response);

    				if (response != null) {
    					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
    					log.info("response  toString is :" + smsSingleResponse);
       					returnDataUtil.setReturnObject(form.getPhoneList().size());
    					return returnDataUtil;
    				} else {
    					return ResultUtil.error(0,"失败发送",null);
    				}
    			}
        	}
        }
        returnDataUtil.setReturnObject(form.getPhoneList().size());
		log.info("<TblCustomerBargainController>--<sendMessage>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "全部发送短信(除已发送电话)",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/sendAllMessage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil sendAllMessage(@RequestBody(required = false)TblCustomerBargainForm form){
		log.info("<TblCustomerBargainController>--<sendMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	 if (form == null) {
             form = new TblCustomerBargainForm();
         }
    	 form.setIsShowAll(1);
    	 form.setSendMessage(0);
//    	 if(form.getState()==null) {
//    		 returnDataUtil.setMessage("请先选择订单状态搜索");
//    		 returnDataUtil.setStatus(0);
//    		 return returnDataUtil;
//    	 }

         List<TblCustomerBargainBean> list=(List<TblCustomerBargainBean>) tblCustomerBargainServiceImpl.listPageWithOutStateNum(form).getReturnObject();
     	 for(TblCustomerBargainBean t:list) {
     		if(StringUtils.isNotBlank(t.getPhone())) {
        		tblCustomerBargainServiceImpl.updateSendMessage(t.getPhone(), t.getId());
     		}
     	 }
         String phoneList="";int t=1;
    	 int time= list.size()%900==0?list.size()/900:list.size()/900+1;
    	 log.info("发送"+time+"次 size"+list.size());
         //非开发模式 
         if(!active.equals("dev")) {
     		for(int i=1;i<=list.size();i++) {
     			if(phoneList.equals("")) {
     				if(StringUtils.isNotBlank(list.get(i-1).getPhone())) {
         				phoneList=list.get(i-1).getPhone().toString();
     				}
     			}else {
     				if(StringUtils.isNotBlank(list.get(i-1).getPhone())) {
         				log.info(phoneList+"-"+list.get(i-1).getPhone());
         				phoneList=phoneList+","+list.get(i-1).getPhone().toString();
     				}
     			}
     			if(i%900==0 || (t==time && i==list.size()))  {

     				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
     				SmsSendRequest smsSingleRequest = new SmsSendRequest(form.getContent(), phoneList);
    				smsSingleRequest.setAccount("N1445247");
    				smsSingleRequest.setPassword("31oVBqdpPF9ec1");

     				String requestJson = JSON.toJSONString(smsSingleRequest);

     				log.info("before request string is: " + requestJson);
     				String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
     				if (response == null) {
     					response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
     				}
     				log.info("response after request result is :" + response);
     				if (response != null) {
     					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
     					log.info("response  toString is :" + smsSingleResponse);
     				} else {
     					return ResultUtil.error(0,"失败发送",null);
     				}
     				t=t+1;
     				phoneList="";
     			}
     		}
         }
        returnDataUtil.setReturnObject(list.size());
		log.info("<TblCustomerBargainController>--<sendMessage>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "砍价订单列表详情",notes = "detail",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil detail(@RequestBody(required = false)TblCustomerBargainForm form){
   	 	if (form == null) {
   	 		form = new TblCustomerBargainForm();
   	 	}
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	returnDataUtil.setReturnObject(tblCustomerBargainServiceImpl.detail(form));
    	returnDataUtil.setStatus(1);
    	return returnDataUtil;
	}
}

