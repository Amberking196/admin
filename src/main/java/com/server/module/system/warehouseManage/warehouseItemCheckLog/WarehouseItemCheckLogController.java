package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.replenishManage.machinesReplenishStatement.ReplenishmentReportBean;
import com.server.module.system.replenishManage.machinesReplenishStatement.ReplenishmentReportForm;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillController;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-06-15 14:00:02
 */
@Api(value = "WarehouseItemCheckLogController", description = "商品盘点")
@RestController
@RequestMapping("/warehouseItemCheckLog")
public class WarehouseItemCheckLogController {

	private static Logger log = LogManager.getLogger(WarehouseItemCheckLogController.class);
	@Autowired
	private WarehouseItemCheckLogService MerchandiseInventoryServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;


	@ApiOperation(value = "商品盘点列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) WarehouseItemCheckLogForm warehouseItemCheckLogForm) {
		log.info("<MerchandiseInventoryController>----<listPage>----start");
		if(warehouseItemCheckLogForm==null) {
			warehouseItemCheckLogForm=new WarehouseItemCheckLogForm();
		}
		returnDataUtil=MerchandiseInventoryServiceImpl.listPage(warehouseItemCheckLogForm);
		log.info("<MerchandiseInventoryController>----<listPage>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
    public void exportFileAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,WarehouseItemCheckLogForm warehouseItemCheckLogForm) {
		log.info("<MerchandiseInventoryController>----<exportAll>----start");
		ReturnDataUtil data1 = MerchandiseInventoryServiceImpl.listPage(warehouseItemCheckLogForm);
		warehouseItemCheckLogForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = MerchandiseInventoryServiceImpl.listPage(warehouseItemCheckLogForm);
		List<WarehouseItemCheckLogBean> data = (List<WarehouseItemCheckLogBean>)returnData.getReturnObject();
		String title ="商品盘点列表";
		String[] headers = new String[]{"num","itemName","quantity","inQuantity","outQuantity","otherQuantity","endQuantity"};
		String[] column = new String[]{"序号","商品名称","初期数","入库数","出库数","其他出库","期末数"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(warehouseItemCheckLogForm.getStartTime()!=null&&warehouseItemCheckLogForm.getEndTime()!=null) {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(warehouseItemCheckLogForm.getStartTime())+"--"+DateUtil.formatYYYYMMDD(warehouseItemCheckLogForm.getEndTime())+"商品盘点的数据");
				
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出商品盘点列表--全部数据");
			}
			ExcelUtil.exportExcel(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("<MerchandiseInventoryController>----<exportAll>----start");
    }
	
}
