package com.server.module.system.adminMenu;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.filter.WebFilter;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.util.ReturnDataUtil;

@RestController
public class AdminMenuController {
	
	public static Logger log = LogManager.getLogger(AdminMenuController.class); 	    
	@Autowired
	AdminMenuService adminMenuService;
	@Autowired
	AdminUserService adminUserService;
	/**
	 * 查询当前登录人的菜单权限
	 * @param request
	 * @return
	 */
	@PostMapping("/findLoginUserMenu")
	public ReturnDataUtil findLoginUserMenu(HttpServletRequest request){

		log.info("AdminMenuController---------findLoginUserMenu------ start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		if(user!=null){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			String role = user.getRole();
			Set<AdminMenuBean> menuSet = adminMenuService.findMenuByRole(role);
			returnData.setReturnObject(menuSet);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
		}
		log.info("AdminMenuController---------findLoginUserMenu------ start"); 
		return returnData;
	}
}
