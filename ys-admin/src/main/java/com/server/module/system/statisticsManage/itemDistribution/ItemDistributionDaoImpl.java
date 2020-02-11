package com.server.module.system.statisticsManage.itemDistribution;

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

import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class ItemDistributionDaoImpl extends MySqlFuns implements ItemDistributionDao{

	private final static Logger log = LogManager.getLogger(ItemDistributionDaoImpl.class);

	@Override
	public List<ItemDistributionDto> queryDistribution(ItemDistributionForm form) {
		log.info("<ItemDistributionDaoImpl--queryDistribution--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ib.`name` AS itemName,vmi.`code` AS vmCode,vmi.`locatoinName` AS address,");
		sql.append(" vmw.`num`,vmw.`fullNum`,vmi2.price,c.monthNum,c.monthNum/30 AS dayNum,vmw.num/(c.monthNum/30) AS day ");
		sql.append(" FROM vending_machines_way AS vmw INNER JOIN vending_machines_item AS vmi2 ON vmi2.`id` = vmw.`itemId`");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmi2.`basicItemId`");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmw.vendingMachinesCode = vmi.`code`");
		sql.append(" LEFT JOIN  (select p.vendingMachinesCode,r.basicItemId,sum(r.num) AS monthNum from pay_record_item r  ");
		sql.append(" INNER JOIN  pay_record  p ON r.payRecordId=p.id where p.state=10001 ");
		sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(p.createtime) group by p.vendingMachinesCode,r.basicItemId) c");
		sql.append(" on vmw.vendingMachinesCode =c.vendingMachinesCode and vmi2.basicItemId=c.basicItemId ");
		sql.append(" WHERE 1=1 AND vmi.`state` = 20001  AND vmi.machineVersion=1");
		if (form.getAreaId() != null && form.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + form.getAreaId() + "' ");
		}
		if(form.getDaySaleStart()!=null){
			sql.append(" AND c.monthNum/30 >="+form.getDaySaleStart());
		}
		if(form.getDaySaleEnd()!=null){
			sql.append(" AND c.monthNum/30 <="+form.getDaySaleEnd());
		}
		if(StringUtil.isNotBlank(form.getItemName())){
			sql.append(" AND ib.`name` LIKE '%"+form.getItemName()+"%'");
		}
		if(form.getBasicItemId() != null){
			sql.append(" AND ib.`id` = "+form.getBasicItemId());
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" AND vmi.`code` = '"+form.getVmCode()+"'");
		}
		if(form.getCompanyId() !=null){
			sql.append(" AND FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+")) ");
		}
		sql.append(" ORDER BY vmw.num");
		if(form.getIsShowAll() == 0){
			sql.append(" limit "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemDistributionDto> disList = new ArrayList<ItemDistributionDto>();
		ItemDistributionDto dis = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				dis = new ItemDistributionDto();
				dis.setAddress(rs.getString("address"));
				dis.setFullNum(rs.getInt("fullNum"));
				dis.setItemName(rs.getString("itemName"));
				dis.setItemNum(rs.getInt("num"));
				dis.setVmCode(rs.getString("vmCode"));
				dis.setPrice(rs.getDouble("price"));
				dis.setMonthNum(rs.getInt("monthNum"));
				if(rs.getBigDecimal("dayNum")!=null) {
					dis.setDayNum(rs.getBigDecimal("dayNum").setScale(2,BigDecimal.ROUND_HALF_UP));
				}else {
					dis.setDayNum(BigDecimal.valueOf(0));
				}
				if(rs.getBigDecimal("day")!=null) {
					dis.setDay(rs.getBigDecimal("day").setScale(2,BigDecimal.ROUND_HALF_UP));
				}else {
					dis.setDay(BigDecimal.valueOf(0));
				}
				disList.add(dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemDistributionDaoImpl--queryDistribution--end>");
		return disList;
	}

	@Override
	public Long queryDistributionNum(ItemDistributionForm form) {
		log.info("<ItemDistributionDaoImpl--queryDistributionNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) as total FROM vending_machines_way AS vmw");
		sql.append(" INNER JOIN vending_machines_item AS vmi2 ON vmi2.`id` = vmw.`itemId`");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmi2.`basicItemId`");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmw.vendingMachinesCode = vmi.`code`");
		sql.append(" LEFT JOIN  (select p.vendingMachinesCode,r.basicItemId,sum(r.num) AS monthNum from pay_record_item r  ");
		sql.append(" INNER JOIN  pay_record  p ON r.payRecordId=p.id where p.state=10001 ");
		sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(p.createtime) group by p.vendingMachinesCode,r.basicItemId) c");
		sql.append(" on vmw.vendingMachinesCode =c.vendingMachinesCode and vmi2.basicItemId=c.basicItemId ");
		sql.append(" WHERE 1=1 AND vmi.`state` = 20001 AND vmi.machineVersion=1");
		if(form.getDaySaleStart()!=null){
			sql.append(" AND c.monthNum/30 >="+form.getDaySaleStart());
		}
		if(form.getDaySaleEnd()!=null){
			sql.append(" AND c.monthNum/30 <="+form.getDaySaleEnd());
		}
		if(StringUtil.isNotBlank(form.getItemName())){
			sql.append(" AND ib.`name` LIKE '%"+form.getItemName()+"%'");
		}
		if(form.getBasicItemId() != null){
			sql.append(" AND ib.`id` = "+form.getBasicItemId());
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" AND vmi.`code` = '"+form.getVmCode()+"'");
		}
		if(form.getCompanyId() !=null){
			sql.append(" AND FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+")) ");
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
		log.info("<ItemDistributionDaoImpl--queryDistributionNum--end>");
		return total;
	}

	@Override
	public List<ItemDistributionDto> queryDisTributionVer2(ItemDistributionForm form) {
		log.info("<ItemDistributionDaoImpl--queryDisTributionVer2--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ib.`name` AS itemName,vmwi.`vmCode`,vmi.`locatoinName` AS address,");
		sql.append(" vmwi.`num`,vmwi.`fullNum`,vmwi.`price`,c.monthNum,c.monthNum/30 AS dayNum,vmwi.num/(c.monthNum/30) AS day ");
		sql.append(" FROM `vending_machines_way_item` AS vmwi");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmwi.`basicItemId`");
		sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.`code` = vmwi.`vmCode`");
		sql.append(" LEFT JOIN  (select p.vendingMachinesCode,r.basicItemId,sum(r.num) AS monthNum from pay_record_item r ");
		sql.append(" INNER JOIN  pay_record  p ON r.payRecordId=p.id where p.state=10001  ");
		sql.append(" and DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(p.createtime) group by p.vendingMachinesCode,r.basicItemId) c");
		sql.append(" on vmwi.vmCode =c.vendingMachinesCode and vmwi.basicItemId=c.basicItemId ");
		sql.append(" WHERE vmi.`state` = 20001 AND vmi.`machineVersion` = 2");
		if (form.getAreaId() != null && form.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + form.getAreaId() + "' ");
		}
		if(form.getDaySaleStart()!=null){
			sql.append(" AND c.monthNum/30 >="+form.getDaySaleStart());
		}
		if(form.getDaySaleEnd()!=null){
			sql.append(" AND c.monthNum/30 <="+form.getDaySaleEnd());
		}
		if(StringUtil.isNotBlank(form.getItemName())){
			sql.append(" AND ib.`name` LIKE '%"+form.getItemName()+"%'");
		}
		if(form.getBasicItemId() != null){
			sql.append(" AND ib.`id` = "+form.getBasicItemId());
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" AND vmi.`code` = '"+form.getVmCode()+"'");
		}
		if(form.getCompanyId() !=null){
			sql.append(" AND FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+")) ");
		}
		sql.append(" ORDER BY vmwi.`num`");
		if(form.getIsShowAll() == 0){
			sql.append(" limit "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemDistributionDto> disList = new ArrayList<ItemDistributionDto>();
		ItemDistributionDto dis = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				dis = new ItemDistributionDto();
				dis.setAddress(rs.getString("address"));
				dis.setFullNum(rs.getInt("fullNum"));
				dis.setItemName(rs.getString("itemName"));
				dis.setItemNum(rs.getInt("num"));
				dis.setVmCode(rs.getString("vmCode"));
				dis.setPrice(rs.getDouble("price"));
				dis.setMonthNum(rs.getInt("monthNum"));
				if(rs.getBigDecimal("dayNum")!=null) {
					dis.setDayNum(rs.getBigDecimal("dayNum").setScale(2,BigDecimal.ROUND_HALF_UP));
				}else {
					dis.setDayNum(BigDecimal.valueOf(0));
				}
				if(rs.getBigDecimal("day")!=null) {
					dis.setDay(rs.getBigDecimal("day").setScale(2,BigDecimal.ROUND_HALF_UP));
				}else {
					dis.setDay(BigDecimal.valueOf(0));
				}
				disList.add(dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemDistributionDaoImpl--queryDisTributionVer2--end>");
		return disList;
	}

	@Override
	public Long queryDistributionNumVer2(ItemDistributionForm form) {
		log.info("<ItemDistributionDaoImpl--queryDistributionNumVer2--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) as total ");
		sql.append(" FROM `vending_machines_way_item` AS vmwi");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmwi.`basicItemId`");
		sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.`code` = vmwi.`vmCode`");
		sql.append(" LEFT JOIN  (select p.vendingMachinesCode,r.basicItemId,sum(r.num) AS monthNum from pay_record_item r ");
		sql.append(" INNER JOIN  pay_record  p ON r.payRecordId=p.id where p.state=10001  ");
		sql.append(" and DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(p.createtime) group by p.vendingMachinesCode,r.basicItemId) c");
		sql.append(" on vmwi.vmCode =c.vendingMachinesCode and vmwi.basicItemId=c.basicItemId ");
		sql.append(" WHERE vmi.`state` = 20001 AND vmi.`machineVersion` = 2");
		if(form.getDaySaleStart()!=null){
			sql.append(" AND c.monthNum/30 >="+form.getDaySaleStart());
		}
		if(form.getDaySaleEnd()!=null){
			sql.append(" AND c.monthNum/30 <="+form.getDaySaleEnd());
		}
		if(StringUtil.isNotBlank(form.getItemName())){
			sql.append(" AND ib.`name` LIKE '%"+form.getItemName()+"%'");
		}
		if(form.getBasicItemId() != null){
			sql.append(" AND ib.`id` = "+form.getBasicItemId());
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" AND vmi.`code` = '"+form.getVmCode()+"'");
		}
		if(form.getCompanyId() !=null){
			sql.append(" AND FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+")) ");
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
		log.info("<ItemDistributionDaoImpl--queryDistributionNumVer2--end>");
		return total;
	}
}
