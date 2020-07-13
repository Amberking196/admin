package com.server.module.system.warehouseManage.warehouseAdmin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoService;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-09-03 14:23:48
 */
@Api(value = "WarehouseAdminController", description = "仓库管理员")
@RestController
@RequestMapping("/warehouseAdmin")
public class WarehouseAdminController {

	@Autowired
	private WarehouseAdminService warehouseAdminServiceImpl;
    @Autowired
    private WarehouseInfoService warehouseInfoService;
	@ApiOperation(value = "仓库管理员列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(WarehouseAdminCondition condition) {
		condition.setPageSize(100);
		return warehouseAdminServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "仓库管理员添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseAdminBean entity) {
		ReturnDataUtil data=new ReturnDataUtil();
        List<WarehouseAdminBean> list=warehouseAdminServiceImpl.listByWarehouseId(entity.getWarehouseInfoId());
		for(WarehouseAdminBean obj:list){
			if(obj.getUserId().equals(entity.getUserId())){
				data.setStatus(0);
				data.setMessage("不能重复添加");
				return data;
			}
		}
		WarehouseInfoBean warehouse=warehouseInfoService.get(entity.getWarehouseInfoId());
		Integer principal=warehouse.getPrincipal();
		Long curId=UserUtils.getUser().getId();
		if(curId.intValue()!=principal.intValue()){
			data.setStatus(0);
			data.setMessage("仓库负责人才有权限操作");
			return data;
		}
		
		entity.setWarehouseName(warehouse.getName());
		return new ReturnDataUtil(warehouseAdminServiceImpl.add(entity));
	}

	/*@ApiOperation(value = "仓库管理员修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseAdminBean entity) {
		return new ReturnDataUtil(warehouseAdminServiceImpl.update(entity));
	}*/

	@ApiOperation(value = "仓库管理员删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		ReturnDataUtil data=new ReturnDataUtil();
		WarehouseAdminBean adminBean=warehouseAdminServiceImpl.get(id);
	
		WarehouseInfoBean warehouse=warehouseInfoService.get(adminBean.getWarehouseInfoId());
		Integer principal=warehouse.getPrincipal();
		Long curId=UserUtils.getUser().getId();
		if(curId.intValue()!=principal.intValue()){
			data.setStatus(0);
			data.setMessage("仓库负责人才有权限操作");
			return data;
		}
		return new ReturnDataUtil(warehouseAdminServiceImpl.del(id));
	}
}
