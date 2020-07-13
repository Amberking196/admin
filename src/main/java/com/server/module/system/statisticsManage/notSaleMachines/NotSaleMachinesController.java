package com.server.module.system.statisticsManage.notSaleMachines;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@Controller
@RequestMapping("/notSale")
public class NotSaleMachinesController {

	@Autowired
	private NotSaleMachinesService notSaleMachinesService;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private CompanyService companyService;
	
	
	/**
	 * 根据条件查询未销售机器信息
	 * @author hebiting
	 * @date 2018年6月1日上午10:15:44
	 * @param form
	 * @return
	 */
	@PostMapping("/queryVMByForm")
	@ResponseBody
	public ReturnDataUtil findNotSaleMachinesByForm(@RequestBody(required=false) NotSaleMachinesForm form,HttpServletRequest request){
		if (form == null) {
			form = new NotSaleMachinesForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		form.setAreaId(user.getAreaId());
		if(form.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			form.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(form.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		form.setCompanyIds(companyIds);
		return notSaleMachinesService.findNotSaleMachinesByForm(form);
	}
	
	@PostMapping("/initInfo")
	@ResponseBody
	public ReturnDataUtil initInfo(HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<CompanyBean> findAllSonCompany = companyService.findAllSonCompany(companyId);
		return ResultUtil.success(findAllSonCompany);
	}
}
