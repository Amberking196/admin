package com.server.module.system.warehouseManage.stockLog;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * author name: yjr 
 * create time: 2018-05-22 11:00:53
 */
@Api(value = "WarehouseStockLogController", description = "库存日志")
@RestController
@RequestMapping("/stockLog")
public class WarehouseStockLogController {

	private static Logger log = LogManager.getLogger(WarehouseStockLogController.class);

	@Autowired
	private WarehouseStockLogService warehouseStockLogServiceImpl;

	@ApiOperation(value = "库存日志列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(WarehouseStockLogCondition condition) {
		return warehouseStockLogServiceImpl.listPage(condition);
	}

	/*@ApiOperation(value = "库存日志添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseStockLogBean entity) {
		return new ReturnDataUtil(warehouseStockLogServiceImpl.add(entity));
	}*/

	/*@ApiOperation(value = "库存日志修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseStockLogBean entity) {
		return new ReturnDataUtil(warehouseStockLogServiceImpl.update(entity));
	}*/

	/*@ApiOperation(value = "库存日志删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(warehouseStockLogServiceImpl.del(id));
	}*/

	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
	public void exportFileAll( HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, WarehouseStockLogCondition condition) {
		log.info("<WarehouseStockLogController>----<exportAll>----start");
		ReturnDataUtil data1 = warehouseStockLogServiceImpl.listPage(condition);
		condition.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = warehouseStockLogServiceImpl.listPage(condition);
		List<WarehouseStockLogBean> data = (List<WarehouseStockLogBean>)returnData.getReturnObject();
		String title ="库存日志";
		String[] headers = new String[]{"itemName","warehouseName","quantity","preQuantity","num","typeLabel","createTime"};
		String[] column = new String[]{"商品名称","仓库名称","更新后库存数量","更新前库存数量","更新的数量","类型","创建时间"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(condition.getStartTime()!=null&&condition.getEndTime()!=null) {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(condition.getStartTime())+"--"+ DateUtil.formatYYYYMMDD(condition.getEndTime())+"库存日志的数据");

			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出库存日志列表--全部数据");
			}
			ExcelUtil.exportExcel(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("<WarehouseStockLogController>----<exportAll>----start");
	}
}
