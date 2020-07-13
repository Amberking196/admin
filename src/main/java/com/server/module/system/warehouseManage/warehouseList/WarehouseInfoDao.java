package com.server.module.system.warehouseManage.warehouseList;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-14 22:06:48
 */
public interface WarehouseInfoDao {

	/**
	 * 查询仓库列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseInfoForm warehouseInfoForm);

	/**
	 * 判断仓库名称是否存在
	 * @param name
	 * @return
	 */
	public boolean checkName(String name,Long companyId);

	/**
	 * 修改仓库
	 * @param entity
	 * @return
	 */
	public boolean update(WarehouseInfoBean entity);

	/**
	 * 删除仓库
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询单个仓库
	 * @param id
	 * @return
	 */
	public WarehouseInfoBean get(Object id);

	/**
	 * 增加仓库
	 * @param entity
	 * @return
	 */
	public WarehouseInfoBean insert(WarehouseInfoBean entity);
	
	/**
	 * 查询所有状态为启用的仓库
	 * @return
	 */
	public List<WarehouseInfoBean> findWarehouseInfoBean();
	
	/**
	 * 判断当前用户是否是仓库的负责人
	 */
	public WarehouseInfoBean checkPrincipal(WarehouseInfoForm warehouseInfoForm) ;

	public List<WarehouseInfoBean> findAllWarehouseInfoBean();

	/**
	 * 根据公司Id查询仓库列表
	 * @param warehouseInfoForm
	 * @return
	 */
    ReturnDataUtil listPageByCompanyId(WarehouseInfoForm warehouseInfoForm);
    /**
     * 根据仓库管理员和公司查询查询仓库名称
     * @param warehouseInfoForm
     * @return
     */
	public ReturnDataUtil findListByForm(WarehouseInfoForm warehouseInfoForm);
}
