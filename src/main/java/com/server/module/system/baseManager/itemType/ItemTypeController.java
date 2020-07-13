package com.server.module.system.baseManager.itemType;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * author name: yjr create time: 2018-04-10 14:28:15
 */
@Api(value = "ItemTypeController", description = "商品类别")
@RestController
@RequestMapping("/itemType")
public class ItemTypeController {

	public static Logger log = LogManager.getLogger(ItemTypeController.class); 	  
	@Autowired
	private ItemTypeService itemTypeServiceImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
    
	@ApiOperation(value = "商品类别列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(ItemTypeCondition condition) {
		log.info("ItemTypeController---------listPage------ start"); 
		returnDataUtil=itemTypeServiceImpl.listPage(condition);
		log.info("ItemTypeController---------listPage------ end"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "商品类别添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(ItemTypeBean entity) {
		log.info("ItemTypeController---------add------ start"); 
		returnDataUtil= new ReturnDataUtil(itemTypeServiceImpl.add(entity));
		log.info("ItemTypeController---------add------ end"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "商品类别修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(ItemTypeBean entity) {
		log.info("ItemTypeController---------update------ start"); 
		returnDataUtil= new ReturnDataUtil(itemTypeServiceImpl.update(entity));
		log.info("ItemTypeController---------update------ start"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "商品类别删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		log.info("ItemTypeController---------del------ start"); 
		returnDataUtil= new ReturnDataUtil(itemTypeServiceImpl.del(id));
		log.info("ItemTypeController---------del------ end"); 
		return returnDataUtil;
	}
	
	
	@ApiOperation(value = "所有商品类别", notes = "所有商品类别（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list() {
		log.info("ItemTypeController---------list------ start");
		ItemTypeCondition condition=new ItemTypeCondition();
		condition.setPageSize(1000);
		returnDataUtil= itemTypeServiceImpl.listPage(condition);
		log.info("ItemTypeController---------list------ start");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "所有商品类别树形显示", notes = "所有商品类别树形显示", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/treeList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil treeList() {
		log.info("ItemTypeController---------treeList------ start");
		ItemTypeCondition condition=new ItemTypeCondition();
		List<ItemTypeBean> list = itemTypeServiceImpl.list(condition);
		returnDataUtil.setReturnObject(list);
		log.info("ItemTypeController---------list------ start");
		return returnDataUtil;
	}
}
