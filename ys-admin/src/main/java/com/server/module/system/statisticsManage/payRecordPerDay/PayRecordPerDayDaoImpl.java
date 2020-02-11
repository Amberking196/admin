package com.server.module.system.statisticsManage.payRecordPerDay;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.VmInfoStateEnum;

@Repository("payRecordPerDayDao")
public class PayRecordPerDayDaoImpl extends MySqlFuns implements PayRecordPerDayDao {

	public static Logger log = LogManager.getLogger(PayRecordPerDayDaoImpl.class);

	@Override
	public List<PayRecordPerDayDto> findPayRecordPerDay(PayRecordPerDayForm payDayForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordPerDay>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from(select a.reportDate,vmCode,basicItemId,a.companyId,");
		sql.append("COUNT(DISTINCT vmCode) as machinesNum,(sum(finishedItemNum)/COUNT(DISTINCT vmCode)) as average,");
		sql.append("b.name as companyName,c.name as itemName,");
		sql.append("sum(finishedMoney) as finishedMoney,");
		sql.append("sum(finishedItemNum) as finishedItemNum,");
		//sql.append("sum(finishedOrderNum) as finishedOrderNum,");
		sql.append("sum(machinesOrderNum) as finishedOrderNum,");//version 2
		sql.append("(sum(finishedMoney)/COUNT(DISTINCT vmCode)) as averageMoney");
		//sql.append("sum(refundMoney) as refundMoney,");
		//sql.append("sum(refundItemNum) as refundItemNum,");
		//sql.append("sum(refundOrderNum) as refundOrderNum,");
		//sql.append("sum(finishedCost) as finishedCost,");
		//sql.append("sum(profit) as profit,a.reportDate as createTime ");
		sql.append(" from tbl_statistics_pay_per_day a left join company b on a.companyId=b.id");
		sql.append(" left join item_basic c on a.basicItemId=c.id ");
		sql.append(" INNER JOIN vending_machines_info vmi ON vmi.CODE = a.vmCode");
		sql.append(" where 1=1 ");
				
		if(payDayForm.getAreaId()!=null && payDayForm.getAreaId()>0){
			sql.append("and vmi.areaId = '" + payDayForm.getAreaId() + "' ");
		}
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate() != null) {
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate() != null) {
			sql.append(" and a.reportDate <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payDayForm.getEndDate(),1) + "'");
		}

		sql.append(" group by reportDate order by reportDate desc)A left join ");
		sql.append(" (select count(*) as refundOrderNum,sum(rr.refundPrice) as refundMoney,DATE_FORMAT(pr.payTime,'%Y-%m-%d') as refundDate from refund_record rr");
		sql.append(" inner join pay_record pr on pr.payCode=rr.payCode");
		sql.append(" inner join vending_machines_info vmi on vmi.code=pr.vendingmachinescode");

		sql.append(" where rr.state=1");
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and vmi.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getAreaId() != null && payDayForm.getAreaId()>0) {
			sql.append("and vmi.areaId = '" + payDayForm.getAreaId() + "' ");
		}
		sql.append(" GROUP BY DATE_FORMAT(pr.payTime,'%Y-%m-%d'))");
		sql.append(" B on A.reportDate=B.refundDate order by reportDate desc");
		if(payDayForm.getIsShowAll()==0){
			sql.append(" limit " + (payDayForm.getCurrentPage()-1)*payDayForm.getPageSize() + "," + payDayForm.getPageSize());
		}
		log.info("查询语句：" + sql);
		List<PayRecordPerDayDto> result = new ArrayList<PayRecordPerDayDto>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordPerDayDto payDayDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				payDayDto = new PayRecordPerDayDto();
				payDayDto.setMachinesNum(rs.getInt("machinesNum"));
				payDayDto.setAverage(rs.getDouble("average"));
				payDayDto.setBasicItemId(rs.getLong("basicItemId"));
				payDayDto.setCompanyId(rs.getLong("companyId"));
				payDayDto.setCompanyName(rs.getString("companyName"));
				//payDayDto.setFinishedCost(rs.getDouble("finishedCost"));
				payDayDto.setFinishedItemNum(rs.getBigDecimal("finishedItemNum"));
				payDayDto.setFinishedMoney(rs.getDouble("finishedMoney"));
				payDayDto.setFinishedOrderNum(rs.getBigDecimal("finishedOrderNum"));
				payDayDto.setItemName(rs.getString("itemName"));
				//payDayDto.setProfit(rs.getDouble("profit"));
				//payDayDto.setRefundItemNum(rs.getBigDecimal("refundItemNum"));
				payDayDto.setRefundMoney(rs.getDouble("refundMoney"));
				payDayDto.setRefundOrderNum(rs.getBigDecimal("refundOrderNum"));
				if(payDayDto.getRefundOrderNum()==null) {
					payDayDto.setRefundOrderNum(new BigDecimal(0));
				}
				payDayDto.setCreateTime(rs.getDate("reportDate"));
				payDayDto.setVmCode(rs.getString("vmCode"));
				payDayDto.setAverageMoney(rs.getBigDecimal("averageMoney"));

				result.add(payDayDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordPerDay>--end");
		return result;
	}

	@Override
	public Long findPayRecordPerDayNum(PayRecordPerDayForm payDayForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordPerDayNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as total from (select 1");
		sql.append(" from tbl_statistics_pay_per_day a left join company b on a.companyId=b.id");
		sql.append(" left join item_basic c on a.companyId=c.id where 1=1");
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and a.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate() != null) {
			sql.append(" and a.reportDate >='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate() != null) {
			sql.append(" and a.reportDate <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payDayForm.getEndDate(),1) + "'");
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
		log.info("<PayRecordDaoImpl>--<findPayRecordPerDayNum>--end");
		return total;
	}

	@Override
	public Integer findNormalMachines(PayRecordPerDayForm payDayForm,Date reportDate) {
		log.info("<PayRecordDaoImpl>--<findNormalMachines>--start");
		Integer num = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS num FROM `vending_machines_info` vmi WHERE state = "+VmInfoStateEnum.MACHINES_NORMAL.getCode());
		if(StringUtil.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" AND companyId IN ("+payDayForm.getCompanyIds()+")");
		}else{
			return num;
		}
		if(payDayForm.getAreaId()!=null && payDayForm.getAreaId()>0){
			sql.append("and vmi.areaId = '" + payDayForm.getAreaId() + "' ");
		}
		if(reportDate!=null){
			DateTime dt = new DateTime(reportDate);
			dt=dt.plusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
			reportDate=dt.toDate();
			sql.append(" AND startUsingTime < '" + DateUtil.formatLocalYYYYMMDDHHMMSS(reportDate,1) + "'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				num = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findNormalMachines>--end");
		return num;
	}

	
	public List<PayRecordPerDayDto> userBuyStation(PayRecordPerDayForm payDayForm){
		log.info("<PayRecordDaoImpl>--<userBuyStation>--start");
		StringBuffer sql = new StringBuffer();
		//sql.append(" SELECT DATE_FORMAT(reportDate,'%Y%m%d') AS day,reportDate,COUNT(DISTINCT vmCode) AS machinesCount ,SUM(finishedMoney) AS finishedMoney,SUM(machinesOrderNum) AS machinesOrderNum ,SUM(finishedItemNum) AS finishedItemNum,SUM(finishedMoney)/SUM(machinesOrderNum) As averageMoney,SUM(finishedItemNum)/SUM(machinesOrderNum) As average FROM tbl_statistics_pay_per_day p INNER JOIN vending_machines_info i ON p.`vmCode`=i.code WHERE 1=1" ); 
		sql.append(" SELECT DATE_FORMAT(payTime,'%Y%m%d') AS day,payTime,max(price) as MaxPrice,min(price) as MinPrice,COUNT(DISTINCT vendingmachinescode) AS machinesCount,SUM(price) AS finishedMoney,count(*) AS machinesOrderNum ,SUM(num) AS finishedItemNum,SUM(price)/count(*) As averageMoney,SUM(num)/count(*) As average  ");
		sql.append(" FROM pay_record p INNER JOIN vending_machines_info i on p.vendingmachinescode=i.code");
		sql.append(" WHERE 1=1");
		sql.append(" and p.state !=10002");
		sql.append(" and DATE_FORMAT(payTime,'%Y%m%d') is not null");
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate() != null) {
			sql.append(" and p.payTime >='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate() != null) {
			sql.append(" and p.payTime <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payDayForm.getEndDate(),1) + "'");
		}
		if (StringUtils.isNotBlank(payDayForm.getVmCode())) {
			sql.append(" and p.vendingmachinescode= "+payDayForm.getVmCode());
		}
		sql.append(" GROUP BY day");

		if(payDayForm.getIsShowAll()==0){
			sql.append(" limit " + (payDayForm.getCurrentPage()-1)*payDayForm.getPageSize() + "," + payDayForm.getPageSize());
		}
		
		log.info("查询语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PayRecordPerDayDto> result = new ArrayList<PayRecordPerDayDto>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			PayRecordPerDayDto payDayDto = null;
			while (rs != null && rs.next()) {
				payDayDto = new PayRecordPerDayDto();
				payDayDto.setMachinesNum(rs.getInt("machinesCount"));
				payDayDto.setFinishedMoney(rs.getDouble("finishedMoney"));//销售额
				payDayDto.setFinishedItemNum(rs.getBigDecimal("finishedItemNum"));//商品数
				payDayDto.setFinishedOrderNum(new BigDecimal(rs.getDouble("machinesOrderNum")));//订单数
				payDayDto.setAverageMoney(new BigDecimal(rs.getDouble("averageMoney")).setScale(2, BigDecimal.ROUND_HALF_UP));//客单价
				payDayDto.setAverage(rs.getDouble("average"));//平均商品数	
				
				payDayDto.setDay(sdf.format(rs.getDate("payTime")));
				payDayDto.setMaxPrice(rs.getDouble("maxPrice"));
				payDayDto.setMinPrice(rs.getDouble("minPrice"));
				payDayDto.setCreateTime(rs.getDate("payTime"));
				result.add(payDayDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<userBuyStation>--end");
		return result;
	}

	@Override
	public Long userBuyStationNum(PayRecordPerDayForm payDayForm) {
		log.info("<PayRecordDaoImpl>--<userBuyStationNum>--start");
		StringBuffer sql = new StringBuffer();

		sql.append(" select count(1) as total from ");
		sql.append(" (SELECT DATE_FORMAT(payTime,'%Y%m%d') AS day,payTime,max(price) as MaxPrice,min(price) as MinPrice,COUNT(DISTINCT vendingmachinescode) AS machinesCount,SUM(price) AS finishedMoney,count(*) AS machinesOrderNum ,SUM(num) AS finishedItemNum,SUM(price)/count(*) As averageMoney,SUM(num)/count(*) As average  ");
		sql.append(" FROM pay_record p  INNER JOIN vending_machines_info i on p.vendingmachinescode=i.code");
		sql.append(" WHERE 1=1");
		sql.append(" and p.state !=10002");
		sql.append(" and DATE_FORMAT(payTime,'%Y%m%d') is not null");
		if(StringUtils.isNotBlank(payDayForm.getCompanyIds())){
			sql.append(" and i.companyId in (" + payDayForm.getCompanyIds() + ")");
		}
		if (payDayForm.getStartDate() != null) {
			sql.append(" and p.payTime >='" + DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()) + "'");
		}
		if (payDayForm.getEndDate() != null) {
			sql.append(" and p.payTime <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(payDayForm.getEndDate(),1) + "'");
		}
		if (payDayForm.getVmCode() != null) {
			sql.append(" and p.vendingmachinescode= "+payDayForm.getVmCode());
		}
		sql.append(" GROUP BY day)t");

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
		log.info("<PayRecordDaoImpl>--<userBuyStationNum>--end");
		return total;
	}
}
