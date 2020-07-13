package com.server.module.system.warehouseManage.warehouseWarrantDetail;

import java.util.List;

import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-17 03:15:57
 */
public interface WarehouseBillItemService {

	public boolean update(WarehouseBillItemBean entity);

	public List<WarehouseBillItemBean> get(int  billId);
	public List<WarehouseBillItemBean> getByBillId(int  billId);

	public WarehouseBillItemBean add(WarehouseBillItemBean entity);
	
	public boolean delete(Object id);
	
	/**
	 * 出入库 编辑后保存 审核
	 * @param bean
	 */
	public void modification(WarehouseOutputBillBean bean);
	
	public ReturnDataUtil listPage(WarehouseBillItemForm condition);
}
