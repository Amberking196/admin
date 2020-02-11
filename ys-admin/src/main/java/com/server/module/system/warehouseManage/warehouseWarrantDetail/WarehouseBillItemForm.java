package com.server.module.system.warehouseManage.warehouseWarrantDetail;
import com.server.module.commonBean.PageAssist;
import lombok.Data;
@Data
public class WarehouseBillItemForm extends PageAssist {
	
	private String companyId;
	private String warehouseId;
	private String itemId;
	
	private String startTime;
	private String endTime;
	private String output;//0 进  1 出
	
	private String number;
	

}
