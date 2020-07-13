package com.server.module.system.promotionManage.timeQuantum;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-08-22 10:56:46
 */
@Api(value = "TimeQuantumController", description = "时间段管理")
@RestController
@RequestMapping("/timeQuantum")
public class TimeQuantumController {

	private static Logger log = LogManager.getLogger(TimeQuantumController.class);
	@Autowired
	private TimeQuantumService timeQuantumServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;

	@ApiOperation(value = "时间段列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumController>----<listPage>------start");
		if (timeQuantumForm == null) {
			timeQuantumForm = new TimeQuantumForm();
		}
		returnDataUtil = timeQuantumServiceImpl.listPage(timeQuantumForm);
		log.info("<TimeQuantumController>----<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "时间段添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody TimeQuantumBean entity) {
		log.info("<TimeQuantumController>----<add>------start");
		entity.setCreateUser(UserUtils.getUser().getId());
		TimeQuantumBean timeQuantumBean = timeQuantumServiceImpl.add(entity);
		if (timeQuantumBean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("时间段增加成功！");
			returnDataUtil.setReturnObject(timeQuantumBean);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("时间段增加成功！");
			returnDataUtil.setReturnObject(timeQuantumBean);
		}
		log.info("<TimeQuantumController>----<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "时间段修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody TimeQuantumBean entity) {
		log.info("<TimeQuantumController>----<update>------start");
		entity.setUpdateUser(UserUtils.getUser().getId());
		boolean update = timeQuantumServiceImpl.update(entity);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("时间段修改成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("时间段修改失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<TimeQuantumController>----<update>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "时间段删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<TimeQuantumController>----<del>------start");
		boolean update = timeQuantumServiceImpl.del(id);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("时间段删除成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("时间段删除失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<TimeQuantumController>----<del>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "时间段条件查询", notes = "findBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findBean(@RequestBody TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumController>----<findBean>------start");
		if (timeQuantumForm.getVmCode() != null && StringUtil.isNotBlank(timeQuantumForm.getVmCode())) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(timeQuantumForm.getVmCode());
			if (bean == null) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("不存在的机器编码或售货机被禁用,请输入正确的售货机编号！");
				returnDataUtil.setReturnObject(null);
			}
		}
		List<TimeQuantumBean> list = timeQuantumServiceImpl.findBean(timeQuantumForm);
		if (list != null && list.size() > 0) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("时间段查看成功！");
			returnDataUtil.setReturnObject(list);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("您公司还未设置相关时间段，请前去设置！");
			returnDataUtil.setReturnObject(list);
		}
		log.info("<TimeQuantumController>----<findBean>------end");
		return returnDataUtil;
	}
}
