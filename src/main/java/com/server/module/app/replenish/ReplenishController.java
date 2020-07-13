package com.server.module.app.replenish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import com.server.module.app.vminfo.WayDto1;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.server.module.app.login.AppLoginInfoService;
import com.server.module.app.login.LoginInfoBean;
import com.server.module.app.login.LoginInfoEnum;
import com.server.module.app.vminfo.AppVminfoService;
import com.server.module.app.vminfo.VminfoDto;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/home")
public class ReplenishController {
	private static Logger log = LogManager.getLogger(ReplenishController.class);

	@Autowired
	private AppLoginInfoService loginInfoService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AppVminfoService vminfoService;
	@Autowired
	private ReplenishService relenishService;

	/**
	 * 补货管理
	 * @author hebiting
	 * @date 2018年6月4日下午4:23:20
	 * @return
	 */
	@GetMapping("/replenish")
	public ReturnDataUtil replenish(ReplenishForm replenishForm,HttpServletRequest request){
		if(replenishForm == null){
			replenishForm = new ReplenishForm();
		}
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID)==null?null:Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean loginUser = loginInfoService.queryById(id);
		Integer companyId = loginUser.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyId(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		replenishForm.setCompanyIds(companyIds);
		if(!LoginInfoEnum.PRINCIPAL.getMessage().equals(loginUser.getIsPrincipal())){
			replenishForm.setDutyId(loginUser.getId());
		}
		replenishForm.setLineId(loginUser.getLineId());
		replenishForm.setAreaId(loginUser.getAreaId());
		replenishForm.setLevel(loginUser.getLevel());
		if(companyService.checkIsReplenishCompany(companyId)) {
			replenishForm.setType(1);
		}
		List<VminfoDto> replenishVMList = vminfoService.queryReplenishVMList(replenishForm);

		List<String> listCode= Lists.newArrayList();
		for (int i = 0; i < replenishVMList.size(); i++) {
			VminfoDto dto=replenishVMList.get(i);
			String code=dto.getVmCode();
			listCode.add(code);
		}

		List<WayDto1> listWay=vminfoService.queryWayItem(listCode,replenishForm.getVersion());

		for (int i = 0; i < replenishVMList.size(); i++) {
			VminfoDto dto=replenishVMList.get(i);
			for (int i1 = 0; i1 < listWay.size(); i1++) {
				WayDto1 way=listWay.get(i1);
				if(dto.getVmCode().equals(way.getVmCode())){
					if(way.getSimpleName()!=null) {
						way.setName(way.getSimpleName());
					}else{
						if(way.getItemName()!=null && way.getItemName().length()>2)
						   way.setName(way.getItemName().substring(0,2));
						else
							way.setName(way.getItemName());

					}

					dto.getWayList().add(way);
				}
			}
		}
		StringBuilder inCodes=new StringBuilder("");
		for (int i = 0; i < listCode.size(); i++) {
			inCodes.append("'"+listCode.get(i)+"'");
			if(i!=listCode.size()-1){
				inCodes.append(",");
			}
		}
		List<ReplenishDto> replenishList = relenishService.queryReplenish(inCodes.toString(),replenishForm.getVersion());
		Map<String,Object> returnData = new HashMap<String,Object>();
		returnData.put("replenishList", replenishList);
		returnData.put("replenishVMList", replenishVMList);
		return ResultUtil.success(returnData);
	}


}
