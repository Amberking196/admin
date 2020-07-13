package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.machinesSaleStatistics.MachinesSaleStatisticsDao;
import com.server.module.system.statisticsManage.machinesSaleStatistics.MachinesSaleStatisticsForm;
import com.server.module.system.statisticsManage.machinesSaleStatistics.PerMachinesSaleDto;
import com.server.module.system.statisticsManage.machinesSaleStatistics.SumMachinesSaleDto;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.VmInfoStateEnum;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("machinesOnSaleStatisticsDao")
public class MachinesOnSaleStatisticsDaoImpl extends BaseDao implements MachinesOnSaleStatisticsDao {

    public static Logger log = LogManager.getLogger(MachinesOnSaleStatisticsDaoImpl.class);

    @Autowired
    CompanyDao companyDaoImpl;
    
	@Override
	public ReturnDataUtil machinesOnSaleList(MachinesOnSaleStatisticsForm form) {
		 log.info("<machinesOnSaleStatisticsDao>--<machinesOnSaleList>--start");
	     StringBuffer sql = new StringBuffer();
	     ReturnDataUtil data = new ReturnDataUtil();
	     sql.append(" select companyid,basicItemId,name as goodsName,SUM(VERSION1) as version from( ");
	     sql.append(" SELECT vmi.companyId,vmi.machineVersion,vmii.basicItemId,ib.NAME,sum(vmw.num) AS version1 FROM vending_machines_info vmi ");
	     sql.append(" LEFT JOIN vending_machines_way vmw ON vmw.vendingMachinesCode = vmi. CODE ");
	     sql.append(" LEFT JOIN vending_machines_item vmii ON vmw.itemId = vmii.id  ");
	     sql.append(" LEFT JOIN item_basic ib ON ib.id = vmii.basicItemId ");
	     sql.append(" LEFT JOIN vending_line vl ON vmi.lineId = vl.id ");
	     sql.append(" LEFT JOIN company c ON vmi.companyId = c.id ");
		 sql.append(" where vmw.state=30001 and vmi.machineVersion=1 and vmw.num!=0 ");
		 if(StringUtils.isNotBlank(form.getGoodsName())){
			 sql.append(" and ib.name like '%" + form.getGoodsName() + "%' ");
		 }
	     if(form.getCompanyId()!=null) {
		     sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
	     }
	     if(form.getAreaId()!=null) {
			 sql.append(" and vl.areaId = '" + form.getAreaId() + "' ");
	     }
		 sql.append(" group by basicItemId ");
		 
		 sql.append(" union all");
		 
		 sql.append(" SELECT vmi.companyId,vmi.machineVersion,vmwi.basicItemId,ib.NAME,sum(vmwi.num) AS version2 FROM vending_machines_info vmi ");
		 sql.append(" LEFT JOIN vending_machines_way vmw ON vmw.vendingMachinesCode = vmi. CODE ");
		 sql.append(" LEFT JOIN vending_machines_way_item vmwi ON vmwi.machineWayId = vmw.id ");
		 sql.append(" LEFT JOIN item_basic ib ON ib.id = vmwi.basicItemId ");
		 sql.append(" LEFT JOIN vending_line vl ON vmi.lineId = vl.id ");
		 sql.append(" LEFT JOIN company c ON vmi.companyId = c.id ");
		 sql.append(" where vmw.state=30001 and vmi.machineVersion = 2 and vmwi.num!=0 ");
		 if(StringUtils.isNotBlank(form.getGoodsName())){
			 sql.append(" and  ib.name like '%" + form.getGoodsName() + "%' ");
		 }
	     if(form.getCompanyId()!=null) {
		     sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
	     }
	     if(form.getAreaId()!=null) {
			 sql.append(" and vl.areaId = '" + form.getAreaId() + "' ");
	     }
		 sql.append(" group by basicItemId) A ");

	     sql.append(" group by basicitemId order by version desc ");


	     log.info("查询语句：" + sql);
	     List<MachinesOnSaleDto> result = new ArrayList<MachinesOnSaleDto>();
	     Connection conn = null;
	     PreparedStatement pst = null;
	     ResultSet rs = null;
	     MachinesOnSaleDto machinesOnSaleDto = null;
         long count = 0;
	     try {
	         conn = openConnection();
	         if(form.getIsShowAll()==0) {
		         pst = conn.prepareStatement("select count(*) from ( "+sql.toString()+" )A");
		         rs = pst.executeQuery();
		         while (rs.next()) {
		        	 count=rs.getInt(1);
		         }
		         long off = (form.getCurrentPage() - 1) * form.getPageSize();
		         pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());
	         }else {
		         pst = conn.prepareStatement(sql.toString());
	         }
	         rs = pst.executeQuery();
	         while (rs != null && rs.next()) {
	        	 if(StringUtils.isNotBlank(rs.getString("goodsName"))) {
	        		 machinesOnSaleDto = new MachinesOnSaleDto();
	        		 machinesOnSaleDto.setGoodsName(rs.getString("goodsName"));
//	        		 if(rs.getInt("machineVersion") == 1) {
	        		 machinesOnSaleDto.setNum(rs.getInt("version"));
//	        		 }else {
//	        			 machinesOnSaleDto.setNum(rs.getInt("version2"));
//	        		 }
	        		 //machinesOnSaleDto.setNum(rs.getInt("version1")+rs.getInt("version2"));
	        		 machinesOnSaleDto.setBasicItemId(rs.getInt("basicItemId"));
	        		 machinesOnSaleDto.setCompanyId(rs.getInt("companyId"));
	        		 
	        		 result.add(machinesOnSaleDto);
	        	 }
	         }
	         data.setCurrentPage(form.getCurrentPage());
	         data.setTotal(count);
	         data.setReturnObject(result);
	         data.setStatus(1);
	         data.setMessage("查询成功");
	     } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e);
	     } finally {
	            this.closeConnection(rs, pst, conn);
	     }
	     log.info("<machinesOnSaleStatisticsDao>--<machinesOnSaleList>--end");
	     return data;
	}

	@Override
	public ReturnDataUtil machinesOnSaleListDetail(MachinesOnSaleStatisticsForm form) {
		 log.info("<machinesOnSaleStatisticsDao>--<machinesOnSaleListDetail>--start");
	     StringBuffer sql = new StringBuffer();
	     ReturnDataUtil data = new ReturnDataUtil();
	     sql.append(" select code as vmCode,companyid,basicItemId,name as goodsName,sum(num) as num from( ");
	     sql.append(" SELECT vmi.code,vmi.companyId,vmi.machineVersion,vmii.basicItemId,ib.NAME,vmw.num FROM vending_machines_info vmi ");
	     sql.append(" LEFT JOIN vending_machines_way vmw ON vmw.vendingMachinesCode = vmi. CODE ");
	     sql.append(" LEFT JOIN vending_machines_item vmii ON vmw.itemId = vmii.id  ");
	     sql.append(" LEFT JOIN item_basic ib ON ib.id = vmii.basicItemId ");
	     sql.append(" LEFT JOIN vending_line vl ON vmi.lineId = vl.id ");
	     sql.append(" LEFT JOIN company c ON vmi.companyId = c.id ");
		 sql.append(" where vmw.state=30001 and vmi.machineVersion=1 ");
		 if(form.getBasicItemId()!=null){
			 sql.append(" and ib.id = '" + form.getBasicItemId() + "' ");
		 }
	     if(form.getCompanyId()!=null) {
		     sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
	     }
	     if(form.getAreaId()!=null) {
			 sql.append(" and vl.areaId = '" + form.getAreaId() + "' ");
	     }
		 
		 sql.append(" union all");
		 
		 sql.append(" SELECT vmi.code,vmi.companyId,vmi.machineVersion,vmwi.basicItemId,ib.NAME,vmwi.num FROM vending_machines_info vmi ");
		 sql.append(" LEFT JOIN vending_machines_way vmw ON vmw.vendingMachinesCode = vmi. CODE ");
		 sql.append(" LEFT JOIN vending_machines_way_item vmwi ON vmwi.machineWayId = vmw.id ");
		 sql.append(" LEFT JOIN item_basic ib ON ib.id = vmwi.basicItemId ");
		 sql.append(" LEFT JOIN vending_line vl ON vmi.lineId = vl.id ");
		 sql.append(" LEFT JOIN company c ON vmi.companyId = c.id ");
		 sql.append(" where vmw.state=30001 and vmi.machineVersion = 2 ");
		 if(form.getBasicItemId()!=null){
			 sql.append(" and ib.id = '" + form.getBasicItemId() + "' ");
		 }
	     if(form.getCompanyId()!=null) {
		     sql.append(" and vmi.companyId in ("+form.getCompanyIds()+")");
	     }
	     if(form.getAreaId()!=null) {
			 sql.append(" and vl.areaId = '" + form.getAreaId() + "' ");
	     }
		 sql.append(" ) A where num!=0 ");
	     if(StringUtils.isNotBlank(form.getVmCode())) {
			 sql.append(" and code like '%" + form.getVmCode() + "%' ");
	     }
	     sql.append(" group by vmCode  ");

	     sql.append(" order by num desc ");


	     log.info("查询语句：" + sql);
	     List<MachinesOnSaleDto> result = new ArrayList<MachinesOnSaleDto>();
	     Connection conn = null;
	     PreparedStatement pst = null;
	     ResultSet rs = null;
	     MachinesOnSaleDto machinesOnSaleDto = null;
	     try {
	         conn = openConnection();
	         pst = conn.prepareStatement("select count(*) from ( "+sql.toString()+" )A");
	         rs = pst.executeQuery();
	         long count = 0;
	         while (rs.next()) {
	        	 count=rs.getInt(1);
	         }
	         long off = (form.getCurrentPage() - 1) * form.getPageSize();
	         pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());
	         rs = pst.executeQuery();
	         while (rs != null && rs.next()) {
	        	 if(StringUtils.isNotBlank(rs.getString("goodsName"))) {
	        		 machinesOnSaleDto = new MachinesOnSaleDto();
	        		 machinesOnSaleDto.setGoodsName(rs.getString("goodsName"));
//	        		 if(rs.getInt("machineVersion") == 1) {
	        			 machinesOnSaleDto.setNum(rs.getInt("num"));
//	        		 }else {
//	        			 machinesOnSaleDto.setNum(rs.getInt("version2"));
//	        		 }
	        		 machinesOnSaleDto.setBasicItemId(rs.getInt("basicItemId"));
	        		 machinesOnSaleDto.setCompanyId(rs.getInt("companyId"));
	        		 machinesOnSaleDto.setVmCode(rs.getString("vmCode"));
	        		 result.add(machinesOnSaleDto);
	        	 }
	         }
	         data.setCurrentPage(form.getCurrentPage());
	         data.setTotal(count);
	         data.setReturnObject(result);
	         data.setStatus(1);
	         data.setMessage("查询成功");
	     } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e);
	     } finally {
	            this.closeConnection(rs, pst, conn);
	     }
	     log.info("<machinesOnSaleStatisticsDao>--<machinesOnSaleListDetail>--end");
	     return data;
	}
}
