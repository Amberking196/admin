package com.server.module.system.statisticsManage.machinesSaleStatistics;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.VmInfoStateEnum;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("machinesSaleStatisticsDao")
public class MachinesSaleStatisticsDaoImpl extends BaseDao implements MachinesSaleStatisticsDao {

    public static Logger log = LogManager.getLogger(MachinesSaleStatisticsDaoImpl.class);

    public List<PerMachinesSaleDto> findPerMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm) {
        log.info("<PayRecordDaoImpl>--<findPerMachinesSale>--start");
        StringBuffer sql = new StringBuffer();
        sql.append("select * from(select vmCode,sppd.companyId as companyId ,c.name as companyName, i.locatoinName as vendingMachinesAddress,");
        sql.append("SUM(machinesOrderNum) as sumFinishedOrderNum,");
        sql.append(" FORMAT(SUM(finishedMoney)/SUM(finishedOrderNum),2) as cusUnitPrice,");
        sql.append(" FORMAT(SUM(finishedCost)/SUM(finishedItemNum),2) as costUnitPrice,");
        sql.append(" SUM(finishedCost) as sumFinishedCost,");
        sql.append(" FORMAT(SUM(finishedMoney)/SUM(finishedItemNum) ,2) as saleUnitPrice,");
        sql.append(" SUM(finishedItemNum) as sumFinishedItemNum,SUM(finishedMoney) as sumFinishedMoney,");
        sql.append(" format(SUM(profit)/SUM(finishedItemNum),2) as profitUnitPrice,SUM(profit) as sumProfit ");
        sql.append(" from tbl_statistics_pay_per_day as sppd left join company as c on c.id=sppd.companyId left join vending_machines_info as i on sppd.vmCode= i.code   where 1=1");
		if(machinesStatisticsForm.getAreaId()!=null && machinesStatisticsForm.getAreaId()>0){
			sql.append(" and i.areaId = '" + machinesStatisticsForm.getAreaId() + "' ");
		}
        if (machinesStatisticsForm != null) {
            if (StringUtil.isNotBlank(machinesStatisticsForm.getVendingMachinesCode())) {
                sql.append(" and sppd.vmCode like '%" + machinesStatisticsForm.getVendingMachinesCode() + "%'");
            }
            if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
                sql.append(" and sppd.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
            }
            if (machinesStatisticsForm.getStartDate() != null) {
                sql.append(" and sppd.reportDate>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
            }
            if (machinesStatisticsForm.getEndDate() != null) {
                sql.append(" and sppd.reportDate<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
            }
            if (StringUtil.isNotEmpty(machinesStatisticsForm.getVendingMachinesAddress())) {
                sql.append(" and i.locatoinName like '%" + machinesStatisticsForm.getVendingMachinesAddress() + "%'");
            }
        }
        sql.append(" GROUP BY vmCode order by vmCode)A");
        sql.append(" left join");
        sql.append(" (select pr.vendingmachinescode,count(*) as sumRefundOrderNum,sum(rr.refundPrice) as sumRefundMoney,DATE_FORMAT(rr.createTime,'%Y-%m-%d') as refundDate ");
        sql.append(" from refund_record rr");
        sql.append(" inner join pay_record pr on pr.payCode=rr.payCode");
        sql.append(" inner join vending_machines_info vmi on vmi.code=pr.vendingmachinescode");
        sql.append(" where rr.state=1");
		if(machinesStatisticsForm.getAreaId()!=null && machinesStatisticsForm.getAreaId()>0){
			sql.append(" and vmi.areaId = '" + machinesStatisticsForm.getAreaId() + "' ");
		}
        if (machinesStatisticsForm.getStartDate() != null) {
            sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
        }
        if (machinesStatisticsForm.getEndDate() != null) {
            sql.append(" and pr.payTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
        }
        if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
            sql.append(" and vmi.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
        }
        sql.append(" group by pr.vendingmachinescode");
        sql.append(" )B on A.vmCode=B.vendingmachinescode");
        sql.append(" left join  (SELECT pr.vendingMachinesCode,sum(pri.price) as basicPrice,SUM(pri.realTotalPrice) FROM pay_record pr left join pay_record_item pri on pri.payrecordid=pr.id");
        sql.append(" where 1=1");
        if (machinesStatisticsForm.getStartDate() != null) {
            sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
        }
        if (machinesStatisticsForm.getEndDate() != null) {
            sql.append(" and pr.payTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
        }		

        sql.append(" GROUP by vendingMachinesCode)C on A.vmCode=C.vendingMachinesCode");
        
        if (machinesStatisticsForm.getIsShowAll() == 0) {
            sql.append(" limit " + (machinesStatisticsForm.getCurrentPage() - 1) * machinesStatisticsForm.getPageSize() + "," + machinesStatisticsForm.getPageSize());
        }
        log.info("查询语句：" + sql);
        List<PerMachinesSaleDto> result = new ArrayList<PerMachinesSaleDto>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PerMachinesSaleDto machinesSaleDto = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                machinesSaleDto = new PerMachinesSaleDto();
                //machinesSaleDto.setCostUnitPrice(rs.getString("costUnitPrice"));
                machinesSaleDto.setCusUnitPrice(rs.getString("cusUnitPrice"));
                machinesSaleDto.setSaleUnitPrice(rs.getString("saleUnitPrice"));
                //machinesSaleDto.setSumProfit(rs.getString("sumProfit"));
                machinesSaleDto.setVmCode(rs.getString("vmCode"));
                machinesSaleDto.setCompanyName(rs.getString("companyName"));
                machinesSaleDto.setProfitUnitPrice(rs.getString("profitUnitPrice"));
                //machinesSaleDto.setSumFinishedCost(rs.getDouble("sumFinishedCost"));
                machinesSaleDto.setSumFinishedItemNum(rs.getDouble("sumFinishedItemNum"));
                machinesSaleDto.setSumFinishedMoney(rs.getDouble("sumFinishedMoney"));
                machinesSaleDto.setSumFinishedOrderNum(rs.getInt("sumFinishedOrderNum"));
                //machinesSaleDto.setSumRefundItemNum(rs.getInt("sumRefundItemNum"));
                machinesSaleDto.setSumRefundOrderNum(rs.getInt("sumRefundOrderNum"));
                machinesSaleDto.setSumRefundMoney(rs.getDouble("sumRefundMoney"));
                machinesSaleDto.setStartDate(machinesStatisticsForm.getStartDate() != null ? machinesStatisticsForm.getStartDate() : null);
                machinesSaleDto.setEndDate(machinesStatisticsForm.getEndDate() != null ? machinesStatisticsForm.getEndDate() : null);
                machinesSaleDto.setVendingMachinesAddress(rs.getString("vendingMachinesAddress"));
                machinesSaleDto.setBasicPrice(rs.getDouble("basicPrice"));
                if(machinesSaleDto.getBasicPrice()==0) {
                    machinesSaleDto.setBasicPrice(0d);
                }
                else {
                    machinesSaleDto.setFreePrice(BigDecimal.valueOf(machinesSaleDto.getBasicPrice()).subtract(BigDecimal.valueOf(machinesSaleDto.getSumFinishedMoney())).doubleValue()); 
                }
                result.add(machinesSaleDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<PayRecordDaoImpl>--<findPerMachinesSale>--end");
        return result;
    }

    public List<PerMachinesSaleDto> findPerMachinesSaleDetail(MachinesSaleStatisticsForm machinesStatisticsForm) {
        log.info("<PayRecordDaoImpl>--<findPerMachinesSale>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ib.name,sppd.vmCode,sum(finishedMoney) as sumFinishedMoney,sum(finishedItemNum) as sumFinishedItemNum,FORMAT(SUM(finishedMoney)/SUM(finishedItemNum) ,2) as saleUnitPrice");
        
        sql.append(" from tbl_statistics_pay_per_day as sppd"
        		+ "  left join company as c on c.id=sppd.companyId "
        		+ "  left join vending_machines_info as i on sppd.vmCode= i.code   "
        		+ "  left join item_basic ib on ib.id = sppd.basicItemId where 1=1 ");
		if(machinesStatisticsForm.getAreaId()!=null && machinesStatisticsForm.getAreaId()>0){
			sql.append(" and i.areaId = '" + machinesStatisticsForm.getAreaId() + "' ");
		}
        if (machinesStatisticsForm != null) {
            if (StringUtil.isNotBlank(machinesStatisticsForm.getVendingMachinesCode())) {
                sql.append(" and sppd.vmCode like '%" + machinesStatisticsForm.getVendingMachinesCode() + "%'");
            }
            if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
                sql.append(" and sppd.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
            }
            if (machinesStatisticsForm.getStartDate() != null) {
                sql.append(" and sppd.reportDate>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
            }
            if (machinesStatisticsForm.getEndDate() != null) {
                sql.append(" and sppd.reportDate<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
            }
            if (StringUtil.isNotEmpty(machinesStatisticsForm.getVendingMachinesAddress())) {
                sql.append(" and i.locatoinName like '%" + machinesStatisticsForm.getVendingMachinesAddress() + "%'");
            }
        }
        sql.append(" and i.state="+VmInfoStateEnum.MACHINES_NORMAL.getCode());
        sql.append(" GROUP BY vmCode,basicItemId order by vmCode");

        log.info("查询语句：" + sql);
        List<PerMachinesSaleDto> result = new ArrayList<PerMachinesSaleDto>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PerMachinesSaleDto machinesSaleDto = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                machinesSaleDto = new PerMachinesSaleDto();
                machinesSaleDto.setVmCode(rs.getString("vmCode"));
                machinesSaleDto.setSumFinishedItemNum(rs.getDouble("sumFinishedItemNum"));
                machinesSaleDto.setSumFinishedMoney(rs.getDouble("sumFinishedMoney"));
                machinesSaleDto.setSaleUnitPrice(rs.getString("saleUnitPrice"));
                machinesSaleDto.setName(rs.getString("name"));
                machinesSaleDto.setStartDate(machinesStatisticsForm.getStartDate() != null ? machinesStatisticsForm.getStartDate() : null);
                machinesSaleDto.setEndDate(machinesStatisticsForm.getEndDate() != null ? machinesStatisticsForm.getEndDate() : null);

                result.add(machinesSaleDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<PayRecordDaoImpl>--<findPerMachinesSale>--end");
        return result;
    }
    
    public Long findMachinesSaleNumRecord(MachinesSaleStatisticsForm machinesStatisticsForm) {
        log.info("<PayRecordDaoImpl>--<findMachinesSaleNumRecord>--start");
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) as total from (select 1 from");
        sql.append(" tbl_statistics_pay_per_day as sppd left join vending_machines_info as i on sppd.vmCode= i.code where 1=1 ");
		if(machinesStatisticsForm.getAreaId()!=null && machinesStatisticsForm.getAreaId()>0){
			sql.append("and i.areaId = '" + machinesStatisticsForm.getAreaId() + "' ");
		}
        if (StringUtil.isNotBlank(machinesStatisticsForm.getVendingMachinesCode())) {
            sql.append(" and sppd.vmCode like '%" + machinesStatisticsForm.getVendingMachinesCode() + "%'");
        }
        if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
            sql.append(" and sppd.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
        }
        if (machinesStatisticsForm.getStartDate() != null) {
            sql.append(" and sppd.reportDate>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
        }
        if (machinesStatisticsForm.getEndDate() != null) {
            sql.append(" and sppd.reportDate<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
        }
        if (StringUtil.isNotEmpty(machinesStatisticsForm.getVendingMachinesAddress())) {
            sql.append(" and i.locatoinName like '%" + machinesStatisticsForm.getVendingMachinesAddress() + "%'");
        }
        sql.append(" group by vmCode) t");
        log.info("查询语句：" + sql);
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
        log.info("<PayRecordDaoImpl>--<findMachinesSaleNumRecord>--end");
        return total;
    }


    @Override
    public ReturnDataUtil findPerMachinesCustomerConsume(MachinesSaleStatisticsForm machinesStatisticsForm) {
        log.info("<PayRecordDaoImpl>--<findPerMachinesCustomerConsume>--start");
        StringBuffer sql = new StringBuffer();
        ReturnDataUtil data = new ReturnDataUtil();
        sql.append(" select tc.userName,pr.price/pr.num as price,pr.num,pr.price money,pr.basicItemId,pr.vendingMachinesCode,vmi.companyId,c.name as companyName,pr.payTime,pr.itemName, "
        		+ "  pri.basicItemId as basicItemId2,pri.itemId as itemId2,pri.price/pri.num as price2,pri.num as num2,pri.itemName as itemName2,pri.itemType as itemTypeId2,pri.realTotalPrice as totalPrice2,pri.costPrice as costPrice2 "
        		+ "  from pay_record as pr " );    
        sql.append(" left join tbl_customer tc on tc.id=pr.customerId");
        sql.append(" left join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code");
        sql.append(" left join pay_record_item pri on pri.payRecordId = pr.id ");
        sql.append(" left join company c on c.id=vmi.companyId");

        sql.append(" where  1=1 ");
        if (machinesStatisticsForm != null) {
            if (StringUtil.isNotBlank(machinesStatisticsForm.getVendingMachinesCode())) {
                sql.append(" and pr.vendingMachinesCode like '%" + machinesStatisticsForm.getVendingMachinesCode() + "%'");
            }
            if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
                sql.append(" and vmi.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
            }
            if (machinesStatisticsForm.getStartDate() != null) {
                sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
            }
            if (machinesStatisticsForm.getEndDate() != null) {
                sql.append(" and pr.payTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
            }
        }
        sql.append("  order by pr.payTime desc");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql>>>:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
            	count=rs.getInt(1);
            }
            long off = (machinesStatisticsForm.getCurrentPage() - 1) * machinesStatisticsForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + machinesStatisticsForm.getPageSize());
            rs = pst.executeQuery();
            List<PerMachinesCustomerConsumeDto> list = Lists.newArrayList();
            while (rs.next()) {
                PerMachinesCustomerConsumeDto pccd = new PerMachinesCustomerConsumeDto();
                pccd = new PerMachinesCustomerConsumeDto();
                pccd.setUserName(rs.getString("userName"));
                pccd.setMoney(rs.getBigDecimal("money"));
                pccd.setPrice(rs.getBigDecimal("price"));
                pccd.setNum(rs.getInt("num"));
                pccd.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                pccd.setPayTime(rs.getTimestamp("payTime"));
                pccd.setItemName(rs.getString("itemName"));
                pccd.setCompanyName(rs.getString("companyName"));
                
                
				if(rs.getLong("basicItemId")==0 ) {
					pccd.setItemName(rs.getString("itemName2"));
					pccd.setPrice(rs.getBigDecimal("Price2"));
					pccd.setNum(rs.getInt("num2"));
					pccd.setMoney(rs.getBigDecimal("totalPrice2"));
				}
                list.add(pccd);
            }
            data.setCurrentPage(machinesStatisticsForm.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");
            log.info("<MachineCustomerDaoImpl--findPerMachinesCustomerConsume--end>");
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return data;
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public SumMachinesSaleDto findSumMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm) {
        log.info("<PayRecordDaoImpl>--<findSumMachinesSale>--start");
        StringBuffer sql = new StringBuffer();
        ReturnDataUtil data = new ReturnDataUtil();
        sql.append("select  sum(finishedItemNum) as sumMachinesSaleNum,round((sum(finishedItemNum)/COUNT(DISTINCT vmCode))) as avgPerMachinesSaleNum,round((sum(finishedMoney)/COUNT(DISTINCT vmCode)),2) as avgPerMachinesSalePrice FROM tbl_statistics_pay_per_day t");
        sql.append(" where 1 = 1 ");
        if (machinesStatisticsForm != null) {
            if (StringUtil.isNotBlank(machinesStatisticsForm.getVendingMachinesCode())) {
                sql.append(" and t.vmCode like '%" + machinesStatisticsForm.getVendingMachinesCode() + "%'");
            }
            if (StringUtils.isNotBlank(machinesStatisticsForm.getCompanyIds())) {
                sql.append(" and t.companyId in (" + machinesStatisticsForm.getCompanyIds() + ")");
            }
            if (machinesStatisticsForm.getStartDate() != null) {
                sql.append(" and t.reportDate>='" + DateUtil.formatYYYYMMDDHHMMSS(machinesStatisticsForm.getStartDate()) + "'");
            }
            if (machinesStatisticsForm.getEndDate() != null) {
                sql.append(" and t.reportDate<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machinesStatisticsForm.getEndDate(), 1) + "'");
            }
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql>>>:" + sql.toString());
        SumMachinesSaleDto sumMachinesSaleDto = new SumMachinesSaleDto();
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs != null && rs.next()) {
                sumMachinesSaleDto.setAvgPerMachinesSalePrice(rs.getDouble("avgPerMachinesSalePrice"));
                sumMachinesSaleDto.setAvgPerMachinesSaleNum(rs.getInt("avgPerMachinesSaleNum"));
                sumMachinesSaleDto.setSumMachinesSaleNum(rs.getInt("sumMachinesSaleNum"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, pst, conn);
        }
        log.info("<MachineCustomerDaoImpl--findSumMachinesSale--end>");
        return sumMachinesSaleDto;
    }

}
