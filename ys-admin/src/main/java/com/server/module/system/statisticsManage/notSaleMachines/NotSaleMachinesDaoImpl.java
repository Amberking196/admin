package com.server.module.system.statisticsManage.notSaleMachines;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class NotSaleMachinesDaoImpl extends MySqlFuns implements NotSaleMachinesDao{

	private static Logger log = LogManager.getLogger(NotSaleMachinesDaoImpl.class);
	
	public List<NotSaleMachinesDto> findNotSaleMachinesByForm(NotSaleMachinesForm form){
		log.info("<NotSaleMachinesDaoImpl--findNotSaleMachinesByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT nsm.id,nsm.vmCode,nsm.startDate,nsm.endDate,nsm.createTime,c.name as companyName,vmi.locatoinName as location ");
		sql.append(" FROM `not_sale_machines` AS nsm ");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmi.code = nsm.vmCode ");
		sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" WHERE 1=1 ");
		if (form.getAreaId() != null && form.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + form.getAreaId() + "' ");
		}
		if(StringUtil.isNotBlank(form.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" and nsm.vmCode = '" + form.getVmCode() +"'");
		}
		if(form.getStartDate()!=null){
			sql.append(" and nsm.startDate< '"+ DateUtil.formatYYYYMMDDHHMMSS(form.getStartDate()) +"'");
		}
		if(form.getEndDate()!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
			Date startDate = calendar.getTime();
			sql.append(" and nsm.endDate>= '" +DateUtil.formatYYYYMMDDHHMMSS(startDate)+"'");
		}
		sql.append(" order by nsm.endDate desc,nsm.vmCode desc");
		if(form.getIsShowAll()==0){
			sql.append(" limit "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<NotSaleMachinesDto> notSaleList = new ArrayList<NotSaleMachinesDto>();
		NotSaleMachinesDto notSaleMachines = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				notSaleMachines = new NotSaleMachinesDto();
				String createTime = rs.getString("createTime");
				String startDate = rs.getString("startDate");
				String endDate = rs.getString("endDate");
				if(createTime.length()>=19){
					createTime = createTime.substring(0, 19);
				}
				if(startDate.length()>=19){
					startDate = startDate.substring(0, 19);
				}
				if(endDate.length()>=19){
					endDate = endDate.substring(0, 19);
				}
				notSaleMachines.setCreateTime(createTime);
				notSaleMachines.setEndDate(endDate);
				notSaleMachines.setStartDate(startDate);
				notSaleMachines.setId(rs.getInt("id"));
				notSaleMachines.setVmCode(rs.getString("vmCode"));
				notSaleMachines.setCompanyName(rs.getString("companyName"));
				notSaleMachines.setLocation(rs.getString("location"));
				notSaleList.add(notSaleMachines);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<NotSaleMachinesDaoImpl--findNotSaleMachinesByForm--end>");
		return notSaleList;
	}

	@Override
	public Long findNumOfNotSaleMachinesByForm(NotSaleMachinesForm form) {
		log.info("<NotSaleMachinesDaoImpl--findNumOfNotSaleMachinesByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) as total");
		sql.append(" FROM `not_sale_machines` AS nsm");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmi.code = nsm.vmCode");
		sql.append(" WHERE 1=1");
		if (form.getAreaId() != null && form.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + form.getAreaId() + "' ");
		}
		if(StringUtil.isNotBlank(form.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" and nsm.vmCode = '" + form.getVmCode() +"'");
		}
		if(form.getStartDate()!=null){
			sql.append(" and nsm.startDate>= '"+ DateUtil.formatYYYYMMDDHHMMSS(form.getStartDate()) +"'");
		}
		if(form.getEndDate()!=null){
			sql.append(" and nsm.endDate<= '" +DateUtil.formatYYYYMMDDHHMMSS(form.getEndDate())+"'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				total = rs.getLong("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<NotSaleMachinesDaoImpl--findNumOfNotSaleMachinesByForm--end>");
		return total;
	}
}
