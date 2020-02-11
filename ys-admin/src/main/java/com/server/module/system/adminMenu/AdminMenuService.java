package com.server.module.system.adminMenu;

import java.util.List;
import java.util.Set;

public interface AdminMenuService {

	
	/**
	 * 获取系统菜单列表
	 * @return 菜单列表
	 */
    List<AdminMenuBean> getMenuList();
    
	/**
	 * 获取树形结构系统菜单列表
	 * @return 菜单列表
	 */
    List<AdminMenuBean> formatGetMenuList();
    
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
	Set<AdminMenuBean> findMenuByRole(String role);
	/**
	 * 以树形结构返回当前角色的菜单权限
	 * @param role
	 * @return
	 */
	List<AdminMenuBean> formatFindMenuByRole(String role);
	
	/**
	 * 根据父id获取子菜单
	 * 
	 * @param pid（父id）
	 * @return 子菜单集合List<AdminMenuBean>
	 */
	List<AdminMenuBean> findMenuByPid(Integer pid) ;
	
	/**
	 * 将结果转换为树形结构
	 * @param menuList
	 * @return
	 */
	List<AdminMenuBean> formatTreeMenu(List<AdminMenuBean> menuList);
}
