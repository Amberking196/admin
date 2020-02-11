package com.server.module.system.adminMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository(value = "AdminMenuDaoImp")
public class AdminMenuDaoImp extends MySqlFuns implements AdminMenuDao {

	public static Logger log = LogManager.getLogger(AdminMenuDaoImp.class);   

	/**
	 * 获取系统菜单列表
	 * 
	 * @return 菜单列表
	 */
	public List<AdminMenuBean> getMenuList() {
		log.info("<AdminMenuDaoImp>--<getMenuList>--start");
		String sql = "select id,pid,name,url,isShow from menu where oldHidden = 1";
		log.info("<AdminMenuDaoImp>--<getMenuList>--sql:" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AdminMenuBean> menuList = new ArrayList<AdminMenuBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				AdminMenuBean menuBean = new AdminMenuBean();
				menuBean.setId(rs.getInt("id"));
				menuBean.setPid(rs.getInt("pid"));
				menuBean.setName(rs.getString("name"));
				menuBean.setUrl(rs.getString("url"));
				menuBean.setIsShow(rs.getInt("isShow"));
				menuList.add(menuBean);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AdminMenuDaoImp>--<getMenuList>--end");
		return menuList;
	}

	/**
	 * 根据父id获取子菜单
	 * 
	 * @param pid（父id）
	 * @return 子菜单集合List<AdminMenuBean>
	 */
	public List<AdminMenuBean> findMenuByPid(Integer pid) {
		log.info("<AdminMenuDaoImp>--<findMenuByPid>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,pid,name,url,isShow from menu where oldHidden = 1 and pid=" + pid);
		log.info("<AdminMenuDaoImp>--<findMenuByPid>--sql:"+ sql); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AdminMenuBean> menuList = new ArrayList<AdminMenuBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				AdminMenuBean menuBean = new AdminMenuBean();
				menuBean.setId(rs.getInt("id"));
				menuBean.setPid(rs.getInt("pid"));
				menuBean.setName(rs.getString("name"));
				menuBean.setUrl(rs.getString("url"));
				menuBean.setIsShow(rs.getInt("isShow"));
				menuList.add(menuBean);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AdminMenuDaoImp>--<findMenuByPid>--end");
		return menuList;
	}

	/**
	 * 更新系统菜单
	 * 
	 * @param menu
	 * @return
	 */
	public List<AdminMenuBean> updateMenu(AdminMenuBean menu) {
		log.info("<AdminMenuDaoImp>--<updateMenu>--start");
		List<AdminMenuBean> relist = new ArrayList<AdminMenuBean>();
//		String sql = "update menu set pid='" + menu.getPid() + "',name='" + menu.getName() + "' ,url='" + menu.getUrl()
//				+ "' where oldHidden = 1 and id='" + menu.getId() + "'";
		String sql = "update menu set pid=?,name=?,url=? where oldHidden=1 and id=?";
		List<Object> param = new ArrayList<Object>();
		param.add(menu.getPid());
		param.add(menu.getName());
		param.add(menu.getUrl());
		param.add(menu.getId());
		int updateFlag = upate(sql,param);
		if (updateFlag != 0) {
			relist = getMenuList();
		}
		log.info("<AdminMenuDaoImp>--<updateMenu>--end");
		return relist;

	}

	/**
	 * 添加菜单
	 * 
	 * @param menu
	 * @return
	 */
	public List<AdminMenuBean> addMenu(AdminMenuBean menu) {
		log.info("<AdminMenuDaoImp>--<addMenu>--start");
		List<AdminMenuBean> relist = new ArrayList<AdminMenuBean>();
//		String sql = "insert into admin_menu(parentId,menuName,menuPath,oldHidden)" + "value('" + menu.getPid() + "','"
//				+ menu.getName() + "','" + menu.getUrl() + "',"+1+")";
		String sql = "insert into menu(pid,name,url,isShow,oldHidden) values(?,?,?,?,?)";
		List<Object> param = new ArrayList<Object>();
		param.add(menu.getPid());
		param.add(menu.getName());
		param.add(menu.getUrl());
		param.add(menu.getIsShow());
		param.add(1);
		int id = insert(sql,param);
		log.info("<AdminMenuDaoImp>--<addMenu>--end");
		if (id != 0) {
			relist = getMenuList();
		}
		return relist;

	}

	/**
	 * 根据角色来获取当前角色的菜单权限
	 * @param role
	 * @return
	 */
	public Set<AdminMenuBean> findMenuByMenuId(String menuId) {
		log.info("<AdminMenuDaoImp>--<findMenuByMenuId>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT m.id,m.pid,m.name,m.url,m.isShow,m.wordName FROM menu AS m");
		sql.append(" where oldHidden = 1 and m.id in (" + menuId + ") order by id asc");
		log.info("AdminMenuDaoImp---------findMenuByMenuId------ sql:"+sql);  
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<AdminMenuBean> menuList = new TreeSet<AdminMenuBean>(new Comparator<AdminMenuBean>(){
			@Override
			public int compare(AdminMenuBean a, AdminMenuBean b) {
				return a.getId()-b.getId();
			}
		});
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				AdminMenuBean menuBean = new AdminMenuBean();
				menuBean.setId(rs.getInt("id"));
				menuBean.setPid(rs.getInt("pid"));
				menuBean.setName(rs.getString("name"));
				menuBean.setUrl(rs.getString("url"));
				menuBean.setIsShow(rs.getInt("isShow"));
				menuBean.setWordName(rs.getString("wordName"));
				menuList.add(menuBean);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AdminMenuDaoImp>--<findMenuByMenuId>--end");
		return menuList;
	}
	
	public List<String> findRoleMenu(String role){
		log.info("<AdminMenuDaoImp>--<findRoleMenu>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT privilege FROM role");
		sql.append(" where id in (" + role + ")");
		log.info("<AdminMenuDaoImp>--<findRoleMenu>--sql:"+sql); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> privilegeList = new ArrayList<String>();
		String privilege = "";
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				privilege = rs.getString("privilege");
				privilegeList.add(privilege);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AdminMenuDaoImp>--<findRoleMenu>--end");
		return privilegeList;
	}
}
