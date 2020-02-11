package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-15 14:00:02
 */
@Service
public class WarehouseItemCheckLogServiceImpl implements WarehouseItemCheckLogService {

	private static Logger log = LogManager.getLogger(WarehouseItemCheckLogServiceImpl.class);
	@Autowired
	private WarehouseItemCheckLogDao merchandiseInventoryDaoImpl;

	/**
	 * 商品盘点列表
	 */
	public ReturnDataUtil listPage(WarehouseItemCheckLogForm warehouseItemCheckLogForm) {
		log.info("<MerchandiseInventoryServiceImpl>----<listPage>-----start");
		ReturnDataUtil listPage = new ReturnDataUtil();
		if(warehouseItemCheckLogForm.getType()==null || warehouseItemCheckLogForm.getType()==1) {
			 listPage = merchandiseInventoryDaoImpl.listPage(warehouseItemCheckLogForm);
		}else {
			 listPage = merchandiseInventoryDaoImpl.newListPage(warehouseItemCheckLogForm);
		}
		log.info("<MerchandiseInventoryServiceImpl>----<listPage>-----end");
		return listPage;
	}
	
}
