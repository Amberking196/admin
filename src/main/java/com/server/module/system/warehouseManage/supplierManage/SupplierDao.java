package com.server.module.system.warehouseManage.supplierManage;

import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
public interface  SupplierDao{
	/**
	 * 查询用户商列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(SupplierForm supplierForm);
	/**
	 * 更改供应商
	 * @param bean
	 * @return
	 */
	public boolean update(SupplierBean entity);
	/**
	 * 删除供应商
	 * @param bean
	 * @return
	 */
	public boolean delete(Object id);
	/**
	 * 查询单个供应商
	 * @param bean
	 * @return
	 */
	public SupplierBean get(Object id);
	/**
	 * 新增供应商
	 * @param entity
	 * @return
	 */
	public SupplierBean insert(SupplierBean entity);
	
	/**
	 * 供应商名字模糊搜索
	 * @return
	 */
	public List<SupplierBean> findBean(String name);

	public List<SupplierVoForSelect> listForSelect();
	/**
	 * 查询全部供应商
	 * @return
	 */
	public ReturnDataUtil findAll();
}

