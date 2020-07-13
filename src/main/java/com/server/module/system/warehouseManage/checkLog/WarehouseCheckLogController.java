package com.server.module.system.warehouseManage.checkLog;
import com.google.common.collect.Lists;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseDto;
import com.server.util.DateUtils;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * author name: yjr
 * create time: 2018-06-02 15:10:23
 */
@Api(value = "WarehouseCheckLogController", description = "盘点统计")
@RestController
@RequestMapping("/warehouseCheckLog")
public class WarehouseCheckLogController {
	@Autowired
	private WarehouseCheckLogService warehouseCheckLogServiceImpl;
	@ApiOperation(value = "盘点统计列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(WarehouseCheckLogForm condition) {
		return warehouseCheckLogServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "盘点统计详情", notes = "listDetail", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listDetail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listDetail(Long lineId,Integer time) {
		return warehouseCheckLogServiceImpl.listDetail(lineId,time);
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "盘点统计导出", notes = "export", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public void exoprt(HttpServletRequest request, HttpServletResponse response, WarehouseCheckLogForm condition) {
		String fileName = "盘点统计" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
		condition.setPageSize(1000);
		ReturnDataUtil data= warehouseCheckLogServiceImpl.listPage(condition);
		try {
			List<String> summaryList= Lists.newArrayList();
			List<String> rightSummaryList= Lists.newArrayList();
			List<WarehouseCheckLogBean> list=(ArrayList<WarehouseCheckLogBean>) data.getReturnObject();
			if(list.size()>0){
				System.out.println("list zize==="+list.size());
				WarehouseCheckLogBean bean=(WarehouseCheckLogBean)list.get(0);
				summaryList.add("公司："+bean.getCompanyName());
				summaryList.add("区域："+bean.getAreaName());
				summaryList.add("线路："+bean.getLineName());
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strtime="盘点时间从 "+sf.format(bean.getStartTime())+" 到 "+sf.format(bean.getEndTime());
				summaryList.add(strtime);
				rightSummaryList.add("线路负责人：       仓库：        财务：       ");
			}


			new ExportExcel("盘点统计",summaryList,rightSummaryList,WarehouseCheckLogBean.class).setDataList(list)
            .write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "盘点统计添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseCheckLogBean entity) {
		return new ReturnDataUtil(warehouseCheckLogServiceImpl.add(entity));
	}

	@ApiOperation(value = "盘点统计修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseCheckLogBean entity) {
		return new ReturnDataUtil(warehouseCheckLogServiceImpl.update(entity));
	}

	@ApiOperation(value = "盘点统计删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(warehouseCheckLogServiceImpl.del(id));
	}
	
	@ApiOperation(value = "盘点", notes = "doCheck", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/doCheck", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil doCheck(@RequestBody CheckForm form) {
		ReturnDataUtil data= new ReturnDataUtil();
		Long lineId=form.getLineId();
		Long areaId=form.getAreaId();
		Long companyId=form.getCompanyId();
		warehouseCheckLogServiceImpl.checkStock(lineId, areaId, companyId);
		return data;
	}

}
