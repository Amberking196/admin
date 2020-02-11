package com.server.module.system.statisticsManage.userActiveDegree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.commonBean.TotalResultBean;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class UserActiveDegreeDaoImpl extends MySqlFuns implements UserActiveDegreeDao{

	private final static Logger log = LogManager.getLogger(UserActiveDegreeDaoImpl.class);

	@Override
	public TotalResultBean<List<UserActiveDegreeBean>> calculateUserActiveGegree(UserActiveDegreeForm form) {
		log.info("UserActiveDegreeDaoImpl--calculateUserActiveGegree--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SQL_CALC_FOUND_ROWS DATE_FORMAT(rf.registerTime,'%Y-%m-%d') AS byDay,");
		sql.append(" SUM(IF(rf.wxOrAli=1,rf.register,0)) AS wxRegisteNum,SUM(IF(rf.wxOrAli=2,rf.register,0)) AS aliRegisteNum,");
		sql.append(" SUM(rf.noPassWordPay) AS noPwpayNum,SUM(rf.cancelNopwPay) AS cancelNopwNum,");
		sql.append(" SUM(rf.attention) AS attentionNum,SUM(rf.cancelAttention) AS cancelAttentionNum,");
		sql.append(" SUM(rf.receivePercent) AS receivePercentNum,SUM(rf.openMachines) AS openMachinesNum,");
		sql.append(" SUM(rf.createOrder) AS createOrderNum,SUM(rf.usedPercent) AS usedPercentNum,");
		sql.append(" SUM(rf.buyNum) AS totalBuyNum");
		sql.append(" FROM register_flow AS rf ");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmi.`code` = rf.`vmCode`");
		sql.append(" WHERE FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+"))");
		if(StringUtils.isNotBlank(form.getVmCode())){
			sql.append(" AND vmi.`code` = '"+form.getVmCode()+"'");
		}
		if(form.getStartTime() != null){
			sql.append(" AND rf.registerTime>='"+DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime())+"'");
		}
		if(form.getEndTime() != null){
			sql.append(" AND rf.registerTime<='"+DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime())+"'");
		}
		sql.append(" GROUP BY byDay ORDER BY rf.registerTime DESC");
		if(form.getIsShowAll() == 0){
			sql.append(" limit "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psTotal = null;
		ResultSet rsTotal = null;
		Long total = 0L;
		List<UserActiveDegreeBean> userActiveList = new ArrayList<UserActiveDegreeBean>();
		UserActiveDegreeBean userActive = null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				userActive = new UserActiveDegreeBean();
				userActive.setAllBuyNum(rs.getInt("totalBuyNum"));
				userActive.setAttentionNum(rs.getInt("attentionNum"));
				userActive.setCancelAttentionNum(rs.getInt("cancelAttentionNum"));
				userActive.setCancelNopwpayNum(rs.getInt("cancelNopwNum"));
				userActive.setCreateOrderNum(rs.getInt("createOrderNum"));
				userActive.setDate(rs.getString("byDay"));
				userActive.setNoPwpayNum(rs.getInt("noPwpayNum"));
				userActive.setOpenMachinesNum(rs.getInt("openMachinesNum"));
				userActive.setReceivePercentNum(rs.getInt("receivePercentNum"));
				userActive.setWxRegisterNum(rs.getInt("wxRegisteNum"));
				userActive.setAliRegisterNum(rs.getInt("aliRegisteNum"));
				userActive.setRegisterNum(userActive.getWxRegisterNum()+userActive.getAliRegisterNum());
				userActive.setUsedPercentNum(rs.getInt("usedPercentNum"));
				userActiveList.add(userActive);
			}
			psTotal = conn.prepareStatement(TotalResultBean.TOTAL_SQL);
			rsTotal = psTotal.executeQuery();
			if(rsTotal != null && rsTotal.next()){
				total = rsTotal.getLong("total");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			this.closeConnection(rsTotal, psTotal, null);
			this.closeConnection(rs, ps, conn);
		}
		log.info("UserActiveDegreeDaoImpl--calculateUserActiveGegree--end");
		return new TotalResultBean<List<UserActiveDegreeBean>>(total,userActiveList);
	}
}
