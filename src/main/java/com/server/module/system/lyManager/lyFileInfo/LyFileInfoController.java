package com.server.module.system.lyManager.lyFileInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * author name: why
 * create time: 2018-04-04 10:09:14
 */
@Api(value = "LyFileInfoController", description = "文件管理列表")
@RestController
@RequestMapping("/LyFileInfo")
public class LyFileInfoController {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class);
	@SuppressWarnings("unused")
	@Autowired
	private  LyFileInfoService LyFileInfoServiceImpl;
	
	
	@ApiOperation(value = "文件管理列表", notes = "文件管理列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(LyFileInfoCondition condition) {
		return LyFileInfoServiceImpl.listPage(condition);		
	}
	
	@ApiOperation(value = "编辑文件", notes = "编辑文件", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(LyFileInfoBean entity){
		return new ReturnDataUtil( LyFileInfoServiceImpl.updateEntity(entity));

	}
	@ApiOperation(value = "删除文件", notes = "删除文件", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(String  name){
		LyFileInfoBean entity=new LyFileInfoBean();
		entity.setName(name);
		return new ReturnDataUtil(LyFileInfoServiceImpl.del(entity));
	}
}
