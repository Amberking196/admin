package com.server.module.system.memberManage.memberTypeManage;

import java.util.List;

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
 * author name: why create time: 2018-09-20 16:08:54
 */
@Api(value = "MemberTypeController", description = "会员类型")
@RestController
@RequestMapping("/memberType")
public class MemberTypeController {

	private static Logger log = LogManager.getLogger(MemberTypeController.class);

	@Autowired
	private MemberTypeService memberTypeServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "会员类型列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) MemberTypeForm memberTypeForm) {
		log.info("<MemberTypeController>-----<listPage>------start");
		if (memberTypeForm == null) {
			memberTypeForm = new MemberTypeForm();
		}
		returnDataUtil = memberTypeServiceImpl.listPage(memberTypeForm);
		log.info("<MemberTypeController>-----<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员类型添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MemberTypeBean entity) {
		log.info("<MemberTypeController>-----<add>-----start");
		// 判断类型是否重复
		boolean flag = memberTypeServiceImpl.checkType(entity.getType());
		if (flag) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员类型重复，请重新输入！");
		} else {
			entity.setCreateUser(UserUtils.getUser().getId());
			MemberTypeBean insert = memberTypeServiceImpl.insert(entity);
			if (insert != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("会员类型添加成功！");
				returnDataUtil.setReturnObject(insert);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("会员类型添加失败！");
				returnDataUtil.setReturnObject(insert);
			}
		}
		log.info("<MemberTypeController>-----<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员类型修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody MemberTypeBean entity) {
		log.info("<MemberTypeController>------<update>-------start");
		// 得到未修改前的信息
		MemberTypeBean bean = memberTypeServiceImpl.get(entity.getId());
		boolean checkType = false;
		// 判断会员类型是否一致
		if (!(bean.getType().equals(entity.getType()))) {
			// 判断会员类型是否已存在
			checkType = memberTypeServiceImpl.checkType(entity.getType());
			log.info("checkType========"+checkType);
		} 
		if (checkType) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员类型已存在，请重新输入！");
			returnDataUtil.setReturnObject(checkType);
		} else {
			boolean update = memberTypeServiceImpl.update(entity);
			if (update) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("会员类型修改成功！");
				returnDataUtil.setReturnObject(update);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("会员类型修改失败！");
				returnDataUtil.setReturnObject(update);
			}
		}
	
		log.info("<MemberTypeController>------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员类型删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<MemberTypeController>------<del>-------start");
		boolean delete = memberTypeServiceImpl.del(id);
		if (delete) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("会员类型删除成功！");
			returnDataUtil.setReturnObject(delete);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员类型删除失败！");
			returnDataUtil.setReturnObject(delete);
		}
		log.info("<MemberTypeController>------<del>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "会员类型下拉列表", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list() {
		log.info("<MemberTypeController>------<list>-------start");
		List<MemberTypeBean> list = memberTypeServiceImpl.list();
		if (list!=null&&list.size() > 0) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("会员类型下拉列表查看成功！");
			returnDataUtil.setReturnObject(list);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("会员类型下拉列表查看失败！");
			returnDataUtil.setReturnObject(list);
		}
		log.info("<MemberTypeController>------<list>-------end");
		return returnDataUtil;
	}
}
