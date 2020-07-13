package com.server.module.system.repairManage.repairRecord;

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
 * create time: 2019-08-14 10:42:05
 */ 
@Api(value ="RepairRecordController",description="维修记录")
@RestController
@RequestMapping("/repairRecord")
public class  RepairRecordController{


@Autowired
private RepairRecordService repairRecordServiceImpl;
@ApiOperation(value = "维修记录列表",notes = "listPage",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
public ReturnDataUtil listPage(@RequestBody(required=false) RepairRecordCondition condition){
	return repairRecordServiceImpl.listPage(condition);
}

@ApiOperation(value = "维修记录列表详情",notes = "listPage",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
public ReturnDataUtil detail(RepairRecordCondition condition){
	return new ReturnDataUtil(repairRecordServiceImpl.detail(condition));
}

@ApiOperation(value = "维修记录添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
public ReturnDataUtil  add(@RequestBody RepairRecordBean entity){
	return new ReturnDataUtil(repairRecordServiceImpl.add(entity));
}
@ApiOperation(value = "维修记录修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
public ReturnDataUtil update(@RequestBody RepairRecordBean entity){
return new ReturnDataUtil(repairRecordServiceImpl.update(entity));
}
@ApiOperation(value = "维修记录删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
public ReturnDataUtil del(Object  id){
return new ReturnDataUtil(repairRecordServiceImpl.del(id));
}
}

