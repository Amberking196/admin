package com.server.module.system.warehouseManage.warehouseGiveBack;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.netflix.discovery.converters.Auto;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.stock.WarehouseStockService;
import com.server.module.system.warehouseManage.warehouseRemoval.WarehouseRemovalController;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemService;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-05-24 11:34:32
 */
@Api(value = "WarehouseGiveBackController", description = "商品归还")
@RestController
@RequestMapping("/warehouseGiveBack")
public class WarehouseGiveBackController {

	private static Logger log = LogManager.getLogger(WarehouseGiveBackController.class);
	@Autowired
	private WarehouseGiveBackService warehouseGiveBackServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private WarehouseBillItemService warehouseBillItemServiceImpl;

	@ApiOperation(value = "归还列表查询", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody WarehouseGiveBackForm warehouseGiveBackForm) {
		log.info("<WarehouseGiveBackController>-----<listPage>-----start");
		returnDataUtil=warehouseGiveBackServiceImpl.listPage(warehouseGiveBackForm);
		log.info("<WarehouseGiveBackController>-----<listPage>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "归还列表添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseGiveBackBean entity) {
		log.info("<WarehouseGiveBackController>-----<add>-----start");
		WarehouseGiveBackBean add = warehouseGiveBackServiceImpl.add(entity);
		if(add!=null) {
			/*List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(entity.getBillId().intValue());
			for (WarehouseBillItemBean warehouseBillItemBean : list) {
				if(warehouseBillItemBean.getItemId().equals(entity.getItemId())) {
				Integer quantity=warehouseBillItemBean.getQuantity()-entity.getQuantity();
				warehouseBillItemBean.setId(warehouseBillItemBean.getId());
				warehouseBillItemBean.setQuantity(quantity);
				warehouseBillItemBean.setMoney(quantity*warehouseBillItemBean.getPrice());
				warehouseBillItemBean.setRemark("本次出库后，归还了"+entity.getQuantity()+"个商品");
				warehouseBillItemServiceImpl.update(warehouseBillItemBean);
				}
			}*/
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("添加成功！");
			log.info("<WarehouseGiveBackController>------<add>-----end");
			return returnDataUtil;
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("添加失败！");
			log.info("<WarehouseGiveBackController>------<add>-----end");
			return returnDataUtil;
		}
		 
	}

	
}
