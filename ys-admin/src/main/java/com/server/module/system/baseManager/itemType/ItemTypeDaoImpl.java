package com.server.module.system.baseManager.itemType;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: yjr create time: 2018-04-10 14:28:15
 */
@Repository
public class ItemTypeDaoImpl extends BaseDao<ItemTypeBean> implements ItemTypeDao {


	public static Logger log = LogManager.getLogger(ItemTypeDaoImpl.class);  

	public ReturnDataUtil listPage(ItemTypeCondition condition) {
		log.info("ItemTypeDaoImpl---------listPage------ start"); 
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,pid,name,state,createTime,updateTime from item_type where 1=1 ");
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
			List<ItemTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				ItemTypeBean bean = new ItemTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setPid(rs.getLong("pid"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
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
			log.info("ItemTypeDaoImpl---------listPage------ end"); 
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pst!=null) {
					pst.close();
				}
				if(conn!=null) {
					closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ItemTypeBean get(Object id) {
		log.info("ItemTypeDaoImpl---------get------ start"); 
		ItemTypeBean re=super.get(id);
		log.info("ItemTypeDaoImpl---------get------ end"); 
		return re;
	}

	public boolean delete(Object id) {
		log.info("ItemTypeDaoImpl---------delete------ start"); 
		ItemTypeBean entity = new ItemTypeBean();
		boolean re= super.del(entity);
		log.info("ItemTypeDaoImpl---------delete------ start"); 
		return re;
	}

	public boolean update(ItemTypeBean entity) {
		log.info("ItemTypeDaoImpl---------update------ start"); 
		boolean re= super.update(entity);
		log.info("ItemTypeDaoImpl---------update------ end"); 
		return re;
	}

	public ItemTypeBean insert(ItemTypeBean entity) {
		log.info("ItemTypeDaoImpl---------insert------ start"); 
		ItemTypeBean re= super.insert(entity);
		log.info("ItemTypeDaoImpl---------insert------ end"); 
		return re;
	}

	public List<ItemTypeBean> list(ItemTypeCondition condition) {
		log.info("ItemTypeDaoImpl---------list------ start"); 
		StringBuilder sql = new StringBuilder();
		List<ItemTypeBean> list = Lists.newArrayList();
		sql.append("select id,pid,name,state,createTime,updateTime from item_type where 1=1 ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ItemTypeBean bean = new ItemTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setPid(rs.getLong("pid"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("ItemTypeDaoImpl---------list------ end"); 
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("ItemTypeDaoImpl---------list------ end"); 
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 查询商品类型 id
	 */
	@Override
	public Long getItemTypeId(String name) {
		String sql="select id from item_type where name='"+name+"' ";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			rs = pst.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
