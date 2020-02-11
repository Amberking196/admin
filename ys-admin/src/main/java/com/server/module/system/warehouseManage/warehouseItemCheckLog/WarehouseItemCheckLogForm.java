package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * author name: why create time: 2018-06-15 14:00:02
 */

@Data
public class WarehouseItemCheckLogForm extends PageAssist {

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
	
	Integer type;// 1旧版 2新版

	//仓库ID
	 Long warehouseId;
}
