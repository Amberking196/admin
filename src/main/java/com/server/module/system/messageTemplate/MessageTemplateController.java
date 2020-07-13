package com.server.module.system.messageTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2019-01-07 11:21:04
 */
@Api(value = "MessageTemplateController", description = "短信模板")
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

	private static Logger log=LogManager.getLogger(MessageTemplateController.class);
	@Autowired
	private MessageTemplateService messageTemplateServiceImpl;

	@ApiOperation(value = "短信模板列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) MessageTemplateForm form) {
		log.info("<MessageTemplateController>------<listPage>-----start");
		if(form==null) {
			form=new MessageTemplateForm();
		}
		ReturnDataUtil returnDataUtil = messageTemplateServiceImpl.listPage(form);
		log.info("<MessageTemplateController>------<listPage>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "短信模板添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MessageTemplateBean entity) {
		log.info("<MessageTemplateController>------<add>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setCreateUser(UserUtils.getUser().getId());
		MessageTemplateBean bean = messageTemplateServiceImpl.add(entity);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("短信模板增加成功！");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("短信模板增加失败！");
		}
		log.info("<MessageTemplateController>------<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "短信模板修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody MessageTemplateBean entity) {
		log.info("<MessageTemplateController>------<update>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setUpdateUser(UserUtils.getUser().getId());
		boolean flag = messageTemplateServiceImpl.update(entity);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("短信模板修改成功！");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("短信模板修改失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<MessageTemplateController>------<update>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "短信模板删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<MessageTemplateController>------<del>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = messageTemplateServiceImpl.del(id);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("短信模板删除成功！");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("短信模板删除失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<MessageTemplateController>------<del>-----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "短信模板审核", notes = "audit", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/audit", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil audit(@RequestBody MessageTemplateBean entity) {
		log.info("<MessageTemplateController>------<audit>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setUpdateUser(UserUtils.getUser().getId());
		boolean flag = messageTemplateServiceImpl.update(entity);
		if(flag) {
			returnDataUtil.setStatus(1);
			if(entity.getState()==0) {
				returnDataUtil.setMessage("短信模板审核未通过！");
			}else {
				returnDataUtil.setMessage("短信模板审核已通过！");
			}
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("短信模板审核失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<MessageTemplateController>------<audit>-----end");
		return returnDataUtil;
	}
}
