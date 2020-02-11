package com.server.module.system.forumManage.forumFilterManage;

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
 * 
 * @author why
 * @date: 2019年3月14日 上午11:28:34
 */
@Api(value = "ForumFilterController", description = "敏感字管理")
@RestController
@RequestMapping("/forumFilter")
public class ForumFilterController {

	private static Logger log = LogManager.getLogger(ForumFilterController.class);
	@Autowired
	private ForumFilterService forumFilterServiceImpl;

	@ApiOperation(value = "敏感字列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) ForumFilterForm forumFilterForm) {
		log.info("<ForumFilterController>-----<listPage>-----start");
		if (forumFilterForm == null) {
			forumFilterForm = new ForumFilterForm();
		}
		ReturnDataUtil returnDataUtil = forumFilterServiceImpl.listPage(forumFilterForm);
		log.info("<ForumFilterController>-----<listPage>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "敏感字滤添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ForumFilterBean entity) {
		log.info("<ForumFilterController>-------<add>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (forumFilterServiceImpl.isNounExists(entity.getNoun())) {
			entity.setCreateUser(UserUtils.getUser().getId());
			ForumFilterBean forumFilterBean = forumFilterServiceImpl.add(entity);
			if (forumFilterBean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("敏感字新增成功！");
				returnDataUtil.setReturnObject(forumFilterBean);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("敏感字新增失败！");
				returnDataUtil.setReturnObject(forumFilterBean);
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("敏感字已存在 ，请重新输入！");
		}
		log.info("<ForumFilterController>-------<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "敏感字修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ForumFilterBean entity) {
		log.info("<ForumFilterController>-------<update>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		boolean flag = false;
		if (forumFilterServiceImpl.get(entity.getId()).getNoun().equals(entity.getNoun())
				|| (!(forumFilterServiceImpl.get(entity.getId()).getNoun().equals(entity.getNoun()))
						&& forumFilterServiceImpl.isNounExists(entity.getNoun()))) {
			flag=true;
		}
		if(flag) {
			entity.setUpdateUser(UserUtils.getUser().getId());
			boolean update = forumFilterServiceImpl.update(entity);
			if (update) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("敏感字修改成功！");
				returnDataUtil.setReturnObject(update);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("敏感字修改失败！");
				returnDataUtil.setReturnObject(update);
			}
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("敏感字已存在 ，请重新输入！");
		}
		log.info("<ForumFilterController>-------<update>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "敏感字删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<ForumFilterController>-------<del>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		boolean del = forumFilterServiceImpl.del(id);
		if (del) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("敏感字删除成功！");
			returnDataUtil.setReturnObject(del);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("敏感字删除失败！");
			returnDataUtil.setReturnObject(del);
		}
		log.info("<ForumFilterController>-------<del>-----end");
		return returnDataUtil;
	}
}
