package com.server.module.system.companyManage.companyMachines;

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

import java.util.List;

/**
 * author name: yjr create time: 2018-09-14 15:10:24
 */
@Api(value = "ReplenishCompanyMachinesController", description = "补水公司机器")
@RestController
@RequestMapping("/replenishCompanyMachines")
public class ReplenishCompanyMachinesController {

	@Autowired
	private ReplenishCompanyMachinesService replenishCompanyMachinesServiceImpl;

	@ApiOperation(value = "补水公司机器列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(ReplenishCompanyMachinesCondition condition) {
		return replenishCompanyMachinesServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "补水公司机器添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(String [] vmCodes ,Long companyId) {
		ReturnDataUtil data=new ReturnDataUtil();
		
		synchronized (ReplenishCompanyMachinesController.class) {
			for(int i=0;i<vmCodes.length;i++){
				System.out.println(vmCodes[i]);
				ReplenishCompanyMachinesBean entity=new ReplenishCompanyMachinesBean();
				entity.setCode(vmCodes[i]);
				entity.setCompanyId(companyId);
				ReplenishCompanyMachinesBean obj=replenishCompanyMachinesServiceImpl.add(entity);
				
			}
		}
		
		//return new ReturnDataUtil(replenishCompanyMachinesServiceImpl.add(entity));
		data.setMessage("添加成功");
		data.setStatus(1);
		return data;
	}

	/*@ApiOperation(value = "补水公司机器修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ReplenishCompanyMachinesBean entity) {
		return new ReturnDataUtil(replenishCompanyMachinesServiceImpl.update(entity));
	}*/

	@ApiOperation(value = "补水公司机器删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long[] ids) {
		ReturnDataUtil data=new ReturnDataUtil();
		for(int i=0;i<ids.length;i++){
			System.out.println(ids[i]);
			replenishCompanyMachinesServiceImpl.del(ids[i]);
		}
		data.setMessage("删除成功");
		return data;
	}
	@ApiOperation(value = "补水公司列表", notes = "listOtherCompanyForSelect", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listOtherCompanyForSelect", produces = "application/json;charset=UTF-8")
	public List<ReplenishCompanyMachinesBean> listOtherCompanyForSelect(){
		return replenishCompanyMachinesServiceImpl.listOtherCompanyForSelect();
	}
}
