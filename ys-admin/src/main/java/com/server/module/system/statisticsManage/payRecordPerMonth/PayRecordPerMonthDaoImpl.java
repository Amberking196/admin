package com.server.module.system.statisticsManage.payRecordPerMonth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.statisticsManage.machinesSaleStatistics.PerMachinesSaleDto;
import com.server.module.system.statisticsManage.payRecordPerMonth.PayRecordPerMonthBean;
import com.server.module.system.statisticsManage.payRecordPerMonth.PayRecordPerMonthForm;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

@Repository("payRecordPerMonthDao")
public class PayRecordPerMonthDaoImpl extends BaseDao implements PayRecordPerMonthDao{

	public static Logger log = LogManager.getLogger(PayRecordPerMonthDaoImpl.class);

	public ReturnDataUtil listPage(PayRecordPerMonthForm payMonthForm) {
		log.info("<PayRecordPerMonthDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,a.companyId,b.name as companyName,sum(num) as num,reportDate from tbl_statistics_pay_per_month a");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");

		if(StringUtils.isNotBlank(payMonthForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payMonthForm.getCompanyIds() + ")");
		}
		if (payMonthForm.getStartDate() != null) {
			sql.append(" and Year(reportDate)=Year('" + DateUtil.formatYYYYMMDDHHMMSS(payMonthForm.getStartDate()) + "')");
			sql.append(" and Month(reportDate)=Month('" + DateUtil.formatYYYYMMDDHHMMSS(payMonthForm.getStartDate()) + "')");
		}

		sql.append("  group by reportDate order by reportDate desc,id desc ");
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		log.info("列表查询sql语句：》》" + sql.toString());
		try {
			conn=openConnection();
			pst=conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}
			long off=(payMonthForm.getCurrentPage()-1)*payMonthForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+" limit "+off+","+payMonthForm.getPageSize());
			if(payMonthForm.getIsShowAll()==1) {
				pst=conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<PayRecordPerMonthBean> list=Lists.newArrayList();
			while(rs.next()){
				PayRecordPerMonthBean bean = new PayRecordPerMonthBean();
				bean.setId(rs.getLong("id"));
				bean.setNum(rs.getInt("num"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
//				bean.setReportStartDate(rs.getString("reportStartDate"));2018-04
				bean.setReportDate(rs.getString("reportDate").substring(0,7));
//				bean.setCreateTime(rs.getDate("createTime"));
//				bean.setUpdateTime(rs.getDate("updateTime"));
//				bean.setCreateUser(rs.getInt("createUser"));
//				bean.setUpdateUser(rs.getInt("updateUser"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(payMonthForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PayRecordPerMonthDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	}
	
	@Override
	public Long findPayRecordPerMonthNum2(PayRecordPerMonthForm payMonthForm) {
		log.info("<PayRecordPerMonthDaoImpl>--<findPayRecordPerMonthNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as total from (");
		sql.append(" select a.id,a.companyId,b.name as companyName,sum(num) as num,reportDate from tbl_statistics_pay_per_month a");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");

		if(StringUtils.isNotBlank(payMonthForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payMonthForm.getCompanyIds() + ")");
		}
		if (payMonthForm.getStartDate() != null) {
			sql.append(" and Year(reportDate)=Year('" + DateUtil.formatYYYYMMDDHHMMSS(payMonthForm.getStartDate()) + "')");
			sql.append(" and Month(reportDate)=Month('" + DateUtil.formatYYYYMMDDHHMMSS(payMonthForm.getStartDate()) + "')");
		}

		sql.append(" group by reportDate) t");
		log.info("查询语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				total=rs.getLong("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordPerMonthDaoImpl>--<findPayRecordPerMonthNum>--end");
		return total;
	}

	
	
	
	
	
	
	
	
	
	
	@Override
	public Long findPayRecordPerMonthNum(PayRecordPerMonthForm payMonthForm) {
		log.info("<PayRecordPerMonthDaoImpl>--<findPayRecordPerMonthNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) as total FROM ");
		sql.append(" (SELECT 1 FROM tbl_statistics_pay_per_day where 1=1 ");
		if(payMonthForm.getCompanyId()!=null){
			sql.append(" and companyId ="+payMonthForm.getCompanyId());
		}else if(StringUtil.isNotBlank(payMonthForm.getCompanyIds())){
			sql.append(" and companyId in ("+payMonthForm.getCompanyIds()+")");
		}
		if(payMonthForm.getStartDate() != null){
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDDHHMMSS(payMonthForm.getStartDate()) + "'");
		}
		if(payMonthForm.getEndDate() != null){
			sql.append(" and a.reportDate <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payMonthForm.getEndDate(),1) + "'");
		}
		sql.append(" GROUP BY LEFT(reportDate, 7)) t");
		log.info("查询语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				total=rs.getLong("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordPerMonthDaoImpl>--<findPayRecordPerMonthNum>--end");
		return total;
	}

}
