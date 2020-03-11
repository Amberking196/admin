package com.server.module.system.refund;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.commonBean.TotalResultBean;
import com.server.module.system.refund.application.RefundApplicationDto;
import com.server.module.system.refund.application.RefundApplicationForm;
import com.server.module.system.refund.principal.RefundPrincipalBean;
import com.server.module.system.refund.principal.RefundPrincipalForm;
import com.server.util.DateUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Repository
public class RefundDaoImpl extends MySqlFuns implements RefundDao {

	private final static Logger log = LogManager.getLogger(RefundDaoImpl.class);

	@Override
	public boolean insertRefundPrincipal(RefundPrincipalBean principal) {
		log.info("<RefundDaoImpl--insertRefundPrincipal--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO refund_principal(loginInfoId,phone,deleteFlag,");
		sql.append(" createUser,createTime,updateUser,updateTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(principal.getLoginInfoId());
		param.add(principal.getPhone());
		param.add(principal.getDeleteFlag());
		param.add(principal.getCreateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(principal.getCreateTime()));
		param.add(principal.getUpdateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(principal.getUpdateTime()));
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<RefundDaoImpl--insertRefundPrincipal--end>");
		if (insertGetID > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateRefundPrincipal(RefundPrincipalBean principal) {
		log.info("<RefundDaoImpl--updateRefundPrincipal--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE refund_principal SET phone = ?,deleteFlag=?,updateUser=?,updateTime=?");
		sql.append(" WHERE id=?");
		List<Object> param = new ArrayList<Object>();
		param.add(principal.getPhone());
		param.add(principal.getDeleteFlag());
		param.add(principal.getUpdateUser());
		param.add(principal.getUpdateTime());
		param.add(principal.getId());
		int upate = upate(sql.toString(), param);
		log.info("<RefundDaoImpl--updateRefundPrincipal--end>");
		if (upate > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<RefundRecordBean> findRefundInfo(RefundRecordForm form) {
		log.info("<RefundDaoImpl--findRefundInfo--start>");
		StringBuffer sql = new StringBuffer();
		// sql.append(" SELECT
		// rr.id,rr.state,rr.`type`,rr.refundPlatform,rr.outRefundNo,rr.platformNo,rr.payCode,rr.ptCode,");
		// sql.append("
		// rr.price,rr.refundPrice,rr.reason,rr.createTime,li.`name` AS
		// createUserName");
		// sql.append(" FROM refund_record AS rr");
		// sql.append(" INNER JOIN pay_record AS pr ON pr.`payCode` =
		// rr.`payCode`");
		// sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.`code`
		// = pr.`vendingMachinesCode`");
		// sql.append(" LEFT JOIN login_info AS li ON li.`id` =
		// rr.`createUser`");
		// sql.append(" WHERE 1=1 ");
		// sql.append(" AND
		// FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+"))");
		sql.append(" SELECT payTime,rr.id,rr.state,rr.`type`,rr.refundPlatform,rr.outRefundNo,");
		sql.append(" rr.platformNo,rr.payCode,rr.ptCode,rr.itemName,abs(month(payTime)-month(rr.createTime)) as remark,");
		sql.append(" rr.price,rr.refundPrice,rr.reason,rr.createTime,rr.refundNum,li.`name` AS createUserName ");

		sql.append(" FROM refund_record AS rr");
		sql.append(" LEFT JOIN login_info AS li ON li.`id` = rr.`createUser`");
//		if (form.getType() != null ) {
//			if (form.getType() != null && form.getType()==5) {
//				sql.append(" LEFT JOIN pay_record_vision pr on pr.payCode = rr.payCode");
//			}else if(form.getType() != null && (form.getType() == 2 || form.getType() == 3)) {
//				sql.append(" LEFT JOIN store_order pr on pr.payCode = rr.payCode");
//			}else if(form.getType() != null && form.getType() == 4 ) {
//				sql.append(" LEFT JOIN member_order pr on pr.payCode = rr.payCode");
//			}else{
//				sql.append(" LEFT JOIN pay_record pr on pr.payCode = rr.payCode");
//			}
//		}

		
		sql.append(" WHERE 1=1 AND FIND_IN_SET(rr.`companyId`,getChildList(" + form.getCompanyId() + "))");
		if (StringUtil.isNotBlank(form.getPayCode())) {
			sql.append(" AND rr.payCode = '" + form.getPayCode() + "'");
		}
		if (StringUtil.isNotBlank(form.getPtCode())) {
			sql.append(" AND rr.ptCode = '" + form.getPtCode() + "'");
		}
		if (form.getType() != null) {
			sql.append(" AND rr.`type`= '" + form.getType() + "'");
		}
		if (form.getRefundPlatform() != null) {
			sql.append(" AND rr.refundPlatform = '" + form.getRefundPlatform() + "'");
		}
		if (form.getState() != null) {
			sql.append(" AND rr.state = '" + form.getState() + "'");
		}
		if (form.getStartTime() != null) {
			sql.append(" AND rr.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()) + "'");
		}
		if (form.getEndTime() != null) {
			sql.append(" AND rr.createTime <= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()) + "'");
		}
		sql.append(" ORDER BY rr.createTime DESC");
		if (form.getIsShowAll() == 0) {
			sql.append(" LIMIT " + (form.getCurrentPage() - 1) * form.getPageSize() + "," + form.getPageSize());
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundRecordBean refund = null;
		List<RefundRecordBean> refundList = new ArrayList<RefundRecordBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				refund = new RefundRecordBean();
				refund.setCreateTime(rs.getTimestamp("createTime"));
				refund.setPayTime(rs.getTimestamp("payTime"));
				refund.setCreateUserName(rs.getString("createUserName"));
				refund.setId(rs.getLong("id"));
				refund.setOutRefundNo(rs.getString("outRefundNo"));
				refund.setPayCode(rs.getString("payCode"));
				refund.setPlatformNo(rs.getString("platformNo"));
				refund.setPrice(rs.getDouble("price"));
				refund.setPtCode(rs.getString("ptCode"));
				refund.setReason(rs.getString("reason"));
				refund.setRefundPlatform(rs.getInt("refundPlatform"));
				refund.setRefundPrice(rs.getDouble("refundPrice"));
				refund.setState(rs.getInt("state"));
				refund.setType(rs.getInt("type"));
				refund.setItemName(rs.getString("itemName"));
				refund.setRefundNum(rs.getInt("refundNum"));
				if(rs.getInt("remark")>0) {
					refund.setRemark("跨月退款");
				}
				refundList.add(refund);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--findRefundInfo--end>");
		return refundList;
	}

	@Override
	public List<RefundPrincipalBean> getRefundPrincipalInfo(RefundPrincipalForm form) {
		log.info("<RefundDaoImpl--getRefundPrincipalInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT rp.`id`,rp.`deleteFlag`,rp.`phone`,li.`name` AS userName,");
		sql.append(" rp.`createTime`,rp.`updateTime`,lic.`name` AS createUserName,liu.`name` AS updateUserName");
		sql.append(" FROM refund_principal AS rp");
		sql.append(" LEFT JOIN login_info AS li ON rp.`loginInfoId` = li.`id`");
		sql.append(" LEFT JOIN login_info AS lic ON lic.`id` = rp.`createUser`");
		sql.append(" LEFT JOIN login_info AS liu ON liu.`id` = rp.`updateUser`");
		sql.append(" WHERE 1 = 1 AND rp.deleteFlag=0 ");
		sql.append(" AND FIND_IN_SET(li.`companyId`,getChildList(" + form.getCompanyId() + "))");
		if (StringUtil.isNotBlank(form.getPhone())) {
			sql.append(" AND rp.phone = '" + form.getPhone() + "'");
		}
		sql.append(" ORDER BY rp.createTime DESC");
		if (form.getIsShowAll() == 0) {
			sql.append(" LIMIT " + (form.getCurrentPage() - 1) * form.getPageSize() + "," + form.getPageSize());
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundPrincipalBean principal = null;
		List<RefundPrincipalBean> principalList = new ArrayList<RefundPrincipalBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				principal = new RefundPrincipalBean();
				principal.setId(rs.getInt("id"));
				principal.setCreateUserName(rs.getString("createUserName"));
				principal.setUpdateUserName(rs.getString("updateUserName"));
				principal.setPhone(rs.getString("phone"));
				principal.setDeleteFlag(rs.getInt("deleteFlag"));
				principal.setUserName(rs.getString("userName"));
				principal.setCreateTime(rs.getTimestamp("createTime"));
				principal.setUpdateTime(rs.getTimestamp("updateTime"));
				principalList.add(principal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getRefundPrincipalInfo--end>");
		return principalList;
	}

	@Override
	public Long getRefundPrincipalNum(RefundPrincipalForm form) {
		log.info("<RefundDaoImpl--getRefundPrincipalNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) as total FROM refund_principal AS rp");
		sql.append(" LEFT JOIN login_info AS li ON rp.`loginInfoId` = li.`id`");
		sql.append(" LEFT JOIN login_info AS lic ON lic.`id` = rp.`createUser`");
		sql.append(" LEFT JOIN login_info AS liu ON liu.`id` = rp.`updateUser`");
		sql.append(" WHERE 1 = 1 AND rp.deleteFlag=0 ");
		sql.append(" AND FIND_IN_SET(li.`companyId`,getChildList(" + form.getCompanyId() + "))");
		if (StringUtil.isNotBlank(form.getPhone())) {
			sql.append(" AND rp.phone = '" + form.getPhone() + "'");
		}
		log.info("sql语句：" + sql);
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getRefundPrincipalNum--end>");
		return total;
	}

	@Override
	public Long findRefundInfoNum(RefundRecordForm form) {
		log.info("<RefundDaoImpl--findRefundInfoNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) as total");
		sql.append(" FROM refund_record AS rr");
		sql.append(" LEFT JOIN login_info AS li ON li.`id` = rr.`createUser`");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND FIND_IN_SET(rr.`companyId`,getChildList(" + form.getCompanyId() + "))");
		if (StringUtil.isNotBlank(form.getPayCode())) {
			sql.append(" AND rr.payCode = '" + form.getPayCode() + "'");
		}
		if (StringUtil.isNotBlank(form.getPtCode())) {
			sql.append(" AND rr.ptCode = '" + form.getPtCode() + "'");
		}
		if (form.getType() != null) {
			sql.append(" AND rr.`type`= '" + form.getType() + "'");
		}
		if (form.getRefundPlatform() != null) {
			sql.append(" AND rr.refundPlatform = '" + form.getRefundPlatform() + "'");
		}
		if (form.getState() != null) {
			sql.append(" AND rr.state = '" + form.getState() + "'");
		}
		if (form.getStartTime() != null) {
			sql.append(" AND rr.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()) + "'");
		}
		if (form.getEndTime() != null) {
			sql.append(" AND rr.createTime <= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()) + "'");
		}
		log.info("sql语句：" + sql);
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}

		log.info("<RefundDaoImpl--findRefundInfoNum--end>");
		return total;
	}

	@Override
	public RefundOrderInfo findOrder(String payCode, Integer orderType) {
		log.info("<RefundDaoImpl--findOrder--start>");
		StringBuffer sql = new StringBuffer();
		if (orderType == 1) {
			// 机器
			sql.append(
					" SELECT ptCode,price,payType,num FROM pay_record WHERE state = 10001 AND ptCode IS NOT NULL AND payCode = '"
							+ payCode + "'");
		} else if (orderType == 2) {
			// 商城
			sql.append(
					" SELECT ptCode,nowprice AS price,payType FROM store_order WHERE (state = 10001 or state = 200004) AND ptCode IS NOT NULL AND payCode = '"
							+ payCode + "'");
		} else if (orderType == 3) {
			// 拼团
			sql.append(
					" SELECT ptCode,nowprice AS price,payType FROM store_order WHERE state = 10001 AND ptCode IS NOT NULL AND payCode = '"
							+ payCode + "'");
		} else if (orderType == 4){ //充值
			sql.append("select ptCode,price,1 as payType,friendCustomerId,customerId from member_order  WHERE state = 10001 AND ptCode IS NOT NULL AND payCode='"
					+payCode+"' ");
		}else if (orderType == 6) {//视觉
			// 机器
			sql.append(
					" SELECT ptCode,price,payType,num FROM pay_record_vision WHERE state = 10001 AND ptCode IS NOT NULL AND payCode = '"
							+ payCode + "'");
		}else {
			return null;
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundOrderInfo orderInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				orderInfo = new RefundOrderInfo();
				orderInfo.setPrice(rs.getBigDecimal("price"));
				orderInfo.setPayType(rs.getInt("payType"));
				orderInfo.setPtCode(rs.getString("ptCode"));
				if(orderType==1 || orderType==6) {
					orderInfo.setNum(rs.getInt("num"));
				}
				
				if(orderType == 4) {
					orderInfo.setCustomerId(rs.getLong("customerId"));
					orderInfo.setFriendCustomerId(rs.getLong("friendCustomerId"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--findOrder--end>");
		return orderInfo;
	}

	@Override
	public String getPrincipalInfoById(Long loginInfoId) {
		log.info("<RefundDaoImpl--getPrincipalInfoById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT phone FROM refund_principal WHERE deleteFlag = 0 AND loginInfoId = '" + loginInfoId + "'");

		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String phone = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				phone = rs.getString("phone");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getPrincipalInfoById--end>");
		return phone;
	}

	@Override
	public RefundPrincipalBean getPrincipalByLid(Long loginInfoId) {
		log.info("<RefundDaoImpl--getPrincipalByLid--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT phone,deleteFlag,createUser,updateUser,createTime,updateTime ");
		sql.append(" FROM refund_principal WHERE deleteFlag =0 AND loginInfoId = '" + loginInfoId + "'");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundPrincipalBean principal = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				principal = new RefundPrincipalBean();
				principal.setPhone(rs.getString("phone"));
				principal.setDeleteFlag(rs.getInt("deleteFlag"));
				principal.setCreateUser(rs.getLong("createUser"));
				principal.setUpdateUser(rs.getLong("updateUser"));
				principal.setCreateTime(rs.getTimestamp("createTime"));
				principal.setUpdateTime(rs.getTimestamp("updateTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getPrincipalByLid--end>");
		return principal;
	}

	@Override
	public TotalResultBean<List<RefundApplicationDto>> findRefundApplication(RefundApplicationForm form) {
		log.info("<RefundDaoImpl--findRefundApplication--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SQL_CALC_FOUND_ROWS ra.id,ra.orderType,ra.payCode,ra.ptCode,ra.phone,ra.reason,ra.state,");
		sql.append(" ra.refundPrice,ra.createTime,ra.updateTime,ra.`updateUser`,li.`name` AS updateUserName ");
		sql.append(" FROM refund_application AS ra LEFT JOIN login_info AS li ON li.`id` = ra.`updateUser`");
		sql.append("  WHERE FIND_IN_SET(ra.companyId,getChildList("+form.getCompanyId()+"))");
		if (StringUtils.isNotBlank(form.getPayCode())) {
			sql.append(" AND ra.payCode = '" + form.getPayCode() + "'");
		}
		if (StringUtils.isNotBlank(form.getPhone())) {
			sql.append(" AND ra.phone = '" + form.getPhone() + "'");
		}
		if (form.getOrderType() != null) {
			sql.append(" AND ra.orderType = '" + form.getOrderType() + "'");
		}
		if (form.getState() != null) {
			sql.append(" AND ra.state = '" + form.getState() + "'");
		}
		if (form.getStartTime() != null) {
			sql.append(" AND ra.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()) + "'");
		}
		if (form.getEndTime() != null) {
			sql.append(" AND ra.createTime <= '" + DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()) + "'");
		}
		sql.append(" order by ra.createTime desc");
		if (form.getIsShowAll() == 0) {
			sql.append(" limit " + (form.getCurrentPage() - 1) * form.getPageSize() + "," + form.getPageSize());
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psTotal = null;
		ResultSet rsTotal = null;
		List<RefundApplicationDto> applicationList = new ArrayList<RefundApplicationDto>();
		RefundApplicationDto application = null;
		Long total = 0L;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				application = new RefundApplicationDto();
				application.setCreateTime(rs.getTimestamp("createTime"));
				application.setId(rs.getLong("id"));
				application.setOrderType(rs.getInt("orderType"));
				application.setPayCode(rs.getString("payCode"));
				application.setPtCode(rs.getString("ptCode"));
				application.setPhone(rs.getString("phone"));
				application.setReason(rs.getString("reason"));
				application.setRefundPrice(rs.getBigDecimal("refundPrice"));
				application.setState(rs.getInt("state"));
				application.setUpdateTime(rs.getTimestamp("updateTime"));
				application.setUpdateUser(rs.getLong("updateUser"));
				application.setUpdateUserName(rs.getString("updateUserName"));
				applicationList.add(application);
			}
			psTotal = conn.prepareStatement(TotalResultBean.TOTAL_SQL);
			rsTotal = psTotal.executeQuery();
			if(rsTotal != null && rsTotal.next()){
				total = rsTotal.getLong("total");
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			this.closeConnection(rsTotal, psTotal, null);
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--findRefundApplication--end>");
		return new TotalResultBean<>(total,applicationList);
	}

	@Override
	public boolean updateRefundApplication(String payCode, Integer state, String backReason,Long updateUser) {
		log.info("<RefundDaoImpl--updateRefundApplication--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE refund_application SET state = ?,updateTime = NOW(),backReason=?,updateUser=? WHERE payCode = ?");
		param.add(state);
		param.add(backReason);
		param.add(updateUser);
		param.add(payCode);
		int upate = upate(sql.toString(), param);
		log.info("<RefundDaoImpl--updateRefundApplication--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePayRecordItem(RefundDto refundDto) {
		List<RefundItemDto> refundItemDtos =refundDto.getItemList();
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		conn = openConnection();
		try {
			conn.setAutoCommit(false);
			for (RefundItemDto r: refundItemDtos) {
				StringBuffer sql = new StringBuffer();
				if(refundDto.getOrderType()!=null && refundDto.getOrderType()==6) {//视觉退款
					sql.append(" update pay_record_item_vision set finalNum = '"+r.getQuantity()+"' , finalTotalPrice = '"+r.getPrice()+"' ");
					sql.append(" where basicItemId = '"+r.getGoods_id()+"'  ");
				}else {
					sql.append(" update pay_record_item set finalNum = '"+r.getQuantity()+"' , finalTotalPrice = '"+r.getPrice()+"' ");
					sql.append(" where basicItemId = '"+r.getGoods_id()+"'  ");
				}
				ps = conn.prepareStatement(sql.toString());
				rs = ps.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			this.closeConnection(null, ps, conn);
		}
		
		return true;
	}

}
