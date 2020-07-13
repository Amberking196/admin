package com.server.module.system.officialManage.officialMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.io.IOException;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ExcelUtil;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
/**
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
@Api(value ="OfficialMessageController",description="官网留言")
@RestController
@RequestMapping("/officialMessage")
public class  OfficialMessageController{

	public static Logger log = LogManager.getLogger(OfficialMessageController.class);
	@Autowired
	private OfficialMessageService officialMessageServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "官网留言列表",notes = "listPage",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) OfficialMessageForm officialMessageForm){
		log.info("<OfficialMessageController>--<listPage>--start");
		if(officialMessageForm==null) {
			officialMessageForm=new OfficialMessageForm();
		}
		returnDataUtil=officialMessageServiceImpl.listPage(officialMessageForm);
		log.info("<OfficialMessageController>--<listPage>--end");
		return returnDataUtil;
	}

	@ApiOperation(value = "官网留言添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(@RequestBody OfficialMessageBean entity){
		entity.setCreateUser(1l);
		if(officialMessageServiceImpl.add(entity)!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("提交成功");
		}
		else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("提交失败");
		}
		return returnDataUtil;
	}
	
	@ApiOperation(value = "官网留言修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody OfficialMessageBean entity){
		entity.setUpdateUser(UserUtils.getUser().getId());
		return new ReturnDataUtil(officialMessageServiceImpl.update(entity));
	}

	@ApiOperation(value = "官网留言删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody OfficialMessageBean entity){
		entity.setDeleteFlag(1);
		entity.setUpdateUser(UserUtils.getUser().getId());
		return new ReturnDataUtil(officialMessageServiceImpl.update(entity));
	}
}

