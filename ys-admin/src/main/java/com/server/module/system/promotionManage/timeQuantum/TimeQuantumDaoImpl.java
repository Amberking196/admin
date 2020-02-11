package com.server.module.system.promotionManage.timeQuantum;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-08-22 10:56:46
 */
@Repository
public class TimeQuantumDaoImpl extends BaseDao<TimeQuantumBean> implements TimeQuantumDao {

	private static Logger log = LogManager.getLogger(TimeQuantumDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;

	/**
	 * 时间段列表查询
	 */
	public ReturnDataUtil listPage(TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumDaoImpl>---<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select t.id,t.companyId,timeSlot,rebate,t.createTime,t.createUser,t.updateTime,t.updateUser,t.deleteFlag,c.name from time_quantum t ");
		sql.append(" left join company c  on t.companyId=c.id where t.deleteFlag=0 ");
		if(timeQuantumForm.getCompanyId()==null) {
			sql.append(" and t.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		}else {
			sql.append(" and t.companyId ='"+timeQuantumForm.getCompanyId()+"' ");
		}
		sql.append(" order by createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("时间段列表查询 SQL语句 "+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(timeQuantumForm.getIsShowAll()==1) {
				pst = conn.prepareStatement(sql.toString());
			}else {
				long off = (timeQuantumForm.getCurrentPage() - 1) * timeQuantumForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + timeQuantumForm.getPageSize());
			}
			rs = pst.executeQuery();
			List<TimeQuantumBean> list = Lists.newArrayList();
			int number=0;
			while (rs.next()) {
				number++;
				TimeQuantumBean bean = new TimeQuantumBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTimeSlot(rs.getString("timeSlot"));
				bean.setRebate(rs.getDouble("rebate"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCompanyName(rs.getString("name"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(timeQuantumForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<TimeQuantumDaoImpl>---<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TimeQuantumDaoImpl>---<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public TimeQuantumBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 时间段 删除
	 */
	public boolean delete(Object id) {
		log.info("<TimeQuantumDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  time_quantum set deleteFlag= 1 where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除时间段sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<TimeQuantumDaoImpl>-----<delete>-----end");
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
	 * 时间段列表修改
	 */
	public boolean update(TimeQuantumBean entity) {
		log.info("<TimeQuantumDaoImpl>----<update>------start");
		boolean update = super.update(entity);
		log.info("<TimeQuantumDaoImpl>----<update>------end");
		return update;
	}

	/**
	 * 时间段增加
	 */
	public TimeQuantumBean insert(TimeQuantumBean entity) {
		log.info("<TimeQuantumDaoImpl>----<insert>------start");
		TimeQuantumBean timeQuantumBean = super.insert(entity);
		log.info("<TimeQuantumDaoImpl>----<insert>------end");
		return timeQuantumBean;
	}

	/**
	 * 根据时间段id 查询时间段
	 */
	public List<TimeQuantumBean> list(String ids) {
		log.info("<TimeQuantumDaoImpl>----<list>------start");
		List<TimeQuantumBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,companyId,timeSlot,rebate,createTime,createUser,updateTime,updateUser,deleteFlag from time_quantum  ");
		sql.append("  where id in ("+ids+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据时间段id 查询时间段 SQL语句 "+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				TimeQuantumBean bean = new TimeQuantumBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTimeSlot(rs.getString("timeSlot"));
				bean.setRebate(rs.getDouble("rebate"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<TimeQuantumDaoImpl>---<list>------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TimeQuantumDaoImpl>---<list>------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 条件查询时间段信息
	 */
	@Override
	public List<TimeQuantumBean> findBean(TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumDaoImpl>---<findBean>------start");
		List<TimeQuantumBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id,t.companyId,timeSlot,rebate,t.createTime,t.createUser,t.updateTime,t.updateUser,t.deleteFlag ");
		sql.append(" from time_quantum t where t.deleteFlag=0 ");
		if(timeQuantumForm.getCompanyId()!=null) {
			sql.append(" and t.companyId='"+timeQuantumForm.getCompanyId()+"' ");
		}
		if(StringUtil.isNotBlank(timeQuantumForm.getVmCode())) {
			sql.append(" and  t.companyId in ( select companyId from vending_machines_info where code='"+timeQuantumForm.getVmCode()+"') ");
		}
		sql.append(" order by t.createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("条件查询时间段信息 SQL语句 "+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				TimeQuantumBean bean = new TimeQuantumBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTimeSlot(rs.getString("timeSlot"));
				bean.setRebate(rs.getDouble("rebate"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<TimeQuantumDaoImpl>---<findBean>------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TimeQuantumDaoImpl>---<findBean>------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
