package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "vendingAreaController", description = "区域信息操作")
@RequestMapping("/vendingArea")
public class VendingAreaController {

	public static Logger log=LogManager.getFormatterLogger(VendingAreaController.class);
	
	@Autowired
	VendingAreaService vendingAreaService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	CompanyService companyService;
	

	/**
	 * 添加区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:50:52
	 * @param areaBean
	 * @return
	 */
	@ApiOperation(value = "添加区域信息", notes = "添加区域信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/add")
	public ReturnDataUtil addVendingArea(@RequestBody VendingAreaBean areaBean){
		if(areaBean !=null && areaBean.getPid() == null){
			areaBean.setPid(0);
		}
		return vendingAreaService.addVendingArea(areaBean);
	}
	/**
	 * 更新区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:51:23
	 * @param areaBean
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation(value = "更新区域信息", notes = "更新区域信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReturnDataUtil updateVendingArea(@RequestBody VendingAreaBean areaBean){
		return vendingAreaService.updateVendingArea(areaBean);
	}
	/**
	 * 更改区域是否启用？（1：启用，0：禁用）
	 * @author hebiting
	 * @date 2018年4月9日下午4:52:42
	 * @param areaId
	 * @param isUsing
	 * @return
	 */
	@PostMapping("/changeStatus")
	@ApiOperation(value = "更改区域是否启用？（1：启用，0：禁用）", notes = "更改区域是否启用？（1：启用，0：禁用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReturnDataUtil changeAreaStatus(Integer areaId,Integer isUsing){
		return vendingAreaService.changeAreaStatus(areaId, isUsing);
	}

	/**
	 * 根据pid查询子区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	@PostMapping("/findAreaByPid")
	@ApiOperation(value = "根据pid查询子区域信息", notes = "根据pid查询子区域信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReturnDataUtil findAreaByPid(Integer pid){
		return vendingAreaService.findAreaByPid(pid);
	}
	/**
	 * 根据pid深度查询所有子区域
	 * @author hebiting
	 * @date 2018年4月9日下午5:47:28
	 * @param pid
	 * @return
	 */
	@ApiOperation(value = "根据pid深度查询所有子区域", notes = "根据pid深度查询所有子区域", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/treeArea")
	public ReturnDataUtil treeArea(@RequestBody Map<String,Integer> param){
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingAreaDto> areaList = vendingAreaService.treeArea(param.get("pid"));
		if(areaList!=null && areaList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(areaList);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
		}
		return returnData;
	}
	/**
	 * 根据子区域id，查询子区域及所有父区域
	 * @author hebiting
	 * @date 2018年4月11日上午11:45:38
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据子区域id，查询子区域及所有父区域", notes = "根据子区域id，查询子区域及所有父区域", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findFaAreaAndSonArea")
	public ReturnDataUtil findFaAreaAndSonArea(Integer id){
		ReturnDataUtil returnData = new ReturnDataUtil();
		VendingAreaDto area = vendingAreaService.findFaAreaAndSonArea(id);
		if(area!=null){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(area);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
			returnData.setReturnObject(null);
		}
		return returnData;
	}
	/**
	 * 根据条件查询区域信息
	 * @author hebiting
	 * @date 2018年4月12日上午10:37:04
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据条件查询区域信息", notes = "根据条件查询区域信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findAreaByForm")
	public ReturnDataUtil findAreaByForm(@RequestBody(required=false) VendingAreaForm areaForm, HttpServletRequest request){
		if(areaForm==null){
			areaForm = new VendingAreaForm();
		}
		if(areaForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			areaForm.setCompanyIds(companyIds);
		}
		return vendingAreaService.findAreaByForm(areaForm);

	}
	
	
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年4月12日上午10:37:04
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "初始化", notes = "初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initInfo")
	public ReturnDataUtil initInfo(HttpServletRequest request){
		ReturnDataUtil returnData = new ReturnDataUtil();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("companyList", companyList);
		if(companyList!=null && companyList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(data);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}
	
	@PostMapping("/isOnlyOne")
	public ReturnDataUtil areaNameIsOnlyOne(@RequestBody Map<String,String> param,
			HttpServletRequest request){
		String areaName = param.get("areaName");
		ReturnDataUtil returnData = new ReturnDataUtil();
//		Integer companyId = Integer.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID).toString());
		if(vendingAreaService.areaNameIsOnlyOne(areaName)){
			returnData.setStatus(1);
			returnData.setMessage("成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("该区域名被占用");
		}
		return returnData;
	}
	
	@PostMapping("/delete")
	@ApiOperation(value = "删除区域信息", notes = "删除区域信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReturnDataUtil deleteVendingArea(@RequestBody List<Integer> id, HttpServletRequest request) {
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		log.info("<VendingAreaController>------<deleteVendingArea>-----start");
		String payIds = StringUtils.join(id, ",");
		boolean delete = vendingAreaService.delete(payIds);
		if (delete) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("区域删除成功!");
			log.info("<VendingAreaController>------<deleteVendingArea>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("区域删除失败!");
			log.info("<VendingAreaController>------<deleteVendingArea>-----end");
			return returnDataUtil;
		}
	}
}
