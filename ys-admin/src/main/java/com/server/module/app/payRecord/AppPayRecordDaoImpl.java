package com.server.module.app.payRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.PayStateEnum;
@Repository("aliPayRecordDao")
public class AppPayRecordDaoImpl extends MySqlFuns implements AppPayRecordDao{

	public static Logger log = LogManager.getLogger(AppPayRecordDaoImpl.class); 
	@Override
	public boolean booleanIsArrearage(Long customerId) {
		log.info("<AliPayRecordDaoImpl--booleanIsArrearage--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from pay_record as pr inner join ");
		sql.append(" tbl_customer as tc on pr.customerId = tc.id where ");
		sql.append(" tc.id = "+customerId);
		sql.append(" and pr.state = "+PayStateEnum.NOT_PAY.getState());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if(rs!=null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--booleanIsArrearage--end>");
		return false;
	}
	@Override
	public PayRecordBean findPayRecordByPayCode(String payCode) {
		log.info("<AliPayRecordDaoImpl--findPayRecordByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,");
		sql.append(" payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,");
		sql.append(" costPrice,refundName,remark,itemName,itemTypeId,wayNumber");
		sql.append(" FROM pay_record where payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordBean payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordBean();
				payRecord.setBasicItemId(rs.getLong("basicItemId"));
				payRecord.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setFinishTime(rs.getDate("finishTime"));
				payRecord.setId(rs.getLong("id"));
				payRecord.setItemId(rs.getLong("itemId"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setItemTypeId(rs.getInt("itemTypeId"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getDate("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getInt("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getDate("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setState(rs.getInt("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getInt("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordByPayCode--end>");
		return payRecord;
	}
	@Override
	public boolean updatePayRecord(PayRecordBean payRecord) {
		log.info("<AliPayRecordDaoImpl--updatePayRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update pay_record set state=?,payTime=?,refundTime=?,");
		sql.append(" finishTime=?,refundName=?,ptCode=?,remark=? where payCode=?");
		List<Object> param = new ArrayList<Object>();
		param.add(payRecord.getState());
		param.add(payRecord.getPayTime());
		param.add(payRecord.getRefundTime());
		param.add(payRecord.getFinishTime());
		param.add(payRecord.getRefundName());
		param.add(payRecord.getPtCode());
		param.add(payRecord.getRemark());
		param.add(payRecord.getPayCode());
		int result = upate(sql.toString(), param);
		log.info("<AliPayRecordDaoImpl--updatePayRecord--end>");
		if(result ==1){
			return true;
		}
		return false;
	}
	@Override
	public List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordDaoImpl--findPayRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  pr.id AS orderId,pr.customerId as customerId, pr.ptCode,pr.state,pr.createTime,pr.`itemName` AS itemName,");
		sql.append(" (pr.price / pr.num) AS price,pr.price AS totalPrice,pr.num,pr.payCode as payCode,tc.phone,c.`name` as companyName,");
		sql.append(" pr.vendingMachinesCode as vmCode FROM pay_record pr ");
		sql.append(" inner join vending_machines_info as vmi on vmi.code = pr.vendingMachinesCode");
		sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" left join vending_line as vl on vl.id = vmi.lineId");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" where 1=1");
		if(StringUtil.isNotBlank(payRecordForm.getPayCodeOrName())){
			sql.append(" and (payCode like '%"+payRecordForm.getPayCodeOrName()+"%' or c.name like '%"+payRecordForm.getPayCodeOrName()+"%')");
		}
		if(payRecordForm.getCompanyId()!=null){
			sql.append(" and vmi.companyId = "+payRecordForm.getCompanyId());
		}else if(StringUtil.isNotBlank(payRecordForm.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+payRecordForm.getCompanyIds()+")");
		}
		if(payRecordForm.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+payRecordForm.getDutyId()+",vl.dutyId)");
		}
		if(StringUtil.isNotBlank(payRecordForm.getVmCode())){
			sql.append(" and pr.vendingMachinesCode = '"+payRecordForm.getVmCode()+"'");
		}
		if(payRecordForm.getCustomerId()!=null){
			sql.append(" and pr.customerId = "+payRecordForm.getCustomerId());
		}
		if(payRecordForm.getState() != null){
			sql.append(" and pr.state = "+payRecordForm.getState());
		}
		if(payRecordForm.getStartDate()!=null){
			sql.append(" and pr.payTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate())+"'");
		}
		if(payRecordForm.getEndDate()!=null){
			sql.append(" and pr.payTime < '"+DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate())+"'");
		}
		sql.append(" order by pr.payTime desc");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setCreateTime(rs.getTimestamp("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(rs.getLong("orderId"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setPhone(rs.getString("phone"));
				payRecord.setCompanyName(rs.getString("companyName"));
				payRecord.setVmCode(rs.getString("vmCode"));
				payRecordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecord--end>");
		return payRecordList;
	}

	public List<PayRecordDto> findPayRecord(String vmCode,int day){
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime=new DateTime();
		dateTime=dateTime.minusDays(day);
		String before = sf.format(dateTime.toDate());
		StringBuffer sql = new StringBuffer();
		sql.append("select i.itemName,i.price,i.num,i.createTime,i.realTotalPrice from pay_record_item i inner join pay_record p on i.payRecordId=p.id where p.vendingMachinesCode='"+vmCode+"' and p.payTime>='"+before+"'");
		sql.append(" order by p.payTime desc");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setCreateTime(rs.getTimestamp("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setTotalPrice(rs.getBigDecimal("realTotalPrice"));
			/*	payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setPhone(rs.getString("phone"));
				payRecord.setCompanyName(rs.getString("companyName"));
				payRecord.setVmCode(rs.getString("vmCode"));*/
				payRecordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return payRecordList;





	}
	@Override
	public PayRecordDto findPayRecordById(Long payRecordId) {
		log.info("<AliPayRecordDaoImpl--findPayRecordById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  pr.id AS orderId,pr.customerId as customerId, pr.ptCode,pr.state,pr.createTime,ib.`name` AS itemName,");
		sql.append(" (pr.price / pr.num) AS price,pr.price AS totalPrice,pr.num,pr.payCode as payCode");
		sql.append(" FROM pay_record pr INNER JOIN vending_machines_item vmi ON pr.itemId = vmi.id");
		sql.append(" INNER JOIN item_basic ib ON vmi.basicItemId = ib.id");
		sql.append(" where pr.id="+payRecordId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(rs.getLong("orderId"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordById--end>");
		return payRecord;
	}
	@Override
	public boolean updatePayState(String ptCode,String payCode, Integer state) {
		log.info("<AliPayRecordDaoImpl--updatePayState--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update pay_record set state = "+state);
		if(state==PayStateEnum.PAY_SUCCESS.getState()){
			sql.append(" ,payTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
			sql.append(" ,finishTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		}
		sql.append(" ,ptCode = '"+ptCode+"'");
		sql.append(" where payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		int result = upate(sql.toString());
		log.info("<AliPayRecordDaoImpl--updatePayState--end>");
		if(result == 1){
			return true;
		}
		return false;
	}
	@Override
	public SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordDaoImpl--findPayRecordNum--start>");
		StringBuffer sql = new StringBuffer();
		if(payRecordForm.getType()!=null) {
			sql.append("select count(1) as total,SUM(price) as sumPrice, SUM(costPrice) as sumCostPrice,SUM(num) as sumNum from pay_record pr ");
			sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
			sql.append(" LEFT JOIN vending_machines_info as vmi ON vmi.`code`=pr.`vendingMachinesCode`");
			sql.append(" INNER JOIN replenish_company_machines as rcm ON rcm.`code`=vmi.`code`");
			sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where 1=1");
			if (payRecordForm.getCompanyId() != null) {
				sql.append(" and rcm.companyId =" + payRecordForm.getCompanyId());
			} else if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
				sql.append(" and rcm.companyId in(" + payRecordForm.getCompanyIds() + ")");
			}
			if(payRecordForm.getDutyId()!=null){
				sql.append(" and FIND_IN_SET("+payRecordForm.getDutyId()+",vl.dutyId)");
			}
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
			sql.append(" and (pr.state=10001 or pr.state=10006) ");
		}else {
			sql.append(
					"select count(1) as total,SUM(price) as sumPrice, SUM(costPrice) as sumCostPrice,SUM(num) as sumNum from pay_record pr ");
			sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
			sql.append(" LEFT JOIN vending_machines_info as vmi ON vmi.`code`=pr.`vendingMachinesCode`");
			sql.append(" left join vending_line as vl on vl.id = vmi.lineId");
			sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where 1=1");
			if(payRecordForm.getDutyId()!=null){
				sql.append(" and FIND_IN_SET("+payRecordForm.getDutyId()+",vl.dutyId)");
			}
			if (payRecordForm.getCompanyId() != null) {
				sql.append(" and vmi.companyId =" + payRecordForm.getCompanyId());
			} else if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
				sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
			}
			if (payRecordForm.getState() != null) {
				sql.append(" and pr.state like '%" + payRecordForm.getState() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getVmCode())) {
				sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVmCode() + "%'");
			}
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
			sql.append(" and (pr.state=10001 or pr.state=10006) ");
		}
		log.info("查询语句：" + sql.toString());
		SumPayRecordDto sumPayRecordDto = new SumPayRecordDto();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				sumPayRecordDto.setTotal(rs.getLong("total"));
				sumPayRecordDto.setSumPrice(rs.getBigDecimal("sumPrice"));
				sumPayRecordDto.setSumCostPrice(rs.getBigDecimal("sumCostPrice"));
				sumPayRecordDto.setSumNum(rs.getLong("sumNum"));
			}
		} catch (Exception e) {
			this.closeConnection(rs, ps, conn);
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordNum--end>");
		return sumPayRecordDto;
	}

}
