package com.server.module.app.payRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.login.AppLoginInfoService;
import com.server.module.app.login.LoginInfoBean;
import com.server.module.app.login.LoginInfoEnum;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/home")
public class AppPayRecordController {
	
	@Autowired
	private AppPayRecordService payRecordService;
	@Autowired
	private AppLoginInfoService loginInfoService;
	@Autowired
	private CompanyService companyService;

	/**
	 * 查询销售记录
	 * @author hebiting
	 * @date 2018年6月5日上午10:13:10
	 * @return
	 */
	@PostMapping("/payRecord")
	public ReturnDataUtil payRecord(@RequestBody(required=false)PayRecordForm payRecordForm,
			HttpServletRequest request){
		if(payRecordForm==null){
			payRecordForm = new PayRecordForm();
		}
		ReturnDataUtil returnData = new ReturnDataUtil();
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DATE), 0, 0, 0);
		Date startDate = calendar.getTime();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DATE), 23, 59, 59);
		Date endDate = calendar.getTime();
		payRecordForm.setStartDate(startDate);
		payRecordForm.setEndDate(endDate);
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID)==null?null:Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean loginUser = loginInfoService.queryById(id);
		Integer companyId = loginUser.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyId(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		payRecordForm.setCompanyIds(companyIds);
		if(!LoginInfoEnum.PRINCIPAL.getMessage().equals(loginUser.getIsPrincipal())){
			payRecordForm.setDutyId(loginUser.getId());
		}
		List<PayRecordDto> findPayRecord = payRecordService.findPayRecord(payRecordForm);
		if(loginUser.getLevel()==1 || loginUser.getLevel()==2) {
			returnData.setStatus(1);
			returnData.setMessage("成功");
			returnData.setReturnObject(findPayRecord);
		}
		else {
			returnData.setStatus(0);
			returnData.setMessage("查询无权限");
			returnData.setReturnObject(null);
		}
		return returnData;
	}
}
