package com.server.module.system.machineManage.machineType;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;

/**
 * author name: yjr create time: 2018-03-22 13:31:26
 */
@Repository
public class MachinesTypeDaoImpl extends BaseDao<MachinesTypeBean> implements MachinesTypeDao {

	public static Logger log = LogManager.getLogger(MachinesTypeDaoImpl.class);

	public MachinesTypeBean get(Integer id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name,wayCount,state,stopDate,buyUpperLimit from machines_type where id=? ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			pst.setObject(1, id);
			rs = pst.executeQuery();
			List<MachinesTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				MachinesTypeBean bean = new MachinesTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setWayCount(rs.getInt("wayCount"));
				bean.setState(rs.getInt("state"));
				bean.setStopDate(rs.getDate("stopDate"));
				bean.setBuyUpperLimit(rs.getInt("buyUpperLimit"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql.toString());
				log.info(id);
			}
			//添加判断list是否有元素
			if(list.size()>0) {
				return list.get(0);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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

	public List listEntity(String sql, List<Object> list) {
		return super.list(sql, list);
	}

	public MachinesTypeBean getEntity(Object id) {
		return super.get(id);
	}

	public boolean delEntity(MachinesTypeBean entity) {
		return super.del(entity);
	}

	public boolean updateEntity(MachinesTypeBean entity) {
		return super.update(entity);
	}

	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(MachinesTypeCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select m.id,m.name,m.wayCount,m.state,m.stopDate,m.buyUpperLimit,s.name from machines_type m,state_info s where m.state=s.state");
		List<Object> plist = Lists.newArrayList();
		if(condition.getId()!=null) {
			sql.append(" and m.id=? ");
			plist.add(condition.getId());
		}
		if (StringUtil.isNotEmpty(condition.getName())) {
			sql.append(" and m.name=? ");
			plist.add(condition.getName());
		}
		if(condition.getWayCount() != null) {
			sql.append(" and m.wayCount=? ");
			plist.add(condition.getWayCount());
		}
		if(condition.getState() != null) {
			sql.append(" and m.state=? ");
			plist.add(condition.getState());
		}
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
			sql.append(" order by m.createTime is null, m.createTime desc ");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<MachinesTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				MachinesTypeBean bean = new MachinesTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setWayCount(rs.getInt("wayCount"));
				bean.setState(rs.getInt("state"));
				bean.setStopDate(rs.getDate("stopDate"));
				bean.setBuyUpperLimit(rs.getInt("buyUpperLimit"));
				bean.setStateName(rs.getString(7));
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

	@Override
	public List<MachinesTypeBean> listEntity(MachinesTypeCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}
    
	public MachinesTypeBean insert(MachinesTypeBean type){
		return super.insert(type);
	}
	
	public boolean del(MachinesTypeBean type){
		return super.del(type);
	}

	@Override
	public ReturnDataUtil checkOnlyOne(MachinesTypeBean entity) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from machines_type where name='"+entity.getName()+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<MachinesTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				data.setStatus(-1);
			}
			if (showSql) {
				log.info(sql);
				
			}
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

	@Override
	public MachinesTypeBean findTypeById(Long id) {
		MachinesTypeBean bean=new MachinesTypeBean();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from machines_type where id="+id);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<MachinesTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setWayCount(rs.getInt("wayCount"));
			}
			if (showSql) {
				log.info(sql);
				
			}
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
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

	@Override
	public ReturnDataUtil findAllByState() {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select m.id,m.name,m.wayCount,m.state,m.stopDate,m.buyUpperLimit,s.name from machines_type m,state_info s where m.state=s.state and m.state=2100");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<MachinesTypeBean> list = Lists.newArrayList();
			while (rs.next()) {
				MachinesTypeBean bean = new MachinesTypeBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setWayCount(rs.getInt("wayCount"));
				bean.setState(rs.getInt("state"));
				bean.setStopDate(rs.getDate("stopDate"));
				bean.setBuyUpperLimit(rs.getInt("buyUpperLimit"));
				bean.setStateName(rs.getString(7));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql.toString());
			}
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

}
