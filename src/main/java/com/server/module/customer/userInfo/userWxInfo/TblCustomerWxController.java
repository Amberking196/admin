package com.server.module.customer.userInfo.userWxInfo;

import java.util.List;
import java.util.Objects;

import com.server.module.customer.order.OrderService;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.customer.CustomerUtil;
import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-11-15 15:05:14
 */
@Api(value = "TblCustomerWxController", description = "微信用户信息")
@RestController
@RequestMapping("/tblCustomerWx")
public class TblCustomerWxController {

	private static Logger log=LogManager.getLogger(TblCustomerWxController.class);
	@Autowired
	private TblCustomerWxService tblCustomerWxServiceImpl;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;
	
	
	@ApiOperation(value = "微信用户信息列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(TblCustomerWxForm condition) {
		return tblCustomerWxServiceImpl.listPage(condition);
	}

	

	@ApiOperation(value = "微信用户信息修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody TblCustomerWxBean entity) {
		return new ReturnDataUtil(tblCustomerWxServiceImpl.update(entity));
	}

	@ApiOperation(value = "查询微信用户信息", notes = "getBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getBean() {
		log.info("<TblCustomerWxController>------<getBean>------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		Long customerId = CustomerUtil.getCustomerId();
		if(customerId==null) {
			customerId=userUtils.getSmsUser().getId();
		}
		String huafaAppOpenId = orderService.findHuafaopenIdByCustomerId(customerId);
		TblCustomerWxBean tblCustomerWxBean = tblCustomerWxServiceImpl.get(customerId);
		if(tblCustomerWxBean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("信息查看成功");
			returnDataUtil.setReturnObject(tblCustomerWxBean);
			if(tblCustomerWxBean.getCompanyId()==0) {
				returnDataUtil.setStatus(-66);
				returnDataUtil.setMessage("非优水用户，目前不支持余额充值！");
			}
		}else if (StringUtil.isNotEmpty(huafaAppOpenId) && Objects.isNull(tblCustomerWxBean)){
			CustomerBean cus = customerService.findCustomerById(customerId);
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("信息查看成功,非优水用户，目前不支持余额充值！");
			returnDataUtil.setReturnObject(cus);
		}else {
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("暂无微信信息,请录入信息！");
		}
		log.info("<TblCustomerWxController>------<getBean>------end");
		return returnDataUtil;
	}
	
	 @ApiOperation(value = "我的邀请奖励", notes = "myInviteRewards", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    @PostMapping(value = "/myInviteRewards", produces = "application/json;charset=UTF-8")
	    public ReturnDataUtil myInviteRewards() {
	        log.info("<TblCustomerWxController>-----<myInviteRewards>-----start");
	        ReturnDataUtil returnDataUtil=new ReturnDataUtil();
	        List<TblCustomerWxBean> list=tblCustomerWxServiceImpl.myInviteRewards();
	        if(list.size()>0 && list!=null){
	            returnDataUtil.setStatus(1);
	            returnDataUtil.setMessage("我的邀请奖励查看成功");
	            returnDataUtil.setReturnObject(list);
	        }else{
	            returnDataUtil.setStatus(0);
	            returnDataUtil.setMessage("我的邀请奖励查看失败");
	            returnDataUtil.setReturnObject(list);
	        }
	        log.info("<TblCustomerWxController>-----<myInviteRewards>-----end");
	        return returnDataUtil;
	    }
}
