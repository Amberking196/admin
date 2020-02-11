package com.server.module.system.baseManager.stateInfo;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.google.common.collect.Lists;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * author name: yjr 
 * create time: 2018-03-30 11:10:15
 */
@Api(value = "StateInfoController", description = "数据字典")
@RestController
@RequestMapping("/stateInfo")
public class StateInfoController {

	public static Logger log = LogManager.getLogger(StateInfoController.class); 	    
	@Autowired
	private StateInfoService stateInfoServiceImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
    
	@ApiOperation(value = "数据字典列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(StateInfoCondition condition) {
		log.info("ItemTypeController---------listPage------ start"); 
		returnDataUtil= stateInfoServiceImpl.listPage(condition);
		log.info("ItemTypeController---------listPage------ end"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "数据字典添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody(required=false) StateInfoBean entity) {//有修改
		log.info("ItemTypeController---------add------ start"); 
		if(entity==null) {
			entity=new StateInfoBean();
		}
		//添加判断字典的状态应该是唯一的。
		returnDataUtil=stateInfoServiceImpl.checkStateOnlyOne(entity);
		if(returnDataUtil.getStatus()==-1) {//该状态码存在
			return returnDataUtil;
		}
		returnDataUtil= new ReturnDataUtil(stateInfoServiceImpl.add(entity));
		log.info("ItemTypeController---------add------ end"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "数据字典修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody StateInfoBean entity) {
		log.info("ItemTypeController---------update------ start");
		returnDataUtil=  new ReturnDataUtil(stateInfoServiceImpl.update(entity));
		log.info("ItemTypeController---------update------ end"); 
		return returnDataUtil;
	}

	@ApiOperation(value = "数据字典删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		log.info("ItemTypeController---------del------ start"); 
		returnDataUtil=  new ReturnDataUtil(stateInfoServiceImpl.del(id));
		log.info("ItemTypeController---------del------ end"); 
		return returnDataUtil;
	}
	
	@ApiOperation(value = "根据字典类别获取字典列表", notes = "根据字典类别获取字典列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getStateInfos", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getStateInfos(String keyName) {
		log.info("ItemTypeController---------getStateInfos------ start"); 
		ReturnDataUtil returnDataUtil= new ReturnDataUtil(stateInfoServiceImpl.findStateInfoByKeyName(keyName));
		log.info("ItemTypeController---------getStateInfos------ end"); 
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "商品状态列表", notes = "商品状态列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getItemStateInfos", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemStateInfos() {
		log.info("ItemTypeController---------getItemStateInfos------ start"); 
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("itemBasic");
		ReturnDataUtil returnDataUtil= new ReturnDataUtil(list);
		log.info("ItemTypeController---------getItemStateInfos------ end"); 
		/*List<Object> lists=Lists.newArrayList();
		for (StateInfoDto dto : list) {//他妈的奇葩，商品状态存的是文本不state
			lists.add(dto.getState());
			lists.add(dto.getName());
		}*/
		return returnDataUtil;
	} 
	@ApiOperation(value = "商品单位列表", notes = "商品单位列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getItemUnits", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemUnits() {
		log.info("ItemTypeController---------getItemUnits------ start");  
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("item_unit");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getItemUnits------ end"); 
		return returnDataUtil;
	} 
	/*@ApiOperation(value = "故障状态列表", notes = "故障状态列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getErrorStates", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getErrorStates() {
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("msg_state");
		return new ReturnDataUtil(list);
	} */
	
	@ApiOperation(value = "机器状态信息列表", notes = "机器状态信息列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getMachineMsg", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getMachineMsg() {
		log.info("ItemTypeController---------getMachineMsg------ start");  
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("msg");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getMachineMsg------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "机器解决状态列表", notes = "机器解决状态列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getMachineMsgSates", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getMachineMsgSates() {
		log.info("ItemTypeController---------getMachineMsgSates------ start");  
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("msg_state");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getMachineMsgSates------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "售货机状态列表", notes = "售货机状态列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getVmInfo", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getVmInfo() {
		log.info("ItemTypeController---------getVmInfo------ start");  
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("VWInfo");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getVmInfo------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "getMachineTypeState列表", notes = "getMachineTypeState列表（下拉框用）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getMachineTypeState", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getMachineTypeState() {
		log.info("ItemTypeController---------getMachineTypeState------ start");  
		List<StateInfoDto>  list=stateInfoServiceImpl.findStateInfoByKeyName("machinesType");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getMachineTypeState------ start");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "仓库状态列表", notes = "仓库状态列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseState", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseState() {
		log.info("ItemTypeController---------getWarehouseState------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseState();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseState------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "入库状态列表", notes = "入库状态列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseWarrantState", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseWarrantState() {
		log.info("ItemTypeController---------getWarehouseWarrantState------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseWarrantState();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseWarrantState------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "入库类型列表", notes = "入库类型列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseWarrantType", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseWarrantType() {
		log.info("ItemTypeController---------getWarehouseWarrantState------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseWarrantType();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseWarrantState------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "出库状态列表", notes = "出库状态列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseRemovalState", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseRemovalState() {
		log.info("ItemTypeController---------getWarehouseRemovalState------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseRemovalState();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseRemovalState------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "出库类型列表", notes = "出库类型列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseRemovalType", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseRemovalType() {
		log.info("ItemTypeController---------getWarehouseRemovalType------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseRemovalType();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseRemovalType------ end");  
		return returnDataUtil;
	} 
	
	@ApiOperation(value = "归还类型列表", notes = "归还类型列表（下拉框用）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getWarehouseReturnType", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseReturnType() {
		log.info("ItemTypeController---------getWarehouseReturnType------ start");  
		List<StateInfoBean> list = stateInfoServiceImpl.getWarehouseReturnType();
		ReturnDataUtil returnDataUtil=new ReturnDataUtil(list);
		log.info("ItemTypeController---------getWarehouseReturnType------ end");  
		return returnDataUtil;
	} 
	
}
