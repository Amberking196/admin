package com.server.module.system.synthesizeManage.roleManage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.module.customer.order.OrderBean;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.module.system.adminMenu.AdminMenuService;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.adminUser.CompanyRoleDto;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.operationLog.JurisdictionLogBean;
import com.server.module.system.logsManager.operationLog.JurisdictionLogService;
import com.server.module.system.logsManager.operationLog.OperationLogService;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/roleManage")
@Api(value = "roleManageController", description = "角色管理操作")
public class RoleManageController {

	@Autowired
	RoleManageService roleManageService;
	@Autowired
	AdminMenuService adminMenuService;
	@Autowired
	CompanyService companyService;

	@Autowired
	AdminUserService adminUserService;

	@Autowired
	private JurisdictionLogService jurisdictionLogServiceImpl;
	@Autowired
	private AdminUserService AdminUserServiceImpl;
	
	public static Logger log = LogManager.getLogger(RoleManageController.class);
	
	@ApiOperation(value = "添加角色", notes = "添加角色", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/addRole")
	public ReturnDataUtil addRole(@RequestBody(required = false) RoleBean role) {
		if(roleManageService.isOnlyOne(role.getCompanyId(), role.getName())){
			return roleManageService.addRole(role);
		}else{
			ReturnDataUtil returnData = new ReturnDataUtil();
			returnData.setStatus(0);
			returnData.setMessage("角色名被占用，请更换");
			return returnData;
		}
	}

	@ApiOperation(value = "更新角色信息", notes = "更新角色信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/updateRole")
	public ReturnDataUtil updateRole(@RequestBody(required = false) RoleBean role, HttpServletRequest request) {
		ReturnDataUtil updateRole = roleManageService.updateRole(role);
		if (updateRole.getStatus() == 1) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = AdminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			JurisdictionLogBean logBean = new JurisdictionLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setContent("修改角色信息");
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加权限操作日志
			jurisdictionLogServiceImpl.insert(logBean);
		}
		return updateRole;
	}

	@ApiOperation(value = "删除角色信息", notes = "删除角色信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/deleteRole")
	public ReturnDataUtil deleteRole(@RequestBody RoleBean role) {
		ReturnDataUtil returnData = new ReturnDataUtil();
    	returnData = roleManageService.deleteRole(role.getId());
    	if(returnData.getStatus()==1) {
        	List<Integer> list=roleManageService.findAdminUserListByRoleId(role);
            String reduce = StringUtils.join(list.toArray(), ",");
        	if(roleManageService.reduceAdminUserList(reduce,role)) {
        		returnData.setMessage("相关用户角色已重置");
        	}
    	}
		return returnData;
	}

	@ApiOperation(value = "根据公司id查询所有角色", notes = "根据公司id查询所有角色", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findRoleByCompany")
	public ReturnDataUtil findRoleByCompany(@RequestBody(required = false) Map<String, Integer> param) {
		return roleManageService.findRoleByCompany(param.get("companyId"));
	}

	@ApiOperation(value = "查询所有菜单", notes = "查询所有菜单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findAllMenu")
	public ReturnDataUtil findAllMenu() {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<AdminMenuBean> menuList = adminMenuService.formatGetMenuList();
		if (menuList != null && menuList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(menuList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
		}
		return returnData;
	}

	@ApiOperation(value = "查询用户拥有菜单", notes = "查询用户拥有菜单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findMenuByUser")
	public ReturnDataUtil findMenuByUser(HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		String role = user != null ? user.getRole() : null;
		List<AdminMenuBean> menuSet = adminMenuService.formatFindMenuByRole(role);
		if (menuSet != null && menuSet.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(menuSet);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}

	@ApiOperation(value = "根据登录用户公司查询所有角色", notes = "根据登录用户公司查询所有角色", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@PostMapping("/findRoleByUser")
	@RequestMapping("/findRoleByUser")
	public ReturnDataUtil findRoleByUser(@RequestBody(required=false) RoleForm roleForm,HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(roleForm==null){
			roleForm = new RoleForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user == null ? null : user.getCompanyId();
		List<Integer> companyIdList = companyService.findAllSonCompanyId(companyId);
		if(roleForm.getCompanyId()!=null){
			if(companyIdList.contains(roleForm.getCompanyId())){
				companyIdList = new ArrayList<Integer>();
				companyIdList.add(roleForm.getCompanyId());
			}else{
				companyIdList = null;
			}
		}
		if (companyIdList != null && companyIdList.size() > 0) {
			List<RoleBean> allRoleList = new ArrayList<RoleBean>();
			for (Integer id : companyIdList) {
				List<RoleBean> roleList = (List<RoleBean>) roleManageService.findRoleByCompany(id)
						.getReturnObject();
				if (roleList != null && roleList.size() > 0) {
					allRoleList.addAll(roleList);
				}
			}
			if (allRoleList != null && allRoleList.size() > 0) {
				returnData.setStatus(1);
				returnData.setMessage("查询成功");
				returnData.setReturnObject(allRoleList);
			} else {
				returnData.setStatus(0);
				returnData.setMessage("查询无数据");
				returnData.setReturnObject(null);
			}
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
			returnData.setReturnObject(null);
		}
		return returnData;
	}
	@ApiOperation(value = "根据登录条件分页查询", notes = "根据登录条件分页查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/listPage")
	public ReturnDataUtil listPage(@RequestBody(required = false) RoleForm roleForm,HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(roleForm==null){
			roleForm = new RoleForm();
		}
		Integer companyId=null;
		if(roleForm.getCompanyId()!=null) {
			companyId=roleForm.getCompanyId();
		}else {
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			companyId = user == null ? null : user.getCompanyId();
		}
		
		List<Integer> companyIdList = companyService.findAllSonCompanyId(companyId);
		if(roleForm.getCompanyId()!=null){
			if(companyIdList.contains(roleForm.getCompanyId())){
				companyIdList = new ArrayList<Integer>();
				companyIdList.add(roleForm.getCompanyId());
			}else{
				companyIdList = null;
			}
		}
		long tatol=0L;
		if (companyIdList != null && companyIdList.size() > 0) {
			StringBuffer sb=new StringBuffer();
			//List<RoleBean> allRoleList = new ArrayList<RoleBean>();
			for (Integer id : companyIdList) {
				sb.append(id+",");
				
			}
			String substring = sb.substring(0, sb.length()-1);
			
			roleForm.setCompanyIds(substring);
			tatol = roleManageService.findRoleByFormNum(roleForm);
			returnData=roleManageService.listPage(roleForm);
			List<RoleBean> roleList =  (List<RoleBean>) returnData.getReturnObject();
			if (roleList != null && roleList.size() > 0) {
				returnData.setStatus(1);
				returnData.setMessage("查询成功");
				returnData.setTotal(tatol);
				returnData.setCurrentPage(roleForm.getCurrentPage());
				returnData.setPageSize(roleForm.getPageSize());
			} else {
				returnData.setStatus(0);
				returnData.setMessage("查询无数据");
				returnData.setReturnObject(null);
			}
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
			returnData.setReturnObject(null);
		}
		return returnData;
	}
	
	
	@ApiOperation(value = "初始化角色", notes = "初始化角色", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initRole")
	public ReturnDataUtil initRole(@RequestBody(required = false) RoleBean role,HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(role==null) {
			role=new RoleBean();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		String roleId=user.getRole();
		role.setId(Integer.valueOf(roleId));
		
		List<RoleBean> list=roleManageService.findSonRole(Integer.valueOf(roleId));
		returnData.setReturnObject(list);
		return returnData;
	}
	
    @ApiOperation(value = "初始化新增信息", notes = "初始化新增信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/initAddInfo")
    public ReturnDataUtil initAddInfo(HttpServletRequest requeset) {
        ReturnDataUtil returnData = new ReturnDataUtil();
        Long userId = (Long) requeset.getAttribute(AdminConstant.LOGIN_USER_ID);
        AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
        Integer companyId = user == null ? null : user.getCompanyId();
        List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
        if (companyList != null && companyList.size() > 0) {
            List<CompanyRoleDto> crList = new ArrayList<CompanyRoleDto>();
            CompanyRoleDto crDto = null;
            for (CompanyBean companyBean : companyList) {
                crDto = new CompanyRoleDto();
                crDto.setComapanyValue(companyBean);
                crDto.setCompanyList(companyService.findAllSonCompany(companyBean.getId()));
                crDto.setRoleList((List<RoleBean>) roleManageService.findRoleByCompany(companyBean.getId()).getReturnObject());
                crList.add(crDto);
            }
            returnData.setStatus(1);
            returnData.setMessage("初始化成功");
            returnData.setReturnObject(crList);
        } else {
            returnData.setStatus(0);
            returnData.setMessage("初始化失败");
        }
        return returnData;
    }
    
    /**
	 * 查询当前登录人的公司信息
	 * @author hebiting
	 * @date 2018年4月11日上午8:52:43
	 * @return
	 */
	@ApiOperation(value = "查询当前登录人的角色信息", notes = "查询当前登录人的角色信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findLoginRole")
	public ReturnDataUtil findLoginRole(HttpServletRequest reqeust){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		Long userId = (Long)reqeust.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		if(user!=null){
			RoleBean role=roleManageService.findRoleById(Integer.parseInt(user.getRole()));
			if(role!=null){
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("查询成功");
				returnDataUtil.setReturnObject(role);
			}else{
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("查询无数据");
			}
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		return returnDataUtil;
	}
	
	@ApiOperation(value = "根据角色查询所有登录用户", notes = "根据角色查询所有登录用户", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequestMapping("/findUserByRole")
	public ReturnDataUtil findUserByRole(@RequestBody(required=false) RoleBean role,HttpServletRequest request) {
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		List<AdminUserBean> adminUserBeanList = roleManageService.findUserByRole(role);
		role.setId(null);
		List<AdminUserBean> adminUserBeanAllList = roleManageService.findUserByRole(role);
		
		List<AdminUserBean> adminUserNotBindList=new ArrayList<>(adminUserBeanAllList);

		log.info(adminUserNotBindList.size());
		Iterator<AdminUserBean> it= adminUserNotBindList.iterator();
		
		while(it.hasNext()){
			AdminUserBean adminUserBeanAll = it.next();
			for(AdminUserBean a:adminUserBeanList) {
				if(a.getId().equals(adminUserBeanAll.getId())) {
					it.remove();
				}
			}
		}
		
        //adminUserNotBindList = adminUserNotBindList.stream().filter(item -> !adminUserBeanList.contains(item)).collect(Collectors.toList());
		log.info(adminUserNotBindList.size());

		AdminUserListDto dto= new AdminUserListDto();
		dto.setAdminUserAllList(adminUserBeanAllList);
		dto.setAdminUserList(adminUserBeanList);
		dto.setAdminUserNotBindList(adminUserNotBindList);
		
		if(adminUserBeanAllList != null && adminUserBeanAllList.size()>0){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
			returnDataUtil.setReturnObject(dto);
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		return returnDataUtil;
	}
	

	@ApiOperation(value = "查看角色信息修改保存", notes = "查看角色信息修改保存", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/updateAdminUser")
	public ReturnDataUtil updateAdminUser(@RequestBody(required = false) RoleBean role,HttpServletRequest reqeust){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if(role==null) {
			 role=new RoleBean();
		}
		//查询该角色的用户
		List<Integer> oldAdminUserList = roleManageService.findAdminUserListByRoleId(role);
		List<Integer> newAdminUserList = role.getAdminUserList();
		Boolean addFlag = true;
		Boolean reduceFlag = true;

        List<Integer> reduceList = oldAdminUserList.stream().filter(item -> !newAdminUserList.contains(item)).collect(Collectors.toList());
        List<Integer> addList = newAdminUserList.stream().filter(item -> !oldAdminUserList.contains(item)).collect(Collectors.toList());
        String add = StringUtils.join(addList.toArray(), ",");
        String reduce = StringUtils.join(reduceList.toArray(), ",");
        
        if(StringUtils.isNotBlank(add)) {
        	addFlag = roleManageService.addAdminUserList(add,role);
        }
        if(StringUtils.isNotBlank(reduce)) {
        	reduceFlag = roleManageService.reduceAdminUserList(reduce,role);
        }
		if(addFlag && reduceFlag){
			returnDataUtil.setMessage("修改成功");
			returnDataUtil.setReturnObject(addFlag);
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		return returnDataUtil;
	}
}
