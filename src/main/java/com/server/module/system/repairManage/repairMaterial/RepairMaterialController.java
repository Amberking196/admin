package com.server.module.system.repairManage.repairMaterial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * author name: yjr
 * create time: 2019-08-13 14:38:18
 */ 
@Api(value ="RepairMaterialController",description="维修材料")
@RestController
@RequestMapping("/repairMaterial")
public class  RepairMaterialController{

	@Autowired
	private RepairMaterialService repairMaterialServiceImpl;

	@ApiOperation(value = "维修材料列表",notes = "listPage",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(RepairMaterialCondition condition){
		return repairMaterialServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "维修材料添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(@RequestBody RepairMaterialBean entity){
		entity.setDeleteFlag(0);
		return new ReturnDataUtil(repairMaterialServiceImpl.add(entity));
	}

	@ApiOperation(value = "维修材料修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody RepairMaterialBean entity){
		return new ReturnDataUtil(repairMaterialServiceImpl.update(entity));
	}

	@ApiOperation(value = "维修材料删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object  id){
		return new ReturnDataUtil(repairMaterialServiceImpl.del(id));
	}
}

