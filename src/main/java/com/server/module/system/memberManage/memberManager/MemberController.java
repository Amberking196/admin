package com.server.module.system.memberManage.memberManager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.server.module.customer.CustomerUtil;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.couponManager.coupon.CouponBean;
import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.couponManager.coupon.CouponService;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerBean;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerService;
import com.server.module.system.memberManage.memberTypeManage.MemberTypeController;
import com.server.util.ReturnDataUtil;
import com.server.util.SmsSendRequest;
import com.server.util.SmsSendResponse;
import com.server.util.SmsSendUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CouponEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-09-21 08:56:10
 */
@Api(value = "MemberController", description = "用户管理")
@RestController
@RequestMapping("/member")
public class MemberController {

	private static Logger log = LogManager.getLogger(MemberTypeController.class);

	@Autowired
	private MemberService memberServiceImpl;
	@Autowired
	private CouponService couponServiceImpl;
	@Autowired
	private CouponCustomerService couponCustomerServiceImpl;
	
	@Autowired
	private  CompanyService companyService;
	
	@Value("${spring.profiles.active}")
	private String active;

	@ApiOperation(value = "会员列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) MemberForm memberForm) {
		log.info("<MemberController>-----<listPage>------start");
		if (memberForm == null) {
			memberForm = new MemberForm();
		}
		if(memberForm.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(memberForm.getCompanyId());
			String companyIds = StringUtils.join(companyList, ",");
			memberForm.setCompanyIds(companyIds);
		}

		ReturnDataUtil	returnDataUtil = memberServiceImpl.listPage(memberForm);
		log.info("<MemberController>-----<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MemberBean entity) {
		log.info("<MemberController>-----<add>-----start");
		ReturnDataUtil	returnDataUtil=new ReturnDataUtil();
		// 判断手机号 是否为优水用户
		List<MemberBean> list = memberServiceImpl.getBean(entity.getPhone());
		if (list != null && list.size() > 0) {
			for (MemberBean memberBean : list) {
				entity.setCustomerId(memberBean.getCustomerId());
				boolean flag = memberServiceImpl.add(entity,list);
				if (flag) {//会员增加成功 下发优惠券
					Long customerId = memberBean.getCustomerId();
					CouponForm couponForm = new CouponForm();
					couponForm.setWay(CouponEnum.Member_COUPON.getState());
					couponForm.setUseWhere(CouponEnum.USE_MACHINES.getState());
					// 获取优惠券赠送方式 为会员赠券的优惠券信息
					List<CouponBean> presentCoupon = couponServiceImpl.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						presentCoupon.stream().forEach(coupon -> {
							CouponCustomerBean couCusBean = new CouponCustomerBean();
							couCusBean.setQuantity(coupon.getSendMax().longValue());
							couCusBean.setCouponId(coupon.getId());
							couCusBean.setCustomerId(customerId);
							couCusBean.setStartTime(coupon.getLogicStartTime());
							couCusBean.setEndTime(coupon.getLogicEndTime());
							couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
							couponCustomerServiceImpl.add(couCusBean);
						});
					}
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("会员添加成功！");
					returnDataUtil.setReturnObject(flag);
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("会员添加失败！");
					returnDataUtil.setReturnObject(flag);
				}
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("您输入的手机号非优水用户,请重新输入！");
		}
		log.info("<MemberController>-----<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员类型修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody MemberBean entity) {
		log.info("<MemberController>------<update>-------start");
		ReturnDataUtil	returnDataUtil=new ReturnDataUtil();
		boolean update = memberServiceImpl.udpate(entity);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("会员类型修改成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员类型修改失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<MemberController>------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员删除", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(String phone) {
		log.info("<MemberController>------<delete>-------start");
		ReturnDataUtil	returnDataUtil=new ReturnDataUtil();
		boolean delete = memberServiceImpl.deleteMember(phone);
		if (delete) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("会员删除成功！");
			returnDataUtil.setReturnObject(delete);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员删除失败！");
			returnDataUtil.setReturnObject(delete);
		}
		log.info("<MemberController>------<delete>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "判断是否是会员", notes = "judgeMember", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/judgeMember", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil judgeMember() {
		log.info("<MemberController>------<judgeMember>-------start");
		ReturnDataUtil returnDataUtil = memberServiceImpl.judgeMember();
		log.info("<MemberController>------<judgeMember>-------end");
		return returnDataUtil;
	}
	
	
	@ApiOperation(value = "会员信息", notes = "findBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findBean() {
		log.info("<MemberController>------<findBean>-------start");
		ReturnDataUtil returnDataUtil =new ReturnDataUtil();
		Long customerId = CustomerUtil.getCustomerId();
		log.info("用户id:===========" + customerId);
		MemberBean bean = memberServiceImpl.findBean(customerId);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(bean);
		}else { 
			returnDataUtil.setStatus(-99);
			returnDataUtil.setReturnObject(bean);
		}
		log.info("<MemberController>------<findBean>-------end");
		return returnDataUtil;
	}
	
	/**
     * 给部分用户发送短信
     */
    @ApiOperation(value = "给部分用户发送短信", notes = "给部分用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendMessage")
    @ResponseBody
    public ReturnDataUtil sendMessage(@RequestBody(required = false) MemberForm form) {
		log.info("<MemberController>--<sendMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if(!active.equals("dev")) {
        	if(form.getPhoneList()!=null) {
    			if(form.getContent()!=null) {
    				log.info("<MemberController--sendMessage--start>");
    				// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
    				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    				// 手机号码                                                                     
    				SmsSendRequest smsSingleRequest = new SmsSendRequest(form.getContent(), StringUtils.strip(form.getPhoneList().toString(),"[]"));

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
		log.info("<MemberController>--<sendMessage>--end");
		return returnDataUtil;
    }
    
    /**
     * 给全部用户发送短信
     */
    @ApiOperation(value = "给全部用户发送短信", notes = "给全部用户发送短信", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/sendAllMessage")
    @ResponseBody
    public ReturnDataUtil sendAllMessage(@RequestBody(required = false) MemberForm form) {
		log.info("<MemberController>--<sendAllMessage>--start");
		log.info("当前系统模式为"+active);
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        if (form == null) {
            form = new MemberForm();
        }
		if(form.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(form.getCompanyId());
			String companyIds = StringUtils.join(companyList, ",");
			form.setCompanyIds(companyIds);
		}
        form.setIsShowAll(1);
		List<MemberBean> list=(List<MemberBean>) memberServiceImpl.listPage(form).getReturnObject();
        List<Long> ids=Lists.newArrayList();
        for (MemberBean memberBean : list) {
			ids.add(memberBean.getCustomerId());
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
		log.info("<MemberController>--<sendAllMessage>--end");
    	return returnDataUtil;
    }
	
}
