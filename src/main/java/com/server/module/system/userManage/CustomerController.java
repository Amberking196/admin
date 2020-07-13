package com.server.module.system.userManage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.app.replenish.ReplenishForm;
import com.server.module.system.itemManage.itemBasic.ItemBasicController;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.replenishManage.machineHistory.VendingMachineHistoryBean;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.synthesizeManage.machineCustomer.MachineCustomerService;
import com.server.module.system.synthesizeManage.machineCustomer.TimeForm;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/userManage")
@Api(value = "CustomerController", description = "用户信息的查询")
public class CustomerController {
	
	public static Logger log = LogManager.getLogger(CustomerController.class);
	
	@Autowired
	CustomerService CustomerService;
	@Autowired
	CustomerDao CustomerDao;
	/**
	 * 查询用户信息
	 * @param userManagerForm
	 * @return
	 */
	@ApiOperation(value = "根据条件查询所有用户信息", notes = "根据条件查询所有用户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findCustomer")
	@ResponseBody
	public ReturnDataUtil findCustomerByForm(@RequestBody(required=false) UserManagerForm userManagerForm, HttpServletRequest request){
		if(userManagerForm == null){
			userManagerForm = new UserManagerForm();
		}
		return CustomerService.findCustomer(userManagerForm);

	}
	
	@ApiOperation(value = "查询用户的购水情况", notes = "查询用户的购水情况", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/queryDetail")
	@ResponseBody
	public ReturnDataUtil queryDetail(@RequestBody(required=false) UserManagerForm userManagerForm, HttpServletRequest request){
		
		return CustomerService.queryDetail(userManagerForm);
	}
	
	
	@ApiOperation(value = "查询推荐奖励", notes = "查询推荐奖励", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/queryInvite")
	@ResponseBody
	public ReturnDataUtil queryInvite(@RequestBody(required=false) UserManagerForm userManagerForm, HttpServletRequest request){
		if(userManagerForm==null) {
			userManagerForm=new UserManagerForm();
		}
		return CustomerService.queryInvite(userManagerForm);
	}
	
	@ApiOperation(value = "查询推荐奖励详情", notes = "查询推荐奖励详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/queryInviteDetail")
	@ResponseBody
	public ReturnDataUtil queryInviteDetail(@RequestBody(required=false) UserManagerForm userManagerForm, HttpServletRequest request){
		
		return CustomerService.queryInviteDetail(userManagerForm);
	}
	
	@ApiOperation(value = "用户多个消费时间导出(暂无导出按钮)", notes = "用户多个消费时间导出(暂无导出按钮)", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/customerExport", produces = "application/json;charset=UTF-8")
	public void customerExport(HttpServletRequest request,HttpServletResponse response){
		List<CustomerVo> list=CustomerDao.customerVo();
		for(CustomerVo c:list) {
			String cp[]=StringUtils.split(c.getPayTime(), ",");
		    List<String> stringB = Arrays.asList(cp);
		    c.setLength(cp.length);
		    List<CustomerPayTimeVo> payTimeList=Lists.newArrayList();
		    for(String a:stringB) {
		    	CustomerPayTimeVo k=new CustomerPayTimeVo();
		    	k.setDate(a);payTimeList.add(k);
		    }
		    c.setPayTimeList(payTimeList);
		}
        try {
            String fileName = "用户消费时间报表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");

            new ExportExcel("用户消费时间记录", CustomerVo.class).setDataList1(list)
                    .write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
