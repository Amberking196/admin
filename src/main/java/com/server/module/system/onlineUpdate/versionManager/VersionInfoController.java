package com.server.module.system.onlineUpdate.versionManager;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.log.Log;
import com.server.module.app.home.ResultEnum;
import com.server.module.system.adminUser.AdminConstant;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/version")
public class VersionInfoController {

	@Autowired
	private VersionInfoService versionInfoService;
	
	/**
	 * 机器主控程序版本信息插入
	 * @author hebiting
	 * @date 2019年1月21日下午1:47:58
	 * @param version
	 * @param request
	 * @return
	 */
	@PostMapping("/insert")
	public ReturnDataUtil insert(@RequestBody VersionInfoBean version,HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		if(userId == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		Date now = new Date();
		version.setCreateUser(userId);
		version.setUpdateUser(userId);
		version.setCreateTime(now);
		version.setUpdateTime(now);
		return versionInfoService.insert(version);
	}
	/**
	 * 机器主控程序版本信息更新
	 * @author hebiting
	 * @date 2019年1月21日下午1:51:11
	 * @param version
	 * @param request
	 * @return
	 */
	@PostMapping("/update")
	public ReturnDataUtil update(@RequestBody VersionInfoBean version,HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		if(userId == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		Date now = new Date();
		version.setUpdateUser(userId);
		version.setUpdateTime(now);
		return versionInfoService.update(version);
	}
	/**
	 * 查询可升级版本信息
	 * @author hebiting
	 * @date 2019年1月21日下午1:59:42
	 * @param versionInfo
	 * @return
	 */
	@PostMapping("/getCanUpdate")
	public ReturnDataUtil getCanUpdateVersion(VersionInfoForm v){
		 System.out.print(v.getType());
		 System.out.print(v.getVersionId());
		 List<VersionInfoBean> canUpdateVersion = versionInfoService.getCanUpdateVersion(v);
		 return ResultUtil.success(canUpdateVersion);
	}
	

	@PostMapping("/getByForm")
	public ReturnDataUtil getVersion(@RequestBody(required=false) VersionInfoForm form){
		if(form == null){
			form = new VersionInfoForm();
		}
		return versionInfoService.getByForm(form);
	}
}
