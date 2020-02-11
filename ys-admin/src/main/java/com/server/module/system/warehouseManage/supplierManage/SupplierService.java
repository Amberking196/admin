package com.server.module.system.warehouseManage.supplierManage;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
public interface  SupplierService{

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
	public boolean del(Object id);
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
	public SupplierBean add(SupplierBean entity);
	
	/**
	 * 模糊搜索供应商
	 * @param name
	 * @return
	 */
	public List<SupplierBean> findBean(String name);


	public List<SupplierVoForSelect> listForSelect();
	/**
	 * 查询全部
	 * @return
	 */
	public ReturnDataUtil findAll();
}


