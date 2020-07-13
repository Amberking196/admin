package com.server.module.system.machineManage.machinesTest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/machinesTestLog")
public class MachinesTestController {

	@Autowired
	private MachinesTestLogService machinesTestService;
	
	/**
	 * 获取所有测试数据(分页)
	 * @author hebiting
	 * @date 2019年2月27日下午5:37:26
	 * @param form
	 * @return
	 */
	@PostMapping("/get")
	public ReturnDataUtil getMachinesTest(@RequestBody(required = false) MachinesTestForm form,HttpServletRequest request){
		if(form == null)
			form = new MachinesTestForm();
		if(form.getCompanyId() == null){
			Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		return machinesTestService.getMachinesTest(form);
	}
	
	/**
	 * 获取当前测试记录相关信息
	 * @author hebiting
	 * @date 2019年2月27日上午11:56:38
	 * @param id
	 * @return
	 */
	@PostMapping("/detail")
	public ReturnDataUtil geteTestRecord(Long id){
		return machinesTestService.geteTestRecord(id);
	}
}
