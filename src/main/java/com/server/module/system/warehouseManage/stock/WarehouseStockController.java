package com.server.module.system.warehouseManage.stock;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.warehouseManage.checkLog.WarehouseCheckLogBean;
import com.server.util.DateUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemForm;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemService;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * author name: yjr create time: 2018-05-22 10:49:58
 */
@Api(value = "WarehouseStockController", description = "库存")
@RestController
@RequestMapping("/warehouseStock")
public class WarehouseStockController {
	
	private static Logger log=LogManager.getLogger(WarehouseStockController.class);

	@Autowired
	private WarehouseStockService warehouseStockServiceImpl;
	
	@Autowired
	private WarehouseBillItemService warehouseBillItemService;

	@ApiOperation(value = "库存列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(WarehouseStockForm condition) {
		return warehouseStockServiceImpl.listPage(condition);
	}


	@ApiOperation(value = "导出列表", notes = "export", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public void export(HttpServletRequest request, HttpServletResponse response, WarehouseStockForm condition) {
		condition.setPageSize(10000);
		ReturnDataUtil data= warehouseStockServiceImpl.listPage(condition);

		try {
			String fileName = "库存列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			//fileName=new String(fileName.getBytes("GB2312"), "iso8859-1");
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			List<WarehouseStockBean> list=(List<WarehouseStockBean>)data.getReturnObject();
			if(condition.getType()==1){
				List<ExportWarehouseVo> listData= Lists.newArrayList();
				for (int i = 0; i < list.size(); i++) {
					ExportWarehouseVo vo=new ExportWarehouseVo();
					BeanUtils.copyProperties(list.get(i),vo);
					listData.add(vo);
				}
				
				bean.setContent("用户: "+bean.getOperatorName()+" 导出库存信息/仓库列表--全部数据");
				new ExportExcel("仓库库存列表", ExportWarehouseVo.class).setDataList(listData)
						.write(response, fileName).dispose();
			}else if(condition.getType()==2){
				List<ExportCompanyVo> listData= Lists.newArrayList();
				for (int i = 0; i < list.size(); i++) {
					ExportCompanyVo vo=new ExportCompanyVo();
					BeanUtils.copyProperties(list.get(i),vo);
					listData.add(vo);
				}
				bean.setContent("用户: "+bean.getOperatorName()+" 导出库存信息/公司列表--全部数据");
				new ExportExcel("公司库存列表", ExportCompanyVo.class).setDataList(listData)
						.write(response, fileName).dispose();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "库存详细列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listItemPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listItemPage(WarehouseBillItemForm condition){
		return warehouseBillItemService.listPage(condition);
	
	}

	@GetMapping(value = "/test", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil testStock(Long billId) {
		//warehouseStockServiceImpl.putStorage(billId);
		return null;
		//return new ReturnDataUtil(warehouseStockServiceImpl.add(entity));
	}
	/*@ApiOperation(value = "库存添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseStockBean entity) {
		return new ReturnDataUtil(warehouseStockServiceImpl.add(entity));
	}

	@ApiOperation(value = "库存修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseStockBean entity) {
		return new ReturnDataUtil(warehouseStockServiceImpl.update(entity));
	}

	@ApiOperation(value = "库存删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(warehouseStockServiceImpl.del(id));
	}*/
	@ApiOperation(value = "根据条形码查询仓库商品 ", notes = "getItemInfo", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getWarehouseItem", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getWarehouseItem(Long warehouseId,String barCode) {
		ReturnDataUtil data = warehouseStockServiceImpl.getWarehouseItem(warehouseId,barCode);
		return data;

	}
	@ApiOperation(value = "查询仓库现有的商品 ", notes = "getItemInfo", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getItemInfo", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemInfo(Long warehouseId,Integer type) {
		//type =1 进库   2  出库查询
		log.info("<WarehouseStockController>----<getItemInfo>----start");
		List<WarehouseStockBean> list = warehouseStockServiceImpl.getItemInfo(warehouseId,type);
		log.info("<WarehouseStockController>----<getItemInfo>----end");
		return new ReturnDataUtil(list);

	}
	@ApiOperation(value = "添加商品到仓库 ", notes = "addItemToWarehouse", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addItemToWarehouse", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil addItemToWarehouse(@RequestBody(required=false) WarehouseStockBean bean) {
		log.info("<WarehouseStockController>----<addItemToWarehouse>----start");
		if(bean==null) {
			bean=new WarehouseStockBean (); 
		}
		ReturnDataUtil data=new ReturnDataUtil();
		//判断数据是否合法
		if(bean.getItemId()==null||bean.getCompanyId()==null||bean.getWarehouseId()==null) {
			//数据不合法，直接返回错误信息
			data.setStatus(0);
			data.setMessage("传递参数不合法");
			return data;
		}
		//判断该仓库是否有该商品，如果有就直接返回，该商品已经存在
		data=warehouseStockServiceImpl.checkOnlyOne(bean);
		if(data.getStatus()==0) {//该商品在该仓库中存在
			data.setMessage("该商品已经存在，请选择其他商品");
			return data;
		}
		//没有的话，就直接添加到数据库中,返回处理结果
		data=warehouseStockServiceImpl.addItemToWarehouse(bean);
		if(data.getStatus()==0) {//添加成功
			data.setStatus(1);
			data.setMessage("添加成功");
		}else {
			data.setStatus(0);
			data.setMessage("添加失败");
		}
		log.info("<WarehouseStockController>----<addItemToWarehouse>----end");
		return data;
	}
	@ApiOperation(value = "更新仓库商品信息 ", notes = "updateItemToWarehouse", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateItemToWarehouse", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateItemToWarehouse(@RequestBody(required=false)WarehouseStockBean bean) {
		log.info("<WarehouseStockController>----<updateItemToWarehouse>----start");
		ReturnDataUtil data=null;
		data=warehouseStockServiceImpl.updateItemToWarehouse(bean);
		if(data.getStatus()==0) {//更新成功
			data.setStatus(1);
			data.setMessage("更新成功");
		}else {
			data.setStatus(0);
			data.setMessage("更新失败");
		}
		log.info("<WarehouseStockController>----<updateItemToWarehouse>----end");
		return data;
	}
}
