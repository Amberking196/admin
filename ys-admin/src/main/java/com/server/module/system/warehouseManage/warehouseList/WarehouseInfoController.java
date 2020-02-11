package com.server.module.system.warehouseManage.warehouseList;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.adminUser.UserVoForSelect;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-05-14 22:06:48
 */
@Api(value = "WarehouseInfoController", description = "仓库列表")
@RestController
@RequestMapping("/warehouseInfo")
public class WarehouseInfoController {

	public static Logger log = LogManager.getLogger(WarehouseInfoController.class);
	@Autowired
	private WarehouseInfoService warehouseInfoServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private AdminUserService adminUserService;

	@ApiOperation(value = "仓库列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoController>------<listPage>-----start");
		if (warehouseInfoForm == null) {
			warehouseInfoForm = new WarehouseInfoForm();
		}
		returnDataUtil = warehouseInfoServiceImpl.listPage(warehouseInfoForm);
		log.info("<WarehouseInfoController>------<listPage>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "根据公司Id查询仓库列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPageByCompanyId", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPageByCompanyId(@RequestBody(required = false) WarehouseInfoForm warehouseInfoForm,HttpServletRequest request) {
		log.info("<WarehouseInfoController>------<listPageByCompanyId>-----start");

		// 根据公司Id来查仓库，不分页！
		if (warehouseInfoForm.getCompanyId() != null) {
			warehouseInfoForm.setPageSize(10000);

		}
		returnDataUtil = warehouseInfoServiceImpl.listPageByCompanyId(warehouseInfoForm);
		log.info("<WarehouseInfoController>------<listPageByCompanyId>-----end");
		return returnDataUtil;
	}
	@ApiOperation(value = "根据公司Id和仓库管理员id查询仓库列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findListByUserId", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findListByUserId(@RequestBody(required = false) WarehouseInfoForm warehouseInfoForm,HttpServletRequest request) {
		log.info("<WarehouseInfoController>------<listPageByCompanyId>-----start");
		
		// 根据公司Id来查仓库，不分页！
		if (warehouseInfoForm.getCompanyId() != null) {
			warehouseInfoForm.setPageSize(10000);
			
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		warehouseInfoForm.setUserId(userId);//设置用户id
		returnDataUtil = warehouseInfoServiceImpl.findListByUserId(warehouseInfoForm);
		log.info("<WarehouseInfoController>------<listPageByCompanyId>-----end");
		return returnDataUtil;
	}


	@ApiOperation(value = "仓库添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseInfoBean entity, HttpServletRequest request) {
		log.info("<WarehouseInfoController>------<add>-----start");
		entity.setCreateTime(new Date());
		entity.setDelFlag(0);
		boolean checkName = warehouseInfoServiceImpl.checkName(entity.getName(),entity.getCompanyId());
		if (checkName) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该仓库名已经被使用,请重新输入!");
			log.info("<WarehouseInfoController>------<add>-----end");
			return returnDataUtil;
		} else {
			WarehouseInfoBean bean = warehouseInfoServiceImpl.insert(entity);
			if (bean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("仓库创建成功!");
				returnDataUtil.setReturnObject(bean);
				log.info("<WarehouseInfoController>------<add>-----end");
				return returnDataUtil;
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("新增仓库失败!");
				log.info("<WarehouseInfoController>------<add>-----end");
				return returnDataUtil;
			}
		}
	}

	@ApiOperation(value = "仓库修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseInfoBean entity, HttpServletRequest request) {
		log.info("<WarehouseInfoController>------<update>-----start");
		// 得到没有修改前的仓库信息
		WarehouseInfoBean warehouseInfoBean = warehouseInfoServiceImpl.get(entity.getId());
		boolean checkName = false;
		if (!(warehouseInfoBean.getName().equals(entity.getName()))) {
			checkName = warehouseInfoServiceImpl.checkName(entity.getName(),entity.getCompanyId());
		}
		if (checkName) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该仓库名已经被使用,请重新输入!");
			log.info("<WarehouseInfoController>------<update>-----end");
			return returnDataUtil;
		} else {
			boolean update = warehouseInfoServiceImpl.update(entity);
			if (update) {
				returnDataUtil.setMessage("仓库修改成功!");
				log.info("<WarehouseInfoController>------<update>-----end");
				return returnDataUtil;
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("仓库修改失败!");
				log.info("<WarehouseInfoController>------<update>-----end");
				return returnDataUtil;
			}
		}
	}

	@ApiOperation(value = "仓库删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody List<Integer> id, HttpServletRequest request) {
		log.info("<WarehouseInfoController>------<del>-----start");
		String payIds = StringUtils.join(id, ",");
		boolean delete = warehouseInfoServiceImpl.delete(payIds);
		if (delete) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("仓库删除成功!");
			log.info("<WarehouseInfoController>------<del>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("仓库删除失败!");
			log.info("<WarehouseInfoController>------<del>-----end");
			return returnDataUtil;
		}
	}

	@ApiOperation(value = "查询所有状态为启用的仓库", notes = "下拉列表仓库", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findWarehouseInfo", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findWarehouseInfo() {
		log.info("<WarehouseInfoController>------<findWarehouseInfo>-----start");
		List<WarehouseInfoBean> list = warehouseInfoServiceImpl.findWarehouseInfoBean();
		returnDataUtil.setStatus(1);
		returnDataUtil.setReturnObject(list);
		log.info("<WarehouseInfoController>------<findWarehouseInfo>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "查询所有状态为启用的仓库", notes = "下拉列表仓库", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findAllWarehouseInfo", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findAllWarehouseInfo() {
		log.info("<WarehouseInfoController>------<findWarehouseInfo>-----start");
		List<WarehouseInfoBean> list = warehouseInfoServiceImpl.findAllWarehouseInfoBean();
		returnDataUtil.setStatus(1);
		returnDataUtil.setReturnObject(list);
		log.info("<WarehouseInfoController>------<findWarehouseInfo>-----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "判断当前用户是否是仓库的负责人", notes = "判断当前用户是否是仓库的负责人", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/checkPrincipal", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil checkPrincipal(@RequestBody(required=false) WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoController>------<checkPrincipal>-----start");
		if (warehouseInfoForm == null) {
			warehouseInfoForm = new WarehouseInfoForm();
		}
		WarehouseInfoBean bean = warehouseInfoServiceImpl.checkPrincipal(warehouseInfoForm);
		 if(bean!=null) {
			 returnDataUtil.setStatus(1);
			 returnDataUtil.setReturnObject(bean);
		 }else {
			 returnDataUtil.setStatus(0);
			 returnDataUtil.setReturnObject(bean);
		 }
		 log.info("<WarehouseInfoController>------<checkPrincipal>-----end");
		return returnDataUtil;
	}
	@ApiOperation(value = "我的仓库列表", notes = "listCurrUserWarehouse", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listCurrUserWarehouse", produces = "application/json;charset=UTF-8")
	public List<WarehouseVo> listCurrUserWarehouse(){
		return warehouseInfoServiceImpl.listCurrUserWarehouse();
	}
	
	@ApiOperation(value = "用户列表", notes = "listUsersForSelect", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listUsersForSelect", produces = "application/json;charset=UTF-8")
	public List<UserVoForSelect> listUsersForSelect(Long warehouseId){
		return warehouseInfoServiceImpl.listUsersForSelect(warehouseId);
	}

	
}
