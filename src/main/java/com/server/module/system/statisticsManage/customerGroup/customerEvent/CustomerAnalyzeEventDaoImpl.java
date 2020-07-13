package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: yjr create time: 2018-10-18 09:10:39
 */
@Repository
public class CustomerAnalyzeEventDaoImpl extends BaseDao<CustomerAnalyzeEventBean> implements CustomerAnalyzeEventDao {

	private static Log log = LogFactory.getLog(CustomerAnalyzeEventDaoImpl.class);
	@Autowired
	private CompanyDao companyDao;
	public ReturnDataUtil listPage(CustomerAnalyzeEventCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,customerId,fromState,currState,fireTime,createTime,eventType from customer_analyze_event where 1=1 ");
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
			List<CustomerAnalyzeEventBean> list = Lists.newArrayList();
			while (rs.next()) {
				CustomerAnalyzeEventBean bean = new CustomerAnalyzeEventBean();
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setFromState(rs.getInt("fromState"));
				bean.setCurrState(rs.getInt("currState"));
				bean.setFireTime(rs.getDate("fireTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setEventType(rs.getInt("eventType"));
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

	public CustomerAnalyzeEventBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		CustomerAnalyzeEventBean entity = new CustomerAnalyzeEventBean();
		return super.del(entity);
	}

	public boolean update(CustomerAnalyzeEventBean entity) {
		return super.update(entity);
	}

	public CustomerAnalyzeEventBean insert(CustomerAnalyzeEventBean entity) {
		return super.insert(entity);
	}

	public List<CustomerAnalyzeEventBean> list(CustomerAnalyzeEventCondition condition) {
		return null;
	}

	@Override
	public List<DateCountVo> listCustomerAnalyzeEventInfo(ChartForm form,int fromState,int currState) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append("SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
        if(fromState!=-1 && currState!=-1){
        	sql.append(" and e.fromState="+fromState+" and e.currState="+currState);
        }
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND i.code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND i.companyId in " + companyDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND i.companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND i.areaId=" + form.getAreaId());
			}
		}
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND e.fireTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND e.fireTime<'" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY e.fireTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				//vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

    @Override
	public List<DateCountVo> listCustomerStateInfo(ChartForm form,int currState) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append("SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if(currState!=-1){
			sql.append(" and  e.currState="+currState);
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND i.code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND i.companyId in " + companyDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND i.companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND i.areaId=" + form.getAreaId());
			}
		}
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND e.fireTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND e.fireTime<'" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY e.fireTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				//vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}


}
