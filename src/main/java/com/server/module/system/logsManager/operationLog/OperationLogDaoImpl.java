package com.server.module.system.logsManager.operationLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-05-07 13:49:31
 */
@Repository
public class OperationLogDaoImpl extends BaseDao<OperationsManagementLogBean> implements OperationLogDao {
 	
	public static Logger log = LogManager.getLogger(OperationLogDaoImpl.class); 
	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 操作日志 查询
	 */
	public ReturnDataUtil listPage(OperationLogCondition condition) {
		log.info("<OperationLogDaoImpl>-----<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,userName,vmCode,content,operationTime from operations_management_log ");
		sql.append(" where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if(StringUtil.isNotBlank(condition.getUserName())) {
			sql.append(" and userName like '%"+condition.getUserName()+"%' ");
		}
		if(StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and vmCode like '%"+condition.getVmCode()+"%' ");
		}
		sql.append(" order by operationTime  desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("操作日志sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<OperationsManagementLogBean> list = Lists.newArrayList();
			Long id=0L;
			while (rs.next()) {
				id++;
				OperationsManagementLogBean bean = new OperationsManagementLogBean();
				bean.setId(id);
				bean.setUserName(rs.getString("userName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setContent(rs.getString("content"));
				String time = rs.getString("operationTime");
				time=time.substring(0, time.length()-2);
				bean.setOperationTime(time);
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OperationLogDaoImpl>-----<listPage>-----start");
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
	 * 增加 运营管理操作日志
	 */
	@Override
	public OperationsManagementLogBean insert(OperationsManagementLogBean entity) {
		// TODO Auto-generated method stub
		return super.insert(entity);
	}

	@Override
	public ReturnDataUtil priceLogListPage(PriceLogCondition condition) {
		log.info("<OperationLogDaoImpl>-----<priceLogListPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,userName,vmCode,content,operationTime from machines_price_log ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(condition.getCompanyIds())) {
			sql.append(" and companyId in(" + condition.getCompanyIds() + ")");
		}
		if(StringUtil.isNotBlank(condition.getUserName())) {
			sql.append(" and userName like '%"+condition.getUserName()+"%' ");
		}
		if(StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and vmCode like '%"+condition.getVmCode()+"%' ");
		}
		sql.append(" order by operationTime  desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("价格修改操作日志sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<PriceLogBean> list = Lists.newArrayList();
			Long id=0L;
			while (rs.next()) {
				id++;
				PriceLogBean bean = new PriceLogBean();
				bean.setId(id);
				bean.setUserName(rs.getString("userName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setContent(rs.getString("content"));
				String time = rs.getString("operationTime");
				time=time.substring(0, time.length()-2);
				bean.setOperationTime(time);
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OperationLogDaoImpl>-----<priceLogListPage>-----start");
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
