package com.server.module.system.couponManager.couponCustomer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import com.server.util.SmsSendRequest;
import com.server.util.SmsSendResponse;
import com.server.util.SmsSendUtil;
import com.server.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */
@Api(value = "CouponCustomerController", description = "客户优惠劵")
@RestController
@RequestMapping("/couponCustomer")
public class CouponCustomerController {

	private static Logger log=LogManager.getLogger(CouponCustomerController.class);
    @Autowired
    private CouponCustomerService couponCustomerServiceImpl;
    
    @Value("${spring.profiles.active}")
	private String active;
    
   /* @ApiOperation(value = "客户优惠劵列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(CouponCustomerForm condition) {
        return couponCustomerServiceImpl.listPage(condition);
    }*/
    @ApiOperation(value = "客户优惠劵列表", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<CustomerCouponVo> list(int customerId) {
        return couponCustomerServiceImpl.list(customerId);
    }

    @ApiOperation(value = "客户列表For 客户添加", notes = "listPageForCustomer", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPageForCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPageForCustomer(CouponCustomerForm condition) {
    	log.info("<CouponCustomerController>----<listPageForCustomer>------start");
    	ReturnDataUtil listPageForCustomer = couponCustomerServiceImpl.listPageForCustomer(condition);
    	log.info("<CouponCustomerController>----<listPageForCustomer>------end");
    	return listPageForCustomer;
    }

    /*@ApiOperation(value = "客户优惠劵添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody CouponCustomerBean entity) {
        return new ReturnDataUtil(couponCustomerServiceImpl.add(entity));
    }*/

   /* @ApiOperation(value = "客户优惠劵修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody CouponCustomerBean entity) {
        return new ReturnDataUtil(couponCustomerServiceImpl.update(entity));
    }*/

    @ApiOperation(value = "用户券短信提醒列表", notes = "conponCustomerNoteList", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/conponCustomerNoteList", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil conponCustomerNoteList(@RequestBody(required=false) ConponCustomerNoteForm conponCustomerNoteForm) {
    	log.info("<CouponCustomerController>----<conponCustomerNoteList>------start");
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	if(conponCustomerNoteForm==null) {
    		conponCustomerNoteForm=new ConponCustomerNoteForm();
    	}
    	returnDataUtil=couponCustomerServiceImpl.conponCustomerNoteList(conponCustomerNoteForm);
    	log.info("<CouponCustomerController>----<conponCustomerNoteList>------end");
    	return returnDataUtil;
    }
    /**
     * 给部分用户发送短信
     */
    @ApiOperation(value = "给部分用户发送短信", notes = "给部分用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendMessage")
    @ResponseBody
    public ReturnDataUtil sendMessage(@RequestBody(required = false) ConponCustomerNoteForm form) {
		log.info("<CouponCustomerController>--<sendMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if(!active.equals("dev")) {
        	if(form.getPhoneList()!=null) {
    			if(form.getContent()!=null) {
    				log.info("<CouponCustomerController--sendMessage--start>");
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
    					//修改状态
    					couponCustomerServiceImpl.updateIsSendState(form);
    					returnDataUtil.setStatus(1);
    					returnDataUtil.setMessage("短信发送成功！");
       					returnDataUtil.setReturnObject(form.getPhoneList().size());
    					return returnDataUtil;
    				} else {
    					returnDataUtil.setStatus(-99);
    					returnDataUtil.setMessage("短信发送失败！");
    					return returnDataUtil;
    				}
    			}
        	}
        }
        returnDataUtil.setReturnObject(form.getPhoneList().size());
		log.info("<CouponCustomerController>--<sendMessage>--end");
		return returnDataUtil;
    }
    
    /**
     * 给全部用户发送短信
     */
    @ApiOperation(value = "给全部用户发送短信", notes = "给全部用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendAllMessage")
    @ResponseBody
    public ReturnDataUtil sendAllMessage(@RequestBody(required = false) ConponCustomerNoteForm form) {
		log.info("<CouponCustomerController>--<sendAllMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if (form == null) {
            form = new ConponCustomerNoteForm();
        }
        form.setIsShowAll(1);    
		List<CouponCustomerDto> list=(List<CouponCustomerDto>) couponCustomerServiceImpl.conponCustomerNoteList(form).getReturnObject();
        List<Long> ids=Lists.newArrayList();
        for (CouponCustomerDto couponCustomerDto : list) {
			ids.add(couponCustomerDto.getCustomerId());
		}
        
        String join = StringUtils.join(ids, ",");
        form.setId(join);
        
        String phoneList="";int t=1;

   		int time= list.size()%900==0?list.size()/900:list.size()/900+1;
   		log.info("发送"+time+"次 size"+list.size());
        //非开发模式 
        if(!active.equals("dev")) {
    		for(int i=1;i<=list.size();i++) {
    			if(phoneList.equals("")) {
    				if(StringUtil.isNotBlank(list.get(i-1).getPhone())) {
        				phoneList=list.get(i-1).getPhone().toString();
    				}
    			}else {
    				if(StringUtil.isNotBlank(list.get(i-1).getPhone())) {
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
    					//修改状态
    					couponCustomerServiceImpl.updateIsSendState(form);
    					returnDataUtil.setStatus(1);
    					returnDataUtil.setMessage("短信发送成功！");
       					returnDataUtil.setReturnObject(list.size());
    				} else {
    					returnDataUtil.setStatus(-99);
    					returnDataUtil.setMessage("短信发送失败！");
    					return returnDataUtil;
    				}
    				t=t+1;
    				phoneList="";
    			}
    		}
        }
        returnDataUtil.setReturnObject(list.size());
		log.info("<CouponCustomerController>--<sendAllMessage>--end");
    	return returnDataUtil;
    }
}

