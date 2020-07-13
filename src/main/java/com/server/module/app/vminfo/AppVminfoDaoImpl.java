package com.server.module.app.vminfo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.app.replenish.ReplenishForm;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository("aliVminfoDao")
public class AppVminfoDaoImpl extends MySqlFuns implements AppVminfoDao{

	public static Logger log = LogManager.getLogger(AppVminfoDaoImpl.class);
	@Override
	public List<VminfoDto> queryVMList1(String condition,String companyIds,Integer machineType,Integer state) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName,vmi.machineVersion,vmi.companyId as companyId");
		sql.append(" FROM vending_machines_info vmi ");
		//sql.append(" vmi.machinesBaseId = vmb.id");
		//sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" WHERE  vmi.companyId IN ("+companyIds+")");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" and (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}

		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setMachineVersion(rs.getInt("machineVersion"));
				//vminfo.setCompanyName(rs.getString("companyName"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryVMList--end>");
		return vminfoList;
	}

	@Override
	public List<VminfoDto> queryVMListForReplenishMan(String condition,String companyIds,Integer state) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName,vmi.machineVersion,vmi.companyId as companyId");
		sql.append(" FROM vending_machines_info vmi ");
		//sql.append(" vmi.machinesBaseId = vmb.id");
		//sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" inner join replenish_company_machines rcm on rcm.code=vmi.code where rcm.companyId IN ("+companyIds+")");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" and (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}
		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setMachineVersion(rs.getInt("machineVersion"));
				//vminfo.setCompanyName(rs.getString("companyName"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryVMListForReplenishMan--end>");
		return vminfoList;
	}
	
	@Override
	public List<VminfoDto> queryVMList(String condition,String companyIds,Integer machineType,Integer state) {
		log.info("<AliVminfoDaoImpl--queryVMList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName,c.id as companyId,c.name as companyName");
		sql.append(" FROM vending_machines_info vmi INNER JOIN vending_machines_base vmb ON");
		sql.append(" vmi.machinesBaseId = vmb.id");
		sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" WHERE vmi.machineVersion=1 and vmi.companyId IN ("+companyIds+")");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" and (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}
		if(machineType!=null){
			sql.append(" AND vmb.machinesTypeId = "+machineType);
		}
		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setCompanyName(rs.getString("companyName"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryVMList--end>");
		return vminfoList;
	}

	@Override
	public List<VminfoDto> queryOwnVMList(String condition, Long dutyId, Integer machineType,Integer state) {
		log.info("<AliVminfoDaoImpl--queryOwnVMList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName");
		sql.append(" FROM vending_machines_info vmi INNER JOIN vending_machines_base vmb ON");
		sql.append(" vmi.machinesBaseId = vmb.id");
		sql.append(" inner join vending_line as vl on vl.id = vmi.lineId");
		sql.append(" where FIND_IN_SET("+dutyId + ",vl.dutyId )");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" AND (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}
		if(machineType!=null){
			sql.append(" and vmb.machinesTypeId = "+machineType);
		}
		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryOwnVMList--end>");
		return vminfoList;
	}

	@Override
	public List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm) {
		log.info("<AliVminfoDaoImpl--queryReplenishVMList--start>");
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT a.* FROM (");
		if(replenishForm.getVersion()==1)
			sql.append("SELECT vmi.CODE AS vmCode,vmi.locatoinName,vmi.lat,vmi.lon,vmw.ratio FROM vending_machines_info vmi INNER JOIN (SELECT vmw.vmCode,(SUM(vmw.fullNum) - SUM(vmw.num)) / SUM(vmw.fullNum) AS ratio FROM vending_machines_way_item vmw GROUP BY vmw.vmCode) AS vmw ON vmi. CODE = vmw.vmCode LEFT JOIN vending_line AS vl ON vmi.lineId = vl.id LEFT JOIN replenish_company_machines rcm ON vmi.CODE = rcm.CODE WHERE vmi.machineVersion = 1");
        else if(replenishForm.getVersion()==2) {
			//sql.append("SELECT vmi.CODE AS vmCode,vmi.locatoinName,vmi.lat,vmi.lon,vmw.ratio FROM vending_machines_info vmi INNER JOIN (SELECT vmw.vmCode,(SUM(vmw.fullNum) - SUM(vmw.num)) / SUM(vmw.fullNum) AS ratio FROM vending_machines_way_item vmw GROUP BY vmw.vmCode) AS vmw ON vmi. CODE = vmw.vmCode LEFT JOIN vending_line AS vl ON vmi.lineId = vl.id LEFT JOIN replenish_company_machines rcm ON vmi.CODE = rcm.CODE WHERE vmi.machineVersion = 2");
			sql.append(" SELECT vmi.CODE AS vmCode,vmi.locatoinName,vmi.lat,vmi.lon,vmw.ratio FROM vending_machines_info vmi INNER JOIN (" );
		    sql.append(" SELECT vmw.vmCode,(CASE WHEN vmi.machineType = 1 THEN (SUM(vmw.fullNum) - SUM(vmw.num)) / SUM(vmw.fullNum) WHEN vmi.machineType = 2 THEN (vmw.maxCapacity - SUM(vmw.num)) / vmw.maxCapacity END) AS ratio ");
		    sql.append(" FROM vending_machines_way_item vmw INNER JOIN vending_machines_info vmi ON vmi. CODE = vmw.vmCode ");
		    sql.append(" GROUP BY vmw.vmCode) AS vmw ON vmi. CODE = vmw.vmCode LEFT JOIN vending_line AS vl ON vmi.lineId = vl.id LEFT JOIN replenish_company_machines rcm ON vmi.CODE = rcm.CODE WHERE vmi.machineVersion = 2");
        }
		if(replenishForm.getRate() == null){
			replenishForm.setRate(new BigDecimal(String.valueOf(0.7)));
		}
		sql.append(" AND vmi.state=20001 AND vmw.ratio>="+replenishForm.getRate().doubleValue());


		//"AND (vmw.fullNum-vmw.num)/vmw.fullNum>0.1 AND vmi.state=20001  AND vmi.companyId IN(76) ORDER BY (vmw.fullNum-vmw.num)/vmw.fullNum DESC")

		if(replenishForm.getLineId()!=null){
			if(replenishForm.getLineId().intValue()!=0)
				sql.append(" and vmi.lineId="+replenishForm.getLineId());
		}
		if(replenishForm.getAreaId()!=null){
			if(replenishForm.getAreaId().intValue()!=0)
				sql.append(" and vmi.areaId="+replenishForm.getAreaId());
		}
		if(StringUtil.isNotBlank(replenishForm.getVmCode())){
			sql.append(" and vmi.code like '%"+replenishForm.getVmCode()+"%'");
		}
		if(StringUtil.isNotBlank(replenishForm.getAddress())){
			sql.append(" and vmi.locatoinName like '%"+replenishForm.getAddress()+"%'");
		}
		if(replenishForm.getOtherCompanyId()!=null){
			sql.append(" and rcm.companyId="+replenishForm.getOtherCompanyId());
		}
		log.info("companyId="+replenishForm.getCompanyId());
		System.out.println("companyId================================="+replenishForm.getCompanyId());
		if(replenishForm.getType()!=null){
			sql.append(" and rcm.companyId in ("+replenishForm.getCompanyIds()+")");
		}
		else if(replenishForm.getCompanyId()!=null){
			sql.append(" and vmi.companyId = "+replenishForm.getCompanyId());
		}else if(StringUtil.isNotBlank(replenishForm.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+replenishForm.getCompanyIds()+")");
		}
		if(replenishForm.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+replenishForm.getDutyId() +",vl.dutyId)");
		}
        sql.append(" GROUP BY vmi.code,vmi.locatoinName ,vmi.lat,vmi.lon) a ORDER BY a.ratio DESC ");
		//sql.append(" vmi.code AS vmCode,vmi.locatoinName,vmi.lat,vmi.lon ORDER BY (vmw.fullNum-vmw.num)/vmw.fullNum DESC");

		//sql.append(" and temp.num/temp.fullNum<="+(new BigDecimal(1).subtract(replenishForm.getRate())));
		//sql.append(" ) AS temp GROUP BY temp.vendingMachinesCode HAVING SUM(temp.num)/SUM(temp.fullNum)<="+(new BigDecimal(1).subtract(replenishForm.getRate())));

		//sql.append(") AS tt ORDER BY tt.ratio DESC ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VminfoDto vminfo  = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locatoinName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				//vminfo.setCompanyName(rs.getString("companyName"));
				vminfo.setRatio(rs.getDouble("ratio"));
				vminfo.setLon(rs.getDouble("lon"));
				vminfo.setLat(rs.getDouble("lat"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			this.closeConnection(rs, ps, conn);
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryReplenishVMList--end>");
		return vminfoList;
	}

	@Override
	public Integer queryAllMachinesNum(Integer companyId, String companyIds,Integer state) {
		log.info("<AliVminfoDaoImpl--queryAllMachinesNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS num FROM vending_machines_info AS vmi WHERE vmi.`companyId` IN (");
		if(companyId!=null){
			sql.append(companyId + ")");
		}else{
			sql.append(companyIds +")");
		}
		if(state!=null){
			sql.append("  and vmi.state = "+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer num = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				num = rs.getInt("num");
			}
		} catch (SQLException e) {
			this.closeConnection(rs, ps, conn);
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryAllMachinesNum--end>");
		return num;
	}

	@Override
	public Integer queryAllMachinesNumForReplenishMan(Integer companyId, String companyIds,Integer state) {
		log.info("<AliVminfoDaoImpl--queryAllMachinesNumForReplenishMan--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS num FROM vending_machines_info AS vmi INNER JOIN replenish_company_machines rcm on rcm.code=vmi.code WHERE rcm.`companyId` IN (");
		if(companyId!=null){
			sql.append(companyId + ")");
		}else{
			sql.append(companyIds +")");
		}
		if(state!=null){
			sql.append("  and vmi.state = "+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer num = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				num = rs.getInt("num");
			}
		} catch (SQLException e) {
			this.closeConnection(rs, ps, conn);
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryAllMachinesNumForReplenishMan--end>");
		return num;
	}

	@Override
	public VminfoDto queryByVmCode(String vmCode) {
		log.info("<AliVminfoDaoImpl--queryByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT `code`,companyId FROM `vending_machines_info` WHERE `code` ='"+vmCode+"'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setVmCode(rs.getString("code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryByVmCode--end>");
		return vminfo;
	}


	@Override
	public List<WayDto1> queryWayItem(List<String> listCode,int version) {
		String  sql="";
		if(version==1)
		  sql="SELECT w.vendingMachinesCode AS vmCode,w.wayNumber,w.num,w.fullNum,b.name,b.simpleName FROM vending_machines_way w INNER JOIN vending_machines_item i ON w.itemId=i.id  LEFT JOIN item_basic b ON i.basicItemId=b.id WHERE w.state=30001 AND w.vendingMachinesCode IN ";
        else if(version==2)
          sql="SELECT w.vmCode  AS vmCode,w.wayNumber,w.num,w.fullNum,b.name,b.simpleName FROM vending_machines_way_item w " +
				  "  LEFT JOIN item_basic b ON w.basicItemId=b.id WHERE  w.vmCode IN ";

		StringBuilder inCodes=new StringBuilder("(");
		for (int i = 0; i < listCode.size(); i++) {
			inCodes.append("'"+listCode.get(i)+"'");
			if(i!=listCode.size()-1){
				inCodes.append(",");
			}
		}
		inCodes.append(")");
        sql=sql+inCodes.toString();
        log.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WayDto1> list= Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				WayDto1 wayDto = new WayDto1();
				wayDto.setFullNum(rs.getInt("fullNum"));
				wayDto.setNum(rs.getInt("num"));
				wayDto.setVmCode(rs.getString("vmCode"));
				wayDto.setItemName(rs.getString("name"));
				wayDto.setSimpleName(rs.getString("simpleName"));
				wayDto.setWayNumber(rs.getInt("wayNumber"));
				list.add(wayDto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}


        return list;
	}
}
