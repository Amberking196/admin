package com.server.module.system.statisticsManage.userState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;

import com.google.common.collect.Lists;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.module.system.statisticsManage.customerGroup.customerEvent.CustomerAnalyzeEventBean;
import com.server.module.system.statisticsManage.customerGroup.customerEvent.CustomerAnalyzeEventDaoImpl;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.statisticsManage.userActiveDegree.UserActiveDegreeDaoImpl;
import com.server.util.DateUtil;

@Repository
public class UserStateDaoImpl extends BaseDao<UserStateBean> implements UserStateDao {
	private final static Logger log = LogManager.getLogger(UserActiveDegreeDaoImpl.class);
	@Autowired
	private CompanyDao companyDao;
	
	@Override
	public UserStateBean stateCompare(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append("SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY,count(e.id) as num,e.currState FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} else {
			if (payDayForm.getCompanyId() != null && payDayForm.getCompanyId() > 1) {
				// 添加查询条件
				if (companyDao.checkIsSubsidiaries(payDayForm.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND i.companyId in " + companyDao.findAllSonCompanyIdForInSql(payDayForm.getCompanyId()));
				} else {
					sql.append(" AND i.companyId =" + payDayForm.getCompanyId());
				}
			}
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		sql.append(" GROUP BY DATE_FORMAT(fireTime,'%Y%m%d'),e.currState ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				switch (rs.getInt("currState")){
					case	1:usb.setOne(rs.getInt("num"));break;
					case	2:usb.setTwo(rs.getInt("num"));break;
					case	3:usb.setThree(rs.getInt("num"));break;
					case	4:usb.setFour(rs.getInt("num"));break;
					case	5:usb.setFive(rs.getInt("num"));break;
					case	6:usb.setSix(rs.getInt("num"));
				}
			}
			if (showSql) {
				log.info(sql);
			}
			return usb;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return usb;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public UserStateBean userStateNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append("SELECT a.state,count(*) as num FROM `customer_analyze` a LEFT JOIN tbl_customer c ON a.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
//		if (payDayForm.getStartDate()!=null) {
//			sql.append(" AND a.registerTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
//		}
//		if (payDayForm.getEndDate()!=null) {
//			sql.append(" AND a.registerTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
//		}
		sql.append(" group by state");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				switch (rs.getInt("state")){
					case	1:usb.setOne(rs.getInt("num"));break;
					case	2:usb.setTwo(rs.getInt("num"));break;
					case	3:usb.setThree(rs.getInt("num"));break;
					case	4:usb.setFour(rs.getInt("num"));break;
					case	5:usb.setFive(rs.getInt("num"));break;
					case	6:usb.setSix(rs.getInt("num"));
				}
			}
			if (showSql) {
				log.info(sql);
			}
			return usb;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return usb;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	@Override
	public UserStateBean userCurrStateNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		if(payDayForm.getVmCode()==null && payDayForm.getCompanyIds()==null) {
			sql.append(" SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY,count(e.id) as num,e.currState FROM customer_analyze_event e WHERE 1=1 ");
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
		}else {
			sql.append("SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY,count(e.id) as num,e.currState FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
			if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
				sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
			} 
			if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
				sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
			}
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
		}
		sql.append(" GROUP BY e.currState ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				switch (rs.getInt("currState")){
					case	1:usb.setOne(rs.getInt("num"));break;
					case	2:usb.setTwo(rs.getInt("num"));break;
					case	3:usb.setThree(rs.getInt("num"));break;
					case	4:usb.setFour(rs.getInt("num"));break;
					case	5:usb.setFive(rs.getInt("num"));break;
					case	6:usb.setSix(rs.getInt("num"));
				}
			}
			if (showSql) {
				log.info(sql);
			}
			return usb;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return usb;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	
	@Override
	public UserStateBean userFromStateNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		if(payDayForm.getVmCode()==null && payDayForm.getCompanyIds()==null) {
			sql.append(" SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY,count(e.id) as num,e.fromState FROM customer_analyze_event e WHERE 1=1 ");
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
		}else {
			sql.append(" SELECT DATE_FORMAT(fireTime,'%Y%m%d') AS DAY,count(e.id) as num,e.fromState FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
			if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
				sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
			} 
			if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
				sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
			}
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
		}

		sql.append(" GROUP BY e.fromState ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				switch (rs.getInt("fromState")){
					case	1:usb.setOne(rs.getInt("num"));break;
					case	2:usb.setTwo(rs.getInt("num"));break;
					case	3:usb.setThree(rs.getInt("num"));break;
					case	4:usb.setFour(rs.getInt("num"));break;
					case	5:usb.setFive(rs.getInt("num"));break;
					case	6:usb.setSix(rs.getInt("num"));
				}
			}
			if (showSql) {
				log.info(sql);
			}
			return usb;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return usb;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
    public Integer userRegisterNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append(" SELECT count(*) as num FROM customer_analyze e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} else {
			if (payDayForm.getCompanyId() != null && payDayForm.getCompanyId() > 1) {
				// 添加查询条件
				if (companyDao.checkIsSubsidiaries(payDayForm.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND i.companyId in " + companyDao.findAllSonCompanyIdForInSql(payDayForm.getCompanyId()));
				} else {
					sql.append(" AND i.companyId =" + payDayForm.getCompanyId());
				}
			}
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND e.registerTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND e.registerTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer num=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				num=rs.getInt("num");
			}
			if (showSql) {
				log.info(sql);
			}
			return num;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return num;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
    }

    public Integer customerRegisterNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append(" SELECT count(*) as num FROM tbl_customer c  LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND c.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND c.createTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		
		sql.append(" AND phone is not null");
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer num=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				num=rs.getInt("num");
			}
			if (showSql) {
				log.info(sql);
			}
			return num;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return num;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
    }
    
    public List<CustomerRegisterNumDto> customerRegisterNumGroupByDay(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append(" SELECT count(*) as num,c.createTime FROM tbl_customer c  LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND c.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND c.createTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		
		sql.append(" AND phone is not null");
		sql.append(" group by DATE_FORMAT(c.createTime,'%Y%m%d')");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<CustomerRegisterNumDto> list= Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerRegisterNumDto cr=new CustomerRegisterNumDto();
				cr.setNum(rs.getInt("num"));
				cr.setDate(rs.getDate("createTime"));
				list.add(cr);
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
    
    public List<CustomerRegisterNumDto> customerRegisterNumGroupByDay(PayRecordPerDayForm payDayForm,PayRecordPerDayForm payDayForm1,PayRecordPerDayForm payDayForm2) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append(" SELECT count(*) as num,c.createTime FROM tbl_customer c  LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1 ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		sql.append(" AND phone is not null");
		sql.append(" AND (c.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate())+ "'");
		sql.append(" AND c.createTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "' ");
		
		sql.append(" or (c.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm1.getStartDate())+ "'");
		sql.append(" AND c.createTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm1.getEndDate()) + "')");
		sql.append(" or (c.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm2.getStartDate())+ "'");
		sql.append(" AND c.createTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm2.getEndDate()) + "'))");
		
		sql.append(" group by DATE_FORMAT(c.createTime,'%Y%m%d')");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<CustomerRegisterNumDto> list= Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerRegisterNumDto cr=new CustomerRegisterNumDto();
				cr.setNum(rs.getInt("num"));
				cr.setDate(rs.getDate("createTime"));
				list.add(cr);
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
	public UserStateBean userChangeNum(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		sql.append(" SELECT fromState,(num2-num) as num from(select count(e.id) as num,e.currState FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1  ");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 		
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		sql.append(" GROUP BY e.currState )A");
		
		
		sql.append(" left join (");

		sql.append(" SELECT count(e.id) as num2,e.fromState FROM customer_analyze_event e LEFT JOIN tbl_customer c ON e.customerId=c.id LEFT JOIN vending_machines_info i ON c.vmCode=i.code WHERE 1=1");
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
		} 		
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate()!=null) {
			sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate()!=null) {
			sql.append(" AND e.fireTime<'" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
		}
		
		sql.append(" GROUP BY e.fromState");
		sql.append(" )B on B.fromState=A.currState");
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				switch (rs.getInt("fromState")){
					case	1:usb.setOne(rs.getInt("num"));break;
					case	2:usb.setTwo(rs.getInt("num"));break;
					case	3:usb.setThree(rs.getInt("num"));break;
					case	4:usb.setFour(rs.getInt("num"));break;
					case	5:usb.setFive(rs.getInt("num"));break;
					case	6:usb.setSix(rs.getInt("num"));break;
				}
			}
			if (showSql) {
				log.info(sql);
			}
			return usb;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return usb;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

    
    @Override
	public LinkedHashMap userStateGroupByDate(PayRecordPerDayForm payDayForm) {
		StringBuilder sql = new StringBuilder();//e.customerId,e.fromState,e.currState,c.vmCode,i.companyId,i.areaId
		if(StringUtils.isBlank(payDayForm.getVmCode()) && StringUtils.isBlank(payDayForm.getCompanyIds())) { 
			sql.append("	SELECT	count(case when fromstate=1 then 0  end) - count(case when currstate=1 then 0  end) as one," + 
					"		count(case when fromstate=2 then 0  end) - count(case when currstate=2 then 0  end) as two," + 
					"		count(case when fromstate=3 then 0  end) - count(case when currstate=3 then 0  end) as three," + 
					"		count(case when fromstate=4 then 0  end) - count(case when currstate=4 then 0  end) as four," + 
					"		count(case when fromstate=5 then 0  end) - count(case when currstate=5 then 0  end) as five," + 
					"		count(case when fromstate=6 then 0  end) - count(case when currstate=6 then 0  end) as six,e.fireTime FROM customer_analyze_event e  " );
			sql.append("	WHERE 1=1  ");
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
			sql.append(" GROUP BY e.fireTime ORDER BY fireTime desc");
		}else {

			sql.append("	SELECT	count(case when fromstate=1 then 0  end) - count(case when currstate=1 then 0  end) as one," + 
					"		count(case when fromstate=2 then 0  end) - count(case when currstate=2 then 0  end) as two," + 
					"		count(case when fromstate=3 then 0  end) - count(case when currstate=3 then 0  end) as three," + 
					"		count(case when fromstate=4 then 0  end) - count(case when currstate=4 then 0  end) as four," + 
					"		count(case when fromstate=5 then 0  end) - count(case when currstate=5 then 0  end) as five," + 
					"		count(case when fromstate=6 then 0  end) - count(case when currstate=6 then 0  end) as six,e.fireTime FROM customer_analyze_event e  " + 
					"		LEFT JOIN tbl_customer c ON e.customerId = c.id "); 
			sql.append("	LEFT JOIN vending_machines_info i ON c.vmCode = i.CODE WHERE 1=1  ");
			if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
				sql.append(" AND i.code ='" + payDayForm.getVmCode() + "'");
			} 		
			if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
				sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
			}
			if (payDayForm.getStartDate()!=null) {
				sql.append(" AND e.fireTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
			}
			if (payDayForm.getEndDate()!=null) {
				sql.append(" AND e.fireTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getEndDate()) + "'");
			}
			sql.append(" GROUP BY e.fireTime ORDER BY fireTime desc");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		UserStateBean usb=new UserStateBean();
		LinkedHashMap hp=new LinkedHashMap<Date,UserStateBean>();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
					usb=new UserStateBean();
					usb.setOne(rs.getInt("one"));
					usb.setTwo(rs.getInt("two"));
					usb.setThree(rs.getInt("three"));
					usb.setFour(rs.getInt("four"));
					usb.setFive(rs.getInt("five"));
					usb.setSix(rs.getInt("six"));
					hp.put(rs.getDate("fireTime"), usb);	
				}

			if (showSql) {
				log.info(sql);
			}
			return hp;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return hp;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

}
