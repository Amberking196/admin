package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;
import com.server.module.commonBean.IdNameBean;
import com.server.util.DateUtil;
import com.server.util.StringUtil;
@Repository
public class ItemSaleStatisticsDaoImpl extends BaseDao implements ItemSaleStatisticsDao {

	public static Logger log = LogManager.getLogger(ItemSaleStatisticsDaoImpl.class);
	
	public List<ItemSaleStatisticsDto> findItemSaleStatistics(ItemSaleStatisticsForm itemSaleStatisticsForm){
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSaleStatistics>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.basicItemId,c.barCode,c.name as itemName,qq.vmNum as machinesNum,");
		sql.append(" c.standard,d.name as unit,c.typeId AS itemTypeId,");
		sql.append(" sum(finishedMoney) AS sumFinishedMoney,SUM(finishedItemNum) AS sumFinishedItemNum,");
		sql.append(" SUM(finishedOrderNum) AS sumFinishedOrderNum,SUM(profit) AS sumProfit,");
		sql.append(" SUM(finishedCost) AS sumFinishedCost,");
		sql.append(" FORMAT(SUM(finishedMoney) / SUM(finishedItemNum),2) AS unitPrice,");
		sql.append(" FORMAT(SUM(profit) / SUM(finishedItemNum), 2) AS unitProfit,");
		sql.append(" FORMAT(SUM(finishedCost) / SUM(finishedItemNum),2) AS costUnitPrice");
		sql.append(" from tbl_statistics_pay_per_day a  left join company b on a.companyId=b.id");
		
		sql.append(" left join item_basic c on a.basicItemId=c.id ");
		sql.append(" LEFT JOIN state_info d ON c.unit=d.id ");
		sql.append(" LEFT JOIN ");
		sql.append(" (SELECT SUM(t.itemOnVmNum) AS vmNum,basicItemId FROM (");
		sql.append(" SELECT tsis.itemOnVmNum,tsis.`basicItemId`,tsis.`companyId`");
		sql.append(" FROM tbl_statistics_item_sale AS tsis,");
		sql.append(" (SELECT MAX(createTime) AS createTime,basicItemId,companyId ");
		sql.append(" FROM `tbl_statistics_item_sale` AS tt GROUP BY tt.basicItemId,tt.companyId ) AS mt");
		sql.append(" WHERE tsis.createTime = mt.createTime");
		sql.append(" AND tsis.basicItemId = mt.basicItemId");
		sql.append(" AND tsis.companyId = mt.companyId");
		/*if(itemSaleStatisticsForm.getCompanyId()!=null){
			sql.append(" and tsis.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
			sql.append(" and tsis.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}
		sql.append(" and reportDate >= '"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'" );
		sql.append(" )t  GROUP BY t.basicItemId) AS qq ON qq.basicItemId = a.`basicItemId` ");
		sql.append(" where 1=1");
		if(itemSaleStatisticsForm!=null){
			/*if(itemSaleStatisticsForm.getCompanyId()!=null){
				sql.append(" and a.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
			}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
				sql.append(" and a.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
			}
			if(itemSaleStatisticsForm.getItemId()!=null){
				sql.append(" and a.basicItemId = "+itemSaleStatisticsForm.getItemId());
			}
			if(itemSaleStatisticsForm.getStartDate()!=null){
				sql.append(" and a.reportDate >='"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'");
			}
			if(itemSaleStatisticsForm.getEndDate()!=null){
				sql.append(" and a.reportDate <'"+DateUtil.formatLocalYYYYMMDDHHMMSS(itemSaleStatisticsForm.getEndDate(),1)+"'");
			}
			sql.append(" group by a.basicItemId");
			if(StringUtils.isNotBlank(itemSaleStatisticsForm.getOrderByType())){
				sql.append(" order by "+itemSaleStatisticsForm.getOrderByType());
			}
			if(StringUtils.isNotBlank(itemSaleStatisticsForm.getSortType())){
				sql.append(" "+itemSaleStatisticsForm.getSortType());
			}
		}
		if(itemSaleStatisticsForm.getIsShowAll()==0){
			sql.append(" limit " + (itemSaleStatisticsForm.getCurrentPage()-1)*itemSaleStatisticsForm.getPageSize() + "," + itemSaleStatisticsForm.getPageSize());
		}
		log.info("商品销售统计查询语句："+sql);
		List<ItemSaleStatisticsDto> result = new ArrayList<ItemSaleStatisticsDto>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ItemSaleStatisticsDto itemSaleDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				itemSaleDto = new ItemSaleStatisticsDto();
				itemSaleDto.setBarCode(rs.getString("barCode"));
				itemSaleDto.setMachinesNum(rs.getString("machinesNum"));
				itemSaleDto.setBasicItemId(rs.getLong("basicItemId"));
				itemSaleDto.setItemName(rs.getString("itemName"));
				itemSaleDto.setStandard(rs.getString("standard"));
				itemSaleDto.setUnit(rs.getString("unit"));
				itemSaleDto.setItemTypeId(rs.getInt("itemTypeId"));
				itemSaleDto.setSumFinishedCost(rs.getDouble("sumFinishedCost"));
				itemSaleDto.setSumFinishedItemNum(rs.getInt("sumFinishedItemNum"));
				itemSaleDto.setSumFinishedMoney(rs.getDouble("sumFinishedMoney"));
				itemSaleDto.setSumFinishedOrderNum(rs.getInt("sumFinishedOrderNum"));
				itemSaleDto.setSumProfit(rs.getDouble("sumProfit"));
				itemSaleDto.setUnitPrice(rs.getDouble("unitPrice"));
				itemSaleDto.setUnitProfit(rs.getDouble("unitProfit"));
				itemSaleDto.setCostUnitPrice(rs.getDouble("costUnitPrice"));
				result.add(itemSaleDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSaleStatistics>--end");
		return result;
	}
 
	public List<ItemSaleStatisticsDto> findItemSaleStatisticsAfterRefund(ItemSaleStatisticsForm itemSaleStatisticsForm){
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSaleStatistics>--start");
		StringBuffer sql = new StringBuffer();
//		sql.append(" select a.basicItemId,c.barCode,c.name as itemName,qq.vmNum as machinesNum,");
//		sql.append(" c.standard,d.name as unit,c.typeId AS itemTypeId,");
//		sql.append(" sum(finishedMoney) AS sumFinishedMoney,SUM(finishedItemNum) AS sumFinishedItemNum,");
//		sql.append(" SUM(finishedOrderNum) AS sumFinishedOrderNum,SUM(profit) AS sumProfit,");
//		sql.append(" SUM(finishedCost) AS sumFinishedCost,");
//		sql.append(" FORMAT(SUM(finishedMoney) / SUM(finishedItemNum),2) AS unitPrice,");
//		sql.append(" FORMAT(SUM(profit) / SUM(finishedItemNum), 2) AS unitProfit,");
//		sql.append(" FORMAT(SUM(finishedCost) / SUM(finishedItemNum),2) AS costUnitPrice");
//		sql.append(" from tbl_statistics_pay_per_day a  left join company b on a.companyId=b.id");
		
		sql.append(" SELECT a.basicItemId,c.barCode,a.itemName,sum(a.num) AS sumFinishedItemNum ,COUNT(a.basicItemId) AS sumFinishedOrderNum,sum(a.realTotalPrice) AS sumFinishedMoney ");
		sql.append("  ,qq.vmNum as machinesNum,c.standard,d.name as unit,c.typeId AS itemTypeId,FORMAT(SUM(finalTotalPrice) / SUM(finalNum),2) AS unitPrice FROM pay_record_item a ");
		sql.append(" left join pay_Record pr on a.payRecordId=pr.id ");
		sql.append(" left join vending_machines_info vmi on vmi.code=pr.vendingMachinesCode ");
		sql.append(" left join company b on vmi.companyId=b.id ");

		sql.append(" left join item_basic c on a.basicItemId=c.id ");
		sql.append(" LEFT JOIN state_info d ON c.unit=d.id ");
		sql.append(" LEFT JOIN ");
		sql.append(" (SELECT SUM(t.itemOnVmNum) AS vmNum,basicItemId FROM (");
		sql.append(" SELECT tsis.itemOnVmNum,tsis.`basicItemId`,tsis.`companyId`");
		sql.append(" FROM tbl_statistics_item_sale AS tsis,");
		sql.append(" (SELECT MAX(createTime) AS createTime,basicItemId,companyId ");
		sql.append(" FROM `tbl_statistics_item_sale` AS tt GROUP BY tt.basicItemId,tt.companyId ) AS mt");
		sql.append(" WHERE tsis.createTime = mt.createTime");
		sql.append(" AND tsis.basicItemId = mt.basicItemId");
		sql.append(" AND tsis.companyId = mt.companyId");
		/*if(itemSaleStatisticsForm.getCompanyId()!=null){
			sql.append(" and tsis.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
			sql.append(" and tsis.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}
		sql.append(" and reportDate >= '"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'" );
		sql.append(" )t  GROUP BY t.basicItemId) AS qq ON qq.basicItemId = a.`basicItemId` ");
		sql.append(" where 1=1 and (pr.state = 10001 or pr.state = 10003)");
		
		if(itemSaleStatisticsForm!=null){
			if (itemSaleStatisticsForm.getAreaId() != null && itemSaleStatisticsForm.getAreaId()>0) {
				sql.append("and vmi.areaId = '" + itemSaleStatisticsForm.getAreaId() + "' ");
			}
			/*if(itemSaleStatisticsForm.getCompanyId()!=null){
				sql.append(" and a.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
			}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
				sql.append(" and vmi.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
			}
			if(itemSaleStatisticsForm.getItemId()!=null){
				sql.append(" and a.basicItemId = "+itemSaleStatisticsForm.getItemId());
			}
			if(itemSaleStatisticsForm.getStartDate()!=null){
				sql.append(" and pr.payTime >='"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'");
			}
			if(itemSaleStatisticsForm.getEndDate()!=null){
				sql.append(" and pr.payTime <'"+DateUtil.formatLocalYYYYMMDDHHMMSS(itemSaleStatisticsForm.getEndDate(),1)+"'");
			}
			sql.append(" group by a.basicItemId");
			if(StringUtils.isNotBlank(itemSaleStatisticsForm.getOrderByType())){
				sql.append(" order by "+itemSaleStatisticsForm.getOrderByType());
			}
			if(StringUtils.isNotBlank(itemSaleStatisticsForm.getSortType())){
				sql.append(" "+itemSaleStatisticsForm.getSortType());
			}
		}
		if(itemSaleStatisticsForm.getIsShowAll()==0){
			sql.append(" limit " + (itemSaleStatisticsForm.getCurrentPage()-1)*itemSaleStatisticsForm.getPageSize() + "," + itemSaleStatisticsForm.getPageSize());
		}
		log.info("商品销售统计查询语句："+sql);
		List<ItemSaleStatisticsDto> result = new ArrayList<ItemSaleStatisticsDto>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ItemSaleStatisticsDto itemSaleDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				itemSaleDto = new ItemSaleStatisticsDto();
				itemSaleDto.setBarCode(rs.getString("barCode"));
				itemSaleDto.setMachinesNum(rs.getString("machinesNum"));
				itemSaleDto.setBasicItemId(rs.getLong("basicItemId"));
				itemSaleDto.setItemName(rs.getString("itemName"));
				itemSaleDto.setStandard(rs.getString("standard"));
				itemSaleDto.setUnit(rs.getString("unit"));
				itemSaleDto.setItemTypeId(rs.getInt("itemTypeId"));
				//itemSaleDto.setSumFinishedCost(rs.getDouble("sumFinishedCost"));
				itemSaleDto.setSumFinishedItemNum(rs.getInt("sumFinishedItemNum"));
				itemSaleDto.setSumFinishedMoney(rs.getDouble("sumFinishedMoney"));
				itemSaleDto.setSumFinishedOrderNum(rs.getInt("sumFinishedOrderNum"));
				//itemSaleDto.setSumProfit(rs.getDouble("sumProfit"));
				itemSaleDto.setUnitPrice(rs.getDouble("unitPrice"));
				//itemSaleDto.setUnitProfit(rs.getDouble("unitProfit"));
				//itemSaleDto.setCostUnitPrice(rs.getDouble("costUnitPrice"));
				result.add(itemSaleDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSaleStatistics>--end");
		return result;
	}


	@Override
	public Long findItemSalesStatisticsNum(ItemSaleStatisticsForm itemSaleStatisticsForm) {
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSalesStatisticsNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as total from (");
		sql.append(" select 1 from tbl_statistics_pay_per_day a  left join company b on a.companyId=b.id");
		sql.append(" left join item_basic c on a.basicItemId=c.id ");
		sql.append(" left join state_info d on c.unit=d.id  ");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=a.`vmCode` where 1=1 ");

		/*if(itemSaleStatisticsForm.getCompanyId()!=null){
			sql.append(" and companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
			sql.append(" and a.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}
		if(itemSaleStatisticsForm.getItemId()!=null){
			sql.append(" and a.basicItemId = "+itemSaleStatisticsForm.getItemId());
		}
		if(itemSaleStatisticsForm.getStartDate()!=null){
			sql.append(" and a.reportDate >='"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'");
		}
		if(itemSaleStatisticsForm.getEndDate()!=null){
			sql.append(" and a.reportDate <'"+DateUtil.formatLocalYYYYMMDDHHMMSS(itemSaleStatisticsForm.getEndDate(),1)+"'");
		}
		if (itemSaleStatisticsForm.getAreaId() != null && itemSaleStatisticsForm.getAreaId()>0) {
			sql.append("and vmi.areaId = '" + itemSaleStatisticsForm.getAreaId() + "' ");
		}
		sql.append(" group by a.basicItemId) t");
		log.info("查询语句："+sql);
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
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSalesStatisticsNum>--end");
		return total;
	}
	
	@Override
	public Long findItemSalesStatisticsNumAfterRefund(ItemSaleStatisticsForm itemSaleStatisticsForm) {
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSalesStatisticsNumAfterRefund>--start");
		StringBuffer sql = new StringBuffer();
//		sql.append("select count(1) as total from (");
//		sql.append(" select 1 from tbl_statistics_pay_per_day a  left join company b on a.companyId=b.id");
//		sql.append(" left join item_basic c on a.basicItemId=c.id ");
//		sql.append(" left join state_info d on c.unit=d.id where 1=1 ");
		
		sql.append("select count(1) as total from (");
		sql.append(" SELECT  1  FROM pay_record_item a ");
		sql.append(" left join pay_Record pr on a.payRecordId=pr.id ");
		sql.append(" left join vending_machines_info vmi on vmi.code=pr.vendingMachinesCode ");
		sql.append(" left join company b on vmi.companyId=b.id ");

		sql.append(" left join item_basic c on a.basicItemId=c.id ");
	
		/*if(itemSaleStatisticsForm.getCompanyId()!=null){
			sql.append(" and companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}else*/ if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}
		if(itemSaleStatisticsForm.getItemId()!=null){
			sql.append(" and a.basicItemId = "+itemSaleStatisticsForm.getItemId());
		}
		if(itemSaleStatisticsForm.getStartDate()!=null){
			sql.append(" and pr.payTime >='"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'");
		}
		if(itemSaleStatisticsForm.getEndDate()!=null){
			sql.append(" and pr.payTime <'"+DateUtil.formatLocalYYYYMMDDHHMMSS(itemSaleStatisticsForm.getEndDate(),1)+"'");
		}
		sql.append(" group by a.basicItemId) t");
		log.info("查询语句："+sql);
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
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemSalesStatisticsNumAfterRefund>--end");
		return total;
	}
	
	public List<IdNameBean> findItemInfo(){
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemInfo>--start");
		String sql = "select id,name,companyId from item_basic";
		log.info("sql语句："+sql);
		List<IdNameBean> itemList = new ArrayList<IdNameBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		IdNameBean bean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				bean = new IdNameBean();
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				itemList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemSaleStatisticsDaoImpl>--<findItemInfo>--end");
		return itemList;
	}
	
	public List<IdNameBean> findItemInfoByCompanyId(ItemSaleStatisticsForm itemSaleStatisticsForm){
		log.info("<ItemSaleStatisticsDaoImpl>----<findItemInfoByCompanyId>----start");
		StringBuffer sql=new StringBuffer();
		sql.append(" select ib.id,ib.name from tbl_statistics_pay_per_day  t inner join item_basic ib on t.basicItemId=ib.id where 1=1 ");
		if(itemSaleStatisticsForm.getStartDate()!=null){
			sql.append(" and t.reportDate >='"+DateUtil.formatYYYYMMDDHHMMSS(itemSaleStatisticsForm.getStartDate())+"'");
		}
		if(itemSaleStatisticsForm.getEndDate()!=null){
			sql.append(" and t.reportDate <'"+DateUtil.formatLocalYYYYMMDDHHMMSS(itemSaleStatisticsForm.getEndDate(),1)+"'");
		}
		if(StringUtil.isNotBlank(itemSaleStatisticsForm.getCompanyIds())){
			sql.append(" and t.companyId in ("+itemSaleStatisticsForm.getCompanyIds()+")");
		}
		sql.append(" group by t.basicItemId  ");
		List<IdNameBean> itemList = new ArrayList<IdNameBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		IdNameBean bean = null;
		log.info("根据公司id 查询在售商品信息sql语句:"+sql.toString());
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				bean = new IdNameBean();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				itemList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ItemSaleStatisticsDaoImpl>----<findItemInfoByCompanyId>----end");
		return itemList;
	}
}
