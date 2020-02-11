package com.server.module.system.warehouseManage.supplierManage;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
@Api(value ="SupplierController",description="供应商")
@RestController
@RequestMapping("/supplier")
public class  SupplierController{
	public static Logger log = LogManager.getLogger(SupplierServiceImpl.class); 	 

	@Autowired
	private SupplierService supplierServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	
	@ApiOperation(value = "供应商列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) SupplierForm supplierForm){
		log.info("<SupplierController>--<listPage>--start"); 
		if(supplierForm==null) {
			supplierForm=new SupplierForm();
		}
		returnDataUtil=supplierServiceImpl.listPage(supplierForm);
		log.info("<SupplierController>--<listPage>--end"); 
		return 	returnDataUtil;
	}
	@ApiOperation(value = "供应商列表forselect",notes = "listForSelect",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listForSelect", produces = "application/json;charset=UTF-8")
	public List<SupplierVoForSelect> listForSelect(){
		return supplierServiceImpl.listForSelect();

	}
	
	@ApiOperation(value = "供应商添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(@RequestBody SupplierBean entity){
		log.info("<SupplierController>--<add>--start"); 
		returnDataUtil=new ReturnDataUtil(supplierServiceImpl.add(entity));
		log.info("<SupplierController>--<add>--end"); 
		return returnDataUtil;
	}
	
	@ApiOperation(value = "供应商修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody SupplierBean entity){
		log.info("<SupplierController>--<update>--start"); 
		returnDataUtil=new ReturnDataUtil(supplierServiceImpl.update(entity));
		log.info("<SupplierController>--<update>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "供应商删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id){
		log.info("<SupplierController>--<del>--start"); 
		returnDataUtil=new ReturnDataUtil(supplierServiceImpl.del(id));
		log.info("<SupplierController>--<del>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "供应商模糊搜索",notes = "findBean",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findBean(@RequestBody SupplierForm supplierForm){
		log.info("<SupplierController>--<findBean>--start"); 
		List<SupplierBean> findBean = supplierServiceImpl.findBean(supplierForm.getCompanyName());
		returnDataUtil.setReturnObject(findBean);
		log.info("<SupplierController>--<findBean>--end"); 
		return 	returnDataUtil;
	}
	@ApiOperation(value = "查询全部供应商",notes = "findAll",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findAll", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findAll() {
		log.info("<SupplierController>--<findAll>--start"); 
		returnDataUtil=supplierServiceImpl.findAll();
		if(returnDataUtil.getStatus()==0) {//查询成功
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询失败");
		}
		log.info("<SupplierController>--<findAll>--end"); 
		return 	returnDataUtil;
	}
}

