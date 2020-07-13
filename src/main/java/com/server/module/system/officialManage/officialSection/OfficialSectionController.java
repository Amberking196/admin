package com.server.module.system.officialManage.officialSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.io.IOException;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.officialManage.officialMessage.OfficialMessageForm;
import com.server.module.system.warehouseManage.supplierManage.SupplierBean;
import com.server.util.ExcelUtil;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
/**
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
@Api(value ="OfficialSectionController",description="官网栏目")
@RestController
@RequestMapping("/officialSection")
public class  OfficialSectionController{

	public static Logger log = LogManager.getLogger(OfficialSectionController.class);
	@Autowired
	private OfficialSectionService officialSectionServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "官网栏目列表",notes = "listPage",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) OfficialSectionForm officialSectionForm){
		log.info("<OfficialSectionController>--<listPage>--start");
		if(officialSectionForm==null) {
			officialSectionForm=new OfficialSectionForm();
		}
		returnDataUtil=officialSectionServiceImpl.listPage(officialSectionForm);
		log.info("<OfficialSectionController>--<listPage>--end");
		return returnDataUtil;
	}

	@ApiOperation(value = "官网栏目添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(@RequestBody OfficialSectionBean entity){
		log.info("<OfficialSectionController>--<add>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setCreateUser(UserUtils.getUser().getId());
		entity.setPid(0l);
		OfficialSectionBean newOfficialSectionBean=officialSectionServiceImpl.add(entity);
		if(entity.getList()!=null&entity.getList().size()>0) {
			for(OfficialSectionBean secondBean:entity.getList()) {
				secondBean.setPid(newOfficialSectionBean.getId());
				secondBean.setCreateUser(UserUtils.getUser().getId());
				officialSectionServiceImpl.add(secondBean);				
			}
		}
		if(newOfficialSectionBean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("新增成功");
		}
		else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("新增失败");
		}
		log.info("<OfficialSectionController>--<add>--end");
		return returnDataUtil;
	}

	
	@ApiOperation(value = "官网子栏目修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateSonSection", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateSonSection(@RequestBody OfficialSectionBean officialSectionBean){
		returnDataUtil=officialSectionServiceImpl.updateSonSection(officialSectionBean);
		return returnDataUtil;
	}
	
	@ApiOperation(value = "官网栏目修改",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody OfficialSectionBean entity){
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();

		if(officialSectionServiceImpl.update(entity)) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("更新成功");
		}
		else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("更新失败");
		}
		
		return returnDataUtil;	
	}

	@ApiOperation(value = "官网栏目删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody OfficialSectionBean entity){
		log.info("<OfficialSectionController>--<del>--start");
		returnDataUtil=officialSectionServiceImpl.del(entity.getId());
		log.info("<OfficialSectionController>--<del>--end");
		return returnDataUtil;
	}


}

