package com.server.module.system.machineManage.machineType;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigLogBean;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigService;
import com.server.module.system.logsManager.operationLog.OperationLogService;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-03-22 13:31:26
 */
@Api(value = "MachinesTypeController", description = "机器类型")
@RestController
@RequestMapping("/machinesType")
public class MachinesTypeController {

	@Autowired
	private MachinesTypeService machinesTypeServiceImpl;
	@Autowired
	private HeadquartersConfigService headquartersConfigServiceImpl;
	@Autowired
	private AdminUserService adminUserServiceImpl;

	@ApiOperation(value = "机器类型列表", notes = "机器类型列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(MachinesTypeCondition condition) {
		return machinesTypeServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "添加机器类型", notes = "添加机器类型", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MachinesTypeBean entity, HttpServletRequest request) {
		//添加判断售货机类型不能重复
		ReturnDataUtil returnData=machinesTypeServiceImpl.checkOnlyone(entity);
		if(returnData.getStatus()==-1) {//说明该类型已存在，添加失败
			returnData.setMessage("该类型已存在，添加失败");
			returnData.setStatus(-1);
			return returnData;
		}
		// 增加售货机基础信息
		MachinesTypeBean insert = machinesTypeServiceImpl.insert(entity);
		if (insert != null) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			HeadquartersConfigLogBean bean = new HeadquartersConfigLogBean();
			bean.setCompanyId(userBean.getCompanyId());
			bean.setUserName(userBean.getName());
			bean.setContent("增加售货机类型");
			bean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加总部配置操作日志
			headquartersConfigServiceImpl.insert(bean);
			return new ReturnDataUtil(insert);
		}
		return null;
	}

	@ApiOperation(value = "修改机器类型", notes = "修改机器类型", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody MachinesTypeBean entity, HttpServletRequest request) {
		//修改时判断售货机类型不能重复
		boolean falg=false;
		MachinesTypeBean type=machinesTypeServiceImpl.findTypeById(entity.getId());
		ReturnDataUtil returnData=machinesTypeServiceImpl.checkOnlyone(entity);
		if(entity.getName().equals(type.getName())||returnData.getStatus()!=-1) {
			//类型发生改变，没有重复的类型可以直接保存//类型没有改变，直接保存
			 falg = machinesTypeServiceImpl.update(entity);
		}else {//类型发生改变，有重复的类型，修改失败
			returnData.setMessage("修改失败，该类型已存在");
			return returnData;
		}
		if (falg) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			HeadquartersConfigLogBean bean = new HeadquartersConfigLogBean();
			bean.setCompanyId(userBean.getCompanyId());
			bean.setUserName(userBean.getName());
			bean.setContent("修改售货机类型");
			bean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加总部配置操作日志
			headquartersConfigServiceImpl.insert(bean);
			return new ReturnDataUtil(falg);
		}
		return null;

	}

	@ApiOperation(value = "删除机器类型", notes = "删除机器类型", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		MachinesTypeBean entity = new MachinesTypeBean();
		entity.setId(id);
		return new ReturnDataUtil(machinesTypeServiceImpl.del(entity));

	}

	@ApiOperation(value = "所有机器类型", notes = "所有机器类型（下拉框调用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list() {
		MachinesTypeCondition condition = new MachinesTypeCondition();
		condition.setPageSize(1000);
		return machinesTypeServiceImpl.listPage(condition);
	}
	@ApiOperation(value = "查询有效的机器类型", notes = "查询有效的机器类型", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findAllByState", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findAllByState() {
		return machinesTypeServiceImpl.findAllByState();
	}
}
