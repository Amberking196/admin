package com.server.module.system.warehouseManage.warehouseAdmin;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: yjr create time: 2018-09-03 14:23:48
 */
@Repository
public class WarehouseAdminDaoImpl extends BaseDao<WarehouseAdminBean> implements WarehouseAdminDao {

	private static Log log = LogFactory.getLog(WarehouseAdminDaoImpl.class);

	public ReturnDataUtil listPage(WarehouseAdminCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,warehouseInfoId,userId,userName,adminType,createTime from warehouse_admin where 1=1 ");
		if(condition.getWarehouseInfoId()!=null){
			sql.append(" and warehouseInfoId="+condition.getWarehouseInfoId());
		}
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
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
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<WarehouseAdminBean> list = Lists.newArrayList();
			while (rs.next()) {
				WarehouseAdminBean bean = new WarehouseAdminBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseInfoId(rs.getLong("warehouseInfoId"));
				bean.setUserId(rs.getString("userId"));
				bean.setUserName(rs.getString("userName"));
				bean.setAdminType(rs.getInt("adminType"));
				//bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public WarehouseAdminBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Long id) {
		WarehouseAdminBean entity = new WarehouseAdminBean();
		entity.setId(id);
		return super.del(entity);
	}

	public boolean update(WarehouseAdminBean entity) {
		return super.update(entity);
	}

	public WarehouseAdminBean insert(WarehouseAdminBean entity) {
		return super.insert(entity);
	}

	public List<WarehouseAdminBean> list(WarehouseAdminCondition condition) {
		return null;
	}
	
	public List<WarehouseAdminBean> listByWarehouseId(Long warehouseInfoId) {

		//Long userId=UserUtils.getUser().getId();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,warehouseInfoId,userId,userName,adminType,warehouseName,createTime from warehouse_admin where warehouseInfoId="+warehouseInfoId);
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseAdminBean> list = Lists.newArrayList();

		try {
			conn = openConnection();
			
			pst = conn.prepareStatement(sql.toString());

			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseAdminBean bean = new WarehouseAdminBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseInfoId(rs.getLong("warehouseInfoId"));
				bean.setUserId(rs.getString("userId"));
				bean.setUserName(rs.getString("userName"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setAdminType(rs.getInt("adminType"));
				//bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public List<WarehouseAdminBean> listCurrUserWarehouse() {

		Long userId=UserUtils.getUser().getId();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,warehouseInfoId,userId,userName,warehouseName,adminType,createTime from warehouse_admin where userId="+userId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WarehouseAdminBean> list = Lists.newArrayList();

		try {
			conn = openConnection();
			
			pst = conn.prepareStatement(sql.toString());

			rs = pst.executeQuery();
			while (rs.next()) {
				WarehouseAdminBean bean = new WarehouseAdminBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseInfoId(rs.getLong("warehouseInfoId"));
				bean.setUserId(rs.getString("userId"));
				bean.setUserName(rs.getString("userName"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setAdminType(rs.getInt("adminType"));
				//bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	
}
