package com.server.module.system.warehouseManage.warehouseList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.netflix.discovery.converters.Auto;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-14 22:06:48
 */
@Repository
public class WarehouseInfoDaoImpl extends BaseDao<WarehouseInfoBean> implements WarehouseInfoDao {

	public static Logger log = LogManager.getLogger(WarehouseInfoDaoImpl.class); 

	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 仓库查询列表
	 */
	public ReturnDataUtil listPage(WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoDaoImpl>-----<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select wi.id,wi.companyId,wi.principal,wi.name,wi.phone,wi.state,wi.createTime,wi.location,c.name companyName,si.name stateName,l.name principalName ");
		sql.append(" from warehouse_info wi left join company c on wi.companyId=c.id "); 
		sql.append(" left join state_info si on wi.state=si.state left join login_info l on wi.principal=l.id ");
		sql.append(" where wi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		
		sql.append(" and delflag= 0 ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("仓库列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" order by createTime desc ");//已修改
			long off = (warehouseInfoForm.getCurrentPage() - 1) * warehouseInfoForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseInfoForm.getPageSize());
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<WarehouseInfoBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseInfoBean bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setStateName(rs.getString("stateName"));
				bean.setPrincipalName(rs.getString("principalName"));
				int state = rs.getInt("state");
				if(state==60002) {
					bean.setIsShow(0);
				}else {
					bean.setIsShow(1);
				}
				
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(warehouseInfoForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseInfoDaoImpl>-----<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil listPageByCompanyId(WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoDaoImpl>-----<listPageByCompanyId>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select wi.id,wi.companyId,wi.principal,wi.name,wi.phone,wi.state,wi.createTime,wi.location,c.name companyName,si.name stateName,l.name principalName ");
		sql.append(" from warehouse_info wi left join company c on wi.companyId=c.id ");
		sql.append(" left join state_info si on wi.state=si.state left join login_info l on wi.principal=l.id ");
		sql.append(" where wi.companyId = " + warehouseInfoForm.getCompanyId() + "");
		sql.append(" and delflag= 0 and wi.state = 60001");
		sql.append(" order by wi.state ,createTime desc ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("仓库列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseInfoForm.getCurrentPage() - 1) * warehouseInfoForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseInfoForm.getPageSize());
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<WarehouseInfoBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseInfoBean bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setStateName(rs.getString("stateName"));
				bean.setPrincipalName(rs.getString("principalName"));
				int state = rs.getInt("state");
				if(state==60002) {
					bean.setIsShow(0);
				}else {
					bean.setIsShow(1);
				}

				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(warehouseInfoForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseInfoDaoImpl>-----<listPageByCompanyId>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}


	/**
	 * 查询单个仓库信息
	 */
	public WarehouseInfoBean get(Object id) {
		log.info("<WarehouseInfoDaoImpl>-----<get>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select id,companyId,principal,name,phone,state,createTime,location from warehouse_info where id='"+id+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询单个仓库sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			WarehouseInfoBean bean=null;
			while(rs.next()) {
				bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
			}
			log.info("<WarehouseInfoDaoImpl>-----<get>-----start");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}

	/**
	 *删除单个仓库
	 */
	public boolean delete(Object id) {
		log.info("<WarehouseInfoDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  warehouse_info set delflag= 1 where id in ("+id+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除仓库sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<WarehouseInfoDaoImpl>-----<delete>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	/**
	 * 修改仓库
	 */
	public boolean update(WarehouseInfoBean entity) {
		return super.update(entity);
	}

	/**
	 * 增加仓库
	 */
	public WarehouseInfoBean insert(WarehouseInfoBean entity) {
		return super.insert(entity);
	}

	/**
	 * 校验仓库名称是否存在
	 */
	@Override
	public boolean checkName(String name,Long companyId) {
		log.info("<WarehouseInfoDaoImpl>-----<checkName>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select name from warehouse_info where companyId='"+companyId+"'  and name='"+name+"' and delFlag=0");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("仓库判断sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			if (rs.next() && rs!=null) {
				log.info("<WarehouseInfoDaoImpl>-----<checkName>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	/**
	 * 查询所有状态为启用的仓库
	 */
	@Override
	public List<WarehouseInfoBean> findWarehouseInfoBean() {
		log.info("<WarehouseInfoDaoImpl>-----<findWarehouseInfoBean>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select id,companyId,principal,name,phone,state,createTime,location from warehouse_info where state=60001 ");
		sql.append(" and  companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseInfoBean> list = Lists.newArrayList();
		log.info("询所有状态为启用的仓库sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next()) {
				WarehouseInfoBean	bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			log.info("<WarehouseInfoDaoImpl>-----<findWarehouseInfoBean>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}

	public List<WarehouseInfoBean> findAllWarehouseInfoBean() {
		log.info("<WarehouseInfoDaoImpl>-----<findWarehouseInfoBean>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select id,companyId,principal,name,phone,state,createTime,location from warehouse_info where state=60001 ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseInfoBean> list = Lists.newArrayList();
		log.info("询所有状态为启用的仓库sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next()) {
				WarehouseInfoBean	bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			log.info("<WarehouseInfoDaoImpl>-----<findWarehouseInfoBean>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}



	/**
	 * 判断当前用户是否是仓库的负责人
	 */
	@Override
	public WarehouseInfoBean checkPrincipal(WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoDaoImpl>-----<checkPrincipal>-----start");
		StringBuilder sql = new StringBuilder();
		if(warehouseInfoForm.getPrincipal()!=null) {
			sql.append("select id,companyId,principal,name,phone,state,createTime,location from warehouse_info where principal='"+warehouseInfoForm.getPrincipal()+"' and  delflag= 0 ");
		}else {
		sql.append("select id,companyId,principal,name,phone,state,createTime,location from warehouse_info where principal='"+UserUtils.getUser().getId()+"' ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		WarehouseInfoBean bean=null;
		log.info("判断当前用户是否是仓库的负责人sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while(rs.next()&& rs!=null) {
				bean=new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
			}
			log.info("<WarehouseInfoDaoImpl>-----<checkPrincipal>-----end");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return bean;
	}

	@Override
	public ReturnDataUtil findListByForm(WarehouseInfoForm warehouseInfoForm) {
		log.info("<WarehouseInfoDaoImpl>-----<findListByForm>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select wi.id,wi.companyId,wi.principal,wi.name,wi.phone,wi.state,wi.createTime,wi.location,c.name companyName,si.name stateName,l.name principalName ");
		sql.append(" from warehouse_info wi left join company c on wi.companyId=c.id ");
		sql.append(" left join state_info si on wi.state=si.state left join login_info l on wi.principal=l.id ");
		sql.append(" left join warehouse_admin wa on wi.id=wa.warehouseInfoId");
		sql.append(" where wi.companyId = " + warehouseInfoForm.getCompanyId() + "");
		sql.append(" and delflag= 0 and wi.state = 60001");
		sql.append(" and wa.userId="+warehouseInfoForm.getUserId() );
		sql.append(" order by wi.state ,createTime desc ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("仓库列表sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (warehouseInfoForm.getCurrentPage() - 1) * warehouseInfoForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + warehouseInfoForm.getPageSize());
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<WarehouseInfoBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseInfoBean bean = new WarehouseInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setPrincipal(rs.getInt("principal"));
				bean.setName(rs.getString("name"));
				bean.setLocation(rs.getString("location"));
				bean.setPhone(rs.getString("phone"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setStateName(rs.getString("stateName"));
				bean.setPrincipalName(rs.getString("principalName"));
				int state = rs.getInt("state");
				if(state==60002) {
					bean.setIsShow(0);
				}else {
					bean.setIsShow(1);
				}

				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(warehouseInfoForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<WarehouseInfoDaoImpl>-----<findListByForm>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	
}
