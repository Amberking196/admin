package com.server.module.system.statisticsManage.payRecordPerHour;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.payRecordPerHour.PayRecordPerHourForm;
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
 * create time: 2018-07-13 09:23:01
 */ 
@Repository
public class  PayRecordPerHourDaoImpl extends BaseDao<PayRecordPerHourBean> implements PayRecordPerHourDao{

	public static Logger log = LogManager.getLogger(PayRecordPerHourDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(PayRecordPerHourForm payHourForm) {
		log.info("<PayRecordPerHourDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,b.name as companyName,reportDate,sum(zero) as zero,sum(one) as one,sum(two) as two,sum(three) as three,sum(four) as four,sum(five) as five,sum(six) as six,sum(seven) as seven,sum(eight) as eight,sum(nine) as nine,sum(ten) as ten,sum(eleven) as eleven,sum(twelve) as twelve,sum(thirteen) as thirteen,sum(fourteen) as fourteen,sum(fifteen) as fifteen,sum(sixteen) as sixteen,sum(seventeen) as seventeen,sum(eighteen) as eighteen,sum(nineteen) as nineteen,sum(twenty) as twenty,sum(twentyone) as twentyone,sum(twentytwo) as twentytwo,sum(twentythree) as twentythree from tbl_statistics_pay_per_hour a ");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");
		if(StringUtils.isNotBlank(payHourForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payHourForm.getCompanyIds() + ")");
		}
		if (payHourForm.getStartDate() != null) {
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDDHHMMSS(payHourForm.getStartDate()) + "'");
		}
		if (payHourForm.getEndDate() != null) {
			sql.append(" and a.reportDate <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payHourForm.getEndDate(),1) + "'");
		}
		sql.append(" group by reportDate order by reportDate desc,id desc ");


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
			long off=(payHourForm.getCurrentPage()-1)*payHourForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+" limit "+off+","+payHourForm.getPageSize());
			if(payHourForm.getIsShowAll()==1) {
				pst=conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<PayRecordPerHourBean> list=Lists.newArrayList();
			while(rs.next()){
				PayRecordPerHourBean bean = new PayRecordPerHourBean();
				bean.setId(rs.getLong("id"));
				bean.setZero(rs.getInt("zero"));
				bean.setOne(rs.getInt("one"));
				bean.setTwo(rs.getInt("two"));
				bean.setThree(rs.getInt("three"));
				bean.setFour(rs.getInt("four"));
				bean.setFive(rs.getInt("five"));
				bean.setSix(rs.getInt("six"));
				bean.setSeven(rs.getInt("seven"));
				bean.setEight(rs.getInt("eight"));
				bean.setNine(rs.getInt("nine"));
				bean.setTen(rs.getInt("ten"));
				bean.setEleven(rs.getInt("eleven"));
				bean.setTwelve(rs.getInt("twelve"));
				bean.setThirteen(rs.getInt("thirteen"));
				bean.setFourteen(rs.getInt("fourteen"));
				bean.setFifteen(rs.getInt("fifteen"));
				bean.setSixteen(rs.getInt("sixteen"));
				bean.setSeventeen(rs.getInt("seventeen"));
				bean.setEighteen(rs.getInt("eighteen"));
				bean.setNineteen(rs.getInt("nineteen"));
				bean.setTwenty(rs.getInt("twenty"));
				bean.setTwentyone(rs.getInt("twentyone"));
				bean.setTwentytwo(rs.getInt("twentytwo"));
				bean.setTwentythree(rs.getInt("twentythree"));
				bean.setReportDate(rs.getString("reportDate"));
				bean.setCompanyName(rs.getString("companyName"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(payHourForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PayRecordPerHourDaoImpl>----<listPage>----end");
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
	public Long findPayRecordPerHourNum(PayRecordPerHourForm payHourForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordPerHourNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as total from (");
		sql.append(" select a.id,b.name as companyName,reportDate,sum(zero) as zero,sum(one) as one,sum(two) as two,sum(three) as three,sum(four) as four,sum(five) as five,sum(six) as six,sum(seven) as seven,sum(eight) as eight,sum(nine) as nine,sum(ten) as ten,sum(eleven) as eleven,sum(twelve) as twelve,sum(thirteen) as thirteen,sum(fourteen) as fourteen,sum(fifteen) as fifteen,sum(sixteen) as sixteen,sum(seventeen) as seventeen,sum(eighteen) as eighteen,sum(nineteen) as nineteen,sum(twenty) as twenty,sum(twentyone) as twentyone,sum(twentytwo) as twentytwo,sum(twentythree) as twentythree from tbl_statistics_pay_per_hour a ");
		sql.append(" left join company b on a.companyId=b.id where 1=1 ");
		if(StringUtils.isNotBlank(payHourForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payHourForm.getCompanyIds() + ")");
		}
		if (payHourForm.getStartDate() != null) {
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDDHHMMSS(payHourForm.getStartDate()) + "'");
		}
		if (payHourForm.getEndDate() != null) {
			sql.append(" and a.reportDate <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payHourForm.getEndDate(),1) + "'");
		}
		sql.append(" group by reportDate) t");
		log.info("查询语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				total = rs.getLong("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordPerHourNum>--end");
		return total;
	}
	public PayRecordPerHourBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		PayRecordPerHourBean entity=new PayRecordPerHourBean();
		return super.del(entity);
	}

	public boolean update(PayRecordPerHourBean entity) {
		return super.update(entity);
	}

	public PayRecordPerHourBean insert(PayRecordPerHourBean entity) {
		return super.insert(entity);
	}
}

