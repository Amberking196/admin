package com.server.module.system.synthesizeManage.machineCustomer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.itemManage.itemBasic.ItemBasicController;
import com.server.module.system.synthesizeManage.roleManage.RoleBean;
import com.server.module.system.synthesizeManage.roleManage.RoleManageService;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaForm;
import com.server.module.system.userManage.CustomerBean;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/machineCustomer")
@Api(value = "machineCustomerController", description = "售货机用户的查询")
public class MachineCustomerController {
	
	public static Logger log = LogManager.getLogger(MachineCustomerController.class); 	
	
	@Autowired
	MachineCustomerService machineCustomerService;
	
	@ApiOperation(value = "根据条件查询售货机所有用户信息", notes = "根据条件查询售货机所有用户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findCustomerByForm")
	@ResponseBody
	public ReturnDataUtil findCustomerByForm(@RequestBody(required=false) TimeForm timeForm, HttpServletRequest request){
		
		return machineCustomerService.findCustomerByForm(timeForm);

	}
}
