package com.server.module.customer.complain.replyCommonLanguage;

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
 * author name: why create time: 2019-01-04 09:50:27
 */
@Api(value = "ComplainReplyCommonLanguageController", description = "故障回复常用语")
@RestController
@RequestMapping("/complainReplyCommonLanguage")
public class ComplainReplyCommonLanguageController {

	private static Logger log=LogManager.getLogger(ComplainReplyCommonLanguageController.class);
	@Autowired
	private ComplainReplyCommonLanguageService complainReplyCommonLanguageServiceImpl;

	@ApiOperation(value = "故障回复常用语列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) ComplainReplyCommonLanguageForm form) {
		if(form==null) {
			form=new ComplainReplyCommonLanguageForm();
		}
		return complainReplyCommonLanguageServiceImpl.listPage(form);
	}

	@ApiOperation(value = "故障回复常用语添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ComplainReplyCommonLanguageBean entity) {
		log.info("<ComplainReplyCommonLanguageController>----<add>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setCreateUser(UserUtils.getUser().getId());
		ComplainReplyCommonLanguageBean bean = complainReplyCommonLanguageServiceImpl.add(entity);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("常用语添加成功！");
			returnDataUtil.setReturnObject(bean);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("常用语添加失败！");
			returnDataUtil.setReturnObject(bean);
		}
		log.info("<ComplainReplyCommonLanguageController>----<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "故障回复常用语修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ComplainReplyCommonLanguageBean entity) {
		log.info("<ComplainReplyCommonLanguageController>----<update>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setUpdateUser(UserUtils.getUser().getId());
		boolean flag = complainReplyCommonLanguageServiceImpl.update(entity);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("常用语编辑成功！");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("常用语编辑失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<ComplainReplyCommonLanguageController>----<update>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "故障回复常用语删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<ComplainReplyCommonLanguageController>----<del>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = complainReplyCommonLanguageServiceImpl.del(id);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("常用语删除成功！");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("常用语删除失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<ComplainReplyCommonLanguageController>----<del>-----end");
		return returnDataUtil;
	}
}
