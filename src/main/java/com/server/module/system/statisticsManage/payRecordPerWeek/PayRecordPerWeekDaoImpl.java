package com.server.module.system.statisticsManage.payRecordPerWeek;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.payRecordPerWeek.PayRecordPerWeekForm;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: hjc
 * create time: 2018-07-14 14:38:10
 */ 
@Repository
public class  PayRecordPerWeekDaoImpl extends BaseDao<PayRecordPerWeekBean> implements PayRecordPerWeekDao{

	public static Logger log = LogManager.getLogger(PayRecordPerWeekDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(PayRecordPerWeekForm payWeekForm) {
		log.info("<PayRecordPerWeekDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,a.companyId,b.name as companyName,sum(num) as num,reportDate,reportStartDate from tbl_statistics_pay_per_week a");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");

		if(StringUtils.isNotBlank(payWeekForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payWeekForm.getCompanyIds() + ")");
		}
		if (payWeekForm.getStartDate() != null) {
			sql.append(" and a.reportStartDate  <= '" + DateUtil.formatYYYYMMDDHHMMSS(payWeekForm.getStartDate()) + "'");
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDD(payWeekForm.getStartDate()) + "'");
		}

		sql.append(" group by reportDate  order by reportDate desc,id desc ");
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
			long off=(payWeekForm.getCurrentPage()-1)*payWeekForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+" limit "+off+","+payWeekForm.getPageSize());
			if(payWeekForm.getIsShowAll()==1) {
				pst=conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<PayRecordPerWeekBean> list=Lists.newArrayList();
			while(rs.next()){
				PayRecordPerWeekBean bean = new PayRecordPerWeekBean();
				bean.setId(rs.getLong("id"));
				bean.setNum(rs.getInt("num"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setReportStartDate(rs.getString("reportStartDate"));
				bean.setReportDate(rs.getString("reportStartDate")+"-"+rs.getString("reportDate"));
//				bean.setCreateTime(rs.getDate("createTime"));
//				bean.setUpdateTime(rs.getDate("updateTime"));
//				bean.setCreateUser(rs.getInt("createUser"));
//				bean.setUpdateUser(rs.getInt("updateUser"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(payWeekForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PayRecordPerWeekDaoImpl>----<listPage>----end");
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
	public Long findPayRecordPerWeekNum(PayRecordPerWeekForm payWeekForm) {
		log.info("<PayRecordPerWeekDaoImpl>--<findPayRecordPerWeekNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as total from (");
		sql.append("select a.id,a.companyId,b.name as companyName,sum(num) as num,reportDate,reportStartDate from tbl_statistics_pay_per_week a");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");

		if(StringUtils.isNotBlank(payWeekForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payWeekForm.getCompanyIds() + ")");
		}
		if (payWeekForm.getStartDate() != null) {
			sql.append(" and a.reportStartDate  <= '" + DateUtil.formatYYYYMMDDHHMMSS(payWeekForm.getStartDate()) + "'");
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDD(payWeekForm.getStartDate()) + "'");
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
		log.info("<PayRecordPerWeekDaoImpl>--<findPayRecordPerWeekNum>--end");
		return total;
	}

	
	public PayRecordPerWeekBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		PayRecordPerWeekBean entity=new PayRecordPerWeekBean();
		return super.del(entity);
	}

	public boolean update(PayRecordPerWeekBean entity) {
		return super.update(entity);
	}

	public PayRecordPerWeekBean insert(PayRecordPerWeekBean entity) {
		return super.insert(entity);
	}
}

