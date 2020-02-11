package com.server.module.customer.refundApplication;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.RefundApplicationEnum;
@Repository
public class RefundApplicationDaoImpl extends MySqlFuns implements RefundApplicationDao{

	private final static Logger log = LogManager.getLogger(RefundApplicationDaoImpl.class);
	
	@Override
	public RefundApplicationDto judgeOrderRefund(String payCode, BigDecimal refundPrice,Integer orderType) {
		log.info("<RefundApplicationDaoImpl--judgeOrderRefund--start>");
		StringBuffer sql = new StringBuffer();
		if(orderType == 1){
			sql.append(" SELECT tc.`phone`,pr.`ptCode`,vmi.companyId FROM pay_record AS pr");
			sql.append("  INNER JOIN vending_machines_info AS vmi ON pr.`vendingMachinesCode` = vmi.`code`");
		}else{
			sql.append(" SELECT tc.`phone`,pr.`ptCode`,pr.companyId FROM store_order AS pr");
		}
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.`id` = pr.`customerId`");
		sql.append(" WHERE pr.payCode = '"+payCode+"'");
		sql.append(" AND pr.state = 10001 AND pr.price>='"+refundPrice+"'");
		sql.append(" AND NOT EXISTS (SELECT 1 FROM refund_application AS ra WHERE ra.`payCode` = pr.`payCode`)");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundApplicationDto applicationDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				applicationDto = new RefundApplicationDto();
				applicationDto.setPhone(rs.getString("phone"));
				applicationDto.setPtCode(rs.getString("ptCode"));
				applicationDto.setCompanyId(rs.getInt("companyId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundApplicationDaoImpl--judgeOrderRefund--end>");
		return applicationDto;
	}

	@Override
	public Long insertRefundApplication(RefundApplicationBean refundApp) {
		log.info("<RefundApplicationDaoImpl--insertRefundApplication--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO refund_application(orderType,payCode,ptCode,phone,companyId,reason,state,refundPrice,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,NOW())");
		List<Object> param = new ArrayList<Object>();
		param.add(refundApp.getOrderType());
		param.add(refundApp.getPayCode());
		param.add(refundApp.getPtCode());
		param.add(refundApp.getPhone());
		param.add(refundApp.getCompanyId());
		param.add(refundApp.getReason());
		param.add(refundApp.getState());
		param.add(refundApp.getRefundPrice());
		int insert = insertGetID(sql.toString(), param);
		log.info("<RefundApplicationDaoImpl--insertRefundApplication--end>");
		return Long.valueOf(insert);
	}

	@Override
	public List<RefundApplicationBean> getRefundApplication(String payCode) {
		log.info("<RefundApplicationDaoImpl--getRefundApplication--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,orderType,payCode,phone,reason,backReason,state,refundPrice,createTime");
		sql.append(" FROM refund_application WHERE payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RefundApplicationBean> applicationList = new ArrayList<RefundApplicationBean>();
		RefundApplicationBean application = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				application = new RefundApplicationBean();
				application.setCreateTime(rs.getTime("createTime"));
				application.setId(rs.getLong("id"));
				application.setOrderType(rs.getInt("orderType"));
				application.setPayCode(rs.getString("payCode"));
				application.setPhone(rs.getString("phone"));
				application.setReason(rs.getString("reason"));
				application.setRefundPrice(rs.getBigDecimal("refundPrice"));
				int state = rs.getInt("state");
				application.setState(state);
				application.setStateName(RefundApplicationEnum.getName(state));
				applicationList.add(application);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundApplicationDaoImpl--getRefundApplication--end>");
		return applicationList;
	}

	
}
