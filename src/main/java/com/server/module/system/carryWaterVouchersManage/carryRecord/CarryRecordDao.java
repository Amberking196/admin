package com.server.module.system.carryWaterVouchersManage.carryRecord;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface CarryRecordDao {

	/**
	 * 查询订单的提水券使用情况
	 * @author hjc
	 * @date 2018年11月9日上午9:42:45
	 * @param carryRecordForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryRecordForm carryRecordForm);
}
