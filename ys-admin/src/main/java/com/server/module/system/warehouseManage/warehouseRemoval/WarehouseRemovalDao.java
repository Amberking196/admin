package com.server.module.system.warehouseManage.warehouseRemoval;

import java.util.List;

import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
public interface WarehouseRemovalDao {

	/**
	 * 出库单列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseRemovalForm warehouseRemovalForm);

	/**
	 * 增加出库单
	 * @param entity
	 * @return
	 */
	public WarehouseOutputBillBean insert(WarehouseOutputBillBean entity);
	
	/**
	 * 修改出库单
	 * @param entity
	 * @return
	 */
	public boolean update(WarehouseOutputBillBean entity);

	/**
	 * 根据单号 查询详情
	 * @param changeId
	 * @return
	 */
	public WarehouseOutputBillBean get(String  changeId);

	/**
	 * 生成出库单号
	 * @return
	 */
	public String findChangeId();
	
	/**
	 * 判断库存是否足够
	 * @param bean
	 * @return
	 */
	public List<WarehouseStockBean> checkQuantity(Long warehouseId ,String itemIds);
	
	
	public WarehouseOutputBillBean getBean(Object id);
}
