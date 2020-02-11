package com.server.module.system.tblManager.tblRateShortsReport;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * author name: yjr create time: 2018-03-28 08:58:56
 */
@Repository
public class TblRateShortsReportDaoImpl extends BaseDao<TblRateShortsReportBean> implements TblRateShortsReportDao {

	public static Logger log = LogManager.getLogger(TblRateShortsReportDaoImpl.class);
	//getListByHQL("from RateShortsReport where reportDate 
	//between ? and ? and companyId in ("+c.getCompanyIds()+") order by companyId asc,reportDate asc",
	@Autowired
	private CompanyDao companyDaoImpl;	
	@SuppressWarnings("resource")
	public List<TblRateShortsReportBean> list(TblRateShortsReportCondition condition) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,vmCode,rateShorts,address,name,reportDate,companyName,companyId from tbl_rate_shorts_report where 1=1 ");
		if (condition.getCompanyId()!=null && condition.getCompanyId()>1){
			sql.append(" and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId()));
		}
		sql.append(" and reportDate between ? and ? order by companyId asc,reportDate asc");
		List<Object> plist = Lists.newArrayList();
		plist.add(condition.getStartTime());
		plist.add(condition.getEndTime());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			/*pst = conn.prepareStatement(super.countSql(sql.toString()));
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
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());*/
			pst = conn.prepareStatement(sql.toString());
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<TblRateShortsReportBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblRateShortsReportBean bean = new TblRateShortsReportBean();
				bean.setId(rs.getLong("id"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setRateShorts(rs.getFloat("rateShorts"));
				bean.setAddress(rs.getString("address"));
				bean.setName(rs.getString("name"));
				bean.setReportDate(rs.getDate("reportDate"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setCompanyId(rs.getLong("companyId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Lists.newArrayList();
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

	public TblRateShortsReportBean getEntity(Object id) {
		return super.get(id);
	}

	public boolean delEntity(TblRateShortsReportBean entity) {
		return super.del(entity);
	}

	public boolean updateEntity(TblRateShortsReportBean entity) {
		return super.update(entity);
	}

	@Override
	public List<TblRateShortsReportBean> listEntity(TblRateShortsReportCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
