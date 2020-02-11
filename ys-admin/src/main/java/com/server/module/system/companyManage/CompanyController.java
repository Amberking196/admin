package com.server.module.system.companyManage;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lly835.bestpay.rest.type.Get;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.server.common.paramconfig.AlipayConfig;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.aliPayConfig.AliPayConfig;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.module.system.warehouseManage.warehouseAdmin.WarehouseAdminCondition;
import com.server.util.JsonUtils;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CompanyEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/company")
@Api(value = "companyController", description = "公司信息操作")
public class CompanyController {
 
	public static Logger log = LogManager.getLogger(CompanyController.class); 	    
	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private AlipayConfig alipayConfig;
	/**
	 * 添加公司信息
	 * @param company
	 * @return
	 */
	@ApiOperation(value = "添加公司信息", notes = "添加公司信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/createCompany")
	public ReturnDataUtil createCompany(@RequestBody(required=false)CompanyBean company,HttpServletRequest requeset){

		log.info("<CompanyController>--<createCompany>--start"); 
		if(company!=null){
			if(company.getParentId()==null){
//				Long userId = (Long)requeset.getAttribute(AdminConstant.LOGIN_USER_ID);
//				AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
				company.setParentId(1);
			}
			Integer id = companyService.createCompany(company);
			if(id!=null){
				Integer parentId = company.getParentId();
				MerchantInfo wxpayConfig = companyService.getPayConfig(parentId);
				if(wxpayConfig == null ){
					wxpayConfig = companyService.getPayConfig(CompanyEnum.YOUSHUIDAOJIA.getIndex());
				}
				wxpayConfig.setCompanyId(id);
				companyService.insertPayConfig(wxpayConfig);
				//增加微信支付参数配置
				PayConfigClient.modifyConfig(wxpayConfig);
				
				AliPayConfig aliConfig = companyService.getAliPayConfig(parentId);
				if(aliConfig == null){
					aliConfig = companyService.getAliPayConfig(CompanyEnum.YOUSHUIDAOJIA.getIndex());
				}
				aliConfig.setCompanyId(id);
				companyService.insertAliPayConfig(aliConfig);
				//在sms里新增支付宝支付参数配置
				PayConfigClient.modifyAliConfig(aliConfig);
				
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("创建成功");
			}else{
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("创建失败");
			}
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("创建失败");
		}
		log.info("<CompanyController>--<createCompany>--end"); 
		return returnDataUtil;
	}
//	/**
//	 * 查询所有公司信息
//	 * @author hebiting
//	 * @date 2018年4月10日上午10:07:54
//	 * @return
//	 */
//	@PostMapping("/findAllCompany")
//	public ReturnDataUtil findAllCompany(){
//		ReturnDataUtil returnData = new ReturnDataUtil();
//		List<CompanyBean> companyList = companyService.findAllCompany();
//		if(companyList!=null && companyList.size()>0){
//			returnData.setStatus(1);
//			returnData.setMessage("查询成功");
//			returnData.setReturnObject(companyList);
//		}else{
//			returnData.setStatus(0);
//			returnData.setMessage("查询无数据");
//		}
//		return returnData;
//	}
	/**
	 * 查询当前登录人的公司信息
	 * @author hebiting
	 * @date 2018年4月11日上午8:52:43
	 * @return
	 */
	@ApiOperation(value = "查询当前登录人的公司信息", notes = "查询当前登录人的公司信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findLoginCompany")
	public ReturnDataUtil findLoginCompany(HttpServletRequest reqeust){
		log.info("<CompanyController>--<findLoginCompany>--start"); 
		Long userId = (Long)reqeust.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
	 
		if(user!=null){
			CompanyBean company = companyService.findCompanyById(user.getCompanyId());
			if(company!=null){
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("查询成功");
				returnDataUtil.setReturnObject(company);
			}else{
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("查询无数据");
			}
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		log.info("<CompanyController>--<findLoginCompany>--end"); 
		return returnDataUtil;
	}
	/**
	 * 查询当前登录人的公司及其子公司信息
	 * @author hebiting
	 * @date 2018年4月13日上午8:59:21
	 * @return
	 */
	@ApiOperation(value = " 查询当前登录人的公司及其子公司信息", notes = " 查询当前登录人的公司及其子公司信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findAllSonCompany")
	public ReturnDataUtil findAllSonCompany(HttpServletRequest request){
		log.info("<CompanyController>--<findAllSonCompany>--start");  
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
		if(companyList!=null && companyList.size()>0){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
			returnDataUtil.setReturnObject(companyList);
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		log.info("<CompanyController>--<findAllSonCompany>--end"); 
		return returnDataUtil;
	}
	/**
	 * 根据条件查询公司信息
	 * @author hebiting
	 * @date 2018年4月13日上午9:01:00
	 * @param companyForm
	 * @return
	 */
	@ApiOperation(value = " 根据条件查询公司信息", notes = " 根据条件查询公司信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findCompanyByForm")
	public ReturnDataUtil findCompanyByForm(@RequestBody(required = false) CompanyForm companyForm,HttpServletRequest request){

		log.info("<CompanyController>--<findCompanyByForm>--start"); 
		if(companyForm==null){
			companyForm = new CompanyForm();
		}
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyIdWithoutState(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		companyForm.setCompanyIds(companyIds);
		returnDataUtil=companyService.findCompanyByForm(companyForm);
		log.info("<CompanyController>--<findCompanyByForm>--end"); 
		return 	returnDataUtil;

	}
	
	@ApiOperation(value = " 查询当前用户的公司", notes = " 查询当前用户的公司", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/getCompanys")
	public List<CompanyVo> getCompanys(){
		log.info("<CompanyController>--<getCompanys>--start"); 
		Integer companyId = UserUtils.getUser().getCompanyId(); 
		List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
		List<CompanyVo> list=Lists.newArrayList();
		if(companyList!=null && companyList.size()>0){
			for (CompanyBean bean : companyList) { 
				CompanyVo  vo=new CompanyVo();
				vo.setId(bean.getId());
				vo.setName(bean.getName());
				list.add(vo);
			}
		}
		log.info("<CompanyController>--<getCompanys>--end"); 
		return list;
	
	}
	
	
	@ApiOperation(value = " 更新公司信息", notes = " 更新公司信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/update")
	public ReturnDataUtil update(@RequestBody CompanyBean company){
		log.info("<CompanyController>--<update>--start");  
		if(company.getId().intValue()!=1 && company.getParentId()==null) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("父公司更新失败");
			return returnDataUtil;
		}
		if(company!=null && companyService.updateCompany(company)){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("更新成功");
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("更新失败");
		}
		log.info("<CompanyController>--<update>--end"); 
		return returnDataUtil;
	}
	
	@PostMapping("/isOnlyOne")
	public ReturnDataUtil isOnlyOne(@RequestBody Map<String,String> param){
		String companyName = param.get("companyName");
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(companyService.isOnlyOne(companyName)){
			returnData.setStatus(1);
			returnData.setMessage("未被占用");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("已被占用,请更换");
		}
		return returnData;
	}
	
	@ApiOperation(value = " 查询所有公司信息", notes = " 查询所有公司信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/findAllCompany")
	public ReturnDataUtil findAllCompany(CompanyForm companyForm,HttpServletRequest request){
		if(companyForm==null)
		{
			companyForm = new CompanyForm();
		}
		companyForm.setIsShowAll(1);
		log.info("<CompanyController>--<findAllCompany>--start"); 
		returnDataUtil=companyService.findCompanyByForm(companyForm);
		log.info("<CompanyController>--<findAllCompany>--end"); 
		return 	returnDataUtil;

	}
		
	@ApiOperation(value = " 新增查询公司信息", notes = " 新增查询公司信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/addQueryCompany")
	public ReturnDataUtil addQueryCompany(CompanyForm companyForm,HttpServletRequest request){
		log.info("<CompanyController>--<addQueryCompany>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(companyForm==null){
			companyForm = new CompanyForm();
		}
		companyForm.setIsShowAll(1);
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();		
		companyForm.setCompanyId(companyId);
		//根据公司id 返回全部公司或此公司
		returnData=companyService.findSecondCompanyByForm(companyForm);
		log.info("<CompanyController>--<addQueryCompany>--end"); 
		return returnData;
	}
	

	@ApiOperation(value = " 编辑查询公司信息", notes = " 编辑查询公司信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/editQueryCompany")
	public ReturnDataUtil editQueryCompany(CompanyForm companyForm,HttpServletRequest request){
		log.info("<CompanyController>--<editQueryCompany>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(companyForm==null){ 
			companyForm = new CompanyForm();
		}
		companyForm.setIsShowAll(1);
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		companyForm.setThisCompanyId(companyForm.getThisCompanyId());
		if(companyId.intValue()==1) {
			//总部 除此公司外的所有公司
			returnData=companyService.findLimitSecondCompanyByForm(companyForm);
		}
		else {
			//根据公司id找父公司
			returnData=companyService.findFatherCompanyByForm(companyForm);
		}
		log.info("<CompanyController>--<editQueryCompany>--end"); 
		return returnData;
	}
	
	
	@ApiOperation(value = "logo图片上传", notes = "logo图片上传")
	@PostMapping(value = "/logoUpdate")
	public String logoUpdate(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<CompanyController>--<logoUpdate>--start");   
		String filePath = file.getOriginalFilename(); // 获取文件的名称 
		String imgName = companyService.findImgName(filePath);
		//filePath = "/home/ysNew/ys-admin/itemImg/" + imgName; // 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getCompanyLogo()+ imgName; // 这是文件的保存路径，如果不设置就会保存到项目的根目录
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			return  imgName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			log.info("<CompanyController>--<logoUpdate>--end");  
		}
	}
	
	@ApiOperation(value = " 查询所有子公司和父公司名称", notes = " 查询所有子公司和父公司名称", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findAllSonCompanyAndFatherName")
	public ReturnDataUtil findAllSonCompanyAndFatherName(HttpServletRequest request){
		log.info("<CompanyController>--<findAllSonCompanyAndFatherName>--start");  
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompanyAndFatherName(companyId);
		if(companyList!=null && companyList.size()>0){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
			returnDataUtil.setReturnObject(companyList);
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		log.info("<CompanyController>--<findAllSonCompanyAndFatherName>--end"); 
		return returnDataUtil;
	}
	
	/**
	 * 查询该公司微信支付参数配置
	 * @author hebiting
	 * @date 2018年8月4日上午10:06:39
	 * @param companyId
	 * @return
	 */
	@PostMapping("/payConfig")
	public ReturnDataUtil getPayConfig(Integer companyId){
		log.info("<CompanyController>--<getPayConfig>--start"); 
		MerchantInfo payConfig = companyService.getPayConfig(companyId);
		log.info("<CompanyController>--<getPayConfig>--end"); 
		if(payConfig != null){
			return ResultUtil.success(payConfig);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 查询该公司支付宝支付参数配置
	 * @author hebiting
	 * @date 2018年8月4日上午10:06:39
	 * @param companyId
	 * @return
	 */
	@PostMapping("/getAliPayConfig")
	public ReturnDataUtil getAliPayConfig(Integer companyId){
		log.info("<CompanyController>--<getAliPayConfig>--start"); 
		AliPayConfig aliPayConfig = companyService.getAliPayConfig(companyId);
		log.info("<CompanyController>--<getAliPayConfig>--end"); 
		if(aliPayConfig != null){
			return ResultUtil.success(aliPayConfig);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 更新微信支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午10:32:35
	 * @param merchant
	 * @return
	 */
	@PostMapping("/updatePayConfig")
	public ReturnDataUtil updateWxPayConfig(@RequestBody MerchantInfo merchant,HttpServletRequest request){
		log.info("<CompanyController>--<updateWxPayConfig>--start"); 
		boolean updatePayConfig = false;
		Boolean modifyConfig = null;
		if(merchant!=null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			merchant.setUpdateUser(userId);
			merchant.setType(0);
			if(merchant.getCompanyId() != null && merchant.getCompanyId().equals(merchant.getUsingCompanyConfig())){
				updatePayConfig = companyService.updatePayConfig(merchant);
			}else if(merchant.getUsingCompanyConfig() != null && merchant.getCompanyId() != null){
				MerchantInfo otherConfig = companyService.getPayConfig(merchant.getUsingCompanyConfig());
				otherConfig.setCompanyId(merchant.getCompanyId());
				updatePayConfig = companyService.updatePayConfig(otherConfig);
			}else{
				return ResultUtil.error(0,"更新失败",null);
			}
			modifyConfig = PayConfigClient.modifyConfig(merchant);
		}
		log.info("<CompanyController>--<updateWxPayConfig>--end"); 
		if(updatePayConfig && (Boolean.valueOf(true)).equals(modifyConfig)){
			return ResultUtil.success();
		}
		return ResultUtil.error(0,"更新失败",null);
	}

	
	/**
	 * 更新阿里支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午10:32:35
	 * @param merchant
	 * @return
	 */
	@PostMapping("/updateAliPayConfig")
	public ReturnDataUtil updateAliPayConfig(@RequestBody AliPayConfig alipayConfig){
		log.info("<CompanyController>--<updateAliPayConfig>--start"); 
		boolean updatePayConfig = false;
		Boolean modifyAliConfig = null;
		if(alipayConfig!=null){
			updatePayConfig = companyService.updateAliPayConfig(alipayConfig);
			modifyAliConfig = PayConfigClient.modifyAliConfig(alipayConfig);
		}
		log.info("<CompanyController>--<updateAliPayConfig>--end"); 
		if(updatePayConfig && (Boolean.valueOf(true)).equals(modifyAliConfig)){
			return ResultUtil.success();
		}
		return ResultUtil.error(0,"更新失败",null);
	}
	
	@ApiOperation(value = "第三方补水公司列表", notes = "/listReplenishWaterCompany", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listReplenishWaterCompany", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listReplenishWaterCompany(HttpServletRequest request) {
		
		CompanyForm	companyForm = new CompanyForm();
		companyForm.setOtherType(3);
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyIdWithoutState(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		companyForm.setCompanyIds(companyIds);
		returnDataUtil=companyService.findCompanyByForm(companyForm);
		log.info("<CompanyController>--<findCompanyByForm>--end"); 
		return 	returnDataUtil;
	}
	@ApiOperation(value = "默认公司的列表", notes = "/listReplenishWaterCompany", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPageByNomal", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPageByNomal(HttpServletRequest request) {
		
		CompanyForm	companyForm = new CompanyForm();
		companyForm.setOtherType(1);
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyIdWithoutState(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		companyForm.setCompanyIds(companyIds);
		returnDataUtil=companyService.findCompanyByForm(companyForm);
		log.info("<CompanyController>--<findCompanyByForm>--end"); 
		return 	returnDataUtil;
	}

	
}
