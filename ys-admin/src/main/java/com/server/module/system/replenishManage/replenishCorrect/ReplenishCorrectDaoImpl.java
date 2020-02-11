package com.server.module.system.replenishManage.replenishCorrect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ddf.EscherSerializationListener;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class ReplenishCorrectDaoImpl extends MySqlFuns implements ReplenishCorrectDao{

	private final static Logger log = LogManager.getLogger(ReplenishCorrectDaoImpl.class);

	@Override
	public List<ReplenishDto> getReplenishInfo(ReplenishForm form) {
		log.info("<ReplenishCorrectDaoImpl--getReplenishInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vwl.state,vwl.id AS replenishId,vwl.basicItemId,vwl.vmCode,vwl.wayNumber,vwl.preNum,vwl.num,vwl.createTime,");
		sql.append(" ib.`name` AS itemName,vwl.opType,ib.pic FROM replenish_record AS vwl");
		sql.append(" LEFT JOIN item_basic AS ib ON vwl.`basicItemId` = ib.`id`");
		
	    sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.code = vwl.vmCode ");

		//sql.append(" LEFT JOIN vending AS ib ON vwl.`basicItemId` = ib.`id`");
	    

		sql.append(" WHERE 1 = 1");
		
		sql.append(" and FIND_IN_SET(vmi.companyId,(getChildList("+form.getCompanyId() +"))) ");

		if(StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" and vwl.vmCode = '"+form.getVmCode()+"'");
		}
		sql.append(" AND vwl.createTime > '"+DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime())+"' ");
		sql.append(" AND vwl.createTime < '"+DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime())+"'");
		sql.append(" AND (vwl.opType = 2 OR vwl.opType = 4) ORDER BY vwl.createTime ASC");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ReplenishDto> replenishList = new ArrayList<ReplenishDto>();
		ReplenishDto replenish = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				replenish = new ReplenishDto();
				replenish.setBasicItemId(rs.getLong("basicItemId"));
				replenish.setItemName(rs.getString("itemName"));
				replenish.setNum(rs.getInt("num"));
				replenish.setPreNum(rs.getInt("preNum"));
				replenish.setReplenishTime(rs.getTimestamp("createTime"));
				replenish.setReplenishId(rs.getLong("replenishId"));
				replenish.setVmCode(rs.getString("vmCode"));
				replenish.setWayNumber(rs.getInt("wayNumber"));
				replenish.setOpType(rs.getInt("opType"));
				replenish.setPic(rs.getString("pic"));
				replenish.setState(rs.getInt("state"));
				if(replenish.getState()==1) {
					replenish.setStateName("已修正");
				}
				if(replenish.getOpType()==2) {
					replenish.setOpTypeName("补货");
				}else if(replenish.getOpType()==4) {
					replenish.setOpTypeName("校准");
				}else if(replenish.getOpType()==5) {
					replenish.setOpTypeName("换货");
				}else if(replenish.getOpType()==6) {
					replenish.setOpTypeName("撤货到其他机器");
				}else if(replenish.getOpType()==3) {
					replenish.setOpTypeName("重置水量");
				}
				replenishList.add(replenish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishCorrectDaoImpl--getReplenishInfo--end>");
		return replenishList;
	}

	@Override
	public List<ReplenishDto> getReplenishProcess(ReplenishForm form) {
		log.info("<ReplenishCorrectDaoImpl--getReplenishInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vwl.changeNum,vwl.finalNum,vwl.id AS replenishId,vwl.basicItemId,vwl.vmCode,vwl.wayNumber,vwl.preNum,vwl.num,vwl.createTime,");
		sql.append(" ib.`name` AS itemName,vwl.opType,ib.pic FROM replenish_record AS vwl");
		sql.append(" LEFT JOIN item_basic AS ib ON vwl.`basicItemId` = ib.`id`");
		sql.append(" left join vending_machines_info vmi on vmi.code=vwl.vmCode");
		sql.append(" left join vending_machines_base vmb on vmb.id=vmi.machinesBaseId");

		sql.append(" WHERE 1 = 1");
		
		sql.append(" and FIND_IN_SET(vmi.companyId,(getChildList("+form.getCompanyId() +"))) ");
		
		sql.append(" and vwl.vmCode = '"+form.getVmCode()+"' AND vwl.createTime > '"+DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime())+"' ");
		sql.append(" AND vwl.createTime < '"+DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime())+"'");
		sql.append(" AND vwl.createTime < '"+DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime())+"'");
		sql.append(" and FIND_IN_SET(vmi.companyId,(getChildList("+form.getCompanyId() +"))) ");
		sql.append(" AND (vmb.mainProgramVersion like '%0507%' or vmb.mainProgramVersion like '%0812%') ORDER BY vwl.createTime ASC");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ReplenishDto> replenishList = new ArrayList<ReplenishDto>();
		ReplenishDto replenish = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				replenish = new ReplenishDto();
				if(rs.getBigDecimal("changeNum")!=null) {
					replenish.setChangeNum(rs.getInt("changeNum"));
				}
				if(rs.getBigDecimal("finalNum")!=null) {
					replenish.setFinalNum(rs.getInt("finalNum"));
				}
				replenish.setBasicItemId(rs.getLong("basicItemId"));
				replenish.setItemName(rs.getString("itemName"));
				replenish.setNum(rs.getInt("num"));
				replenish.setPreNum(rs.getInt("preNum"));
				replenish.setReplenishTime(rs.getTimestamp("createTime"));
				replenish.setReplenishId(rs.getLong("replenishId"));
				replenish.setVmCode(rs.getString("vmCode"));
				replenish.setWayNumber(rs.getInt("wayNumber"));

				replenish.setOpType(rs.getInt("opType"));
				if(replenish.getOpType()==2) {
					replenish.setOpTypeName("补货");
				}else if(replenish.getOpType()==4) {
					replenish.setOpTypeName("校准");
				}else if(replenish.getOpType()==5) {
					replenish.setOpTypeName("换货");
				}else if(replenish.getOpType()==6) {
					replenish.setOpTypeName("撤货到其他机器");
				}else if(replenish.getOpType()==3) {
					replenish.setOpTypeName("重置水量");
				}
				replenish.setPic(rs.getString("pic"));
				replenishList.add(replenish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishCorrectDaoImpl--getReplenishInfo--end>");
		return replenishList;
	}
	
	@Override
	public boolean updateReplenishInfo(String ids) {
		log.info("<ReplenishCorrectDaoImpl--updateReplenishInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE replenish_record SET opType = 2,state = 1 WHERE opType = 4 AND id IN("+ids+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<ReplenishCorrectDaoImpl--updateReplenishInfo--end>");
		if(result>0){
			return true;
		}
		return false;
	}
}
