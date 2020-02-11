package com.server.module.system.warehouseManage.warehouseReplenish;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.warehouseManage.warehouseRemoval.WarehouseRemovalController;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayTypeEnum;
import com.server.util.stateEnum.WarehouseOutputEnum;
import com.server.util.stateEnum.WarehouseTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * author name: hjc
 * create time: 2018-05-28 17:58:52
 */ 
@Api(value ="warehouseReplenishController",description="补货领取水量")
@RestController
@RequestMapping("/warehouseReplenish")
public class  WarehouseReplenishController{
	private static Logger log = LogManager.getLogger(WarehouseRemovalController.class);
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private WarehouseReplenishService warehouseReplenishServiceImpl;
	@ApiOperation(value = "补货领取水量列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) WarehouseReplenishForm warehouseReplenishForm){
		log.info("<WarehouseReplenishController>------<listPage>-----start");
		if (warehouseReplenishForm == null) {
			warehouseReplenishForm = new WarehouseReplenishForm();
		}
		returnDataUtil=warehouseReplenishServiceImpl.listPage(warehouseReplenishForm);
		log.info("<WarehouseReplenishController>------<listPage>-----end");
		return returnDataUtil;

	}	
	
	@ApiOperation(value = "导出", notes = "导出" )
	@PostMapping(value = "/exportFileAll" )
    public void exportFileAll(@RequestBody(required = false) WarehouseReplenishForm warehouseReplenishForm,HttpServletRequest request, HttpServletResponse response) {	
		log.info("<WarehouseReplenishController>------<exportFileAll>-----start");
		if(warehouseReplenishForm==null) {
			warehouseReplenishForm=new WarehouseReplenishForm();
		}
		ReturnDataUtil returnData = warehouseReplenishServiceImpl.listPage(warehouseReplenishForm);
		List<WarehouseOutputBillBean> data = (List<WarehouseOutputBillBean>) returnData.getReturnObject();
		StringBuffer date = new StringBuffer("起始--至今");
		if (warehouseReplenishForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDD(warehouseReplenishForm.getStartDate()));
		}
		if (warehouseReplenishForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDD(warehouseReplenishForm.getEndDate()));
		}
		if(warehouseReplenishForm.getIsShowAll()==0){
			date.append("--第"+warehouseReplenishForm.getCurrentPage()+"页");
		}
		String title = "补货领水量列表";
		String[] headers = new String[] { "num", "number", "warehouseName","outputName","typeName","itemName","allNum","time",
				"auditorName" };
		String[] column = new String[] { "序号", "编号", "仓库", "种类","类型","商品","数量","制单时间", "审核人" };
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(warehouseReplenishForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(warehouseReplenishForm.getEndDate())+"的补货领水量列表的数据");
			ExcelUtil.exportExcel(title, headers, column, response, data, date.toString());
			log.info("<WarehouseReplenishController>------<exportFileAll>-----end");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	@PostMapping("/initInfo")
	@ResponseBody
	public ReturnDataUtil initInfo() {
		ReturnDataUtil data = new ReturnDataUtil();
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<Object, Object> type = new HashMap<Object, Object>();
		for (WarehouseTypeEnum warehouseType: WarehouseTypeEnum.values()) {
			type.put(warehouseType.getIndex(), warehouseType.getType ());
		}
		Map<Object, Object> outputType = new HashMap<Object, Object>();
		for (WarehouseOutputEnum warehouseOutput: WarehouseOutputEnum.values()) {
			outputType.put(warehouseOutput.getIndex(), warehouseOutput.getType ());
		}
		returnData.put("type", type);
		returnData.put("output", outputType);
		if (returnData != null && returnData.size() > 0) {
			data.setStatus(1);
			data.setMessage("初始化成功");
			data.setReturnObject(returnData);
		} else {
			data.setStatus(0);
			data.setMessage("初始化失败");
		}
		return data;
	}

}

