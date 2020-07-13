package com.server.module.system.adminMenu;

import java.util.List;
import java.util.Set;

public interface AdminMenuDao {

	
	/**
	 * 获取系统菜单列表
	 * @return 菜单列表
	 */
    List<AdminMenuBean> getMenuList();
    
    /**
     * 更新系统菜单
     * @param menu
     * @return
     */
    List<AdminMenuBean> updateMenu(AdminMenuBean menu);
    
    /**
     * 添加菜单
     * @param menu
     * @return
     */
    List<AdminMenuBean> addMenu(AdminMenuBean menu);
    
	/**
	 * 根据角色来获取当前角色的菜单权限
	 * @param role
	 * @return
	 */
	Set<AdminMenuBean> findMenuByMenuId(String menuId);
	
	/**
	 * 根据父id获取子菜单
	 * @param pid（父id）
	 * @return 子菜单集合List<AdminMenuBean>
	 */
	List<AdminMenuBean> findMenuByPid(Integer pid) ;
	/**
	 * 根据角色获取菜单id
	 * @author hebiting
	 * @date 2018年4月10日上午11:54:44
	 * @param role
	 * @return
	 */
	List<String> findRoleMenu(String role);
}
