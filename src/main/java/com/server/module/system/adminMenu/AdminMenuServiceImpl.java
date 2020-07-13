package com.server.module.system.adminMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AdminMenuServiceImpl implements AdminMenuService{
	
	public static Logger log = LogManager.getLogger(AdminMenuServiceImpl.class); 	    
	@Autowired
	AdminMenuDao adminMenuDao;
	@Override
	public List<AdminMenuBean> getMenuList() { 
		log.info("<AdminMenuServiceImpl>--<getMenuList>--start");
		List<AdminMenuBean> relist=adminMenuDao.getMenuList();
		log.info("<AdminMenuServiceImpl>--<getMenuList>--end");
		return relist;
	}
	@Override
	public List<AdminMenuBean> formatGetMenuList(){
		log.info("<AdminMenuServiceImpl>--<formatGetMenuList>--start");
		List<AdminMenuBean> menuList = adminMenuDao.getMenuList();
		log.info("<AdminMenuServiceImpl>--<formatGetMenuList>--end");
		return formatTreeMenu(menuList);
	}
	
	@Override
	public List<AdminMenuBean> updateMenu(AdminMenuBean menu) { 
		log.info("<AdminMenuServiceImpl>--<updateMenu>--start");
		List<AdminMenuBean> relist=adminMenuDao.updateMenu(menu);
		log.info("<AdminMenuServiceImpl>--<updateMenu>--end");
		return relist;
	}

	@Override
	public List<AdminMenuBean> addMenu(AdminMenuBean menu) {
		log.info("<AdminMenuServiceImpl>--<addMenu>--start");
		List<AdminMenuBean> relist= adminMenuDao.addMenu(menu);
		log.info("<AdminMenuServiceImpl>--<addMenu>--end");
		return relist;
	}

	@Override
	public Set<AdminMenuBean> findMenuByRole(String role) {
		log.info("<AdminMenuServiceImpl>--<findMenuByRole>--start");
		List<String> privilegeList = adminMenuDao.findRoleMenu(role);
		Set<String> menuIdSet = new HashSet<String>();
		for (String privilege : privilegeList) {
			String[] menuIds = StringUtils.split(privilege,",");
			Collections.addAll(menuIdSet,menuIds);
		}
		String menuId = StringUtils.join(menuIdSet,",");
		Set<AdminMenuBean> menuSet = adminMenuDao.findMenuByMenuId(menuId);
		log.info("<AdminMenuServiceImpl>--<findMenuByRole>--end");
		return menuSet;
	}

	@Override
	public List<AdminMenuBean> findMenuByPid(Integer pid) {
		return adminMenuDao.findMenuByPid(pid);
	}
	@Override
	public List<AdminMenuBean> formatFindMenuByRole(String role){
		if(role == null){
			return null;
		}
		List<String> privilegeList = adminMenuDao.findRoleMenu(role);
		Set<String> menuIdSet = new HashSet<String>();
		for (String privilege : privilegeList) {
			String[] menuIds = StringUtils.split(privilege,",");
			Collections.addAll(menuIdSet,menuIds);
		}
		String menuId = StringUtils.join(menuIdSet,",");
		Set<AdminMenuBean> menuSet = adminMenuDao.findMenuByMenuId(menuId);
		List<AdminMenuBean> menuList = new ArrayList<AdminMenuBean>(menuSet);
		return formatTreeMenu(menuList);
	}
	@Override
	public List<AdminMenuBean> formatTreeMenu(List<AdminMenuBean> menuList){
		if(menuList!=null && menuList.size()>0){
			for (AdminMenuBean adminMenuBean : menuList) {
				if(adminMenuBean.getPid()!=0){
					for (AdminMenuBean menu : menuList) {
						if(menu.getId().equals(adminMenuBean.getPid())){
							if(menu.getSonMenu()==null){
								menu.setSonMenu(new ArrayList<AdminMenuBean>());
							}
							menu.getSonMenu().add(adminMenuBean);
						}
					}
				}
			}
			Iterator<AdminMenuBean> iterator = menuList.iterator();
			while(iterator.hasNext()){
				AdminMenuBean adminMenuBean = iterator.next();
				if(adminMenuBean.getPid()!=0){
					iterator.remove();
				}
			}
		}
		return menuList;
	}
	
}
